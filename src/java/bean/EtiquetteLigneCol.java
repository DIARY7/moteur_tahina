/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.ArrayList;

/**
 *
 * @author root
 */
public class EtiquetteLigneCol {
    ArrayList<ValeurEtiquette> ligne=new ArrayList<ValeurEtiquette>() ;

    public ArrayList<ValeurEtiquette> getLigne() {
        return ligne;
    }

    /**
     * Donner des etiquettes à ligne 
     * @param ligne
     */
    public void setLigne(ArrayList<ValeurEtiquette> ligne) {
        this.ligne = ligne;
        
    }

    /**
     * Constructeur par defaut
     */
    public EtiquetteLigneCol()
    {
        
    }
    public EtiquetteLigneCol(ValeurEtiquette v) throws Exception
    {
        getLigne().add(v);
    }

    /**
     * 
     * @param c
     * @throws Exception
     * @deprecated fonction vide 
     */
    public void ajouter(ClassMAPTable c) throws Exception
    {
        
    }

    /**
     * Sert à comparer la valeur des lignes avec des autres valeurs
     * @param autre
     * @return true or false
     * @throws Exception
     */
    public boolean comparer(EtiquetteLigneCol autre) throws Exception
    {
        int i=0;
        for(ValeurEtiquette valLigne:ligne)
        {
            ValeurEtiquette valAutre=autre.getLigne().get(i);
            if(valAutre.getValeur().compareToIgnoreCase(valLigne.getValeur())!=0) return false;
            i++;
        }
        return true;
    }

}
