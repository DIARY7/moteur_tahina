package bean;


import bean.ClassMAPTable;

/**
 * Objet de mapping pour stocker
 * les resultats d'une recherche groupée par colonne
 * 
 * 
 * @author BICI
 * @version 1.0
 */

public class ResultatGroupe extends ClassMAPTable {

  private String nomColonne;
  private double somme;
  private double nombre;
  public ResultatGroupe() {
  }
  public ResultatGroupe(String nomCol,double som,double nb) {
    this.setNomColonne(nomCol);
    this.setSomme(som);
    this.setNombre(nb);
  }

  public String getAttributIDName() {
    /**@todo Implement this bean.ClassMAPTable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getAttributIDName() not yet implemented.");
  }
  public String getTuppleID() {
    /**@todo Implement this bean.ClassMAPTable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getTuppleID() not yet implemented.");
  }
  /**
   * 
   * @param nomColonne nom du colonne de groupage
   */
  public void setNomColonne(String nomColonne) {
    this.nomColonne = nomColonne;
  }

  public String getNomColonne() {
    return nomColonne;
  }
  public void setSomme(double somme) {
    this.somme = somme;
  }
  public double getSomme() {
    return somme;
  }
  public void setNombre(double nombre) {
    this.nombre = nombre;
  }
  public double getNombre() {
    return nombre;
  }
}