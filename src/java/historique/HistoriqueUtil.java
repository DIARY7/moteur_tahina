package historique;
import bean.*;
import java.sql.*;
import java.util.*;
import utilitaire.*;
import java.util.*;


public class HistoriqueUtil extends GenUtil implements java.io.Serializable,java.lang.Cloneable
{
	public HistoriqueUtil()
	{
	super("Historique","historique.MapHistorique");
        System.out.println("ca y ets");
          //super("HISTORIQUE");
		//nomTable = "HISTORIQUE_ORD";
		//makeChamp();
	}
/*
*@Permet de retourner une exception pour test
*/
        public int testException() throws Exception
        {
          try {
            if(1>2)
            return 0;
            throw new Exception("HistoriqueUtil");
          }
          catch (Exception ex) {
            throw new Exception("Exception aty @ "+ex.getMessage());
          }
	}
/*
*@Permet de rechercher les historiques d'un ordre
*/
	public MapHistorique[] rechercheById(String ref)
	{
		MapHistorique [] retour=(MapHistorique [])rechercher (4,ref);
		return retour;
	}
/*
*@Permet de rechercher les historiques d'un acteur
*/
	public MapHistorique[] rechercheActeur(String user)
	{
		MapHistorique [] retour=(MapHistorique [])rechercher (6,user);
		return retour;
	}

	public MapHistorique[] rechercheHeureAction(String action,String ref)
	{
                try {
                  int []a={4,5};
                  String []val=new String[a.length];
                  val[0]=new String(ref);
                  val[1]=new String(action);
                  MapHistorique [] retour=(MapHistorique [])rechercher (a,val);
                  return retour;
                }
                catch (Exception ex) {
                  return null;
                }
	}
	/*
	*@Permet d'avoir la liste des historiques d'un utilisateur de ref pour une date donnee.
	*/
	public MapHistorique[] rechercheByRefDate(String refUser,String daty)
	{
                try {
                  int []a={2,6};
                  String []val=new String[a.length];
                  val[0]=daty;
                  val[1]=refUser;
                  MapHistorique[] retour=(MapHistorique[])rechercher (a,val);
                  return retour;
                }
                catch (Exception ex) {
                  return null;
                }
	}

        public MapHistorique[] rechercheHisto(String refUser,String objet,String action,String daty)
        {
                try {
                  int []a={2,4,5,6};
                  String []val=new String[a.length];
                  val[0]=new String(daty);
                  val[1]=new String(objet);
                  val[2]=new String(action);
                  val[3]=new String(refUser);
                  MapHistorique [] retour=(MapHistorique [])rechercher (a,val);
                  return retour;
                }
                catch (Exception ex) {
                  return null;
                }
	}

	public MapHistorique[] recherche(int [] numChamp,Object[] valeur)
	{
                try {
                  MapHistorique[] retour=(MapHistorique[])rechercher (numChamp,valeur);
                  return retour;
                }
                catch (Exception ex) {
                  return null;
                }
	}

	public Object[] resultatGen (ResultSet rs)
	{
		try
		{
			int i = 0, k = 0;
			MapHistorique temp = null;
			Vector vect = new Vector();

			while(rs.next())
			{
				temp = new MapHistorique(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
				vect.add(i,temp);
				i++;
			}

			MapHistorique []retour = new MapHistorique[i];

			while (k < i)
			{
				retour[k] = (MapHistorique)(vect.elementAt(k));
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