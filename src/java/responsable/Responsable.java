/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responsable;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 *
 * @author itu
 */
public class Responsable extends ClassMAPTable {
    
    private String id;
    private String nom;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Responsable(){
        super.setNomTable("RESPONSABLE");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("RESPONSABLE");
        this.preparePk("RESP","getseqresponsable");
        this.setId(makePK(c));
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
