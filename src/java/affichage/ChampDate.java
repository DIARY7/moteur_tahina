package affichage;

import utilitaire.Utilitaire;
/**
 * champ spécial pour les attributs de date.
 * Ceffe classe a besoin du fichier javascript champdate.js pour fonctionner.
 * 
 * @author BICI
 */

public class ChampDate extends Champ{
    
    private final String MODEUPDATE = "UPDATE";
    private String mode = "";
    /**
     * Constructeur par défaut
     */
    public ChampDate(){
        super();
    }
    /**
     * 
     * @param nom nom 
     */
    public ChampDate(String nom){
        super(nom);
    }
    
    public ChampDate(String nom, String mode){
        super(nom);
        this.mode = mode;
    }
    
    public void makeHtml() throws Exception{
        String temp = "";
        String val = getValeur();
        if(mode.compareToIgnoreCase(MODEUPDATE) == 0){
            val = Utilitaire.convertDatyFormtoRealDatyFormat(getValeur());
        }else{
            val = Utilitaire.dateDuJour();
        }
        if(getVisible()==false)
        {
            temp = "<input name=" + getNom() + " type='hidden' class=" + getCss() + "  id=" + getNom() + " value='" + val + "' onmouseover=\"datepicker('"+getNom()+"')\" " + getAutre() + ">";
        }
        else {
            temp = "<input name=" + getNom() + " type=" + getType() + " class=" + getCss() + "  id=" + getNom() + " value='" + val + "' onmouseover=\"datepicker('"+getNom()+"')\" " + getAutre() + ">";
        }
        setHtml(temp);
    }
}
