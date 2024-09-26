package utilisateur;

import bean.ClassMAPTable;

/**
 * Classe representant la restriction sur une table avec les restrictions directes
 * sur les actions.
 * @author BICI
 */
public class RestrictionLibelle extends ClassMAPTable {

    private String idrole;
    private String tablename;
    private String ajout;
    private String modif;
    private String suppr;
    private String read;
    private String idaction;
    private String iddirection;

    public RestrictionLibelle(String idrole, String tablename, String ajout, String modif, String suppr, String read, String idaction, String iddirection) {
        this.setIdrole(idrole);
        this.setTablename(tablename);
        this.setAjout(ajout);
        this.setModif(modif);
        this.setSuppr(suppr);
        this.setRead(read);
        this.setIdaction(idaction);
        this.setIddirection(iddirection);
        super.setNomTable("RESTRICTION_LIBELLE");
    }

    public String getIddirection() {
        return iddirection;
    }

    public void setIddirection(String iddirection) {
        this.iddirection = iddirection;
    }
    /**
     * constructeur par défaut
     */
    public RestrictionLibelle() {
        super.setNomTable("RESTRICTION_LIBELLE");
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
     * @return nom de la table 
     */
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    /**
     * 
     * @return situation sur ajout 0 ou 1
     */
    public String getAjout() {
        return ajout;
    }

    public void setAjout(String ajout) {
        this.ajout = ajout;
    }
    /**
     * 
     * @return situation sur modifier 0 ou 1
     */
    public String getModif() {
        return modif;
    }

    public void setModif(String modif) {
        this.modif = modif;
    }
    /**
     * 
     * @return situation sur supprimer 0 ou 1
     */
    public String getSuppr() {
        return suppr;
    }

    public void setSuppr(String suppr) {
        this.suppr = suppr;
    }
    /**
     * 
     * @return situawtion sur lecture 0 ou 1
     */
    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    @Override
    public String getTuppleID() {
        return this.getIdrole();
    }

    @Override
    public String getAttributIDName() {
        return "idrole";
    }
    /**
     * 
     * @return id de l'action
     */
    public String getIdaction() {
        return idaction;
    }

    public void setIdaction(String idaction) {
        this.idaction = idaction;
    }

}
