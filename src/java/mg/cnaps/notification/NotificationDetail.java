/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mg.cnaps.notification;

import bean.ClassMAPTable;
import java.sql.Connection;
import utilitaire.Utilitaire;

/**
 *
 * @author Admin
 */
public class NotificationDetail extends ClassMAPTable{
    private String id,idnotif,refuser,nom,prenom,iduser,idobjet;

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
        this.preparePk("NFD", "getSeqNOTIFDETAIL");
        this.setId(makePK(c));
    }
    public NotificationDetail() {
        this.setNomTable("notif_detail");
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdnotif() {
        return idnotif;
    }

    public void setIdnotif(String idnotif) {
        this.idnotif = idnotif;
    }

    public String getRefuser() {
        return refuser;
    }

    public void setRefuser(String refuser) {
        this.refuser = refuser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        if(getMode().compareTo("modif")==0 && iduser!=null && iduser.contains("/")){
            String[] g = Utilitaire.split(iduser, "/");
            String t = g[0];
            this.iduser= g[0];
            return;
        }
        this.iduser = iduser;
    }

    public String getIdobjet() {
        return idobjet;
    }

    public void setIdobjet(String idobjet) {
        this.idobjet = idobjet;
    }
}
