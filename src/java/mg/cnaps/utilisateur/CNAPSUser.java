/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.cnaps.utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;
import utilitaire.Utilitaire;

/**
 *
 * @author Anthony
 */
public class CNAPSUser extends ClassMAPTable {

    private String id;
    private String code_dr;
    private String user_init;
    private String agent_mo;
    private String agent_disponible;
    private String username;
    private String code_service;
    private String id_utilisateur;

    public CNAPSUser(String code_dr, String user_init, String agent_mo, String agent_disponible, String username, String code_service, String id_utilisateur) {
        this.code_dr = code_dr;
        this.user_init = user_init;
        this.agent_mo = agent_mo;
        this.agent_disponible = agent_disponible;
        this.username = username;
        this.code_service = code_service;
        this.id_utilisateur = id_utilisateur;
        super.setNomTable("cnaps_user");
    }

    public CNAPSUser(String id1,String code_dr, String user_init, String agent_mo, String agent_disponible, String username, String code_service, String id_utilisateur) {
         super.setNomTable("cnaps_user");
        this.setId(id1);
        this.code_dr = code_dr;
        this.user_init = user_init;
        this.agent_mo = agent_mo;
        this.agent_disponible = agent_disponible;
        this.username = username;
        this.code_service = code_service;
        this.id_utilisateur = id_utilisateur;
    }

    public CNAPSUser() {
        super.setNomTable("cnaps_user");
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
       
        this.preparePk("CNP", "getSeqcnapsuser");
        this.setId(makePK(c));
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode_dr() {
        return Utilitaire.champNull(code_dr);
    }

    public void setCode_dr(String code_dr) {
        this.code_dr = code_dr;
    }

    public String getUser_init() {
        return Utilitaire.champNull(user_init);
    }

    public void setUser_init(String user_init) {
        this.user_init = user_init;
    }

    public String getAgent_mo() {
        return Utilitaire.champNull(agent_mo);
    }

    public void setAgent_mo(String agent_mo) {
        this.agent_mo = agent_mo;
    }

    public String getAgent_disponible() {
        return Utilitaire.champNull(agent_disponible);
    }

    public void setAgent_disponible(String agent_disponible) {
        this.agent_disponible = agent_disponible;
    }

    public String getUsername() {
        return Utilitaire.champNull(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode_service() {
        return Utilitaire.champNull(code_service);
    }

    public void setCode_service(String code_service) {
        this.code_service = code_service;
    }

    public String getId_utilisateur() {
        return Utilitaire.champNull(id_utilisateur);
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
    

}
