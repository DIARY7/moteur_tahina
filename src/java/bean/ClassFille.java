/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;
import constante.ConstanteEtat;
import utilitaire.UtilDB;

/**
 *
 * @author pro
 */

public abstract class ClassFille extends ClassEtat{
    
    private String liaisonMere;
    private ClassMere mere;
    static String nomClasseMere;
    
    
    
    
    /**
     * Avoir le nom de la classe mère
     * @return nom de la classe mère
     */
    public static String getNomClasseMere() {
        return nomClasseMere;
    }

    public static void setNomClasseMere(String nomClasseMer) throws Exception {
        nomClasseMere = nomClasseMer;
    }

    /**
     * Avoir le champs de liaison entre fille et mère
     * @return le champs de liaison
     */
    public String getLiaisonMere() {
        return liaisonMere;
    }

    public void setLiaisonMere(String liaisonMere) {
        this.liaisonMere = liaisonMere;
    }

    /**
     * Avoir tous la classe fille de cette classe mère 
     * @return tous la classe fille
     */
    public ClassMere getMere() {
        return mere;
    }

    /**
     * Obtenir la classe mère
     * @param nomTable
     * @param c connexion
     * @return la classe mère ou null si la recherche n'as pas de reponse
     * @throws Exception
     */
    public ClassMere findMere(String nomTable,Connection c)throws Exception
    {
         boolean estOuvert=false;
        try
        {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            ClassMere crt=(ClassMere)Class.forName(getNomClasseMere()).newInstance();
            if(nomTable!=null&&nomTable.compareToIgnoreCase("")!=0) crt.setNomTable(nomTable);
            crt.setValChamp(this.getAttributIDName(), this.getValInsert(this.getLiaisonMere()));
            ClassMere[] valiny= (ClassMere[]) CGenUtil.rechercher(crt, null, null, c, "");
            if(valiny.length>0)
            {
                this.setMere(valiny[0]);
                return valiny[0];
            }
            return null;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(estOuvert==true&&c!=null)c.close();
        }
    }

    /**
     * Cette fonction utilise la fonction <pre>controlerUpdate(Connection c)</pre>
     * @param c connection
     * @throws Exception
     */
    public void controlerDelete(Connection c) throws Exception {
        controlerUpdate(c);
    }

    /**
     * Controle pour mis à jour 
     * @param c connection
     */
    public void controlerUpdate(Connection c) throws Exception {
        ClassMere m=findMere(null,c);
        if(m.getEtat()>=ConstanteEtat.getEtatValider()) throw new Exception("Objet mere deja valide");
    }
    public void setMere(ClassMere mere) {
        this.mere = mere;
    }
    
}
