package affichage;

import java.sql.Connection;
import java.lang.reflect.Field;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import utilitaire.Utilitaire;

/**
 * Objet à utiliser à l'insertion multiple pour génerer la page de saisie d'un objet de mapping donné.
 * Ci-dessous une exemple de comment créer une page de saisie multipli. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 * 
 *  AvoirFC mere = new AvoirFC(); 
 *  AvoirFCFilleLib fille = new AvoirFCFilleLib();
 *  AvoirFCFilleLib[] liste = (AvoirFCFilleLib[])CGenUtil.rechercher(fille, null, null, "");
 *  fille.setIdmere(request.getParameter("id"));
 * 
 *  
 *  PageUpdateMultiple pi = new PageUpdateMultiple(mere, fille, liste, request, (user.UserEJB) session.getValue("u"), 2);
 *  pi.setLien((String) session.getValue("lien"));
 * 
 *  pi.preparerDataFormu();
 * 
 * }
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * <pre>
 * {@code 
 *  <%
 *  pi.getFormu().makeHtmlInsertTabIndex();
 *  pi.getFormufle().makeHtmlInsertTableauIndex();
 *  out.println(pi.getFormu().getHtmlInsert());
 *  %>
 * 
 * }
 * 
 * @version 1.0
 */
public class PageUpdateMultiple extends PageInsert {

    private bean.ClassMAPTable critere;
    Formulaire formufle;
    private ClassMAPTable fille;
    private int nombreLigne = 0;
    private int tailleFille = 0;
    private String[] tabIndice;

    /**
     * Constructeur 
     * @param bse Classe mapping mère
     * @param fle Classe mapping fille 
     * @param req contexte HTTP de la page
     * @param nbLine nombre de ligne 
     * @param tabIndice liste des indices
     * @throws Exception
     */
    public PageUpdateMultiple(ClassMAPTable bse, ClassMAPTable fle, HttpServletRequest req, int nbLine, String[] tabIndice) throws Exception {
        setBase(bse);
        setFille(fle);
        setReq(req);
        setNombreLigne(nbLine);
        setTabIndice(tabIndice);
    }

    /**
     * Constructeur 
     * @param o classe mapping 
     * @param fle classe mapping 
     * @param fille classe mapping 
     * @param req contexte HTTP de la page
     * @param u session de l'utilisateur courant
     * @param nombreLigne nombre de ligne
     * @throws Exception
     */
    public PageUpdateMultiple(ClassMAPTable o, ClassMAPTable fle, ClassMAPTable[] fille, HttpServletRequest req, user.UserEJB u, int nombreLigne) throws Exception {
        setCritere(o);
        setReq(req);
        setUtilisateur(u);
        makeCritere();
        getData();
        setFille(fle);
        setDataFille(fille);
        setTailleFille(fille.length);
        setNombreLigne(getTailleFille()+nombreLigne);
        makeFormulaire();
        
        
        if (getFormu().getChamp("id") != null) {
            getFormu().getChamp("id").setAutre("readonly");
        }
        formufle.setDataFille(fille);
        formufle.setEstFille(true);
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

    public void setDataFille(ClassMAPTable[] fille) throws Exception {
        setBaseTableau(fille);
    }
    
    public ClassMAPTable[] getDataFille() throws Exception
    {
        return getBaseTableau();
    }

    public void makeHtml() {
    }

    /**
     * Cette fonction permet de prendre la valeur le l'id passé en paramètre dans la requète HTTP 
     * et de donner cette valeur à l'objet mapping 
     * 
     * @throws Exception
     */
    public void makeCritere() throws Exception {
        String valeur = getReq().getParameter(getCritere().getAttributIDName());
        Field f = CGenUtil.getField(getCritere(), getCritere().getAttributIDName());
        CGenUtil.setValChamp(getCritere(), f, valeur);
    }

     /**
     * creation formulaire 
     * et donner les valeur respective à chaque liste champs par rapport à la valeur dans la base 
     */
    public void makeFormulaire() throws Exception {
        formu = new Formulaire();
        formufle = new Formulaire();
        affichage.Champ[] tbse = null;
        affichage.Champ[] tfle = null;

        bean.Champ[] champbse = bean.ListeColonneTable.getFromListe(getBase(), null);
        bean.Champ[] champfle = bean.ListeColonneTable.getFromListe(getFille(), null);

        tbse = new Champ[champbse.length - 1];
        tfle = new Champ[(champfle.length)*getNombreLigne()];

        int nbElement = 0;
        for (int i = 0; i < tbse.length + 1; i++) {
            tbse[nbElement] = new Champ(champbse[i].getNomColonne());
            tbse[nbElement].setLibelle(champbse[i].getNomColonne());
            if (champbse[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName()) != 0) {
                if (champbse[i].getTypeJava().compareToIgnoreCase("double") == 0) {
                    double val = (double) CGenUtil.getValeurFieldByMethod(getBase(), champbse[i].getNomColonne());
                    tbse[nbElement].setValeur(Utilitaire.doubleWithoutExponential(val));
                } else if (champbse[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {
                    Object ret = (CGenUtil.getValeurFieldByMethod(getBase(), champbse[i].getNomColonne()));
                    String valDate = null;
                    if (ret != null) {
                        valDate = ret.toString();
                    }
                    tbse[nbElement] = new ChampDate(champbse[i].getNomColonne(), "UPDATE");
                    tbse[nbElement].setLibelle(champbse[i].getNomColonne());
                    tbse[nbElement].setValeur(valDate);
                } else {
                    tbse[nbElement].setValeur(String.valueOf(CGenUtil.getValeurFieldByMethod(getBase(), champbse[i].getNomColonne())));
                }
                nbElement++;
            }
        }
        formu.setListeChamp(tbse);
        formu.setObjet(critere);

        int nbElementfle = 0;
        
        for(int ligne=0; ligne<getNombreLigne() ; ligne++){
            for (int i = 0; i < champfle.length; i++) {
                tfle[nbElementfle] = new Champ(champfle[i].getNomColonne()+"_"+ligne);
                tfle[nbElementfle].setLibelle(champfle[i].getNomColonne()+"_"+ligne);
                if (champfle[i].getTypeJava().compareToIgnoreCase("double") == 0) {
                    if(ligne < getTailleFille()){
                        double val = (double) CGenUtil.getValeurFieldByMethod(getBaseTableau()[ligne], champfle[i].getNomColonne());
                        tfle[nbElementfle].setValeur(Utilitaire.doubleWithoutExponential(val));
                    }else{
                        tfle[nbElementfle] = new ChampCalcul(champfle[i].getNomColonne()+"_"+ligne);
                    }
                } else if (champfle[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {
                    if(ligne < getTailleFille()){
                        Object ret = (CGenUtil.getValeurFieldByMethod(getBaseTableau()[ligne], champfle[i].getNomColonne()));
                        String valDate = null;
                        if (ret != null) {
                            valDate = ret.toString();
                        }
                        tfle[nbElementfle] = new ChampDate(champfle[i].getNomColonne()+"_"+ligne, "UPDATE");
                        tfle[nbElementfle].setLibelle(champfle[i].getNomColonne());
                        tfle[nbElementfle].setValeur(valDate);
                    }else{
                        tfle[nbElementfle] = new ChampDate(champfle[i].getNomColonne()+"_"+ligne);
                    }
                } else {
                    if(ligne < getTailleFille()){
                        tfle[nbElementfle].setValeur(String.valueOf(CGenUtil.getValeurFieldByMethod(getBaseTableau()[ligne], champfle[i].getNomColonne())));
                        
                    }/*else{
                        tfle[nbElementfle].setValeur("");
                    }*/
                }
                nbElementfle++;
            }
        }
        formufle.setListeChamp(tfle);
        formufle.setObjet(getFille());
        formufle.setNbLigne(getNombreLigne());
    }

    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setCritere(bean.ClassMAPTable critere) {
        this.critere = critere;
    }

    public ClassMAPTable getFille() {
        return fille;
    }

    public void setFille(ClassMAPTable fille) {
        this.fille = fille;
    }

    public Formulaire getFormufle() {
        return formufle;
    }

    public void setFormufle(Formulaire formufle) {
        this.formufle = formufle;
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
            formufle.getAllDataFille(c);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw (ex);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public int getNombreLigne() {
        return nombreLigne;
    }

    public void setNombreLigne(int nombreLigne) {
        this.nombreLigne = nombreLigne;
    }

    public int getTailleFille() {
        return tailleFille;
    }

    public void setTailleFille(int tailleFille) {
        this.tailleFille = tailleFille;
    }
    
    public String[] getTabIndice() {
        return tabIndice;
    }

    public void setTabIndice(String[] tabIndice) {
        this.tabIndice = tabIndice;
    }

    /**
     * Définir les valeurs des attribut de l'objet fille de base de la base à partir du contexte HTTP
     * @return l'objet de base de la page
     * @throws Exception
     */

    public ClassMAPTable[] getObjectFilleAvecValeur() throws Exception {
        int nombreLigne = getNombreLigne();
        //String[] tIndice = getTabIndice();
        String[] tIndice=this.getReq().getParameterValues("id");
        if(tIndice == null||tIndice.length<=1)
            throw new Exception("Vous devez cocher");
        ClassMAPTable[] liste = new ClassMAPTable[tIndice.length];
        try {
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getFille());
            int x=0;
            for (int iLigne = 0; iLigne < nombreLigne; iLigne++) {
                ClassMAPTable ex =  (ClassMAPTable)Class.forName(getFille().getClassName()).newInstance();
                for(int indice=0; indice<tIndice.length; indice++){
                    String ligne = ""+iLigne;
                    if(ligne.equals(tIndice[indice])){
                        for (int i = 0; i < tempChamp.length; i++) {
                            Field f = tempChamp[i];
                            String nomChamp = f.getName()+"_"+iLigne;
                            String valeur = getParamSansNull(nomChamp);
                            if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, valeur);
                            }
                            if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                                //System.out.println("La date en sortie = "+utilitaire.Utilitaire.string_date("dd/MM/yyyy",valeur));
                                bean.CGenUtil.setValChamp(ex, f, utilitaire.Utilitaire.string_date("dd/MM/yyyy", valeur));
                            }
                            if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));
                            }
                            if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                                if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                                    bean.CGenUtil.setValChamp(ex, f, new Integer(0));
                                } else {
                                    bean.CGenUtil.setValChamp(ex, f, new Integer(valeur));
                                }
                            }
                            if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, new Float(utilitaire.Utilitaire.stringToFloat(valeur)));
                            }
                            if(i==0) x++;
                        }
                        liste[indice]  = ex;
                    }
                }
            }
            ClassMAPTable[] ret = new ClassMAPTable[x]; 
			
            int countOk = 0; 
            for(int j = 0; j<tIndice.length; j++){
                if (liste[j] != null){
                    ret[countOk] = liste[j];
                    countOk ++;
                }
            }
            return ret;
        } catch (NumberFormatException n) {
            n.printStackTrace();
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Définir les valeurs des attribut de l'objet fille de base de la base à partir du contexte HTTP
     * @return l'objet de base de la page
     * @throws Exception
     */

    public ClassMAPTable[] getObjectFilleAvecValeurSansControle() throws Exception {
        int nombreLigne = getNombreLigne();
        //String[] tIndice = getTabIndice();
        String[] tIndice=this.getReq().getParameterValues("id");
        ClassMAPTable[] liste = new ClassMAPTable[tIndice.length];
        try {
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getFille());
            int x=0;
            for (int iLigne = 0; iLigne < nombreLigne; iLigne++) {
                ClassMAPTable ex =  (ClassMAPTable)Class.forName(getFille().getClassName()).newInstance();
                for(int indice=0; indice<tIndice.length; indice++){
                    String ligne = ""+iLigne;
                    if(ligne.equals(tIndice[indice])){
                        for (int i = 0; i < tempChamp.length; i++) {
                            Field f = tempChamp[i];
                            String nomChamp = f.getName()+"_"+iLigne;
                            String valeur = getParamSansNull(nomChamp);
                            if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, valeur);
                            }
                            if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                                //System.out.println("La date en sortie = "+utilitaire.Utilitaire.string_date("dd/MM/yyyy",valeur));
                                bean.CGenUtil.setValChamp(ex, f, utilitaire.Utilitaire.string_date("dd/MM/yyyy", valeur));
                            }
                            if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));
                            }
                            if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                                if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                                    bean.CGenUtil.setValChamp(ex, f, new Integer(0));
                                } else {
                                    bean.CGenUtil.setValChamp(ex, f, new Integer(valeur));
                                }
                            }
                            if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                                bean.CGenUtil.setValChamp(ex, f, new Float(utilitaire.Utilitaire.stringToFloat(valeur)));
                            }
                            if(i==0) x++;
                        }
                        liste[indice]  = ex;
                    }
                }
            }
            ClassMAPTable[] ret = new ClassMAPTable[x]; 
			
            int countOk = 0; 
            for(int j = 0; j<tIndice.length; j++){
                if (liste[j] != null){
                    ret[countOk] = liste[j];
                    countOk ++;
                }
            }
            return ret;
        } catch (NumberFormatException n) {
            n.printStackTrace();
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
