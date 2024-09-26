/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;
/**
 * @deprecated
 * Utilisé pour gérer les utilisateurs Back-office.
 * Utilisez {@link historique.MapUtilisateur} 
 * pour la gestion des utilisateurs.
 * 
 * 
 * @author BICI
 */
public class Utilisateur extends ClassMAPTable{

    private String refuser;
    private String loginuser ;
    private String pwduser;
    private String idrole;
    private String nomuser;
    private String teluser;
    /**
     * Constructeur par défaut
     */
    public Utilisateur() {
        super.setNomTable("utilisateur");
    }
    /**
     * 
     * @param loginuser nom unique utilisé en login
     * @param pwduser mot de passe
     * @param idrole id du role de l'utilisateur
     * @param nomuser nom de l'utilisateur
     * @param teluser télephone contact de l'utilisateur
     */
    public Utilisateur(String loginuser, String pwduser, String idrole, String nomuser, String teluser) {
        this.setLoginuser(loginuser);
        this.setPwduser(pwduser);
        this.setIdrole(idrole);
        this.setNomuser(nomuser);
        this.setTeluser(teluser);
    }
    /**
     * 
     * @return id de l'utilisateur
     */
    public String getRefuser() {
        return refuser;
    }

    public void setRefuser(String refuser) {
        this.refuser = refuser;
    }
    /**
     * 
     * @return nom d'utilisateur de connexion
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
    public String getIdRole() {
        return idrole;
    }

    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }
    /**
     * 
     * @return nom courant de l'utilisateur
     */
    public String getNomuser() {
        return nomuser;
    }

    public void setNomuser(String nomuser) {
        this.nomuser = nomuser;
    }
    /**
     * 
     * @return contact de l'utilisateur
     */
    public String getTeluser() {
        return teluser;
    }
    /**
     * 
     * @param teluser contact de l'utilisateur
     */
    public void setTeluser(String teluser) {
        this.teluser = teluser;
    }
    
    @Override
    public String getTuppleID() {
        return this.refuser;
    }

    @Override
    public String getAttributIDName() {
        return "refuser"  ; 
    }
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("user_bo");
        this.preparePk("USER", "getSeqUtilisateur");
        this.setRefuser(makePK(c));
    }
    
}
