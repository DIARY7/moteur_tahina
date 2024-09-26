/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 * Object representant les autorisations sur un menu.
 * La table utilisée est la table "USERMENU", le prefixe de l'ID est "MEN".
 * 
 * @author tahina
 */
public class UserMenu  extends ClassMAPTable {

    private String id;
    private String idrole;
    private String idmenu;
    private String codeservice;
    private String codedir;
    private int interdit;
    private String refuser;
    /**
     * 
     * @param idrole role possible
     * @param idmenu id du menu
     * @param codeservice code de service
     * @param codedir code de direction
     * @param interdit si interdit ou pas
     * @param refuser id de l'utilisateur
     */

    public UserMenu(String idrole, String idmenu, String codeservice, String codedir, int interdit, String refuser) {
        this.idrole = idrole;
        this.idmenu = idmenu;
        this.codeservice = codeservice;
        this.codedir = codedir;
        this.interdit = interdit;
        this.refuser = refuser;
    }
    
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("usermenu");
        this.preparePk("MEN", "getSeqUserMenu");
        this.setId(makePK(c));
    }
    /**
     * Constructeur par défaut
     */
    public UserMenu() {
        super.setNomTable("usermenu");
    }
    /**
     * 
     * @return id de l'usermenu
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 
     * @return id du role correspondant
     */
    public String getIdrole() {
        return idrole;
    }
 
    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }
    /**
     * 
     * @return id du menu
     */
    public String getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(String idmenu) {
        this.idmenu = idmenu;
    }

    public String getCodeservice() {
        return codeservice;
    }

    public void setCodeservice(String codeservice) {
        this.codeservice = codeservice;
    }
    /**
     * 
     * @return code de direction
     */
    public String getCodedir() {
        return codedir;
    }

    public void setCodedir(String codedir) {
        this.codedir = codedir;
    }
    /**
     * 
     * @return id de l'utilisateur concerné
     */
    public String getRefuser() {
        return refuser;
    }

    public void setRefuser(String refuser) {
        this.refuser = refuser;
    }

    
    /**
     * 
     * @return represente si ce menu est interdit alors 1 sinon 0
     */
    public int getInterdit() {
        return interdit;
    }

    public void setInterdit(int interdit) {
        this.interdit = interdit;
    }
    
    @Override
    public String getTuppleID() {
        return getId();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    
}
