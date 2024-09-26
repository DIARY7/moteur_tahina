/*
 * To change user license header, choose License Headers in Project Properties.
 * To change user template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import bean.CGenUtil;
import bean.ClassMAPTable;
import historique.MapUtilisateur;
import historique.ParamCrypt;
import java.sql.Connection;
import java.sql.Date;
import constante.ConstanteEtat;
import utilitaire.Utilitaire;
/**
 * Utilitaire pour la mise à jour d'objets(update)
 * Uniformisation des mises à jour des objets peu importe la classe
 * 
 * 
 * @author BICI
 */
public class UpdateObject {

    /**
     * mettre à jour un objet de mapping
     * @param o  objet à mettre à jour
     * @param c connexion ouverte à la base de données
     * @param u utilisateur courant
     * @param user session de l'utilisateur courant
     * @return objet après mise à jour
     * @throws Exception
     */
    public static Object updateObject(ClassMAPTable o, Connection c, MapUtilisateur u, UserEJBBean user) throws Exception {
        try {
            String objetNotif = "";
            String messageNotif = "";
            String idobjetNotif = "";
            String lienNotif = "";
            if (o instanceof bean.ClassEtat) {
                o.setValChamp("iduser", u.getTuppleID());
            }

            if (o instanceof bean.ClassUser) {
                o.setValChamp("iduser", u.getTuppleID());
            }
            if(o instanceof historique.MapUtilisateur){
                MapUtilisateur util=(MapUtilisateur)o;
                ParamCrypt crt = new ParamCrypt();
                crt.setIdUtilisateur(String.valueOf(util.getRefuser()));
                ParamCrypt[] pc = (ParamCrypt[]) CGenUtil.rechercher(crt, null, null, c, "");
                if (pc.length == 0) {
                    throw new Exception("Pas de cryptage associe");
                }
                String passCrypt = Utilitaire.cryptWord(util.getPwduser(), pc[0].getNiveau(), pc[0].getCroissante());
                util.setPwduser(passCrypt);
                o = util;
            }
            o.controlerUpdate(c);
            o.updateToTableWithHisto(u.getTuppleID(), c);
           
            return o;
        } catch (Exception e) {
            throw e;
        }
    }
}
