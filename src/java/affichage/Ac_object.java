
package affichage;

/**
 *
 * Objet representant une option d'autocomplete
 * 
 * @author rakotondralambokoto
 */
public class Ac_object {
    private Object valeur;
    private Object affiche; 

    /**
     * 
     * @return valeur à inserer/update
     */
    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }
    /**
     * 
     * @return valeur à afficher sur le formulaire
     */
    public Object getAffiche() {
        return affiche;
    }

    public void setAffiche(Object affiche) {
        this.affiche = affiche;
    }
    /**
     * 
     * @param valeur valeur à inserer/update
     * @param affiche valeur à afficher sur le formulaire
     */
    public Ac_object(Object valeur, Object affiche) {
        this.setValeur(valeur);
        this.setAffiche(affiche);
    }

    public Ac_object() {
    }
    
    
}
