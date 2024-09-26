/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.List;

/**
 *
 * @author mbola
 */
public class ValidationException extends Exception {
    private String messageavalider;
    private boolean response = false;
    private ClassMAPTable objet;
    private String acte;
    private List<ClassMAPTable> liste_object;
    
    
    
    public ClassMAPTable getObjet() {
        return objet;
    }

    public void setObjet(ClassMAPTable objet) {
        this.objet = objet;
    }

    public String getActe() {
        return acte;
    }

    public void setActe(String acte) {
        this.acte = acte;
    }
    
    
    
    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessageavalider() {
        return messageavalider;
    }

    public void setMessageavalider(String messageavalider) {
        this.messageavalider = messageavalider;
    }

    
    public ValidationException(String messageavalider) {
        this.setMessageavalider(messageavalider);
        
    }

    public ValidationException(String messageavalider, ClassMAPTable objet, String acte) {
        this.messageavalider = messageavalider;
        this.objet = objet;
        this.acte = acte;
    }

    
    public ValidationException(String message, boolean response) {
        this.setMessageavalider(messageavalider);
        this.setResponse(response);
    }

    public List<ClassMAPTable> getListe_object() {
        return liste_object;
    }

    public void setListe_object(List<ClassMAPTable> liste_object) {
        this.liste_object = liste_object;
    }

    
    
    
}
