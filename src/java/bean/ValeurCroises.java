/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author root
 */
public class ValeurCroises {
    double valeur;
    String lien;
    double[] listeVal;

    public double[] getListeVal() {
        return listeVal;
    }

    public void setListeVal(double[] listeVal) {
        this.listeVal = listeVal;
    }
    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
    public ValeurCroises(double val,String lie)
    {
        this.setValeur(val);
        this.setLien(lie);
    }
    public ValeurCroises(double[] val,String lie)
    {
        this.setListeVal(val);
        if(val.length>=1)this.setValeur(val[0]);
        this.setLien(lie);
    }
    
    
    
    
}
