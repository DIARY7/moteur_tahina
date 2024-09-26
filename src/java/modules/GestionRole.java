package modules;

import bean.CGenUtil;
import config.Table;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import utilisateur.Restriction;
import utilitaire.UtilDB;

/**
 * Utilité pour facilité la gestion des restrictions pour des roles donnés.
 * 
 * @author Jetta
 */
public class GestionRole {

    /**
     * @deprecated
     * controler si une action est restreinte ou pas
     * @param idrole role concerné
     * @param permission action à controler
     * @param table table concernée par la restriction
     * @param direc direction concerné
     * @param c connexion ouverte à la base de données
     * @return 1 si restreinte sinon 0
     * @throws Exception
     */
    public int  testRestriction(String idrole, String permission, String table,String direc,Connection c) throws Exception {

        try {
            Restriction r = new Restriction();
            //TypeObjet to=new TypeObjet();
           // r.setIdrole(idrole);

            
           // r.setIdaction(act);
            r.setTablename(table);
            String aw=" AND idaction='"+permission+"' and IDDIRECTION='"+direc+"'";
            // System.out.print("awhere="+aw);
            Restriction[] res = (Restriction[]) CGenUtil.rechercher(r, null, null, c, aw);
           
            if (res!=null&&res.length != 0) {
                return 1;
            }
            return 0;
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * @deprecated
     * ne fais rien
     * @param con
     * @return
     * @throws Exception
     */
    public String[] getAllTAble(Connection con) throws Exception {
        try {
            String req = "SELECT table_name FROM matable";
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(req);
            Vector v = new Vector();
            while (res.next()) {
                String st = res.getString(1);
                v.add(st);
            }
            String[] valin = new String[v.size()];
            v.copyInto(valin);
            return valin;
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Ajouter une restriction pour un role donné sur une action sur différentes tables
     * @param valtab liste de table à restreindre
     * @param idrole id du role concerné
     * @param act id de l'action à restreindre
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void ajoutrestriction(String[] valtab, String idrole, String act, Connection c) throws Exception {
        try {

            for (int i = 0; i < valtab.length; i++) {
                Restriction r = new Restriction(idrole, act, valtab[i]);
                //System.out.println(idrole + " " + act + " " + valtab[i]);
                r.construirePK(c);
                r.insertToTableWithHisto("1060", c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }
    /**
     * Ajouter une restriction pour un role donné dans une direction sur une action sur différentes tables
     * @param valtab liste de table à restreindre
     * @param idrole id du role concerné
     * @param act id de l'action à restreindre
     * @param c connexion ouverte à la base de données
     * @param depart direction concernée
     * @throws Exception
     */
    public void ajoutrestriction(String[] valtab, String idrole, String act,String depart,Connection c) throws Exception {
        try {

            for (int i = 0; i < valtab.length; i++) {
                Restriction r = new Restriction(idrole, act, valtab[i], null, null, depart);
                r.construirePK(c);
                r.insertToTableWithHisto("1060", c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }
    /**
     * @deprecated ne fais rien
     * @param valtab
     * @param idrole
     * @param ajout
     * @param modif
     * @param suppr
     * @param read
     * @param req
     * @param c
     * @throws Exception
     */
    public void modifRestriction(String[] valtab, String idrole, String ajout, String modif, String suppr, String read, HttpServletRequest req, Connection c) throws Exception {

    }
    /**
     * Supprimer ou mettre à jour une restriction
     * @param tab table concerné par la restriction, null si on veut juste supprimer
     * @param rest id de la restriction à modifier
     * @param role id du role concerné, null si on veut juste supprimer
     * @param act id de l'action, null si on veut juste supprimer
     * @param req contexte HTTP(pas important)
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void updateRestriction(String tab, String rest, String role, String act, HttpServletRequest req, Connection c) throws Exception {
        try {
            Restriction r = new Restriction();
            r.setId(rest);
            r.deleteToTable();
            if (act != null) {
                r = new Restriction(role, act, tab);
                r.insertToTableWithHisto("UPDATE RESTRICTION");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * Récuperer l'ensemble des tables auquelles j'ai full access
     * @param role id de mon role
     * @param adr id de la direction où je suis
     * @return liste de table auquelles j'ai accès
     * @throws Exception
     */
    public config.Table[] getListeTable(String role,String adr) throws Exception {
       Connection con = null;
        try {
            con=new UtilDB().GetConn();
            String req = "SELECT * FROM matable where 1<2 and  table_name not in(select r.tablename from restriction r where r.idrole='" + role + "' and r.iddirection='"+adr+"')";
            //System.out.print(req);
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(req);
            Vector v = new Vector();
            while (res.next()) {
                Table st = new Table();
                st.setTable_name(res.getString(1));
                v.add(st);
            }
            Table[] valin = new Table[v.size()];
            v.copyInto(valin);
            return valin;
        } catch (SQLException e) {
            throw e;
        }
        finally{
            if(con!=null)con.close();
        }
    }

}
