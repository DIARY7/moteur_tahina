
package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 * Classe représentant une restriction sur une action sur une table pour un role d'une direction donnée.
 * 
 * <p>
 * Par exemple, on veut restreindre l'action modifier pour le role "ROLE0001" sur la table "Table1" sur l'action
 * "ACT0001".
 * </p>
 * <pre>
 * //Connexion déjà ouverte quelque part de nom c
 * Restriction restriction = new Restriction("ROLE0001","ACT0001","Table1",null,"Description sur la restriction",null);
 * restriction.createObject(c);
 * </pre>
 * 
 * @author BICI
 */
public class Restriction extends ClassMAPTable {

    private String id;
    private String idrole;
    private String idaction;
    private String tablename;
    private String autorisation;
    private String description;
    private String iddirection;
    /**
     * 
     * @param idrole id du role concerné
     * @param idaction id de l'action concernée
     * @param tablename nom de la table de restriction
     * @param autorisation inutile comme ce sera toujours false
     * @param description description de la restriction
     * @param iddirection direction concernée
     */
    public Restriction(String idrole, String idaction, String tablename, String autorisation, String description, String iddirection) {
        this.setIdrole(idrole);
        this.setIdaction(idaction);
        this.setTablename(tablename);
        this.setAutorisation("false");
        this.setDescription(description);
        this.setIddirection(iddirection);
        super.setNomTable("restriction");
    }
    /**
     * 
     * @return id de la direction
     */
    public String getIddirection() {
        return iddirection;
    }
    /**
     * 
     * @param iddirection id de la direction
     */
    public void setIddirection(String iddirection) {
        this.iddirection = iddirection;
    }
    /**
     * 
     * @param idrole id role concerné
     * @param idpermission id de l'action à restreindre
     * @param nt  nom de la table de restriction
     */
    public Restriction(String idrole, String idpermission, String nt) {
        super.setNomTable("restriction");
        this.setIdrole(idrole);
        this.setIdaction(idpermission);
        this.setTablename(nt);
        this.setAutorisation("false");
        this.setDescription("");
    }
    /**
     * 
     * @param idrole id role concerné
     * @param idpermission id de l'action à restreindre
     * @param nt  nom de la table de restriction
     * @param direction id de la direction concernée
     */
    public Restriction(String idrole, String action, String nt, String direction) {
        super.setNomTable("restriction");
        this.setIdrole(idrole);
        this.setIdaction(action);
        this.setTablename(nt);
        this.setAutorisation("non");
           this.setIddirection(direction);
    }
    /**
     * Constructeur par défaut
     */
    public Restriction() {
        super.setNomTable("restriction");
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("restriction");
        this.preparePk("RES", "getSeqRestriction");
        this.setId(makePK(c));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 
     * @return id du role concerné
     */
    public String getIdrole() {
        return idrole;
    }

    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }
    /**
     * 
     * @return id de l'action à restreindre
     */
    public String getIdaction() {
        return idaction;
    }

    public void setIdaction(String idaction) {
        this.idaction = idaction;
    }
    /**
     * 
     * @return table de restriction
     */
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String idelement) {
        this.tablename = idelement;
    }
    /**
     * @deprecated
     * @return
     */
    public String getAutorisation() {
        return autorisation;
    }
    
    public void setAutorisation(String autorisation) {
        this.autorisation = autorisation;
    }
    /**
     * 
     * @return description de la restriction
     */
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
