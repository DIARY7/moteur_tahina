/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;
import user.CreateObject;
import user.UserEJB;
import constante.ConstanteEtat;
import utilitaire.UtilDB;

/**
 *
 * @author BICI
 */
public abstract class ClassMere extends ClassEtat{
    
    private ClassFille[] fille;
    private String liaisonFille;
    String nomClasseFille;
    
    /**
     * Avoir le nom de classe fille
     * @return nom de la classe fille
     */
    public  String getNomClasseFille() {
        return nomClasseFille;
    }

    public void setNomClasseFille(String nomClasseFill) throws Exception {
        nomClasseFille = nomClasseFill;
    }

    
    /**
     * Avoir tous les classes filles de cette classe mère 
     * @return tous les classes filles
     */
    public ClassFille[] getFille() {
        return fille;
    }

    public void setFille(ClassFille[] fille) {
        this.fille = fille;
    }

    /**
     * Avoir le champs de liaison entre fille et mère
     * @return le champs de liaison
     */
    public String getLiaisonFille() {
        return liaisonFille;
    }

    public void setLiaisonFille(String liaisonFille) {
        this.liaisonFille = liaisonFille;
    }



    /**
     * Inserer un objet
     * @param u id de l'utilisateur courant qui a fait l'insertion
     * @param c connexion ouverte à la base de données
     * @return l'objet qu'on vient d'inserer
     */
    public ClassMAPTable createObject(String u,Connection c)throws Exception
    {
        ClassMAPTable ret= super.createObject(u, c);
        if(fille==null) return ret;
        createObjectFille(u,c);
        return ret;
    }

    /**
     * Inserer un objet fille
     * @param u id de l'utilisateur courant qui a fait l'insertion
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void createObjectFille(String u,Connection c) throws Exception
    {
        /**for(ClassFille f:getFille())
        {
            String idMereF=f.getValInsert(liaisonFille);
            if(idMereF==null||this.getTuppleID().compareToIgnoreCase(idMereF)!=0)f.setValChamp(liaisonFille, this.getTuppleID());
            f.createObject(u, c);
        }**/
    }
    
    /**
     * Inserer un objet
     * @param u id de l'utilisateur courant qui a fait l'insertion
     * @param c connexion ouverte à la base de données
     * @return l'objet qu'on vient d'inserer
     */
    public ClassMAPTable createObjectMultiple(String u,Connection c)throws Exception
    {
        ClassMAPTable ret= super.createObject(u, c);
        if(fille==null) return ret;
        createObjectFilleMultiple(u,c);
        return ret;
    }
    
    /**
     * Inserer un objet fille Multiple
     * @param u id de l'utilisateur courant qui a fait l'insertion
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void createObjectFilleMultiple(String u,Connection c) throws Exception
    {
        for(ClassFille f:getFille())
        {
            String idMereF=f.getValInsert(liaisonFille);
            if(idMereF==null||this.getTuppleID().compareToIgnoreCase(idMereF)!=0)f.setValChamp(liaisonFille, this.getTuppleID());
            f.createObject(u, c);
        }
    }


    /**
     * A obtenir les classes filles
     * @param nomTable 
     * @param c connection
     * @param apresWhere critère pour la recherche
     * @return tous les classes filles 
     * @throws Exception
     */
    public ClassFille[] getFille(String nomTable,Connection c,String apresWhere) throws Exception
    {
        boolean estOuvert=false;
        try
        {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            ClassFille crt=(ClassFille)Class.forName(getNomClasseFille()).newInstance();
            crt.setValChamp(getLiaisonFille(), this.getTuppleID());
            if(nomTable!=null&&nomTable.compareToIgnoreCase("")!=0) crt.setNomTable(nomTable);
            return (ClassFille[])CGenUtil.rechercher(crt, null, null, c, apresWhere);
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
     * Setter la valeur de fille qui est une attribut de la classeMere 
     * @param user id de l'utilisateur courant qui a fait la duplication
     * @param c connection 
     * @return l'objet qui vient de dupliquer
     * @throws Exception
     */
    public ClassMAPTable dupliquer(String user,Connection c) throws Exception
    { 
        boolean estOuvert=false;
        try
        {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
                c.setAutoCommit(false);
            }
            ClassMere cm=(ClassMere) super.dupliquer(user,c);
            cm.setEtat(ConstanteEtat.getEtatCreer());
            cm.insertToTableWithHisto(user, c);
            if(this.getFille()==null) this.setFille(getFille(null,c,""));
            cm.setFille(dupliquerFille(user,cm.getTuppleID(), c));
            if(estOuvert==true)c.commit();
            return cm;
        }
        catch(Exception e)
        {
            c.rollback();
            throw e;
        }
        finally
        {
            if(estOuvert==true&&c!=null)c.close();
        }
    }

    /**
     * Duplier la classe fille 
     * @param user id de l'utilisateur courant qui a fait la duplication
     * @param idMere
     * @param c connection
     * @return les classes qui vient d'être dupliquer
     * @throws Exception
     */
    public ClassFille[] dupliquerFille(String user,String idMere,Connection c) throws Exception
    {
        ClassFille[] retour=new ClassFille[getFille().length];
        int i=0;
        for(ClassFille f:getFille())
        {
            ClassFille dupl=(ClassFille)f.dupliquer(user, c);
            dupl.setValChamp(liaisonFille, idMere);
            dupl.insertToTableWithHisto(user, c);
            retour[i]=dupl;
            i++;
        }
        return retour;
    }
    
    
}
