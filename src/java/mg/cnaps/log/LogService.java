/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.cnaps.log;

import bean.CGenUtil;
import bean.ClassMAPTable;
import java.sql.Connection;
import utilitaire.Utilitaire;

/**
 *
 * @author Jetta
 */
public class LogService extends ClassMAPTable {

    private String id, libelle, code_dr, dr_rattache,code_service;

    public void setCode_service(String code_service) {
        this.code_service = code_service;
    }

    public String getCode_service() {
        return code_service;
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
        this.preparePk("SERV", "GETSEQLOGSERVICE");
        this.setId(makePK(c));
    }
    
    

    public LogService() {
        super.setNomTable("log_service");
    }

    public LogService(String libelle, String code_dr, String dr_rattache) throws Exception {

        super.setNomTable("log_service");
        this.setLibelle(libelle);
        this.setCode_dr(code_dr);
        this.setDr_rattache(dr_rattache);
    }
     public LogService(String id,String libelle, String code_dr, String dr_rattache) throws Exception {

        super.setNomTable("log_service");
        this.setId(id);
        this.setLibelle(libelle);
        this.setCode_dr(code_dr);
        this.setDr_rattache(dr_rattache);
    }
     public LogService(String id,String libelle, String code_dr, String dr_rattache,String code_service) throws Exception {

        super.setNomTable("log_service");
        this.setId(id);
        this.setLibelle(libelle);
        this.setCode_dr(code_dr);
        this.setDr_rattache(dr_rattache);
        this.setCode_service(code_service);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return Utilitaire.champNull(libelle);
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode_dr() {
        return Utilitaire.champNull(code_dr);
    }

    public void setCode_dr(String code_dr) throws Exception {
        if(getMode().compareTo("modif")!=0)
        {
            this.code_dr = code_dr;
            return;
        }
        if(code_dr=="") throw new Exception("Champ Code Direction Regionale manquant");
        this.code_dr = code_dr;
    }

    public String getDr_rattache() {
        return Utilitaire.champNull(dr_rattache);
    }

    public void setDr_rattache(String dr_rattache) {
        this.dr_rattache = dr_rattache;
    }
    
    @Override
    public String getValColLibelle() {
        return this.libelle;
    }

}
