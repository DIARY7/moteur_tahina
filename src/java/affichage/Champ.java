package affichage;

import bean.CGenUtil;
import bean.ClassMAPTable;
import constante.ConstanteAffichage;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import user.UserEJB;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;

/**
 * <p>
 * Classe représentant l'affichage d'un attribut de classe de mapping dans une page que ce soit
 * consultation ou formulaire.
 * </p>
 * 
 * Cette classe est la classe a utilisé pour jouer avec les informations d'un champ.
 * <p>
 * Pour changer le libellé d
 * 
 * </p>
 *
 * @author BICI
 * @version 1.0
 */
public class Champ {

    private String nom;
    private String defaut = "";
    private String css = "form-control";
    private String type = ConstanteAffichage.typeTextBox;
    private String libelle;
    private String pageAppel;
    private String cssLibelle = "left";
    private String html;
    private int taille;
    private String valeur;
    private String typeData = ConstanteAffichage.typeTexte;
    private java.lang.reflect.Field attribut;
    private int estIntervalle = 0;
    private String autre;
    private String libelleAffiche;
    private String htmlInsert;
    private boolean visible = true;
    private boolean photo = false;
    private String htmlTableauInsert;
    private String htmlTableau;
    private String champReturn;
    private String apresLienPageappel = "";
    private String urlFiche = "";
    private boolean autoComplete = false;
    private String valeur_ac;
    private String[] colFiltre;
    private String[] valFiltre;
    
    private String ac_affiche;
    private String ac_valeur;
    private String ac_nomTable;
    private String ac_aWhere;
    private boolean autocomplete = false;
    private Ac_object[] tab_ac;
    private bean.ClassMAPTable[] base;
    private bean.ClassMAPTable mine;
    private boolean estMultiple = false;
    private String ac_classeMapping;
    private boolean autocompleteDynamique;
    private boolean useMotCle;
    private String champUrl;
    private String pageAppelInsert;
    private String champRetour="";
    private String champRetourMapping="";



    Formulaire formulaire;
    private String valeurLibelle;

    public String getChampRetour() {
        return champRetour;
    }

    public void setChampRetour(String champRetour) {
        this.champRetour = champRetour;
    }

    public String getChampRetourMapping() {
        return champRetourMapping;
    }

    public void setChampRetourMapping(String champRetourMapping) {
        this.champRetourMapping = champRetourMapping;
    }

    public String getChampUrl() {
        return champUrl;
    }

    public void setChampUrl(String champUrl) {
        this.champUrl = champUrl;
    }

    public String getPageAppelInsert() {
        return pageAppelInsert;
    }

    public static void setPageAppelInsert(Champ[] tChamps, String pageAppelInsert) {
        for(Champ champ: tChamps){
            champ.setPageAppelInsert(pageAppelInsert);
        }
    }
    public static void setPageAppelInsert(Champ[] tChamps, String pageAppelInsert,String champUrl) {
        for(Champ champ: tChamps){
            champ.setPageAppelInsert(pageAppelInsert,champ.getNom()+";"+champ.getNom()+"libelle",champUrl);
        }
    }
    public void setPageAppelInsert(String pageAppelInsert) {
        this.pageAppelInsert = pageAppelInsert;
        this.champReturn = getNom()+";"+getNom()+"libelle";
        this.champUrl = "id;id";
    }

    public static void setPageAppelInsert(Champ[] tChamps, String pageAppelInsert, String champReturn, String champUrl){
        for(Champ champ: tChamps){
            champ.setPageAppelInsert(pageAppelInsert, champReturn, champUrl);
        }
    }

    public void setPageAppelInsert(String pageAppelInsert, String champReturn, String champUrl){
        this.pageAppelInsert = pageAppelInsert;
        this.champReturn = champReturn;
        this.champUrl = champUrl;
    }

    public String getHtmlPageAppelInsert(){
        String rep = "";
        if (this.pageAppelInsert != null) {
            rep += "<button type='button'  onclick=pagePopUp('"
                    + "modulePopup.jsp?but=" + pageAppelInsert + "&champReturn=" + getChampReturn() + "&champUrl=" + champUrl
                    + getApresLienPageappel() + "')><i class='fa fa-plus'></i></button>";
        }
        return rep;
    }

    
    /**
     * 
     * @return formulaire où le champ se trouve
     */
    public Formulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(Formulaire formulaire) {
        this.formulaire = formulaire;
    }
    /**
     * @deprecated
     * @return
     */
    public ClassMAPTable getMine() {
        return mine;
    }

    public void setMine(ClassMAPTable mine) {
        this.mine = mine;
    }

    public boolean getEstMultiple() {
        return estMultiple;
    }

    public String getAc_classeMapping() {
        return ac_classeMapping;
    }

    public void setAc_classeMapping(String ac_classeMapping) {
        this.ac_classeMapping = ac_classeMapping;
    }

    public boolean isAutocompleteDynamique() {
        return autocompleteDynamique;
    }

    public void setAutocompleteDynamique(boolean autocompleteDynamique) {
        this.autocompleteDynamique = autocompleteDynamique;
    }

    public boolean isUseMotCle() {
        return useMotCle;
    }

    public void setUseMotCle(boolean useMotCle) {
        this.useMotCle = useMotCle;
    }

    public static void setPageAppelComplete(Champ[] tChamps,String ac_classeMapping, String ac_valeur, String ac_nomTable ){
        for(Champ champ: tChamps){
            champ.setPageAppelComplete(ac_classeMapping, ac_valeur, ac_nomTable);
        }
    }

    public static void setPageAppelComplete(Champ[] tChamps,String ac_classeMapping, String ac_valeur, String ac_nomTable,String champRetour,String champRetourMapping){
        for(int i=0;i<tChamps.length;i++){
            tChamps[i].setPageAppelComplete(ac_classeMapping, ac_valeur, ac_nomTable);
            tChamps[i].setChampRetour(champRetour);
            String[] mappings = champRetourMapping.split(";");
            for(int a=0;a<mappings.length;a++){
                mappings[a] = mappings[a]+"_"+i;
                if(mappings[a].contains("libelle")){
                    mappings[a] = mappings[a].replace("libelle_"+i, "_"+i+"libelle");
                }
            }
            tChamps[i].setChampRetourMapping(Arrays.stream(mappings).collect(Collectors.joining(";")));
        }
    }

    public void setAutocompleteDynamique(String ac_classeMapping, String ac_affiche, String ac_valeur, String ac_nomTable) {
        setAc_classeMapping(ac_classeMapping);
        setAc_affiche(ac_affiche);
        setAc_valeur(ac_valeur);
        setAc_nomTable(ac_nomTable);
        setAutocompleteDynamique(true);
        setUseMotCle(false);
    }
    public void setPageAppelComplete(String ac_classeMapping, String ac_valeur, String ac_nomTable,String champRetour,String champRetourMapping) {
        setPageAppelComplete(ac_classeMapping, ac_valeur, ac_nomTable);
       setChampRetour(champRetour);
       setChampRetourMapping(champRetourMapping);
    } 
    public void setPageAppelComplete(String ac_classeMapping, String ac_valeur, String ac_nomTable) {
        setAc_classeMapping(ac_classeMapping);
        setAc_valeur(ac_valeur);
        setAc_nomTable(ac_nomTable);
        setUseMotCle(true);
        setAutocompleteDynamique(true);
       
    } 

  

    public void setEstMultiple(boolean estMultiple) {
        this.estMultiple = estMultiple;
    }
    
    
    /**
     * Pour les champs avec valeurs de selection
     * liste des objets à mettre en option
     * @return
     */
    public bean.ClassMAPTable[] getBase() {
	return base;
    }

    public void setBase(bean.ClassMAPTable[] base) {
	this.base = base;
    }
    /**
     * 
     * @return champ autocomplete ou pas
     */
    public boolean isAutocomplete() {
        return autocomplete;
    }
    /**
     * definir si un champ est autocomplete ou pas
     * @param autocomplete
     */
    public void setAutocomplete(boolean autocomplete) {
        this.autocomplete = autocomplete;
    }
    
    public String getAc_affiche() {
        return ac_affiche;
    }
    /**
     * definir la colonne en base representant la valeur à afficher si autocomplete
     * @param ac_affiche
     */
    public void setAc_affiche(String ac_affiche) {
        this.ac_affiche = ac_affiche;
    }

    public String getAc_valeur() {
        return ac_valeur;
    }
    /**
     * definir la colonne en base representant la valeur d'un champ autocomplete
     * @param ac_valeur valeur d'un champ autocomplete
     */
    public void setAc_valeur(String ac_valeur) {
        this.ac_valeur = ac_valeur;
    }

    public String getAc_nomTable() {
        return ac_nomTable;
    }
    /**
     * 
     * @param ac_nomTable nom de la table de recherche pour autocomplete
     */
    public void setAc_nomTable(String ac_nomTable) {
        this.ac_nomTable = ac_nomTable;
    }
    
    public Ac_object[] getTab_ac() {
        return tab_ac;
    }
    /**
     * 
     * @param tab_ac liste des objets pour autocomplete
     */
    public void setTab_ac(Ac_object[] tab_ac) {
        this.tab_ac = tab_ac;
        transformToStringAc();
    }

    public String getAc_aWhere() {
        return ac_aWhere;
    }

    /**
     * requ
     * @param ac_aWhere partie de la requete SQL de la clause where pour filtrer 
     * les autocomplete à utiliser
     */
    public void setAc_aWhere(String ac_aWhere) {
        this.ac_aWhere = ac_aWhere;
    }

    
    PageConsulteLien lien;

    /**
     * 
     * @return liste de colonnes à filtrer sur une selection par popup
     */
    public String[] getColFiltre() {
        return colFiltre;
    }

    /**
     * 
     * @param colFiltre liste de colonnes à filtrer sur une selection par popup
     */
    public void setColFiltre(String[] colFiltre) {
        this.colFiltre = colFiltre;
    }
    /**
     * 
     * @return liste des valeurs des colonnes à filtrer sur une selection par popup
     */
    public String[] getValFiltre() {
        return valFiltre;
    }
    /**
     * 
     * @param valFiltre liste des valeurs des colonnes à filtrer sur une selection par popup
     */
    public void setValFiltre(String[] valFiltre) {
        this.valFiltre = valFiltre;
    }
    /**
     * 
     * @param pageAppel lien de page de popup de selection
     * @param champReturnn champs du formulaire courant pour retour
     * @param col liste des colonnes à filtrer
     * @param val liste des valeurs des colonnes à filtrer
     */
     public void setPageAppelAvecFiltre(String pageAppel, String champReturnn,String[] col,String[] val) {
        this.pageAppel = pageAppel;
        this.champReturn = champReturnn;
        this.setColFiltre(col);
        this.setValFiltre(val);
    }
    
    /**
     * 
     * @return valeur selectionnée de l'autocomplete
     */
    public String getValeur_ac() {
        return valeur_ac;
    }
     public void setValeur_ac(String valeur_ac) {
        this.valeur_ac = valeur_ac;
    }
    public String getUrlFiche() {
        return urlFiche;
    }
    /**
     * 
     * @param urlFiche
     */
    public void setUrlFiche(String urlFiche) {
        this.urlFiche = urlFiche;
    }

    /**
     * 
     * @return paramètre supplementaire à envoyer au popup d'appel
     */
    public String getApresLienPageappel() {
        return apresLienPageappel;
    }

    public void setApresLienPageappel(String apresLienPageappel) {
        this.apresLienPageappel = apresLienPageappel;
    }
    /**
     * 
     * @return information sur la page de fiche pour un champ donné
     */
    public PageConsulteLien getLien() {
        return lien;
    }

    /**
     * 
     * @param information sur la page de fiche pour un champ donné
     */
    public void setLien(PageConsulteLien lien) {
        this.lien = lien;
    }
    /**
     * 
     * @param lien lien vers la page
     * @param queryString argument après le lien vers la page
     */
    public void setLien(String lien, String queryString) {
        setLien(new PageConsulteLien(lien, queryString));
    }
    /**
     * 
     * @return si le champ est un champ photo
     * 
     */
    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }
    /**
     * Constructeur par défaut de champ
     */
    public Champ() {
    }

    public Champ(String nom) {
        setNom(nom);
    }
    /**
     * @return representation en chaine de caractère d'un champ
     */
    public String toString() {
        return getNom();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * 
     * @param defaut valeur par défaut d'un champs
     */
    public void setDefaut(String defaut) {
        this.defaut = defaut;
    }
    /**
     * Initialiser la valeur de plusieurs champs
     * @param tChamp liste des champs
     * @param defaut valeur par défaut pour les champs en argument
     */
    public static void setDefaut(Champ[] tChamp, String defaut) {
        for(Champ c : tChamp)
            c.setDefaut(defaut);
    }
    /**
     * 
     * @return valeur par défaut du champs
     */
    public String getDefaut() {
        return defaut;
    }
    /**
     * 
     * @param css css customisé du champs
     */
    public void setCss(String css) {
        this.css = css;
    }
    /**
     * Initialiser le css customisé  de plusieurs champs
     * @param tChamp liste des champs
     * @param defaut valeur par défaut pour les champs en argument
     */
    public static void setCss(Champ[] tChamp, String css) {
        for(Champ c : tChamp)
            c.setCss(css);
    }

    public String getCss() {
        return css;
    }
    /**
     * Pour un formulaire fixer le type d'input
     * @param type type d'input(text,textarea,hidden)
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 
     * @return type d'input pour le formulaire
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param libelle label du champs à afficher à l'utilisateur
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    /**
     * 
     * @return label du champs à afficher à l'utilisateur
     */
    public String getLibelle() {
        if (libelle == null || libelle.compareToIgnoreCase("") == 0) {
            return nom;
        }
        return libelle;
    }
    /**
     * Mettre un popup sur un champ pour choix
     * @param pageAppel lien du popup
     */
    public void setPageAppel(String pageAppel) {
        this.pageAppel = pageAppel;
        this.champReturn = this.getNom() + ";" + this.getNom() + "libelle";
    }
    /**
     * Mettre un popup sur une liste de champ pour pop up
     * @param tChamp liste des champs
     * @param pageAppel lien du popup
     * @param champReturn liste des champs qui vont prendre les valeurs après fermeture de popup
     * @throws Exception
     */
    public static void setPageAppel(Champ[] tChamp, String pageAppel, String champReturn) throws Exception {
        for(Champ c : tChamp){
            c.pageAppel = pageAppel;
            c.champReturn = champReturn;
        }
    }
    /**
     * Initialiser une liste de champs en tant qu'auto complete
     * @param tChamp liste des champs
     * @param affiche nom de colonne à afficher pour le champs
     * @param valeur nom de colonne de valeur pour le champs
     * @param nomTable nom de la table de recherche
     * @param aWhere filtre SQL pour la recherche
     * @throws Exception
     */
    public static void setAutocomplete(Champ[] tChamp, String affiche, String valeur, String nomTable, String aWhere) throws Exception {
        for(Champ c : tChamp){
            c.ac_affiche = affiche;
            c.ac_valeur = valeur;
            c.ac_nomTable = nomTable;
            c.autocomplete = true;
            c.ac_aWhere = aWhere;
        }
    }
    
    public String getChampReturn() {
        return champReturn;
    }
    /**
     * 
     * @param champReturn champ valeur et de libellé délimité par ;
     * sur le formulaire suite à popup
     */
    public void setChampReturn(String champReturn) {
        this.champReturn = champReturn;
    }
    /**
     * Mettre un popup sur un champ pour choix
     * @param pageAppel lien du popup
     * @param champReturnn champ valeur et de libellé délimité par ;
     * sur le formulaire suite au popup
     */
    public void setPageAppel(String pageAppel, String champReturnn) {
        this.pageAppel = pageAppel;
        this.champReturn = champReturnn;
    }
    
    
    /**
     * 
     * @return lien du popup
     */
    public String getPageAppel() {
        return pageAppel;
    }

    public void setCssLibelle(String cssLibelle) {
        this.cssLibelle = cssLibelle;
    }
    /**
     * 
     * @return css du libellé
     */
    public String getCssLibelle() {
        return cssLibelle;
    }
    /**
     * 
     * @param html html representant le champ
     */
    public void setHtml(String html) {
        this.html = html;
    }

    public void setHtmlTableau(String htmlTableau) {
        this.htmlTableau = htmlTableau;
    }
    /**
     * retourne ou génere un html representant le champ
     * @return html html representant le champ
     * @throws Exception
     */
    public String getHtml() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtml();
        }
        return html;
    }
    /**
     * retourne ou génere un html representant le champ 
     * pour une ligne de formulaire pour une table
     * @param i indice de la ligne de la ligne de la table
     * @return 
     * @throws Exception
     */
    public String getHtmlTableau(int i) throws Exception {
        this.makeHtmlTableau(i);
        return htmlTableau;
    }
    /**
     * Génerer le html correspondant au champ
     * @throws Exception
     */
    public void makeHtml() throws Exception {
        String temp = "";
        if(getVisible()==false)
        {
            temp = "<input name='" + getNom() + "' type='hidden' class=" + getCss() + "  id=" + getNom() + " value='" + getValeur() + "' " + getAutre() + ">";
        }
        else if (type.compareTo(ConstanteAffichage.textarea) == 0) {
            //temp = "<textarea name='" + getNom() + "' row='3' class='" + getCss() + "' id='" + getNom() + "' maxlength='" + getTaille() + "'>" + getValeur() + "</textarea>";
            temp = "<textarea name='" + getNom() + "' class='" + getCss() + "' id='" + getNom() + "' style='resize: none;'>" + getValeur() + "</textarea>";
        } // ==== nampiana 
        else if (typeData.compareTo(ConstanteAffichage.typeDaty) == 0) {
            temp = "<input name=" + getNom() + " type=" + getType() + " class='" + getCss() + " datepicker'  id=" + getNom() + " value='" + getValeur() + "' " + getAutre() + ">";

        } // ==== nampiana 
        else if (this.isAutocompleteDynamique()) {
            temp += getInputHiddenAutoComplete();
        }
        else if (!this.isPhoto()) 
        {
            temp = "<input name=" + getNom() + " type=" + getType() + " class=" + getCss() + "  id=" + getNom() + " value=\"" + getValeur() + "\" " + getAutre() + " data-text=\"Sélectionnez votre fichier\">";
            if (!this.isAutoComplete()) {
                temp += "<input type=hidden value='' name=" + getNom() + "auto>";
            }
            
        } else {
            temp = "<input name=" + getNom() + " type=\"file\" class=" + getCss() + " id=" + getNom() + " " + getAutre() + " data-text=\"Sélectionnez votre photo\">";
            if (!getValeur().isEmpty()) {
                temp += "<input type=\"hidden\" value=\"" + getValeur() + "\" name=\"" + getNom() + "2" + "\">";
            }
        }
        temp += getHtmlPageAppelInsert();
        setHtml(temp);
    }
    /**
     * Génerer le html correspondant au champ
     * @param i indice de la ligne sur la table
     * @throws Exception
     */
    public void makeHtmlTableau(int i) throws Exception {
        String temp = "";
        //System.out.println("i="+i);
        if(getVisible()==false)
        {
            temp = "<input name='" + getNom() + "' type='hidden' class=" + getCss() + "  id=" + getNom() + " value=\"" + getValeur() + "\" " + getAutre() + ">";
        }
        else if (type.compareTo(ConstanteAffichage.textarea) == 0) {
            //temp = "<textarea name='" + getNom() + "' row='3' class='" + getCss() + "' id='" + getNom() + "' maxlength='" + getTaille() + "'>" + getValeur() + "</textarea>";
            temp = "<textarea name='" + getNom() + "' class='" + getCss() + "' id='" + getNom() + "' style='resize: none;'>" + getValeur() + "</textarea>";
        } 
        else if (this.isAutocompleteDynamique()) {
            temp += getInputHiddenAutoComplete();
            temp += getHtmlPageAppelInsert();
        }
        else if (!this.isPhoto()) {
            //System.out.println("***************"+getValeur());
            temp = "<input name='" + getNom() + "' type=" + getType() + " class=" + getCss() + "  id=" + getNom() + " value=\"" + getValeur() + "\" " + getAutre() + " data-text=\"Sélectionnez votre fichier\">";

        } else {
            //System.out.println("getValeur() insert ===  " + getValeur());
            temp = "<input name='" + getNom() + "' type=\"file\" class=" + getCss() + " id=" + getNom() + " " + getAutre() + "  data-text=\"Sélectionnez votre fichier\">";
            if (!getValeur().isEmpty()) {

                temp += "<input type=\"hidden\" value=\"" + getValeur() + "\" name=\"" + getNom() + "2" + "_" + i + "\">";
            }
        }
        setHtmlTableau(temp);
    }
    /**
     * Ne fais rien
     * @throws Exception
     */
    public void makeHtmlInsert() throws Exception {
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }
    
    public static void setTaille(Champ[] tChamp, int taille) {
        for(Champ c : tChamp)
            c.setTaille(taille);
    }

    public int getTaille() {
        return taille;
    }
    /**
     * 
     * @param valeur valeur du champs
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
    /**
     * Specifier la valeur d'une liste de champs
     * @param tChamp liste de champs
     * @param valeur valeur à fixer pour le champs
     */
    public static void setValeur(Champ[] tChamp, String valeur) {
        for(Champ c : tChamp)
            c.setValeur(valeur);
    }
    /**
     * Prendre la valeur d'un champ
     * Si valeur non initialisé prendre défaut et si défaut non initialisé retourner string vide
     * @return
     */
    public String getValeur() {
        if (valeur == null || valeur.compareToIgnoreCase("null") == 0 || valeur.compareToIgnoreCase("") == 0) {
            if (getDefaut() == null || getDefaut().compareToIgnoreCase("null") == 0) {
                return "";
            }
            return getDefaut();
        }
        return valeur;
    }

    /**
     * Utilisé pour les champs de selection
     * prendre la valeur à afficher pour un champ
     * Si valeur non initialisé prendre défaut et si défaut non initialisé retourner string vide
     * @return 
     */
    
    public String getValeurLibelle() {
        if (valeurLibelle == null || valeurLibelle.compareToIgnoreCase("null") == 0 || valeurLibelle.compareToIgnoreCase("") == 0) {
            if (getDefaut() == null || getDefaut().compareToIgnoreCase("null") == 0) {
                return this.getValeur();
            }
            return getValeur();
        }
        String valeur = valeurLibelle;
        if(valeur!=null && valeur.contains("'")){
            valeur = valeur.replaceAll("'", "&apos;");
        }
        return valeur;
    }
    /**
     * 
     * @param valeurLibelle  valeur à afficher pour un champ
     */
    public void setValeurLibelle(String valeurLibelle) {
        this.valeurLibelle = valeurLibelle;
    }
    /**
     * Champ utilisé sur la construction formulaire pour savoir le type de data
     * @param typeData type de data(text,date,..)
     */
    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }
    /**
     * Specifier le type de data pour une liste de champs
     * @param tChamp
     * @param typeData
     */
    public static void setTypeData(Champ[] tChamp, String typeData) {
        for(Champ c : tChamp)
            c.setTypeData(typeData);
    }

    public String getTypeData() {
        return typeData;
    }
    /**
     * 
     * @param attribut attribut associé au champ
     */
    public void setAttribut(java.lang.reflect.Field attribut) {
        this.attribut = attribut;
    }
    /**
     * 
     * @return attribut associé au champ
     */
    public java.lang.reflect.Field getAttribut() {
        return attribut;
    }
    /**
     * 
     * @param estIntervalle verifier si un champ est intervalle
     */
    public void setEstIntervalle(int estIntervalle) {
        this.estIntervalle = estIntervalle;
    }

    public int getEstIntervalle() {
        return estIntervalle;
    }
    /**
     * prendre les objets à utiliser pour le autocomplete
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void findDataAutoComplete(Connection c) throws Exception
    {
        if (isAutocomplete()) 
        {
            //System.out.println("mandalo autocomplete = " );
            Ac_object[] ac_objt = null;
            ResultSet dr = null;
            Statement st = null;
            try {
                String req = "select " + getAc_affiche() + ", " + getAc_valeur() + " from " + getAc_nomTable() +" where 1<2 "+getAc_aWhere();
                st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                dr = st.executeQuery(req);
                List<Ac_object> liste = new ArrayList<>();
                while (dr.next()) {
                    Ac_object aco=new Ac_object(dr.getObject(getAc_valeur()), dr.getObject(getAc_affiche()));
                    liste.add(aco);
                }
                if (liste.size() > 0) {
                    ac_objt = new Ac_object[liste.size()];
                    for (int k = 0; k < liste.size(); k++) {
                        ac_objt[k] = liste.get(k);
                    }
                    setTab_ac(ac_objt);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (st != null) {
                    st.close();
                }
                if (dr != null) {
                    dr.close();
                }
            }
        }
    }
    /**
     * fonction à surcharger pour le chargement de données
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void findData(Connection c) throws Exception {
        
    }
    /**
     * Mettre de paramètre supplémentaire pour les pages de popup
     * @param autre string formaté de paramètre à utiliser pour le popup
     */
    public void setAutre(String autre) {
        this.autre = autre;
    }
    /**
     * Mettre de paramètre supplémentaire pour les pages de popup
     * @param tChamp liste de champs
     * @param autre string formaté de paramètre à utiliser pour le popup
     */
    public static void setAutre(Champ[] tChamp, String autre) {
        for(Champ c : tChamp)
            c.setAutre(autre);
    }
    /**
     * 
     * @return string formaté de paramètre sinon string vide
     */
    public String getAutre() {
        if (autre == null) {
            return "";
        }
        return autre;
    }

    public void setLibelleAffiche(String libelleAffiche) {
        this.libelleAffiche = libelleAffiche;
    }

    public String getLibelleAffiche() {
        if (libelleAffiche == null || libelleAffiche.compareToIgnoreCase("") == 0) {
            return getLibelle();
        }
        return libelleAffiche;
    }

    public void setHtmlInsert(String htmlInsert) {
        this.htmlInsert = htmlInsert;
    }
    /**
     * 
     * @return html representant le champ
     * @throws Exception
     */
    public String getHtmlInsert() throws Exception {
        if (htmlInsert == null || htmlInsert.compareToIgnoreCase("") == 0) {
            makeHtmlInsert();
        }
        if (htmlInsert == null || htmlInsert.compareToIgnoreCase("") == 0) {
            htmlInsert = getHtml();
        }
        return htmlInsert;
    }
    /**
     * generer un input hidden pour le champ
     * @return html d'input hidden avec name
     * @throws Exception
     */
    public String getInputHidden() throws Exception {
        makeInputPopup();
        return htmlInsert;
    }
    /**
     * generer un input hidden et libelle pour un champ autocomplete
     * @return html representant un autocomplete
     * @throws Exception
     */
    public String getInputHiddenAutoComplete() throws Exception {
        String temp = "";
        temp += "<div style='display: inline-flex; align-items: center;width: 90%;'>"; 
        temp += "<input name='" + getNom() + "libelle' type='" + getType() + "' class='" + getCss() + "' id='" + getNom() + "libelle' value='" + getValeur() + "' style='min-width: 50%;' " + getAutre() + "'>"; 
        temp += "<input type='hidden' value='" + getValeur() + "' name='" + getNom() + "' id='" + getNom() + "'>";
        temp += "<button type='button' id='" + getNom() + "searchBtn'><i class=\"fa fa-search\" aria-hidden=\"true\"></i></button>"; 
        temp += "</div>";
        return temp;
    }

    /**
     * Génerer le html pour un input de type popup
     * @throws Exception
     */
   
    public void makeInputPopup() throws Exception {
        String temp = "";
        String[] val = getNom().split("_");
        if(val.length > 1 && Utilitaire.checkNumber(val[1])){
            temp = "<input name=" + val[0] + "libelle_" + val[1] + " type=" + getType() + " class=" + getCss() + "  id=" + val[0] + "libelle_" + val[1] + " value='" + getValeurLibelle() + "' " + getAutre() + ">";
            temp += "<input type='hidden' value='" + getValeur() + "' name='" + getNom() + "' id='" + getNom() + "'>";
        }else{
            temp = "<input name=" + getNom() + "libelle" + " type=" + getType() + " class=" + getCss() + "  id=" + getNom() + "libelle" + " value='" + getValeurLibelle() + "' " + getAutre() + ">";
            temp += "<input type='hidden' value='" + getValeur() + "' name='" + getNom() + "' id='" + getNom() + "'>";
        }
        setHtml(temp);
        setHtmlInsert(temp);
    }

    /**
     * définir le html pour les tables de formulaire
     * @param htmlTableauInsert
     */
    public void setHtmlTableauInsert(String htmlTableauInsert) {
        this.htmlTableauInsert = htmlTableauInsert;
    }

    /**
     * prendre le html du champs pour but d'être utilisé dans une table
     * @param i indice de ligne
     * @return html representant le champ pour la ligne donnée
     * @throws Exception
     */
    public String getHtmlTableauInsert(int i) throws Exception {
        htmlTableauInsert = null;
        makeHtmlInsertTableau(i);
        if (htmlTableauInsert == null || htmlTableauInsert.compareToIgnoreCase("") == 0) {
            htmlTableauInsert = getHtmlTableau(i);
        }
        return htmlTableauInsert;
    }
    /**
     * 
     * @param visible 
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    /**
     * rendre une liste de champs visible
     * @param tChamp liste de champs
     * @param visible visibilité d'un champ
     */
    public static void setVisible(Champ[] tChamp, boolean visible) {
        for(Champ c : tChamp)
            c.setVisible(visible);
    }
    /**
     * 
     * @return visibilité d'un champ
     */
    public boolean getVisible() {
        return visible;
    }
    /**
     * @deprecated
     * Génerer et stocker les valeurs des autocompletes du champs
     * @param affiche attribut à afficher
     * @param valeur attribut de valeur
     * @param classMapTable classe de mapping
     * @param u session utilisateur
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void setAutocomplete(String affiche, String valeur, ClassMAPTable classMapTable, UserEJB u,Connection c) throws Exception {
        int indice =0;
        try {
            if(c==null){
                c = new UtilDB().GetConn();
                indice = 1;
            }
            this.autoComplete = true;
            Object[] liste = CGenUtil.rechercher(classMapTable, null, null,c, "");
            Class<?> clazz = Class.forName(classMapTable.getClassName());
            StringBuilder sbA = new StringBuilder();
            sbA = new StringBuilder();
            sbA.append("get")
                    .append(affiche.substring(0, 1).toUpperCase())
                    .append(affiche.substring(1));
            String getterAffiche = sbA.toString();
            StringBuilder sbV = new StringBuilder();
            sbV = new StringBuilder();
            sbV.append("get")
                    .append(valeur.substring(0, 1).toUpperCase())
                    .append(valeur.substring(1));
            String getterValeur = sbV.toString();
            String jsons = "[{";
            for (int i = 0; i < liste.length; i++) {
                Method getter = clazz.getMethod(getterAffiche);
                Method getter2 = clazz.getMethod(getterValeur);
                jsons += "value : '" + getter.invoke(liste[i]) + "',";
                jsons += "label:'" + getter.invoke(liste[i]) + "',";
                jsons += "id:'" + getter2.invoke(liste[i]) + "'";
                if (i < liste.length - 1) {
                    jsons += "},{ ";
                }
            }
            jsons += "}]";
            u.getMapAutoComplete().put(this.getNom(), jsons);
            System.out.println(jsons);
            System.out.println(isAutoComplete());
            if(indice==1) c.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally{
            if(indice == 1 && c!=null)c.close();
        }

    }

    /**
     * 
     * @return si le champ est autocomplete
     */
    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }
    /**
     * prendre champ return pour les champs dans une table
     *<p>
     *Utilisé dans la géneration de table de formulaire
     *</p>
     * @param indexe indice de la ligne
     * @return champ return du champ pour la ligne donnée
     * @throws Exception
     */
    public String getChampReturn(int indexe) throws Exception {
        String champ = "";
        String[] tchampReturn = Utilitaire.split(champReturn, ";");
        for(int i=0; i<tchampReturn.length; i++){
            String[] champRet = tchampReturn[i].split("_");
            if(champRet.length > 1 && Utilitaire.checkNumber(champRet[1])){
                champ += tchampReturn[i];
            }else{
                champ += tchampReturn[i]+"_"+indexe;
            }
            if(i != tchampReturn.length-1)
                champ += ";";
        }
        return champ;
    }
    /**
     * La géneration de l'input devrait se faire ici mais non
     * @param indice indice de la ligne
     * @throws Exception
     */
    public void makeHtmlInsertTableau(int indice) throws Exception {
    }
    /**
     * génerer le html du champ pour une ligne donnée
     * @param i indice de la ligne
     * @param valeur valeur du champ
     * @return
     * @throws Exception
     */
    public String getHtmlTableauInsert(int i, String valeur) throws Exception {
        htmlTableauInsert = null;
        makeHtmlInsertTableau(i, valeur);
        if (htmlTableauInsert == null || htmlTableauInsert.compareToIgnoreCase("") == 0) {
            htmlTableauInsert = getHtmlTableau(i, valeur);
        }
        return htmlTableauInsert;
    }
    
    public void makeHtmlInsertTableau(int indice, String valeur) throws Exception {
    }
    
    /**
     * 
     * @param i
     * @param valeur
     * @return
     * @throws Exception
     */
    public String getHtmlTableau(int i, String valeur) throws Exception {
        this.makeHtmlTableau(i, valeur);
        return htmlTableau;
    }
    /**
     * devrait être utilisé pour génerer le html du champ
     * pour une ligne de table
     * @param i indice de la ligne
     * @param valeur valeur du champ
     * @throws Exception
     */
    public void makeHtmlTableau(int i, String valeur) throws Exception {
        String temp = "";
        setValeur(valeur);
        if (type.compareTo(ConstanteAffichage.textarea) == 0) {
            temp = "<textarea name='" + getNom() + "_" + i + "' class='" + getCss() + "' id='" + getNom() + "_" + i + "' style='resize: none;'>" + getValeur() + "</textarea>";
        } else if (typeData.compareTo(ConstanteAffichage.typeDaty) == 0) {
            temp = "<input name=" + getNom() + "_" + i + " type=" + getType() + " class='" + getCss() + " datepicker'  id=" + getNom() + "_" + i + " value='" + getValeur() + "' " + getAutre() + ">";

        } else if (!this.isPhoto()) {
            temp = "<input name=" + getNom() + "_" + i + " type=" + getType() + " class=" + getCss() + "  id=" + getNom() + "_" + i + " value='" + getValeur() + "' " + getAutre() + ">";

        } else {
            temp = "<input name=" + getNom() + "_" + i + " type=\"file\" class=" + getCss() + " id=" + getNom() + "_" + i + " " + getAutre() + " data-text=\"Sélectionnez votre fichier\">";
            if (!getValeur().isEmpty()) {
                temp += "<input type=\"hidden\" value=\"" + getValeur() + "\" name=\"" + getNom() + "_" + i + "2" + "\">";
            }
        }
        setHtmlTableau(temp);
    }
    public void setPageAppelIndice(String pageAppel, int i) {
        this.pageAppel = pageAppel;
        String[] tGetNom = this.getNom().split("_");
        if(tGetNom.length > 0)
            this.champReturn = this.getNom() + ";" + tGetNom[0]+"libelle_"+ tGetNom[1];
    }
    /**
     * definir un champ en autocomplete
     * @param affiche colonne à afficher
     * @param valeur colonne de valeur
     * @param nomTable nom de la table
     * @param aWhere filtre SQL
     * @throws Exception
     */
    public void setAutocomplete(String affiche, String valeur, String nomTable, String aWhere) throws Exception {
        this.ac_affiche = affiche;
        this.ac_valeur = valeur;
        this.ac_nomTable = nomTable;
        this.autocomplete = true;
        this.ac_aWhere = aWhere;
    }
    /**
     * transformer les données de l'autocomplete
     * en chaine de caractère JSON
     */
    public void transformToStringAc() {
        String jsons = "";
        if (getTab_ac() != null && getTab_ac().length > 0) {
            jsons += "[";
            for (int i = 0; i < getTab_ac().length; i++) {
                if(getTab_ac()[i].getAffiche() != null){
                    jsons += "\"" + getTab_ac()[i].getAffiche().toString().replace("'", " ").replace("\"", " ") + " :: " + getTab_ac()[i].getValeur().toString().replace("'", " ").replace("\"", " ") + "\"";

    //jsons += "\"" + getTab_ac()[i].getAffiche().toString().replace("'", " ") + "\"";
                    if (i < getTab_ac().length - 1) {
                        jsons += ", ";
                    }
                }
            }
            jsons += "]";
            //System.out.println("JSONNNN "+jsons);
            this.setValeur_ac(jsons);

        }
    }

    
    /**
     * Définir un popup pour une liste de champs 
     * Utilisé pour les affichage mère - fille
     * @param tChamp liste des champs
     * @param pageAppel lien de page appel
     * @param champReturn champ return
     * @param isautocomplete ne sert à rien
     * @throws Exception
     */
    public static void setPageAppel(Champ[] tChamp, String pageAppel, String champReturn, boolean isautocomplete) throws Exception {
        for(Champ c : tChamp){
            //c.autocomplete = isautocomplete;
            c.pageAppel = pageAppel;
            c.champReturn = champReturn;
        }
    }

public String getAutocompleteDynamiqueJs() {
    if (this.isAutocompleteDynamique()) {
        String nomlib = getNom() + "libelle";
        String nom = getNom();
        String acAffiche = getAc_affiche();
        String acValeur = getAc_valeur();
        System.out.println(nomlib+" "+nom);
        String[] maps = getChampRetourMapping().split(";");
        String rep =  "$(function() {\n" +
               "    var autocompleteTriggered = false;\n" +
               "    $(\"#" + nomlib + "\").autocomplete({\n" +
               "        source: function(request, response) {\n" +
               "            $(\"#" + nom + "\").val('');\n" +  
               "            if (autocompleteTriggered) {\n" +
               "                fetchAutocomplete(request, response, \"" + acAffiche + "\", \"" + acValeur + "\", \"" + acAffiche + "\", \"" + getAc_nomTable() + "\", \"" + getAc_classeMapping() + "\", \"" + isUseMotCle() + "\",\""+getChampRetour()+"\");\n" +
               "            }\n" +
               "        },\n" +
               "        select: function(event, ui) {\n" +
               "            $(\"#" + nomlib + "\").val(ui.item.label);\n" +
               "            $(\"#" + nom + "\").val(ui.item.value);\n" +
               "            $(\"#" + nom + "\").trigger('change');\n" +
               "            $(this).autocomplete('disable');\n" ;
        rep += "            var champsDependant = ["+Arrays.asList(champRetourMapping.split(";")).stream().map(v-> "'"+v+"'").collect(Collectors.joining(",")) +"];" +
               "for(let i=0;i<champsDependant.length;i++){\n" +
               "        $(`#${champsDependant[i]}`).val(ui.item.retour.split(';')[i]);\n }";
        rep += "            autocompleteTriggered = false;\n" +
               "            return false;\n" +
               "        }\n" +
               "    }).autocomplete('disable');\n" +
               "    $(\"#" + nomlib + "\").keydown(function(event) {\n" +
               "        if (event.key === 'Tab') {\n" +
               "            event.preventDefault();\n" +
               "            autocompleteTriggered = true;\n" +
               "            $(this).autocomplete('enable').autocomplete('search', $(this).val());\n" +
               "        }\n" +
               "    });\n" +
               "    $(\"#" + nomlib + "\").on('input', function() {\n" +
               "        $(\"#" + nom + "\").val('');\n" +
               "        autocompleteTriggered = false;\n" +
               "        $(this).autocomplete('disable');\n" +
               "    });\n" +
               "    $(\"#" + getNom() + "searchBtn\").click(function() {\n" +
               "        autocompleteTriggered = true;\n" +
               "        $(\"#" + nomlib + "\").autocomplete('enable').autocomplete('search', $(\"#" + nomlib + "\").val());\n" +
               "    });\n" +
               "});";
        return rep;
    } else {
        return "";
    }
}

}
