package affichage;

import java.sql.Connection;
import java.lang.reflect.Field;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import utilitaire.Utilitaire;

/**
 * Objet à utiliser à l'insertion pour génerer la page de saisie d'un objet de mapping donné.
 * Ci-dessous une exemple de comment créer une page de saisie. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 *  Avoir  objet = new Avoir();
 *  objet.setNomTable(nomtable);
 *  PageInsert pi = new PageInsert(objet, request, u);
 *  pi.getFormu().changerEnChamp(liste);
 *  pi.preparerDataFormu();
 * 
 * }
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * <pre>
 * {@code 
 *  <%
 * pi.getFormu().makeHtmlInsertTabIndex();
 * out.println(pi.getFormu().getHtmlInsert());
 *  %>
 * 
 * }
 * 
 * @author BICI
 * @version 1.0
 */
public class PageInsert extends Page {

    protected String champReturn;
    protected String champUrl;

    public String getChampReturn() {
        return champReturn;
    }
    public void setChampReturn(String champReturn) {
        this.champReturn = champReturn;
    }
    public String getChampUrl() {
        return champUrl;
    }
    public void setChampUrl(String champUrl) {
        this.champUrl = champUrl;
    }
    /**
     * Constructeur par défaut
     */
    public PageInsert() {

    }

    /**
     * constructeur 
     * @param o classe mapping
     * @param req  contexte HTTP de la page
     * @param u session de l'utilisateur courant
     * @throws Exception
     */
    public PageInsert(ClassMAPTable o, HttpServletRequest req, user.UserEJB u) throws Exception {
        setBase(o);
        setReq(req);
        setUtilisateur(u);
        makeFormulaire();
    }

    /**
     * 
     * @param o classe mapping
     * @param req contexte HTTP de la page
     * @throws Exception
     */
    public PageInsert(ClassMAPTable o, HttpServletRequest req) throws Exception {
        setBase(o);
        setReq(req);
    }

    /**
     * @deprecated tout en commentaire 
     * @throws Exception
     */
    public void makeFormulaireFF() throws Exception{
    } 
    

    /**
     * creation du formulaire 
     * qui comprend comme champs les intersections des listes d'attribut de l'objet de mapping
     * et les listes des colonnes dans la base 
     * @throws Exception
     */
    public void makeFormulaire() throws Exception {
//        if(getBase().getClassName().compareToIgnoreCase("mg.gallois.facture.FactureFournisseur")==0){
//            makeFormulaireFF();
//            return;
//        } 
        setChampReturn(getReq().getParameter("champReturn"));
        setChampUrl(getReq().getParameter("champUrl"));       
        formu = new Formulaire();
        affichage.Champ[] t = null;
        //Field[]f=getBase().getFieldList();
        bean.Champ[] f = bean.ListeColonneTable.getFromListe(getBase(), null);
        //t=new Champ[getBase().getNombreChamp()-1];
        t = new Champ[f.length - 1];
        int nbElement = 0;
        for (int i = 0; i < t.length + 1; i++) {
            if (f[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName()) != 0) {
                    if (f[i].getTypeJava().compareToIgnoreCase("double") == 0 || f[i].getTypeJava().compareToIgnoreCase("int") == 0 || f[i].getTypeJava().compareToIgnoreCase("float") == 0) {
                        t[nbElement] = new ChampCalcul(f[i].getNomColonne());
                    } else if (f[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {
                        t[nbElement] = new ChampDate(f[i].getNomColonne());
                    } else {
                        t[nbElement] = new Champ(f[i].getNomColonne());
                        t[nbElement].setLibelle(f[i].getNomColonne());
                        t[nbElement].setValeur("");
                    }
                    nbElement++;
             }
        }
        formu.setListeChamp(t);
    }


    /**
     * Sert à inserser des données dans les selects et les auto complete
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
    * @deprecated fonction vide 
    */
    public void makeHtml() {

    }

    /**
     * Définir les valeurs des attribut de l'objet de base de la base à partir du contexte HTTP
     * @return l'objet de base de la page
     * @throws Exception
     */
    public ClassMAPTable getObjectAvecValeur() throws Exception {
        //Field[]tempChamp=getBase().getFieldList();
        try {
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getBase());
            for (int i = 0; i < tempChamp.length; i++) {
                Field f = tempChamp[i];
                String valeur = getParamSansNull(f.getName());
                getBase().setMode("modif");
                if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    if((valeur==null||(valeur!=null&&valeur.compareTo("")==0)))
                    {
                        valeur=getParamSansNull(f.getName()+"libelle");
                    }
                    bean.CGenUtil.setValChamp(getBase(), f, valeur);
                }
                if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                    //System.out.println("La date en sortie = "+utilitaire.Utilitaire.string_date("dd/MM/yyyy",valeur));
                    bean.CGenUtil.setValChamp(getBase(), f, utilitaire.Utilitaire.string_date("dd/MM/yyyy", valeur));
                }
                if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                    if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(0));
                    } else {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(valeur));
                    }
                }
                if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Float(utilitaire.Utilitaire.stringToFloat(valeur)));
                }
                //if(getBase().getClassName().endsWith("Matiere"))
                //  System.out.println("valeur="+valeur);
            }
            return getBase();
        } catch (NumberFormatException n) {
            n.printStackTrace();
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            //System.out.println("ERREUUURRR = "+e.getMessage());
            throw e;
        }
    }
    
    public String getHtmlAddOnPopup() {
        String rep = "";
        if(this.champUrl!=null && this.champReturn!=null){
            rep += "<input type=\"hidden\" name=\"champReturn\" value=\""+this.champReturn+"\" />";
            rep += "<input type=\"hidden\" name=\"champUrl\" value=\""+this.champUrl+"\" />";
            rep += "<input type=\"hidden\" name=\"rajoutLien\" value=\"champReturn-champUrl"+this.getRajoutLienAPartirChampUrl()+"\" />";
        }
        return rep;
    }

    private String getRajoutLienAPartirChampUrl() {
        String[] champs = this.champUrl.split(";");
        if(champs[0].compareToIgnoreCase(champs[1])!=0){
            return "-"+champs[1];
        }
        return "";
    }

    /**
     * Définir les valeurs des attribut de l'objet de base  à partir d'un dictionnaire
     * @param listeValeur dictionnaire avec la liste de valeur directement
     * @return l'objet de base
     * @throws Exception
     */
    public ClassMAPTable getObjectAvecValeur(HashMap<String, String> listeValeur) throws Exception {
        try {
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getBase());
            for (int i = 0; i < tempChamp.length; i++) {
                Field f = tempChamp[i];
                String valeur = getParamSansNull(f.getName(), listeValeur);
                getBase().setMode("modif");
                if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, valeur);
                }
                if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, utilitaire.Utilitaire.string_date("dd/MM/yyyy", valeur));
                }
                if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                    if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(0));
                    } else {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(valeur));
                    }
                }
                if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Float(utilitaire.Utilitaire.stringToFloat(valeur)));
                }
            }
            return getBase();
        } catch (NumberFormatException n) {
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            throw e;
        }
    }
}
