/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;
import java.sql.Date;

/**
 * Objet gardant les historiques des rejets d'objets d'état.
 * 
 * @author BICI
 */
public class RejetTable extends ClassMAPTable {

    private String id;
    private java.sql.Date daty;
    private String remarque;
    private String mere;
    private String motif;
    private String categorierejet;
    /**
     * 
     * @param id id de l'objet rejet table
     * @param daty date de rejet
     * @param remarque remarque sur le rejet
     * @param mere id de l'objet rejeté
     * @param motif motif de rejet
     * @param categorierejet id de la catégorie de rejet
     */
    public RejetTable(String id, Date daty, String remarque, String mere, String motif, String categorierejet) {
        this.id = id;
        this.daty = daty;
        this.remarque = remarque;
        this.mere = mere;
        this.motif = motif;
        this.categorierejet = categorierejet;
    }
    
    public RejetTable() {
        
    }
    /**
     * 
     * @param daty
     * @param remarque
     * @param mere
     * @param motif
     * @param categorierejet
     */
    public RejetTable(Date daty, String remarque, String mere, String motif, String categorierejet) {
        this.daty = daty;
        this.remarque = remarque;
        this.mere = mere;
        this.motif = motif;
        this.categorierejet = categorierejet;
    }
    
    public void construirePK(Connection c) throws Exception{
        this.preparePk("REJ", "getSeqRejet");
        this.setId(this.makePK(c));
    } 
    
    public String getAttributIDName() {
        return "id";
    }

    public String getTuppleID() {
        return this.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 
     * @return date de rejet
     */
    public java.sql.Date getDaty() {
        return daty;
    }
    
    public void setDaty(java.sql.Date daty) {
        this.daty = daty;
    }

    public String getRemarque() {
        return remarque;
    }
    /**
     * 
     * @param remarque sur le rejet
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    /**
     * 
     * @return id de l'objet qui vient d'être rejeté
     */
    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
    }
    /**
     * 
     * @return motif du rejet
     */
    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
    /**
     * 
     * @return id du type objet de rejet
     */
    public String getCategorierejet() {
        return categorierejet;
    }

    public void setCategorierejet(String categorierejet) {
        this.categorierejet = categorierejet;
    }

}
