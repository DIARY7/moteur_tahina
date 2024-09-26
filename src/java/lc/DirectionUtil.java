package lc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import bean.GenUtil;

public class DirectionUtil extends GenUtil {
	
	public DirectionUtil(){
		super("direction","lc.Direction");
	}
	
	public Object[] resultatGen(ResultSet rs) {
		try {
			int i = 0;
			Direction temp = null;
			Vector vect = new Vector();
			while (rs.next()) {
				temp = new Direction(rs.getString(1), rs.getString(2), rs.getString(3),rs.getDouble(4), rs.getString(5));
				vect.add(i, temp);
				i++;
			}

			Direction[] retour = new Direction[i];
			vect.copyInto(retour);
			return retour;
			
		} catch (Exception s) {
			System.out.println("Recherche Direction " + s.getMessage());
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println("Erreur Fermeture SQL Direction " + e.getMessage());
			}
		}
	}

	public Object[] resultatLimit(int numBloc, ResultSet rs) {
		throw new java.lang.UnsupportedOperationException(
				"Method resultatLimit() not yet implemented.");
	}


}
