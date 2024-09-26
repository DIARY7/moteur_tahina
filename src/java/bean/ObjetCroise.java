package bean;

/**
 * Objet permettant de calculer des objets croisés à partir d'objets
 * <p>
 * Prenant par exemple la classe <code>Depense</code> 
 * avec comme attribut <code>Id,typeDepense,montant, produit, departement</code>. On peut faire un objet croisé comme-ci:
 * </p>
 * <pre>
 *  Depense[] depenses = (Depense[]) CGenUtil.rechercher(new Depense(),null,null,"");
 *  String colonne = "departement";
 *  String ligne = "typeDepense";
 *  String calcul = "montant";
 *  ObjetCroise resultat = new ObjetCroise(depenses,colonne,ligne, calcul,"depense");
 * </pre>
 * <p>
 *  L'objet resultat va avoir plusieurs attributs(à voir en bas) 
 *  qui vont vous permettre de construire tableau croisé.
 * </p>
 * @author unascribed
 * @version 1.0
 */

public class ObjetCroise {

    double[][]croise=null;
    String nomCol;
    String nomLigne;
    String[] valCol=null;
    String[] valLigne=null;
    Object[] aCroise;
    String nomTable;
    String nomCentre;
    double[] sommeColonne=null;
    double[] sommeLigne=null;
    double sommeTotal=0;
    /**
     * Constructeur
     * @param obj liste des objets à valeur pour construire les valeurs croisées
     * @param nomCol attribut representant la colonne de croisement
     * @param nomLigne attribut representant la ligne de croisement
     * @param centre attribut representant l'attribut de somme
     * @param table table en base de données de l'objet
     * @throws Exception
     */
    public ObjetCroise(Object[] obj,String nomCol,String nomLigne,String centre,String table) throws Exception
    {
      this.setACroise(obj);
      this.setNomCol(nomCol);
      this.setNomLigne(nomLigne);
      if (nomCol.compareToIgnoreCase(nomLigne)==0)
        throw new Exception("Colonne et ligne identique");
     this.setNomTable(table);
      this.setNomCentre(centre);
      this.setValColBdd();
     this.calculCroise();
      this.calculSommeColonne();
      this.setValColBdd();
      this.calculSommeLigne();

      this.calculSommeTotal();
    }
    /**
     * Calcul la somme totale de tous les objets pour l'attribut donné au constructeur
     */
    public void calculSommeTotal()
    {
      for (int i=0;i<sommeLigne.length;i++)
      {
        sommeTotal=sommeTotal+sommeLigne[i];
      }
    }
    /**
     * Calculer la somme pour chaque ligne du tableau croisé
     *  avec au préalable la liste des lignes déjà obtenues
     */
    public void calculSommeLigne()
    {
      sommeLigne=new double[valLigne.length];
      for (int i=0;i<valLigne.length;i++)
      {
        sommeLigne[i]=0;
        for(int k=0;k<valCol.length;k++)
        {
          sommeLigne[i]=sommeLigne[i]+croise[i][k];
        }
      }
    }
    /**
     * Calculer la somme pour chaque colonne du tableau croisé
     * avec au préalable la liste des colonnes déjà obtenues et les valeurs du tableau aussi
     */
    public void calculSommeColonne()
    {
      sommeColonne=new double[valCol.length];
      for (int i=0;i<valCol.length;i++)
      {
        sommeColonne[i]=0;
        for(int k=0;k<valLigne.length;k++)
        {
          sommeColonne[i]=sommeColonne[i]+croise[k][i];
        }
      }
    }
    /**
     * Calculer les valeurs du tableau croisé
     * avec au préalable la liste des colonnes et des lignes déjà obtenues
     */
    public void calculCroise()
    {
      croise=new double[valLigne.length][valCol.length];
      for (int i=0;i<valLigne.length;i++)
      {
        for(int j=0;j<valCol.length;j++)
        {
          croise[i][j]=getSommeUnique(valLigne[i],valCol[j]);
        }
      }
    }

    /**
     * @deprecated
     * Concatener toutes les valeurs de l'attribut somme des objets sources
     * @return calcul objet croisé
     */
    public String getSortie()
    {
      String resultat="";
      double retour=0;
      try {
        for (int i=0;i<aCroise.length;i++)
        {
          String valColAi=(String) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomCol),null).invoke(aCroise[i],null));
          String valLigneAi=(String) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomLigne),null).invoke(aCroise[i],null));


            //Double temp=(Double) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomCentre),null).invoke(aCroise[i],null));
            resultat =resultat + (String) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomCentre),null).invoke(aCroise[i],null));


        }
        return resultat;
      }
      catch (Exception ex) {
        ex.printStackTrace();
       return resultat;
      }
    }

    /**
     * Prendre pour tous les objets de source la somme pour la valeur d'une ligne et d'une colonne
     * @param valLi valeur de l'attribut ligne
     * @param valCo valeur de l'attribut colonne
     * @return
     */
    public double getSommeUnique(String valLi,String valCo)
    {
      double retour=0;
      try {
        for (int i=0;i<aCroise.length;i++)
        {
          String valColAi=(String) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomCol),null).invoke(aCroise[i],null));
          String valLigneAi=(String) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomLigne),null).invoke(aCroise[i],null));
          if(valColAi.compareToIgnoreCase(valCo)==0 && valLigneAi.compareToIgnoreCase(valLi)==0)
          {
            Double temp=(Double) (aCroise[i].getClass().getMethod("get"+utilitaire.Utilitaire.convertDebutMajuscule(nomCentre),null).invoke(aCroise[i],null));
            retour=retour+temp.doubleValue();
          }
        }
        return retour;
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return 0;
      }
    }
    /**
     * Prendre en base de données les valeurs possibles distinctes de la colonne et de la table
     */
    public void setValColBdd()
    {
		try{
      valCol=utilitaire.Utilitaire.getvalCol(nomTable,nomCol);
      valLigne=utilitaire.Utilitaire.getvalCol(nomTable,nomLigne);
	  }catch(Exception ex){
	  ex.printStackTrace();
	  }
    }
  /**
   * 
   * @return objets sources pour le calcul de tableau croisé
   */
  public Object[] getACroise() {
    return aCroise;
  }
  /**
   * 
   * @return resultat du tableau croisé
   */
  public double[][] getCroise() {
    return croise;
  }
  /**
   * 
   * @return nom de la colonne utilisée pour la construction du tableau
   */
  public String getNomCol() {
    return nomCol;
  }
  /**
   * 
   * @return nom de la ligne utilisée pour la construction du tableau
   */
  public String getNomLigne() {
    return nomLigne;
  }
  /**
   * 
   * @return liste des valeurs en colonnes
   */
  public String[] getValCol() {
    return valCol;
  }
  /**
   * 
   * @return liste des valeurs en ligne
   */
  public String[] getValLigne() {
    return valLigne;
  }
  /**
   * 
   * @param nomLigne nom de l'attribut à utiliser à la construction du tableau
   */
  public void setNomLigne(String nomLigne) {
    this.nomLigne = nomLigne;
  }
  /**
   * 
   * @param nomCol nom de l'attribut à utiliser en colonne à la construction du tableau
   */
  public void setNomCol(String nomCol) {
    this.nomCol = nomCol;
  }
  /**
   * 
   * @param croise liste à deux dimensions des valeurs de calcul
   */
  public void setCroise(double[][] croise) {
    this.croise = croise;
  }
  /**
   * 
   * @param aCroise objet source pour la construction du tableau
   */
  public void setACroise(Object[] aCroise) {
    this.aCroise = aCroise;
  }
  /**
   * 
   * @return nom de la table en base
   */
  public String getNomTable() {
    return nomTable;
  }
  public void setNomTable(String nomTable) {
    this.nomTable = nomTable;
  }
  /**
   * 
   * @return nom de l'attribut pour les calculs
   */
  public String getNomCentre() {
    return nomCentre;
  }
  public void setNomCentre(String nomCentre) {
    this.nomCentre = nomCentre;
  }
  /**
   * 
   * @return liste des sommes en ligne
   */
  public double[] getSommeLigne() {
    return sommeLigne;
  }
  /**
   * 
   * @return liste des sommes en colonne
   */
  public double[] getSommeColonne() {
    return sommeColonne;
  }
  /**
   * 
   * @return somme total
   */
  public double getSommeTotal() {
    return sommeTotal;
  }
}