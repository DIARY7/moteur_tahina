package affichage;

import bean.CGenUtil;
import bean.ClassMAPTable;
import constante.ConstanteAffichage;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import utilitaire.Utilitaire;

/**
 * Objet à utiliser à l'affichage pour génerer la page recherche avec liste d'un objet de mapping
 * 
 * Ci-dessous un exemple de comment créer une page de recherche. Cette partie doit se trouver au début du jsp :
 * 
 * <pre>
 * {@code
 *      String listeCrt[] = {"idObjet","idTypePatrimoine"};
 *      String listeInt[] = {};
 *      String libEntete[] = {"idObjet", "idTypePatrimoine", "remarque"};
 *      PageRecherche pr = new PageRecherche(s, request, listeCrt, listeInt, 3, libEntete, libEntete.length);
 *      pr.getFormu().getChamp("idObjet").setLibelle("objet");
 *      pr.getFormu().getChamp("idTypePatrimoine").setLibelle("Type du Patrimoine");
 *      pr.setUtilisateur((user.UserEJB) session.getValue("u"));
 *      pr.setLien((String) session.getValue("lien"));
 *      pr.setApres("patrimoine/patrimoine-liste.jsp");
 *      String[] colSomme = null;
 *      pr.creerObjetPage(libEntete, colSomme);
 * }
 * </pre>
 * 
 * Ensuite sur le corps du jsp où on veut mettre la barre de recherche, on va imprimer : 
 * <pre>
 * {@code 
 * <form action="<%=pr.getLien()%>?but=patrimoine/patrimoine-liste.jsp" method="post">
 *          <%
 *              out.println(pr.getFormu().getHtmlEnsemble());
 *          %>
 * </form>
 * }
 * </pre>
 * Puis sur le corps du jsp où on veut mettre la récapitulation, on va imprimer:
 * <pre>
 * {@code 
 * <%
 *           String lienTableau[] = {pr.getLien() + "?but=patrimoine/patrimoine-fiche.jsp"};
 *           String colonneLien[] = {"id"};
 *           pr.getTableau().setLien(lienTableau);
 *           pr.getTableau().setColonneLien(colonneLien);
 *           out.println(pr.getTableauRecap().getHtml());%>
 *       <br>
 * }
 * </pre>
 * Ensuite sur le corps du jsp où on veut mettre la liste, on va imprimer : 
 * <pre>
 * {@code
 * <%
 *   String libEnteteAffiche[] = {"Objet", "Type de Patrimoine", "Remarque"};
 *   pr.getTableau().setLibelleAffiche(libEnteteAffiche);
 *   out.println(pr.getTableau().getHtml());
 *   out.println(pr.getBasPage());
 *   %>
 * }
 * </pre>
 * @author BICI
 * @version 1.0
 */
public class PageRecherche extends Page {

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
    private String ordre = "";
    private String apresLienPage = "";
    private int npp = 0;
    private String[] tableauAffiche;
    private int nbTableauAffiche;
    private boolean premier = false;
    private String[] tableauAfficheDefaut;
    private String critereHttp = "";
    private String multipleAwhere = "";
    /**
     * construire les colonnes à afficher à partir de la requête HTTP
     * @throws Exception
     */
    public void makeTableauAffiche() throws Exception {
        String temp[] = new String[getNbTableauAffiche()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = getReq().getParameter("colAffiche" + (i + 1));
        }
        setTableauAffiche(Utilitaire.formerTableauGroupe(temp));
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
        } else {
            //System.out.println("NIDITRA PREPARER DATALIST Bouton ");
            String recap = getReq().getParameter("recap");
            recap = "checked";
            this.getFormu().setRecapcheck(recap);
            String req = getMultipleAwhere() + getAWhere();
            rs = getUtilisateur().getDataPage(critere, getColInt(), getValInt(), getNumPage(), req, getColSomme(), c, npp);    
        }
        setListe((bean.ClassMAPTable[]) rs.getResultat());
    }



    public String getOrdre() {
        return ordre;
    }
    /**
     * 
     * @param ordre ordre SQL order by colonne
     */
    public void setOrdre(String ordre) {
        this.ordre = ordre;
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

    public void setPremier(boolean premier) {
        this.premier = premier;
    }

    public boolean getPremier() {
        return premier;
    }

    /**
     * 
     * @return verifier si c'est la première navigation à la page
     */
    public boolean isPremier() {
        return premier;
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
        this.getCritere().setMode("select");
        String temp = "";
        for (int i = 0; i < tempChamp.length; i++) {
            Field f = bean.CGenUtil.getField(getBase(), tempChamp[i].getNom());
             boolean isMultiple = tempChamp[i].getEstMultiple();
//             System.out.println("IsMultiple "+ tempChamp[i].getNom()+" "+isMultiple);
//            System.out.println("getParamSansnUll "+getParamSansNull(tempChamp[i].getNom()) + " tempChamp === "+tempChamp[i].getValeur());
            if(isMultiple){
                String[] valeurs = getReq().getParameterValues(tempChamp[i].getNom());
                if(valeurs != null && valeurs.length > 0 && !valeurs[0].isEmpty()){
                    if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                        multipleAwhere += " AND " + tempChamp[i].getNom() + " IN ("+Utilitaire.tabToString(valeurs, "'", ",") + ")";
                    }
                    else{
                        multipleAwhere += " AND " + tempChamp[i].getNom() + " IN ("+Utilitaire.tabToString(valeurs, "", ",") + ")";
                    }
//                    System.out.println("multipleAwhere "+multipleAwhere);
                }
            }
            else{
            if (tempChamp[i].getEstIntervalle() == 1) {
                colIntv.add(tempChamp[i].getNom());
                if((getParamSansNull(tempChamp[i].getNom() + "1")==null||getParamSansNull(tempChamp[i].getNom() + "1").compareToIgnoreCase("")==0)&&this.getFormu().getChamp((tempChamp[i].getNom() + "1"))!=null&&this.getFormu().getChamp((tempChamp[i].getNom() + "1")).getDefaut()!=null&&this.getFormu().getChamp((tempChamp[i].getNom() + "1")).getDefaut().compareToIgnoreCase("")!=0)
                {
                    valIntv.add(this.getFormu().getChamp((tempChamp[i].getNom() + "1")).getDefaut());
                }
                else valIntv.add(getParamSansNull(tempChamp[i].getNom() + "1"));
                if((getParamSansNull(tempChamp[i].getNom() + "2")==null||getParamSansNull(tempChamp[i].getNom() + "2").compareToIgnoreCase("")==0)&&this.getFormu().getChamp((tempChamp[i].getNom() + "2"))!=null&&this.getFormu().getChamp((tempChamp[i].getNom() + "2")).getDefaut()!=null&&this.getFormu().getChamp((tempChamp[i].getNom() + "2")).getDefaut().compareToIgnoreCase("")!=0)
                {
                    valIntv.add(this.getFormu().getChamp((tempChamp[i].getNom() + "2")).getDefaut());
                }
                else valIntv.add(getParamSansNull(tempChamp[i].getNom() + "2"));
            } else if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                if((getParamSansNull(tempChamp[i].getNom())==null||getParamSansNull(tempChamp[i].getNom()).compareToIgnoreCase("")==0)&&this.getFormu().getChamp((tempChamp[i].getNom()))!=null&&this.getFormu().getChamp((tempChamp[i].getNom())).getDefaut()!=null&&this.getFormu().getChamp((tempChamp[i].getNom())).getDefaut().compareToIgnoreCase("")!=0)
                {
                    bean.CGenUtil.setValChamp(getCritere(), f, this.getFormu().getChamp(tempChamp[i].getNom()).getValeur());
                }
                else
                {
                    String valeurChamp = getReq().getParameter(utilitaire.Utilitaire.remplacerUnderscore(tempChamp[i].getNom()));
                    if (valeurChamp != null && valeurChamp.compareTo("") != 0) {
                        System.out.println(" getCritere() = " + getCritere() + " f.getName() " + f.getName() + " getParamSansNull(tempChamp[i].getNom()) " + getParamSansNull(tempChamp[i].getNom()));
                        
                        bean.CGenUtil.setValChamp(getCritere(), f, getParamSansNull(tempChamp[i].getNom()));
                    }
                }
            } 
            else {
                if((getParamSansNull(tempChamp[i].getNom())==null||getParamSansNull(tempChamp[i].getNom()).compareToIgnoreCase("")==0)&&this.getFormu().getChamp((tempChamp[i].getNom()))!=null&&this.getFormu().getChamp((tempChamp[i].getNom())).getDefaut()!=null&&this.getFormu().getChamp((tempChamp[i].getNom())).getDefaut().compareToIgnoreCase("")!=0)
                    aWhere += " and " + f.getName() + " like '" + Utilitaire.getValeurNonNull(this.getFormu().getChamp(tempChamp[i].getNom()).getValeur()) + "'";
                else    
                    aWhere += " and " + f.getName() + " like '" + Utilitaire.getValeurNonNull(getParamSansNull(tempChamp[i].getNom())) + "'";
            }
            }
       
            if(getParamSansNull(tempChamp[i].getNom()) != null && getParamSansNull(tempChamp[i].getNom()).compareToIgnoreCase("") != 0){
//                System.out.println("&" + f.getName() + "=" + getParamSansNull(tempChamp[i].getNom()));
                critereHttp += "&" + f.getName() + "=" + getParamSansNull(tempChamp[i].getNom());
            }
//            System.out.println("critereHttp "+critereHttp);
        }
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
        }
        if (formu.getChamp("colonne").getValeur() != null && formu.getChamp("ordre").getValeur().compareToIgnoreCase("-") != 0 && formu.getChamp("colonne").getValeur().compareToIgnoreCase("") != 0 ) {
            this.setOrdre(" order by " + formu.getChamp("colonne").getValeur() + " " + formu.getChamp("ordre").getValeur());
        }
        aWhere += this.getOrdre();
        /** mettre la valeur de currentEtat */
        if(getReq().getParameter("etat_table")!=null){
            formu.setCurrentEtat(getReq().getParameter("etat_table"));
        }
    }
    /**
     * Génerer le tableau de valeur avec entête
     * @param libEntete liste des champs en tant qu'entête du tableau
     * @throws Exception
     */
    public void creerObjetPage(String libEntete[]) throws Exception {
        getValeurFormulaire();
        preparerData();
        makeTableauRecap();
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString() + "";

        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        setTableau(new TableauRecherche(getListe(), libEntete, critereLienTab, listF));

    }
    /**
     * Génerer la table de valeur avec entête
     * @param liste resultat de recherche
     * @param libEntete liste des champs en tant qu'entête du tableau
     * @throws Exception
     */

    public void creerObjetPage(ClassMAPTable[] liste, String libEntete[]) throws Exception {
        setListe(liste);
        creerObjetPage(libEntete);
    }
    /**
     * Génerer la table de valeur selon colonnes d'entête
     * @param libEnteteDefaut liste des colonnes en tant qu'entête si le formulaire de recherche n'a pas specifié les colonnes à afficher
     * @param libEntete liste des colonnes en tant qu'entête du tableau
     * @throws Exception
     */
    public void creerObjetPage(String libEnteteDefaut[], String[] colSom) throws Exception {
        setColSomme(colSom);
        getValeurFormulaire();
        preparerData();
        makeTableauRecap();
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();

        String[] enteteAuto = getTableauAffiche();
        if (getTableauAffiche() == null) {
            setTableau(new TableauRecherche(getListe(), libEnteteDefaut, critereLienTab, listF));
        } else {
            setTableau(new TableauRecherche(getListe(), enteteAuto, critereLienTab, listF));
        }
    }

    /**
     * 
     * @param o objet de mapping
     * @param req contexte HTTP de la page
     * @param vrt liste des champs de critère pour le formulaire de recherche
     * @param listInterval liste des champs à prendre en tant qu'intervalle
     * @param nbRange nombre de rangés dans le formulaire de recherche
     * @param tabAff colonne par défaut à afficher pour le tableau
     * @param nbAff nombre de champs sur le formulaire d'affichage de colonnes
     * @throws Exception
     */

    public PageRecherche(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] tabAff, int nbAff,Boolean premier) throws Exception {
       this.premier = premier;
        setBase(o);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        setReq(req);
        if (getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0) {
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
        setNbTableauAffiche(nbAff);
        setTableauAfficheDefaut(tabAff);
        makeTableauAffiche();
        formu.makeChampTableauAffiche(getNbTableauAffiche(), tabAff);
        if (req != null) {
            if (req.getSession().getAttribute("lang") != null) {
                formu.lang = String.valueOf(req.getSession().getAttribute("lang"));
            }
        }
    }


    public PageRecherche(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] tabAff, int nbAff) throws Exception {
        setBase(o);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        setReq(req);
        if (getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0) {
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
        setNbTableauAffiche(nbAff);
        setTableauAfficheDefaut(tabAff);
        makeTableauAffiche();
        formu.makeChampTableauAffiche(getNbTableauAffiche(), tabAff);
        if (req != null) {
            if (req.getSession().getAttribute("lang") != null) {
                formu.lang = String.valueOf(req.getSession().getAttribute("lang"));
            }
        }
    }
    /**
     * constructeur par défaut
     */
    public PageRecherche() {
    }

    /**
     * 
     * @param o objet de mapping
     * @param req contexte HTTP
     * @param vrt liste des colonnes à mettre en champs de formulaire de recherche
     * @param listInterval liste des colonnes à mettre en intervalle sur le formulaire de recherche
     * @param nbRange
     * @throws Exception
     */

    public PageRecherche(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange) throws Exception {
        setBase(o);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        setReq(req);
        if (getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0) {
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
    }
    /**
     * 
     * @return resultat de la recherche
     */
    public bean.ClassMAPTable[] getListe() {
        return liste;
    }

    public void setListe(bean.ClassMAPTable[] liste) {
        this.liste = liste;
    }

    public void setListeTabDet(TableauRecherche listeTabDet) {
        this.listeTabDet = listeTabDet;
    }
    /**
     * @deprecated non referencé
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
     * @return objet de mapping avec les attributs avec les valeurs des critère de recherches
     */
    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setColInt(String[] colInt) {
        this.colInt = colInt;
    }
    /**
     * 
     * @return colonnes d'intervalles
     */
    public String[] getColInt() {
        return colInt;
    }

    public void setValInt(String[] valInt) {
        this.valInt = valInt;
    }
    /**
     * 
     * @return valeur des colonnes d'intervalle par indice de 2
     */
    public String[] getValInt() {
        return valInt;
    }
    /**
     * @deprecated ne fait rien
     */
    public void makeHtml() {

    }

    public void setRs(bean.ResultatEtSomme rs) {
        this.rs = rs;
    }

    /**
     * 
     * @return liste des resultats et liste des sommes de la recherche
     */

    public bean.ResultatEtSomme getRs() {
        return rs;
    }

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
        //System.out.println("tempsS==>"+tempS);
        if (tempS == null || tempS.compareToIgnoreCase("") == 0) {
            return numPage;
        }
        temp = utilitaire.Utilitaire.stringToInt(tempS);
        if (temp > 0) {
            return temp;
        }
        return numPage;
    }

    public void setColSomme(String[] colSomme) {
        this.colSomme = colSomme;
    }
    /**
     * 
     * @return liste des colonnes avec somme sur la récapitulation
     */
    public String[] getColSomme() {
        return colSomme;
    }
    /**
     * Génerer le tableau de récapitulation
     * @throws Exception
     */
    public void makeTableauRecap() throws Exception {
        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String[][] data = null;
        String[] entete = null;
        int nbColSomme = 0;
        if (getColSomme() != null) {
            nbColSomme = getColSomme().length;
        }
        entete = new String[nbColSomme + 2];
        data = new String[1][entete.length];
        entete[0] = "";
        data[0][0] = RB.getString("total");
        entete[1] = RB.getString("nombre");
        data[0][1] = utilitaire.Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[nbColSomme]);
        for (int i = 0; i < nbColSomme; i++) {
            data[0][i + 2] = utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[i]));
            entete[i + 2] = RB.getString("sommede") + " " + String.valueOf(getColSomme()[i]);
        }
        TableauRecherche t = new TableauRecherche(data, entete);
        t.setTitre(RB.getString("recapitulation"));
        setTableauRecap(t);
    }
    /**
     * 
     * @return nombre de page selon le nombre de colonnes en base et le nombre d'élèment par page
     */
    public int getNombrePage() {
        return (Utilitaire.calculNbPage(rs.getSommeEtNombre()[getNombreColonneSomme()], getNpp()));
    }
    /**
     * 
     * @return nombre de colonnes de somme pour la récapitulation
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
        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center width='100%'>";
        retour += "<tr><td height=25><b>" + RB.getString("nbresultat") + " :</b> " + utilitaire.Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getNombreColonneSomme()]) + "</td><td align='right'><strong>page</strong> " + getNumPage() + " <b>sur</b> " + getNombrePage() + "</td>";
        retour += "</tr>";
        retour += "<tr>";
        retour += "<td width=50% valign='top' height=25>";
        if (getNumPage() > 1) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + formu.getListeCritereStringCheckbox(this) + ">&lt;&lt;" + RB.getString("pageprecedente") + "</a>";
        }
        retour += "</td>";
        retour += "<td width=50% align=right>";
        if (getNumPage() < getNombrePage()) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + formu.getListeCritereStringCheckbox(this) + ">" + RB.getString("pagesuivante") + "&gt;&gt;</a>";
        }
        retour += "</td>";
        retour += "</tr>";
        retour += "</table>";
        retour += getDownloadForm();
        return retour;
    }
    /**
     * 
     * @return html du bas de la page de recherche avec navigation de pagination 
     * sans utilitaire d'exportation
     * 
     * @throws Exception
     */
    public String getBasPagee() {

        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center width='100%'>";
        retour += "<tr><td height=25><b>" + RB.getString("nbresultat") + " :</b> " + utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[getNombreColonneSomme()])) + "</td><td align='right'><strong>page</strong> " + getNumPage() + " <b>sur</b> " + getNombrePage() + "</td>";
        retour += "</tr>";
        retour += "<tr>";
        retour += "<td width=50% valign='top' height=25>";
        if (getNumPage() > 1) {
            retour += "<a href=" + getApres() + "?a=1" + formu.getListeCritereString() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + "&champReturn=etudiant>&lt;&lt;" + RB.getString("pageprecedente") + "</a>";
        }
        retour += "</td>";
        retour += "<td width=50% align=right>";
        if (getNumPage() < getNombrePage()) {
            retour += "<a href=" + getApres() + "?a=1" + formu.getListeCritereString() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + "&champReturn=etudiant>" + RB.getString("pagesuivante") + "&gt;&gt;</a>";
        }
        retour += "</td>";
        retour += "</tr>";
        retour += "</table>";
        return retour;
    }

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
     * @return requete SQL après la clause where pour filtrer le resultat
     */
    public String getAWhere() {
        return aWhere;
    }

    public void setAWhere(String aWhere) {
        this.aWhere = aWhere;
    }

    public String getMultipleAwhere() {
        return multipleAwhere;
    }

    public void setMultipleAwhere(String multipleAwhere) {
        this.multipleAwhere = multipleAwhere;
    }
    
    public void setApresLienPage(String apresLienPage) {
        this.apresLienPage = apresLienPage;
    }
    /**
     * 
     * @return paramètre supplémentaire à mettre après le lien de recherche
     */
    public String getApresLienPage() {
        return apresLienPage;
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

    public void setTableauAffiche(String[] tableauAffiche) {
        this.tableauAffiche = tableauAffiche;
    }
    /**
     * 
     * @return liste des colonnes à afficher sur la table de resultat
     */
    public int getNbTableauAffiche() {
        return nbTableauAffiche;
    }

    public void setNbTableauAffiche(int nbTableauAffiche) {
        this.nbTableauAffiche = nbTableauAffiche;
    }
    /**
     * Génerer le html d'export de données
     * @return html d'utilitaire d'export de données
     * @throws Exception
     */
    public String getDownloadForm() throws Exception {
        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String tab = "";
        String tab1 = "";
        try {
            tab = this.getTableau().getHtml();
            tab1 = this.getTableau().getHtmlPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String awhere = " AND " + CGenUtil.makeWhere(getBase()) + CGenUtil.makeWhereIntervalle(colInt, valInt) + this.getAWhere().replace('"', '\'');
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-solid collapsed-box'>";
        temp += "<div style='background-color:"+ ConstanteAffichage.couleurPrimaire+"; color:white;'class='box-header with-border'>";
        temp += "<h3 class='box-title'>" + RB.getString("exporter") + "</h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body'>";
        temp += "<form action=\"../download\" method=\"post\">";
        temp += "<table id='export'>";
        temp += "<tr>";
        temp += "<th colspan='4'><h3 class='box-title'>" + RB.getString("format") + "</h3></th>";
        temp += "<th colspan='2'><h3 class='box-title'>" + RB.getString("donneeexport") + "</h3></th>";
        temp += "<th colspan='2'><h3 class='box-title'><strong>LANDSCAPE</strong>(pour pdf)</h3></th>";
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
        temp += "<label for='donnee'>" + RB.getString("pageencours") + " </label>";
        temp += "<input type='radio' name='donnee' value=\"" + 0 + "\" checked='checked' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='donnee'>" + RB.getString("toutes") + " </label>";
        temp += "<input type='radio' name='donnee' value=\"" + 1 + "\" />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='landscape'>Portrait</label>";
        temp += "<input type='radio' name='landscape' value=\"" + 0 + "\" checked='checked' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='landscape'>Paysage</label>";
        temp += "<input type='radio' name='landscape' value=\"" + 1 + "\" />";
        temp += "</td>";
        temp += "<td>";
        if (this.getTableau().getExpxml() != null) {
            temp += "<input type=\"hidden\" name=\"xml\" value=\"" + this.getTableau().getExpxml().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"csv\" value=\"" + this.getTableau().getExpcsv().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"awhere\" value=\"" + awhere + "\" />";
            temp += "<input type=\"hidden\" name=\"table\" value=\"" + tab1.replace('"', '*').replace("\u0027", " ").replace("\"", " ") + "\" />";
            temp += "<input type=\"hidden\" name=\"entete\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibeEntete(), "", ";") + "\" />";
            temp += "<input type=\"hidden\" name=\"entetelibelle\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibelleAffiche(), "", ";") + "\" />";
        }
        int sommeId = Utilitaire.estIlDedans("montant", getColSomme());
        if (sommeId > -1) {
            temp += "<input type=\"hidden\" name=\"nombreEncours\" value=\"" + Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getColSomme().length]) + "\" />";
            double test = rs.getSommeEtNombre()[sommeId];
            String testEnString = Utilitaire.enleverExponentielleDouble(test);
            temp += "<input type=\"hidden\" name=\"recap\" value=\"" + testEnString + "\" />";
        }

        temp += "<input type='submit' value='" + RB.getString("exporter") + "' class='btn btn-default' />";
        temp += "<input type='hidden' name='titre' value='" + getTitre() + "' />";
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
     * @return la liste des colonnes à afficher sur le tableau
     */
    public String[] getTableauAffiche() {
        if (tableauAffiche == null || tableauAffiche.length == 0) {
            return tableauAfficheDefaut;
        }
        return tableauAffiche;
    }
    /**
     * 
     * @return la liste des colonnes à afficher sur le tableau par défaut 
     * si les colonnes n'ont pas été specifiées en paramètre de la requête HTTP
     */
    public String[] getTableauAfficheDefaut() {
        return tableauAfficheDefaut;
    }

    public void setTableauAfficheDefaut(String[] tableauAfficheDefaut) {
        this.tableauAfficheDefaut = tableauAfficheDefaut;
    }

    public String getCritereHttp() {
        return critereHttp;
    }

    public void setCritereHttp(String critereHttp) {
        this.critereHttp = critereHttp;
    }
    
    /**
     * 
     * @param o objet de la recherche avec critère
     * @param req contexte HTTP
     * @param vrt liste des colonnes à utiliser pour construire le formulaire de recherche
     * @param listInterval liste des colonnes à prendre comme intervalle sur le formulaire de recherche
     * @param nbRange liste des colonnes à prendre comme intervalle sur le formulaire
     * @param tabAff liste des colonnes à afficher pour les resultats
     * @throws Exception
     */
    public PageRecherche(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] tabAff) throws Exception {
        setBase(o);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        setReq(req);
        if(getReq().getParameter("premier") != null && getReq().getParameter("premier").compareToIgnoreCase("") != 0){
            setPremier(Boolean.valueOf(getReq().getParameter("premier")).booleanValue());
        }
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
        setNbTableauAffiche(tabAff.length);
        setTableauAfficheDefaut(tabAff);
        makeTableauAffiche();
        formu.makeChampTableauAffiche(getNbTableauAffiche(), tabAff);
    }
    
    public String getPagination() throws Exception{
        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));
        String formLien = getApres() + getCritereHttp();
                
        String retour = "";

        int maxPageAffiche = 2;
        int startPage = Math.max(1, getNumPage() - maxPageAffiche);
        int endPage = Math.min(getNombrePage(), getNumPage() + maxPageAffiche);
        
        retour += "<div class=\"pagination d-flex justify-content-between align-items-center \">\n" +
"                <div class=\"number \">\n" +
"                    "+getListe().length+" r&eacute;sultats sur "+utilitaire.Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getNombreColonneSomme()])+"\n" +
"                </div>\n" +
"                <div class=\"page d-flex justify-content-around \">\n";
        if(getNumPage() > 1){
            retour += "<div class=\"btn pagination-apj nav-btn \"><a href=" + getLien() + "?but=" + formLien + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + formu.getListeCritereString() + "><i class=\"fas fa-chevron-left\"></i></a></div>\n";
        }
        if(startPage > 1){
           retour += "<div class=\"btn pagination-apj\"><a href=" + getLien() + "?but=" + formLien + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf("1") + getApresLienPage() + formu.getListeCritereString() + ">1</a></div>\n";
           retour += "<div class=\"btn pagination-apj \">...</div>\n"; 
        }
        
        for(int i = startPage; i <= endPage; i++){
            String current = getNumPage() == i ? "current-page" : "";
            retour += "<div class=\"btn pagination-apj "+current+"\"><a href=" + getLien() + "?but=" + formLien + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(i) + getApresLienPage() + formu.getListeCritereString() + ">"+i+"</a></div>\n";  
        }
        if(endPage < getNombrePage()){
            retour += "<div class=\"btn pagination-apj \">...</div>\n";    
            retour += "<div class=\"btn pagination-apj \"><a href=" + getLien() + "?but=" + formLien + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNombrePage()) + getApresLienPage() + formu.getListeCritereString() + ">"+getNombrePage()+"</a></div>\n";
        }
        if(getNumPage() < getNombrePage()){   
            retour +="<div class=\"btn pagination-apj nav-btn \"><a href=" + getLien() + "?but=" + formLien + "&recap=" + getFormu().getRecapcheck() + "&premier=" + getPremier() + "&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + formu.getListeCritereString() + "><i class=\"fas fa-chevron-right\"></i></a></div>\n";
        }
        retour += "</div></div>";
        return retour;
    }
    
    public String getBouttonExport() throws Exception{
        String lang = String.valueOf(getReq().getSession().getAttribute("lang"));
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String tab = "";
        String tab1 = "";
        try {
            tab1 = this.getTableau().getHtmlPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String awhere = " AND " + CGenUtil.makeWhere(getBase()) + CGenUtil.makeWhereIntervalle(colInt, valInt) + this.getAWhere().replace('"', '\'');
        String temp = "";
        temp += "<div id='sourceDiv' class=\"export-block inactive\">\n" +
"                    <h4>Exporter les donn&eacute;es</h4>\n" +
"                    <hr>\n" +
"                       <form action=\"../download\" method=\"post\">"+
"                    <div class=\"form-group\">\n" +
"                        <label for=\"\">Format</label>\n" +
"                        <select name='ext' id='ext' class=\"form-control\">\n" +
"                           <option value='csv' style=\"background-image: url('../dist/img/csv_text.png')\" selected><img src='../dist/img/csv_text.png' alt=\"logo\">CSV</option>\n" +
"                           <option value='xls' style=\"background-image: url('../dist/img/file_xls.png')\"><img src='../dist/img/file_xls.png' alt=\"logo\">XLS</option>\n" +
"                           <option value='xml' style=\"background-image: url('../dist/img/file_xml.png')\"><img src='../dist/img/file_xml.png' alt=\"logo\">XML</option>\n" +
"                           <option value='pdf' style=\"background-image: url('../dist/img/file_pdf.png')\"><img src='../dist/img/file_pdf.png' alt=\"logo\">PDF</option>\n" +
"                        </select>\n" +
"                    </div>\n" +
"                    <div class=\"form-group\">\n" +
"                        <label for=\"\">Donn&eacute;es &agrave; exporter</label>\n" +
"                        <select name='donnee' id='donnee' class=\"form-control\">\n" +
"                           <option value='0' selected>"+RB.getString("pageencours")+"</option>\n" +
"                           <option value='1'>"+RB.getString("toutes")+"</option>\n" +
"                       </select>\n" +
"                    </div>\n" +
"                    <div class=\"form-group\">\n" +
"                        <label for=\"\">Orientation</label>\n" +
"                    </div>\n" +
"                    <select name='landscape' id='landscape' class=\"form-control\">\n" +
"                        <option value='0'>Portrait</option>\n" +
"                        <option value='1'>Paysage</option>\n" +
"                    </select>\n";
        
        if (this.getTableau().getExpxml() != null) {
            temp += "<input type=\"hidden\" name=\"xml\" value=\"" + this.getTableau().getExpxml().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"csv\" value=\"" + this.getTableau().getExpcsv().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"awhere\" value=\"" + awhere + "\" />";
            temp += "<input type=\"hidden\" name=\"table\" value=\"" + tab1.replace('"', '*').replace("\u0027", " ").replace("\"", " ") + "\" />";
            temp += "<input type=\"hidden\" name=\"entete\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibeEntete(), "", ";") + "\" />";
            temp += "<input type=\"hidden\" name=\"entetelibelle\" value=\"" + Utilitaire.tabToString(this.getTableau().getLibelleAffiche(), "", ";") + "\" />";
        }
        int sommeId = Utilitaire.estIlDedans("montant", getColSomme());
        if (sommeId > -1) {
            temp += "<input type=\"hidden\" name=\"nombreEncours\" value=\"" + Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getColSomme().length]) + "\" />";
            double test = rs.getSommeEtNombre()[sommeId];
            String testEnString = Utilitaire.enleverExponentielleDouble(test);
            temp += "<input type=\"hidden\" name=\"recap\" value=\"" + testEnString + "\" />";
        }
        
        temp += "<div class=\"form-group d-flex mt-2 justify-content-center justify-items-center\">";
        temp += "<input type='submit' value='" + RB.getString("exporter") + "' class='btn btn-search' />";
        temp += "<input type='hidden' name='titre' value='" + getTitre() + "' />";
        temp += "</div></form></div>";
        return temp;
    }


    public String getHtmlEnsemble() throws Exception{
        String temp = "";
        temp += "<div class=\"result mt-5 \">";
        temp += "<div class=\"export-container mb-1\">"+
        "<div class=\"btn btn-export \" onclick=\"exprt()\">\n" +
"                    <i class=\"fas fa-file-export\"></i> Exporter\n" +
"                </div>";
        temp += "<div id='destinationDiv'></div></div>";
        temp += getTableau().getHtmlAvecOrdre();
        temp += getPagination();
        temp += getBouttonExport();
        temp += "</div>";
        
        return temp;
    }
    
}
