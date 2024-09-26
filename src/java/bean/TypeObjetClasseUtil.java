package bean;

import java.sql.*;
import java.util.Vector;

public class TypeObjetClasseUtil extends GenUtil {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3722295495031696148L;
	
	public TypeObjetClasseUtil() {
	}
	
	public TypeObjetClasse[] findTypeObjetClasse(String nomTable, String id,
            String val, String desc, String idParent) throws Exception {
		try {
			TypeObjetClasseUtil to = new TypeObjetClasseUtil();
			to.setNomTable(nomTable);
			int[] a = {
					1, 2, 3, 4};
			String[] valeurs = new String[a.length];
			valeurs[0] = id;
			valeurs[1] = val;
			valeurs[2] = desc;
			valeurs[3] = idParent;
			TypeObjetClasse[] retour = (TypeObjetClasse[]) to.rechercher(a, valeurs,
			"ORDER BY VAL ASC");
			return retour;
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    /**@todo Implement this bean.GenUtil abstract method*/
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }
  public Object[] resultatGen (ResultSet rs)
  {
          try
          {
                  int i = 0, k = 0;
                  TypeObjetClasse temp = null;
                  Vector vect = new Vector();
                  while(rs.next())
                  {
                      temp = new TypeObjetClasse (rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
                          vect.add(i,temp);
                          i++;
                  }

                  TypeObjetClasse []retour = new TypeObjetClasse[i];

                  while (k < i)
                  {
                          retour[k] = (TypeObjetClasse)(vect.elementAt(k));
                          k++;
                  }
                  return retour;
          }
          catch (Exception s)
          {
                  System.out.println("Resultat TypeObjetClasse"+s.getMessage());
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
              System.out.println("Erreur Fermeture SQL Type TypeObjetClasse "+ e.getMessage());
            }
          }
  }
}
