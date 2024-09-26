package historique;

import bean.*;
import java.sql.*;
import java.util.*;


/**
 * Utile pour Obtenir les résultats de base de données sous forme de d'objet 
 * Cette classe extends {@link GenUtil} 
*/

public class ObjetUtil extends GenUtil {

  /**
   * Constructeur
   */
  public ObjetUtil() {
    super("objet");
  }
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }

  /**
   * Obtenir les résultats de base de données sous forme de tableau d'objet 
   * @param rs un ensemble de résultats de base de données
   * @return liste des objets 
   */
  public Object[] resultatGen(ResultSet rs) {
    try
     {
        int i = 0, k = 0;
        Objet temp = null;
        Vector vect = new Vector();
        while(rs.next())
        {
          temp = new Objet (rs.getString(1),rs.getString(2));
          vect.add(i,temp);
          i++;
        }

        Objet []retour = new Objet[i];

        while (k < i)
        {
                retour[k] = (Objet)(vect.elementAt(k));
                k++;
        }
        return retour;
     }
     catch (Exception s)
     {
             System.out.println("Resultat "+s.getMessage());
             return null;
     }
     finally
     {
       try
       {
             if (rs!=null) rs.close();
       }
       catch(SQLException e)
       {
         System.out.println("Erreur Fermeture SQL Operation Util "+ e.getMessage());
       }
           }
  }
}