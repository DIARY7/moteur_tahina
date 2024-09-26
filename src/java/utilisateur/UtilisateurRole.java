package utilisateur;

import bean.ClassMAPTable;
import historique.UtilisateurUtil;
/**
 * Classe représentant la jointure entre la table utilisateur et la table role.
 * Cette classe permet d'accèder rapidement aux informations de rang d'un utilisateur.
 * 
 * 
 * @author BICI
 */
public class UtilisateurRole extends ClassMAPTable{

    private int rang;
    private int refuser;
    private String loginuser ;
    private String pwduser;
    private String direction;
    private String service;
    private String codeEtablissement;
    /**
     * 
     * @return direction de l'utilisateur
     */
    public String getDirection() {
        return direction;
    }
    /**
     * 
     * @param direction direction de l'utilisateur
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
    /**
     * 
     * @return code de l'etablissement de l'utilisateur
     */
    public String getCodeEtablissement() {
        return codeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }
    /**
     * 
     * @return service de l'utilisateur
     */
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    /**
     * 
     * @return id de l'utilisateur concerné
     */
    public int getRefuser() {
        return refuser;
    }

    public void setRefuser(int refuser) {
        this.refuser = refuser;
    }
    /**
     * 
     * @return nom d'utilisateur 
     */
    public String getLoginuser() {
        return loginuser;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }
    /**
     * 
     * @return mot de passe crypté de l'utilisateur
     */
    public String getPwduser() {
        return pwduser;
    }

    public void setPwduser(String pwduser) {
        this.pwduser = pwduser;
    }
    /**
     * 
     * @return role associé à l'utilisateur
     */
    public String getIdrole() {
        return idrole;
    }

    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }
    /**
     * 
     * @return nom de l'utilisateur
     */
    public String getNomuser() {
        return nomuser;
    }

    public void setNomuser(String nomuser) {
        this.nomuser = nomuser;
    }
    /**
     * 
     * @return contact telephonique de l'utilisateur
     */
    public String getTeluser() {
        return teluser;
    }

    public void setTeluser(String teluser) {
        this.teluser = teluser;
    }
    private String idrole;
    private String nomuser;
    private String teluser;
    private String adruser;

    public String getAdruser() {
        return adruser;
    }

    public void setAdruser(String adruser) {
        this.adruser = adruser;
    }

    public UtilisateurRole() {
        super.setNomTable("utilisateurrole");
    }
    /**
     * 
     * @return rang de l'utilisateur, selon ce rang des actions seront restreintes
     */
    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    @Override
    public String getTuppleID() {
        return refuser+"";
    }

    @Override
    public String getAttributIDName() {
        return "refuser";
    }
    
}
