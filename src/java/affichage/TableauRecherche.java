package affichage;

import bean.AdminGen;
import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ListeColonneTable;
import bean.ValeurEtiquette;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utilitaire.Utilitaire;
import java.net.URL;

/**
 * Classe representant une liste de résultat, peut-être simple avec checkbox ou radio bouton. 
 * Avec ou sans action.
 * 
 * <p>
 * Exemple d'utilisation: 
 * </p>
 * 
 * <pre>
 *     <%
 *	    String libEnteteAffiche[] = {"ID ", "Date ", "heure", "objet"};
 *	    pr.getTableau().setLibelleAffiche(libEnteteAffiche);
 *	    out.println(pr.getTableau().getHtml());
 *      %>
 * </pre>
 * @author BICI
 * @version 1.0
 */
public class TableauRecherche {

    private String[] libeEntete;
    private bean.ClassMAPTable[] data;
    private String cssEntete = "head";
    private String cssTableau = "table" + " " + "table-hover";
    private String html, htmlRecap;
    private String titre = "LISTE";
    private int[] propEntet;
    private String tailleTableau = "100%";
    private String[] libelleAffiche;
    private String[][] dataDirecte;
    private String[] lien;
    private String[] colonneLien;
    private String modelePage;
    private String[] attLien = null;
    private String[] valeurLien;
    private String expxml;
    private String expcsv;
    private String exppdf;
    private double[][] pourc;
    private String[] pourcentage;
    private String critereLienString = "";
    private boolean afficheBouttondevalider = false;
    private String ids;
    private String[] apresLienTab = null;
    Field[] listeField = null;
    private String[] urlLien;
    private String[] urlLienAffiche;
    private String nameBoutton = "Valider";
    private String nameActe = "attacher";
    private String nameBoutton2 = "";
    private String nameActe2 = "";
    private Map libelleEnteteAAffiche = new HashMap();
    
    private bean.ClassMAPTable[] dataM1;
    private List<String> colGroupe = null;
    private String colMoisMoins1;
    private String nomTable;
    
    private boolean groupe = false;
    private String lienFormulaire = "";
    
    private String[] colonneCacher = null;
    private Map<String,String> libelle_champ = new HashMap<String,String>();
    
    ValeurEtiquette[][] valeurEtiquette;
    
    private Formulaire formu;
    /**
     * 
     * @return formulaire associée au tableau
     */
    public Formulaire getFormu() {
        return formu;
    }

    public void setFormu(Formulaire formu) {
        this.formu = formu;
    }
    /**
     * construire les libellés à afficher de chaque champs
     * @param champ
     * @param libelle
     */
    public void construire_libelle(String[] champ,String[] libelle){
        for(int i = 0;i<champ.length;i++){
            libelle_champ.put(champ[i], libelle[i]);
        }
    }

    public Map<String, String> getLibelle_champ() {
        return libelle_champ;
    }

    public void setLibelle_champ(Map<String, String> libelle_champ) {
        this.libelle_champ = libelle_champ;
    }
    
    /**
     * 
     * @return liste des colonnes à ne pas afficher sur la table
     */
    public String[] getColonneCacher() {
        return colonneCacher;
    }

    public void setColonneCacher(String[] colonneCacher) {
        this.colonneCacher = colonneCacher;
    }
    
    
    /**
     * Verifier si un colonne est à afficher ou pas
     * @param colName nom de la colonne
     * @return vrai si colonne à ne pas afficher sinon faux
     */
    public boolean isColumnToHide(String colName){
        if(this.getColonneCacher() != null && this.getColonneCacher().length > 0){
            System.out.println("colName = " + colName);
            
            for(int i = 0; i < this.getColonneCacher().length; i++){
                String col = "Somme de "+this.getColonneCacher()[i];
                if(this.getColonneCacher()[i].compareToIgnoreCase(colName) == 0 || col.compareToIgnoreCase(colName) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public String getLienFormulaire() {
        return lienFormulaire;
    }

    public void setLienFormulaire(String lienFormulaire) {
        this.lienFormulaire = lienFormulaire;
    }

    public boolean isGroupe() {
        return groupe;
    }

    public void setGroupe(boolean groupe) {
        this.groupe = groupe;
    }

    public List<String> getColGroupe() {
        return colGroupe;
    }

    public void setColGroupe(List<String> colGroupe) {
        this.colGroupe = colGroupe;
    }

    public String getColMoisMoins1() {
        return colMoisMoins1;
    }

    public void setColMoisMoins1(String colMoisMoins1) {
        this.colMoisMoins1 = colMoisMoins1;
    }
    
    public ClassMAPTable[] getDataM1() {
        return dataM1;
    }

    public void setDataM1(ClassMAPTable[] dataM1) {
        this.dataM1 = dataM1;
    }

    public String getNameBoutton2() {
        return nameBoutton2;
    }

    public void setNameBoutton2(String nameBoutton2) {
        this.nameBoutton2 = nameBoutton2;
    }

    public String getNameActe2() {
        return nameActe2;
    }

    public void setNameActe2(String nameActe2) {
        this.nameActe2 = nameActe2;
    }

    public void setNameBoutton(String nameBoutton) {
        this.nameBoutton = nameBoutton;
    }

    public void setNameActe(String nameActe) {
        this.nameActe = nameActe;
    }

    public String[] getUrlLienAffiche() {
        return urlLienAffiche;
    }

    public void setUrlLienAffiche(String[] urlLienAffiche) {
        this.urlLienAffiche = urlLienAffiche;
    }

    public Field[] getListeField() {
        return listeField;
    }

    public void setListeField(Field[] listeField) {
        this.listeField = listeField;
    }

    public String[] getApresLienTab() {
        return apresLienTab;
    }

    public void setApresLienTab(String[] apresLienTab) {
        this.apresLienTab = apresLienTab;
    }
    /**
     * @return verifier si les boutons "Viser" et "Detacher" doivent être affichés
     */
    public boolean isAfficheBouttondevalider() {
        return afficheBouttondevalider;
    }
    /**
     * afficher les boutons "Viser" et "Detacher" à la fin de la liste si valeur true
     * @param afficheBouttondevalider
     */
    public void setAfficheBouttondevalider(boolean afficheBouttondevalider) {
        this.afficheBouttondevalider = afficheBouttondevalider;
    }

    public String getHtmlRecap() throws Exception {
        if (htmlRecap == null || htmlRecap.compareToIgnoreCase("") == 0) {
            this.makeHtmlRecap();
        }
        return htmlRecap;
    }

    public void setHtmlRecap(String htmlRecap) {
        this.htmlRecap = htmlRecap;
    }
    
    /**
     * 
     * @return lien de critère à utiliser pour chaque colonne de la table
     */

    public String getCritereLienString() {
        return critereLienString;
    }
    
    public void setCritereLienString(String critereLienString) {
        this.critereLienString = critereLienString;
    }
    /**
     * 
     * @param donne liste des resultats de recherches
     * @param lib liste des attributs à transformer en colonnes de la table
     * @param prop dimensionnement des colonnes en pourcentage
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String lib[], int[] prop) throws Exception {
        setData(donne);
        setLibeEntete(lib);
        setPropEntet(prop);
        transformerDataString();
    }
    /**
     * 
     * @param donne liste des resultats de recherches
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte) throws Exception {
        setData(donne);
        setLibeEntete(libEnte);
        transformerDataString();
    }
    /**
     * 
     * @param donne liste des resultats de recherches déjà transformé en tableau à 2 dimensions clé/valeur
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @throws Exception
     */
    public TableauRecherche(String[][] donne, String[] libEnte) throws Exception {
        setLibeEntete(libEnte);
        setDataDirecte(donne);
    }
    /**
     * 
     * @param donne liste des resultats de recherches
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @param critereLienTab lien de redirection après choix de colonne d'ordre
     * @param listFields liste des attributs de l'objet de mapping
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte, String critereLienTab, Field[] listFields) throws Exception {
        setData(donne);
        setListeField(listFields);
        setCritereLienString(critereLienTab);
        setLibeEntete(libEnte);
        transformerDataString();
    }
    /**
     * 
     * @param donne liste des resultats de recherches 
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @param critereLienTab lien de redirection après choix de colonne d'ordre 
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte, String critereLienTab) throws Exception {
        setData(donne);
        setCritereLienString(critereLienTab);
        setLibeEntete(libEnte);
        transformerDataString();
    }
    /**
     * 
     * @param donne liste des resultats de recherches
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @param critereLienTab lien de redirection après choix de colonne d'ordre
     * @param ids valeur par défaut des idées selectionnées sur le tableau
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte, String critereLienTab, String ids) throws Exception {
        setData(donne);
        setCritereLienString(critereLienTab);
        setLibeEntete(libEnte);
        setIds(ids);
        transformerDataString();
    }
    /**
     * @apiNote le setPourcentage n'est pas appelé implicitement
     * @param donne liste des resultats de recherches
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @param pourcentage liste des champs avec precision de pourcentage
     * @param pourc liste des valeurs de pourcentage pour colonne
     * @param critereLienTab  lien de redirection après choix de colonne d'ordre
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte, String[] pourcentage, double[][] pourc, String critereLienTab) throws Exception {
        setData(donne);
        setPourc(pourc);
        //setPourcentage(pourcentage);
        setLibeEntete(libEnte);
        setCritereLienString(critereLienTab);
        transformerDataString();
    }
    /**
     * 
     * @param donne liste des resultats de recherches
     * @param libEnte liste des attributs à transformer en colonnes de la table
     * @param pourcentage liste des champs avec precision de pourcentage
     * @param pourc liste des valeurs de pourcentage pour colonne
     * @param critereLienTab lien de redirection après choix de colonne d'ordre
     * @param listFields liste des attributs de l'objet de mapping
     * @throws Exception
     */
    public TableauRecherche(bean.ClassMAPTable[] donne, String[] libEnte, String[] pourcentage, double[][] pourc, String critereLienTab, Field[] listFields) throws Exception {
        setData(donne);
        setPourc(pourc);
        setListeField(listFields);
        //setPourcentage(pourcentage);
        setLibeEntete(libEnte);
        setCritereLienString(critereLienTab);
        transformerDataString();
    }
    /**
     * Générer un HTML(tableau) à partir des resultats de recherche donnés
     * @throws Exception
     */
    public void makeHtml() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
//        if(data != null) {
//            ClassMAPTable c = data[0].getClass().getConstructor(null).newInstance(null);
//            c.setNomTable(getNomTable());
//            dataM1 = (ClassMAPTable[]) CGenUtil.rechercher(c, null, null, "");
//            System.out.println("cls = " + dataM1.length);
//        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"row\">";
        temp += "<div class=\"row col-md-12\">";
        temp += "<div class=\"box box-solid\">";
        temp += "<div class=\"box-header\">";
        temp += "<h3 class=\"box-title\" align=\"center\">" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class=\"box-body table-responsive no-padding\">";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"0\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-hover table-bordered\">";
        temp += "<thead>";
        temp += "<tr class=\"" + getCssEntete() + "\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;

        Field[] listeFields = getListeField();

        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            String alignement = "center";

            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                String libelle = "";
                /*if(getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null){
                    libelle = getLibelle_champ().get(getLibelleAffiche()[i]);
                    System.out.println("----------- KKK ---------- : ato am IF");
                }else{
                     System.out.println("----------- KKK ---------- : ato am ELSE");
                    libelle = (String)getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                }*/
                ///System.out.println("---------------------->"+getLibelle_champ().get(getLibelleAffiche()[i]));
                if(getLibelle_champ().get(getLibelleAffiche()[i]) != null){
                    libelle = getLibelle_champ().get(getLibelleAffiche()[i]);
                    //System.out.println("----------- KKK ---------- : ato am IF");
                }else{
                     //System.out.println("----------- KKK ---------- : ato am ELSE");
                    libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                }
                    
                    
                    
//                String libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                if(this.getColGroupe() != null &&  this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#103a8e\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " style='color:white;'>" + libelle + " m-1</a></th>";
                }
                temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#103a8e\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " style='color:white;'>" + libelle + "</a></th>";
            } else {
                if(this.getColGroupe() != null && this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#ffffff\">" + getLibeEntete()[i] + " m-1</th>";
                }
                temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#ffffff\">" + getLibeEntete()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {

            temp += "<tr onmouseover=\"this.style.backgroundColor='#EAEAEA'\" onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>";
            int nombreLien = 0, j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) {
                String alignement = "left";
                if (listeFields != null) {
                    for (int k = 0; k < listeFields.length; k++) {
                        if (listeFields[k].getName().compareToIgnoreCase(getLibeEntete()[j]) == 0) {
                            if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("String") == 0) {
                                alignement = "left";
                                break;
                            } else if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("Double") == 0) {
                                alignement = "right";
                                break;
                            } else {
                                alignement = "center";
                                break;
                            }
                        }
                    }
                }
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());

                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    String lienAutre = "";
                    if (this.isGroupe()) {
                        for (int g = 0; g < getLibeEntete().length - 1; g++) {
                            lienAutre += getLibeEntete()[g] + "=" + getDataDirecte()[i][g] + "&";
                        }
                        lienAutre += getLibeEntete()[getLibeEntete().length - 1] + "=" + getDataDirecte()[i][getLibeEntete().length - 1]+"&"+getLienFormulaire();
                    }else{
                        lienAutre = colLien + "=" + valL;
                    }

                    lien = "<a href='" + getLien()[numeroColonne] + "&" + lienAutre + rajoutLien + "'>";
                    apresLien = "</a>";
                }
                boolean test = false;
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if(this.getColGroupe() != null && this.getColGroupe().contains(this.getLibeEntete()[j])) {
                    /*Date date = (Date) CGenUtil.getValeurFieldByMethod(data[i], this.getColMoisMoins1());
                    System.out.println("date = " + date);
                    Date dateM1 = Utilitaire.ajoutMoisDate(date, -1);
                    String[] colDate = {this.getColMoisMoins1()};
                    String[] valDate = {dateM1.toString()};
                    System.out.println("valDate[0] = " + valDate[0]);
                    ClassMAPTable[] cmt = AdminGen.find(this.getDataM1(), colDate, valDate);
//                    System.out.println("cmt = " + cmt.length);
                    String o = "0";
                    if(cmt.length != 0) {
                        o = CGenUtil.getValeurFieldByMethod(cmt[0], this.getLibeEntete()[j]).toString();
                    }
                    System.out.println("o = " + o);
                    temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.formaterAr(Utilitaire.champNull(o)) + apresLien + " </td>";
//                    temp += "<td>Magie </td>";*/
                }
                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {

                   // System.out.println("getLibeEntete()[j]) ==== " + getLibeEntete()[j] + " === ");
                    if(isColumnToHide(getLibeEntete()[j])){
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align='center' >" + lien + "-"+" " + apresLien + " </td>";
                    } else {
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\" >" + lien + Utilitaire.champNull(getDataDirecte()[i][j])+" " + apresLien + " </td>";
                    }
                    
                }

                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }
    
    public void makeHtmlAvecOrdre() throws Exception{
        String temp = "";
        
         if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"table-result\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"0\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-sortable \">";
        temp += "<thead>";
        temp += "<tr class=\"table-header\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;

        Field[] listeFields = getListeField();

        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            String ordre = "";
            String colonneOrdre = "";
            String[] params = getCritereLienString().split("&");
            if(params.length > 0){
                for(int j=0 ; j<params.length; j++){
                    System.out.println("params[j] "+params[j]);
                    if(params[j].contains("ordre")){
                        ordre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }if(params[j].contains("colonne")){
                        colonneOrdre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }
                }
            }
            String alignement = "center";
            
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                String libelle = "";
                if(getLibelle_champ().get(getLibelleAffiche()[i]) != null){
                    libelle = getLibelle_champ().get(getLibelleAffiche()[i]);
                }else{
                    libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                }

                if(this.getColGroupe() != null &&  this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span> m-1</a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr; m-1</a></th>";
                    }
                    else{
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr; m-1</a></th>";
                    }
                    temp += entete;
                }
                String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span></a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr;</a></th>";
                    }
                    else{
                        entete = "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr;</a></th>";
                    }
                    temp += entete;
            } else {
                if(this.getColGroupe() != null && this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + " m-1</th>";
                }
                temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        temp += "</thead>";
        temp += "<tbody>\n";
        
        for (int i = 0; i < nombreLigne; i++) {

            temp += "<tr onmouseover=\"this.style.backgroundColor='#EAEAEA'\" onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>";
            int nombreLien = 0, j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) {
                String alignement = "left";
                if (listeFields != null) {
                    for (int k = 0; k < listeFields.length; k++) {
                        if (listeFields[k].getName().compareToIgnoreCase(getLibeEntete()[j]) == 0) {
                            if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("String") == 0) {
                                alignement = "left";
                                break;
                            } else if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("Double") == 0) {
                                alignement = "right";
                                break;
                            } else {
                                alignement = "center";
                                break;
                            }
                        }
                    }
                }
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());

                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    String lienAutre = "";
                    if (this.isGroupe()) {
                        for (int g = 0; g < getLibeEntete().length - 1; g++) {
                            lienAutre += getLibeEntete()[g] + "=" + getDataDirecte()[i][g] + "&";
                        }
                        lienAutre += getLibeEntete()[getLibeEntete().length - 1] + "=" + getDataDirecte()[i][getLibeEntete().length - 1]+"&"+getLienFormulaire();
                    }else{
                        lienAutre = colLien + "=" + valL;
                    }

                    lien = "<a href='" + getLien()[numeroColonne] + "&" + lienAutre + rajoutLien + "'>";
                    apresLien = "</a>";
                }
                boolean test = false;
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }

                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {
                    if(isColumnToHide(getLibeEntete()[j])){
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align='center' >" + lien + "-"+" " + apresLien + " </td>";
                    } else {
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\" >" + lien + Utilitaire.champNull(getDataDirecte()[i][j])+" " + apresLien + " </td>";
                    }
                }

                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        
        temp += "</tbody>\n" +
                "</table>\n" +
                "</div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }
    
    public void makeHtmlRecap() throws Exception{
        String temp = "";
        
         if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        temp += "<div class=\"recap-container d-flex\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;

        nombreColonne = getDataDirecte()[0].length;     
        for (int p = 0; p < nombreLigne; p++) {
            for (int j = 0; j < nombreColonne; j++) {
                int nbrColMd = 12 / (nombreColonne - 1);
                if(nbrColMd <= 1) nbrColMd = 1;
                if(nbrColMd >= 12) nbrColMd = 10;
                String classe = "'col-md-"+nbrColMd+" recap p-3'";
                if(getLibeEntete()[j].compareToIgnoreCase("") != 0 && getDataDirecte()[p][j].compareToIgnoreCase("Total") != 0){
                    temp += "<div class="+classe+">";
                    temp += "<div class=\"recap-header\">"+getLibeEntete()[j]+"</div>";
                    temp += "<div class='recap-data'>"+Utilitaire.champNull(getDataDirecte()[p][j])+"</div>";
                    temp += "</div>";
                }
            }

        }
        
        temp += "</div>";
        setHtmlRecap(temp);
    }
    /**
     * @deprecated
     * @param recap si il y a un recap
     * @param recap1 valeur somme montant
     * @param recap2 valeur crédit
     * @throws Exception
     */
    public void makeHtml(boolean recap, String recap1, String recap2) throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"row\">";
        temp += "<div class=\"row col-md-12\">";
        temp += "<div class=\"box\">";
        temp += "<div class=\"box-header\">";
        temp += "<h3 class=\"box-title\" align=\"center\">" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class=\"box-body table-responsive no-padding\">";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"1\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-hover table-bordered\">";
        temp += "<thead>";

        if (recap) {
            //System.out.println("******** MISY RECAP");
            temp += "<tr class=\"" + getCssEntete() + "\">";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th>Somme montant</th>";
            temp += "<th>" + recap2 + "</th>";
            temp += "<th>Somme credit</th>";
            temp += "<th>" + recap2 + "</th>";
            temp += "</tr>";
        }

        temp += "<tr class=\"" + getCssEntete() + "\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;

        Field[] listeFields = getListeField();

        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            String alignement = "center";

            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                String libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#103a8e\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " style='color:white;'>" + libelle + "</a></th>";
            } else {
                temp += "<th width=\"" + getPropEntet()[i] + "%\" align=\"" + alignement + "\" valign=\"top\" style=\"background-color:#103a8e\">" + getLibeEntete()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {

            temp += "<tr onmouseover=\"this.style.backgroundColor='#EAEAEA'\" onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>";
            int nombreLien = 0, j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) {
                String alignement = "left";
                if (listeFields != null) {
                    for (int k = 0; k < listeFields.length; k++) {
                        if (listeFields[k].getName().compareToIgnoreCase(getLibeEntete()[j]) == 0) {
                            if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("String") == 0) {
                                alignement = "left";
                                break;
                            } else if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("Double") == 0) {
                                alignement = "right";
                                break;
                            } else {
                                alignement = "center";
                                break;
                            }
                        }
                    }
                }
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());

                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + rajoutLien + "'>";
                    apresLien = "</a>";
                }
                boolean test = false;
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {

                    if(isColumnToHide(getLibeEntete()[j])){
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align='center' >" + lien + "-" + apresLien + " </td>";
                    } else {
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\" >" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + apresLien + " </td>";
                    }
                }
                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }
    /**
     * génerer table html destiné pour les pdfs
     * @throws Exception
     */
    public void makeHtmlPDF() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";

        temp += "<table align='center' border='1'>";
        temp += "<thead>";
        temp += "<tr>";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<th style=\"font-size:9px\">" + getLibelleAffiche()[i] + "</th>";
        }
        temp += "</tr>";

        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr>";
            int nombreLien = 0, j = 0;
            for (j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "";
                    apresLien = "";
                }
                boolean test = false;
                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td align='center' style=\"font-size:9px\">" + lien + getDataDirecte()[i][j] + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {
                    //if (Utilitaire.isStringNumeric(getDataDirecte()[i][j])) {
                    temp += "<td align='center' style=\"font-size:9px\">" + lien + getDataDirecte()[i][j] + apresLien + " </td>";

                }

            }

            temp += "</tr>";
        }
        temp += "</tbody>";
        temp += "</table>";
        //temp+=" <p> "+tempxml+" </p>";
        setHtml(temp);

    }
    /**
     * génerer table html destiné pour les pdfs 
     * @param recap recap existant
     * @param v1 somme montant
     * @param v2 somme credit
     * @throws Exception
     */
    public void makeHtmlPDF(boolean recap, String v1, String v2) throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";

        temp += "<table align='center' border='1'>";
        temp += "<thead>";
        if (recap) {
            System.out.println("******** MISY RECAP");
            temp += "<tr>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th></th>";
            temp += "<th>Somme montant</th>";
            temp += "<th>" + v1 + "</th>";
            temp += "<th>Somme credit</th>";
            temp += "<th>" + v2 + "</th>";
            temp += "</tr>";
        }
        temp += "<tr>";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<th style=\"font-size:9px\">" + getLibelleAffiche()[i] + "</th>";
        }
        temp += "</tr>";

        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr>";
            int nombreLien = 0, j = 0;
            for (j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "";
                    apresLien = "";
                }
                boolean test = false;
                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td align='center' style=\"font-size:9px\">" + lien + getDataDirecte()[i][j] + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {
                    //if (Utilitaire.isStringNumeric(getDataDirecte()[i][j])) {
                    temp += "<td align='center' style=\"font-size:9px\">" + lien + getDataDirecte()[i][j] + apresLien + " </td>";

                }

            }

            temp += "</tr>";
        }
        temp += "</tbody>";
        temp += "</table>";
        //temp+=" <p> "+tempxml+" </p>";
        setHtml(temp);

    }
    /**
     * Ajouter des paramètres supplémentaire au lien des colonnes de la table
     * @param e objet de mapping
     * @param lien liste de colonnes
     * @param lienAffiche valeur des colonnes
     * @return
     */
    public String getRajoutLienMultiple(bean.ClassMAPTable e, String lien, String lienAffiche) {
        String retour = "";
        if (lien != null || lien.compareTo("") != 0) {
            String[] g = Utilitaire.split(lien, "-");
            String[] affiche = Utilitaire.split(lienAffiche, "-");
            Field[] lf = getListeField();
            for (int i = 0; i < g.length; i++) {
                String valeur = e.getValInsert(g[i]);
                for (int k = 0; k < lf.length; k++) {
                    if (lf[k].getName().compareTo(g[i]) == 0) {
                        if (lf[k].getType().getSimpleName().compareToIgnoreCase("Date") == 0) {
                            valeur = Utilitaire.convertDatyFormtoRealDatyFormat(valeur);
                        }
                        break;
                    }
                }
                if (lienAffiche != null) {
                    retour = retour + "&" + affiche[i] + "=" + valeur;
                } else {
                    retour = retour + "&" + g[i] + "=" + valeur;
                }
            }
        }
        return retour;
    }
    /**
     * Transformer la liste d'objet en un tableau de chaine de caractères à deux dimensions
     * pour éviter de faire du reflect à chaque lecture
     * @throws Exception
     */
    public void transformerDataString() throws Exception {
        Object valeurC = null;
        dataDirecte = new String[getData().length][getLibeEntete().length];
        for (int i = 0; i < getData().length; i++) {
            int j = 0;
            for (j = 0; j < getLibeEntete().length; j++) 
            {
                bean.Champ c = ListeColonneTable.getChamp(data[i], getLibeEntete()[j]);
                valeurC = CGenUtil.getValeurFieldByMethod(data[i], getLibeEntete()[j]);
                if (valeurC == null && c == null) {
                    dataDirecte[i][j] = "";
                    continue;
                }
                else if(valeurC!=null&&c==null)
                {
                    dataDirecte[i][j]=String.valueOf(valeurC);
                    if(valeurC instanceof Number) dataDirecte[i][j]=utilitaire.Utilitaire.formaterAr(String.valueOf(valeurC));
                }
                else if (c.getTypeJava().compareToIgnoreCase("double") == 0) {
                    dataDirecte[i][j] = utilitaire.Utilitaire.formaterAr(String.valueOf(valeurC));
                } else if (c.getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {
                    dataDirecte[i][j] = Utilitaire.formatterDaty((java.sql.Date) (valeurC));
                } else {
                    dataDirecte[i][j] = String.valueOf(valeurC);
                }
            }

            //valeurC = CGenUtil.getValeurFieldByMethod(data[i], "nombrepargroupe");
            //dataDirecte[i][j-1] = String.valueOf(valeurC);
        }

    }
    /**
     * 
     * @return liste des colonnes à afficher sur la table
     */
    public String[] getLibeEntete() {
        return libeEntete;
    }

    public void setLibeEntete(String[] libeEntete) {
        this.libeEntete = libeEntete;
    }

    public void setData(bean.ClassMAPTable[] data) {
        this.data = data;
    }
    /**
     * 
     * @return valeurs brutes des tables
     */
    public bean.ClassMAPTable[] getData() {
        return data;
    }
    
    public void setCssEntete(String cssEntete) {
        this.cssEntete = cssEntete;
    }

    
    public String getCssEntete() {
        return cssEntete;
    }

    public void setCssTableau(String cssTableau) {
        this.cssTableau = cssTableau;
    }

    public String getCssTableau() {
        return cssTableau;
    }
    /**
     * 
     * @param mettre le html representant la table
     */
    public void setHtml(String html) {
        this.html = html;
    }
    /**
     * retourne le html de la table sinon géneration  d'un html de base
     * @return html de la table
     * @throws Exception
     */
    public String getHtml() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtml();
        }
        return html;
    }
    
    public String getHtmlAvecOrdre() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlAvecOrdre();
        }
        return html;
    }

    /**
     * ceci crée automatiquement le html de pdf
     * @return html destiné pour pdf
     * @throws Exception
     */

    public String getHtmlPDF() throws Exception {
        this.makeHtmlPDF();
        return html;
    }
    /**
     * ceci crée automatiquement le html de pdf
     * @return html destiné pour pdf
     * @throws Exception
     */
    public String getHtmlPDF(boolean recap, String v1, String v2) throws Exception {
        this.makeHtml(recap, v1, v2);
        return html;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * 
     * @return titre de la table
     */
    public String getTitre() {
        return titre;
    }

    public void setPropEntet(int[] propEntet) {
        this.propEntet = propEntet;
    }
    /**
     * Si valeur de prop entete vide, génere une proportion égale des colonnes
     * @return liste des proportions des colonnes
     */
    public int[] getPropEntet() {
        if (propEntet == null) {
            int[] ret = new int[getLibeEntete().length];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = 100 / ret.length;
            }
            return ret;
        }
        return propEntet;
    }

    public void setTailleTableau(String tailleTableau) {
        this.tailleTableau = tailleTableau;
    }
    /**
     * 
     * @return taille de la table
     */
    public String getTailleTableau() {
        return tailleTableau;
    }

    /**
     * mettre à jour la liste des colonnes à afficher suite à formulaire de choix de colonnes
     * @param libelleAffiche
     */
    public void setLibelleAffiche(String[] libelleAffiche) {
        this.libelleAffiche = libelleAffiche;
    }
    /**
     * 
     * @return 
     */
    public String[] getLibelleAffiche() {
        if (libelleAffiche == null) {
            return getLibeEntete();
        }
        return libelleAffiche;
    }

    public void setDataDirecte(String[][] dataDirecte) {
        this.dataDirecte = dataDirecte;
    }
    /**
     * 
     * @return données de la liste transformées en tableaux deux dimensions
     */
    public String[][] getDataDirecte() {
        return dataDirecte;
    }

    public void setLien(String[] lien) {
        this.lien = lien;
    }

    public String[] getLien() {
        return lien;
    }

    public String[] getUrlLien() {
        return urlLien;
    }
    /**
     * 
     * @param listeLien valeur des liens pour chaque colonne
     */
    public void setUrlLien(String[] listeLien) {
        urlLien = listeLien;
    }
    /**
     * 
     * @param colonneLien colonnes avec lien
     */
    public void setColonneLien(String[] colonneLien) {
        this.colonneLien = colonneLien;
    }

    public String[] getColonneLien() {
        return colonneLien;
    }

    public void setModelePage(String modelePage) {
        this.modelePage = modelePage;
    }

    public String getModelePage() {
        return modelePage;
    }

    public void setAttLien(String[] attLien) {
        this.attLien = attLien;
    }

    public String[] getAttLien() {
        return attLien;
    }

    public void setValeurLien(String[] valeurLien) {
        this.valeurLien = valeurLien;
    }

    public String[] getValeurLien() {
        return valeurLien;
    }
    /**
     * 
     * @return données à utiliser pour l'export xml
     */
    public String getExpxml() {
        return expxml;
    }

    public void setExpxml(String s) {
        expxml = s;
    }

    /**
     * 
     * @return données à utiliser pour l'export csv
     */

    public String getExpcsv() {
        return expcsv;
    }

    public void setExpcsv(String s) {
        expcsv = s;
    }

    /**
     * 
     * @return données à utiliser pour l'export pdf
     */
    public String getExppdf() {
        return exppdf;
    }

    public void setExppdf(String s) {
        exppdf = s;
    }
    /**
     * construire le html de la table avec support de radio button
     * @throws Exception
     */
    public void makeHtmlWithRadioButton() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        temp += "<p align=center><strong><u>" + getTitre() + "</u></strong></p>";
        temp += "<div id=\"divchck\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class=" + getCssTableau() + ">";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<td align=center valign=top></td>";
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<td width=" + getPropEntet()[i] + "% align=center valign=top>" + getLibelleAffiche()[i] + "</td>";
        }
        temp += "</tr>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";

            temp += "<td align=center>";
            //System.out.println("dataaaaa : "+data[i].getValColLibelle()+" data i: "+data[i]);
            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {
                String temps = data[i].getValColLibelle();
                if (temps != null && temps.contains("'")) {
                    temps = temps.replaceAll("'", "&apos;");
                }
                if (temps != null && temps.contains("\"")) {
                    temps = temps.replaceAll("\"", " ");
                }
                temp += "<input type='radio' value='" + getDataDirecte()[i][0] + ";" + temps + "' name='choix' onMouseDown='getChoix()' id='choix' class='radio'/>";

            } else {
                temp += "<input type='radio' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' onMouseDown='getChoix()' id='choix' class='radio'/>";
            }
            temp += "</td>";

            int nombreLien = 0;
            for (int j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {

                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (j == 1) {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + ">" + getDataDirecte()[i][j] + "</td>";
                } else {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + ">" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                }
            }

            temp += "</tr>";
        }
        temp += "</table>";
        temp += "</div>";
        setHtml(temp);
    }
    
    public void makeHtmlWithRadioButtonNew() throws Exception{
        String temp = "";
        
         if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        temp += "<div class=\"result p-3 mt-3 \">";
        
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"export-container mb-1\">"+
                    "<div class=\"btn btn-export \" onclick=\"exprt()\">\n" +
"                    <i class=\"fas fa-file-export\"></i> Exporter\n" +
"                </div>";
        temp += "<div id='destinationDiv'></div></div>";
                    
        temp += "<div class=\"table-result\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"0\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-sortable \">";
        temp += "<thead>";
        temp += "<tr class=\"table-header\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        temp += "<th align=center></td>";
        Field[] listeFields = getListeField();

        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            String ordre = "";
            String colonneOrdre = "";
            String[] params = getCritereLienString().split("&");
            if(params.length > 0){
                for(int j=0 ; j<params.length; j++){
                    System.out.println("params[j] "+params[j]);
                    if(params[j].contains("ordre")){
                        ordre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }if(params[j].contains("colonne")){
                        colonneOrdre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }
                }
            }
            String alignement = "";
            
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                String libelle = "";
                if(getLibelle_champ().get(getLibelleAffiche()[i]) != null){
                    libelle = getLibelle_champ().get(getLibelleAffiche()[i]);
                }else{
                    libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                }

                if(this.getColGroupe() != null &&  this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span> m-1</a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr; m-1</a></th>";
                    }
                    else{
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr; m-1</a></th>";
                    }
                    temp += entete;
                }
                String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span></a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr;</a></th>";
                    }
                    else{
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr;</a></th>";
                    }
                    temp += entete;
            } else {
                if(this.getColGroupe() != null && this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    temp += "<th align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + " m-1</th>";
                }
                temp += "<th  align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        temp += "</thead>";
        temp += "<tbody>\n";
        
        for (int i = 0; i < nombreLigne; i++) {

            temp += "<tr onmouseover=\"this.style.backgroundColor='#EAEAEA'\" onmouseout=\"this.style.backgroundColor=''\">";
            temp += "<td align=center>";
            //System.out.println("dataaaaa : "+data[i].getValColLibelle()+" data i: "+data[i]);
            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {
                String temps = data[i].getValColLibelle();
                if (temps != null && temps.contains("'")) {
                    temps = temps.replaceAll("'", "&apos;");
                }
                if (temps != null && temps.contains("\"")) {
                    temps = temps.replaceAll("\"", " ");
                }
                temp += "<input type='radio' value='" + getDataDirecte()[i][0] + ";" + temps + "' name='choix' onMouseDown='getChoix()' id='choix' class='radio'/>";

            } else {
                temp += "<input type='radio' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' onMouseDown='getChoix()' id='choix' class='radio'/>";
            }
            temp += "</td>";
            tempxml += "<row>";
            int nombreLien = 0, j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) {
                String alignement = "left";
                if (listeFields != null) {
                    for (int k = 0; k < listeFields.length; k++) {
                        if (listeFields[k].getName().compareToIgnoreCase(getLibeEntete()[j]) == 0) {
                            if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("String") == 0) {
                                alignement = "left";
                                break;
                            } else if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("Double") == 0) {
                                alignement = "right";
                                break;
                            } else {
                                alignement = "center";
                                break;
                            }
                        }
                    }
                }
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());

                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    String lienAutre = "";
                    if (this.isGroupe()) {
                        for (int g = 0; g < getLibeEntete().length - 1; g++) {
                            lienAutre += getLibeEntete()[g] + "=" + getDataDirecte()[i][g] + "&";
                        }
                        lienAutre += getLibeEntete()[getLibeEntete().length - 1] + "=" + getDataDirecte()[i][getLibeEntete().length - 1]+"&"+getLienFormulaire();
                    }else{
                        lienAutre = colLien + "=" + valL;
                    }

                    lien = "<a href='" + getLien()[numeroColonne] + "&" + lienAutre + rajoutLien + "'>";
                    apresLien = "</a>";
                }
                boolean test = false;
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }

                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {
                    if(isColumnToHide(getLibeEntete()[j])){
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align='center' >" + lien + "-"+" " + apresLien + " </td>";
                    } else {
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\" >" + lien + Utilitaire.champNull(getDataDirecte()[i][j])+" " + apresLien + " </td>";
                    }
                }

                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        
        temp += "</tbody>\n" +
                "</table>\n" +
                "</div></div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }
    /**
     * construire le html de la table avec support de checkbox 
     * pour action avec bouton "Valider"
     * @throws Exception
     */
    public void makeHtmlWithMultipleCheckbox() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        temp += "<p align=center><strong><u>" + getTitre() + "</u></strong></p>";
        temp += "<div id=\"divchck\" class='table-responsive'>";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class=" + getCssTableau() + ">";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<td align=center valign=top></td>";
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<td align=center valign=top>" + getLibelleAffiche()[i] + "</td>";
        }
        temp += "</tr>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";

            //temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/></td>";
            temp += "<td align=center>";

            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {

                temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox'/>";

            } else {
                temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/>";
            }
            temp += "</td>";

            int nombreLien = 0;
            for (int j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {

                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (j == 1) {
                    temp += "<td align=" + alignement + ">" + getDataDirecte()[i][j] + "</td>";
                } else {
                    temp += "<td align=" + alignement + ">" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                }
            }

            temp += "</tr>";
        }
        temp += "</table>";
        temp += "</div>";
        temp += "<input class=\"btn btn-success\" type=\"submit\" value=\"Valider\" />";
        setHtml(temp);
    }
    /**
     * construire le html de la table avec support de checkbox déjà tous coché
     * pour action avec bouton "Valider"
     * @throws Exception
     */
    public void makeHtmlWithMultipleCheckboxTous() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        temp += "<p align=center><strong><u>" + getTitre() + "</u></strong></p>";
        temp += "<div id=\"divchck\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class=" + getCssTableau() + ">";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<td align=center valign=top></td>";
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<td width=" + getPropEntet()[i] + "% align=center valign=top>" + getLibelleAffiche()[i] + "</td>";
        }
        temp += "</tr>";
        temp += "<th align=center valign=top style='background-color:#bed1dd'><input onclick=\"CocheToutCheckbox(this, 'choix')\" type=\"checkbox\"></th>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";

            //temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/></td>";
            temp += "<td align=center>";

            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {

                temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox'/>";

            } else {
                temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/>";
            }
            temp += "</td>";

            int nombreLien = 0;
            for (int j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {

                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (j == 1) {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + "><input type='text' readonly=true style='border:none;text-align:center;' name='idEmp" + i + "' value='" + getDataDirecte()[i][j] + "'></td>";
                } else {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + ">" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                }
            }

            temp += "</tr>";
        }
        temp += "</table>";
        temp += "<input class=\"btn btn-success\" type=\"submit\" value=\"Valider\" />";
        temp += "</div>";
        setHtml(temp);
    }
    /**
     * construire le html de la table avec support de checkbox et retient des choix déjà réalisés
     * pour action avec bouton "Valider" 
     * @param choix liste des choix déjà réalisés
     * @throws Exception
     */
    public void makeHtmlWithMultipleCheckboxRetientChoix(String[] choix) throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        temp += "<p align=center><strong><u>" + getTitre() + "</u></strong></p>";
        temp += "<div id=\"divchck\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class=" + getCssTableau() + ">";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<td align=center valign=top></td>";
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<td width=" + getPropEntet()[i] + "% align=center valign=top>" + getLibelleAffiche()[i] + "</td>";
        }
        temp += "</tr>";

        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";

            //temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/></td>";
            temp += "<td align=center>";

            int dansChoix = Utilitaire.estIlDedans(getDataDirecte()[i][0], choix);
            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {
                if (dansChoix != -1) {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox' checked/>";
                } else {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox'/>";
                }
            } else {
                if (dansChoix != -1) {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox' checked/>";
                } else {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/>";
                }
            }
            temp += "</td>";

            int nombreLien = 0;
            for (int j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {

                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (j == 1) {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + "><input type='text' readonly=true style='border:none;text-align:center;' name='idEmp" + i + "' value='" + getDataDirecte()[i][j] + "'></td>";
                } else {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + ">" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                }
            }

            temp += "</tr>";
        }
        temp += "</table>";
        temp += "<input class=\"btn btn-success\" type=\"submit\" value=\"Valider\" />";
        temp += "</div>";
        setHtml(temp);
    }
    /**
     * construire le html de la table avec support de checkbox et retient des choix déjà réalisés
     * pour action avec bouton "Valider" 
     * @param choix liste des choix déjà réalisés
     * @throws Exception
     */
    public void makeHtmlWithMultipleCheckboxRetientChoixTous(String[] choix) throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        temp += "<p align=center><strong><u>" + getTitre() + "</u></strong></p>";
        temp += "<div id=\"divchck\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class=" + getCssTableau() + ">";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<td align=center valign=top></td>";
        for (int i = 0; i < nombreColonne; i++) {
            temp += "<td width=" + getPropEntet()[i] + "% align=center valign=top>" + getLibelleAffiche()[i] + "</td>";
        }
        temp += "</tr>";
        temp += "<th align=center valign=top style='background-color:#bed1dd'><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\"></th>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";

            //temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/></td>";
            temp += "<td align=center>";

            int dansChoix = Utilitaire.estIlDedans(getDataDirecte()[i][0], choix);
            if (data[i].getValColLibelle() != null && data[i].getValColLibelle().compareToIgnoreCase("") != 0) {
                if (dansChoix != -1) {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox' checked/>";
                } else {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + data[i].getValColLibelle() + "' name='choix' id='choix' class='checkbox'/>";
                }
            } else {
                if (dansChoix != -1) {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox' checked/>";
                } else {
                    temp += "<input type='checkbox' value='" + getDataDirecte()[i][0] + ";" + getDataDirecte()[i][0] + "' name='choix' id='choix' class='checkbox'/>";
                }
            }
            temp += "</td>";

            int nombreLien = 0;
            for (int j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {

                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                if (j == 1) {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + "><input type='text' readonly=true style='border:none;text-align:center;' name='idEmp" + i + "' value='" + getDataDirecte()[i][j] + "'></td>";
                } else {
                    temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + ">" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                }
            }

            temp += "</tr>";
        }
        temp += "</table>";
        temp += "<input class=\"btn btn-success\" type=\"submit\" value=\"Valider\" />";
        temp += "</div>";
        setHtml(temp);
    }
    /**
     * 
     * @return html avec radio avec generation implicite
     * @throws Exception
     */
    public String getHtmlWithRadioButton() throws Exception {
        this.makeHtmlWithRadioButton();
        return html;
    }
    
    public String getHtmlWithRadioButtonNew() throws Exception {
        this.makeHtmlWithRadioButtonNew();
        return html;
    }

    /**
     * 
     * @return html avec checkbox avec generation implicite
     * @throws Exception
     */

    public String getHtmlWithMultipleCheckbox() throws Exception {
        this.makeHtmlWithMultipleCheckbox();
        return html;
    }

    /**
     * @param choix chaine de caractère délimitée par ; sur les choix précédemment réalisées
     * @return html avec checkbox qui retient les choix avec generation implicite
     * @throws Exception
     */
    public String getHtmlWithMultipleCheckboxRetientChoix(String choix) throws Exception {
        //System.out.println("-----------     choix        ---------------    " + choix);
        if (choix != null && choix.compareTo("") != 0) {
            String[] listeChoix = choix.split(";");
            this.makeHtmlWithMultipleCheckboxRetientChoix(listeChoix);
        } else {
            this.makeHtmlWithMultipleCheckbox();
        }
        return html;
    }
    /**
     * 
     * @param choix chaine de caractère délimité par ; sur les choix précédemment réalisés
     * @return html avec checkbox qui retient les choix avec generation implicite
     * @throws Exception
     */
    public String getHtmlWithMultipleCheckboxRetientChoixTous(String choix) throws Exception {
        //System.out.println("-----------     choix        ---------------    " + choix);
        if (choix != null && choix.compareTo("") != 0) {
            String[] listeChoix = choix.split(";");
            this.makeHtmlWithMultipleCheckboxRetientChoixTous(listeChoix);
        } else {
            //System.out.println("-----------     Dans else        ---------------    " + choix);
            this.makeHtmlWithMultipleCheckboxTous();
        }
        return html;
    }

    /**
     * 
     * @param pourc liste des pourcentage sur les données
     */
    public void setPourc(double[][] pourc) {
        this.pourc = pourc;
    }
    /**
     * 
     * @return liste des pourcentage sur les données par colonne
     */
    public double[][] getPourc() {
        return pourc;
    }
    /**
     * 
     * @param pourcentage liste de colonne avec pourcentage
     */
    public void setPourcentage(String[] pourcentage) {
        this.pourcentage = pourcentage;
    }

    /**
     * 
     * @return  liste de colonne avec pourcentage
     */
    public String[] getPourcentage() {
        //System.out.print(pourcentage.length);
        return pourcentage;
    }
    /**
     * génerer html avec checkbox
     * @throws Exception
     */
    public void makeHtmlWithCheckbox() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-warning'>";
        temp += "<div class='box-header'>";
        temp += "<h3 class='box-title' align=center>" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class='box-body table-responsive no-padding'>";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class='table table-hover'>";
        temp += "<thead>";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<th align=center valign=top style='background-color:#bed1dd'><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\"></th>";
        for (int i = 0; i < nombreColonne; i++) {
//            temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getLibelleAffiche()[i] + "</th>";
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + ">" + getLibelleAffiche()[i] + "</a></th>";
            } else {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getLibelleAffiche()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        //temp += "<th align=center valign=top style='background-color:#bed1dd'>Nombre</th>";
        temp += "</tr>";
        tempcsv += "\r\n";
        tempxml += "\r\n";
        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>\r\n";
            int nombreLien = 0, j = 0, l = 0;
            temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='id' id='checkbox" + i + "'/></td>";
            for (j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + rajoutLien + "'>";
                    apresLien = "</a>";
                }

                //if (Utilitaire.isStringNumeric(getDataDirecte()[i][j])) {
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + " >" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + apresLien + " </td>";

                //temp += "<td width=" + getPropEntet()[j] + "%>" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                tempcsv += Utilitaire.verifNumerique(Utilitaire.champNull(getDataDirecte()[i][j]));
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(Utilitaire.champNull(getDataDirecte()[i][j])) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "<input type=\"hidden\" name=\"ids\" value=\"" + getIds() + "\">";
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='box-footer'>";
        if (isAfficheBouttondevalider()) {
            temp += "<input id='acte' type='hidden' name='acte' value='update'/> ";
            temp += "<button type='button' name='Submit2' class='btn btn-apj-secondary pull-left' style='margin-left: 25px;' onClick=\"document.getElementById('acte').value='detacher'; document.getElementById('formmultiple').submit();\">D&eacute;tacher</button> ";
            temp += "<button type='button' name='Submit2' class='btn btn-apj-secondary pull-left' style='margin-left: 25px;' onClick=\"document.getElementById('acte').value='valider'; document.getElementById('formmultiple').submit();\">Viser</button> ";
        } else {
            if (this.getNameBoutton2().compareTo("") != 0) {
                temp += "<button type='submit' name='Submit2' class='btn btn-danger pull-right' style='margin-right: 25px;' onClick=\"acte.value='" + getNameActe2() + "'\">" + getNameBoutton2() + "</button> ";
            }
            temp += "<button type='submit' name='Submit2' class='btn btn-apj-secondary pull-right' style='margin-right: 25px;' onClick=\"acte.value='" + getNameActe() + "'\">" + getNameBoutton() + "</button> ";

        }
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='col-xs-12'>";

        temp += "</div>";
        //temp+=" <p> "+tempxml+" </p>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);

    }
    
    public void makeHtmlWithCheckboxNew() throws Exception{
        String temp = "";
        
         if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        temp += "<div class=\"result p-3 mt-3 \">";
        
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"export-container mb-1\">"+
                    "<div class=\"btn btn-export \" onclick=\"exprt()\">\n" +
"                    <i class=\"fas fa-file-export\"></i> Exporter\n" +
"                </div>";
        temp += "<div id='destinationDiv'></div></div>";
                    
        temp += "<div class=\"table-result\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"0\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-sortable \">";
        temp += "<thead>";
        temp += "<tr class=\"table-header\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        temp += "<th align=center valign=top><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\"></th>";
        Field[] listeFields = getListeField();

        nombreColonne = getDataDirecte()[0].length;
        for (int i = 0; i < nombreColonne; i++) {
            String ordre = "";
            String colonneOrdre = "";
            String[] params = getCritereLienString().split("&");
            if(params.length > 0){
                for(int j=0 ; j<params.length; j++){
                    System.out.println("params[j] "+params[j]);
                    if(params[j].contains("ordre")){
                        ordre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }if(params[j].contains("colonne")){
                        colonneOrdre = params[j].split("=").length > 1 ? params[j].split("=")[1] : "";
                    }
                }
            }
            String alignement = "";
            
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                String libelle = "";
                if(getLibelle_champ().get(getLibelleAffiche()[i]) != null){
                    libelle = getLibelle_champ().get(getLibelleAffiche()[i]);
                }else{
                    libelle = getLibelleEnteteAAffiche().get(getLibeEntete()[i]) == null ? getLibelleAffiche()[i] : (String) getLibelleEnteteAAffiche().get(getLibeEntete()[i]);
                }

                if(this.getColGroupe() != null &&  this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span> m-1</a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr; m-1</a></th>";
                    }
                    else{
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr; m-1</a></th>";
                    }
                    temp += entete;
                }
                String entete = "";
                    if(ordre.compareToIgnoreCase("asc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;<span class=\"fleche-asc-desc\">&uarr;</span></a></th>";
                    }
                    else if(ordre.compareToIgnoreCase("desc") == 0 && colonneOrdre.compareToIgnoreCase(getLibeEntete()[i]) == 0){
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "<span class=\"fleche-asc-desc\">&darr;</span>&uarr;</a></th>";
                    }
                    else{
                        entete = "<th  align=\"" + alignement + "\" valign=\"top\">" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + " >" + libelle + "&darr;&uarr;</a></th>";
                    }
                    temp += entete;
            } else {
                if(this.getColGroupe() != null && this.getColGroupe().contains(this.getLibeEntete()[i])) {
                    temp += "<th align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + " m-1</th>";
                }
                temp += "<th  align=\"" + alignement + "\" valign=\"top\">" + getLibeEntete()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        temp += "</thead>";
        temp += "<tbody>\n";
        
        for (int i = 0; i < nombreLigne; i++) {

            temp += "<tr onmouseover=\"this.style.backgroundColor='#EAEAEA'\" onmouseout=\"this.style.backgroundColor=''\">";
            temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='id' id='checkbox" + i + "'/></td>";
            tempxml += "<row>";
            int nombreLien = 0, j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) {
                String alignement = "left";
                if (listeFields != null) {
                    for (int k = 0; k < listeFields.length; k++) {
                        if (listeFields[k].getName().compareToIgnoreCase(getLibeEntete()[j]) == 0) {
                            if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("String") == 0) {
                                alignement = "left";
                                break;
                            } else if (listeFields[k].getType().getSimpleName().compareToIgnoreCase("Double") == 0) {
                                alignement = "right";
                                break;
                            } else {
                                alignement = "center";
                                break;
                            }
                        }
                    }
                }
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());

                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    String lienAutre = "";
                    if (this.isGroupe()) {
                        for (int g = 0; g < getLibeEntete().length - 1; g++) {
                            lienAutre += getLibeEntete()[g] + "=" + getDataDirecte()[i][g] + "&";
                        }
                        lienAutre += getLibeEntete()[getLibeEntete().length - 1] + "=" + getDataDirecte()[i][getLibeEntete().length - 1]+"&"+getLienFormulaire();
                    }else{
                        lienAutre = colLien + "=" + valL;
                    }

                    lien = "<a href='" + getLien()[numeroColonne] + "&" + lienAutre + rajoutLien + "'>";
                    apresLien = "</a>";
                }
                boolean test = false;
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }

                if (getPourcentage() != null) {
                    for (int element = 0; element < getPourcentage().length; element++) {
                        if (libeEntete[j].equals(getPourcentage()[element])) {
                            test = true;
                            temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\">" + lien + Utilitaire.champNull(getDataDirecte()[i][j]) + " (" + Utilitaire.formaterAr(getPourc()[element][i]) + " %)" + apresLien + " </td>";
                            break;
                        }
                    }
                }
                if (!test) {
                    if(isColumnToHide(getLibeEntete()[j])){
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align='center' >" + lien + "-"+" " + apresLien + " </td>";
                    } else {
                        temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + alignement + "\" >" + lien + Utilitaire.champNull(getDataDirecte()[i][j])+" " + apresLien + " </td>";
                    }
                }

                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        
        
        temp += "</tbody>\n" +
                "</table>\n" ;

        if (isAfficheBouttondevalider()) {
            temp += "<input id='acte' type='hidden' name='acte' value='update'/> ";
            temp += "<button type='button' name='Submit2' class='btn btn-success pull-left' style='margin-left: 25px;' onClick=\"document.getElementById('acte').value='detacher'; document.getElementById('formmultiple').submit();\">D&eacute;tacher</button> ";
            temp += "<button type='button' name='Submit2' class='btn btn-success pull-left' style='margin-left: 25px;' onClick=\"document.getElementById('acte').value='valider'; document.getElementById('formmultiple').submit();\">Viser</button> ";
        } else {
            if (this.getNameBoutton2().compareTo("") != 0) {
                temp += "<button type='submit' name='Submit2' class='btn btn-danger pull-right' style='margin-right: 25px;' onClick=\"acte.value='" + getNameActe2() + "'\">" + getNameBoutton2() + "</button> ";
            }
            temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' onClick=\"acte.value='" + getNameActe() + "'\">" + getNameBoutton() + "</button> ";

        }
        temp += "</div></div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }

    public void makeHtmlCorrection() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-warning'>";
        temp += "<div class='box-header'>";
        temp += "<h3 class='box-title' align=center>" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class='box-body table-responsive no-padding'>";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class='table table-hover'>";
        temp += "<thead>";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<th align=center valign=top style='background-color:#bed1dd'></th>";
        for (int i = 0; i < nombreColonne; i++) {
//            temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getLibelleAffiche()[i] + "</th>";
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + ">" + getLibelleAffiche()[i] + "</a></th>";
            } else {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getLibelleAffiche()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        //temp += "<th align=center valign=top style='background-color:#bed1dd'>Nombre</th>";
        temp += "</tr>";
        tempcsv += "\r\n";
        tempxml += "\r\n";
        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>\r\n";
            int nombreLien = 0, j = 0;
            temp += "<td align=center><input type='checkbox' value='" + getDataDirecte()[i][0] + "' name='id' id='checkbox" + i + "'/></td>";
            for (j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + "'>";
                    apresLien = "</a>";
                }

                //if (Utilitaire.isStringNumeric(getDataDirecte()[i][j])) {
                String alignement = "left";
                if (getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    alignement = "left";
                }
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if (testsplit.contains(",")) {
                    String[] testsplits = testsplit.split(",");
                    if (Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) {
                        alignement = "right";
                    }
                }
                temp += "<td width=" + getPropEntet()[j] + "% align=" + alignement + " >" + lien + getDataDirecte()[i][j] + apresLien + " </td>";

                //temp += "<td width=" + getPropEntet()[j] + "%>" + lien + getDataDirecte()[i][j] + apresLien + "</td>";
                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "<input type=\"hidden\" name=\"ids\" value=\"" + getIds() + "\">";
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='box-footer'>";
        temp += "<input type='hidden' name='acte' value='update'/> ";
        temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-left: 25px;' onClick=\"acte.value='corriger'\">Corriger</button> ";

        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='col-xs-12'>";

        temp += "</div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);

    }

    public String getHtmlWithCheckbox() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlWithCheckbox();
        }
        return html;
    }
    
      public String getHtmlWithCheckboxNew() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlWithCheckboxNew();
        }
        return html;
    }

    public String getHtmlCorrection() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlCorrection();
        }
        return html;
    }
    
    
    /**
     * 
     * @return html de checkbox avec bouton valider
     * @throws Exception
     */
    public String getHtmlWithCheckboxSelected() throws Exception {
        this.setAfficheBouttondevalider(true);
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlWithCheckbox();
        }
        return html;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getIds() {
        return ids;
    }

    private String getNameBoutton() {
        return nameBoutton;
    }

    private String getNameActe() {
        return nameActe;
    }

    public Map getLibelleEnteteAAffiche() {
        return libelleEnteteAAffiche;
    }

    public void setLibelleEnteteAAffiche(Map libelleEnteteAAffiche) {
        this.libelleEnteteAAffiche = libelleEnteteAAffiche;
    }
    /**
     * 
     * @return html avec checkbox pour update d'un champs
     * @throws Exception
     */
    public String getHtmlWithCheckboxUpdateMultiple() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtmlWithCheckboxUpdateMultiple();
        }
        return html;
    }
    /**
     * générer html avec checkbox pour update d'un champs
     * @throws Exception
     */
    public void makeHtmlWithCheckboxUpdateMultiple() throws Exception {
        if (getDataDirecte() == null || getDataDirecte().length == 0) {
            return;
        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box box-warning'>";
        temp += "<div class='box-header'>";
        temp += "<h3 class='box-title' align=center>" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class='box-body table-responsive no-padding'>";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=" + getTailleTableau() + " border=0 align=center cellpadding=3 cellspacing=3 class='table table-hover'>";
        temp += "<thead>";
        temp += "<tr class=" + getCssEntete() + ">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getDataDirecte().length;
        nombreColonne = getDataDirecte()[0].length;
        temp += "<th align=center valign=top style='background-color:#bed1dd'><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\"></th>";
        for (int i = 0; i < nombreColonne; i++) {
            if (getCritereLienString().compareToIgnoreCase("") != 0) {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getCritereLienString() + "&triCol=yes&newcol=" + getLibeEntete()[i] + ">" + getLibelleAffiche()[i] + "</a></th>";
            } else {
                temp += "<th width=" + getPropEntet()[i] + "% align=center valign=top style='background-color:#bed1dd'>" + getLibelleAffiche()[i] + "</th>";
            }
            tempcsv += getLibelleAffiche()[i];
            if (i != nombreColonne - 1) {
                tempcsv += ";";
            }
        }
        temp += "</tr>";
        tempcsv += "\r\n";
        tempxml += "\r\n";
        temp += "</thead>";
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<tr onmouseover=this.style.backgroundColor='#EAEAEA' onmouseout=\"this.style.backgroundColor=''\">";
            tempxml += "<row>\r\n";
            int nombreLien = 0, j = 0, l=0;
            temp += "<td align=center><input type='checkbox' value='" + i + "' name='id' id='checkbox" + i + "'/></td>";
            
            for (j = 0; j < nombreColonne; j++) {
                String lien = "";
                String apresLien = "";
                int numeroColonne = Utilitaire.estIlDedans(getLibeEntete()[j], getColonneLien());
                if (numeroColonne != -1) {
                    String colLien = getLibeEntete()[j];
                    String valL = getDataDirecte()[i][j];
                    if (getAttLien() != null) {
                        colLien = getAttLien()[nombreLien];
                    }
                    if (getValeurLien() != null) {
                        Object valeurC = CGenUtil.getValeurFieldByMethod(data[i], getValeurLien()[nombreLien]);
                        valL = String.valueOf(valeurC);
                    }
                    nombreLien++;
                    String rajoutLien = "";
                    if (getUrlLien() != null) {
                        rajoutLien = getRajoutLienMultiple(getData()[i], getUrlLien()[l], getUrlLienAffiche()[l]);
                    }
                    l++;
                    lien = "<a href='" + getLien()[numeroColonne] + "&" + colLien + "=" + valL + rajoutLien +"'>";
                    apresLien = "</a>";
                }

                //if (Utilitaire.isStringNumeric(getDataDirecte()[i][j])) {
                String alignement="left";
                if(getDataDirecte()[i][j].getClass().getName().compareToIgnoreCase("java.lang.String")==0) alignement = "left";
                String testsplit = Utilitaire.enleverEspaceDoubleBase(getDataDirecte()[i][j]);
                if(testsplit.contains(",")){
                    String[] testsplits = testsplit.split(",");
                    if(Utilitaire.isStringNumeric(testsplits[0]) && Utilitaire.isStringNumeric(testsplits[1])) alignement ="right";
                }
                String val = lien + getDataDirecte()[i][j] + apresLien;
                //temp += "<td>"+val+"</td>";
                if(!getIds().equals(getLibeEntete()[j])){
                    temp += getFormu().makeHtmlUpdateTableau(getLibeEntete()[j], val, i);
                }else{
                    temp += "<td width=" + getPropEntet()[j] + "% align="+alignement+" ><input type='hidden' id='"+getLibeEntete()[j]+"_"+i+"' name='"+getLibeEntete()[j]+"_"+i+"' value='"+lien + getDataDirecte()[i][j] + apresLien+"'>"+lien + getDataDirecte()[i][j] + apresLien+"</td>";   
                }
                //temp += "<td width=" + getPropEntet()[j] + "% align="+alignement+" ><input type='textbox' id='"+getLibeEntete()[j]+"_"+i+"' name='"+getLibeEntete()[j]+"_"+i+"' value='"+lien + getDataDirecte()[i][j] + apresLien+"'></td>";
                
                tempcsv += Utilitaire.verifNumerique(getDataDirecte()[i][j]);
                tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getDataDirecte()[i][j]) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "<input type=\"hidden\" name=\"ids\" value=\"" + getIds() + "\">";
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='box-footer'>";
        temp += "<div class='col-xs-12'>";
        temp += "<button type='submit' name='Submit2' class='btn btn-apj-secondary pull-right' style='margin-right: 25px;'>Enregistrer</button> ";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='col-xs-12'>";

        temp += "</div>";
        //temp+=" <p> "+tempxml+" </p>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);

    }

    public ValeurEtiquette[][] getValeurEtiquette() {
        return valeurEtiquette;
    }

    public void setValeurEtiquette(ValeurEtiquette[][] valeurEtiquette) {
        this.valeurEtiquette = valeurEtiquette;
    }
    public TableauRecherche()
    {
        
    }
}
