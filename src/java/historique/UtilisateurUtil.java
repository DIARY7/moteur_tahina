/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

import bean.CGenUtil;
import bean.GenUtil;
import utilitaire.UtilDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import utilitaire.Utilitaire;

/**
 *
 * @author MSI
 */

public class UtilisateurUtil extends GenUtil implements java.io.Serializable,java.lang.Cloneable
{
  public UtilisateurUtil()
  {
    super("Utilisateur");
  }

/*
  *@Permet de rechercher un utilisateur selon user et passe
*/
  public MapUtilisateurServiceDirection testeValide(String user,String passe) throws Exception
  {
    Connection c=null;
    try {
        c=new UtilDB().GetConn();

        this.setNomTable("UtilisateurValide");
        MapUtilisateurServiceDirection[] temp = null;
        int []a={1};
        String [] valeur=new String [a.length];
        if((user.compareToIgnoreCase("")==0)||(passe.compareToIgnoreCase(""))==0||(passe.compareToIgnoreCase("%"))==0) throw new Exception("Erreur de Login ");
        valeur[0]=user;
        MapUtilisateurServiceDirection rech=new MapUtilisateurServiceDirection();
        rech.setNomTable("UtilisateurValide");
        temp = (MapUtilisateurServiceDirection[]) CGenUtil.rechercher(rech, null, null, c, String.format(" and loginuser like '%s'", user));
        if (temp.length>0)
        {
            ParamCrypt[] pc = (ParamCrypt[])new ParamCryptUtil().rechercher(4,temp[0].getTuppleID(),c);

            if(pc.length==0) {
                throw new Exception("Pas de cryptage associe");
            }

            String passCrypt=Utilitaire.cryptWord(passe, pc[0].getNiveau(),pc[0].getCroissante());

            int u=temp[0].getPwduser().compareTo(passCrypt);
            if(u==0) return temp[0];
            else throw new Exception("Erreur de Login ");
        }
        else{
            throw new Exception("Erreur de Login ");
        }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    }
    finally{
        if(c!=null){
            c.close();
        }
    }
  }
  /**
   * tester si un nom d'utilisateur et un mot de passe correspondent Ã  un utilisateur en base
   * @param nomTable : table de l'utilisateur
   * @param user : nom d'utilisateur
   * @param passe : mot de passe non cryptÃ© de l'utilisateur
   * @return utilisateur correspondant au nom d'utilisateur et mot de passe
   * @throws Exception
   */
  public MapUtilisateur testeValide(String nomTable,String user,String passe) throws Exception
 {
   Connection c=null;
   try {
     c=new UtilDB().GetConn();
     this.setNomTable(nomTable);
     MapUtilisateur[] temp = null;
     int []a={2};
     String [] valeur=new String [a.length];
     if((user.compareToIgnoreCase("")==0)||(passe.compareToIgnoreCase(""))==0||(passe.compareToIgnoreCase("%"))==0) throw new Exception("Erreur de Login ");
     valeur[0]=user;
     //valeur[1]=passe;
     temp=((MapUtilisateur[])rechercher(a,valeur));
     if (temp.length>0)
     {
       //if(temp[0].getLoginuser().compareToIgnoreCase("dg")==0)return temp[0];
       ParamCrypt[] pc = (ParamCrypt[])new ParamCryptUtil().rechercher(4,temp[0].getTuppleID(),c);
       if(pc.length==0) throw new Exception("Pas de cryptage associe");
       String passCrypt= Utilitaire.cryptWord(passe, pc[0].getNiveau(),pc[0].getCroissante());
       System.out.println(temp[0].getLoginuser() +" : " +passCrypt);
       if(temp[0].getPwduser().compareTo(passCrypt)==0)
         return temp[0];
       else throw new Exception("Erreur de Login ");
     }

     else
       throw new Exception("Erreur de Login ");
   }
   catch (Exception ex) {
       ex.printStackTrace();
     throw new Exception(ex.getMessage());
   }
   finally
   {
     if(c!=null)c.close();
   }
  }
 /**
  * rechercher un utilisateur par identifiant
  * @param refUser : identifiant de l'utilisateur
  * @return
  */
  public MapUtilisateur rechercheByRef(String refUser)
  {
    MapUtilisateur  retour=((MapUtilisateur [])rechercher (1,refUser))[0];
    return retour;
  }
 /*
  *@Permet de rechercher des utilisateurs par son type
 */
  public MapUtilisateur[] recherche()
  {
    MapUtilisateur[]  retour=(MapUtilisateur [])rechercher (1,"%");
    return retour;
  }
 /*
  *@Permet de rechercher les utilisateurs selon son role
 */
  public MapUtilisateur[] rechercheByRole(int role)
  {
    try {
      MapUtilisateur[]  retour=null;
      int []a={7};
      Object [] valeur=new Object [a.length];
      if (role==0)
      {
        retour=(MapUtilisateur [])rechercher (1,"%");
      }
      else
      {
        valeur[0]=String.valueOf(role);
        retour=(MapUtilisateur [])rechercher (a,valeur);
      }
      return retour;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * GÃ©nerer une liste d'utilisateur Ã  partir d'un result set
   * @param rs pointeur de rÃ©sultat vers la base de donnÃ©es
   * @return liste d'utilisateur gÃ©nÃ©rÃ©s
   */
  public Object[] resultatGen (ResultSet rs)
  {
    try
    {
      int i = 0, k = 0;
      MapUtilisateur temp = null;
      Vector vect = new Vector();

      while(rs.next())
      {
        temp = new MapUtilisateur(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
        vect.add(i,temp);
        i++;
      }

      MapUtilisateur []retour = new MapUtilisateur[i];

      while (k < i)
      {
        retour[k] = (MapUtilisateur)(vect.elementAt(k));
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