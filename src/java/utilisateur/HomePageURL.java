/*
 * Package regroupant les utilités pour jouer avec les utilisateurs et leur accès
 */
package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;
/**
 * Objet gardant les pages d'accueil de chaque role, direction et service
 * 
 * Grace à cet objet on peut rediriger chaque login à la page qu'il faut.
 * 
 * @author BICI
 */
public class HomePageURL extends ClassMAPTable{
    
    private String id;
    private String codeservice;
    private String urlpage;
    private String idrole;
    private String codedir;
    /**
     * Constructeur par défaut
     */
    public HomePageURL() {
        this.setNomTable("USERHOMEPAGE");
    }
    /**
     * 
     * @return id informatique du lien
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 
     * @return code de service du lien
     */
    public String getCodeservice() {
        return codeservice;
    }

    public void setCodeservice(String codeservice) {
        this.codeservice = codeservice;
    }
    /**
     * 
     * @return url vers le lien 
     */
    public String getUrlpage() {
        return urlpage;
    }

    public void setUrlpage(String urlpage) {
        this.urlpage = urlpage;
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
     * @return code de direction actuelle
     */
    public String getCodedir() {
        return codedir;
    }

    public void setCodedir(String codedir) {
        this.codedir = codedir;
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
        this.preparePk("UHP", "getSeqHomePageURL");
        this.setId(makePK(c));
    }
    
}
