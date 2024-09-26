package affichage;

import utilitaire.Utilitaire;

/**
 * champ spécial pour les attributs numériques.
 * Ceffe classe a besoin du fichier javascript champcalcul.js pour fonctionner.
 * Les nombres vont être arrondi à 2 chiffres après la virgule
 * 
 * @author BICI
 */

public class ChampCalcul extends Champ{
    /**
     * constructeur par défaut
     */
    public ChampCalcul(){
        super();
    }
    
    /**
     * 
     * @param nom nom de l'attribut
     */
    public ChampCalcul(String nom){
        super(nom);
    }
    /**
     * Surchargement de makeHTML
     * génerer le html d'input pour les numériques
     */
    public void makeHtml() throws Exception{
        String temp = "";
        if(getVisible()==false)
        {
            temp = "<input name=" + getNom() + " type='hidden' class=" + getCss() + "  id=" + getNom() + " value='" + Utilitaire.doubleWithoutExponential(Utilitaire.stringToDouble(getValeur())) + "' onblur=\"calculer('"+getNom()+"')\" " + getAutre() + ">";
        }
        else {
            temp = "<input name=" + getNom() + " type=" + getType() + " class=" + getCss() + "  id=" + getNom() + " value='" + Utilitaire.doubleWithoutExponential(Utilitaire.stringToDouble(getValeur())) + "' onblur=\"calculer('"+getNom()+"')\" " + getAutre() + ">";
        }
        setHtml(temp);
    }
}
