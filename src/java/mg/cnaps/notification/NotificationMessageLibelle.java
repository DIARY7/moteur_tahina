/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.cnaps.notification;

import bean.ClassMAPTable;
import java.sql.*;
import utilitaire.Utilitaire;

public class NotificationMessageLibelle  extends ClassMAPTable {
    private String id;
    private String objet_message;
    private String message;
    private String expediteur;
    private String direction_expediteur;
    private String service;
    private String service_destinataire;
    private String idobjet;
    private int etat,priorite,classee;
    private String lien;
    private java.sql.Date daty;
    private String IDUSER_RECEVANT;
    private String iduser;

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

    public String getIDUSER_RECEVANT() {
        return IDUSER_RECEVANT;
    }

    public void setIDUSER_RECEVANT(String IDUSER_RECEVANT) {
        if(getMode().compareTo("modif")==0 && IDUSER_RECEVANT!=null && IDUSER_RECEVANT.contains("/")){
            String[] g = Utilitaire.split(IDUSER_RECEVANT, "/");
            String t = g[0];
            this.IDUSER_RECEVANT= g[0];
            return;
        }
        this.IDUSER_RECEVANT = IDUSER_RECEVANT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjet_message() {
        return objet_message;
    }

    public void setObjet_message(String objet_message) {
        this.objet_message = objet_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getDirection_expediteur() {
        return direction_expediteur;
    }

    public void setDirection_expediteur(String direction_expediteur) {
        this.direction_expediteur = direction_expediteur;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService_destinataire() {
        return service_destinataire;
    }

    public void setService_destinataire(String service_destinataire) {
        this.service_destinataire = service_destinataire;
    }

    public String getIdobjet() {
        return idobjet;
    }

    public void setIdobjet(String idobjet) {
        this.idobjet = idobjet;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
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

    
    public NotificationMessageLibelle() {
        super.setNomTable("notification_libelle");
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
