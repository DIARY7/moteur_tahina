/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mg.cnaps.notification;

/**
 *
 * @author Doudou
 */
public class NotificationLibelleS extends NotificationLibelle  {
    private String sous_prestation_libelle;
    private String heure;
    
    public NotificationLibelleS(){
        super.setNomTable("NOTIFICATION_LIBELLE3");
    }

    public String getSous_prestation_libelle() {
        return sous_prestation_libelle;
    }

    public void setSous_prestation_libelle(String sous_prestation_libelle) {
        this.sous_prestation_libelle = sous_prestation_libelle;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
    
}
