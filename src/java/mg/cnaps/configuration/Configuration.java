package mg.cnaps.configuration;

import bean.ClassMAPTable;
import java.sql.Connection;
import java.sql.Date;
import utilitaire.Utilitaire;

public class Configuration extends ClassMAPTable{
    String id;
    String valmin;
    String valmax;
    String desce;
    String remarque;
    Date daty;
    String typeconfig;
    String compte_credit;

    public String getCompte_credit() {
        return compte_credit;
    }

    public void setCompte_credit(String compte_credit) {
        this.compte_credit = compte_credit;
    }

    public Configuration() {
        super.setNomTable("configuration");
    }

    public Configuration(String valmin, String valmax, String desce, String remarque, Date daty, String typeconfig) throws Exception {
        super.setNomTable("configuration");
        setValmin(valmin);
        setValmax(valmax);
        setDesce(desce);
        setRemarque(remarque);
        setDaty(daty);
        setTypeconfig(typeconfig);
    }
    
    public void construirePK(Connection c) throws Exception{
        this.preparePk("CONF","getSeqConfiguration");
        this.setId(makePK(c));
    }
    
    public String getTuppleID() {
        return getId();
    }

    public String getAttributIDName() {
       return "id";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValmin() {
        return Utilitaire.champNull(valmin);
    }

    public void setValmin(String valmin) throws Exception{
        try{
            if(getMode().compareTo("modif")!=0){
                this.valmin =  valmin;
                return;
            }
            if(valmin==null || valmin.compareToIgnoreCase("")==0){
                throw new Exception("Valeur minimale vide");
            }
            this.valmin = valmin;
        }catch(Exception e){
            throw e;
        }        
    }

    public String getValmax() {
        return Utilitaire.champNull(valmax);
    }

    public void setValmax(String valmax) throws Exception{
        try{
            if(getMode().compareTo("modif")!=0){
                this.valmax =  valmax;
                return;
            }
            if(valmax==null || valmax.compareToIgnoreCase("")==0){
                throw new Exception("Valeur maximale vide");
            }  
            this.valmax = valmax;
        }catch(Exception e){
            throw e;
        }        
    }
    
    

    public String getDesce() {
        return Utilitaire.champNull(desce);
    }

    public void setDesce(String desce) throws Exception {
        try{
            if(getMode().compareTo("modif")!=0){
                this.desce =  desce;
                return;
            }
            if(desce==null || desce.compareToIgnoreCase("")==0){
                throw new Exception("Description vide");
            }            
            this.desce = desce;
        }catch(Exception e){
            throw e;
        }        
    }

    public String getRemarque() {
        return Utilitaire.champNull(remarque);
    }

    public void setRemarque(String remarque) throws Exception{
       /* try{
            if(getMode().compareTo("modif")!=0){
                this.remarque =  remarque;
                return;
            }
            if(remarque==null || remarque.compareToIgnoreCase("")==0){
                throw new Exception("Code vide");
            }
        }catch(Exception e){
            throw e;
        }*/
       this.remarque = remarque;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) throws Exception{
        try{
            if(getMode().compareTo("modif")!=0){
                this.daty =  daty;
                return;
            }
            if(daty==null){
                throw new Exception("Date vide");
            }            
            this.daty = daty;
        }catch(Exception e){
            throw e;
        } 
    }

    public String getTypeconfig() {
        return Utilitaire.champNull(typeconfig);
    }

    public void setTypeconfig(String typeconfig) {
        this.typeconfig = typeconfig;
    }
    
    
    
}
