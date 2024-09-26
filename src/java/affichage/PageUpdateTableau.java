package affichage;

import java.sql.Connection;
import java.lang.reflect.Field;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.sql.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import utilitaire.Utilitaire;

/**
  * Objet à utiliser à l'update d'un tableau pour génerer la page de modification d'un objet de mapping donné.
 * Ci-dessous une exemple de comment créer une page de modification. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 *  
 * Directionregionale[] liste = (Directionregionale[])CGenUtil.rechercher(pj, null, null, "");
 * PageUpdateTableau pi = new PageUpdateTableau(pj, request, u, liste);
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
 * @version 1.0
 */
public class PageUpdateTableau extends PageInsert {

    private bean.ClassMAPTable critere;

    /**

     * @param o classe mapping 
     * @param o classe mapping
     * @param req  contexte HTTP de la page
     * @param u session de l'utilisateur courant
     * @param id liste des objets à mettre dans le tableau
     * @throws Exception
     */

    public PageUpdateTableau(ClassMAPTable o, HttpServletRequest req, user.UserEJB u, ClassMAPTable[] id) throws Exception {
        setCritere(o);
        setReq(req);
        setUtilisateur(u);
        makeCritere();
        getData(id);
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
    public void getData(ClassMAPTable[] id) throws Exception {
        if (id == null || id.length == 0) {
            throw new Exception("Pas de resultat pour votre consultation");
        }
        setNombreLigne(id.length);
        setBase(id[0]);
        setBaseTableau(id);
    }

    /**
     * fonction vide 
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
        affichage.Champ[][] t = null;
        bean.Champ[] f = bean.ListeColonneTable.getFromListe(getBase(), null);
        // for(int i=)
        t = new Champ[getBaseTableau().length][f.length];
        for(int j = 0 ; j<getBaseTableau().length ; j++){
            ClassMAPTable liste = getBaseTableau()[j];
        for (int i = 0; i < f.length; i++) {
            //System.out.println(" type java ============== " + f[i].getTypeJava());
            t[j][i] = new Champ(f[i].getNomColonne());
            t[j][i].setLibelle(f[i].getNomColonne());
            if (f[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName()) == 0) {
                //t[i].setAutre("readonly='readonly'");
                t[j][i].setType("hidden");
            }
            if(f[i].getNomColonne().compareToIgnoreCase(getBase().getAttributIDName())==0) t[j][i].setType("hidden");
            if (f[i].getTypeJava().compareToIgnoreCase("java.sql.Date") == 0) {//si le champ est de type date one le met de la forme dd/mm/yyyy
                Object ret = (CGenUtil.getValeurFieldByMethod(getBaseTableau()[j], f[i].getNomColonne()));
                String valDate = null; if(ret!=null) valDate = ret.toString();
                //Date daty = Utilitaire.string_date("dd/MM/yyyy", valDate);
                //System.out.println(" valeur ========== " + daty);
                //String val = Utilitaire.formatterDaty(daty);
                t[j][i] = new ChampDate(f[i].getNomColonne(),"UPDATE");
                t[j][i].setLibelle(f[i].getNomColonne());
                //System.out.println(" ========== " + val);
                t[j][i].setValeur(valDate);
            }
            if (f[i].getTypeJava().compareToIgnoreCase("double") == 0){
                double val = (double)CGenUtil.getValeurFieldByMethod(getBaseTableau()[j], f[i].getNomColonne());
                t[j][i].setValeur(Utilitaire.doubleWithoutExponential(val));
            }
            else
            {
                t[j][i].setValeur(String.valueOf(CGenUtil.getValeurFieldByMethod(getBaseTableau()[j],f[i].getNomColonne())));
            }
            formu.setListeChamp(t[j]);
            
      }
      }
      formu.setListeChampTableau(t);
      formu.setObjet(critere);
      formu.setNbLigne(getNombreLigne());
      //formu.getChamp(getBase().getAttributIDName()).setAutre("readonly='readonly'");
    }
    public bean.ClassMAPTable getCritere() {
        return critere;
    }

    public void setCritere(bean.ClassMAPTable critere) {
        this.critere = critere;
    }

}
