package affichage;

import bean.AnalyseLien;
import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ResultatEtSomme;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import user.UserEJB;
import utilitaire.Utilitaire;

/**
 *
 * Objet à utiliser à l'affichage pour génerer la page recherche groupe d'un objet de mapping
 * 
 * <p>
 * Ci dessous un exemple d'utilisation en jsp:
 * </p>
 * <pre>
 *  Voiture voiture = new Voiture();
 *  voiture.setNomTable("VOITURE");
 *  String listeCrt[]={"id_type_vehicule","id_marque","carburant","etat"};
 *  String listeInt[]={"carburant"};
 *  String pourcentage[]={""};
 *  String colDefaut[] = {"id_type_vehicule","id_marque"}; 
 *  String somDefaut[]={"carburant"};
 *  PageRechercheGroupe pg = new PageRechercheGroupe(voiture, request, listeCrt, listeInt,2, colDefaut,somDefaut,pourcentage,2,1);
 *  pg.creerObjetPagePourc();   
 * </pre>
 * <p>
 *  Sur la partie du resultat, pareil comme sur les pages de recherches. Pour le recap:
 * </p>
 * <pre>
 *  <%
        out.println(pr.getTableauRecap().getHtml());%>
 * </pre>
 * <p>
 *  Pour le tableau
 * </p>
 * <pre>
 * <%
        out.println(pr.getTableau().getHtml());
    %>
 * </pre>
 * @author BICI
 * @version 1.0
 */
public class PageRechercheGroupe extends Page {

    private TableauRecherche listeTabDet;
    private bean.ClassMAPTable[] liste;
    private bean.ClassMAPTable critere;
    private String[] colInt;
    private String[] valInt;
    private bean.ResultatEtSomme rs;
    private int numPage = 1;
    private String[] colSomme;
    private String apres;
    private String aWhere = "";
    private String apresLienPage = "";
    private String[] colGroupe; // Tableau de chaine representant les valeurs des colonnes selectionnees.
    private String[] sommeGroupe;
    private String ordre = "";
    private int nbColGroupe = 2;
    private int nbSommeGrp = 2;
    private String[] colGroupeDefaut;
    private String[] sommeGroupeDefaut;
    private int npp = 0;
    private String[] listeColonneMoyenne;
    private String[] pourcentage;
    private double[][] pourc;
    private boolean premier = false;
    private Liste count;
    private PageInsert insertLienAnalyse;
    private String[] ligneGroupe; 
    
    private ClassMAPTable[] dataM1;

    public ClassMAPTable[] getDataM1() {
        return dataM1;
    }

    public void setDataM1(ClassMAPTable[] dataM1) {
        this.dataM1 = dataM1;
    }

    public PageInsert getInsertLienAnalyse() {
        return insertLienAnalyse;
    }

    public void setInsertLienAnalyse(PageInsert insertLienAnalyse) {
        this.insertLienAnalyse = insertLienAnalyse;
    }

    /**
     * Prendre les données à afficher de la page à la base de données
     * Selon la valeur de recap, on va prendre ou pas le recap
     * @param c connexion ouverte à la base données
     * @throws Exception
     */

    public void preparerDataList(Connection c) throws Exception {
        critere.setNomTable(getBase().getNomTable());
        makeCritere();
        if (premier == true) {
            rs = new bean.ResultatEtSomme();
            rs.initialise(getColSomme());
        } 
        else {
            rs = getUtilisateur().getDataPageGroupe(critere, getColGroupe(), getSommeGroupe(), getColInt(), getValInt(), getNumPage(), getAWhere(), getColSomme(), getOrdre(), c, npp);
        }
        bean.ClassMAPTable[] listeVal = (bean.ClassMAPTable[]) rs.getResultat();
        setListe(listeVal);
    }

    public void calculPourcentage(bean.ResultatEtSomme rs, bean.ClassMAPTable[] listeVal, Connection c) throws Exception {
        bean.ClassMAPTable[] liste = (bean.ClassMAPTable[]) CGenUtil.rechercher(getBase(), null, null, c, "");
        pourc = new double[getPourcentage().length][listeVal.length];
        for (int iCol = 0; iCol < getPourcentage().length; iCol++) {
            for (int iLigne = 0; iLigne < listeVal.length; iLigne++) {
                double valeur = Double.parseDouble(CGenUtil.getValeurFieldByMethod(listeVal[iLigne], getPourcentage()[iCol]).toString());
                if (getSommeGroupe().length != 0 && getPourcentage()[iCol].compareToIgnoreCase("nombrepargroupe") != 0) {
                    int k = 0;
                    for (k = 0; k < getSommeGroupe().length; k++) {
                        if (getSommeGroupe()[k].compareTo(getPourcentage()[iCol]) == 0) {
                            break;
                        }
                    }
                    pourc[iCol][iLigne] = (valeur * 100 / rs.getSommeEtNombre()[k]);
                } else {
                    pourc[iCol][iLigne] = (valeur * 100 / liste.length);
                }
            }
        }
    }
    /**
     * Sert à inserer des données dans les selects et les auto complete
     * @throws Exception
     */
    public void preparerDataFormu() throws Exception {
        Connection c = null;
        try {
            c = new utilitaire.UtilDB().GetConn();
            formu.getAllData(c);
        } catch (Exception ex) {
            throw (ex);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * preparer les données du formulaires(select/autocomplete) 
     * et prendre le resultat de recherche en même temps
     * @throws Exception
     */
    public void preparerData() throws Exception {
        Connection c = null;
        try {
            c = new utilitaire.UtilDB().GetConn();
            preparerDataList(c);
            formu.getAllData(c);
            getReq().getSession().setAttribute("critere", getCritere());
        } catch (Exception ex) {
            throw (ex);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * génerer la partie de la requête where et order by de recherche à partir du contexte HTTP 
     * et des valeurs de l'objet de base
     * @throws Exception
     */
    public void makeCritere() throws Exception {
        Vector colIntv = new Vector();
        Vector valIntv = new Vector();
        Champ[] tempChamp = formu.getCrtFormu();
        for (int i = 0; i < tempChamp.length; i++) {
            Field f = bean.CGenUtil.getField(getBase(), tempChamp[i].getNom());
            if (tempChamp[i].getEstIntervalle() == 1) {
                colIntv.add(tempChamp[i].getNom());
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "1"));
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "2"));
            } else if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                bean.CGenUtil.setValChamp(getCritere(), f, getParamSansNull(tempChamp[i].getNom()));
            } else {
                //aWhere+=" and "+f.getName() +" like '"+ Utilitaire.getValeurNonNull(formu.getListeChamp()[i].getValeur()) +"'";
                aWhere += " and " + f.getName() + " like '" + Utilitaire.getValeurNonNull(getParamSansNull(tempChamp[i].getNom())) + "'";
            }
        }
        //System.out.println("AWHERE = "+aWhere);
        colInt = new String[colIntv.size()];
        colIntv.copyInto(colInt);
        valInt = new String[valIntv.size()];
        valIntv.copyInto(valInt);
        if (getReq().getParameter("triCol") != null) {
            formu.getChamp("colonne").setValeur(getReq().getParameter("newcol"));
            if (getReq().getParameter("ordre").compareToIgnoreCase("") == 0) {
                formu.getChamp("ordre").setValeur("asc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " asc ");
            } else if (getReq().getParameter("ordre").compareToIgnoreCase("desc") == 0) {
                formu.getChamp("ordre").setValeur("asc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " asc ");
            } else {
                formu.getChamp("ordre").setValeur("desc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " desc ");
            }
        } else if (formu.getChamp("colonne").getValeur() != null && formu.getChamp("ordre").getValeur().compareToIgnoreCase("-") != 0 && formu.getChamp("colonne").getValeur().compareToIgnoreCase("") != 0) {
            if (Utilitaire.estIlDedans(formu.getChamp("colonne").getValeur(), Utilitaire.concatener(getColGroupe(), getColSomme())) != -1) {
                setOrdre(" order by " + formu.getChamp("colonne").getValeur() + " " + formu.getChamp("ordre").getValeur());
            }
        }
    }
    /**
     * génerer la partie de la requête where et order by de recherche à partir du contexte HTTP
     * pour les paramètres avec valeurs multiples
     * @throws Exception
     */
    public void makeCritereMultiple() throws Exception {
        Vector colIntv = new Vector();
        Vector valIntv = new Vector();
        Champ[] tempChamp = formu.getCrtFormu();
        for (int i = 0; i < tempChamp.length; i++) {
            Field f = bean.CGenUtil.getField(getBase(), tempChamp[i].getNom());
            if (tempChamp[i].getEstIntervalle() == 1) {
                colIntv.add(tempChamp[i].getNom());
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "1"));
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "2"));
            } else if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                String temp = getReq().getParameter(utilitaire.Utilitaire.remplacerUnderscore(tempChamp[i].getNom()));
                if (temp != null && temp.compareToIgnoreCase("") != 0) {
                    bean.CGenUtil.setValChamp(getCritere(), f, getParamSansNullMultiple(tempChamp[i].getNom()));
                }
            } else {
                //aWhere+=" and "+f.getName() +" like '"+ Utilitaire.getValeurNonNull(formu.getListeChamp() [i].getValeur()) +"'";
                aWhere += " and " + f.getName() + " like '" + Utilitaire.getValeurNonNull(getParamSansNullMultiple(tempChamp[i].getNom())) + "'";
            }
        }
        colInt = new String[colIntv.size()];
        colIntv.copyInto(colInt);
        valInt = new String[valIntv.size()];
        valIntv.copyInto(valInt);
        if (formu.getChamp("colonne").getValeur() != null && formu.getChamp("colonne").getValeur().compareToIgnoreCase("") != 0) {
            ordre += " order by " + formu.getChamp("colonne").getValeur() + " " + formu.getChamp("ordre").getValeur();
        }
    }

    private void setAnalyseLien() throws Exception {
        AnalyseLien crit = new AnalyseLien();
        crit.setNomTable("analyselien");
        int idUser = this.getUtilisateur().getUser().getRefuser();
        crit.setIdUtilisateur(idUser);
        crit.setPage(this.getPathPage());
        AnalyseLien[] liens = (AnalyseLien[]) CGenUtil.rechercher(crit, null, null, " and idutilisateur="+idUser);
        this.getFormu().setLiens(liens);
    }
    /**
     * Génerer les composants de l'affichage:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche,
     * prendre les données depuis la base de données,
     * préparer la table de récapitulation et
     * préparer la table de données la table de données
     * @throws Exception
     */
    public void creerObjetPage() throws Exception {
//        this.setAnalyseLien();
        
        getValeurFormulaire(); //Recuperation des valeurs choisi

        preparerData(); // Recuperation des donnees de la base

        makeTableauRecap();
        
        makeInsertLienModal();

        String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        this.setTableau(new TableauRecherche(getListe(), enteteAuto, critereLienTab, listF)); // Formation du tableau de resultat

    }
    /**
     * 
     * @throws Exception
     */
    public void makeInsertLienModal() throws Exception {
        try{
        AnalyseLien analyselien=new AnalyseLien();
        analyselien.setNomTable("analyselien");
        String lienAnalyse = this.getLien() +"?"+getReq().getQueryString();
        //analyselien.setLien(lienAnalyse);
        PageInsert pageinsertModal=new PageInsert(analyselien,getReq(),this.getUtilisateur());
        pageinsertModal.setLien(this.getLien());
        pageinsertModal.getFormu().getChamp("lien").setDefaut(lienAnalyse);
        pageinsertModal.getFormu().getChamp("lien").setAutre("readonly");
        pageinsertModal.getFormu().getChamp("idUtilisateur").setDefaut(""+this.getUtilisateur().getUser().getRefuser());
        pageinsertModal.getFormu().getChamp("idUtilisateur").setAutre("readonly");
        pageinsertModal.getFormu().getChamp("idUtilisateur").setLibelle("Reference");
        pageinsertModal.getFormu().getChamp("page").setDefaut(this.getPathPage()); 
        pageinsertModal.getFormu().getChamp("page").setAutre("readonly");
        pageinsertModal.getFormu().getChamp("page").setLibelle("lien de la page");
      
        this.setInsertLienAnalyse(pageinsertModal);
        }
        catch(Exception e){
            
            e.printStackTrace();
            
        }
    }
    
    public String getInsertLienModal()throws Exception {
        PageInsert analyselien=this.getInsertLienAnalyse();
        
        String ret = "<div class='modal fade' id='saveLienModal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>";
           ret+="<div class='modal-dialog'>";
             ret+=" <div class='modal-content'>";
                ret+=" <div class='modal-header'>";
                    ret+=" <button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button>";
                    ret+=" <h4 class='modal-title' id='myModalLabel'>Analyse Lien insertion</h4>";
                ret+="</div>";
                ret+="<div class='modal-body'>";
                ret+="<form action='"+analyselien.getLien()+"?but=apresTarif.jsp' method='post'  name='diplome' id='diplome'  data-parsley-validate>";
                analyselien.getFormu().makeHtmlInsertTabIndex();
                ret+=analyselien.getFormu().getHtmlInsert();
                ret+="<input name='acte' type='hidden' id='nature' value='insert'>";
                ret+="<input name='bute' type='hidden' id='bute' value='"+this.getPathPage()+"'>";
                ret+="<input name='classe' type='hidden' id='classe' value='bean.AnalyseLien'>";
                ret+="</form>";
                ret+="</div>";
            ret+="</div>";
         ret+="</div>";
        ret+="</div>";
        return ret;
    }
    /**
     * @deprecated la liste ne va pas être utilisé on va toujours 
     * prendre depuis la base de données
     * @param liste
     * @throws Exception
     */
    public void creerObjetPage(ClassMAPTable[] liste) throws Exception {
        setListe(liste);
        creerObjetPage();
    }
    /**
     * @deprecated col groupe n'a aucune utilité
     * @param colGroupe
     * @throws Exception
     */
    public void creerObjetPage(String colGroupe[]) throws Exception {
        creerObjetPage();
    }
    /**
     * 
     * Génerer les composants de l'affichage avec support de pourcentage sur les colonnes de la table:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche,
     * prendre les valeurs des liste/autocomplete et des données de recherche,
     * préparer la table de recapitulation,
     * préparer la table de données avec comme entête la somme des colonnes de groupe et les colonnes de sommes
     *  avec une colonne en plus appelé nombrepargroupe
     * @throws Exception
     */
    public void creerObjetPagePourc() throws Exception {
        getValeurFormulaire(); //Recuperation des valeurs choisi

        preparerData(); // Recuperation des donnees de la base

        makeTableauRecap();
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        String[] enteteNombre = {"nombrepargroupe"};
        enteteAuto = Utilitaire.ajouterTableauString(enteteAuto, enteteNombre);
        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        
        this.setTableau(new TableauRecherche(getListe(), enteteAuto, getPourcentage(), getPourc(), critereLienTab, listF)); // Formation du tableau de resultat
       

    }
    /**
     * préparer les données de la table de resultats avec nombrepargroupe comptant le nombre par groupe
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void preparerDataListMultiple(Connection c) throws Exception {
        critere.setNomTable(getBase().getNomTable());
        makeCritereMultiple();
        if (premier == true) {
            rs = new bean.ResultatEtSomme();
            rs.initialise(getColSomme());
        } else {
            rs = getUtilisateur().getDataPageGroupeMultiple(critere, getColGroupe(), getSommeGroupe(), getColInt(), getValInt(), getNumPage(), getAWhere(), getColSomme(), getOrdre(), c, npp);
        }
           
        setListe((bean.ClassMAPTable[]) rs.getResultat());
    }
    /**
     * préparer les données de la table de resultats avec nombrepargroupe comptant le nombre par groupe
     * @param count colonne de groupage pour le compte nombre par groupe
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void preparerDataListMultiple(String count, Connection c) throws Exception {
        critere.setNomTable(getBase().getNomTable());
        makeCritereMultiple();
        if (premier == true) {
            rs = new bean.ResultatEtSomme();
            rs.initialise(getColSomme());
        } else {
            rs = getUtilisateur().getDataPageGroupeMultiple(critere, getColGroupe(), getSommeGroupe(), getColInt(), getValInt(), getNumPage(), getAWhere(), getColSomme(), getOrdre(), c, npp, count);
        }

        setListe((bean.ClassMAPTable[]) rs.getResultat());
    }
    
    /**
     * préparer les données de la table de resultats avec nombrepargroupe comptant le nombre par groupe
     * @throws Exception
     */
    public void preparerDataMultiple() throws Exception {
        Connection c = null;
        try {
            c = new utilitaire.UtilDB().GetConn();
            preparerDataListMultiple(c);
            formu.getAllData(c);
            getReq().getSession().setAttribute("critere", getCritere());
        } catch (Exception ex) {
            throw (ex);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * préparer les données de la table de resultats avec nombrepargroupe comptant le nombre par groupe
     * @param count colonne de groupage pour le compte nombre par groupe
     * @throws Exception
     */
    public void preparerDataMultiple(String count) throws Exception {
        Connection c = null;
        try {
            c = new utilitaire.UtilDB().GetConn();
            preparerDataListMultiple(count, c);
            formu.getAllData(c);
            getReq().getSession().setAttribute("critere", getCritere());
        } catch (Exception ex) {
            throw (ex);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Génerer les composants de l'affichage avec support de pourcentage sur les colonnes de la table:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche en supportant les valeurs multiples de paramètre,
     * prendre les valeurs des liste/autocomplete et des données de recherche,
     * préparer la table de recapitulation,
     * préparer la table de données avec comme entête la somme des colonnes de groupe et les colonnes de sommes
     *  avec une colonne en plus appelé nombrepargroupe 
     * @throws Exception
     */
    public void creerObjetPagePourcMultiple() throws Exception {

        getValeurFormulaireMultiple(); //Recuperation des valeurs choisi

        preparerDataMultiple(); // Recuperation des donnees de la base

        makeTableauRecap();
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        String[] enteteNombre = {"nombrepargroupe"};
        enteteAuto = Utilitaire.ajouterTableauString(enteteAuto, enteteNombre);
        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        this.setTableau(new TableauRecherche(getListe(), enteteAuto, getPourcentage(), getPourc(), critereLienTab, listF)); // Formation du tableau de resultat

    }
    /**
     * Génerer les composants de l'affichage avec support de pourcentage sur les colonnes de la table:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche en supportant les valeurs multiples de paramètre,
     * prendre les valeurs des liste/autocomplete et des données de recherche,
     * préparer la table de recapitulation,
     * préparer la table de données avec comme entête la somme des colonnes de groupe et les colonnes de sommes
     *  avec une colonne en plus appelé nombrepargroupe 
     * @param count colonne de groupage pour le nombrepargroupe
     * @throws Exception
     */
    public void creerObjetPagePourcMultiple(String count) throws Exception {

        getValeurFormulaireMultiple(); //Recuperation des valeurs choisi

        preparerDataMultiple(count); // Recuperation des donnees de la base

        makeTableauRecap();
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        String[] enteteNombre = {"nombrepargroupe"};
        enteteAuto = Utilitaire.ajouterTableauString(enteteAuto, enteteNombre);
        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        this.setTableau(new TableauRecherche(getListe(), enteteAuto, getPourcentage(), getPourc(), critereLienTab, listF)); // Formation du tableau de resultat

    }
    /**
     * 
     * @param o classe de mapping
     * @param req contexte http
     * @param vrt liste des attributs pour le formulaire de recherche
     * @param listInterval liste des attributs à changer en intervalle
     * @param nbRange nombre de rangé sur le formulaire de recherche
     * @param colGr liste des attributs de groupage par défaut
     * @param sommGr liste des attributs de somme par défaut
     * @param nbCol nombre d'attributs de groupage pour le formulaire de choix
     * @param somGr nombre d'attribut de somme pour le formulaire de choix
     * @throws Exception
     */
    public PageRechercheGroupe(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] colGr, String[] sommGr, int nbCol, int somGr) throws Exception {
        setBase(o);
        o.setMode("groupe");
        o.setGroupe(true);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        getCritere().setMode("groupe");
        setReq(req);
        if (getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0) {
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
        setColGroupeDefaut(colGr);
        setSommeGroupeDefaut(sommGr);
        setNbColGroupe(nbCol);
        setNbSommeGrp(somGr);
        makeGroupe();
        formu.makeChampGroupe(getNbColGroupe(), getNbSommeGrp());
    }
    /**
     * A utiliser si on veut mettre nombrepargroupe
     * @param o classe de mapping
     * @param req contexte HTTP
     * @param vrt liste des attributs pour le formulaire de recherche
     * @param listInterval liste des attributs à changer en intervalle
     * @param nbRange nombre de rangé sur le formulaire de recherche
     * @param colGr liste des attributs de groupage par défaut
     * @param sommGr liste des attributs de somme par défaut
     * @param pourc liste des attributs avec pourcentages
     * @param nbCol nombre d'attributs de groupage pour le formulaire de choix
     * @param somGr nombre d'attribut de somme pour le formulaire de choix
     * @throws Exception
     */
    public PageRechercheGroupe(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] colGr, String[] sommGr, String[] pourc, int nbCol, int somGr) throws Exception {
        setBase(o);
        o.setGroupe(true);
        o.setMode("groupe");
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        getCritere().setMode("groupe");
        setReq(req);
        if (getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0) {
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        setColGroupeDefaut(colGr);
        setSommeGroupeDefaut(sommGr);
        setNbColGroupe(nbCol);
        setNbSommeGrp(somGr);
        setPourcentage(pourc);
        formu.makeChampFormu(vrt, listInterval, nbRange);

        makeGroupe();
        formu.makeChampGroupe(getNbColGroupe(), getNbSommeGrp());
    }
    /**
     * A utiliser si on veut mettre nombrepargroupe
     * @param o classe de mapping
     * @param req contexte HTTP
     * @param vrt liste des attributs pour le formulaire de recherche
     * @param listInterval liste des attributs à changer en intervalle
     * @param nbRange nombre de rangé sur le formulaire de recherche
     * @param colGr liste des attributs de groupage par défaut
     * @param sommGr liste des attributs de somme par défaut
     * @param pourc liste des attributs avec pourcentages
     * @param nbCol nombre d'attributs de groupage pour le formulaire de choix
     * @param somGr nombre d'attribut de somme pour le formulaire de choix
     * @param count attribut de groupage pour nombrepargroupe
     * @throws Exception
     */
    public PageRechercheGroupe(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] colGr, String[] sommGr, String[] pourc, int nbCol, int somGr, Liste count) throws Exception {
        this(o, req, vrt, listInterval, nbRange, colGr, sommGr, pourc, nbCol, somGr);
        this.setCount(count);
    }
    /**
     * Prendre les valeurs des colonnes groupes, ligne de groupe et somme depuis le contexte HTTP
     * @throws Exception
     */
    public void makeGroupe() throws Exception {
        String temp[] = new String[getNbColGroupe()];
        String ligneReq[]=new String[getNbColGroupe()];
        for (int i = 0; i < temp.length*2; i++) {
            if(i<getNbColGroupe())
            temp[i] = getReq().getParameter("colGroupe" + (i + 1));
            if(i>=getNbColGroupe()&&i<getNbColGroupe()*2)
            ligneReq[i-getNbColGroupe()]=getReq().getParameter("ligneGroupe" + (i + 1));
            
        }
        setColGroupe(Utilitaire.formerTableauGroupe(temp));
        
        setLigneGroupe(Utilitaire.formerTableauGroupe(ligneReq));
        
        
        String temp2[] = new String[getNbSommeGrp()];
        for (int i = 0; i < temp2.length; i++) {
            temp2[i] = getReq().getParameter("colSomme" + (getNbColGroupe() + i + 1));
        }
        setSommeGroupe(Utilitaire.formerTableauGroupe(temp2));
        //String st1[]={request.getParameter("colGroupe1"),request.getParameter("colGroupe2")};
    }
    /**
     * 
     * @return colonne de compte pour chaque groupe
     */
    public Liste getCount() {
        return count;
    }

    public void setCount(Liste count) {
        this.count = count;
    }
    /**
     * 
     * @return résultat de recherche groupé
     */
    public bean.ClassMAPTable[] getListe() {
        return liste;
    }

    public void setListe(bean.ClassMAPTable[] liste) {
        this.liste = liste;
    }
    /**
     * @deprecated aucune utilisation
     * @param listeTabDet
     */
    public void setListeTabDet(TableauRecherche listeTabDet) {
        this.listeTabDet = listeTabDet;
    }
    /**
     * @deprecated
     * @return
     */
    public TableauRecherche getListeTabDet() {
        return listeTabDet;
    }
    
    public void setCritere(bean.ClassMAPTable critere) {
        this.critere = critere;
    }
    /**
     * 
     * @return critère de recherche
     */
    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setColInt(String[] colInt) {
        this.colInt = colInt;
    }
    /**
     * 
     * @return colonne à mettre en intervalle
     */
    public String[] getColInt() {
        return colInt;
    }

    public void setValInt(String[] valInt) {
        this.valInt = valInt;
    }
    /**
     * 
     * @return liste des valeurs des intervalles
     */
    public String[] getValInt() {
        return valInt;
    }
    /**
     * @deprecated
     */
    public void makeHtml() {

    }
    /**
     * 
     * @param rs resultats et recapitulation de la recherche
     */
    public void setRs(bean.ResultatEtSomme rs) {
        this.rs = rs;
    }

    public bean.ResultatEtSomme getRs() {
        return rs;
    }
    /**
     * 
     * @param numPage numéro de page actuel
     */
    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }
    /**
     * Si numéro page dans la requête retourner cette valeur, sinon valeur setté dans l'objet
     * @return numéro de page courante
     */
    public int getNumPage() {
        int temp = 1;
        String tempS = getParamSansNull("numPag");
        if (tempS == null || tempS.compareToIgnoreCase("") == 0) {
            return numPage;
        }
        temp = utilitaire.Utilitaire.stringToInt(tempS);
        if (temp > 0) {
            return temp;
        }
        return numPage;
    }
    /**
     * 
     * @param colSomme liste des colonnes de sommes à utiliser prioritaire aux colonnes par défaut
     */
    public void setColSomme(String[] colSomme) {
        this.colSomme = colSomme;
    }
    /**
     * 
     * @return liste des colonnes de sommes à utiliser prioritaire aux colonnes par défaut
     */
    public String[] getColSomme() {
        return colSomme;
    }
    /**
     * Construire le tableau de recapitulation avec des colonnes récapitulant des colonnes de somme.
     * <p>
     *  Les colonnes de sommes auront le nom "Somme" + nomAttribut sans formattage d'entête
     * </p>
     * @throws Exception
     */
    public void makeTableauRecap() throws Exception {
        String[][] data = null;
        String[] entete = null;
        int nbColSomme = 0;
        if (getColSomme() != null) {
            nbColSomme = getColSomme().length;
        }
        entete = new String[nbColSomme + 2];
        data = new String[1][entete.length];
        entete[0] = "";
        data[0][0] = "Total";
        entete[1] = "Nombre";
        data[0][1] = utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[nbColSomme]));
        for (int i = 0; i < nbColSomme; i++) {
            data[0][i + 2] = utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[i]));
            entete[i + 2] = "Somme de " + String.valueOf(getColSomme()[i]);
        }
        TableauRecherche t = new TableauRecherche(data, entete);
        t.setTitre("RECAP");
        setTableauRecap(t);
    }
    /**
     * 
     * @return nombre total de page
     */
    public int getNombrePage() {
        return (Utilitaire.calculNbPage(rs.getSommeEtNombre()[getNombreColonneSomme()], getNpp()));
    }
    /**
     * 
     * @return nombre colonne de sommes actuels
     */
    public int getNombreColonneSomme() {
        int nbColSomme = 0;
        if (getColSomme() != null) {
            nbColSomme = getColSomme().length;
        }
        return nbColSomme;
    }
    /**
     * 
     * @return html du bas de la page de recherche avec navigation de pagination 
     * et utilitaire d'exportation
     * 
     * @throws Exception
     */
    public String getBasPage() throws Exception {
        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center width='100%'>";
        retour += "<tr><td height=25><b>Nombre de r&eacute;sultat :</b> " + utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[getNombreColonneSomme()])) + "</td><td align='right'><strong>page</strong> " + getNumPage() + " <b>sur</b>" + getNombrePage() + "</td>";
        retour += "</tr>";
        retour += "<tr>";
        retour += "<td width=50% valign='top' height=25>";
        if (getNumPage() > 1) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&premier="+ getPremier() +"&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + formu.getListeCritereString() + ">&lt;&lt;Page pr&eacute;c&eacute;dente</a>";
        }
        retour += "</td>";
        retour += "<td width=50% align=right>";
        if (getNumPage() < getNombrePage()) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&premier="+ getPremier()+"&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + formu.getListeCritereString() + ">Page suivante&gt;&gt;</a>";
        }
        retour += "</td>";
        retour += "</tr>";
        retour += "</table>";
        retour += getDownloadForm();
        return retour;
    }
    /**
     * 
     * @param apres lien à appeler suite à l'appui du bouton rechercher
     */
    public void setApres(String apres) {
        this.apres = apres;
    }
    /**
     * 
     * @return lien à appeler suite à l'appui du bouton rechercher
     */
    public String getApres() {
        return apres;
    }
    /**
     * 
     * @return requete SQL de filtre pour la recherche
     */
    public String getAWhere() {
        return aWhere;
    }

    public void setAWhere(String aWhere) {
        this.aWhere = aWhere;
    }
    /**
     * 
     * @param apresLienPage supplémentaire à mettre après le lien de recherche
     */
    public void setApresLienPage(String apresLienPage) {
        this.apresLienPage = apresLienPage;
    }

    public String getApresLienPage() {
        return apresLienPage;
    }
    /**
     * 
     * @param colGroupe colonnes de groupage pour la recherche
     */
    public void setColGroupe(String[] colGroupe) {
        this.colGroupe = colGroupe;
    }
    /**
     * 
     * @return colonnes de groupage si défini sinon les valeurs par défaut
     */
    public String[] getColGroupe() {
        if (colGroupe == null || colGroupe.length == 0) {
            return getColGroupeDefaut();
        }
        return colGroupe;
    }
    /**
     * 
     * @param sommeGroupe colonnes de somme pour la recherche
     */
    public void setSommeGroupe(String[] sommeGroupe) {
        this.sommeGroupe = sommeGroupe;
        setColSomme(getSommeGroupe());
    }
    /**
     * 
     * @param sommeGroupe colonnes de somme pour la recherche
     */
    public String[] getSommeGroupe() {
        if (sommeGroupe == null || sommeGroupe.length == 0) {
            return sommeGroupeDefaut;
        }
        return sommeGroupe;
    }
    /**
     * 
     * @param ordre ordre SQL order by colonne
     */
    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getOrdre() {
        return ordre;
    }
    /**
     * 
     * @param nbColGroupe nombre de colonnes de groupage pour le formulaire de choix
     */
    public void setNbColGroupe(int nbColGroupe) {
        this.nbColGroupe = nbColGroupe;
    }
    /**
     * 
     * @return nombre de colonnes de groupage pour le formulaire de choix
     */
    public int getNbColGroupe() {
        return nbColGroupe;
    }
    /**
     * 
     * @param nbSommeGrp nombre de colonnes de somme pour le formulaire de choix
     */
    public void setNbSommeGrp(int nbSommeGrp) {
        this.nbSommeGrp = nbSommeGrp;
    }

    public int getNbSommeGrp() {
        return nbSommeGrp;
    }

    /**
     * 
     * @param colGroupeDefaut colonne par défaut de groupage
     */
    public void setColGroupeDefaut(String[] colGroupeDefaut) {
        this.colGroupeDefaut = colGroupeDefaut;       
    }
    /**
     * définir la table representant le resultat
     */
    @Override
     public void setTableau(TableauRecherche tableau) {
        super.setTableau(tableau);
        if (this.getTableau() != null) {
            this.getTableau().setGroupe(true);
            this.getTableau().setLienFormulaire(generateUrlFormulaire());
        }
    }
    
    public String[] getColGroupeDefaut() {
        return colGroupeDefaut;
    }
    /**
     * 
     * @param sommeGroupeDefaut colonne par défaut de somme
     */
    public void setSommeGroupeDefaut(String[] sommeGroupeDefaut) {
        this.sommeGroupeDefaut = sommeGroupeDefaut;
        setColSomme(sommeGroupeDefaut);
    }

    public String[] getSommeGroupeDefaut() {
        return sommeGroupeDefaut;
    }

    public void setNpp(int npp) {
        this.npp = npp;
    }
    /**
     * 
     * @return nombre d'élèment par page
     */
    public int getNpp() {
        return npp;
    }
    /**
     * @deprecated
     * @param listeColonneMoyenne
     */
    public void setListeColonneMoyenne(String[] listeColonneMoyenne) {
        this.listeColonneMoyenne = listeColonneMoyenne;
    }
    /**
     * @deprecated
     * @return
     */
    public String[] getListeColonneMoyenne() {
        return listeColonneMoyenne;
    }
    /**
     * Génerer le html d'export de données
     * @return html d'utilitaire d'export de données
     * @throws Exception
     */
   public String getDownloadForm() {
        String tab = "";
        String[] somDefaut = this.getSommeGroupe();
        String[] colDefaut = this.getColGroupe();
        String sD = "";
        if (somDefaut != null && somDefaut.length > 0) {
            sD = somDefaut[0];
        }
        String cD = "";
        if (colDefaut != null) {
            cD = colDefaut[0];
        }
        String cI = "";
        if (colInt != null) {
            cI = colInt[0];
        }
        String vI = "";
        if (valInt != null) {
            vI = valInt[0];
        }
        try {

            tab = this.getTableau().getHtml();

            if (somDefaut != null) {
                for (int i = 1; i < somDefaut.length; i++) {
                    sD = sD + "," + somDefaut[i];
                }
            }
            if (colDefaut != null) {
                for (int j = 1; j < colDefaut.length; j++) {
                    cD = cD + "," + colDefaut[j];
                }
            }
            if (colInt != null) {
                for (int j = 1; j < colInt.length; j++) {
                    cI = cI + "," + colInt[j];
                }
            }
            if (valInt != null) {
                for (int j = 1; j < valInt.length; j++) {
                    vI = vI + "," + valInt[j];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-primary box-solid collapsed-box'>";
        temp += "<div style='background:#0E6D36; color:white;'class='box-header with-border'>";
        temp += "<h3 class='box-title'>Exporter</h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body'>";
        temp += "<form action=\"../downloadGroupe\" method=\"post\">";
        temp += "<table id='export'>";
        temp += "<tr>";
        temp += "<th colspan='4'><h3 class='box-title'>Format</h3></th>";
        temp += "<th colspan='2'><h3 class='box-title'>Donne&eacute;es &agrave; exporter</h3></th>";
        temp += "<th></th>";
        temp += "</tr>";
        temp += "<tr>";
        temp += "<td>";
        temp += "<img src='../dist/img/csv_text.png'>";
        temp += "<input type='radio' name='ext' value='csv' checked='checked' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<img src='../dist/img/file_xls.png'>";
        temp += "<input type='radio' name='ext' value='xls' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<img src='../dist/img/file_xml.png'>";
        temp += "<input type='radio' name='ext' value='xml' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<img src='../dist/img/file_pdf.png'>";
        temp += "<input type='radio' name='ext' value='pdf' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='donnee'>Page En Cours</label>";
        temp += "<input type='radio' name='donnee' value=\"" + 0 + "\" checked='checked' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='donnee'>Toutes</label>";
        temp += "<input type='radio' name='donnee' value=\"" + 1 + "\" />";
        temp += "</td>";
        temp += "<td>";
        if (this.getTableau().getExpxml() != null) {
            temp += "<input type=\"hidden\" name=\"xml\" value=\"" + this.getTableau().getExpxml().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"csv\" value=\"" + this.getTableau().getExpcsv().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"awhere\" value=\"" + this.getAWhere().replace('"', '\'') + "\" />";
            temp += "<input type=\"hidden\" name=\"table\" value=\"" + tab.replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"colDefaut\" value=\"" + cD + "\" />";
            temp += "<input type=\"hidden\" name=\"somDefaut\" value=\"" + sD + "\" />";
            temp += "<input type=\"hidden\" name=\"colInt\" value=\"" + cI + "\" />";
            temp += "<input type=\"hidden\" name=\"valInt\" value=\"" + vI + "\" />";
            temp += "<input type=\"hidden\" name=\"ordre\" value=\"" + this.getOrdre() + "\" />";
            temp += "<input type=\"hidden\" name=\"entete\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibeEntete(), "", ";") + "\" />";
            temp += "<input type=\"hidden\" name=\"entetelibelle\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibelleAffiche(), "", ";") + "\" />";
        }
        int sommeId = Utilitaire.estIlDedans("ttc", getColSomme());
        if (sommeId > -1) {
            temp += "<input type=\"hidden\" name=\"nombreEncours\" value=\"" + Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getColSomme().length]) + "\" />";
            double test = rs.getSommeEtNombre()[sommeId];
            temp += "<input type=\"hidden\" name=\"recap\" value=\"" + test + "\" />";
        }
        temp += "<input type='hidden' name='titre' value='" + getTitre() + "' />";
        temp += "<input type='submit' value='Exporter' class='btn btn-default' />";
        temp += "</td>";
        temp += "</tr>";
        temp += "</table>";
        temp += "</form>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        return temp;
    }
    /**
     * 
     * @param pourcentage liste des pourcentages
     */
    public void setPourcentage(String[] pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String[] getPourcentage() {
        return pourcentage;
    }
    /**
     * 
     * @param pourc valeur des pourcentages
     */
    public void setPourc(double[][] pourc) {
        this.pourc = pourc;
    }

    public double[][] getPourc() {
        return pourc;
    }
    /**
     * @deprecated
     * @return
     */
    public String getCamembertChart() {
        String ret = "";
        return ret;
    }
    /**
     * 
     * @param premier première navigation à la page
     */
    public void setPremier(boolean premier) {
        this.premier = premier;
    }

    public boolean getPremier() {
        return premier;
    }

    public boolean isPremier() {
        return premier;
    }
    /**
     * 
     * @return lien avec paramètre venant du formulaire
     */
    public String generateUrlFormulaire(){
        String retour = "";
        Champ[] c = formu.getListeChamp(); 
        for (int i = 0; i < c.length; i++) {
            String valeurRecup = getParamSansNull(c[i].getNom());
            if(valeurRecup!=null && !valeurRecup.isEmpty() && !valeurRecup.equals("%")){
                 retour+=c[i].getNom()+"="+valeurRecup+"&";
            }                                      
        }        
        return retour;
    }
    /**
     * @deprecated le cumul est inutile pour les pages paginés comme on va avoir le cumul de la page actuelle
     * @param rs liste des resultats
     * @param col liste des colonnes à cumuler
     * @return liste en deux dimensions des cumuls
     * @throws Exception
     */
    public static double[][] cumul(ResultatEtSomme rs, String[] col) throws Exception {
        ClassMAPTable[] os = (ClassMAPTable[]) rs.getResultat();
        double[][] ret = new double[os.length][col.length];
        for(int j=0; j<col.length; j++) {
            double cumul = 0;
            for(int i=0; i<os.length; i++) {
                cumul += (double) CGenUtil.getValeurFieldByMethod(os[i], col[j]);
                ret[i][j] += cumul;
            }
            System.out.println("");
        }
        return ret;
    }
    
    public String[] getLigneGroupe() {
        return ligneGroupe;
    }
    /**
     * 
     * @param ligneGroupe liste des colonne à utiliser en groupe
     */
    public void setLigneGroupe(String[] ligneGroupe) {
        this.ligneGroupe = ligneGroupe;
    }
    
    /**
     * Génerer les composants de l'affichage:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche,
     * prendre les données depuis la base de données,
     * préparer la table de récapitulation et
     * préparer la table de données en tant que tableau croisé
     * @param lien base de lien de redirection pour les valeurs
     * @throws Exception
     */
    
    public void creerObjetPageCroise(String lien) throws Exception
    {
        getValeurFormulaire(); //Recuperation des valeurs choisi

        preparerData(); // Recuperation des donnees de la base

        makeTableauRecap();
        //String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        
        setTableau(new TableauRechercheGroupe(getListe(), this.getColGroupe(),getSommeGroupeDefaut(),lien) ); // Formation du tableau de resultat   
    }
    /**
     * Génerer les composants de l'affichage:
     * Récuperer les valeurs de filtre à partir du formulaire de recherche,
     * prendre les données depuis la base de données,
     * préparer la table de récapitulation et
     * préparer la table de données en tant que tableau croisé
     * @param colon liste des attributs de colonne
     * @param lien base de lien de redirection pour les valeurs
     * @throws Exception
     */
    public void creerObjetPageCroise(String []colon,String lien) throws Exception
    {
        if(this.getLigneGroupe()!=null&&this.getLigneGroupe().length>0)colon=this.getLigneGroupe();
        this.setColGroupe(Utilitaire.ajouterTableauString(getColGroupe(), colon));
        getValeurFormulaire(); //Recuperation des valeurs choisi

        preparerData(); // Recuperation des donnees de la base

        makeTableauRecap();
        //String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        
        setTableau(new TableauRechercheGroupe(getListe(), this.getColGroupe(),colon,getSommeGroupe(),lien) ); // Formation du tableau de resultat   
        
    }

}
