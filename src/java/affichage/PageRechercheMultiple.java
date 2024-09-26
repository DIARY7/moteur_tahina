package affichage;

import javax.servlet.http.HttpServletRequest;
import bean.ListeColonneTable;
import java.util.Vector;
import java.lang.reflect.Field;
import java.sql.Connection;
import utilitaire.Utilitaire;
import bean.ClassMAPTable;

/**
 *
 * @deprecated
 * 
 * @author BICI
 * @version 1.0
 */
public class PageRechercheMultiple extends Page {

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
    private String[] tableauAfficheDefaut;
    private int nbTableauAffiche;
    private boolean premier = false;
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
        //makeCritere();
        //System.out.println("geeeeeee==="+getAWhere());
        String recap=getReq().getParameter("recap");
        critere.setNomTable(getBase().getNomTable());
        makeCritere();
        if(recap!=null && recap.compareToIgnoreCase("checked")==0)
        {
            recap="checked";
            this.getFormu().setRecapcheck(recap);
            rs = getUtilisateur().getDataPage(critere, getColInt(), getValInt(), getNumPage(), getAWhere(), getColSomme(), c, npp);
        }
        else
        {
            recap="";
            rs = getUtilisateur().getDataPageMaxSansRecap(critere, getColInt(), getValInt(), getNumPage(), getAWhere(), getColSomme(), c, npp);
        }        
        setListe((bean.ClassMAPTable[]) rs.getResultat());
    }

    public String getOrdre() {
        return ordre;
    }

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

        String temp = "";
        for (int i = 0; i < tempChamp.length; i++) {
            Field f = bean.CGenUtil.getField(getBase(), tempChamp[i].getNom());
            if (tempChamp[i].getEstIntervalle() == 1) {
                colIntv.add(tempChamp[i].getNom());
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "1"));
                valIntv.add(getParamSansNull(tempChamp[i].getNom() + "2"));
            } else if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                String valeurChamp = getReq().getParameter(utilitaire.Utilitaire.remplacerUnderscore(tempChamp[i].getNom()));
                if (valeurChamp != null && valeurChamp.compareTo("") != 0) {
                    // System.out.println(" getCritere() = " + getCritere() + " f.getName() " + f.getName() + " getParamSansNull(tempChamp[i].getNom()) " + getParamSansNull(tempChamp[i].getNom()));
                    bean.CGenUtil.setValChamp(getCritere(), f, getParamSansNull(tempChamp[i].getNom()));
                }
            } else {
                aWhere += " and " + f.getName() + " like '" + Utilitaire.getValeurNonNull(getParamSansNull(tempChamp[i].getNom())) + "'";
            }
        }
        colInt = new String[colIntv.size()];
        colIntv.copyInto(colInt);
        valInt = new String[valIntv.size()];
        valIntv.copyInto(valInt);
        if(getReq().getParameter("triCol")!=null){
            formu.getChamp("colonne").setValeur(getReq().getParameter("newcol"));
            if(getReq().getParameter("ordre").compareToIgnoreCase("")==0){
                formu.getChamp("ordre").setValeur("asc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " asc ");
            }else if(getReq().getParameter("ordre").compareToIgnoreCase("desc")==0){
                formu.getChamp("ordre").setValeur("asc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " asc ");
            }else{
                formu.getChamp("ordre").setValeur("desc");
                this.setOrdre(" order by " + getReq().getParameter("newcol") + " desc ");
            }
        }
        if (formu.getChamp("colonne").getValeur() != null && formu.getChamp("colonne").getValeur().compareToIgnoreCase("") != 0) {
            this.setOrdre(" order by " + formu.getChamp("colonne").getValeur() + " " + formu.getChamp("ordre").getValeur());
        } 
        aWhere += this.getOrdre();
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

        setTableau(new TableauRecherche(getListe(), libEntete, critereLienTab));

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

        String[] enteteAuto = getTableauAffiche();
        if (getTableauAffiche() == null) {
            setTableau(new TableauRecherche(getListe(), libEnteteDefaut, critereLienTab));
        } else {
            setTableau(new TableauRecherche(getListe(), enteteAuto, critereLienTab));
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
    public PageRechercheMultiple(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] tabAff, int nbAff) throws Exception {
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
        setNbTableauAffiche(nbAff);
        setTableauAfficheDefaut(tabAff);
        makeTableauAffiche();
        formu.makeChampTableauAffiche(getNbTableauAffiche(), tabAff);
    }
    /**
     * 
     * @param o objet de mapping
     * @param vrt liste des colonnes à mettre en champs de formulaire de recherche
     * @param listInterval liste des colonnes à mettre en intervalle sur le formulaire de recherche
     * @param nbRange nombre de rangé du formulaire de recherche
     * @throws Exception
     */
     public PageRechercheMultiple(bean.ClassMAPTable o,String[] vrt, String[] listInterval, int nbRange) throws Exception {
        setBase(o);
        //setTitre(titre);
        setCritere((bean.ClassMAPTable) (Class.forName(o.getClassName()).newInstance()));
        formu = new Formulaire();
        formu.setObjet(getBase());
        formu.makeChampFormu(vrt, listInterval, nbRange);
    }
    /**
     * Constructeur par défaut
     */
    public PageRechercheMultiple(){
    }
    /**
     * 
     * @param o objet de mapping
     * @param req contexte HTTP
     * @param vrt liste des colonnes à mettre en champs de formulaire de recherche
     * @param listInterval liste des colonnes à mettre en intervalle sur le formulaire de recherche
     * @param nbRange nombre de rangé du formulaire de recherche
     * @param champReturn champ du formulaire d'appel à utiliser pour les valeurs de retour
     * @throws Exception
     */
    public PageRechercheMultiple(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange) throws Exception {
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
        data[0][1] = utilitaire.Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[nbColSomme]);
        for (int i = 0; i < nbColSomme; i++) {
            data[0][i + 2] = utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[i]));
            entete[i + 2] = "Somme de " + String.valueOf(getColSomme()[i]);
        }
        TableauRecherche t = new TableauRecherche(data, entete);
        t.setTitre("RECAPITULATION");
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
    
    public boolean getPremier(){
        return premier;
    } 
    /**
     * 
     * @return html du bas de la page de recherche avec navigation de pagination 
     * et utilitaire d'exportation
     * 
     * @throws Exception
     */
    public String getBasPage() {
        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center width='100%'>";
        retour += "<tr><td height=25><b>Nombre de r&eacute;sultat :</b> " + utilitaire.Utilitaire.formaterSansVirgule(rs.getSommeEtNombre()[getNombreColonneSomme()]) + "</td><td align='right'><strong>page</strong> " + getNumPage() + " <b>sur</b> " + getNombrePage() + "</td>";
        retour += "</tr>";
        retour += "<tr>";
        retour += "<td width=50% valign='top' height=25>";
        if (getNumPage() > 1) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&recap="+getFormu().getRecapcheck() + "&premier="+ getPremier()+ "&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + formu.getListeCritereString() + ">&lt;&lt;Page pr&eacute;c&eacute;dente</a>";
        }
        retour += "</td>";
        retour += "<td width=50% align=right>";
        if (getNumPage() < getNombrePage()) {
            retour += "<a href=" + getLien() + "?but=" + getApres() + "&recap="+getFormu().getRecapcheck() + "&premier="+ getPremier()+"&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + formu.getListeCritereString() + ">Page suivante&gt;&gt;</a>";
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
        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center width='100%'>";
        retour += "<tr><td height=25><b>Nombre de r&eacute;sultat :</b> " + utilitaire.Utilitaire.formaterAr(String.valueOf(rs.getSommeEtNombre()[getNombreColonneSomme()])) + "</td><td align='right'><strong>page</strong> " + getNumPage() + " <b>sur</b> " + getNombrePage() + "</td>";
        retour += "</tr>";
        retour += "<tr>";
        retour += "<td width=50% valign='top' height=25>";
        if (getNumPage() > 1) {
            retour += "<a href=" + getApres() + "?a=1" + formu.getListeCritereString() + "&premier="+ getPremier()+"&numPag=" + String.valueOf(getNumPage() - 1) + getApresLienPage() + "&champReturn=etudiant>&lt;&lt;Page pr&eacute;c&eacute;dente</a>";
        }
        retour += "</td>";
        retour += "<td width=50% align=right>";
        if (getNumPage() < getNombrePage()) {
            retour += "<a href=" + getApres() + "?a=1" + formu.getListeCritereString() + "&premier="+ getPremier()+"&numPag=" + String.valueOf(getNumPage() + 1) + getApresLienPage() + "&champReturn=etudiant>Page suivante&gt;&gt;</a>";
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

    public String getAWhere() {
        return aWhere;
    }
    /**
     * 
     * @return requete SQL après la clause where pour filtrer le resultat
     */
    public void setAWhere(String aWhere) {
        this.aWhere = aWhere;
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
     /**
     * 
     * @return liste des colonnes à afficher sur la table de resultat
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

    public void setTableauAffiche(String[] tableauAffiche) {
        this.tableauAffiche = tableauAffiche;
    }
    /**
     * 
     * @return nombre de colonne à afficher
     */
    public int getNbTableauAffiche() {
        return nbTableauAffiche;
    }

    public void setNbTableauAffiche(int nbTableauAffiche) {
        this.nbTableauAffiche = nbTableauAffiche;
    }
    public void setPremier(boolean premier) {
        this.premier = premier;
    }

    public boolean isPremier() {
        return premier;
    }
    /**
     * Génerer le html d'export de données
     * @return html d'utilitaire d'export de données
     * @throws Exception
     */
    public String getDownloadForm() {
        String tab = "";
        String tab1 = "";
        try {
            tab = this.getTableau().getHtml();
            tab1 = this.getTableau().getHtmlPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-primary box-solid collapsed-box'>";
        temp += "<div class='box-header with-border'>";
        temp += "<h3 class='box-title'>Exporter</h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body'>";
        temp += "<form action=\"../download\" method=\"post\">";
        temp += "<table id='export'>";
        temp += "<tr>";
        temp += "<th colspan='4'><h3 class='box-title'>Format</h3></th>";
        temp += "<th colspan='2'><h3 class='box-title'>Donn&eacute;es &agrave; exporter</h3></th>";
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
        temp += "<label for='donnee'>Page En Cours </label>";
        temp += "<input type='radio' name='donnee' value=\"" + 0 + "\" checked='checked' />";
        temp += "</td>";
        temp += "<td>";
        temp += "<label for='donnee'>Toutes </label>";
        temp += "<input type='radio' name='donnee' value=\"" + 1 + "\" />";
        temp += "</td>";
        temp += "<td>";
        if (this.getTableau().getExpxml() != null) {
            temp += "<input type=\"hidden\" name=\"xml\" value=\"" + this.getTableau().getExpxml().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"csv\" value=\"" + this.getTableau().getExpcsv().replace('"', '*') + "\" />";
            temp += "<input type=\"hidden\" name=\"awhere\" value=\"" + this.getAWhere().replace('"', '\'') + "\" />";
            temp += "<input type=\"hidden\" name=\"table\" value=\"" + tab1.replace('"', '*') + "\" />";
        }
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
}
