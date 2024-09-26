package bean;

import java.sql.*;

/**
 * Objet de configuration sur les informations extra d'une table
 * 
 * 
 * @author BICI
 * 
 */
public class CaractTable extends bean.ClassMAPTable {

  private String id;
  private String nomTable;
  private String nomSeq;
  private String nomProc;
  private String nomFille;

  public CaractTable() {
  }
  public String getAttributIDName() {
          return "id";
  }

  public String getTuppleID() {
          return id;
	}
  public CaractTable(String i,String nomT,String nomS,String nomProc) {
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return id;
  }

  public void setNomTable(String nomTable) {
    this.nomTable = nomTable;
  }
  /**
   * Nom de la table 
   */
  public String getNomTable() {
    return nomTable;
  }

  public void setNomSeq(String nomSeq) {
    this.nomSeq = nomSeq;
  }
  /**
   * 
   * @return nom de la séquence à utiliser pour la table donnée
   */
  public String getNomSeq() {
    return nomSeq;
  }
  public void setNomProc(String nomProc) {
    this.nomProc = nomProc;
  }
  /**
   * 
   * @return nom de la procédure utilisée pour la géneration de valeur ID pour la table donnée
   */
  public String getNomProc() {
    return nomProc;
  }
  public void setNomFille(String nomFille) {
    this.nomFille = nomFille;
  }
  /**
   * 
   * @return nom de la table fille
   */
  public String getNomFille() {
    return nomFille;
  }
}