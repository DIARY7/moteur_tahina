package affichage;

import bean.ClassMAPTable;
import javax.servlet.http.HttpServletRequest;
import bean.CGenUtil;
import bean.ListeColonneTable;
import historique.MapUtilisateur;
import java.lang.reflect.Field;
import java.sql.Connection;
import user.UserEJB;

import constante.ConstanteEtat;
import utilitaire.Utilitaire;

/**
 * 
 * Objet à utiliser à l'affichage pour génerer la page fiche d'un objet de mapping donné.
 * 
 * Ci-dessous une exemple de comment créer une page fiche. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 *   UserEJB u = (user.UserEJB) session.getValue("u");
 *   FactureClient e = new FactureClient();
 *   e.setNomTable("facture_client_union_ordre");
 *   PageConsulte pc = new PageConsulte(e, request, u);
 *   pc.setTitre("FICHE FACTURE CLIENT");
 *   pc.getChampByName("tierslib").setLibelle("Tiers");
 *   pc.getChampByName("daty").setLibelle("Date");
 *   pc.getChampByName("montant").setLibelle("Montant HT");
 * }
 * </pre>
 * Cela nous a permis d'initialiser la page avec filtre automatique venant de la requête HTTP et en formattant le nom des champs.
 *   Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * <pre>
 * {@code 
 * <%
 *       out.println(pc.getHtml());
 *   %>
 * }
 * 
 * </pre>
 * 
 * @author BICI
 * 
 * @version 1.0
 * 
 */
public class PageConsulte extends Page {

    private Champ[] listeChamp;
    private bean.ClassMAPTable critere;
    private String[] libAffichage;
    private String cssTableau = "monographe";
    private String apres;
    private String apresLienPage = "";
    private String donnee;
    private String titre;
    private boolean canUploadFile = true;
    private String fichier_table_cible = "ATTACHER_FICHIER";
    private String fichier_table_procedure = "getSeq_attacher_fichier";
    private String nomClasseFilleADuplique = "";
    private String nomColonneMere = "";
    private String nomtable="";
    String[]ordre=null;

    public String[] getOrdre() {
        return ordre;
    }

    /**
     * permet de mettre en ordre les champs (ordre pas comme dans la base )
     * @param ordre des champs
     */
    public void setOrdre(String[] ordre) {
        this.ordre = ordre;
        Champ[]listeVaovao=new Champ[getListeChamp().length];
        for(int k=0;k<listeVaovao.length;k++)
        {
            listeVaovao[k]=getListeChamp()[k];
        }
        int iVao=0;
        for(int i=0;i<ordre.length;i++)
        {
            //listeVaovao[iVao]=listeChamp[iVao];
            for(int iListe=0;iListe<listeChamp.length;iListe++)
            {
                if(listeChamp[iListe].getNom().compareToIgnoreCase(ordre[i])==0)
                {
                    Champ temporaire=listeVaovao[iVao];
                    listeVaovao[iVao]=listeChamp[iListe];
                    listeVaovao[iListe]=temporaire;
                    
                    iVao++;
                }
                
            }
        }
        setListeChamp(listeVaovao);
    }
    

    public String getNomColonneMere() {
        return nomColonneMere;
    }

    public void setNomColonneMere(String nomColonneMere) {
        this.nomColonneMere = nomColonneMere;
    }

    public String getNomClasseFilleADuplique() {
        return nomClasseFilleADuplique;
    }

    public void setNomClasseFilleADuplique(String nomClasseFilleADuplique) {
        this.nomClasseFilleADuplique = nomClasseFilleADuplique;
    }

    public String getTitre() {
        return this.titre;
    }
    /**
     * @param titre titre de la page fiche
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }
    /**
     * Constructeur par défaut
     */
    public PageConsulte() {
    }

    public boolean isCanUploadFile() {
        return canUploadFile;
    }

    public void setCanUploadFile(boolean canUploadFile) {
        this.canUploadFile = canUploadFile;
    }

    public String getFichier_table_cible() {
        return fichier_table_cible;
    }

    public void setFichier_table_cible(String fichier_table_cible) {
        this.fichier_table_cible = fichier_table_cible;
    }

    public String getFichier_table_procedure() {
        return fichier_table_procedure;
    }

    public void setFichier_table_procedure(String fichier_table_procedure) {
        this.fichier_table_procedure = fichier_table_procedure;
    }

    public PageConsulte(ClassMAPTable o, HttpServletRequest req, user.UserEJB u) throws Exception {
        setCritere(o);
        setReq(req);
        makeCritere();
        setUtilisateur(u);
        getData();
        makeChamp();
    }


    /**
     * permet d'obtenir l'objet de mapping à partir des paramètres dans la requête HTTP et l'objet de mapping de la classe
     * 
     * @throws Exception Au cas où aucun objet de mapping n'est donné ou aucune requête HTTP
     */
    public void getData() throws Exception {
        ClassMAPTable[] result = (ClassMAPTable[]) getUtilisateur().getData(getCritere(), null, null, null, "");
        if (result == null || result.length == 0) {
            throw new Exception("Pas de resultat pour votre consultation");
        }
        setBase(result[0]);
    }

    /**
     * relation entre les colonnes dans la base et dans la classe (mapping)
     * @throws Exception
     */
    public void makeChamp() throws Exception {
        Champ[] listeChamp = null;

        bean.Champ[] t = ListeColonneTable.getFromListe(getBase(), null);
        listeChamp = new Champ[t.length];
        //Field[] f = getBase().getFieldList();
        for (int i = 0; i < t.length; i++) {
            listeChamp[i] = new Champ(t[i].getNomColonne());
            listeChamp[i].setLibelle(t[i].getNomColonne());
            Object tempVal = CGenUtil.getValeurFieldByMethod(getBase(), t[i].getNomColonne());
            String valeur = String.valueOf(tempVal);
            if(valeur!=null && valeur.contains("'")){
                valeur = valeur.replaceAll("'", "&apos;");
            }
            if(valeur!=null && valeur.contains("\"")){
                    valeur = valeur.replaceAll("\"", " ");
            }
            if (tempVal == null) {
                valeur = "";
            } else if (t[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {
                valeur = Utilitaire.formatterDaty(valeur);
            } else if (t[i].getTypeJava().compareToIgnoreCase("double") == 0) {
                valeur = Utilitaire.formaterAr(valeur);
            }
            listeChamp[i].setValeur(valeur);
        }
        setListeChamp(listeChamp);
    }

    /**
     * permet de faire un critère pour la recherche dans la base
     * @throws Exception
     */
    public void makeCritere() throws Exception {
        String valeur = getReq().getParameter(getCritere().getAttributIDName());
        Field f = CGenUtil.getField(getCritere(), getCritere().getAttributIDName());
        if(f.getName().compareToIgnoreCase("refuser") == 0 && (f.getType().equals("int")) || (f.getType().equals("Integer"))){
            CGenUtil.setValChamp(getCritere(), f, Integer.parseInt(valeur));
        }else{
            CGenUtil.setValChamp(getCritere(), f, valeur);
        }
    }

    /**
     * 
     * @return liste des champs à construire ordonnés à l'affichage
     */
    public Champ[] getListeChamp() {
        return listeChamp;
    }
    /**
     * 
     * @param listeChamp liste des champs à prendre en base et à afficher
     */
    public void setListeChamp(Champ[] listeChamp) {
        this.listeChamp = listeChamp;
    }



    /**
     * pour faire un tableau résumant les valeurs des champs avec leur libellé respectif. 
     * Au cas où la liste des champs est supérieur À 13. On aura deux colonnes de tableau.
     * C'est dans cette partie qu'on utilise le format d'un champ (visible, photo, etc)
    */
    public void makeHtml() {
        java.util.Properties prop = configuration.CynthiaConf.load();
        String temp = "";
        Class[] paramType = {int.class};   
        if(getListeChamp().length<13){
            temp += "<div class='row'>";
            temp += "<div class='col-md-3'></div>"
            + "<div class='col-md-6'>";        
            temp += "<table class='table table-bordered'>";
            
            for (int i = 0; i < getListeChamp().length; i++) {
                Champ champ= getListeChamp()[i];
                String lienDebut =  champ.getLien() ==null ? "" : " <a href='"+ champ.getLien().getLien() +"&" + champ.getLien().getQueryString() + getListeChamp()[i].getValeur() + "'>" ;
                //System.out.println("lienDebut ==== " + lienDebut);
                String lienFin= champ.getLien()==null ?"": "</a>";
                if (getListeChamp()[i].getTypeData().compareToIgnoreCase("date") == 0) {
                }
                if (getListeChamp()[i].isPhoto()) {
                    temp += "<tr>";
                    temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>"+ getListeChamp()[i].getLibelle() + "</B></td>";
                    temp += "<td>" +lienDebut+ "<img src='" + prop.getProperty("cdnReadUri") + getListeChamp()[i].getValeur() + "' alt='" + getListeChamp()[i].getValeur() + "' style='width:100%;'/>" +lienFin+ " </td>";
                    temp += "</tr>";
                } else if (getListeChamp()[i].getVisible() == true) {
                    temp += "<tr>";
                    temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>" + getListeChamp()[i].getLibelle() + "</B></td>";
                    //if()System.out.println(" instance classe etat page consulte");
                    if(getListeChamp()[i].getNom().compareToIgnoreCase("etat") == 0 && getBase() instanceof bean.ClassEtat){ // 
                        temp += "<td>" +lienDebut+ "<b>" + ((bean.ClassEtat)getBase()).getEtatText(Utilitaire.stringToInt(getListeChamp()[i].getValeur())) + "</b>" +lienFin+ "</td>";
                    }
                    else{
                        String css = "";
                        if(getListeChamp()[i].getLibelle().contains("mail")){
                            css = "style='text-transform: lowercase;'";
                        };
                        temp += "<td "+css+">" +lienDebut+  getListeChamp()[i].getValeur() +lienFin+ "</td>";
                    }
                    temp += "</tr>";
                }
            }
            temp += "<input type='hidden' name='id' value='" + getListeChamp()[0].getValeur() + "'/>";

            temp += "</table>";
            temp += "</div></div>";
        }
        else{
            int nb2 = getListeChamp().length/2;
            
            temp += "<div class='row'>";            
                temp += "<div class='col-md-6'>";
                    temp += "<table class='table table-bordered'>";
                for (int i = 0; i < nb2; i++) {
                    Champ champ= getListeChamp()[i];
                    String lienDebut =  champ.getLien() ==null ? "" : " <a href='"+ champ.getLien().getLien() +"&" + champ.getLien().getQueryString() + getListeChamp()[i].getValeur() + "'>" ;
                    //System.out.println("lienDebut ==== " + lienDebut);
                    String lienFin= champ.getLien()==null ?"": "</a>";
                    if (getListeChamp()[i].getTypeData().compareToIgnoreCase("date") == 0) {
                    }
                    if (getListeChamp()[i].isPhoto()) {
                        temp += "<tr>";
                        temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>"+ getListeChamp()[i].getLibelle() + "</B></td>";
                        temp += "<td>" +lienDebut+ "<img src='" + prop.getProperty("cdnReadUri") + getListeChamp()[i].getValeur() + "' alt='" + getListeChamp()[i].getValeur() + "' style='width:100%;'/>" +lienFin+ " </td>";
                        temp += "</tr>";
                    } else if (getListeChamp()[i].getVisible() == true) {
                        temp += "<tr>";
                        temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>" + getListeChamp()[i].getLibelle() + "</B></td>";
                        //if()System.out.println(" instance classe etat page consulte");
                        if(getListeChamp()[i].getNom().compareToIgnoreCase("etat") == 0 && getBase() instanceof bean.ClassEtat){ // 
                            temp += "<td>" +lienDebut+ "<b>" + ((bean.ClassEtat)getBase()).getEtatText(Utilitaire.stringToInt(getListeChamp()[i].getValeur())) + "</b>" +lienFin+ "</td>";
                        }
                        else{
                            String css = "";
                            if(getListeChamp()[i].getLibelle().contains("mail")){
                                css = "style='text-transform: lowercase;'";
                            }
                            temp += "<td "+css+">" +lienDebut+  getListeChamp()[i].getValeur() +lienFin+ "</td>";
                        }
                        temp += "</tr>";
                    }
                }
                    temp += "</table>";
                temp+= "</div>";
            
                temp += "<div class='col-md-6'>";
                    temp += "<table class='table table-bordered'>";
                        for (int i = nb2; i < getListeChamp().length; i++) {
                            Champ champ= getListeChamp()[i];
                            String lienDebut =  champ.getLien() ==null ? "" : " <a href='"+ champ.getLien().getLien() +"&" + champ.getLien().getQueryString() + getListeChamp()[i].getValeur() + "'>" ;
                            //System.out.println("lienDebut ==== " + lienDebut);
                            String lienFin= champ.getLien()==null ?"": "</a>";
                            if (getListeChamp()[i].getTypeData().compareToIgnoreCase("date") == 0) {
                            }
                            if (getListeChamp()[i].isPhoto()) {
                                temp += "<tr>";
                                temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>"+ getListeChamp()[i].getLibelle() + "</B></td>";
                                temp += "<td>" +lienDebut+ "<img src='" + prop.getProperty("cdnReadUri") + getListeChamp()[i].getValeur() + "' alt='" + getListeChamp()[i].getValeur() + "' style='width:100%;'/>" +lienFin+ " </td>";
                                temp += "</tr>";
                            } else if (getListeChamp()[i].getVisible() == true) {
                                temp += "<tr>";
                                temp += "<td class='" + getListeChamp()[i].getCssLibelle() + "'><B>" + getListeChamp()[i].getLibelle() + "</B></td>";
                                //if()System.out.println(" instance classe etat page consulte");
                                if(getListeChamp()[i].getNom().compareToIgnoreCase("etat") == 0 && getBase() instanceof bean.ClassEtat){ // 
                                    temp += "<td>" +lienDebut+ "<b>" + ((bean.ClassEtat)getBase()).getEtatText(Utilitaire.stringToInt(getListeChamp()[i].getValeur())) + "</b>" +lienFin+ "</td>";
                                }
                                else{
                                    temp += "<td>" +lienDebut+  getListeChamp()[i].getValeur() +lienFin+ "</td>";
                                }
                                temp += "</tr>";
                        }
                }
                    temp += "</table>";
                temp+= "</div>";
            temp+= "</div>";
        }                
        setHtml(temp);
    }
    
    /**
     * Permet d'accéder à la valeur d'un attribut
     * @param nomChamp : nom de l'attribut
     * @return valeur du champs donné
     * @throws Exception
     */
    public String getValeurByNom(String nomChamp) throws Exception{
        return CGenUtil.getValeurFieldByMethod(getBase(), nomChamp).toString();
    }

    public void setCritere(bean.ClassMAPTable critere) {
        this.critere = critere;
    }
    /**
     * 
     * @return objet de mapping avec valeurs d'attribut initialisée pour effectuer des recherches
     */
    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setLibAffichage(String[] libAffichag) throws Exception {
        /*if (libAffichag.length != getListeChamp().length) {
            throw new Exception("Nombre de champ non valide dans les lib specifiques");
        }*/
        //this.libAffichage = libAffichag;
        for (int i = 0; i < libAffichag.length; i++) {
            Champ c = getListeChamp()[i];
            c.setLibelle(libAffichag[i]);
        }
    }

    public String[] getLibAffichage() {
        return libAffichage;
    }

    public void setCssTableau(String cssTableau) {
        this.cssTableau = cssTableau;
    }

    public String getCssTableau() {
        return cssTableau;
    }


    /**
     * Permet de créer un html qui se trouve en bas du page fiche.
     * Cet html est une liste d'utilité générique(uploader fichier) selon le type d'objet à consulter.
     * @return html qui se trouve en bas du page fiche 
     */
    public String getBasPage() {
    	String uploadXhtml = (isCanUploadFile()) ? "<td style=\"padding:5px;\"><a class=\"btn btn-success\" href=\"" + getReq().getSession().getAttribute("lien") + "?but=pageupload.jsp&id=" + getReq().getParameter("id") + "&nomtable=" + fichier_table_cible + "&procedure=" + fichier_table_procedure + "&bute=" + getReq().getParameter("but") + "\">Attacher fichier</a></td>" : "";
        String retour = "";
        
        //System.out.println("\n\n\n\n\n\n");
        //System.out.println(this.getHtml());
        
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center style=\"text-align: center;\">";
        retour += "<tr>"
                + uploadXhtml
                + "<td style=\"padding:5px;\"><form action=\"" + getReq().getSession().getAttribute("lien") + "?but=apresTarif.jsp\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"nomClasseFille\" value=\"" + this.getNomClasseFilleADuplique() + "\">"
                + "<input type=\"hidden\" name=\"nomColonneMere\" value=\"" + this.getNomColonneMere() + "\">"
                + "<input type=\"hidden\" name=\"bute\" value=\"" + getReq().getParameter("but") + "\">"
                + "<input type=\"hidden\" name=\"id\" value=\"" + getReq().getParameter("id") + "\">"
                + "<input type=\"hidden\" name=\"rajoutLien\" value=\"id\">"
                + "<input type=\"hidden\" name=\"acte\" value=\"dupliquer\">"
                + "<input type=\"hidden\" name=\"nomtable\" value=\""+getNomtable()+"\">"
                + "<input type=\"hidden\" name=\"classe\" value=\""+this.getCritere().getClassName()+"\">"
                + "<td style=\"padding:5px;\"><form action=\"../ExportServlet\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"but\" value=\"" + getReq().getParameter("but") + "\">"
                + "<input type=\"hidden\" name=\"titre\" value=\"" + getTitre() + "\">"
                + "<input type=\"hidden\" name=\"htmlcontent\" value=\"" + this.getHtml() + "\">"
                //+ "<button type=\"submit\" class=\"btn btn-warning\">Export PDF</button>
                + "</form></td>";
        if(getBase().getClass().getSuperclass().getSimpleName()!=null && getBase().getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat")==0){
            retour+= "<td style=\"padding:5px;\"><form action=\"" + getReq().getSession().getAttribute("lien") + "?but=apresTarif.jsp\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"bute\" value=\"" + getReq().getParameter("but") + "\">"
                + "<input type=\"hidden\" name=\"id\" value=\"" + getReq().getParameter("id") + "\">"
                + "<input type=\"hidden\" name=\"rajoutLien\" value=\"id\">"
                + "<input type=\"hidden\" name=\"acte\" value=\"annulerVisa\">"
                + "<input type=\"hidden\" name=\"nomtable\" value=\""+getNomtable()+"\">"
                + "<input type=\"hidden\" name=\"classe\" value=\""+this.getCritere().getClassName()+"\">";
            retour +="</form></td>";
        }
        retour += "</tr>";
        retour += "</table>";
        return retour;
    }

    /**
     * Permet d'apparaitre un bouton exporter
     * @return bouton exporter pdf
     */
    public String getBasPageSansUpload() {
        String retour = "";
        retour += "<table border=0 cellpadding=0 cellspacing=0 align=center style=\"text-align: center;\">";
        retour += "<tr>"
                + "<td style=\"padding:5px;\"><form action=\"../ExportServlet\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"but\" value=\"" + getReq().getParameter("but") + "\">"
                + "<input type=\"hidden\" name=\"titre\" value=\"" + getTitre() + "\">"
                + "<input type=\"hidden\" name=\"htmlcontent\" value=\"" + this.getHtml() + "\">"
                + "<button type=\"submit\" class=\"btn btn-warning\">Export PDF</button></form></td>";
        retour += "</tr>";
        retour += "</table>";
        return retour;
        //return getbas
    }
    /**
     * @deprecated
     * @param apres
     */
    public void setApres(String apres) {
        this.apres = apres;
    }

    public String getApres() {
        return apres;
    }
    /**
     * @deprecated
     * @param apres
     */
    public void setApresLienPage(String apresLienPage) {
        this.apresLienPage = apresLienPage;
    }
    /**
     * @deprecated
     * @param apres
     */
    public String getApresLienPage() {
        return apresLienPage;
    }

    public void setDonnee(String d) {
        getReq().getSession().setAttribute("donnee", d);
    }

    public String getDonnee() {
        return donnee;
    }
    public String getNomtable()
    {
        return this.nomtable;
    }
    public void setNomtable(String nomtable)
    {
        this.nomtable=nomtable;
    }

    /**
     * @deprecated : return null 
     * @param nomChamp : nom de l'attribut
     * @return : null
     */
    public Champ getChampByName(String nomChamp) {
        for (int i = 0; i < getListeChamp().length; i++) {
            if (getListeChamp()[i].getNom().compareToIgnoreCase(nomChamp) == 0) {
                return getListeChamp()[i];
            }
        }
        return null;
    }
}
