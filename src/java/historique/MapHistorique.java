 /* 
  * package regroupant les utilitaires pour garder les historiques sur les actions
  */
package historique;

import java.sql.Connection;

/**
 * Objet pour garder les historiques de modifications des objets en base de données
 * 
 * 
 * @author BICI
 */

public class MapHistorique extends bean.ClassMAPTable implements java.io.Serializable {

    /**
     * clé primaire de l'historique
     */
    private String idHistorique;
    /**
     * Date de la modification/insertion
     */
    private java.sql.Date dateHistorique;
    /**
     * Heure de la modification/insertion
     */
    private String heure;
    /**
     * Classe/Nom de l'objet modifié/inséré
     */
    private String objet;
    /**
     * Action réalisée : insert, update, delete
     */
    private String action;
    /**
     * Utilisateur ayant fait la modification/insertion
     */
    private String idUtilisateur;
    /**
     * Identifiant de l'objet insérer
     */
    private String refObjet;

    
    
    public MapHistorique() {
        super.setNomTable("historique");
    }

    public MapHistorique(String refe, java.sql.Date date, String lera, String refOrd, String action, String user, String refObje) {
        super.setNomTable("historique");
        this.setIdHistorique(refe);
        this.setDateHistorique(date);
        this.setHeure(lera);
        this.setObjet(refOrd);
        this.setAction(action);
        this.setIdUtilisateur(user);
        this.setRefObjet(refObje);
    }
    public MapHistorique(String obje, String action, String user, String refObje) {
        this.setNomTable("historique");
        setNomProcedureSequence("getSeqHistorique");
        this.setLonguerClePrimaire(10);
        this.setIdHistorique(makePK());
        this.setDateHistorique(utilitaire.Utilitaire.dateDuJourSql());
        this.setHeure(utilitaire.Utilitaire.heureCourante());
        this.setAction(action);
        this.setIdUtilisateur(user);
        this.setRefObjet(refObje);
        this.setObjet(objet);
       
    }
    public MapHistorique(String obje, String action, String user, String refObje, Connection c) throws Exception {
        this.setNomTable("historique");
        setNomProcedureSequence("getSeqHistorique");
        this.setLonguerClePrimaire(10);
        this.setIdHistorique(makePK(c));
        this.setDateHistorique(utilitaire.Utilitaire.dateDuJourSql());
        this.setHeure(utilitaire.Utilitaire.heureCourante());
        this.setAction(action);
        this.setIdUtilisateur(user);
        this.setRefObjet(refObje);
        this.setObjet(objet);
       
    }

    public String getTuppleID() {
        return String.valueOf(getIdHistorique());
    }

    /**
     * Implementation de la methode qui doit donner le nom du champ de la cle
     * primaire (tjrs pour update)
     */
    public String getAttributIDName() {
        return "idHistorique";
    }

    

    public MapUtilisateur getUtilisateurs() {
        return (MapUtilisateur) new UtilisateurUtil().rechercher(1, this.getIdUtilisateur())[0];
    }

    public void setDateHistorique(java.sql.Date dateHistorique) {
        if (String.valueOf(dateHistorique).compareTo("") == 0 || dateHistorique == null) {
            this.dateHistorique = utilitaire.Utilitaire.dateDuJourSql();
        } else {
            this.dateHistorique = dateHistorique;
        }
    }

    public String getObjet() {
        return objet;
    }

   

    public MapUtilisateur getUtilisateur() {
        return (MapUtilisateur) new UtilisateurUtil().rechercher(1, this.getIdUtilisateur())[0];
    }

    public String getIdHistorique() {
        return idHistorique;
    }

    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }
    /**
     * 
     * @return heure de la modification/insertion
     */
    public String getHeure() {
        return heure;
    }
    /**
     * setter de heure
     * @param heure
     */
    public void setHeure(String heure) {
        this.heure = heure;
    }
    /**
     * 
     * @return action réalisée sur l'objet : insert, update, delete
     */
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    /**
     * 
     * @return id de l'utilisateur ayant réalisé la modification
     */
    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * 
     * @return identifiant de l'objet modifié
     */
    public String getRefObjet() {
        return refObjet;
    }

    public void setRefObjet(String refObjet) {
        this.refObjet = refObjet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    /**
     * @return date de la modification/insertion
     */
    public java.sql.Date getDateHistorique() {
        return dateHistorique;
    }
}
