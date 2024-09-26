package bean;
import utilitaire.Parametre;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import java.sql.*;
import java.util.*;
import constante.*;
import java.util.*;
import bean.*;
import java.lang.reflect.Field;

public abstract class GenUtil implements java.io.Serializable
{
  String nomTable = null;
  String nomCle=null;
  int nbTuple = 0;
  Vector vect = null;
  String msgErreur = null;
  int nbChampaMapper=0;

  Champ[] champ=null;
  private String nomClasse="";
  public void utiliserChampBase()
  {
    Table t=new Table(getNomTable());
    champ=t.getChamp();
  }

    public void utiliserChampBase(Connection c)
  {
    Table t=new Table(getNomTable(),c);
    champ=t.getChamp();
  }

  public Object findByPk(ClassMAPTable c)
  {

    return null;
  }
  public Object[] findByPk(String[] pk,ClassMAPTable c)
  {
    return null;
  }
  public void modifNomTable(String n)
  {
    this.nomTable=n;
  }
  public void utiliserChampClasse()
  {
    getChampFromClass();
  }
  public GenUtil(String nomtable,String nomClasse)
  {
    this.setNomClasse(nomClasse);
    this.setNomTable(nomtable);
    this.utiliserChampBase();
  }
    public GenUtil(String nomtable,String nomClasse, Connection c)
  {
    this.setNomClasse(nomClasse);
    this.setNomTable(nomtable);
    this.utiliserChampBase(c);
  }
  public GenUtil(String nomtable,String nomClasse,int nbCM)
  {
    this.setNomClasse(nomClasse);
    this.setNomTable(nomtable);
    this.setNbChampaMapper(nbCM);
    //Table t=new Table(nomtable);
    //t.setNomTable(nomtable);
    this.utiliserChampBase();
  }


  public GenUtil(String table)
  {
    Table t=new Table(table);
    nomTable=t.getNomTable();
    champ=t.getChamp();
  }

  public GenUtil(String table,java.sql.Connection c)
  {
    Table t=new Table(table,c);
    nomTable=t.getNomTable();
    champ=t.getChamp();
  }

  public void getChampFromClass()
  {
    try {

      Class f=Class.forName(this.getNomClasse());

      ClassMAPTable cmp=(ClassMAPTable)f.newInstance();

      champ=new Champ[cmp.getNombreChamp()];
      int nb=0;
      if(this.getNbChampaMapper()==0)
        nb=cmp.getNombreChamp();
      else
        nb=this.getNbChampaMapper();

      for(int j = 0; j < nb; j++)
      {
        champ[j]=new Champ(cmp.getFieldList()[j]);
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public void setChamp(Champ[] cham)
  {
    champ=cham;
  }
  public Champ[] getChamp()
  {
    return champ;
  }
  public GenUtil(){
  }

  public void setNomTable(String obj)
  {
    Table t=new Table(obj);
    this.modifNomTable(t.getNomTable());
    if (champ==null)
    {
      if ((this.getNomClasse().compareToIgnoreCase("")==0)||(this.getNomClasse()==null))
      {
        champ=t.getChamp();
      }
      else
      {
        getChampFromClass();
      }
    }
  }
  public void setNomTable(String obj,java.sql.Connection c)
  {
    Table t=new Table(obj,c);
    nomTable=t.getNomTable();
    if(champ==null)
    {
      if ((this.getNomClasse().compareToIgnoreCase("")==0)||(this.getNomClasse()==null))
      {
        champ=t.getChamp();
      }
      else
      {
        getChampFromClass();
      }
    }
  }

  public void setNomCle(String cle)
  {
    nomCle=cle;
  }

  public String getNomTable()
  {
    return nomTable;
  }

  public String getNomCle()
  {
    return nomCle;
  }


/*
  *@Permet de rechercher un tuple à partir d'un reference
  *@Elle ne sera valide que si la cle est un String
*/
  public Object[] rechercher(String ref)
  {
    return (rechercher(1,ref));
  }
/*
  *@Permet de rechercher tous les tuples à partir
*/
  public Object[] rechercherAll()
  {
    return rechercher(1,"%");
  }
/*
  *@Permet de rechercher un tuple à partir d'un reference
  *@Il faut indiquer le nom de la colonne, son type, et sa valeur
*/
  public Object[] rechercher(String nomColonne,String type,Object valeur)
  {
    UtilDB util = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    Connection c=null;
    try
    {
      util = new UtilDB();
      c = util.GetConn();
      String param = "SELECT * FROM " + getNomTable() + " WHERE "+nomColonne+" like ?";
      //st=c.prepareStatement(param);
      st = c.prepareStatement(param,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

      //          if((type.compareToIgnoreCase("Varchar")==0)||(type.compareToIgnoreCase("char")==0))
      if((type.compareToIgnoreCase("Varchar2")==0)||(type.compareToIgnoreCase("char")==0))
      {
        String temp=(String)valeur;
        if (temp.compareTo("")==0)
          valeur="%";
        st.setString(1, (String)valeur);
      }

      //if((type.compareToIgnoreCase("Integer")==0)||(type.compareToIgnoreCase("Counter")==0))
      if((type.compareToIgnoreCase("Number")==0)||(type.compareToIgnoreCase("Counter")==0))
        st.setInt(1, Utilitaire.stringToInt((String)valeur));
      if(type.compareToIgnoreCase("Date")==0)
        st.setDate(1, (java.sql.Date)valeur);
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (SQLException s)
    {
      System.out.println("RechercheExecution11 par Reference " + s.getMessage());
      return null;
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
        if (c!=null) c.close();
        util.close_connection();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
/*
  *@Methode qui permet de faire une recherche mono-critere sur une table
*/
  public Object[] rechercher(int numChamp,Object valeur,Connection c) throws Exception
  {
    UtilDB util = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    String nomColonne=champ[numChamp-1].getNomColonne();
    String type=champ[numChamp-1].getTypeColonne();
    try
    {
      String param = "SELECT * FROM " + getNomTable() + " WHERE "+nomColonne+" like ?";
      //st=c.prepareStatement(param);
      st = c.prepareStatement(param,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

      //          if((type.compareToIgnoreCase("Varchar")==0)||(type.compareToIgnoreCase("char")==0))
      if((type.compareToIgnoreCase("Varchar2")==0)||(type.compareToIgnoreCase("char")==0))
      {
        String temp=(String)valeur;
        if (temp.compareTo("")==0)
          valeur="%";
        st.setString(1, (String)valeur);
      }

      //if((type.compareToIgnoreCase("Integer")==0)||(type.compareToIgnoreCase("Counter")==0))
      if((type.compareToIgnoreCase("Number")==0)||(type.compareToIgnoreCase("Counter")==0))
      {
        if (Utilitaire.stringToInt((String)valeur)!=0)
        {
          st.setInt(1, Utilitaire.stringToInt((String)valeur));
        }
        else
          st.setString(1,"%");
      }

      //if(type.compareToIgnoreCase("Datetime")==0)
      if(type.compareToIgnoreCase("Date")==0)
      {
        java.sql.Date date1 = Utilitaire.string_date("dd/MM/yyyy",(String)valeur);
        st.setDate(1, date1);
      }
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (SQLException s)
    {
      s.printStackTrace();
      System.out.println("RechercheExecution2222 par Reference " + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      if(rs!=null)rs.close();
      if (st!=null) st.close();
    }
  }
  public Object[] rechercher(int numChamp,Object valeur)
  {
    UtilDB util = null;
    Connection c=null;
     try
    {
       c=new UtilDB().GetConn();
       return rechercher(numChamp,valeur,c);
    }
    catch (Exception s)
    {
      s.printStackTrace();
      System.out.println("RechercheExecution2222 par Reference " + s.getMessage());
      return null;
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
/*
  *@Methode qui permet de faire une recherche mono-critere sur une table, avec une intervalle
*/
  public Object[] rechercher(int numChamp,Object valeur,Object valeurSup)
  {
    UtilDB util = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    Connection c=null;
    String nomColonne=champ[numChamp-1].getNomColonne();
    String type=champ[numChamp-1].getTypeColonne();
    try
    {
      util = new UtilDB();
      c = util.GetConn();
      String param = "SELECT * FROM " + getNomTable() + " WHERE "+nomColonne+" <= ? and "+nomColonne+" >=?";
      //st=c.prepareStatement(param);
      st = c.prepareStatement(param,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);


      //	if((type.compareToIgnoreCase("Integer")==0)||(type.compareToIgnoreCase("Counter")==0))
      if((type.compareToIgnoreCase("Number")==0)||(type.compareToIgnoreCase("Counter")==0))
      {
        if (Utilitaire.stringToInt((String)valeur)!=0)
        {
          st.setInt(1, Utilitaire.stringToInt((String)valeur));
          st.setInt(2, Utilitaire.stringToInt((String)valeurSup));
        }
        else
          st.setString(1,"%");
      }

      //if(type.compareToIgnoreCase("Datetime")==0)
      if(type.compareToIgnoreCase("Date")==0)
      {
        java.sql.Date date1 = Utilitaire.string_date("dd/MM/yyyy",(String)valeur);
        java.sql.Date date2 = Utilitaire.string_date("dd/MM/yyyy",(String)valeurSup);
        st.setDate(1, date1);
        st.setDate(2, date2);
      }
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (SQLException s)
    {
      System.out.println("RechercheExecution333 par Reference " + s.getMessage());
      return null;
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
        if (c!=null) c.close();
        util.close_connection();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
 /*
  *@ Methode permettant de gerer les objets envoyes en fonction du num de Page
 */
  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere,int numPage)
  {
    return rechercher(numChamp,valeur,apresWhere,null,null,numPage);
  }
 /*
  *@ Methode permettant de gerer les objets envoyes en fonction du num de Page
  *@numChampDaty et daty representent les intervalles
 */
  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere,Connection c,int numChampDaty[],String daty[],int numPage)
  {
    UtilDB util = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    int indiceInf = 0;
    int indiceSup=0;
    int nbParPage=Parametre.getNbParPage();

    try
    {
      int[] num={1};String val[]={"1"};
      indiceInf = (nbParPage*(numPage-1))+1;
      indiceSup=nbParPage*(numPage);
      String param="select * from (select "+getListeChampPage()+",rowNum as r from (select * from " + getNomTable() + " where "+makeWhere(numChamp,valeur)+" "+makeWhereIntervalle(numChampDaty,daty)+" "+apresWhere+")) where r between "+indiceInf+" and "+indiceSup;
      st=preparerStatement(param,numChamp,valeur, c);
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (Exception s)
    {
      System.out.println("Erreur dans Recherche multicritere rechercher(int[] numChamp,Object[] valeur,String apresWhere,Connection c,int numChampDaty[],String daty[],int numPage " + s.getMessage());
      return null;
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }

 /*
  *@ Methode permettant de gerer les objets envoyes en fonction du num de Page
 */
  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere,int numChampDaty[],String daty[],int numPage)
  {
    UtilDB util = null;
    Connection c=null;
    int indiceInf = 0;
    int indiceSup=0;

    try
    {
      util = new UtilDB();
      c = util.GetConn();
      return rechercher(numChamp,valeur,apresWhere,c,numChampDaty,daty,numPage);
    }
    catch (Exception s)
    {
      s.printStackTrace();
      return null;
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
 /*
  *@Surdef ny
 */
  public Object[] rechercher(int[] numChamp,Object[] valeur,int numPage)
  {
    return rechercher(numChamp,valeur,"",numPage);
  }
/*
  *@Methode qui permet de faire une recherche multi-critere sur une table
  *@numChamp[] donne une liste de numero de champ et valeur[] donne
  *@une liste de valeur.
*/
  public Object[] rechercher(int[] numChamp,Object[] valeur) throws Exception
  {
    try {
      return rechercher(numChamp,valeur,"");
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
/*
  *@Methode qui permet de faire une recherche multi-critere sur une table
  *@numChamp[] donne une liste de numero de champ et valeur[] donne
  *@une liste de valeur. apresWhere est utilise s'il y a une syntaxe SQL
  *@apres le mot where ex:group by
*/
  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere)  throws Exception
  {
    UtilDB util = null;
    Connection c=null;
    try
    {
      util = new UtilDB();
      c = util.GetConn();
      return this.rechercher(numChamp,valeur,apresWhere,c);
    }
    catch (Exception s)
    {
      s.printStackTrace();
      throw s;
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
  public Object[] rechercherVect(int[] numChamp,Object[] valeur,String apresWhere)  throws Exception
  {
    UtilDB util = null;
    Connection c=null;
    try
    {
      util = new UtilDB();
      c = util.GetConn();
      return this.rechercherVect(numChamp,valeur,apresWhere,c);
    }
    catch (Exception s)
    {
      System.out.println("Erreur dans Recherche multicritere 2" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }

  public Object[] rechercherVect(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c) throws Exception
  {
    ResultSet rs=null;
    Vector v=null;
    try {
      rs = this.rechercherResult(numChamp,valeur,apresWhere,c);
      v=this.resultToVect(rs);
    }
    catch (Exception ex) {
      throw new Exception();
    }
    finally
    {
      if(rs!=null)rs.close();
    }
    return resultatGen(v);
  }
  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    try
    {
      String param = "SELECT * FROM " + getNomTable() + " WHERE "+makeWhere(numChamp,valeur)+" "+apresWhere;
        System.out.println("param = " + param);
        st=preparerStatement(param,numChamp,valeur, c);
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (SQLException s)
    {
      System.out.println("Erreur dans Recherche multicritere 1" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        e.printStackTrace();
        throw e;
      }
    }
  }
  public ResultSet rechercherResult(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    try
    {
      String param = "SELECT * FROM " + getNomTable() + " WHERE "+makeWhere(numChamp)+" "+apresWhere;
      //System.out.println(param);
      //st = c.prepareStatement(param,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      st = c.prepareStatement(param);
      for (int j=0;j<valeur.length;j++)
      {

        String type=champ[numChamp[j]-1].getTypeColonne();
        //if((type.compareToIgnoreCase("Varchar")==0)||(type.compareToIgnoreCase("char")==0))
        if((type.compareToIgnoreCase("Varchar2")==0)||(type.compareToIgnoreCase("char")==0))
        {

          String temp=(String)valeur[j];
          if ((temp.compareTo("")==0)||temp==null)
            valeur[j]="%";
          st.setString(j+1, (String)valeur[j]);
          //System.out.println(j+"="+(String)valeur[j]);
        }

        //if((type.compareToIgnoreCase("Integer")==0)||(type.compareToIgnoreCase("Counter")==0))
        if((type.compareToIgnoreCase("Number")==0)||(type.compareToIgnoreCase("Counter")==0))
        {
          if ((((String)valeur[j]).compareTo("%")==0)||(((String)valeur[j]).compareTo("")==0)||(valeur[j]==null)){
            st.setString(j+1, "%");
            //System.out.println(j+"="+"%");
          }else
            st.setInt(j+1, Utilitaire.stringToInt((String)valeur[j]));
          //System.out.println(j+"="+(String)valeur[j]);
        }

        //if(type.compareToIgnoreCase("Datetime")==0)
        if(type.compareToIgnoreCase("Date")==0)
        {
          java.sql.Date date1 = Utilitaire.string_date("dd/MM/yyyy",(String)valeur[j]);
          st.setDate(j+1, date1);
          //   System.out.println(j+"="+(String)valeur[j]);
        }
      }

      rs = st.executeQuery();
      return rs;
    }
    catch (SQLException s)
    {
      System.out.println("Erreur dans Recherche multicritere 1" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }

  public int calculNombreLigne(int[] numChamp,Object[] valeur,String apresWhere) throws Exception
  {
    java.sql.Connection c=null;
    try
    {
      UtilDB util = new UtilDB();
      c = util.GetConn();
      c.setAutoCommit(true);
      return calculNombreLigne(numChamp, valeur,apresWhere,c);
    }
    catch (Exception s)
    {
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL Calcul Nombre"+ e.getMessage());
      }
    }
  }


  public int calculNombreLigne(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    try
    {
      String param = "SELECT count(*) FROM " + getNomTable() + " WHERE "+makeWhere(numChamp)+" "+apresWhere;
      st=preparerStatement(param,numChamp,valeur, c);
      rs = st.executeQuery();
      rs.next();
      return rs.getInt(1);
    }
    catch (SQLException s)
    {
      System.out.println("Erreur dans Recherche multicritere " + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
  public String getListeChampPage()
  {
    String retour=null;
    retour=this.getChamp()[0].getNomColonne();
    for(int i=1;i<this.getChamp().length;i++)
    {
      retour=retour+","+this.getChamp()[i].getNomColonne();
    }
    return retour;
  }
 /*
  *@Methode qui permet d'avoir le mot where qu'on va utiliser dans la requete complexe
 */
  public String makeWhere(int[] numChamp,Object[] val)
  {
    String retour=null;
    retour="UPPER("+champ[numChamp[0]-1].getNomColonne()+") like UPPER(?)";
    for(int i=1;i<numChamp.length;i++)
    {
      String mot=(String)val[i];
      if((val[i]==null)||(mot.compareTo("%"))==0||(mot.compareTo(""))==0)
      {
      }
      else
      {
          retour=retour+" AND UPPER("+champ[numChamp[i]-1].getNomColonne()+") like UPPER(?)";
      }
    }
    if(retour==null) return "1<2";
    return retour;
  }
 /*
  *@Methode qui permet d'avoir le mot where qu'on va utiliser dans la requete complexe
 */
  public String makeWhereIntervalle(int[] numChamp,String[] val)
  {
    String retour="";
    if((numChamp==null)||(val==null))return "";
    val=Utilitaire.remplacerNullParBlanc(val,"");
    for(int i=0;i<numChamp.length;i++)
    {
      if ((val[2*i].compareTo("")>0) & (val[(2*i)+1].compareTo("")==0))
        retour=retour+" AND "+champ[numChamp[i]-1].getNomColonne()+" >= '"+val[2*i]+"'";
      if ((val[2*i].compareTo("")==0) & (val[(2*i)+1].compareTo("")>0))
        retour=retour+" AND "+champ[numChamp[i]-1].getNomColonne()+" <= '"+val[(2*i)+1]+"'";
      if ((val[2*i].compareTo("")>0) & (val[(2*i)+1].compareTo("")>0))
        retour=retour+" AND "+champ[numChamp[i]-1].getNomColonne()+" >= '"+val[2*i]+"'"+" AND "+champ[numChamp[i]-1].getNomColonne()+" <= '"+val[(2*i)+1]+"'";
    }
    return retour;
  }
 /*
  *@Methode qui permet d'avoir le mot where qu'on va utiliser dans la requete complexe
 */
  public String makeWhere(int[] numChamp)
  {
    String retour=null;
    retour="UPPER("+champ[numChamp[0]-1].getNomColonne()+") like UPPER(?)";
    for(int i=1;i<numChamp.length;i++)
    {
      retour=retour+" AND UPPER("+champ[numChamp[i]-1].getNomColonne()+") like UPPER(?)";
    }
    return retour;
  }
  public Vector resultToVect(ResultSet rs) throws Exception
  {
    try {
      Vector obj=new Vector();
      Vector result=new Vector();
      ResultSetMetaData md=rs.getMetaData();
      int nbColonne=md.getColumnCount();
      while (rs.next())
      {
        for(int i=0;i<nbColonne;i++)
        {
          obj.add(rs.getString(i));
        }
        result.add(obj);
      }
      return result;
    }
    catch (SQLException ex) {
      throw new Exception();
    }
  }

  public Object[] resultatGen(Vector v)
  {
    return null;
  }

  public abstract Object[] resultatLimit (int numBloc,ResultSet rs);
  public void setNomClasse(String nomClasse) {
    this.nomClasse = nomClasse;
  }
  public String getNomClasse() {
    return nomClasse;
  }
  public Object[] rechercher(int[] numChamp,Object[] valeur,int[]numChampDaty,String[]daty) throws Exception
  {
    try {
      return rechercher(numChamp,valeur,"",numChampDaty,daty);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    }
  }
  public String getColonneSomme(String [] listeCol)
  {
    if((listeCol==null)||(listeCol.length==0))
      return "0";
    String retour="sum("+listeCol[0]+")";
    for(int i=1;i<listeCol.length;i++)
    {
      retour=retour+",sum("+listeCol[i]+")";
    }
    return retour;
  }
  public double[] calculSommeNombre(int[] numChamp,Object[] valeur,String apresWhere, int numChampDaty[],String daty[],String[] nomColSomme) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    Connection c=null;
    try
    {
      c = new UtilDB().GetConn();
      return calculSommeNombre(numChamp,valeur,apresWhere,c,numChampDaty, daty,nomColSomme);
    }
    catch (SQLException s)
    {
      System.out.println("Erreur dans calculSommeNombre multicritere 1" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL calculSommeNombre "+ e.getMessage());
      }
    }
  }
   /*
   Retourne la liste des sommes à calculer et le nombre de ligne total à la fin
   */
  public double[] calculSommeNombre(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c,int numChampDaty[],String daty[],String[] nomColSomme) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    try
    {

      String param = "SELECT "+getColonneSomme(nomColSomme)+",COUNT(*) FROM " + getNomTable() + " WHERE "+makeWhere(numChamp,valeur)+" "+makeWhereIntervalle(numChampDaty,daty)+" "+apresWhere;
      //if (getNomTable().compareToIgnoreCase("Tiers")==0)System.out.println("param"+param);
      st=preparerStatement(param,numChamp,valeur, c);
      rs = st.executeQuery();
      double []retour=null;
      if (nomColSomme==null)
        retour=new double[2];
      else
        retour=new double[nomColSomme.length+1];
      rs.next();
      int i=0;
      for(i=0;i<retour.length-1;i++)
      {
        retour[i]=rs.getDouble(i+1);
      }
      retour[i]=rs.getDouble(i+1);
      return retour;
    }
    catch (SQLException s)
    {
      s.printStackTrace();
      System.out.println("Erreur dans Recherche multicritere 1" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
  public PreparedStatement preparerStatement(String param,int[]numChamp,Object[] valeur,Connection c) throws Exception
  {
    PreparedStatement st = c.prepareStatement(param,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    for (int j=0,k=0;j<valeur.length;j++)
    {
      String type=champ[numChamp[j]-1].getTypeColonne();
      if((type.compareToIgnoreCase("Varchar2")==0)||(type.compareToIgnoreCase("char")==0))
      {
        if ((valeur[j]==null)||(((String)valeur[j]).compareTo("%")==0)||(((String)valeur[j]).compareTo("")==0))
        {
          if(j==0)
          {
            st.setString(k+1, "%");
            k++;
          }
        }
        else
        {
          st.setString(k+1, (String)valeur[j]);
          k++;
        }
      }
      if((type.compareToIgnoreCase("Number")==0)||(type.compareToIgnoreCase("Counter")==0))
      {
        if ((((String)valeur[j]).compareTo("%")==0)||(((String)valeur[j]).compareTo("")==0)||(valeur[j]==null)){
          if(j==0)
          {
            st.setString(k+1, "%");
            k++;
          }
        }else
        {
          st.setInt(k+1, Utilitaire.stringToInt((String)valeur[j]));
          k++;
        }
      }
      if((type.compareToIgnoreCase("Float")==0))
      {
        if ((((String)valeur[j]).compareTo("%")==0)||(((String)valeur[j]).compareTo("")==0)||(valeur[j]==null)){
          if(j==0)
          {
            st.setString(k+1, "%");
            k++;
          }
        }else
        {
          st.setDouble(k+1, Utilitaire.stringToDouble((String)valeur[j]));
          k++;
        }
      }
      if(type.compareToIgnoreCase("Date")==0)
      {
        if ((((String)valeur[j]).compareTo("%")==0)||(((String)valeur[j]).compareTo("")==0)||(valeur[j]==null)){
          if(j==0)
          {
            st.setString(k+1, "%");
            k++;
          }
        }
        else
        {
          java.sql.Date date1 = Utilitaire.string_date("dd/MM/yyyy",(String)valeur[j]);
          st.setDate(k+1, date1);
          k++;
        }
      }
    }
    return st;
  }


  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere, java.sql.Connection c,int numChampDaty[],String daty[]) throws Exception
  {
    PreparedStatement st = null;
    ResultSet rs = null;
    try
    {

      String param = "SELECT * FROM " + getNomTable() + " WHERE "+makeWhere(numChamp,valeur)+" "+makeWhereIntervalle(numChampDaty,daty)+" "+apresWhere;
      st=preparerStatement(param,numChamp,valeur, c);
      rs = st.executeQuery();
      return resultatGen(rs);
    }
    catch (SQLException s)
    {
      System.out.println("Erreur dans Recherche multicritere 1" + s.getMessage());
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if(rs!=null)rs.close();
        if (st!=null) st.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }


  public Object[] rechercher(int[] numChamp,Object[] valeur,String apresWhere,int[] numChampDaty,String[]daty)  throws Exception
  {
    UtilDB util = null;
    Connection c=null;
    try
    {
      util = new UtilDB();
      c = util.GetConn();
      return this.rechercher(numChamp,valeur,apresWhere,c,numChampDaty,daty);
    }
    catch (Exception s)
    {
      System.out.println("Erreur dans Recherche multicritere 2" + s.getMessage());
      s.printStackTrace();
      throw new Exception(s.getMessage());
    }
    finally
    {
      try
      {
        if (c!=null) c.close();
      }
      catch(SQLException e)
      {
        System.out.println("Erreur Fermeture SQL RechercheExecution "+ e.getMessage());
      }
    }
  }
  public Object[] resultatGen(ResultSet rs) {
    List list = new ArrayList();
    Vector v=new Vector();
    Class cls=null;
    try {
      cls = Class.forName(getNomClasse());
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Object[] aret=null;
    try {
      ClassMAPTable temp=(ClassMAPTable)cls.newInstance();
      Field[] fields = temp.getFieldList();
      while (rs.next()) {
//        if (this.getNomTable().compareToIgnoreCase("EngagementLcComplet")==0)System.out.println("Taille enreg 1");
        ClassMAPTable obj = (ClassMAPTable)cls.newInstance();
        obj.setNomTable(this.getNomTable());
        for (int i = 0; i < fields.length; i++) {
          Field fld = fields[i];
          switch (obj.getTypeMAPField(fld)) {

            case 0: // '\0'
              setValField(obj, fld, new Boolean(rs.getBoolean(fld.getName())));
              break;

            case 1: // '\001'
              setValField(obj, fld,new Byte(rs.getByte(fld.getName())));
              break;

            case 2: // '\002'
              setValField(obj, fld,new Short(rs.getShort(fld.getName())));
              break;
            case 3: // '\003'
              setValField(obj, fld,new Integer(rs.getInt(fld.getName())));
              break;

            case 4: // '\004'
              setValField(obj, fld,new Long(rs.getLong(fld.getName())));
              break;

            case 5: // '\005'
              setValField(obj, fld,new Float(rs.getFloat(fld.getName())));
              break;

            case 6: // '\006'
              setValField(obj, fld,new Double(rs.getDouble(fld.getName())));
              break;
            case 10: // '\n'
              setValField(obj, fld, rs.getString(fld.getName()));
              break;

            case 21: // '\025'
              setValField(obj, fld, rs.getDate(fld.getName()));
              break;

            case 33: // '!'
              setValField(obj, fld,new Integer(rs.getInt(fld.getName())));
              break;

          }
        }
        v.add(obj);

      }

      aret = (Object[]) java.lang.reflect.Array.newInstance(cls.newInstance().getClass(), v.size());
      v.copyInto(aret);
      return aret;
    } catch (Exception ex) {
      ex.printStackTrace();
      ;
    } finally {
      //return list.toArray();
      return aret;
    }
  }

  public Object setValField(Object obj,Field f,Object value)
  {
    try {
      String nomMethode="set"+Utilitaire.convertDebutMajuscule(f.getName());
      Class[] paramT={f.getType()};
      Object[] args={value};
      Object o=obj.getClass().getMethod(nomMethode,paramT).invoke(obj,args);
      return o;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

  }

  //end naina
  public int getNbChampaMapper() {
    return nbChampaMapper;
  }
  public void setNbChampaMapper(int nbChampaMapper) {
    this.nbChampaMapper = nbChampaMapper;
  }

public ResultatEtSomme rechercherPage(int[] numChamp,Object[] valeur,String apresWhere,Connection c,int numChampDaty[],String daty[],int numPage,String nomColSom[]) throws Exception

 {

    Object[] ret=rechercher(numChamp,valeur, apresWhere, c, numChampDaty, daty, numPage);

    double[] sommeNombre=calculSommeNombre(numChamp,valeur,apresWhere,c,numChampDaty,daty,nomColSom);

    return new ResultatEtSomme(ret,sommeNombre);

 }

 public ResultatEtSomme rechercherPage(int[] numChamp,Object[] valeur,String apresWhere,int numChampDaty[],String daty[],int numPage,String nomColSom[]) throws Exception
{
   Connection c=null;
  try {
    c=new UtilDB().GetConn();
    return(rechercherPage(numChamp,valeur, apresWhere, c, numChampDaty, daty, numPage,nomColSom));
  }
  catch (Exception ex) {
    return null;
  }

  finally{

    if(c!=null)c.close();

  }

 }


}