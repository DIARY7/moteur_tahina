 package bean;

import java.sql.*;
import java.util.Vector;

/**
 * @deprecated
 * Cette classe permet la facilité à l'accès de {@link bean.TypeObjetSupprime}. 
 */

public class TypeObjetSupprimeUtil extends GenUtil {


	private static final long serialVersionUID = 4805940850425269963L;

public TypeObjetSupprimeUtil() {
  }
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    /**@todo Implement this bean.GenUtil abstract method*/
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }

 /**
   * Obtenir les résultats de base de données sous forme de tableau d'objet  
   * @param rs  un ensemble de résultats de base de données
   * @return liste d'Objet
   */
  public Object[] resultatGen (ResultSet rs)
  {
          try
          {
                  int i = 0, k = 0;
                  TypeObjetSupprime temp = null;
                  Vector vect = new Vector();
                  while(rs.next())
                  {
                    //String id,String idobjet,String iduser,java.sql.Date daty, String rmq,String utilBDD
                    temp = new TypeObjetSupprime (rs.getString(1),rs.getString(2),rs.getString(3),rs.getDate(4),rs.getString(5),"");
                          vect.add(i,temp);
                          i++;
                  }

                 TypeObjetSupprime []retour = new TypeObjetSupprime[i];

                  while (k < i)
                  {
                          retour[k] = (TypeObjetSupprime)(vect.elementAt(k));
                          k++;
                  }
                  return retour;
          }
          catch (Exception s)
          {
                  System.out.println("Resultat TypeObjetSupprime:"+s.getMessage());
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
              System.out.println("Erreur Fermeture SQL Type Objet Supprime"+ e.getMessage());
            }
          }
  }
}
