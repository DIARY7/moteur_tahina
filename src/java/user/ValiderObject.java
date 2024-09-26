/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import bean.CGenUtil;
import bean.ClassEtat;
import bean.ClassMAPTable;

import historique.MapUtilisateur;
import java.sql.Connection;
import constante.ConstanteEtat;
import mg.cnaps.configuration.Configuration;

/**
 * Utilitaire pour uniformiser la validation des objets (Visa)
 * 
 * @author BICI
 */
public class ValiderObject {
    /**
     * Valider un objet : changé l'etat d'un objet en visé avec les controles divers
     * @param c connexion ouverte à la base de données
     * @param o objet à valider
     * @param user session de l'utilisateur 
     * @param u utilisateur courant
     * @param listeConfig liste de configuration
     * @return objet après validation
     * @throws Exception
     */
    public static Object validerObject(Connection c, ClassMAPTable o, UserEJBBean user, MapUtilisateur u, Configuration[] listeConfig) throws Exception {
        try {
            o.setMode("modif");
            ClassEtat []lo=null;
            if (o instanceof ClassEtat)
            {
                ClassEtat filtre=(ClassEtat)o.getClass().newInstance();
                filtre.setNomTable(o.getNomTable());
                
                lo=(ClassEtat [])CGenUtil.rechercher(filtre,null,null,c," and "+o.getAttributIDName()+"='"+o.getTuppleID()+"'");
                if(lo.length==0)throw new Exception("objet non existante dans "+filtre.getNomTable());
                if (lo[0].getEtat()>=ConstanteEtat.getEtatValider())throw new Exception("objet deja vise");
                o=lo[0];
              
            }
            if(o instanceof ClassEtat)
            {
                ((ClassEtat) o).viser(user, c);
            }
            else
            {
                o.setMode("modif");
                o.controler(c);
                user.updateEtat(o, ConstanteEtat.getEtatValider(), o.getValInsert(o.getAttributIDName()), c);
            }
            
            return o;
            
        } catch (Exception e) {
            throw e;
        }
    }
}
