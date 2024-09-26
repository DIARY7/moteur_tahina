/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

/**
 * Objet representant le role d'un utilisateur
 * 
 * @author BICI
 */

public class MapRoles extends bean.ClassMAPTable implements java.io.Serializable {

    public String idrole;
    public String descrole;
    public int rang;

    /**
     * Constructeur par defaut
     */
    public MapRoles() {
        super.setNomTable("roles");
    }

    public MapRoles(String descRole, String idRole) {
        this.descrole = descRole;
        this.idrole = idRole;
        super.setNomTable("roles");
    }

    public MapRoles(String descRole, String idRole, int rang) {
        this.descrole = descRole;
        this.idrole = idRole;
        this.setRang(rang);
        super.setNomTable("roles");
    }
    /**
     * Implémentation de la fonction pour donner le nom de la colonne d'ID
     */
    @Override
    public String getAttributIDName() {
        return "idrole";
    }
    /**
     * Implémentation de la fonction pour donner la valeur d'ID
     */
    @Override
    public String getTuppleID() {
        return idrole;
    }
    /**
     * 
     * @return rang du role
     */
    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public String getDescrole() {
        return descrole;
    }

    public void setDescrole(String descrole) {
        this.descrole = descrole;
    }

    public void setIdrole(String idrole) {
        this.idrole = idrole;
    }

    public String getIdrole() {
        return idrole;
    }
}
