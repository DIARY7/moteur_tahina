/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;
import utilitaire.Utilitaire;
/**
 *
 * @author root
 */
public class ValeurEtiquette {
    String valeur;
    String lien;
    String type="valeur";
    String align="right";

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
    public ValeurEtiquette(String val,String lie)
    {
        this.setValeur(formatter(val));
        this.setLien(lie);
        this.setType("entete");
        this.setAlign("center");
    }
    public ValeurEtiquette(double val,String lie)
    {
        this.setValeur(utilitaire.Utilitaire.formaterAr(val));
        this.setLien(lie);
    }
    public ValeurEtiquette(ValeurCroises v)
    {
        if(v!=null)
        {
            if(v.getListeVal()!=null&&v.getListeVal().length>0)
            {
                String retour="";
                for(int i=0;i<v.getListeVal().length;i++)
                {
                    retour=retour+utilitaire.Utilitaire.formaterAr(v.getListeVal()[i]);
                    if(i<v.getListeVal().length-1) retour=retour+"<BR>";
                }
                this.setValeur(retour);
            }
            else this.setValeur(utilitaire.Utilitaire.formaterAr(v.getValeur()));
            this.setLien(v.getLien());
        }
        
    }

    /**
     * Formater une date en local
     * @param val 
     * @return
     */
    public static String formatter(String val)
    {
        try
        {
            boolean dat=Utilitaire.isValidDate(val, "yyyy-mm-dd");
            boolean dat2=Utilitaire.isValidDate(val, "yy-mm-dd");
            if(dat||dat2) return Utilitaire.formatterDaty(val);
            return val;
        }
        catch(Exception e)
        {
            return val;
        }
    }
}
