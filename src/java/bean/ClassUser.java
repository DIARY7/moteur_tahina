/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Date;

/**
 *
 * @author MSI
 */

public abstract class ClassUser extends ClassMAPTable{
    private String iduser;
    private String idverif, idrempli;
    private Date dateverif, daterempli, datesaisie;
    private String direction;
    String service;

    public String getDirection() {
        return direction;
    }

    public Date getDatesaisie() {
        return datesaisie;
    }

    public void setDatesaisie(Date datesaisie) {
        this.datesaisie = datesaisie;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }    

    public String getIdverif() {
        return idverif;
    }

    public void setIdverif(String idverif)  throws Exception {
        this.idverif = idverif;
    }

    public String getIdrempli() {
        return idrempli;
    }

    public void setIdrempli(String idrempli)  throws Exception {
        this.idrempli = idrempli;
    }

    public Date getDateverif() {
        return dateverif;
    }

    public void setDateverif(Date dateverification)  throws Exception {
        this.dateverif = dateverification;
    }

    public Date getDaterempli() {
        return daterempli;
    }

    public void setDaterempli(Date dateremplissage)  throws Exception {
        this.daterempli = dateremplissage;
    }
    
}
