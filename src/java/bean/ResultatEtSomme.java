/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import bean.ClassMAPTable;

/**
 * Objet de resultat suite à une recherche avec somme.
 * <pre>
 * le champ resultat comporte les resultats de la recherche.
 * le champ somme comporte la liste des sommes selon les colonnes spécifiées dans la recherche et le dernier indice de la liste correspondant au count des resultats.
 * </pre>
 * @author BICI
 */

public class ResultatEtSomme {

    Object[] resultat;
    private double[] sommeEtNombre;

    public ResultatEtSomme() {
    }

    public ResultatEtSomme(Object[] r, double[] rn) {
        this.setResultat(r);
        this.setSommeEtNombre(rn);
    }

    public ResultatEtSomme(Object[] r) {
        this.setResultat(r);
        //this.setSommeEtNombre(rn);
    }
    /**
     * 
     * @return liste d'objets classMapTable
     */
    public Object[] getResultat() {
        return resultat;
    }
    /**
     * 
     * @return indice 0 à length - 2 somme de colonnes, indice length -1 compte des resultats
     */
    public double[] getSommeEtNombre() {
        return sommeEtNombre;
    }

    public void setResultat(Object[] resultat) {
        this.resultat = resultat;
    }

    public void setSommeEtNombre(double[] sommeEtNombre) {
        this.sommeEtNombre = sommeEtNombre;
    }
    /**
     * Créer une instance vide sans valeur nul sur les attributs
     * @param colSomme : la liste des colonnes sommées
     */
    public void initialise(String[] colSomme) {
        resultat = new ClassMAPTable[0];

        if (colSomme == null) {
            sommeEtNombre = new double[1];
            sommeEtNombre[0] = 0;
        } else {
            sommeEtNombre = new double[colSomme.length + 1];
            for (int i = 0; i < colSomme.length; i++) {
                sommeEtNombre[i] = 0;
            }
            sommeEtNombre[colSomme.length] = 0;
        }
    }
}
