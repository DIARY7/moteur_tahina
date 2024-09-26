package historique;
import bean.*;
import java.sql.*;
import java.util.*;
import utilitaire.*;
import java.util.*;


public class RoleUtil extends GenUtil implements java.io.Serializable,java.lang.Cloneable
{
	public RoleUtil()
	{
		super("roles");
	}


/*
*@Permet de rechercher les roles par leur ref
*/
	public MapRoles rechercheById(int id)
	{
		MapRoles retour=((MapRoles [])rechercher (1,String.valueOf(id)))[0];
		return retour;
	}

/*
*@Permet de rechercher les roles par leur Description
*/
	public MapRoles rechercheByDesc(String desc)
	{
		MapRoles retour=((MapRoles [])rechercher (2,desc))[0];
		return retour;
	}



	public Object[] resultatGen (ResultSet rs)
	{
		try
		{
			int i = 0, k = 0;
			MapRoles temp = null;
			Vector vect = new Vector();

			while(rs.next())
			{
				temp = new MapRoles(rs.getString(1),rs.getString(2));
				vect.add(i,temp);
				i++;
			}

			MapRoles []retour = new MapRoles[i];

			while (k < i)
			{
				retour[k] = (MapRoles)(vect.elementAt(k));
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
                    System.out.println("Erreur Fermeture SQL HistoriqueOrdUtil "+ e.getMessage());
                  }
                }
	}
	public Object[] resultatLimit (int numBloc,ResultSet rs)
	{
		return null;
	}

}