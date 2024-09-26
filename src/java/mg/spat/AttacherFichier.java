/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.spat;

import bean.ClassMAPTable;
import java.sql.Connection;
import java.sql.Date;

/**
 *
 * @author JESSI
 */
public class AttacherFichier extends ClassMAPTable {

    private String id; 
    private String chemin;
    private String libelle;
    private String mere;

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
    }

    public AttacherFichier() {
        super.setNomTable("ATTACHER_FICHIER");
    }
    public void construirePK(Connection c) throws Exception {
        this.preparePk("ATT", "GETSEQ_ATTACHER_FICHIER");
        this.setId(makePK(c));
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  
    
    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    
    
}
