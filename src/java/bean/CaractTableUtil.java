package bean;

import java.sql.*;

/**
 * Utilitaire pour fetch les valeurs dans caractTable.
 * 
 * Exemple:
 * 
 * On veut prendre les extra informations de la table "Facture".
 * 
 * <pre>{@code
 *   CaractTable[] caractTables = (CaractTable[]) new CaractTableUtil().rechercher(2,"Facture");
 *   String nomSequenceFacture = caractTables[0].getNomSeq();
 *   String nomProcedureFacture = caractTables[0].getNomProc();
 *   String nomTableFille = caractTables[0].getNomFille(); 
 * }
 * </pre>
 * 
 * @author BICI
 */

public class CaractTableUtil extends GenUtil {

  public CaractTableUtil() {
    super("caractTable","bean.CaractTable");
  }
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    /**@todo Implement this bean.GenUtil abstract method*/
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }
}