/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mg.cnaps.notification;

import bean.ClassEtat;
import java.sql.Date;
import utilitaire.Utilitaire;

/**
 *
 * @author Andriniaina
 */
public class NotificationLibelle extends ClassEtat {
    
    private String id;
    private String objet;
    private String message;
    private String expediteur;
    private String idobjet,idpers,idservice,iddirection;
    private int priorite,classee;
    private String lien;
    private java.sql.Date daty;
    private String iduser_recevant, destinataire;
    
    public NotificationLibelle() {
        super.setNomTable("NOTIFICATION_LIBELLE2");
    }
    
    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdobjet() {
        return idobjet;
    }

    public void setIdobjet(String idobjet) {
        this.idobjet = idobjet;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public int getClassee() {
        return classee;
    }

    public void setClassee(int classee) {
        this.classee = classee;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public String getIduser_recevant() {
        return iduser_recevant;
    }

    public void setIduser_recevant(String iduser_recevant) {
        if(getMode().compareTo("modif")==0 && iduser_recevant!=null && iduser_recevant.contains("/")){
            String[] g = Utilitaire.split(iduser_recevant, "/");
            String t = g[0];
            this.iduser_recevant= g[0];
            return;
        }
        this.iduser_recevant = iduser_recevant;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getIdpers() {
        return idpers;
    }

    public void setIdpers(String idpers) {
        this.idpers = idpers;
    }

    public String getIdservice() {
        return idservice;
    }

    public void setIdservice(String idservice) {
        this.idservice = idservice;
    }

    public String getIddirection() {
        return iddirection;
    }

    public void setIddirection(String iddirection) {
        this.iddirection = iddirection;
    }
    
}
