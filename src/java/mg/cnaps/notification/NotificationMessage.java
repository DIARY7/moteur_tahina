/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.cnaps.notification;

import bean.Annotation;
import bean.ClassEtat;
import java.sql.Connection;
import java.sql.Date;
import utilitaire.Utilitaire;

public class NotificationMessage extends ClassEtat {
    private String id;
    private String message;
    private java.sql.Date daty;
    private String objet;
    private String destinataire;
    private String idobjet;
    private String lien;
    private String service;
    private int priorite;
    private int classee;
    private String heure;
    @Annotation(libelle = "Utilisateur")
    
    private String iduser_recevant;
    private String prestation;

    public String getPrestation() {
        return prestation;
    }

    public void setPrestation(String prestation) {
        this.prestation = prestation;
    }

    public int getPriorite() {
        return priorite;
    }
    public String getService(){
        return service;
    }
    public void setService(String service){
        this.service = service;
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

    public String getIduser_recevant() {
        return iduser_recevant;
    }

    public void setIduser_recevant(String iduser_recevant) {
        if(getMode().compareTo("modif")==0 && iduser_recevant!=null && iduser_recevant.contains("/")){
            String[] g = Utilitaire.split(iduser_recevant, "/");
            this.iduser_recevant= g[0];
            return;
        }
        this.iduser_recevant = iduser_recevant;
    }

    public String getIdobjet() {
        return idobjet;
    }

    public void setIdobjet(String idobjet) throws Exception {
        if(getMode().compareTo("modif")==0 && idobjet == null || (idobjet!=null && idobjet.compareToIgnoreCase("")==0)){
            throw new Exception("Le champ id objet ne doit pas etre vide");
        }
        this.idobjet = idobjet;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public NotificationMessage() {
        super.setNomTable("NOTIFICATION");
    }

    @Override
    public void construirePK(Connection c) throws Exception {     
        this.preparePk("NOTIF", "getSeqNotification");
        this.setId(makePK(c));
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
    
}
