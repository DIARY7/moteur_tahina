package bean;



import java.util.Hashtable;
import java.sql.*;
import java.util.Vector;
import java.lang.reflect.Field;
import utilitaire.UtilDB;
import java.util.Enumeration;
import java.util.List;
import java.util.Arrays;
import bean.*;
/**
 * Classe permettant de lier les colonnes dans un objet de mapping et un 
 * @author unascribed
 * @version 1.0
 */
public class ListeColonneTable {

    static Hashtable listeColPTable = new Hashtable();
    /**
     * Fonction permettant de prendre tous les attributs d'une classe enfant de ClassMapTable 
     * @param e : objet héritant de classMaPTable 
     * @return liste des attributs déclarés et hérités d'un objet classMapTable
     * @throws Exception
     */
    public static Field[] getFieldListeHeritage(ClassMAPTable e)throws Exception  {
        Field[] prim = e.getClass().getDeclaredFields();
        //System.out.println("Le nombre de champ de base prim "+e.getClass().getName()+" = "+prim.length);
        if (e.getClass().getSuperclass().getName().endsWith("ClassMAPTable") == true) {
            //return prim;
        }
        Vector listeF = new Vector();
        List temp = Arrays.asList(prim);
        listeF.addAll(temp);
        Class p = e.getClass().getSuperclass();
        while (1 < 2) {
            if (p.getName().endsWith("ClassMAPTable") == true){
                //if(e.getNomTable().compareToIgnoreCase("op_complet")==0)System.out.println("MODE DE E====="+e.getMode());
                if(e.getMode().compareToIgnoreCase("groupe")==0)
                {
                    //Field nbPargroupe=p.getDeclaredField("nombrepargroupe");
                    //listeF.add(nbPargroupe);
                }
                break;
            }
            Field second[] = p.getDeclaredFields();
            //System.out.println("Le nombre de champ de base second "+p.getName()+"  = "+second.length);
            listeF.addAll(Arrays.asList(second));
            p = p.getSuperclass();
        }
        
        Field[] retour = new Field[listeF.size()];
        listeF.copyInto(retour);
        return retour;
    }
    /**
     * Permet de prendre et enregistrer une fois l'intersection des attributs déclarés/hérités et de la table associé à un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion à la base de données, si connexion non existante, mettre nul
     * @throws Exception
     */
    public static void ajouterAlaListe(ClassMAPTable e, Connection c) throws Exception {
        boolean estOuvert=false;
        try
        {
            String nomTable = e.getNomTable();
            boolean testDedans = listeColPTable.containsKey(nomTable+e.getClassName());
            if (testDedans == false) {
                if(c==null)
                {
                    c=new UtilDB().GetConn();
                    estOuvert=true;
                }
                Vector r = getChampFromTable(nomTable, c);          
                Field fieldlist[] = getFieldListeHeritage(e);
                if (e.getNomTable().compareToIgnoreCase("LOG_DEMANDE_DEPL_INFO")==0)System.out.println("Taille du r "+fieldlist.length+" vector="+r.size());
                Champ[] retour = new Champ[r.size()];
                Vector intersect = new Vector();
                for (int i = 0; i < r.size(); i++) {
                    retour[i] = (Champ) r.get(i);
                    for (int k = 0; k < fieldlist.length; k++) {
                        if (retour[i].getNomColonne().compareToIgnoreCase(fieldlist[k].getName()) == 0) {
                            intersect.add(new Champ(fieldlist[k], retour[i].getPrecision()));
                            break;
                        }
                    }
                }
                Champ[] valiny = new Champ[intersect.size()];
                intersect.copyInto(valiny);
                //Champ[] =Champ.getChampFromTable(nomTable,c);
                //for(int i=0;i<valiny.length;i++) System.out.println("liste champ=="+valiny[i].getNomColonne());
                if (valiny.length > 0) {
                    listeColPTable.put(nomTable+e.getClassName(), valiny);
                }
            }
        }
        catch(Exception ex)
        {
            if(c!=null)c.rollback();
            throw ex;
        }
        finally
        {
            if(estOuvert==true&&c!=null)c.close();
        }
    }
    /**
     * Fonction permettant de prendre les intersections des attributs déclarés/hérités et de la table pour un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données, si non existante mettre null
     * @return
     * @throws Exception
     */
    public static Field[] getChampBase(ClassMAPTable e, Connection c) throws Exception {
        String nomTable = e.getNomTable();
        boolean testDedans = listeColPTable.containsKey(nomTable+e.getClassName());
        Champ[] retour = null;
        if (testDedans == false) {
            Vector r = getChampFromTable(nomTable, c);
            retour = new Champ[r.size()];
            r.copyInto(retour);
        } else {
            retour = (Champ[]) listeColPTable.get(nomTable+e.getClassName());
        }
        Field fieldlist[] = getFieldListeHeritage(e);
        Vector intersect = new Vector();
        for (int i = 0; i < retour.length; i++) {
            for (int k = 0; k < fieldlist.length; k++) {
                if (retour[i].getNomColonne().compareToIgnoreCase(fieldlist[k].getName()) == 0) {
                    intersect.add(fieldlist[k]);
                    break;
                }
            }
        }
        Field[] valiny = new Field[intersect.size()];
        intersect.copyInto(valiny);
        return valiny;
    }
    /**
     * Fonction permettant de prendre les champs valides d'un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données, si non existante mettre null
     * @return
     * @throws Exception
     */
    public static Champ[] getFromListe(ClassMAPTable e, Connection c) throws Exception {
        String nomTable = e.getNomTable();
        ajouterAlaListe(e, c);
        return (Champ[]) listeColPTable.get(nomTable+e.getClassName());
    }
    /**
     * Permettant de prendre le champ associé à un attribut d'un objet de mapping selon la table setté
     * @param e : objet de mapping
     * @param col : nom de l'attribut
     * @param cnx : connexion ouverte à la base de données
     * @return
     * @throws Exception
     */
    public static Champ getChamp(ClassMAPTable e, String col, Connection cnx) throws Exception {
        try {
            Champ[] liste = null;
            //System.out.println("AVANT EEE TABLE "+e.getNomTable() );
            //if (listeColPTable.containsKey(e.getNomTable()) == false) 
            if (listeColPTable.containsKey(e.getNomTable()+e.getClassName()) == false) 
            {
                //System.out.println("INTERIEUR PAS DE TABLE:"+e.getNomTable());
                //cnx = new UtilDB().GetConn();
                liste = getFromListe(e, cnx);
                //System.out.println("TAILLE DE LA LISTE TABLE "+liste.length);
            } else {
                //System.out.println("INTERIEUR ELSE PAS DE TABLE=="+e.getNomTable());
                liste = (Champ[]) listeColPTable.get(e.getNomTable()+e.getClassName());
            }
            if(liste==null)return null;
            for (int i = 0; i < liste.length; i++) {
	        //System.out.println("liste["+i+"]======="+liste[i].getNomColonne());
                if (liste[i].getNomColonne().compareToIgnoreCase(col) == 0) {
                    return liste[i];
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } 
    }
    /**
     * Permettant de prendre le champ associé à un attribut d'un objet de mapping selon la table setté lorsqu'aucune connexion ouverte existe
     * @param e : objet de mapping
     * @param col : nom de l'attribut
     * @return
     */
    public static Champ getChamp(ClassMAPTable e, String col) throws Exception {
        Connection cnx=null;
        try {
            Champ[] liste = null;
            
            //System.out.println("AVANT EEE TABLE "+e.getNomTable() );
            if (listeColPTable.containsKey(e.getNomTable()+e.getClassName()) == false) 
            {
                //System.out.println("INTERIEUR PAS DE TABLE:"+e.getNomTable());
                cnx = new UtilDB().GetConn();
                liste = getFromListe(e, cnx);
                //System.out.println("TAILLE DE LA LISTE TABLE "+liste.length);
            } else {
                //System.out.println("INTERIEUR ELSE PAS DE TABLE=="+e.getNomTable());
                liste = (Champ[]) listeColPTable.get(e.getNomTable()+e.getClassName());
            }
            for (int i = 0; i < liste.length; i++) {
	        //System.out.println("liste["+i+"]======="+liste[i].getNomColonne());
                if (liste[i].getNomColonne().compareToIgnoreCase(col) == 0) {
                    return liste[i];
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } 
        finally
        {
            if(cnx!=null)cnx.close();
        }
    }

    /**
     * Fonction permettant de prendre les champs d'une table
     * @param nomT : nom de la table
     * @param c : connexion ouverte à la base de données
     * @return Liste des champs de la table spécifiée
     * @throws Exception
     */
    public static Vector getChampFromTable(String nomT, Connection c) throws Exception {
        Statement st = null;
        ResultSet dr = null;
        try {
            String comande = "SELECT * FROM USER_TAB_COLUMNS where table_Name = upper('" + nomT + "') order by column_id asc";     
            st = c.createStatement();
            dr = st.executeQuery(comande);
            Vector retour = new Vector();
            int i = 0;
            while (dr.next()) {
                Champ temp = new Champ(dr.getString(2), dr.getString(3), dr.getInt(7), dr.getInt(8));
                retour.add(temp);
                i++;
            }
            Champ temp = new Champ("nombrepargroupe", "NUMBER", 10, 0);
            retour.add(temp);
            return retour;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (dr != null);
            dr.close();
        }
    }

}
