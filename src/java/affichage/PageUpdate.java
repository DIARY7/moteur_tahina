package affichage;

import java.sql.Connection;
import java.lang.reflect.Field;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import utilitaire.Utilitaire;

/**
 * Objet à utiliser à l'update pour génerer la page de modification d'un objet de mapping donné.
 * Ci-dessous une exemple de comment créer une page de modification. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 * 
 *  Traite base = new Traite();
 *  base.setNomTable("TRAITE_LIB");
 *  PageUpdate pi = new PageUpdate(base, request, (user.UserEJB) session.getValue("u"));
 *  pi.preparerDataFormu();
 * 
 * }
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * <pre>
 * 
 * {@code 
 *  <%
    out.println(pi.getFormu().getHtmlInsert());
    %>
 * }
 * </pre>
 * @author BICI
 * @version 1.0
 */
public class PageUpdate extends PageInsert {

    private bean.ClassMAPTable critere;


    /**
     * constructeur 
     * @param o classe mapping
     * @param req  contexte HTTP de la page
     * @param u session de l'utilisateur courant
     * @throws Exception
     */

    public PageUpdate(ClassMAPTable o, HttpServletRequest req, user.UserEJB u) throws Exception {
        setCritere(o);
        setReq(req);
        setUtilisateur(u);
        makeCritere();
        getData();
        makeFormulaire();
        if (getFormu().getChamp("id") != null) {
            getFormu().getChamp("id").setAutre("readonly");
        }
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
     * @deprecated il y a rien dans la fonction
    */
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
        affichage.Champ[] t = null;
        bean.Champ[] f = bean.ListeColonneTable.getFromListe(getBase(), null);
        // for(int i=)
        t = new Champ[f.length];
        for (int i = 0; i < t.length; i++) {
            //System.out.println(" type java ============== " + f[i].getTypeJava());
            t[i] = new Champ(f[i].getNomColonne());
            t[i].setLibelle(f[i].getNomColonne());
            if (f[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName()) == 0) {
                //t[i].setAutre("readonly='readonly'");
                t[i].setType("hidden");
            }
            if(f[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName())==0) t[i].setType("hidden");
            if (f[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {//si le champ est de type date one le met de la forme dd/mm/yyyy
                Object ret = (CGenUtil.getValeurFieldByMethod(getBase(), f[i].getNomColonne()));
                String valDate = null; if(ret!=null) valDate = ret.toString();
                //Date daty = Utilitaire.string_date("dd/MM/yyyy", valDate);
                //System.out.println(" valeur ========== " + daty);
                //String val = Utilitaire.formatterDaty(daty);
                t[i] = new ChampDate(f[i].getNomColonne(),"UPDATE");
                t[i].setLibelle(f[i].getNomColonne());
                //System.out.println(" ========== " + val);
                t[i].setValeur(valDate);
            }
            if (f[i].getTypeJava().compareToIgnoreCase("double") == 0){
                double val = (double)CGenUtil.getValeurFieldByMethod(getBase(), f[i].getNomColonne());
                t[i].setValeur(Utilitaire.doubleWithoutExponential(val));
            }
            else
            {
                t[i].setValeur(String.valueOf(CGenUtil.getValeurFieldByMethod(getBase(),f[i].getNomColonne())));
            }
      }
      formu.setListeChamp(t);
      formu.setObjet(critere);
      //formu.getChamp(getBase().getAttributIDName()).setAutre("readonly='readonly'");
    }
    
    /**
     * 
     * @return objet de base avec valeurs des attributs pour faire une recherche
     */
    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setCritere(bean.ClassMAPTable critere) {
        this.critere = critere;
    }

}
