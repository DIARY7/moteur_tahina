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
 * @author Admin
 */
public class NotifRecu extends ClassEtat{
    
    private Date daty;
    private String objet,message,direction_initiale,service_initiale,destinataire,lien;
     private String id,idnotif,refuser,nom,prenom,iduser,idobjet;

    public NotifRecu(Date daty, String objet, String message, String direction_initiale, String service_initiale, String destinataire, String lien, String id, String idnotif, String refuser, String nom, String prenom, String iduser, String idobjet) {
        this.setDaty(daty);
        this.setObjet(objet);
        this.setMessage(message);
        this.setDirection_initiale(direction_initiale);
        this.setService_initiale(service_initiale);
        this.setDestinataire(destinataire);
        this.setLien(lien);
        this.setId(id);
        this.setIdnotif(idnotif);
        this.setRefuser(refuser);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setIduser(iduser);
        this.setIdobjet(idobjet);
    }

    public String getDirection_initiale() {
        return direction_initiale;
    }
    public void setDirection_initiale(String direction_initiale) {
        this.direction_initiale = direction_initiale;
    }
    public String getService_initiale() {
        return service_initiale;
    }
    public void setService_initiale(String service_initiale) {
        this.service_initiale = service_initiale;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

     @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

   
    public NotifRecu() {
        this.setNomTable("NOTIF_COMPLET");
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
