/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import bean.ClassMAPTable;
import java.lang.reflect.Field;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;

/**
 * Objet à utiliser à l'insertion d'un tableau pour génerer la page de saisie d'un objet de mapping donné.
 * Ci-dessous une exemple de comment créer une page de saisie. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 *  
 *  int nombreLigne = 2;
 *  String autreparsley = "data-parsley-range='[8, 40]' required";
 *  UserEJB u = (user.UserEJB) session.getValue("u");
 *  Directionregionale  pj = new Directionregionale();
 *  pj.setNomTable("sig_dr");
 *  PageInsertTableau pi = new PageInsertTableau(pj, request, u, nombreLigne);
 * 
 *  pi.preparerDataFormu();
 * 
 * }
 * 
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * <pre>
 * {@code 
 *  <%
 * pi.getFormu().makeHtmlInsertTableauIndex();
 * out.println(pi.getFormu().getHtmlTableauInsert());
 *  %>
 * 
 * }
 * @author BICI
 */
public class PageInsertTableau extends PageInsert {

    private int nombreLigne = 0;

    public int getNombreLigne() {
        return nombreLigne;
    }

   /**
    * Donner nombre de ligne à afficher  
    * @param nombreLigne : nombre de ligne à afficher 
    */

    public void setNombreLigne(int nombreLigne) {
        if (nombreLigne <= 0) {
            this.nombreLigne = 10;
        }
        this.nombreLigne = nombreLigne;
    }

    /**
     * Constructeur qui prenne en paramètre : 
     * @param o classe mapping 
     * @param req contexte HTTP de la page
     * @param u session de l'utilisateur courant
     * @param nbLigne nombre de ligne à afficher 
     * @throws Exception
     */

    public PageInsertTableau(ClassMAPTable o, HttpServletRequest req, user.UserEJB u, int nbLigne) throws Exception {
        setBase(o);
        setReq(req);
        setUtilisateur(u);
        setNombreLigne(nbLigne);
        makeFormulaire();

    }

    /**
     * Constructeur qui prenne en paramètre : 
     * @param o classe mapping
     * @param req contexte HTTP de la page
     * @param nombreLigne nombre de ligne
     * @throws Exception
     */
    public PageInsertTableau(ClassMAPTable o, HttpServletRequest req, int nombreLigne) throws Exception {
        setBase(o);
        setReq(req);
        setNombreLigne(nombreLigne);
    }

    /**
     * creation du formulaire 
     * qui comprend comme champs les intersections des listes d'attribut de l'objet de mapping
     * et les listes des colonnes dans la base 
     * @throws Exception
     */
    public void makeFormulaire() throws Exception {
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
        formu.setNbLigne(getNombreLigne());
    }

    /**
     *
     * Définir les valeurs des attribut de l'objet de base de la base à partir du contexte HTTP
     * @return tableau objet de base de la page
     */
    public ClassMAPTable[] getObjectAvecValeurTableau() throws Exception {
        //Field[]tempChamp=getBase().getFieldList();
        int nombreLigne = getNombreLigne();
        ClassMAPTable[] liste = new ClassMAPTable[nombreLigne];
        try {
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getBase());
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
}
