package utilitaire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * 
 * @author Bici
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.TimeZone;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * 
 * Classe permettant de créer une connexion (session d'accès) aux bases de données. Cette classe regroupe les fonctions nécessaires pour accéder aux bases de données.
 * 
 */
public class UtilDB {
    /**
     * java.sql.Connection: classe d'accès aux bases de données par défaut du langage Java
     */
    Connection conn;
    /**
     * 
     */
    public static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
    /**
     * Url de base
     */
    public static final String PROVIDER_URL = "localhost:1099";

    /**
     * 
     * Contexte actuel du projet en cours
     * @return InitialContext
     * @throws NamingException 
     */
    public static InitialContext getContext() throws NamingException {
        Properties env = new Properties();
        env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        env.put("java.naming.provider.url", PROVIDER_URL);
        env.put("java.naming.rmi.security.manager", "yes");
        env.put("java.naming.factory.url.pkgs", "org.jboss.naming");
        return new InitialContext(env);
    }

    /**
     * 
     * @param user : Oracle database user
     * @param passWOracle : Password of Oracle database user
     */
    public UtilDB(String user, String passWOracle) {
        conn = null;
        try {
            InitialContext jndiContext = getContext();
            DataSource ds = (DataSource) jndiContext.lookup("java:VVFinances");
            conn = ds.getConnection(user, passWOracle);
        } catch (NamingException ne) {
            System.out.println("UtilDB Erreur Naming : ".concat(String.valueOf(String.valueOf(ne.getMessage()))));
        } catch (SQLException se) {
            System.out.println("UtilDB Erreur Connexion : ".concat(String.valueOf(String.valueOf(se.getMessage()))));
        }
    }

    /**
     * Constructeur vide
     */
    public UtilDB() {
        conn = null;
    }

    /**
     * Fonction à appeler dans GetConn au cas où le format de date de l'hote lançant le programme n'est pa dd/mm/yyyy
     * @param con : Connexion ouverte à la base de données
     * @throws Exception
     */
    private void setDefaulteDateFormat(Connection con)throws Exception{
        if(con==null) return;
        Statement stmt=null;
        try {
            stmt = con.createStatement();
            String req="ALTER SESSION SET NLS_DATE_FORMAT = 'dd/mm/yyyy'";
            stmt.executeUpdate(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(stmt!=null){
                stmt.close();
            }
        }
    }

    /**
     * Méthode retournant un objet de type java.sql.Connection, crée une connection à une base de données. Cette classe de connexion d'accès aux bases de données est multibase; il faut modifier les valeurs contenues dans DriverManager.getConnection
     * <br/>Si postgres, décommenter Class.forName("org.pstgresql.Driver") et utiliser jdbc:postgresql://<ip>:<port>/<database> comme uri de connexion
     * <br/>Si oracle, décommenter Class.forName("oracle.jdbc.driver.OracleDriver") et utiliser jdbc:oracle:thin:@<ip>:<port>/<database> comme uri de connexion
     * @return java.sql.Connection
     */
    public Connection GetConn() {
        Connection conn = null;
        try {
            Context jndiContext = new InitialContext();
            DataSource ds = (DataSource) jndiContext.lookup("java:jboss/datasources/paskDS");
            conn = ds.getConnection();
        } catch (Exception ne) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Baghdad");
                TimeZone.setDefault(timeZone);
                Class.forName("oracle.jdbc.driver.OracleDriver");
                PropertiesLoader loader = PropertiesLoader.getInstance();
                Properties properties = loader.getProperties();
                String url = properties.getProperty("apj.connection.url");
                String user = properties.getProperty("apj.connection.user");
                String password = properties.getProperty("apj.connection.password");
                //System.out.println("bonsoir");
                conn = DriverManager.getConnection(url, user, password);
                setDefaulteDateFormat(conn);
                Connection connection2 = conn;
                return connection2;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("UtilDB Erreur Connexion : ".concat(String.valueOf(String.valueOf(ex.getMessage()))));
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
        return conn;
    }

    /**
     * Modifie la valeur autoCommit de la connection en true
     */
    public void commitON() {
        try {
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(" ** Erreur commit on: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    /**
     * Modifie la valeur autoCommit de la connection en false
     */
    public void commitOFF() {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferme la connection
     */
    public void close_connection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validation de la transaction en cours
     */
    public void valider() {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Annulation de la transaction en cours
     */
    public void annuler() {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
