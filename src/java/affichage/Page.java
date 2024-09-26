package affichage;

import bean.CGenUtil;
import java.lang.reflect.Field;
import bean.ClassMAPTable;
import bean.ListeColonneTable;
import utilitaire.Utilitaire;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * Classe mère de toutes les pages. Cette classe est une classe reservée pour génerer des htmls de pages à partir d'un objet donné et de requête HTTP.
 * Cette classe ne devrait pas être directement initialisée, il est nécessaire de passer par les classes filles.
 * 
 * <pre>
 * {@code}
 *  public class EnfantPage extends Page {
 *  //logique des pages
 * }
 * </pre>
 * 
 * @author BICI
 * @version 1.0
 */
public class Page {

    Formulaire formu;
    private TableauRecherche tableau;
    private TableauRecherche tableauRecap;
    private String lien;
    private TableauRecherche tableauRegroup;
    private bean.ClassMAPTable base;
    private bean.ClassMAPTable[] baseTableau;
    private String titre;
    private user.UserEJB utilisateur;
    private String html;
    private javax.servlet.http.HttpServletRequest req;
    private boolean testRecap = false;
    private int nombreLigne = 0;
    private String pathPage;
    private String[] tabIndexe;

    /**
     * 
     * @return liste des indice des lignes à prendre en compte 
     */
    public String[] getTabIndexe() {
        return tabIndexe;
    }

    public void setTabIndexe(String[] tabIndexe) {
        this.tabIndexe = tabIndexe;
    }
    
    public int getNombreLigne() {
        return nombreLigne;
    } 
    public String getPathPage() {
        return pathPage;
    }

    public void setPathPage(String pathPage) {
        this.pathPage = pathPage;
    }
    /**
     * 
     * @param nombreLigne si inférieur à 0 automatiquement 10
     */
    public void setNombreLigne(int nombreLigne) {
        if (nombreLigne <= 0) {
            this.nombreLigne = 10;
        }
        this.nombreLigne = nombreLigne;
    } 
    /**
     * Constructeur par défaut
     */
    public Page() {
    }

    public Page(ClassMAPTable p, HttpServletRequest r) {
        setBase(p);
        setReq(r);
    }
    
    public Page(ClassMAPTable p, HttpServletRequest r, int nombreligne) {
        setBase(p);
        setReq(r);
        setNombreLigne(nombreligne);
    }
    /**
     * Fonction à surdefinir pour génerer le html du corps de la page
     */
    public void makeHtml() {

    }
    /**
     * @return formulaire associée à la page, si page recherche formulaire de recherche sinon formulaire d'entrée/update
     */
    public Formulaire getFormu() {
        return formu;
    }

    public void setFormu(Formulaire formu) {
        this.formu = formu;
    }

    /**
     * 
     * @param tableau tableau associé à la page
     */

    public void setTableau(TableauRecherche tableau) {
        this.tableau = tableau;
    }
    /**
     * 
     * @return tableau associé à la page
     */
    public TableauRecherche getTableau() {
        return tableau;
    }

    public void setBase(bean.ClassMAPTable base) {
        this.base = base;
    }
    /**
     * 
     * @param baseT
     */
    public void setBaseTableau(bean.ClassMAPTable[] baseT) {
        this.baseTableau = baseT;
    }
    
    public bean.ClassMAPTable[] getBaseTableau() {
        return baseTableau;
    }

    /**
     * @return classe de base associée à la page
     */
    public bean.ClassMAPTable getBase() {
        return base;
    }
    /**
     * Lien 
     * @param lien lien de base de navigation
     */
    public void setLien(String lien) {
        this.lien = lien;
    }
    /**
     * 
     * @return lien de base de navigation
     */
    public String getLien() {
        return lien;
    }
    /**
     * 
     * @return tableau de récapitulation
     */
    public TableauRecherche getTableauRecap() {
        return tableauRecap;
    }

    public void setTableauRecap(TableauRecherche tableauRecap) {
        this.testRecap = true;
        this.tableauRecap = tableauRecap;
    }

    public void setTableauRegroup(TableauRecherche tableauRegroup) {
        this.tableauRegroup = tableauRegroup;
    }

    public TableauRecherche getTableauRegroup() {
        return tableauRegroup;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    /**
     * 
     * @return titre de la page
     */
    public String getTitre() {
        return titre;
    }

    public void setUtilisateur(user.UserEJB utilisateur) {
        this.utilisateur = utilisateur;
    }

    public boolean getTestRecap() {
        return testRecap;
    }
    /**
     * @return session utilisateur de l'utilisateur courant
     */
    public user.UserEJB getUtilisateur() {
        return utilisateur;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    /**
     * prendre le html du corps de la page si déjà fait sinon génerer
     * @return html 
     */
    public String getHtml() {
        if (html == null || html.compareToIgnoreCase("") == 0) {
            makeHtml();
        }
        return html;
    }
    /**
     * définir le contexte HTTP de la page. 
     * Ceci va aussi initialisé le lien de la page
     * @param req : requête HTTP ouverte
     */
    public void setReq(javax.servlet.http.HttpServletRequest req) {
        this.req = req;
        if(this.getReq()!=null) {
            this.setPathPage(this.getReq().getParameter("but"));
        }
    }
    /**
     * 
     * @return contexte HTTP de la page
     */
    public javax.servlet.http.HttpServletRequest getReq() {
        return req;
    }
    /**
     * récuperer la valeur d'un paramètre sans null.
     * @param nomP nom de paramètre
     * @return valeur du paramètre en chaine de caractère
     */
    public String getParamSansNull(String nomP) {
        String temp = getReq().getParameter(Utilitaire.remplacerUnderscore(nomP));
        if ((temp != null) && temp.compareToIgnoreCase("") != 0) {
            temp = temp.replace("'", "\'");
            return temp;
        }
        return "";
    }
    /**
     * récuperer la valeur d'un paramètre sans null.
     * @param nomP nom de paramètre
     * @param valeurs dictionnaire avec des paramètres
     * @return valeur du paramètre en chaine de caractère
     */
    public String getParamSansNull(String nomP, HashMap<String, String> valeurs) {
        String temp = valeurs.get(Utilitaire.remplacerUnderscore(nomP));
        if ((temp != null) && temp.compareToIgnoreCase("") != 0) {
            return temp;
        }
        return "";
    }
    /**
     * Définir les valeurs des champs de chaque composant du formulaire à partir du contexte HTTP
     *  si paramètre correspondant existe
     * @throws Exception
     */
    public void getValeurFormulaire() throws Exception {
        Champ[] c = formu.getListeChamp();  
        for (int i = 0; i < c.length; i++) {
            String valeurRecup = getParamSansNull(c[i].getNom());

            c[i].setValeur(valeurRecup);
        }
        Champ[] f = formu.getChampGroupe();
        if (f != null) {
            for (int j = 0; j < f.length; j++) {
                f[j].setValeur(getParamSansNull(f[j].getNom()));
            }
        }
        Champ[] af = formu.getChampTableauAff();
        if (af != null) {
            for (int j = 0; j < af.length; j++) {
                af[j].setValeur(getParamSansNull(af[j].getNom()));
            }
        }
    }
    /**
     * Définir les valeurs des attribut de l'objet de base de la base à partir du contexte HTTP
     * @return l'objet de base de la page
     * @throws Exception
     */
    public ClassMAPTable getObjectAvecValeur() throws Exception {
        //Field[]tempChamp=getBase().getFieldList();
        Field[] tempChamp = ListeColonneTable.getFieldListeHeritage(getBase());
        for (int i = 0; i < tempChamp.length; i++) {
            Field f = tempChamp[i];
            String valeur = getParamSansNull(f.getName());
            if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                bean.CGenUtil.setValChamp(getBase(), f, valeur);
            }
            if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                //System.out.println("La date en sortie = "+Utilitaire.string_date("dd/MM/yyyy",valeur));
                bean.CGenUtil.setValChamp(getBase(), f, Utilitaire.string_date("dd/MM/yyyy", valeur));
            }
            if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                bean.CGenUtil.setValChamp(getBase(), f, new Double(Utilitaire.stringToDouble(valeur)));
            }
            if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                if (valeur != null && valeur.compareToIgnoreCase("") != 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Integer(valeur));
                }
            }
            if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                bean.CGenUtil.setValChamp(getBase(), f, new Float(Utilitaire.stringToFloat(valeur)));
            }
        }
        if(getBase().getEstHistorise()){
            getBase().setMemo(getParamSansNull("memo"));
        }
        return getBase();
    }
    
    public static String getContenuServletWithTiret(String valeurFiltre,String nomTable,String nomClasse,String nomColoneFiltre,String nomColvaleur,String nomColAffiche,boolean esttiret) throws Exception
    {
        ClassMAPTable filtre=(ClassMAPTable)Class.forName(nomClasse).newInstance();
        filtre.setNomTable(nomTable);
        String rekety="select * from "+nomTable+" where "+nomColoneFiltre+"='"+valeurFiltre+"'";
        ClassMAPTable listee[]=(ClassMAPTable[])CGenUtil.rechercher(filtre , rekety);
        String valeur="[";
        if(esttiret){
            valeur+="{\"id\":\"" + "" +"\",\"valeur\":\"" + "-" +"\"}";
             if(listee.length>0)valeur+=",";
        }
        for(int i=0;i<listee.length;i++)
        {
            ClassMAPTable to=listee[i];
            String valeurId=(String)CGenUtil.getValeurFieldByMethod(to, nomColvaleur);
            String valeurAffiche=(String)CGenUtil.getValeurFieldByMethod(to, nomColAffiche);
            valeur+="{\"id\":\"" + valeurId+"\",\"valeur\":\"" + valeurAffiche+"\"}";
            if(i<listee.length-1)valeur+=",";
        }
        valeur+="]";
        return("{\"valeure\":" + valeur +"}");
    }
    /**
     * Définir les valeurs des attribut de l'objet de base  à partir d'un dictionnaire
     * @param listeValeur dictionnaire avec la liste de valeur directement
     * @return l'objet de base
     * @throws Exception
     */
    public ClassMAPTable getObjectAvecValeur(HashMap<String, String> listeValeur) throws Exception {
        try {
            Field[] tempChamp = ListeColonneTable.getFieldListeHeritage(getBase());
            for (int i = 0; i < tempChamp.length; i++) {
                Field f = tempChamp[i];
                String valeur = getParamSansNull(f.getName(), listeValeur);
                if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, valeur);
                }
                if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, Utilitaire.string_date("dd/MM/yyyy", valeur));
                }
                if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Double(Utilitaire.stringToDouble(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                    if (valeur != null && valeur.compareToIgnoreCase("") != 0) {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(valeur));
                    }
                }
                if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Float(Utilitaire.stringToFloat(valeur)));
                }
            }
            return getBase();
        } catch (NumberFormatException n) {
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Génerer une liste d'objet de base avec les valeurs des attributs définies à partir du contexte HTTP
     * @return liste d'objets de base avec critère
     * @throws Exception
     */
    public ClassMAPTable[] getObjectAvecValeurTableau() throws Exception {
        //Field[]tempChamp=getBase().getFieldList();
        int nombreLigne = getNombreLigne();
        ClassMAPTable[] liste = new ClassMAPTable[nombreLigne];
        try {
            Field[] tempChamp = ListeColonneTable.getFieldListeHeritage(getBase());
            int x=0;
            for (int iLigne = 0; iLigne < nombreLigne; iLigne++) {
                ClassMAPTable ex = (ClassMAPTable)Class.forName(getBase().getClassName()).newInstance();
                for (int i = 0; i < tempChamp.length; i++) {
                    Field f = tempChamp[i];
                    String nomChamp = f.getName()+"_"+iLigne;
                    String valeur = getParamSansNull(nomChamp);
                    if(i==1 && (valeur==null || valeur.compareTo("")==0)){break;}
                    if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                        bean.CGenUtil.setValChamp(ex, f, valeur);
                    }
                    if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                        //System.out.println("La date en sortie = "+Utilitaire.string_date("dd/MM/yyyy",valeur));
                        bean.CGenUtil.setValChamp(ex, f, Utilitaire.string_date("dd/MM/yyyy", valeur));
                    }
                    if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                        bean.CGenUtil.setValChamp(ex, f, new Double(Utilitaire.stringToDouble(valeur)));
                    }
                    if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                        if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                            bean.CGenUtil.setValChamp(ex, f, new Integer(0));
                        } else {
                            bean.CGenUtil.setValChamp(ex, f, new Integer(valeur));
                        }
                    }
                    if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                        bean.CGenUtil.setValChamp(ex, f, new Float(Utilitaire.stringToFloat(valeur)));
                    }
                    if(i==1) x++;
                }
                liste[iLigne]  = ex;
            }
            ClassMAPTable[] ret = new ClassMAPTable[x]; 
            for(int j = 0; j<x ; j++){
                ret[j] = liste[j];
            }
            return ret;
        } catch (NumberFormatException n) {
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            //System.out.println("ERREUUURRR = "+e.getMessage());
            throw e;
        }
    }
    /**
     * Définir les valeurs des champs de formulaire avec support de paramètre à valeur multiple
     * @throws Exception
     */
    public void getValeurFormulaireMultiple() throws Exception {
        Champ[] c = formu.getListeChamp();
        for (int i = 0; i < c.length; i++) {
            String valeurRecup = getParamSansNullMultiple(c[i].getNom());
            c[i].setValeur(valeurRecup);
        }
        Champ[] f = formu.getChampGroupe();
        if (f != null) {
            for (int j = 0; j < f.length; j++) {
                f[j].setValeur(getParamSansNullMultiple(f[j].getNom()));
            }
        }
        Champ[] af = formu.getChampTableauAff();
        if (af != null) {
            for (int j = 0; j < af.length; j++) {
                af[j].setValeur(getParamSansNullMultiple(af[j].getNom()));
            }
        }
    }
    /**
     * Formatter un paramètre avec valeur multiple venant du contexte HTTP 
     * en une chaine de charactère délimitée par virgule
     * @param nomP nom du paramètre
     * @return
     */
    public String getParamSansNullMultiple(String nomP) {
        String[] listetemp = getReq().getParameterValues(Utilitaire.remplacerUnderscore(nomP));
        String temp = "";
        if (listetemp != null) {
            temp = listetemp[0];
            for (int i = 1; i < listetemp.length; i++) {
                if (listetemp[i].compareToIgnoreCase("") != 0) {
                    temp = temp + "','" + listetemp[i];
                }
            }
        }
        if (nomP.compareTo("colonne") == 0 || nomP.compareTo("ordre") == 0) {
            temp = getReq().getParameter(Utilitaire.remplacerUnderscore(nomP));
        }
        if ((temp != null) && temp.compareToIgnoreCase("") != 0) {
            return temp;
        }
        return "";
    }
    /**
     * Génerer un JSON avec liste d'objets pour un critère donné
     * @param valeurFiltre valeur
     * @param nomTable nom de la table de sélection
     * @param nomClasse nom de la classe de retour
     * @param nomColoneFiltre nom de la colonne en base pour le filtre
     * @param nomColvaleur champ d'ID en retour
     * @param nomColAffiche champ valeur en retour
     * @return liste de clé/value après filtre en json <pre> {
     *   valeure:[
     *    {
     *      id: "1",
     *      valeur: "test"
     *    }
     * ]
     * }
     * </pre>
     *  
     * @throws Exception
     */
    public static String getContenuServlet(String valeurFiltre,String nomTable,String nomClasse,String nomColoneFiltre,String nomColvaleur,String nomColAffiche) throws Exception
    {
        ClassMAPTable filtre=(ClassMAPTable)Class.forName(nomClasse).newInstance();
        filtre.setNomTable(nomTable);
        String rekety="select * from "+nomTable+" where "+nomColoneFiltre+"='"+valeurFiltre+"'";
        ClassMAPTable listee[]=(ClassMAPTable[])CGenUtil.rechercher(filtre , rekety);
        String valeur="[";
        for(int i=0;i<listee.length;i++)
        {
            ClassMAPTable to=listee[i];
            String valeurId=(String)CGenUtil.getValeurFieldByMethod(to, nomColvaleur);
            Object val=CGenUtil.getValeurFieldByMethod(to, nomColAffiche);
            String valeurAffiche=val.toString();
            valeur+="{\"id\":\"" + valeurId+"\",\"valeur\":\"" + valeurAffiche+"\"}";
            if(i<listee.length-1)valeur+=",";
        }
        valeur+="]";
        return("{\"valeure\":" + valeur +"}");
    }
    /**
     * Génerer un JSON avec liste d'objets pour un critère donné
     * avec support de order by 
     * et possibilité de retourner qu'une seule valeur
     * @param valeurFiltre valeur de filtre
     * @param nomTable nom de la table de sélection
     * @param nomClasse nom de la classe de retour
     * @param nomColoneFiltre nom de la colonne en base pour le filtre
     * @param nomColvaleur champ d'ID en retour
     * @param nomColAffiche champ valeur en retour
     * @param nomOrderby colonne d'ordre
     * @param sensOrderBy ASC/DESC sens de l'order by
     * @param liste si faux on ne retourne qu'une seule valeur
     * @return liste de clé/value après filtre en json <pre> {
     *   valeure:[
     *    {
     *      id: "1",
     *      valeur: "test"
     *    }
     * ]
     * }
     * </pre>
     * @throws Exception
     */
    public static String getContenuServlet(String valeurFiltre,String nomTable,String nomClasse,String nomColoneFiltre,String nomColvaleur,String nomColAffiche,String nomOrderby, String sensOrderBy,boolean liste) throws Exception
    {
        ClassMAPTable filtre=(ClassMAPTable)Class.forName(nomClasse).newInstance();
        filtre.setNomTable(nomTable);
        String rekety="select * from "+nomTable+" where "+nomColoneFiltre+"='"+valeurFiltre+"' order by "+nomOrderby+" "+sensOrderBy;
        ClassMAPTable listee[]=(ClassMAPTable[])CGenUtil.rechercher(filtre , rekety);
        int taille=listee.length;
        if(!liste)taille=1;
        String valeur="[";
        for(int i=0;i<taille;i++)
        {
            ClassMAPTable to=listee[i];
            String valeurId=(String)CGenUtil.getValeurFieldByMethod(to, nomColvaleur);
            String valeurAffiche=CGenUtil.getValeurFieldByMethod(to, nomColAffiche).toString();
            valeur+="{\"id\":\"" + valeurId+"\",\"valeur\":\"" + valeurAffiche+"\"}";
            if(i<taille-1)valeur+=",";
        }
        valeur+="]";
        return("{\"valeure\":" + valeur +"}");
    }
    /**
     * Génerer une liste d'objet de base avec les valeurs des attributs définies à partir du contexte HTTP
     * Selon liste de table index
     * @return liste d'objets de base avec critère
     * @throws Exception
     */
    public ClassMAPTable[] getObjectAvecValeurTableauUpdate() throws Exception {
        //Field[]tempChamp=getBase().getFieldList();
        int nombreLigne = getNombreLigne();
        String[] tabIndexe = getTabIndexe();
        if(tabIndexe == null)
            throw new Exception("Vous devez cocher");
        ClassMAPTable[] liste = new ClassMAPTable[tabIndexe.length];
        try {
            Field[] tempChamp = ListeColonneTable.getFieldListeHeritage(getBase());
            int x=0;
            for (int iLigne = 0; iLigne < nombreLigne; iLigne++) {
                ClassMAPTable ex = (ClassMAPTable)Class.forName(getBase().getClassName()).newInstance();
                for(int indice=0; indice<tabIndexe.length; indice++){
                    String ligne = ""+iLigne;
                    if(ligne.equals(tabIndexe[indice])){
                        for (int i = 0; i < tempChamp.length; i++) {
                            Field f = tempChamp[i];
                            String nomChamp = f.getName()+"_"+iLigne;
                            String valeur = getParamSansNull(nomChamp);
                            if(valeur != null && valeur.compareTo("")!= 0){
                                if(i==1 && (valeur==null || valeur.compareTo("")==0)){break;}
                                if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                                    bean.CGenUtil.setValChamp(ex, f, valeur);
                                }
                                if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                                    //System.out.println("La date en sortie = "+Utilitaire.string_date("dd/MM/yyyy",valeur));
                                    bean.CGenUtil.setValChamp(ex, f, Utilitaire.string_date("dd/MM/yyyy", valeur));
                                }
                                if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                                    bean.CGenUtil.setValChamp(ex, f, new Double(Utilitaire.stringToDouble(valeur)));
                                }
                                if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                                    if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                                        bean.CGenUtil.setValChamp(ex, f, new Integer(0));
                                    } else {
                                        bean.CGenUtil.setValChamp(ex, f, new Integer(valeur));
                                    }
                                }
                                if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                                    bean.CGenUtil.setValChamp(ex, f, new Float(Utilitaire.stringToFloat(valeur)));
                                }
                                if(i==1) x++;
                            }
                        }
                        liste[indice]  = ex;
                    }
                }
            }
             ClassMAPTable[] ret = new ClassMAPTable[liste.length]; 
            for(int j = 0; j<liste.length ; j++){
                ret[j] = liste[j];
            }
            return ret;
        } catch (NumberFormatException n) {
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            //System.out.println("ERREUUURRR = "+e.getMessage());
            throw e;
        }
    }
    /**
     * Constructeur par défaut
     * @param p objet de base
     * @param r contexte HTTP
     * @param nombreligne nombre de ligne si valeur multiple
     * @param tabIndexe
     */
    public Page(ClassMAPTable p, HttpServletRequest r, int nombreligne, String[] tabIndexe) {
        setBase(p);
        setReq(r);
        setNombreLigne(nombreligne);
        setTabIndexe(tabIndexe);
    }
}
