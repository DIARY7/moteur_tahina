package user;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.TypeObjet;
import historique.MapUtilisateur;
import java.sql.Connection;
import constante.ConstanteEtat;
import mg.cnaps.configuration.Configuration;
import utilitaire.Utilitaire;

/**
 * Utilitaire pour la création d'objets(insertion)
 * 
 * @author BICI
 */
public class CreateObject {
    /**
     * Insérer un objet de mapping
     * @param o objet à inserer
     * @param c connexion ouverte à la base de données
     * @param u utilisateur courant
     * @param user session de l'utilisateur
     * @param listeConfig liste des configurations 
     * @param listeSource liste des sources
     * @return l'objet après enregistrement
     * @throws Exception
     */
    public static Object createObject(ClassMAPTable o, Connection c, MapUtilisateur u, UserEJBBean user, Configuration[] listeConfig, TypeObjet[] listeSource) throws Exception {
        try {
             String refusers = u.getTuppleID();
            if (refusers != null && refusers.contains("/")) {
                String[] g = Utilitaire.split(refusers, "/");
                refusers = g[0];
            }
            //if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0)
            if (o instanceof bean.ClassEtat) {
                o.setValChamp("etat", Integer.valueOf(1));
                o.setValChamp("iduser", refusers);
            }
            if (o instanceof bean.ClassUser) {
                o.setValChamp("iduser", refusers);
            }
            return o.createObject(u.getTuppleID(), c);
            
            //if(c != null) c.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        //return o;
    }
    /**
     * Insérer un objet de mapping explicitement avec controler, création de PK et insertion avec histo
     * @param o objet à inserer
     * @param c connexion ouverte à la base de données
     * @param u utilisateur courant
     * @param user session de l'utilisateur
     * @param listeConfig liste des configurations 
     * @param listeSource liste des sources
     * @param action action
     * @return objet après enregistrement
     * @throws Exception
     */
    public static Object createObject(ClassMAPTable o, Connection c, MapUtilisateur u, UserEJBBean user, Configuration[] listeConfig, TypeObjet[] listeSource, String action) throws Exception {
        try {
            String objetNotif = "";
            String messageNotif = "";
            String idobjetNotif = "";
            String lienNotif = "";
            String refusers = u.getTuppleID();
            if (refusers != null && refusers.contains("/")) {
                String[] g = Utilitaire.split(refusers, "/");
                refusers = g[0];
            }
            if (o instanceof bean.ClassEtat) {
                o.setValChamp("etat", Integer.valueOf(1));
                o.setValChamp("iduser", refusers);
            }
            o.controler(c);
            o.construirePK(c);
            o.insertToTableWithHisto(u.getTuppleID(), c);
            
            if(c != null) c.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return o;
    }
    
}