package bean;

import java.sql.*;
import java.util.Vector;

/**
 * <p>Title: Gestion des recettes </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class UnionIntraTableUtil extends GenUtil {

  public UnionIntraTableUtil() {
  }
  public Object[] resultatLimit(int numBloc, ResultSet rs) {
    /**@todo Implement this bean.GenUtil abstract method*/
    throw new java.lang.UnsupportedOperationException("Method resultatLimit() not yet implemented.");
  }
  public UnionIntraTable[] findUnionIntraTable(String nomTable, String id, String id1,String id2,String rem,String mont1,String mont2,String eta,String apresW)
      throws Exception
  {
      try
      {
          //UnionIntraTableUtil to = new UnionIntraTableUtil();
          this.setNomTable(nomTable);
          int a[] = {1,2,3,4,6};
          String val[] = {id,id1,id2,rem,eta};
          int b[]={5};
          String[] valInt={mont1,mont2};
          this.utiliserChampBase();
          UnionIntraTable atypeobjet[] = (UnionIntraTable[])rechercher(a, val,apresW,b,valInt);
          for(int i=0;i<atypeobjet.length;i++)
            atypeobjet[i].setNomTable(nomTable);
          return atypeobjet;
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
          throw new Exception("Erreur dans UnionIntraTableUtil "+ex.getMessage());
      }
    }
  public Object[] resultatGen(ResultSet rs){
        try
        {
            int i = 0;
            int k = 0;
            UnionIntraTable temp = null;
            Vector vect = new Vector();
            while(rs.next())
            {
                temp = new UnionIntraTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getDouble(5),rs.getInt(6));
                vect.add(i, temp);
                i++;
            }
            UnionIntraTable retour[] = new UnionIntraTable[i];
            vect.copyInto(retour);
            return retour;
        }
        catch(Exception s)
        {
          s.printStackTrace();
            System.out.println("Erreur dans "+this.getClass().getName()+" "+s.getMessage());
            return null;
        }
    finally
    {
        try
        {
            if(rs != null)
                rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("Erreur Fermeture SQL Type Objet "+this.getClass().getName()+" "+e.getMessage());
        }
        }
  }
}