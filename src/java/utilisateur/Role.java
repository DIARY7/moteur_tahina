
package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 * Classe représentant le role d'un utilisateur et est mappé à la table "ROLES".
 * Cette classe est important pour jouer avec les droits des utilisateurs avec l'aide 
 * des classes utilitaires {@link menu.MenuDynamique},{@link utilisateur.Restriction}.
 * 
 * @author BICI
 */
public class Role extends ClassMAPTable {

    private String idrole;
    private String descrole;
    private int rang; 
    /**
     * Constructeur par défaut
     */
    public Role() {
        super.setNomTable("roles");
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
     * @return description du role
     */
    public String getDescrole() {
        return descrole;
    }

    public void setDescrole(String descrole) {
        this.descrole = descrole;
    }
    /**
     * 
     * @return rang du role par rapport aux autres roles
     */
    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }
    

    @Override
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("roles");
        this.preparePk("ROL", "getSeqRoles");
        this.setIdrole(makePK(c));
    }

    @Override
    public String getTuppleID() {
        return this.getIdrole();
    }

    @Override
    public String getAttributIDName() {
        return "idrole";
    }

}
