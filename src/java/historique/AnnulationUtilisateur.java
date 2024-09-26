package historique;
import bean.ClassMAPTable;
import utilitaire.Utilitaire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MSI
 */
public class AnnulationUtilisateur extends ClassMAPTable {
    
    public String idAnnulationUser;
    public int refUser;
    public java.sql.Date daty;
    
    public AnnulationUtilisateur(String id, String ref, java.sql.Date dat) {
        this.setIdAnnulationUser(id);
        this.setRefUser(ref);
        this.setDaty(dat);
        this.setNomTable("AnnulationUtilisateur");
    }
    
    public AnnulationUtilisateur(String ref, java.sql.Date dat) {
        setIndicePk("ANU");
//    setNomProcedureSequence("getSeqAnnulationUser");
        setNomProcedureSequence("GET_SEQ_ANNULATIONUTILISATEUR");
        this.setIdAnnulationUser(makePK());
        this.setRefUser(ref);
        this.setDaty(dat);
        this.setNomTable("AnnulationUtilisateur");
    }
    
    public AnnulationUtilisateur(String ref, String dat) {
        setIndicePk("ANU");
        setNomProcedureSequence("GET_SEQ_ANNULATIONUTILISATEUR");
        this.setIdAnnulationUser(makePK());
        this.setRefUser(ref);
        if (ref.compareTo("") == 0 || ref == null) {
//            this.setRefUser("-");
        } else {
//            this.setRefUser(ref);
        }

//    this.setDaty(dat);
        if (String.valueOf(dat).compareTo("") == 0 || dat == null) {
            this.setDaty(Utilitaire.dateDuJourSql());
        } else {
            this.setDaty(Utilitaire.string_date("dd/MM/yyyy", dat));
        }
        
        this.setNomTable("AnnulationUtilisateur");
    }
    
    public AnnulationUtilisateur(String ref) {
        setIndicePk("ANU");
        setNomProcedureSequence("GET_SEQ_ANNULATIONUTILISATEUR");
        this.setIdAnnulationUser(makePK());
        this.setRefUser(ref);
        this.setDaty(Utilitaire.dateDuJourSql());
        this.setNomTable("AnnulationUtilisateur");
    }
    
    public String getAttributIDName() {
        return "idAnnulationUser";
    }
    
    public String getTuppleID() {
        return this.getIdAnnulationUser();
    }
    
    public void setIdAnnulationUser(String idAnnulationUser) {
        this.idAnnulationUser = idAnnulationUser;
    }
    
    public String getIdAnnulationUser() {
        return idAnnulationUser;
    }

    public void setRefUser(String refUser) {
        this.setRefUser(Integer.parseInt(refUser));
    }
    
//  public String getRefUser() {
//    return refUser;
//  }

    public int getRefUser() {
        return refUser;
    }
    
    public void setRefUser(int refUser) {
        this.refUser = refUser;
    }
    
    public void setDaty(java.sql.Date daty) {
        if (String.valueOf(daty).compareTo("") == 0 || daty == null) {
            this.daty = utilitaire.Utilitaire.dateDuJourSql();
        } else {
            this.daty = daty;
        }
    }
    
    public java.sql.Date getDaty() {
        return daty;
    }
}
