/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilisateur;

import bean.ClassEtat;
import java.sql.Connection;
import java.sql.Date;
import utilitaire.Utilitaire;

/**
 *
 * @author Ignafah
 */
public class UtilisateurInterim extends ClassEtat{

    private String id, iduser_interim, iduser_a_remplacer;
    private Date daty, date_debut, date_fin;
    
    public void construirePK(Connection c) throws Exception {
        this.preparePk("UI", "getSeqUtilisateurInterim");
        this.setId(makePK(c));
    }

    public UtilisateurInterim() {
        super.setNomTable("UTILISATEUR_INTERIM");
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIduser_interim() {
        return iduser_interim;
    }

    public void setIduser_interim(String iduser_interim) throws Exception {
        try{
            if(getMode().compareTo("modif")!=0){
                 this.iduser_interim = iduser_interim;
                 return;
            }
            if(iduser_interim==null || iduser_interim.compareToIgnoreCase("")==0){
                    throw new Exception("Le Champs interim est obligatoire");
            }
                
            if(getMode().compareTo("modif")==0 && iduser_interim!=null && iduser_interim.contains("/")){
                String[] g = Utilitaire.split(iduser_interim, "/");
                String t = g[0];
                this.iduser_interim= g[0];
                return;
            }
            
            this.iduser_interim = iduser_interim;
        }catch(Exception e){
            throw e;
        }        
    }

    public String getIduser_a_remplacer() {
        return iduser_a_remplacer;
    }

    public void setIduser_a_remplacer(String iduser_a_remplacer) throws Exception{
        try{
            if(getMode().compareTo("modif")!=0){
                 this.iduser_a_remplacer = iduser_a_remplacer;
                 return;
            }
            if(iduser_a_remplacer==null || iduser_a_remplacer.compareToIgnoreCase("")==0){
                    throw new Exception("Le Champ a remplacer est obligatoire");
            }
            if(getMode().compareTo("modif")==0 && iduser_a_remplacer!=null && iduser_a_remplacer.contains("/")){
                
                String[] g = Utilitaire.split(iduser_a_remplacer, "/");
                String t = g[0];
                this.iduser_a_remplacer= g[0];
                return;
            }
            this.iduser_a_remplacer = iduser_a_remplacer;
        }catch(Exception e){
            throw e;
        }        
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) throws Exception{
        try{
            if(getMode().compareTo("modif")!=0){
                 this.daty = daty;
                 return;
            }
            if(daty==null){
                throw new Exception("Le Champs date est obligatoire");
            }
            this.daty = daty;
        }catch(Exception e){
            throw e;
        }       
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) throws Exception {
        try{
            if(getMode().compareTo("modif")!=0){
                 this.date_debut = date_debut;
                 return;
            }
            if(date_debut==null){
                throw new Exception("Le Champs date debut est obligatoire");
            }
            this.date_debut = date_debut;
        }catch(Exception e){
            throw e;
        }        
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
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
