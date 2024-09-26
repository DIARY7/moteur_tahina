/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import constante.ConstanteEtat;
import user.UserEJBBean;
import utilitaire.UtilDB;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Toutes les classes ayant un attribut état doivent hériter de cette classe. Ci-dessus un exemple d'implementation
 * 
 * <pre>
 * 
 * public class Facture extends ClassEtat{
 *      @override
 *      validerObject(String u, Connection c) throws Exception{
 *          //logique sur la validation
 *          //L'appel ci-dessous doit se faire si vous voulez enregistrer la validation
 *          super.validerObject(u,c);
 *      }
 * }
 * </pre>
 *
 * @author BICI
 */

public abstract class ClassEtat extends ClassUser{

    int etat = 1;

    public int getEtat() {
        return etat;
    }
    
    public void setEtat(int etat) {
        this.etat = etat;
    }
    /**
     * 
     * @param value valeur numérique d'un état
     * @return la valeur textuelle d'un état
     */
    public String getEtatText(int value){
        return chaineEtat(value);
    }
    /**
     * Annule la validation de l'objet
     * @param user identifiant de l'utilisateur courant
     * @param c connexion ouverte à la base de données
     * @return nombre de ligne modifiée suite à la mise à jour
     * @throws Exception 
     */
    public void annulerVisa(String u,Connection c) throws Exception {
        int ouvert = 0;
        try{
            if(c == null){
                c=new UtilDB().GetConn();
                c.setAutoCommit(false);
                ouvert = 1;
            }
            this.setMode("modif");
            this.controlerAnnulationVisa(c);
            this.setEtat(1);
            this.updateToTableWithHisto(u,c);
            if(c!=null && ouvert ==1){
                c.commit();
            }
        }
        catch(Exception e){
            if(c!=null && ouvert ==1){
                c.rollback();
            }
            e.printStackTrace();
            throw e;
        }
        finally{
            if(c!=null && ouvert ==1){
                c.close();
            }
        }
    }
    
    
    /**
     * Met à jour l'état de l'objet
     * @param valeurEtat valeur de l'etat pour mettre à jour
     * @param id id de l'objet à mettre à jour
     * @param c connexion ouverte à la base de données
     * @return nombre de ligne modifiée suite à la mise à jour
     * @throws Exception 
     */
    public int updateEtat(int valeurEtat, String id, Connection c) throws Exception {
        Statement cmd = null;
        try {
            String req = "update " + getNomTable() + " set etat=" + valeurEtat + " where " + getAttributIDName() + " = '" + id + "'";
            cmd = c.createStatement();
            return cmd.executeUpdate(req);

        } catch (Exception ex) {
            if( c != null ){c.rollback();}
            throw ex;
        } finally {
            cmd.close();
        }
    }
    
    /**
     * Modifier cette fonction pour afficher le texte correspondant à un état
     * @param value valeur de l'état
     * @return 
     */
    public String chaineEtat(int value){
        if(value == ConstanteEtat.getEtatCreer()) return "<b style='color:lightskyblue'>CR&Eacute;&Eacute;(E)</b>";
        if(value == ConstanteEtat.getEtatValider()) return "<b style='color:green'>TRAIT&Eacute;(E)</b>";
        if(value == ConstanteEtat.getEtatAnnuler()) return "<b style='color:orange'>ANNUL&Eacute;(E)</b>";
        if(value==ConstanteEtat.getEtatCloture()) return "<b style='color:orange'>CLOTUR&Eacute;(E)</b>";
        if(value==ConstanteEtat.getEtatPlanifie()) return "<b style='color:lightskyblue'>PLANIFI&Eacute;(E)</b>";
        if(value==ConstanteEtat.getEtatInterviewe()) return "<b style='color:lightskyblue'>INTERVIEW&Eacute;(E)</b>";
        return "<b style='color:lightskyblue'>AUTRE</b>";
    }
    
    /**
     * Valider un objet
     * @param u identifiant de l'utilisateur
     * @param c connexion ouverte à la base de données
     * @return lui même après validation
     * @throws Exception 
     */
    public Object validerObject(String u, Connection c) throws Exception
    {
        setMode("modif");
        controlerUpdate(c);
        this.setEtat(ConstanteEtat.getEtatValider());
        this.updateToTableWithHisto(u, c);
        return this;
    }
    /**
     * 
     * @param user contexte utilisateur
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void viser(UserEJBBean user,Connection c) throws Exception
    {
        validerObject(user.getUser().getTuppleID() ,c);
    }

    public void controleAnnuler(Connection c) throws Exception
    {
        if(getEtat()>=ConstanteEtat.getEtatValider()) throw new Exception("Impossible à annuler, deja valide");
    }
    public void controleAnnulerVisa(Connection c) throws Exception
    {
        if(getEtat()>ConstanteEtat.getEtatValider()) throw new Exception("Impossible d annuler le Visa, superieur valide");
    }
    
    public int annuler(String user,Connection c)throws Exception
    {
        controleAnnuler(c);
        setMode("modif");
        this.setEtat(ConstanteEtat.getEtatAnnuler());
        return this.updateToTableWithHisto(user, c);
    }
    
    public Object annulerValidation(String u, Connection c) throws Exception
    {
        annulerVisa(u, c);
        return this;
    }

    public Object cloturerObject(String u, Connection c) throws Exception{
        setMode("modif");
        this.setEtat(ConstanteEtat.getEtatCloture());
        controler(c);
        this.updateToTableWithHisto(u, c);
        return this;
    }


    public Object payerObject(String u, Connection c) throws Exception{
        setMode("modif");
        this.setEtat(ConstanteEtat.getEtatPayer());
        controler(c);
        this.updateToTableWithHisto(u, c);
        return this;
    }


    public void controlerAnnulationVisa(Connection c) throws Exception{
    }
    public void changeState(String acte, String u,Connection con)throws Exception{

    }
}
