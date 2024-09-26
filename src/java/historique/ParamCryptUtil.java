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
 *
 * @author MSI
 */

public class ParamCryptUtil extends GenUtil {
	
	public ParamCryptUtil(){
		super("ParamCrypt");
	}
	
	public Object[] resultatGen(ResultSet rs) {
		try{
			   int i = 0, k = 0;
			   ParamCrypt temp = null;
			   Vector vect = new Vector();
			   while(rs.next()){
			     // String idutil,String idprofil,String nom,String passwd,String code
			     temp = new ParamCrypt(rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getString(4));
			     vect.add(i,temp);
			     i++;
			   }

			   ParamCrypt []retour = new ParamCrypt[i];

			   while (k < i){
			     retour[k] = (ParamCrypt)(vect.elementAt(k));
			     k++;
			   }
			   return retour;
			 }
			 catch (Exception s){
			   System.out.println("Recherche ParamCrypt "+s.getMessage());
			   return null;
			 }
			 finally{
			   try{
			     if (rs!=null) rs.close();
			   }
			   catch(SQLException e){
			     System.out.println("Erreur Fermeture SQL ParamCrypt "+ e.getMessage());
			   }
			 }
		
	}

	public Object[] resultatLimit(int numBloc, ResultSet rs) {
		throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
	}

}
