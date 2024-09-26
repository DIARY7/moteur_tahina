package affichage;

import bean.AnalyseLien;
import bean.ClassMAPTable;
import bean.ListeColonneTable;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import user.UserEJB;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import constante.ConstanteAffichage;
/**
 * Classe pour génerer un formulaire à partir d'un objet de base.
 * Cette classe est utilisée dans les enfants de Page : formulaire de recherche, formulaire d'insertion,
 * formulaire d'update.
 * 
 * Cette classe permet l'interaction avec les champs du formulaire.
 * 
 * <pre>
 *  {@code
 *   pr.getFormu().getChamp("test").setLibelle("Test")
 * }
 * </pre>
 *
 * @author BICI
 * @version 1.0
 */
public class Formulaire {

    private Champ[] listeChamp;
    private Champ[][] listeChampTableau;
    private String id;
    private int nbRangee = 2;
    private String html, htmlNew;
    private String tailleTableau = "100%";
    private String cssTableau = "monographe";
    private String cssClass;
    private bean.ClassMAPTable objet;
    private Champ[] crtFormu;
    private int nbIntervalle = 0;
    private String htmlTri;
    private String htmlButton;
    private String htmlInsert;
    private String htmlAutoComplete;
    private Champ[] champGroupe;
    private Champ[] champTableauAff;
    private String htmlGroupe;
    private String htmlTableauAff, htmlTableauAffNew;
    private String htmlTableauInsert;
    private int nbLigne = 0;
    String recapcheck = "";
    public String lang = "fr";
    boolean estFille=false;
    String[] colOrdre=null;
    private bean.ClassMAPTable[] dataFille;
    private AnalyseLien[] liens;
    private String currentEtat;
    private String[] etatAff;
    private String[] etatVal;

    
    public String getCurrentEtat() {
        return currentEtat;
    }

    public void setCurrentEtat(String currentEtat) {
        this.currentEtat = currentEtat;
    }

    public String[] getEtatAff() {
        return etatAff;
    }

    public void setEtatAff(String[] etatAff) {
        this.etatAff = etatAff;
    }

    public String[] getEtatVal() {
        return etatVal;
    }

    public void setEtatVal(String[] etatVal) {
        this.etatVal = etatVal;
    }

    public AnalyseLien[] getLiens() {
        return liens;
    }

    public void setLiens(AnalyseLien[] liens) {
        this.liens = liens;
    }
    
    
    /**
     * 
     * @return liste des valeurs du tableau fille du formulaire
     */
    public ClassMAPTable[] getDataFille() {
        return dataFille;
    }

    public void setDataFille(ClassMAPTable[] dataFille) {
        this.dataFille = dataFille;
    }
    
    
    
    /**
     * 
     * @return ordre d'affichage des colonnes
     */
    public String[] getColOrdre() {
        return colOrdre;
    }

    public void setColOrdre(String[] colOrdre) {
        this.colOrdre = colOrdre;
    }

    /**
     * 
     * @return si le formulaire est une formulaire fille d'une classe fille
     */
    public boolean isEstFille() {
        return estFille;
    }

    public void setEstFille(boolean estFille) {
        this.estFille = estFille;
    }
    
    String[]ordre;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String[] getOrdre() {
        return ordre;
    }

    public String getHtmlNew() throws Exception {
        if (htmlNew == null || htmlNew.compareToIgnoreCase("") == 0) {
            this.makeHtmlNew();
        }
        return htmlNew;
    }

    public void setHtmlNew(String htmlNew) {
        this.htmlNew = htmlNew;
    }

    public String getHtmlTableauAffNew() {
        return htmlTableauAffNew;
    }

    public void setHtmlTableauAffNew(String htmlTableauAffNew) {
        this.htmlTableauAffNew = htmlTableauAffNew;
    }
    
    
    /**
     * Prendre la liste de champs correspondant à un attribut donné
     * @param nom nom de l'attribut
     * @return liste de champs qui correspond au valeur de l'attribut
     * @throws Exception
     */
    public ListeChamp getChampMulitple(String nom)throws Exception
    {
        List<Champ> lc=new ArrayList<>();
        for(Champ ch:this.getListeChamp())
        {
            String[] split=utilitaire.Utilitaire.split(ch.getNom(), "_");
            String gauche="";
            for(int i=0;i<split.length-1;i++)
            {
                gauche=gauche+split[i];
            }
            if(split.length>=2&&gauche.compareToIgnoreCase(nom)==0)
            {
                lc.add(ch);
            }
        }
        ListeChamp lca=new ListeChamp(this,lc);
        return lca;
    }
    
    /**
     * ordonner la liste de champs du formulaire
     * @param ordre liste des colonnes du formulaire ordonné selon ordre d'affichage
     */
    public void setOrdre(String[] ordre) {
        this.ordre = ordre;
        Champ[]listeVaovao=new Champ[getListeChamp().length];
        int iVao=0;
        for(int i=0;i<ordre.length;i++)
        {
            listeVaovao[iVao]=listeChamp[iVao];
            for(int iListe=0;iListe<listeChamp.length;iListe++)
            {
                if(listeChamp[iListe].getNom().compareToIgnoreCase(ordre[i])==0)
                {
                    listeVaovao[iVao]=listeChamp[iListe];
                    iVao++;
                }
            }
        }
        setListeChamp(listeVaovao);
    }
    /**
     * Modifier la place d'un champs de formulaire
     * @param nom nom du champs
     * @param rang nouvelle place dans la liste de champs du formulaire
     */
    public void insererChamp(String nom, int rang)
    {
        List<Champ> liste=new ArrayList();
        liste.addAll(Arrays.asList(listeChamp));
        int i=1;
        for(Champ c:liste)
        {
            if(c.getNom().compareToIgnoreCase(nom)==0)
            {
                liste.add(rang, c);
                liste.remove(i);
                break;
            }
            i++;
        }
        liste.toArray(listeChamp);
    }
    

    public String getHtmlAutoComplete() {
        return htmlAutoComplete;
    }

    public void setHtmlAutoComplete(String htmlAutoComplete) {
        this.htmlAutoComplete = htmlAutoComplete;
    }

    public String getRecapcheck() {
        return recapcheck;
    }

    public void setRecapcheck(String recapcheck) {
        this.recapcheck = recapcheck;
    }

    /**
     * Pour les tableaux
     * @return nombre de ligne à initialiser ou nombre de ligne du formulaire
     */
    public int getNbLigne() {
        return nbLigne;
    }

    public void setNbLigne(int nbLigne) {
        this.nbLigne = nbLigne;
    }

    public Formulaire() {
    }
    /**
     * modifier un tableau de nom en tableau de champs
     * @param list liste de noms des champs
     * @return
     */
    public static Champ[] transformerEnChamp(String[] list) {
        Champ[] ret = new Champ[list.length];
        for (int i = 0; i < list.length; i++) {
            ret[i] = new Champ(list[i]);
        }
        return ret;
    }
    
   public void mergeCritereFormu(Champ[] list) throws Exception {
        System.out.println("Changer en champs");
        int nombre = list.length;
        for (int i = 0; i < nombre; i++) {
            for (int j = 0; j < crtFormu.length; j++) {
                boolean condition=crtFormu[j].getNom().compareToIgnoreCase(list[i].getNom()) == 0;
                if (condition==true) {
                    crtFormu[j].setEstMultiple(list[i].getEstMultiple());
                                          
                }
            }
        }
    }
    
    
    /**
     * copier les valeurs des champs du formulaire dans une liste de champs selon le nom de champs
     * @param list liste des champs
     * @throws Exception
     */
    public void changerEnChamp(Champ[] list) throws Exception {
        int nombre = list.length;
        for (int i = 0; i < nombre; i++) {
            for (int j = 0; j < listeChamp.length; j++) {
                boolean condition=listeChamp[j].getNom().compareToIgnoreCase(list[i].getNom()) == 0;
                if(this.isEstFille()==true) condition=listeChamp[j].getNom().startsWith(list[i].getNom()+"_");
                String valeurChamp=listeChamp[j].getValeur();

                if (condition==true) {
                    if (list[i].getValeur() == null || list[i].getValeur().compareToIgnoreCase("") == 0) {
                        list[i].setValeur(listeChamp[j].getValeur());
                    }
                    if (list[i].getNom().compareToIgnoreCase("mois2") == 0) {
                        ((Liste) (list[i])).setDefaultSelected("12");
                        //System.out.println("niditra "+i+" nom = "+list[i].getNom());
                    }
                    if(this.isEstFille()==false){
                        listeChamp[j] = list[i];
                        break;
                    }
                    //("unite",unit,"desce","id");
                    Liste lis=(Liste)list[i];
                    //System.out.println("------------------"+lis.getAutre());
                    listeChamp[j]=new Liste(listeChamp[j].getNom(),lis.getSingleton(),lis.getColAffiche(),lis.getColValeur());
                    listeChamp[j].setValeur(valeurChamp);
                    listeChamp[j].setAutre(lis.getAutre());
                    listeChamp[j].setEstMultiple(list[i].getEstMultiple());
                                          
                }
            }
        }
    }
    /**
     * 
     * @param listeC liste des champs à mettre sur le formulaire
     * @param range nombre de rangé à afficher en disposition des champs
     */
    public Formulaire(Champ[] listeC, int range) {
        setListeChamp(listeC);
        setNbRangee(range);
    }
    /**
     * 
     * @param listeC liste des champs à mettre sur le formulaire
     * @param range nombre de rangé à afficher en disposition des champs
     */
    public Formulaire(String[] listeC, int range) {
        setListeChamp(transformerEnChamp(listeC));
        setNbRangee(range);
    }
    /**
     * @deprecated ne retourne rien, juste un string vide
     */
    public String makeHtmlForm() throws Exception {
        String retour = "";
        //retour+="<form action="++"?but="++" method='post' name='formu'";
        return retour;
    }
    /**
     * Génerer le html de tri d'un formulaire.
     * 
     * @throws Exception
     */
    public void makeHtmlTri() throws Exception {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String temp = "";
        temp += "<div class='col-md-3 triecolonne'>";
        temp += "<div class='box box-solid collapsed-box'>";
        temp += "<div style='background:#103a8e; color:white'class='box-header with-border'>";
        temp += "<h3 class='box-title' color='#103a8e'><span color='#103a8e'>" + RB.getString("tri") + "</span></h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body'>";
        temp += "<div class='form-group'>";
        temp += "<div class='col-xs-5'>";
        temp += "<label for='Colonne'>Colonne</label>";
        temp += getListeChamp()[getListeChamp().length - 2].getHtml();
        temp += "</div>";
        temp += "<div class='col-xs-5'>";
        temp += "<label for='Ordre'>Ordre</label>";
        temp += getListeChamp()[getListeChamp().length - 1].getHtml();
        temp += "</div>";
        temp += "</div>";
        temp += "</div></div>";
        setHtmlTri(temp);
    }
    /**
     * Génerer la liste de bouton d'un formulaire.
     * <p>
     * Les boutons seront : Reinitialiser, Afficher, Enregistrer
     * </p>
     * @return
     */
    public String makeHtmlButton() {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        //System.out.println("NIDITRA HTML Bouton "+getRecapcheck());
        String temp = "";
//        temp += "<input type='hidden' name='premier' value='false'/>";

        temp += "<div class='col-xs-4' align='center'>";
        temp += "<button type='reset' class='btn btn-default'>" + RB.getString("reinitialiser") + "</button>";
        temp += "</div>";
        temp += "<div class='col-xs-4' align='center'>";
        temp += "<input name='afficher' value='"+RB.getString("afficher")+"' type='submit' class='btn' style='background:"+ ConstanteAffichage.couleurPrimaire +"; color:white' id='btnListe'/>";
        temp += "</div>";
        return temp;
    }
    
    /**
     * @return liste des champs qui sont des intervalles dans le formulaire
     */
    public Champ[] getListeIntervalle() {
        Vector ret = new Vector();
        for (int i = 0; i < getListeChamp().length; i++) {
            if (getListeChamp()[i].getEstIntervalle() == 1) {
                ret.add(getListeChamp()[i]);
            }
        }
        Champ[] retour = new Champ[ret.size()];
        ret.copyInto(retour);
        return retour;
    }
    /**
     * Génerer le corps du formulaire avec tri, 
     * choix des colonnes à afficher et choix si recapitulation à afficher ou pas,
     * à la fin la liste de bouton "Reinitialiser, Afficher, Enregistrer"
     * @return html des formulaires 
     */
    public String getHtmlEnsemble() throws Exception {
        String temp = "";
        temp += "<div class='row'>";

        temp += "<div class='row col-md-12'>";
        temp += getHtml();
        temp += "<div class='box-footer'>";
        makeHtmlGroupe();
        temp += getHtmlGroupe();
        
        makeHtmlTableauAff();
        temp += getHtmlTableauAff();
        temp += getHtmlTri();
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='row col-md-12'>";
        temp += makeHtmlButton();
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";

        temp += "</div>";
        //temp+="</div>";
        return temp;
    }
    
    public String getHtmlEnsembleNew() throws Exception {
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class=\"filter p-3 mt-3 mb-3\">";
        temp += getHtmlNew();
        makeHtmlGroupe();
        temp += getHtmlGroupe();
        makeHtmlTableauAffNew();
        temp += getHtmlTableauAffNew();
        temp += "</div>";
        temp+="</div>";
        return temp;
    }

        public String getHtmlSimple() throws Exception {
        String temp = "";
        temp += "<div class='row'>";

        temp += "<div class='row col-md-12'>";
        temp += getHtml();
        temp += "<div class='box-footer'>";

        
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='row col-md-12'>";
        temp += makeHtmlButton();
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        //temp+="</div>";
        return temp;
    }

    /**
     * trouver un champ dans le formulaire
     * @param nomChamp nom du champs
     * @return
     */
    public Champ getChamp(String nomChamp) {
        for (int i = 0; i < getListeChamp().length; i++) {
            if (getListeChamp()[i].getNom().compareToIgnoreCase(nomChamp) == 0) {
                return getListeChamp()[i];
            }
        }
        return null;
    }

    /**
     * mettre à jour les libellés des champs
     * @param libelle liste des libellés ordonnés selon l'ordre des champs
     * @throws Exception
     */
    public void changerLibelle(String[] libelle) throws Exception {
        if (libelle.length != getListeChamp().length) {
            throw new Exception("nombre de champ different de linitial");
        }
        for (int i = 0; i < getListeChamp().length; i++) {
            getListeChamp()[i].setLibelle(libelle[i]);
        }
    }

    /**
     * Génerer les champs à utiliser pour le formulaire
     * @param crt liste des champs de critère pour le formulaire
     * @param listeInterval liste des intervalles de champs
     * @param nbRange nombre de colonne sur le formattage du formulaire
     * @throws Exception
     */
    public void makeChampFormu(String[] crt, String[] listeInterval, int nbRange) throws Exception {
        setCrtFormu(transformerEnChamp(crt));
        Vector liste = new Vector();
        for (int i = 0; i < crt.length; i++) {
            //System.out.println("critere = "+crt[i]+" get Champ =========="+getObjet()+"======= "+ListeColonneTable.getChamp(getObjet(),crt[i]));
            String type = ListeColonneTable.getChamp(getObjet(), crt[i]).getTypeColonne();
            //System.out.println("type ="+type);
            if (Utilitaire.estIlDedans(crt[i], listeInterval) != -1) //On fait des intervalles pour les valeurs dates et montant
            {
                getCrtFormu()[i].setEstIntervalle(1);
                setNbIntervalle(getNbIntervalle() + 1);
                String t1 = crt[i] + "1";
                String t2 = crt[i] + "2";
                // ==== nampiana 
                Champ champ1 = new Champ(t1);
                Champ champ2 = new Champ(t2);
                // ==== nampiana 
                if (type.compareToIgnoreCase("Date") == 0) {
                    getCrtFormu()[i].setType(ConstanteAffichage.typeDaty);
                    // ==== nampiana 
                    champ1.setType(ConstanteAffichage.typeDaty);
                    champ1.setTypeData(ConstanteAffichage.typeDaty);
                    champ2.setType(ConstanteAffichage.typeDaty);
                    champ2.setTypeData(ConstanteAffichage.typeDaty);
                    champ1.setType("text");
                    champ2.setType("text");
                    // ==== nampiana 
                }
                // ==== niova 
                liste.add(champ1);
                liste.add(champ2);
                // ==== niova 
            } else {
                liste.add(new Champ(crt[i]));
            }
        }
        bean.Champ[] nomCol = ListeColonneTable.getFromListe(getObjet(), null);
        Champ col = new Liste("colonne", nomCol);
        String[] val = {"-", "DESC", "ASC"};
        Champ ord = new Liste("ordre", val);
        liste.add(col);
        liste.add(ord);
        Champ[] listeForm = new Champ[liste.size()];
        liste.copyInto(listeForm);
        setListeChamp(listeForm);
        setNbRangee(nbRange);
    }
    /**
     * @deprecated
     */
    public void makeChampFormuInsert() throws Exception {
    }
    /**
     * Génerer le script JQuery d'autocomplete des champs autocomplete
     * @param u session de l'utilisateur courant
     * @return
     * @throws Exception
     */
    public String makeHtmlAutoComplete(UserEJB u) throws Exception {
        String html = "";
        html += "$(document).ready(function () {";
        Champ[] champListe = this.getListeChamp();
        int count =1;
        for (int i = 0; i < champListe.length; i++) {
            
            String[] valColonne = champListe[i].getNom().split("_");
            String nameInput = "";
            if(valColonne.length > 1 && Utilitaire.checkNumber(valColonne[1])){
                nameInput = valColonne[0]+"libelle_"+valColonne[1];
            }else{
                nameInput = champListe[i].getNom()+"libelle";
            }
            
            if (champListe[i].isAutoComplete() && champListe[i].getPageAppel() != null && champListe[i].getPageAppel().compareToIgnoreCase("") != 0) {
                String liste =u.getMapAutoComplete().get(champListe[i].getNom());
                html += "var liste"+count+" = " + liste+ "; ";
                html += "$('#"+nameInput+"').autocomplete({";
                html += "source: liste"+count+",";
                html += "minLength: 2,";
                html += "select : function(event,ui){";
                html += "$('#"+nameInput+"').val(ui.item.id);";
                html += "}";
                html += "});";
            }else if(champListe[i].isAutoComplete()){
                String liste =u.getMapAutoComplete().get(champListe[i].getNom());
                html += "var liste"+count+" = " + liste+ "; ";
                html += "$('#"+champListe[i].getNom()+"libelle').autocomplete({";
                html += "source: liste"+count+",";
                html += "minLength: 2,";
                html += "select : function(event,ui){";
                html += "$('#"+champListe[i].getNom()+"').val(ui.item.id);";
                html += "}";
                html += "});";
            }
            count++;
        }
        html += "});";
        setHtmlAutoComplete(html);
        return html;
    }
    /**
     * Génere le html du corps du formulaire principal des champs
     * @throws Exception
     */
    public void makeHtml() throws Exception {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        String temp = "";
        //temp+="<form action="++"?but="++" method='post' name='formu'";
        //temp+="<table width="+getTailleTableau()+" border='0' align='center' cellpadding=2 cellspacing=0 class="+getCssTableau()+">";
        temp += "<div class='box box-solid collapsed'>";
        temp += "<div style='background:#103a8e; color:white;'class='box-header with-border'>";
        temp += "<h3 class='box-title' color='#103a8e'><span color='#103a8e'>" + RB.getString("Recherche_avance") + "</span></h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body' id='pagerecherche'>";
        int reste = listeChamp.length % getNbRangee();
        int nombreLigne = 0;
        if (reste != 0) {
            nombreLigne = listeChamp.length / getNbRangee();
        } else {
            nombreLigne = (listeChamp.length / getNbRangee()) + 1;
        }
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<div class='form-group'>";
            for (int j = 0; j < getNbRangee(); j++) {
                temp += "<div class='col-md-4'>";
                if (((i * getNbRangee()) + j) >= listeChamp.length - 2) {
                    temp += "</div>";
                    break;
                };
                Champ c = listeChamp[(i * getNbRangee()) + j];
                c.setAutre(c.getAutre()+" onkeydown='return searchKeyPress(event)'");
                String troisiemeTd = "";
//                if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
//                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getNom() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
//                }
                if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getNom() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                }
                temp += "<label for=" + c.getLibelleAffiche() + ">" + c.getLibelleAffiche() + "</label>";
                temp += c.getHtml();
                temp += troisiemeTd;
                temp += "</div>";
            }
            temp += "</div>";
        }
        temp += "</div>";
        setHtml(temp);
    }
    
    private String getFiltreEtat() {
        String temp = "";
        if(etatVal!=null){
           temp = "<div class=\"filter-header d-flex flex-row\">\n";
           for(int i=0;i<etatVal.length;i++){
                String selected = "";
                if((currentEtat == null && i == 0) || etatVal[i].equals(currentEtat)){
                    selected = "checked";
                }
                temp += "          <div class=\"type text-center etat "+selected+"\" onclick=\"changeEtat('"+etatVal[i]+"')\" value=\""+etatVal[i]+"\">"+etatAff[i]+"</div>\n";
           }
           temp+="</div>";
           temp+="<input type=\"hidden\" id=\"etat_table\" name=\"etat_table\" />";
        }
        return temp;
    }
    private String getHtmlRechercheButton() {
        String temp = "";
        temp += "<div class=\"form-group p-3 col-md-3 col-sm-6 d-flex align-items-center justify-content-start filter-option \">\n" +
"                    <img src='../assets/img/Show-filter.svg' alt=\" \" srcset=\" \" class=\"show-btn\">\n";
        temp += "<input name='afficher' value='Rechercher' type='submit' class='btn btn-search' style='background:"+ ConstanteAffichage.couleurPrimaire +"; color:white' id='btnListe'/>";
        temp += "<button type='reset' class='btn btn-reset'>" +"reinitialiser" + "</button>" +
        "</div>";
        return temp;
    }
    public void makeHtmlNew() throws Exception{
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class=\"filter p-3 mt-3 mb-3\">";
         temp += getFiltreEtat();
        temp += "<div class=\"filter-body d-flex flex-wrap align-items-end p-1 \">";
        int nombreLigne = listeChamp.length - 2;
        for (int i = 0; i < nombreLigne; i++) {
            for (int j = 0; j < 1; j++) {
                String hideChamp = i > 2 ? "hidden-input" : "";
                temp += "<div class='form-group p-3 col-md-3 col-sm-6 "+hideChamp+"'>";
//                if (((i * getNbRangee()) + j) >= listeChamp.length - 2) {
//                    temp += "</div>";
//                    break;
//                };
                Champ c = listeChamp[i];
                c.setAutre(c.getAutre()+" onkeydown='return searchKeyPress(event)'");
                String troisiemeTd = "";

                if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getNom() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                }
                temp += "<label for=" + c.getLibelleAffiche() + ">" + c.getLibelleAffiche() + "</label>";
                temp += c.getHtml();
                temp += troisiemeTd;
                temp += "</div>";
            }
            if(i == 2) {
                temp += getHtmlRechercheButton();
            }
//            temp += "</div>";
        }
        if(nombreLigne <= 2){
            temp += getHtmlRechercheButton();
        }
        temp += "</div>";
        temp+="</div>";
        setHtmlNew(temp);
    }

    /**
     * Génerer la liste des champs à afficher de base pour la partie de choix de champs à afficher du
     * formulaire
     * @param nbTabAffiche nombre de colonne à afficher
     * @param defaut les colonnes par défaut à afficher
     * @throws Exception
     */
    public void makeChampTableauAffiche(int nbTabAffiche, String[] defaut) throws Exception {
        Champ[] listeAff = new Champ[nbTabAffiche];
        bean.Champ[] nomCol = ListeColonneTable.getFromListe(getObjet(), null);
        String valeur[] = new String[nomCol.length + 1];
        valeur[0] = "-";
        for (int k = 0; k < nomCol.length; k++) {
            valeur[k + 1] = nomCol[k].getNomColonne();
        }
        for (int i = 0; i < nbTabAffiche; i++) {
            listeAff[i] = new Liste("colAffiche" + (i + 1), "Colonne " + (i + 1), valeur);
            //System.out.println("colAffiche"+(i+1)+" == "+valeur);
            ((Liste) (listeAff[i])).setDefaultSelected(defaut[i]);
        }
        setChampTableauAff(listeAff);
    }
    /**
     * Génerer les champs à utiliser pour les recherches groupés (colonne, ligne, somme)
     * @param nombreChamps nombre de champs de colonne et de ligne
     * @param nbsomme nombre de champs de sommes
     * @throws Exception
     */
    public void makeChampGroupe(int nombreChamp, int nbsomme) throws Exception {
        Champ[] listeGroupe = new Champ[(nombreChamp*2) + nbsomme];
        bean.Champ[] nomCol = ListeColonneTable.getFromListe(getObjet(), null);
        String valeur[] = new String[nomCol.length + 1];
        valeur[0] = "-";
        for (int k = 0; k < nomCol.length; k++) {
            valeur[k + 1] = nomCol[k].getNomColonne();
        }
        //Champ col=new Liste("colonne",nomCol);
        //System.out.println("nombre="+nombreChamp+"nbsomme="+nbsomme+"nombreChamp+nbsomme="+(nombreChamp+nbsomme));
        for (int i = 0; i < (nombreChamp*2) + nbsomme; i++) {
            if (i < nombreChamp) 
            {
                listeGroupe[i] = new Liste("colGroupe" + (i + 1), "Ligne Group&eacute;e " + (i + 1), valeur);
                
            } 
            else if (i >= nombreChamp&&i<nombreChamp*2) 
            {
                listeGroupe[i] = new Liste("ligneGroupe" + (i + 1), "Colonne Group&eacute;e " + (i-nombreChamp + 1), valeur);
                
            } 
            else 
            {
                listeGroupe[i] = new Liste("colSomme " + (i + 1-(nombreChamp*2)), valeur);
            }
        }
        setChampGroupe(listeGroupe);
    }

    /**
     * Génerer le html de la partie choix des colonnes à afficher
     * @throws Exception
     */
    public void makeHtmlTableauAff() throws Exception {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        if (getChampTableauAff() == null) {
            setHtmlTableauAff("");
            return;
        }
        String temp = "";
        temp += "<div class='row col-md-12'></div>";
        temp += "<div class='row col-md-12 containerColAffTrie'>";
        temp += "<div class='col-md-6 tableauaffiche'>";
        temp += "<div class='box box-solid collapsed-box'>";
        temp += "<div style='background-color:#103a8e; color:white' class='box-header with-border'>";
        temp += "<h3 class='box-title'>" + RB.getString("Choix_col") + "</h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body' id='pagerecherche'>";
        int reste = getChampTableauAff().length % getNbRangee();
        int nombreLigne = 0;
        if (reste != 0) {
            nombreLigne = getChampTableauAff().length / getNbRangee() + 1;
        } else {
            nombreLigne = (getChampTableauAff().length / getNbRangee());
        }
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<div class='col-md-3'>";
            temp += "<div class='form-group'>";
            for (int j = 0; j < getNbRangee(); j++) {
                if (((i * getNbRangee()) + j) == getChampTableauAff().length) {
                    break;
                }
                Champ c = getChampTableauAff()[(i * getNbRangee()) + j];
                temp += "<label for=" + c.getNom() + ">" + c.getLibelleAffiche() + "</label>";
                temp += c.getHtml();
            }
            temp += "</div>";
            temp += "</div>";
        }
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "<div class='col-md-3' align='center'>";
        temp += "<div class='box box-solid'>";
        temp += "<div style='background-color:#103a8e; color:white;'class='box-header with-border'>";
        temp += "<h3 class='box-title'><input type='checkbox' name='recap' value='checked' " + getRecapcheck() + "> &nbsp; " + RB.getString("recapitulation") + "</h3>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";

        setHtmlTableauAff(temp);
    }
    
    public void makeHtmlTableauAffNew() throws Exception {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));

        if (getChampTableauAff() == null) {
            setHtmlTableauAff("");
            return;
        }
        String temp = "";
       
     
        int reste = getChampTableauAff().length % getNbRangee();
        int nombreLigne = 0;
        if (reste != 0) {
            nombreLigne = getChampTableauAff().length / getNbRangee() + 1;
        } else {
            nombreLigne = (getChampTableauAff().length / getNbRangee());
        }
        for (int i = 0; i < nombreLigne; i++) {
            for (int j = 0; j < getNbRangee(); j++) {
                if (((i * getNbRangee()) + j) == getChampTableauAff().length) {
                    break;
                }
                temp += "<div class='form-group p-3 col-md-4 col-sm-6 hidden-input'>";
                Champ c = getChampTableauAff()[(i * getNbRangee()) + j];
                temp += "<label for=" + c.getNom() + ">" + c.getLibelleAffiche() + "</label>";
                temp += c.getHtml();
                temp += "</div>";
            }
        }

        setHtmlTableauAffNew(temp);
    }
    /**
     * Génerer le html de la partie groupage et somme
     * @throws Exception
     */
    public void makeHtmlGroupe() throws Exception {
        if (getChampGroupe() == null) {
            setHtmlGroupe("");
            return;
        }
        //temp+="<form action="++"?but="++" method='post' name='formu'";
        String temp = "";
        temp += "<div class='row col-md-12'></div>";
        temp += "<div class='row col-md-12 containerColAffTrie'>";
        temp += "<div class='col-md-6 tableauaffiche'>";
        temp += "<div class='box box-solid collapsed-box'>";
        temp += "<div style='background-color:#103a8e; color:white'class='box-header with-border'>";
        temp += "<h3 class='box-title'>Choix des colonnes group&eacute;es &agrave; afficher</h3>";
        temp += "<div class='box-tools pull-right'><button data-original-title='Collapse' class='btn btn-box-tool' data-widget='collapse' data-toggle='tooltip' title=''><i class='fa fa-plus'></i></button> </div>";
        temp += "</div>";
        temp += "<div class='box-body' id='pagerecherche'>";
        int reste = getChampGroupe().length % getNbRangee();
        int nombreLigne = 0;
        if (reste != 0) {
            nombreLigne = getChampGroupe().length / getNbRangee() + 1;
        } else {
            nombreLigne = (getChampGroupe().length / getNbRangee());
        }
        for (int i = 0; i < nombreLigne; i++) {
            temp += "<div class='col-md-4'>";
            temp += "<div class='form-group'>";
            for (int j = 0; j < getNbRangee(); j++) {
                if (((i * getNbRangee()) + j) == getChampGroupe().length) {
                    break;
                }
                Champ c = getChampGroupe()[(i * getNbRangee()) + j];
                temp += "<label for=" + c.getNom() + ">" + c.getLibelleAffiche() + "</label>";
                temp += c.getHtml();
            }
            temp += "</div>";
            temp += "</div>";
        }
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtmlGroupe(temp);
    }

    /**
     * mettre à jour les libellés des champs du formulaire
     * @param liste liste de libellé
     */
    public void setLibelleAffiche(String[] liste) {
        for (int i = 0; i < liste.length; i++) {
            listeChamp[i].setLibelleAffiche(liste[i]);
        }
    }

    /**
     * Modifier le libellé d'un champ spécifique
     * @param libelle libellé du champs
     * @param numeroLib indice du champs à modifier
     */

    public void setLibelleAffiche(String libelle, int numeroLib) {
        listeChamp[numeroLib].setLibelleAffiche(libelle);
    }

    /**
     * @deprecated
     * Génerer le html d'un formulaire d'insert
     * @throws Exception
     */

    public void makeHtmlInsert() throws Exception {
        String temp = "";
        if (getListeChamp().length < 13) {
            temp += "<div class='row'>";
            temp += "<div class='col-md-1'></div>";
            temp += "<div class='col-md-10'>";
            temp += "<div class='box'>";
            temp += "<div class='box-body'>";
            temp += "<table class=\"table table-bordered\">";
            
            for (int i = 0; i < getListeChamp().length; i++) {
                Champ c = getListeChamp()[i];
                String troisiemeTd = "";

                if (c.getVisible() == true) {
                    temp += "<tr>";
                    temp += "<th>";
                    temp += "<label for='" + getListeChamp()[i].getLibelle() + "'>" + getListeChamp()[i].getLibelle() + "</label>";
                    temp += "</th>";
                    //System.out.println("getListeChamp()[i].getHtmlInsert() ====== " + getListeChamp()[i].getHtmlInsert());
                    //System.out.println("mbola tsy autocomplete ilay izy");
                    if (c.isAutoComplete()) {
                        System.out.println("autocomplete ilay izy");
                        temp += "<td>" + getListeChamp()[i].getInputHiddenAutoComplete() + "</td>";
                    } else if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } else {
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }

                    temp += "</tr>";
                } else {
                    getListeChamp()[i].setType("hidden");
                    temp += getListeChamp()[i].getHtmlInsert();
                }
            }
            if (getObjet().getEstHistorise()) {
                temp += "<tr>";
                temp += "<th>";
                temp += "<label for='Memo'>Memo Modification</label>";
                temp += "</th>";
                temp += "<td>";
                temp += "<textarea name='memo' class='form-control'></textarea>";
                temp += "</td>";
                temp += "</tr>";
            }
            temp += "</table>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "<div class='col-md-1'></div>";
            temp += "</div>";
        } else {
            int nb2 = getListeChamp().length / 2;

            temp += "<div class='row'>";
            temp += "<div class='col-md-6'>";
            temp += "<table class='table table-bordered'>";
            for (int i = 0; i < nb2; i++) {
                Champ c = getListeChamp()[i];
                String troisiemeTd = "";

                if (c.getVisible() == true) {
                    temp += "<tr>";
                    temp += "<th>";
                    temp += "<label for='" + getListeChamp()[i].getLibelle() + "'>" + getListeChamp()[i].getLibelle() + "</label>";
                    temp += "</th>";
                    //System.out.println("getListeChamp()[i].getHtmlInsert() ====== " + getListeChamp()[i].getHtmlInsert());

                    if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } else {
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }

                    temp += "</tr>";
                } else {
                    getListeChamp()[i].setType("hidden");
                    temp += getListeChamp()[i].getHtmlInsert();
                }
            }
            temp += "</table>";
            temp += "</div>";
            temp += "<div class='col-md-6'>";
            temp += "<table class='table table-bordered'>";
            for (int i = nb2; i < getListeChamp().length; i++) {
                Champ c = getListeChamp()[i];
                String troisiemeTd = "";

                if (c.getVisible() == true) {
                    temp += "<tr>";
                    temp += "<th>";
                    temp += "<label for='" + getListeChamp()[i].getLibelle() + "'>" + getListeChamp()[i].getLibelle() + "</label>";
                    temp += "</th>";
                    //System.out.println("getListeChamp()[i].getHtmlInsert() ====== " + getListeChamp()[i].getHtmlInsert());

                    if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } else {
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }

                    temp += "</tr>";
                } else {
                    getListeChamp()[i].setType("hidden");
                    temp += getListeChamp()[i].getHtmlInsert();
                }
            }
            temp += "</table>";
            temp += "</div>";
            temp += "</div>";
        }
        temp += getHtmlAutocomplete();
        setHtmlInsert(temp);
    }
    /**
     * Génerer le html pour un insert avec bouton ajout
     * @throws Exception
     */
    public void makeHtmlAddTabIndex() throws Exception {
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='col-md-3'></div>";
        temp += "<div class='col-md-6'>";
        temp += "<div class='box-insert-amadia'>";
        temp += "<div class='box box-primary'>";
        temp += "<div class='box-body'>";
        temp += "<table class='table table-bordered'>";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            String troisiemeTd = "";
            if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                c.setVisible(false);
            }
            if (c.getVisible() == true) {
                //temp+="<div class='form-group'>";
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<tr>";
                temp += "<th><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

                if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                    temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                } else {
                    temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                }

                if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
                temp += "</tr>";

            }
        }
        temp += "</table>";
        temp += "</div>";
        temp += "<div class='box-footer'>";
        temp += "<div class='col-xs-12'>";
        temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Ajouter</button> ";
        temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        temp += "</div></div></div></div></div>";
        temp += "<div class='col-md-3'></div>";
        temp += "</div>";
        setHtmlInsert(temp);
    }
    
    /**
     * Génerer le html pour insert avec bouton insérer et reinitialiser
     */

    public void makeHtmlInsertTabIndex() throws Exception {
        String temp = "";
        if (getListeChamp().length < 13) {

            temp += "<div class='row'>";
            temp += "<div class='col-md-3'></div>";
            temp += "<div class='col-md-6'>";
            temp += "<div class='box-insert-amadia'>";
            temp += "<div class='box'>";
            temp += "<div class='box-body'>";
            temp += "<table class=\"table table-bordered\">";
            for (int i = 0; i < getListeChamp().length; i++) {
                Champ c = getListeChamp()[i];
                
                String troisiemeTd = "";
                if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                    c.setVisible(false);
                }
                if (c.getVisible() == true) 
                {
                    c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                    temp += "<tr>";
                    temp += "<th><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

                    if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                        String autre = "";
                        if(c.getColFiltre()!=null && c.getValFiltre()!=null){
                            for(int j = 0;j<c.getColFiltre().length;j++){
                                autre+="&"+c.getColFiltre()[j]+"="+c.getValFiltre()[j];
                            }
                        }
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + autre+"') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } 
                    else {
                        
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }
                    if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {

                        troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                        temp += "<td>" + troisiemeTd + "</td>";
                    }
                    temp += "</tr>";

                }
            }
            temp += "</table>";
            temp += "</div>";
            temp += "<div class='box-footer'>";
            temp += "<div class='col-xs-12'>";
            temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Enregistrer</button> ";
            temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "<div class='col-md-3'></div>";
            temp += "</div>";
        } else {
            int nb2 = getListeChamp().length / 2;

            temp += "<div class='box-insert-amadia'>";
            temp += "<div class='box'>";
            temp += "<div class='box-body'>";

            temp += "<div class='row'>";
            temp += "<div class='col-md-6'>";
            temp += "<table class='table table-bordered'>";
            for (int i = 0; i < nb2; i++) {
                Champ c = getListeChamp()[i];
                String troisiemeTd = "";
                if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                    c.setVisible(false);
                }
                if (c.getVisible() == true) {
                    //temp+="<div class='form-group'>";
                    c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                    temp += "<tr>";
                    temp += "<th><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

                    if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                         String autre = "";
                        if(c.getColFiltre()!=null && c.getValFiltre()!=null){
                            for(int j = 0;j<c.getColFiltre().length;j++){
                                autre+="&"+c.getColFiltre()[j]+"="+c.getValFiltre()[j];
                            }
                        }
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + autre+"') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } else {
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }
                    if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                        //System.out.println("niditra tato ndray");
                        troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                        temp += "<td>" + troisiemeTd + "</td>";
                    }
                    temp += "</tr>";

                }
            }
            temp += "</table>";
            temp += "</div>";
            temp += "<div class='col-md-6'>";
            temp += "<table class='table table-bordered'>";
            for (int i = nb2; i < getListeChamp().length; i++) {
                Champ c = getListeChamp()[i];
                String troisiemeTd = "";
                if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                    c.setVisible(false);
                }
                if (c.getVisible() == true) {
                    //temp+="<div class='form-group'>";
                    c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                    temp += "<tr>";
                    temp += "<th><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

                    if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                        temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    } else {
                        temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                    }
                    if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                        //System.out.println("niditra tato ndray");
                        troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                        temp += "<td>" + troisiemeTd + "</td>";
                    }
                    temp += "</tr>";

                }
            }
            temp += "</table>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "<div class='box-footer'>";
            temp += "<div class='col-xs-12'>";
            temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Enregistrer</button> ";
            temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";
            temp += "</div>";

        }
        //Autocomplete 
        temp += getHtmlAutocomplete();
        //Autocomplete 
        temp+=getHtmlChampInvisible();
        setHtmlInsert(temp);
    }

    public void makeHtmlInsertSimple() throws Exception {
        String temp = "";
        int nb2 = getListeChamp().length / 2;
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            String troisiemeTd = "";
            if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                c.setVisible(false);
            }
            if (c.getVisible() == true) {
                // temp+="<div class='form-group'>";
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<div class=\"form-group col-lg-4 col-md-6 col-sm-6 col-xs-12 p-1\">";
                temp += "<label class='apj-label' for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">"
                        + getListeChamp()[i].getLibelle() + "</label>";

                if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                    String autre = "";
                    if (c.getColFiltre() != null && c.getValFiltre() != null) {
                        for (int j = 0; j < c.getColFiltre().length; j++) {
                            autre += "&" + c.getColFiltre()[j] + "=" + c.getValFiltre()[j];
                        }
                    }
                    /* troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('"
                    + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel="
                    + c.getApresLienPageappel() + autre + "') value=... />"; */
                    troisiemeTd = "<div class=\"apj-pop-up-container\">";
                    troisiemeTd += getListeChamp()[i].getInputHidden();
                    troisiemeTd += "<input name='choix' type='button' class='apj-pop-up btn btn-apj-primary' onclick=pagePopUp('"
                            + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel="
                            + c.getApresLienPageappel() + autre + "') value=\"...\" />";
                    troisiemeTd += "</div>";
                    temp += troisiemeTd;
                } else {
                    temp += getListeChamp()[i].getHtmlInsert();
                }
                if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    // System.out.println("niditra tato ndray");
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    temp += troisiemeTd;
                }
                temp += "</div>";

            }
        }
        temp += "<div class=\"form-group p-1 col-lg-12 col-md-12 col-sm-12 d-flex justify-content-end align-items-center filter-option \">";
        temp += "<button type='reset' name='Submit2' class='btn btn-apj-primary' tabIndex='"+ getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        temp += "<button type='submit' name='Submit2' class='btn btn-apj-secondary' tabIndex='"+ getListeChamp().length + 1 + "'>Enregistrer</button> ";
        temp+="</div>";
        // Autocomplete
        temp += getHtmlAutocomplete();
        // Autocomplete
        temp += getHtmlChampInvisible();
        setHtmlInsert(temp);
    }

    /**
     * prendre les champs à ne pas afficher mais de type hidden sur le formulaire
     * @return liste des champs invisibles
     */

    public ArrayList<Champ> getChampInvisible()
    {
        ArrayList<Champ> lc=new ArrayList<Champ>();
        for(Champ c:getListeChamp())
        {
            if(c.getVisible()==false)lc.add(c);
        }
        return lc;
    }
    /**
     * 
     * @return html des champs invisibles <input type="hidden" />
     * @throws Exception
     */
    public String getHtmlChampInvisible() throws Exception
    {
        String retour="";
        ArrayList<Champ>lc= getChampInvisible();
        for(Champ c:lc)
        {
            retour=retour+c.getHtml();
        }
        return retour;
    }
    /**
     * Génerer le script javascript d'autocomplete des champs autocomplete
     * @return chaine de charactère de script
     * @throws Exception
     */
    public String getHtmlAutocomplete() throws Exception {
        String retour = "<script>";
        if(getListeChamp().length>0){
            Map<String,String> map = new HashMap<String, String>();
            for (int i = 0; i < getListeChamp().length; i++) {
                Champ cTemp = getListeChamp()[i];
                if(cTemp.getValeur_ac()!=null){
                    map.put("data_"+Utilitaire.getStringAC(cTemp.getNom()), cTemp.getValeur_ac());
//                    retour += "  var data_"+Utilitaire.getStringAC(cTemp.getNom())+" = "+cTemp.getValeur_ac()+"\n ";
                }
            }
            for (Map.Entry me : map.entrySet()) {
                retour+="var "+me.getKey()+" = "+me.getValue()+"; \n";
//                System.out.println("Key: "+me.getKey() + " & Value: " + );
            }
           
        }
        retour += " jQuery(document).ready(function () {";
//        String retour = "<script> jQuery(document).ready(function () {";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            //champListe[i].getPageAppel() != null && champListe[i].getPageAppel().compareToIgnoreCase("") != 0
            if (c.getValeur_ac() != null && c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                String nameInput = "";
                String[] valColonne = c.getNom().split("_");
                if(valColonne.length > 1 && Utilitaire.checkNumber(valColonne[1])){
                    nameInput = valColonne[0]+"libelle_"+valColonne[1];
                }else{
                    nameInput = c.getNom()+"libelle";
                }
                
                retour += "$(document).on('keydown', '#" + nameInput + "', function (e) {\n"
                        + "            var keyCode = e.keyCode || e.which;\n"
                        + "            if ((keyCode == 9 || keyCode == 13)) {\n"
                        + "                var prest = $('#" + nameInput + "').val();\n"
                        + "                if (prest != null && prest.trim() != \"\") {\n"
                        + "                    var temp = prest.split(\"::\");\n"
                        + "                    $('#" + nameInput + "').val(temp[0].trim());\n"
                        + "                }\n"
                        + "\n"
                        + "            } else {\n"
//                        + "                var data = " + c.getValeur_ac() + "\n"
                        + "                $('#" + nameInput + "').autocomplete({\n"
                        + "                    source: function (request, response) {\n"
                        + "                         var matches = $.map(data_"+Utilitaire.getStringAC(c.getNom())+", function (acItem) {\n"
                        + "                            if (acItem.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {\n"
                        + "                                return acItem;\n"
                        + "                            }\n"
                        + "                        });\n"
                        + "                        response(matches);\n"
                        + "                    },\n"
                        + "                    minLength: 0,\n"
                        + "                    select: function (e, ui) {\n"
                        + "                        var selectedObj = ui.item;\n"
                        + "                        var prest = selectedObj.value;\n"
                        + "                        var temp = prest.split(\"::\");\n"
                        + "                        var compte = temp[1].trim();\n"
                        + "$('#" + nameInput + "').focusout(function() { "
                        + "                        $('#" + nameInput + "').val(compte);\n"
                        
                        + "                        console.log(compte);\n"
                        + "                        console.log(selectedObj);\n"
                        + "                        console.log(prest);\n"
                        + "                        console.log(temp);\n"
                        + "});"
                        + "                    }\n"
                        + "                });\n"
                        + "            }\n"
                        + "        });";
            }else if (c.getValeur_ac() != null) {
                retour += "$(document).on('keydown', '#" + c.getNom() + "', function (e) {\n"
                        + "            var keyCode = e.keyCode || e.which;\n"
                        + "            if ((keyCode == 9 || keyCode == 13)) {\n"
                        + "                var prest = $('#" + c.getNom() + "').val();\n"
                        + "                if (prest != null && prest.trim() != \"\") {\n"
                        + "                    var temp = prest.split(\"::\");\n"
                        + "                    $('#" + c.getNom() + "').val(temp[0].trim());\n"
                        + "                }\n"
                        + "\n"
                        + "            } else {\n"
//                        + "                var data = " + c.getValeur_ac() + "\n"
                        + "                $('#" + c.getNom() + "').autocomplete({\n"
                        + "                    source: function (request, response) {\n"
                        + "                        var matches = $.map(data_"+Utilitaire.getStringAC(c.getNom())+", function (acItem) {\n"
                        + "                            if (acItem.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {\n"
                        + "                                return acItem;\n"
                        + "                            }\n"
                        + "                        });\n"
                        + "                        response(matches);\n"
                        + "                    },\n"
                        + "                    minLength: 0,\n"
                        + "                    select: function (e, ui) {\n"
                        + "                        var selectedObj = ui.item;\n"
                        + "                        var prest = selectedObj.value;\n"
                        + "                        var temp = prest.split(\"::\");\n"
                        + "                        var compte = temp[1].trim();\n"
                        + "$('#" + c.getNom() + "').focusout(function() { "
                        + "                        $('#" + c.getNom() + "').val(compte);\n"
                        
                        + "                        console.log(compte);\n"
                        + "                        console.log(selectedObj);\n"
                        + "                        console.log(prest);\n"
                        + "                        console.log(temp);\n"
                        + "});"
                        + "                    }\n"
                        + "                });\n"
                        + "            }\n"
                        + "        });";
            }
            retour+=c.getAutocompleteDynamiqueJs();
        }
        retour += "}); "
                + ""
                + "</script>";
        return retour;
    }
    
    /*public void makeHtmlInsertTableauIndex() throws Exception {
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box'>";
        temp += "<div class='box box-primary'>";
        temp += "<div class='box-body'>";
        temp += "<table class=\"table table-bordered\">";
        temp += "<tr>";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            if (c.getVisible() == true) {
                //temp+="<div class='form-group'>";
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th style='background-color:#0E6D36'><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";
                //temp+="<td></td>";
                //temp+="</tr>";
                //temp+="<tr>";
                //temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                //temp+="<tr>";
                //temp+="</div>";
            }
        }
        temp += "</tr>";
        for (int iLigne = 0; iLigne < getNbLigne(); iLigne++) {
            String troisiemeTd = "";
            temp += "<tr>";
            //System.out.println("nb ligne=="+getNbLigne());

            for (int iCol = 0; iCol < getListeChamp().length; iCol++) {
                Champ ch = getListeChamp()[iCol];
                //System.out.println("iLigne="+iLigne);
                temp += "<td>" + getListeChamp()[iCol].getHtmlTableauInsert(iLigne) + "</td>";
                if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getNom() + "_" + iLigne + "') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
                if (ch.getTypeData() != null && ch.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    //System.out.println("niditra tato ndray");
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    //troisiemeTd="<a href='javascript:cal.popup()'>DATE</a>";
                    // troisiemeTd="<input name='choix' type='button' class='submit' onclick=pagePopUp('"+c.getPageAppel()+"?champReturn="+c.getNom()+"') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
            }
            temp += "</tr>";
        }
        temp += "</table>";
        temp += "</div>";
        temp += "<div class='box-footer'>";

//						temp+="<div class='col-xs-5' align='center'>";
//							temp+="<button type='submit' name='Submit2' class='btn btn-success' tabIndex='"+getListeChamp().length+1+"'>Valider</button>";
//						temp+="</div>";
//                                                temp+="<table class=\"table table-bordered\">";
//                                                    temp+="<tr>";
//                                                        temp+="<th></th>";
//                                                        temp+="<td>";
//                                                            temp+="<button type='submit' name='Submit2' class='btn btn-success' tabIndex='"+getListeChamp().length+1+"'>Valider</button>";
//							temp+="<button type='reset' name='Submit2' class='btn btn-default' tabIndex='"+getListeChamp().length+2+"'>R&eacute;initialiser</button>";
//                                                        temp+="</td>";
//                                                        
//                                                    temp+="</tr>";
//                                                temp+= "</table>";
//                                                
        temp += "<div class='col-xs-12'>";
        temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Valider</button> ";
        temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtmlTableauInsert(temp);
    }*/
    /**
     * Générer table HTML pour la modification de lignes multiples sans possibilité d'ajout de ligne
     * @throws Exception
     */
    public void makeHtmlUpdateTableauIndex() throws Exception {
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box'>";
        temp += "<div class='box box-primary'>";
        temp += "<div class='box-body'>";
        temp += "<table class=\"table table-bordered\">";
        temp += "<tr>";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            if (c.getVisible() == true) {
                //temp+="<div class='form-group'>";
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th style='background-color:#103a8e'><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";
                //temp+="<td></td>";
                //temp+="</tr>";
                //temp+="<tr>";
                //temp += "<td>" + getListeChamp()[i].getHtmlInsert() + "</td>";
                //temp+="<tr>";
                //temp+="</div>";
            }
        }
        temp += "</tr>";
        for (int iLigne = 0; iLigne < getNbLigne(); iLigne++) {
            String troisiemeTd = "";
            temp += "<tr>";
            //System.out.println("nb ligne=="+getNbLigne());

            for (int iCol = 0; iCol < getListeChamp().length; iCol++) {
                Champ ch = getListeChamp()[iCol];
                //System.out.println("iLigne="+iLigne);
                temp += "<td>" + getListeChampTableau()[iLigne][iCol].getHtmlTableauInsert(iLigne) + "</td>";
                if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getNom() + "_" + iLigne + "') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
                if (ch.getTypeData() != null && ch.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    //System.out.println("niditra tato ndray");
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    //troisiemeTd="<a href='javascript:cal.popup()'>DATE</a>";
                    // troisiemeTd="<input name='choix' type='button' class='submit' onclick=pagePopUp('"+c.getPageAppel()+"?champReturn="+c.getNom()+"') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
            }
            temp += "</tr>";
        }
        temp += "</table>";
        temp += "</div>";
        temp += "<div class='box-footer'>";

//						temp+="<div class='col-xs-5' align='center'>";
//							temp+="<button type='submit' name='Submit2' class='btn btn-success' tabIndex='"+getListeChamp().length+1+"'>Valider</button>";
//						temp+="</div>";
//                                                temp+="<table class=\"table table-bordered\">";
//                                                    temp+="<tr>";
//                                                        temp+="<th></th>";
//                                                        temp+="<td>";
//                                                            temp+="<button type='submit' name='Submit2' class='btn btn-success' tabIndex='"+getListeChamp().length+1+"'>Valider</button>";
//							temp+="<button type='reset' name='Submit2' class='btn btn-default' tabIndex='"+getListeChamp().length+2+"'>R&eacute;initialiser</button>";
//                                                        temp+="</td>";
//                                                        
//                                                    temp+="</tr>";
//                                                temp+= "</table>";
//                                                
        temp += "<div class='col-xs-12'>";
        temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Modifier</button> ";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtmlTableauInsert(temp);
    }
    /**
     * 
     * @return html de de deux boutons Valider et Annuler
     */
    public String getBoutonsValiderAnnulerTabIndex() {
        String temp = "";
        temp += "<input type='submit' name='Submit2' value='valider' class='submit' tabIndex='" + getListeChamp().length + 1 + "'>";
        temp += "<input type='reset' name='Submit2' value='Annuler' class='submit' tabIndex='" + getListeChamp().length + 2 + "'>";
        return temp;
    }
    /**
     * Récuperer en url encoded les valeurs des champs donnés
     * @return chaine de charactère de paramètre HTTP
     */
    public String getListeCritereString() {
        String retour = "";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ temp = getListeChamp()[i];
            String valeur = temp.getValeur();
            if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                valeur = Utilitaire.remplacePourcentage(valeur);
            }
            retour += "&" + temp.getNom() + "=" + valeur;
        }
        if (getChampGroupe() != null) {
            for (int j = 0; j < getChampGroupe().length; j++) {
                Champ temp = getChampGroupe()[j];
                String valeur = temp.getValeur();
                if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                    valeur = Utilitaire.remplacePourcentage(valeur);
                }
                retour += "&" + temp.getNom() + "=" + valeur;
            }
        }
        if (getChampTableauAff() != null) {
            for (int j = 0; j < getChampTableauAff().length; j++) {
                Champ temp = getChampTableauAff()[j];
                String valeur = temp.getValeur();
                if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                    valeur = Utilitaire.remplacePourcentage(valeur);
                }
                retour += "&" + temp.getNom() + "=" + valeur;
            }
        }
        return retour;
    }
    
    public String getListeCritereStringCheckbox(PageRecherche pr) {
        String retour = "";
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ temp = getListeChamp()[i];
            if(temp.getEstMultiple()){
                String[] valeurs = pr.getReq().getParameterValues(temp.getNom());
                if(valeurs != null && valeurs.length > 0 && !valeurs[0].isEmpty()){
                    for(int j=0; j<valeurs.length; j++){
                        retour += "&" + temp.getNom() + "=" + valeurs[j];
                    }
                }
            }
            else{
            String valeur = temp.getValeur();
            if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                valeur = Utilitaire.remplacePourcentage(valeur);
            }
            retour += "&" + temp.getNom() + "=" + valeur;
            }
        }
        if (getChampGroupe() != null) {
            for (int j = 0; j < getChampGroupe().length; j++) {
                Champ temp = getChampGroupe()[j];
                String valeur = temp.getValeur();
                if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                    valeur = Utilitaire.remplacePourcentage(valeur);
                }
                retour += "&" + temp.getNom() + "=" + valeur;
            }
        }
        if (getChampTableauAff() != null) {
            for (int j = 0; j < getChampTableauAff().length; j++) {
                Champ temp = getChampTableauAff()[j];
                String valeur = temp.getValeur();
                if (temp.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeTexte) == 0) {
                    valeur = Utilitaire.remplacePourcentage(valeur);
                }
                retour += "&" + temp.getNom() + "=" + valeur;
            }
        }
        return retour;
    }

    /**
     * initialiser les valeurs des listes deroulantes et autocomplete 
     * pour les formulaires avec plusieurs lignes de data
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */

    public void getAllDataFille(Connection c)throws Exception
    {
        bean.Champ[] champfle = ListeColonneTable.getFromListe(getObjet(), c);
        int nbChampFille=champfle.length;
        for (int i = 0; i < getListeChamp().length; i++) 
        {
            if(i<nbChampFille) // Raha premiere ligne no fenoina dia maka base
            {
                getListeChamp()[i].findData(c);
                getListeChamp()[i].findDataAutoComplete(c);
            }
            else // à partir ny deuxieme ligne
            {
                getListeChamp()[i].setBase(getListeChamp()[i%nbChampFille].getBase());
                getListeChamp()[i].setTab_ac(getListeChamp()[i%nbChampFille].getTab_ac());
            }
        }
        
    }
    /**
     * Récuperer les données des listes déroulantes/autocomplete
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void getAllData(Connection c) throws Exception {
        
        for (int i = 0; i < getListeChamp().length; i++) 
        { 
            if(isEstFille()==false)
            {
                getListeChamp()[i].findData(c);
                getListeChamp()[i].findDataAutoComplete(c);
            }
            
            
            /*if (getListeChamp()[i].isAutocomplete()) 
            {
                //System.out.println("mandalo autocomplete = " );
                Ac_object[] ac_objt = null;
                ResultSet dr = null;
                Statement st = null;
                try {
                    String req = "select " + getListeChamp()[i].getAc_affiche() + ", " + getListeChamp()[i].getAc_valeur() + " from " + getListeChamp()[i].getAc_nomTable() +" where 1<2 "+getListeChamp()[i].getAc_aWhere();
                    st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    dr = st.executeQuery(req);
                    List<Ac_object> liste = new ArrayList<>();
                    while (dr.next()) {
                        Ac_object aco=new Ac_object(dr.getObject(getListeChamp()[i].getAc_valeur()), dr.getObject(getListeChamp()[i].getAc_affiche()));
                        liste.add(aco);
                    }
                    if (liste.size() > 0) {
                        ac_objt = new Ac_object[liste.size()];
                        for (int k = 0; k < liste.size(); k++) {
                            ac_objt[k] = liste.get(k);
                        }
                        getListeChamp()[i].setTab_ac(ac_objt);
                        getListeChamp()[i].transformToStringAc();
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
            }*/
        }
    }
    /**
     * 
     * @return liste des champs du formulaire
     */
    public Champ[] getListeChamp() {
        return listeChamp;
    }

    public void setListeChamp(Champ[] listeChamp) {
        this.listeChamp = listeChamp;
    }
    /**
     * 
     * @return liste des champs en tableau
     */
    public Champ[][] getListeChampTableau() {
        return listeChampTableau;
    }

    public void setListeChampTableau(Champ[][] listeChamp) {
        this.listeChampTableau = listeChamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNbRangee(int nbRangee) {
        this.nbRangee = nbRangee;
    }
    /**
     * Nombre de rangée dans le formulaire principal
     */
    public int getNbRangee() {
        return nbRangee;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    /**
     * Prendre le html géneré
     * Sinon géneration d'un html avec makeHtml()
     * @return
     * @throws Exception
     */
    public String getHtml() throws Exception {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            this.makeHtml();
        }
        return html;
    }

    public void setTailleTableau(String tailleTableau) {
        this.tailleTableau = tailleTableau;
    }

    public String getTailleTableau() {
        return tailleTableau;
    }

    public void setCssTableau(String cssTableau) {
        this.cssTableau = cssTableau;
    }

    public String getCssTableau() {
        return cssTableau;
    }

    public void setObjet(bean.ClassMAPTable objet) {
        this.objet = objet;
    }
    /**
     * 
     * @return objet de mapping du formulaire
     */
    public bean.ClassMAPTable getObjet() {
        return objet;
    }
    /**
     * Définir les champs pour un formulaire de recherche
     * @param crtFormu
     */
    public void setCrtFormu(Champ[] crtFormu) {
        this.crtFormu = crtFormu;
    }
    /**
     * Pour les pages de recherche
     * @return champ champs de critère pour le formulaire
     */
    public Champ[] getCrtFormu() {
        return crtFormu;
    }

    /**
     * 
     * @param nbIntervalle nombre de colonne d'intervalle
     */
    public void setNbIntervalle(int nbIntervalle) {
        this.nbIntervalle = nbIntervalle;
    }

    public int getNbIntervalle() {
        return nbIntervalle;
    }

    public void setHtmlTri(String htmlTri) {
        this.htmlTri = htmlTri;
    }

    /**
     * si valeur existe retourne html sinon création du html
     * @return html de la partie de tri du formulaire pour la page recherche
     * @throws Exception
     */
    public String getHtmlTri() throws Exception {
        if (htmlTri == null || htmlTri.compareToIgnoreCase("") == 0) {
            this.makeHtmlTri();
        }
        return htmlTri;
    }

    public void setHtmlButton(String htmlButton) {
        this.htmlButton = htmlButton;
    }

    /**
     * 
     * @return html de bouton HTML
     */
    public String getHtmlButton() {
        return htmlButton;
    }

    public void setHtmlInsert(String htmlInsert) {
        this.htmlInsert = htmlInsert;
    }

    /**
     * si valeur existe html stocké sinon géneration
     * @return html de formulaire pour un insert
     * @throws Exception
     */

    public String getHtmlInsert() throws Exception {
        if (htmlInsert == null || htmlInsert.compareToIgnoreCase("") == 0) {
            makeHtmlInsert();
        }
        return htmlInsert;
    }
    /**
     * @deprecated
     * si valeur existe html stocké sinon géneration
     * @return html de formulaire pour un insert
     * @throws Exception
     */
    public String getHtmlAdd() throws Exception {
        if (htmlInsert == null || htmlInsert.compareToIgnoreCase("") == 0) {
            makeHtmlInsert();
        }
        return htmlInsert;
    }
    /**
     * @deprecated ne génere pas le html tableau insert
     * si valeur existe html stocké sinon géneration
     * @return html de formulaire pour un insert
     * @throws Exception
     */
    public String getHtmlTableauInsert() throws Exception {
        if (htmlTableauInsert == null || htmlTableauInsert.compareToIgnoreCase("") == 0) {
            makeHtmlInsert();
        }
        return htmlTableauInsert;
    }

    /**
     * 
     * @param htmlTableauInsert
     */
    public void setHtmlTableauInsert(String htmlTableauInsert) {
        this.htmlTableauInsert = htmlTableauInsert;
    }
    
    public void setChampGroupe(Champ[] champGroupe) {
        this.champGroupe = champGroupe;
    }

    /**
     * 
     * @param champGroupe Liste des champs de groupages
     */
    public Champ[] getChampGroupe() {
        return champGroupe;
    }

    public void setHtmlGroupe(String htmlGroupe) {
        this.htmlGroupe = htmlGroupe;
    }

    public String getHtmlGroupe() {
        return htmlGroupe;
    }

    public Champ[] getChampTableauAff() {
        return champTableauAff;
    }

    public void setChampTableauAff(Champ[] champTableauAff) {
        this.champTableauAff = champTableauAff;
    }

    public String getHtmlTableauAff() {
        return htmlTableauAff;
    }

    public void setHtmlTableauAff(String htmlTableauAff) {
        this.htmlTableauAff = htmlTableauAff;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
    /**
     * @deprecated
     * génerer le html de la partie de champ pour une commande
     * @throws Exception
     */
    public void makeHtmlInsertCommande() throws Exception {
        String temp = "";
        temp += "<div class='row'>";
        temp += "<div class='insertFormulaire'>";
        temp += "<div>";
        temp += "<div class='box box-primary'>";
        temp += "<div class='box-body'>";
        temp += "<table class=\"table table-bordered\">";
        
        
        for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            //System.out.println("ch.getNom() " + c.getNom());
            String troisiemeTd = "";
            if (c.getNom().compareToIgnoreCase("iduser") == 0) {
                c.setVisible(false);
            }
            if (c.getVisible() == true) {
                //temp+="<div class='form-group'>";
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<tr>";
                temp += "<th><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";
                
                if (c.getPageAppel() != null && c.getPageAppel().compareToIgnoreCase("") != 0) {
                    temp += "<td>" + getListeChamp()[i].getInputHidden() + "</td>";
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + c.getPageAppel() + "?champReturn=" + c.getChampReturn() + "&apresLienPageAppel=" + c.getApresLienPageappel() + "') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                } else {
                    temp += "<td>" + getListeChamp()[i].getHtmlInsert()+ "</td>";
                }
                if (c.getTypeData() != null && c.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    //System.out.println("niditra tato ndray");
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    temp += "<td>" + troisiemeTd + "</td>";
                }
                temp += "</tr>";
                
            }
        }
        temp += "</table>";
        temp += "</div>";
        temp += "<div class='box-footer'>";                              
        temp += "<div class='col-xs-12'>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtmlInsert(temp);
    }
    /**
     * Génerer html de la partie liste d'un formulaire
     * @throws Exception
     */
    public void makeHtmlInsertTableauIndex() throws Exception {
        String temp = "";
        String acstring = "";
        
        temp += "<div class='row'>";
        temp += "<div class='row col-md-12'>";
        temp += "<div class='box'>";
        temp += "<div class='box box-primary'>";
        temp += "<div class='box-body'>";
        temp += "<input id='nbrLigne' name='nbrLigne' type='hidden' value='" + getNbLigne() + "'/>";
        temp += "<input name='indexMultiple' id='indexMultiple' type='hidden' value='" + getNbLigne() + "'/>";
        temp += "<table class='table table-bordered'>";
        temp += "<tr>";
        
        /*for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            if (c.getVisible() == true) {
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th style='background-color:#bed1dd' colspan='2'><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

            }
        }*/
        
        //bean.Champ[] champfle = bean.ListeColonneTable.getFromListe(getObjet(), null);
        //Champ[] champfle=this.getListeChamp();
        String[] colonne=null;
        if(this.getColOrdre()!=null)colonne=this.getColOrdre();
        else
        {
            
            bean.Champ[] champfle = ListeColonneTable.getFromListe(getObjet(), null);
            colonne=new String[champfle.length];
            for(int i=0;i<champfle.length;i++)
            {
                System.out.println(" -- "+champfle[i]);
                colonne[i]=champfle[i].getNomColonne();
            }
        }
        for (int i = 0; i < colonne.length; i++) 
        {
            Champ c = this.getChamp(colonne[i]+"_"+0);
            if(i == 0)
                temp += "<th style='background-color:#bed1dd; text-align: center' colspan='1'><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\"></th>";
            if (c.getVisible() == true) {
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th style='background-color:#bed1dd' colspan='1'><label for='" + c.getNom() + "' " + c.getAutre() + ">" + c.getLibelle() + "</label></th>";
            }
        }
        
        temp += "</tr>";
        temp += "<tbody id='ajout_multiple_ligne'>";
        
        for (int iLigne = 0; iLigne < getNbLigne(); iLigne++) {
            String idFille = "";
            String liendelete = "#";
            acstring += getHtmlAutocomplete(iLigne);
            String troisiemeTd = "";
            temp += "<tr id='ligne-multiple-" + iLigne + "'>";
            temp += "<td align=center><input type='checkbox' value='" + iLigne + "' name='id' id='checkbox" + iLigne + "'/></td>";
            if(this.isEstFille()){
                if(this.getDataFille() != null && iLigne<this.getDataFille().length){
                ClassMAPTable t = this.getDataFille()[iLigne];
                if(t!=null)
                    idFille = t.getTuppleID();
                    liendelete = "module.jsp?but=apresTarif.jsp&amp;id="+idFille+"&amp;acte=delete&amp;bute="+getApres()+"&rajoutLienFormu="+getRajoutLien()+"&amp;classe="+getObjet().getClassName();
                    //liendelete = "module.jsp?but=apresTarif.jsp&amp;id="+idFille+"&amp;acte=delete&amp;bute=&amp;classe="+getObjet().getClassName();
                }
            }
            for (int iCol = 0; iCol < colonne.length; iCol++) {
                
                Champ ch = this.getChamp(colonne[iCol]+"_"+iLigne);
                {
                    if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                        String autre = "";
                        if(ch.getColFiltre()!=null && ch.getValFiltre()!=null){
                            for(int i = 0;i<ch.getColFiltre().length;i++){
                                autre+="&"+ch.getColFiltre()[i]+"="+ch.getValFiltre()[i];
                            }
                        }
                        temp += "<td style=\"display: inline-flex;\">" + ch.getInputHidden();
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getChampReturn(iLigne) + "&apresLienPageAppel=" + ch.getApresLienPageappel() + autre+"') value=... />";
                        temp += troisiemeTd  + "</td>";
                        
                    }
                    else
                    {
                        if (ch.getVisible() == true) temp += "<td>" + ch.getHtmlTableauInsert(iLigne) +"</td>";
                        else temp+=ch.getHtmlTableauInsert(iLigne);
                    }
                    if (ch.getTypeData() != null && ch.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                        troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                        temp += "<td>" + troisiemeTd + "</td>";
                        temp += "<td></td>";
                    }
                }
            }
            
            temp += "<td><a href='"+liendelete+"'><span class='glyphicon glyphicon-remove'></span></a></td>";
            temp += "</tr>";
            
        }
        temp += "</tbody>";
        temp += "</table>";
        temp += "</div>";
        temp += "<div class='box-footer'>";

        temp += "<div class='col-xs-12'>";
        Gson gson = new Gson();
        String json = gson.toJson(getListeChamp());
        temp += "<button type='submit' name='Submit2' class='btn btn-success pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Enregistrer</button> ";
        temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        
        temp += "<button type='button' class='btn btn-default pull-right' onclick='add_line_tab(JSON.stringify("+json+"))' style='margin-right: 15px;'>Ajouter une ligne</button>";
        temp += "<button type='button' class='btn btn-default pull-right' onclick='add_line_tabs(JSON.stringify("+json+"))' style='margin-right: 15px;'>Ajouter dix lignes</button>";
        //temp += "<button type='button' class='btn btn-default pull-right' onclick='add_line()' style='margin-right: 15px;'>Ajouter une ligne</button>";
        //temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += acstring;
        
        temp += getHtmlAutocomplete();
        //System.out.println("temp + = "+temp);
        temp+=getHtmlChampInvisible();
        setHtmlTableauInsert(temp);
    }

    public void makeHtmlInsertTableauSimple() throws Exception {
        String temp = "";
        String acstring = "";
        temp += "<input id='nbrLigne' name='nbrLigne' type='hidden' value='" + getNbLigne() + "'/>";
        temp += "<input name='indexMultiple' id='indexMultiple' type='hidden' value='" + getNbLigne() + "'/>";
        temp += "<table class='table table-insert'>";
        temp += "<tr class=\"table-header\">";
        
        /*for (int i = 0; i < getListeChamp().length; i++) {
            Champ c = getListeChamp()[i];
            if (c.getVisible() == true) {
                c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th style='background-color:#bed1dd' colspan='2'><label for='" + getListeChamp()[i].getLibelle() + "' " + c.getAutre() + ">" + getListeChamp()[i].getLibelle() + "</label></th>";

            }
        }*/
        
        //bean.Champ[] champfle = bean.ListeColonneTable.getFromListe(getObjet(), null);
        //Champ[] champfle=this.getListeChamp();
        String[] colonne=null;
        if(this.getColOrdre()!=null)colonne=this.getColOrdre();
        else
        {
            bean.Champ[] champfle = ListeColonneTable.getFromListe(getObjet(), null);
            colonne=new String[champfle.length];
            for(int i=0;i<champfle.length;i++)
            {
                colonne[i]=champfle[i].getNomColonne();
            }
        }
        for (int i = 0; i < colonne.length; i++) 
        {
            Champ c = this.getChamp(colonne[i]+"_"+0);
            if(i == 0)
                temp += "<th style='background-color:#bed1dd; text-align: center' colspan='1'><input onclick=\"CocheToutCheckbox(this, 'id')\" type=\"checkbox\" class=\"form-check-input search-check\"></th>";
            if (c.getVisible() == true) {
                // c.setAutre("tabindex='" + (i + 1) + "'" + c.getAutre());
                temp += "<th class='apj-label' "+ c.getAutre() + ">" + c.getLibelle() + "</th>";
            }
        }
        

        temp+="<th></th>";
        temp += "</tr>";

        // temp += "<tbody id='ajout_multiple_ligne'>";
        
        for (int iLigne = 0; iLigne < getNbLigne(); iLigne++) {
            String idFille = "";
            String liendelete = "#";
            acstring += getHtmlAutocomplete(iLigne);
            String troisiemeTd = "";
            temp += "<tr id='ligne-multiple-" + iLigne + "'>";
            temp += "<td align=center><input type='checkbox' value='" + iLigne + "' name='id' id='checkbox" + iLigne + "' class=\"form-check-input search-check\"/></td>";
            if(this.isEstFille()){
                if(this.getDataFille() != null && iLigne<this.getDataFille().length){
                ClassMAPTable t = this.getDataFille()[iLigne];
                if(t!=null)
                    idFille = t.getTuppleID();
                    liendelete = "module.jsp?but=apresTarif.jsp&amp;id="+idFille+"&amp;acte=delete&amp;bute="+getApres()+"&rajoutLienFormu="+getRajoutLien()+"&amp;classe="+getObjet().getClassName();
                    //liendelete = "module.jsp?but=apresTarif.jsp&amp;id="+idFille+"&amp;acte=delete&amp;bute=&amp;classe="+getObjet().getClassName();
                }
            }
            for (int iCol = 0; iCol < colonne.length; iCol++) {
                
                Champ ch = this.getChamp(colonne[iCol]+"_"+iLigne);
                {
                    if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                        String autre = "";
                        if(ch.getColFiltre()!=null && ch.getValFiltre()!=null){
                            for(int i = 0;i<ch.getColFiltre().length;i++){
                                autre+="&"+ch.getColFiltre()[i]+"="+ch.getValFiltre()[i];
                            }
                        }
                        temp += "<td style=\"display: inline-flex;\">" + ch.getInputHidden();
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getChampReturn(iLigne) + "&apresLienPageAppel=" + ch.getApresLienPageappel() + autre+"') value=... />";
                        temp += troisiemeTd  + "</td>";
                        
                    }
                    else
                    {
                        if (ch.getVisible() == true) temp += "<td>" + ch.getHtmlTableauInsert(iLigne) +"</td>";
                        else temp+=ch.getHtmlTableauInsert(iLigne);
                    }
                    if (ch.getTypeData() != null && ch.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                        troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                        temp += "<td>" + troisiemeTd + "</td>";
                        temp += "<td></td>";
                    }
                }
            }
            
            temp += "<td class='cancel'><a href='"+liendelete+"'><span class=\"\">&times;</span></a></td>";
            temp += "</tr>";
            
        }
        // temp += "</tbody>";
        temp += "<tr>";

        temp += "<td colspan='100%'>";
        Gson gson = new Gson();
        String json = gson.toJson(getListeChamp());
        temp += "<button type='submit' name='Submit2' class='btn btn-apj-secondary pull-right' style='margin-right: 25px;' tabIndex='" + getListeChamp().length + 1 + "'>Enregistrer</button> ";
        temp += "<button type='reset' name='Submit2' class='btn btn-apj-primary pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        
        temp += "<button type='button' class='btn btn-apj-another-action-secondary pull-right' onclick='add_line_tab(JSON.stringify("+json+"))' style='margin-right: 15px;'>Ajouter une ligne</button>";
        temp += "<button type='button' class='btn btn-apj-another-action pull-right' onclick='add_line_tabs(JSON.stringify("+json+"))' style='margin-right: 15px;'>Ajouter dix lignes</button>";
        //temp += "<button type='button' class='btn btn-default pull-right' onclick='add_line()' style='margin-right: 15px;'>Ajouter une ligne</button>";
        //temp += "<button type='reset' name='Submit2' class='btn btn-default pull-right' style='margin-right: 15px;' tabIndex='" + getListeChamp().length + 2 + "'>R&eacute;initialiser</button>";
        temp += "</td>";
        temp += "</table>";
        temp += acstring;
        
        temp += getHtmlAutocomplete();
        //System.out.println("temp + = "+temp);
        temp+=getHtmlChampInvisible();
        setHtmlTableauInsert(temp);
    }
    /**
     * @deprecated ne fait rien
     * @param k
     * @return
     * @throws Exception
     */
    public String getHtmlAutocomplete(int k) throws Exception {
        String retour = "";
//        String retour = "<script>";
//        if(getListeChamp().length>0){
//            Champ cTemp = getListeChamp()[0];
//            retour += "  var data = "+cTemp.getValeur_ac()+"\n ";
//        }
//        retour += " jQuery(document).ready(function () {";
//        for (int i = 0; i < getListeChamp().length; i++) {
//            Champ c = getListeChamp()[i];
//            if (c.getValeur_ac() != null) {
//                retour += "$(document).on('keydown', '#" + c.getNom() + "_"+k+"', function (e) {\n"
//                        + "            var keyCode = e.keyCode || e.which;\n"
//                        + "            if ((keyCode == 9 || keyCode == 13)) {\n"
//                        + "                var prest = $('#" + c.getNom() + "_"+k+"').val();\n"
//                        + "                if (prest != null && prest.trim() != \"\") {\n"
//                        + "                    var temp = prest.split(\"::\");\n"
//                        + "                    $('#" + c.getNom() + "_"+k+"').val(temp[0].trim());\n"
//                        + "                }\n"
//                        + "\n"
//                        + "            } else {\n"
////                        + "                var data = " + c.getValeur_ac() + "\n"
//                        + "                $('#" + c.getNom() + "_"+k+"').autocomplete({\n"
//                        + "                    source: function (request, response) {\n"
//                        + "                        var matches = $.map(data, function (acItem) {\n"
//                        + "                            if (acItem.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {\n"
//                        + "                                return acItem;\n"
//                        + "                            }\n"
//                        + "                        });\n"
//                        + "                        response(matches);\n"
//                        + "                    },\n"
//                        + "                    minLength: 0,\n"
//                        + "                    select: function (e, ui) {\n"
//                        + "                        var selectedObj = ui.item;\n"
//                        + "                        var prest = selectedObj.value;\n"
//                        + "                        var temp = prest.split(\"::\");\n"
//                        + "                        var compte = temp[0].trim();\n"
//                        + "$('#" + c.getNom() + "_"+k+"').focusout(function() { "
//                        + "                        $('#" + c.getNom() + "_"+k+"').val(compte);\n"
//                        
//                        + "                        console.log(compte);\n"
//                        + "});"
//                        + "                    }\n"
//                        + "                });\n"
//                        + "            }\n"
//                        + "        });";
//            }
//        }
//        retour += "}); "
//                + ""
//                + "</script>";
        return retour;
    }
    /**
     * Pour un formulaire avec insertion multiple, trouver la liste des champs pour un colonne donné
     * @param colonne nom de la colonne
     * @return
     * @throws Exception
     */
    public Champ[] getChampFille(String colonne) throws Exception{
        Champ[] tChamp = new Champ[getNbLigne()];
        int iV = 0;
        bean.Champ[] champfle = ListeColonneTable.getFromListe(getObjet(), null);
        for(int ligne =0; ligne<getNbLigne(); ligne++){
            for(int i =0; i<champfle.length; i++){
                if((colonne).compareToIgnoreCase(champfle[i].getNomColonne()) == 0){
                    tChamp[iV] = this.getChamp(colonne+"_"+ligne);
                    iV ++;
                }
            }
        }
        return tChamp;
    }
    /**
     * Filtrer les champs à afficher pour le formulaire
     * @param aAff liste des noms des colonnes à afficher
     * @throws Exception
     */
    public void makeChampFormuLigne(String[] aAff) throws Exception {
        Vector liste = new Vector();
        for (int i = 0; i < aAff.length; i++) {
            //System.out.println("critere = "+crt[i]+" get Champ =========="+getObjet()+"======= "+ListeColonneTable.getChamp(getObjet(),crt[i]));
            String type = ListeColonneTable.getChamp(getObjet(), aAff[i]).getTypeColonne();
            
            liste.add(new Champ(aAff[i]));
            
        }
        bean.Champ[] nomCol = ListeColonneTable.getFromListe(getObjet(), null);
        Champ[] listeForm = new Champ[liste.size()];
        liste.copyInto(listeForm);
        setListeChamp(listeForm);
    }
    /**
     * @deprecated ne réalise pas le champs
     * @param libEntete libellé de l'entête
     * @param valeur valeur du champs du formulaire
     * @param iLigne
     * @return
     * @throws Exception
     */
    public String makeHtmlUpdateTableau(String libEntete, String valeur, int iLigne) throws Exception {
        String temp = "";
        String troisiemeTd = "";
        
        //for (int iCol = 0; iCol < getListeChamp().length; iCol++) 
        {
            Champ ch=this.getChamp(libEntete);
            //Champ ch = getListeChamp()[iCol];
            System.out.println(" ---> "+ch.getClass().toString());
            if (ch!=null && ch.getVisible() == true) 
            {
                //if(libEntete.equals(getListeChamp()[iCol].getNom()))
                {
                    temp += "<td>" + ch.getHtmlTableauInsert(iLigne, valeur) + "</td>";
                    if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                        troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getNom() + "_" + iLigne + "') value=... />";
                        temp += "<td>" + troisiemeTd + "</td>";
                    }
                }
                
                /*if (ch.getPageAppel() != null && ch.getPageAppel().compareToIgnoreCase("") != 0) {
                    troisiemeTd = "<input name='choix' type='button' class='submit' onclick=pagePopUp('" + ch.getPageAppel() + "?champReturn=" + ch.getNom() + "_" + iLigne + "') value=... />";
                    temp += "<td>" + troisiemeTd + "</td>";
                } else if (ch.getTypeData() != null && ch.getTypeData().compareToIgnoreCase(ConstanteAffichage.typeDaty) == 0) {
                    troisiemeTd = "<a href='javascript:cal.popup()'><img src='calendar/img/cal.gif' alt='Cliquez ici pour choisir une date' width=16 height=16 border=0 align='absmiddle' /></a>";
                    temp += "<td>" + troisiemeTd + "</td>";
                    temp += "<td></td>";
                } else {
                    temp += "<td></td>";
                }*/
            }
        }
        return temp;
    }
    
    
    private String apres;
    private String rajoutLien;
    
    /**
     * 
     * @return lien de submit 
     */
    public String getApres() {
        return apres;
    }

    public void setApres(String apres) {
        this.apres = apres;
    }

    /**
     * 
     * @return liste de paramètre délimité par "-" à mettre sur le lien de redirection après formulaire
     */

    public String getRajoutLien() {
        return rajoutLien;
    }

    public void setRajoutLien(String rajoutLien) {
        this.rajoutLien = rajoutLien;
    }
    
}
