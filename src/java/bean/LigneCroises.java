/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.ArrayList;

/**
 * @deprecated
 * Objet representant une ligne de la table de valeur groupé
 * @author BICI
 */
public class LigneCroises {
    ArrayList<ValeurCroises> ligne=new ArrayList<ValeurCroises>() ;

    public ArrayList<ValeurCroises> getLigne() {
        return ligne;
    }

    public void setLigne(ArrayList<ValeurCroises> ligne) {
        this.ligne = ligne;
        
    }
    public void ajouter(ValeurCroises v) throws Exception
    {
        getLigne().add(v);
    }
    public LigneCroises(double c,String lien) throws Exception
    {
        ligne.add(new ValeurCroises(c,lien));
    }
    
}
