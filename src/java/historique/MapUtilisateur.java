/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

import utilitaire.Utilitaire;

/**
 * Objet représentant un utilisateur avec ses informations de login et personnel
 *
 * @author MSI
 */

public class MapUtilisateur extends bean.ClassMAPTable implements java.io.Serializable {

    private String loginuser;
    private String pwduser;
    private String idrole;
    private String nomuser;
    private String adruser;
    private String teluser;
    private int refuser;
    private int rang;

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }
    /**
     * Constructeur par défaut
     */
    public MapUtilisateur() {
        //setIndicePk("US");
        setNomTable("utilisateur");
        //idrole="admin";
    }

    public MapUtilisateur(int refus, String loginus, String pwdus, String nomus, String adrus, String telus, String idrol) throws Exception{
        this.setRefuser(refus);
        this.setLoginuser(loginus);
        this.setPwduser(pwdus);
        this.setNomuser(nomus);
        this.setAdruser(adrus);
        this.setTeluser(telus);
        this.setIdrole(idrol);
        super.setNomTable("utilisateur");
    }

    public MapUtilisateur(String refus, String loginus, String pwdus, String nomus, String adrus, String telus, String idrol) throws Exception{
        this.setRefuser(Integer.valueOf(refus));
        this.setLoginuser(loginus);
        this.setPwduser(pwdus);
        this.setNomuser(nomus);
        this.setAdruser(adrus);
        this.setTeluser(telus);
        this.setIdrole(idrol);
        super.setNomTable("utilisateur");
    }

    public MapUtilisateur(String loginus, String pwdus, String nomus, String adrus, String telus, String idrol) throws Exception{
        super.setNomTable("utilisateur");
        this.setRefuser(Utilitaire.getMaxSeq("getsequtilisateur"));
        this.setLoginuser(loginus);
        this.setPwduser(pwdus);
        this.setNomuser(nomus);
        this.setAdruser(adrus);
        this.setTeluser(telus);
        this.setIdrole(idrol);
        super.setNomTable("utilisateur");
    }

    public String getAttributIDName() {
        return "refuser";
    }

    public String getTuppleID() {
        return String.valueOf(refuser);
    }

    public MapUtilisateur(String loginuser, String pwduser) {
        this.loginuser = loginuser;
        this.pwduser = pwduser;
        setNomTable("utilisateur");
        super.setNombreChamp(7);
    }

    public MapRoles getRole() {
        //RoleUtil rU=new RoleUtil();
        //MapRoles a=rU.rechercheById(idrole);
        return null;
    }
    /**
     * Nom de l'action à réaliser vis-à-vis de l'activation désactivation de l'utilisateur
     * @return "Activer" si utilisateur désactivé sinon "Désactiver"
     */
    public String getEtat() {
        AnnulationUtilisateurUtil au = new AnnulationUtilisateurUtil();
        if (au.rechercher(2, this.getTuppleID()).length > 0)//si lutilisateur est desactive
        {
            return "activer";
        } else {
            return "desactiver"; //si l'utilisateur est active
        }
    }
    /**
     * Verifier si l'utilisateur est un super utilisateur
     * @return
     */
    public boolean isSuperUser() {
        if (getIdrole().compareToIgnoreCase("dg") == 0 || getIdrole().compareToIgnoreCase("controle") == 0) {
            return true;
        }
        return false;
    }
    /**
     *
     * @return nom avec lequel il devrait se logger
     */
    public String getLoginuser() {
        return loginuser;
    }
    
    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }
    /**
     * 
     * @return  mot de passe de l'utilisateur
     */
    public String getPwduser() {
        return pwduser;
    }
    /**
     * 
     * @param pwduser mot de passe à mettre à jour
     * @throws Exception si l'objet est en état mis à jour alors que le mot de passe est vide
     */
    public void setPwduser(String pwduser) throws Exception {
        if (this.getMode().equals("modif")) {
            if(pwduser == null || pwduser.compareToIgnoreCase("") == 0){
                throw new Exception("Mots de passe invalide");
            }
        }
        this.pwduser = pwduser;
    }
    /**
     * Role associé à un utilisateur
     * @return identifiant du role de l'utilisateur
     */
    public String getIdrole() {
        return idrole;
    }

    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }
    /**
     * 
     * @return nom complet de l'utilisateur
     */
    public String getNomuser() {
        return nomuser;
    }

    public void setNomuser(String nomuser) {
        this.nomuser = nomuser;
    }
    /**
     * 
     * @return adresse de l'utilisateur
     */
    public String getAdruser() {
        return adruser;
    }

    public void setAdruser(String adruser) {
        this.adruser = adruser;
    }
    /**
     * 
     * @return télephone de l'utilisateur
     */
    public String getTeluser() {
        return teluser;
    }

    public void setTeluser(String teluser) {
        this.teluser = teluser;
    }
    /**
     * 
     * @return identifiant de l'utilisateur
     */
    public int getRefuser() {
        return refuser;
    }

    public void setRefuser(int refuser) {
        this.refuser = refuser;
    }

    public void setRefuser(String refuser) {
        this.refuser = Integer.valueOf(refuser);
    }
}
