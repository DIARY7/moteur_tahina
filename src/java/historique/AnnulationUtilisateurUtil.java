/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

import bean.GenUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Cette classe extends {@link GenUtil}
 * @author BICI
 */
public class AnnulationUtilisateurUtil extends GenUtil {

  public AnnulationUtilisateurUtil() {
    super("AnnulationUtilisateur");
  }
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    /**@todo Implement this bean.GenUtil abstract method*/
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }

  /**
   * Obtenir les résultats de base de données sous forme de tableau d'objet 
   * @param rs un ensemble de résultats de base de données
   * @return liste des objets 
   */
  public Object[] resultatGen (ResultSet rs)
  {
	  try
	  {
		  int i = 0, k = 0;
		  AnnulationUtilisateur temp = null;
		  Vector vect = new Vector();
		  while(rs.next())
		  {                          
			  temp = new AnnulationUtilisateur(rs.getString(1),rs.getString(2),rs.getDate(3));
			  vect.add(i,temp);
			  i++;
		  }

		  AnnulationUtilisateur []retour = new AnnulationUtilisateur[i];

		  while (k < i)
		  {
			  retour[k] = (AnnulationUtilisateur)(vect.elementAt(k));
			  k++;
		  }
		  return retour;
	  }
	  catch (Exception s)
	  {
		  System.out.println("AnnulationUtilisateur "+s.getMessage());
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
	      System.out.println("Erreur Fermeture SQL AnnulationUtilisateur "+ e.getMessage());
	    }
	  }
  }
}