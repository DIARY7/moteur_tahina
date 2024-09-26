/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import utilitaire.UtilDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe avec les configuration hors metiers.
 * Attention: les configurations de métier devraient être stockées en base ou dans un fichier de constante pas dans cette classe.
 * 
 * @author BICI
 */
public class Parametre
{
    /**
     * Constructeur par défaut
     */
    public Parametre()
    {
    }
    /**
     * Initialisé les paramètres à partir de valeur en base de données
     */
    public static void getParametre()
    {
//        UtilDB util = null;
        Connection c = null;
        PreparedStatement st = null;
        try
        {
//            util = new UtilDB();
            c = new UtilDB().GetConn();
            String param = "SELECT * FROM parametre WHERE ref LIKE ?";
            st = c.prepareStatement(param);
            st.setString(1, "1");
            ResultSet rs = st.executeQuery();
            rs.next();
            nbParPage = rs.getInt(3);
            exercice=rs.getInt(2);
        }
        catch(SQLException s)
        {
            System.out.println("Parametre non recu ".concat(String.valueOf(String.valueOf(s.getMessage()))));
        }
        finally
        {
            try
            {
                if(st != null)
                    st.close();
                if(c != null)
                    c.close();
//                util.close_connection();
            }
            catch(SQLException e)
            {
                System.out.println("Erreur Fermeture SQL Parametre ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }
    /**
     * Mettre à jour des paramètres
     * @return nombre de ligne modifié en base
     */
    public static int updParametre()
    {
//        UtilDBJDBC util = null;
        Connection c = null;
        PreparedStatement st = null;
        try
        {
            try
            {
//                util = new UtilDBJDBC();
                c = new UtilDB().GetConn();
                String param = "update parametre set reductionTrafic=?,nbPage=? ref LIKE ?";
                st = c.prepareStatement(param);
                st.setInt(1, reductionTrafic);
                st.setInt(2, nbParPage);
                st.setInt(4, 1);
                int i = st.executeUpdate();
                return i;
            }
            catch(SQLException s)
            {
                System.out.println("Parametre non recu ".concat(String.valueOf(String.valueOf(s.getMessage()))));
            }
            int j = 0;
            return j;
        }
        finally
        {
            try
            {
                if(st != null)
                    st.close();
                if(c != null)
                    c.close();
//                util.close_connection();
            }
            catch(SQLException e)
            {
                System.out.println("Erreur Fermeture SQL Parametre ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }
    /**
     * 
     * @return nom du jndi de la base de données
     */
    public static String getNomJndiDB()
    {
        return nomJndiDB;
    }

    public static void setNomJndiDB(String nomJnd)
    {
        nomJndiDB = nomJnd;
    }

    public static String getPwd()
    {
        return pwd;
    }

    public static void setPwd(String pwdd)
    {
        pwd = pwdd;
    }

    public static String getServeurApp()
    {
        return serveurApp;
    }

    public static void setServeurApp(String serveurAppl)
    {
        serveurApp = serveurAppl;
    }

    public static String getUser()
    {
        return user;
    }

    public static void setUser(String usere)
    {
        user = usere;
    }

    public static String getMonographieJNDI()
    {
        return monographieJNDI;
    }

    public static void setMonographieJNDI(String mono)
    {
        monographieJNDI = mono;
    }

    public static String getEntrepriseJNDI()
    {
        return entrepriseJNDI;
    }

    public static void setEntrepriseJNDI(String mono)
    {
        entrepriseJNDI = mono;
    }

    public static String getTransportJNDI()
    {
        return transportJNDI;
    }

    public static void setTransportJNDI(String mono)
    {
        transportJNDI = mono;
    }

    public static String getSanteJNDI()
    {
        return santeJNDI;
    }

    public static void setSanteJNDI(String mono)
    {
        santeJNDI = mono;
    }

    public static String getTelecomJNDI()
    {
        return telecomJNDI;
    }

    public static void setTelecomJNDI(String mono)
    {
        telecomJNDI = mono;
    }

    public static void setReductionTrafic(int red)
    {
        reductionTrafic = red;
    }

    public static int getReductionTrafic()
    {
        return reductionTrafic;
    }

    public static void setNbParPage(int page)
    {
        nbParPage = page;
    }
    /**
     * 
     * @return nombre d'élèment par page - 25 statiquement si aucun paramètre en base
     */
    public static int getNbParPage()
    {
        return nbParPage;
    }

    static String ref = "1";
    static String user = "haja";
    static String pwd = "haja";
    static String serveurApp = "localhost";
    static String nomJndiDB = "Port";
    static String monographieJNDI = "MonographieJNDI";
    static String entrepriseJNDI = "Entreprise";
    static String transportJNDI = "TransportRemote";
    static String santeJNDI = "CentresanteRemote";
    static String telecomJNDI = "Telecom";
    static String trasnportJNDI = "Transport";
    static String ministereJNDI = "Ministere";
    static String serviceadmJNDI = "Serviceadm";
    static String infoetcommunicJNDI = "Infoetcommunic";
    static int reductionTrafic = 20;
    static int nbParPage = 25;
    static int exercice=2009;
    public static final int maxRecherche=50;

}
