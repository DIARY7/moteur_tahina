/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;
import user.UserEJB;
import user.UserEJBBean;
import utilitaire.Utilitaire;

/**

 * 
 * @author BICI
 */
public class AnalyseLien extends ClassMAPTable {
    
    private String id;
    
    private String page;
    
    private int idUtilisateur;
        
    private String titre;
    
    private String lien;
    
     public AnalyseLien() {
        this.setNomTable("analyselien");
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @return the idUtilisateur
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * @param idUtilisateur the idUtilisateur to set
     */
    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre)throws Exception {
       
          if(this.getMode().compareTo("modif")==0){
           if(titre.isEmpty() || titre==null)
                throw new Exception("Titre ne peut etre null");
        }
        this.titre = titre;
        
    }
    public void controler(UserEJB user)throws Exception
    {
        if(this.getIdUtilisateur()!=user.getUser().getRefuser())
        {
            throw new Exception("Utilisateur non valide");
        }
    }
    /**
     * @return the lien
     */
    public String getLien() {
        return lien;
    }

    /**
     * @param lien the lien to set
     */
    public void setLien(String lien) {
        this.lien = lien;
    }

    @Override
    public String getTuppleID() {
        return getId();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
     public void construirePK(Connection c) throws Exception {
         this.preparePk("AL", "getseqanalyselien");
        this.setId(makePK(c));
        
    }

   
    
}
