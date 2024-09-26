package bean;

/*
 * package regroupant toutes utilités pour interagir avec la base donnée
 */


import historique.MapHistorique;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import utilitaire.Parametre;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;

/**
 * Utilitaire pour interagir avec la base de données à partir des objet de mapping
 * @author BICI
 */

public class CGenUtil {

    public CGenUtil() {
    }
    /**
     * Prendre les champs (intersection entre attributs déclarés/hérités et colonnes de table) d'un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @return
     * @throws Exception
     */
    public static String getListeChampPage(ClassMAPTable e, Connection c) throws Exception {
        String retour = "";
        Champ[] liste = ListeColonneTable.getFromListe(e, c);
        if (liste.length == 0) {
            throw new Exception("Table non existante");
        }
        retour = liste[0].getNomColonne();
        for (int i = 1; i < liste.length; i++) {
            retour = retour + "," + liste[i].getNomColonne();
        }
        return retour;
    }

    public static Object[] getValFromEntete(ClassMAPTable e,String[] entete) throws Exception{
        Object[] results = new Object[entete.length];
        for(int i=0;i<entete.length;i++){
            System.out.println("Test entete"+entete[i]);
            results[i] = getValeurFieldByMethod(e, entete[i]);
            
        }
        return results;
    }
    /**
     * Formatter la partie somme de la requête sql selon les champs 
     * @param groupe : colonnes sans somme
     * @param somme : colonnes avec somme
     * @return
     * @throws Exception
     */
    public static String getListeChampGroupeSomme(String[] groupe, String[] somme) throws Exception {
        if (groupe == null || groupe.length == 0) {
            return "*";
        }
        String retour = groupe[0];
        for (int i = 1; i < groupe.length; i++) {
            if (groupe[i] != null && groupe[i].compareToIgnoreCase("") != 0 && groupe[i].compareToIgnoreCase("-") != 0) {
                retour = retour + "," + groupe[i];
            }
        }
        if (somme == null) {
            return retour;
        }
        for (int j = 0; j < somme.length; j++) {
            if (somme[j] != null && somme[j].compareToIgnoreCase("") != 0 && somme[j].compareToIgnoreCase("-") != 0) {
                retour = retour + ", sum(" + somme[j] + ") as " + somme[j];
            }
        }
        return retour;
    }
    /**
     * Formatter la partie de selection sans opération d'une requête avec groupe by
     * @param groupe : colonne de groupage
     * @param somme : colonne de somme (non valide)
     * @return
     * @throws Exception
     */
    public static String getListeChampGroupeSimple(String[] groupe, String[] somme) throws Exception {
        if (groupe == null) {
            return "*";
        }
        String retour = groupe[0];
        for (int i = 1; i < groupe.length; i++) {
            if (groupe[i] != null && groupe[i].compareToIgnoreCase("") != 0 && groupe[i].compareToIgnoreCase("-") != 0) {
                retour = retour + "," + groupe[i];
            }
        }
        if (somme == null) {
            return retour;
        }
        for (int j = 0; j < somme.length; j++) {
            if (somme[j] != null && somme[j].compareToIgnoreCase("") != 0 && somme[j].compareToIgnoreCase("-") != 0) {
                retour = retour + ", " + somme[j];
            }
        }
        return retour;
    }
    /**
     * Calculer les sommes et count d'une source et colonnes données
     * @param selectInterne : requête imbriquée pour la partie FROM
     * @param nomColSomme : les colonnes à sommer
     * @param c : connexion ouverte à la base de données
     * @return tableau des sommes et le dernier indices étant le count 
     * @throws Exception
     */
    public static double[] calculSommeNombreGroupe(String selectInterne, String[] nomColSomme, Connection c) throws Exception {
        ResultSet dr = null;
        try {
            String param = null;
            if (nomColSomme == null || nomColSomme.length == 0) {
                param = "SELECT COUNT(*) FROM (" + selectInterne + ")";
            } else {
                param = "SELECT " + getColonneSomme(nomColSomme) + ",COUNT(*) FROM (" + selectInterne + ")";
            }
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(param);
            double[] retour = null;
            if (nomColSomme == null) {
                retour = new double[1];
            } else {
                retour = new double[nomColSomme.length + 1];
            }
            dr.next();
            int i = 0;
            for (i = 0; i < retour.length - 1; i++) {
                retour[i] = dr.getDouble(i + 1);// getDouble(i + 1);
            }
            retour[i] = dr.getDouble(i + 1);//pour avoir le nombre de ligne 
            return retour;
        } catch (Exception s) {
            s.printStackTrace();
            throw new Exception(s.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
        }
    }
    /**
     * Calculer les sommes de colonnes d'un objet de mapping donné 
     * @param e : objet de mapping
     * @param apresWhere : condition de filtre
     * @param numChampDaty : nombre de champs d'intervalle de date
     * @param daty : colonne d'intervalles
     * @param nomColSomme : colonnes de sommes
     * @param c : connexion ouverte à la base de données
     * @return tableau des sommes et un count à l'indice final du tableau
     * @throws Exception
     */
    public static double[] calculSommeNombre(ClassMAPTable e, String apresWhere, String[] numChampDaty, String[] daty, String[] nomColSomme, Connection c) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            String param = null;
            if (nomColSomme == null) {
                param = "SELECT COUNT(*) FROM " + e.getNomTableSelect() + " WHERE " + makeWhere(e) + " " + makeWhereIntervalle(numChampDaty, daty) + " " + apresWhere;
            } else {
                param = "SELECT " + getColonneSomme(nomColSomme) + ",COUNT(*) FROM " + e.getNomTableSelect() + " WHERE " + makeWhere(e) + " " + makeWhereIntervalle(numChampDaty, daty) + " " + apresWhere;
            }
            //System.out.println("REQUETE CALCUL SOMME "+param);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(param);
            double[] retour = null;
            if (nomColSomme == null) {
                retour = new double[1];
            } else {
                retour = new double[nomColSomme.length + 1];
            }
            dr.next();
            int i = 0;
            for (i = 0; i < retour.length - 1; i++) {
                retour[i] = dr.getDouble(i + 1);// getDouble(i + 1);
            }
            retour[i] = dr.getDouble(i + 1);//pour avoir le nombre de ligne
            return retour;
        } catch (Exception s) {
            s.printStackTrace();
            throw new Exception(s.getMessage());
        } finally {
            if (st != null) {
                st.close();
            }
            if (dr != null) {
                dr.close();
            }
        }
    }
    /**
     * Formatter la partie sum d'une requête SQL
     * @param listeCol : colonne à sommer
     * @return chaine de caractère formattée de la partie sum de la requête
     */
    public static String getColonneSomme(String[] listeCol) {
        if ((listeCol == null) || (listeCol.length == 0)) {
            return "0";
        }
        String retour = "sum(" + listeCol[0] + ")";
        for (int i = 1; i < listeCol.length; i++) {
            retour = retour + ",sum(" + listeCol[i] + ")";
        }
        return retour;
    }

    /**
     * génerer la clé primaire d'un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @return clé primaire de l'objet de mapping
     */
    public static String makePK(ClassMAPTable e, Connection c) {
        int maxSeq = Utilitaire.getMaxSeq(e.getNomProcedureSequence());
        String nombre = Utilitaire.completerInt(e.getLonguerClePrimaire(), maxSeq);
        return e.getINDICE_PK() + nombre;
    }
    /**
     * Enregistrer un objet de mapping en créant au préalable la clé primaire
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @return l'objet de mapping après enregistrement
     * @throws Exception
     */
    public static ClassMAPTable saveWithPKSeq(ClassMAPTable e, Connection c) throws Exception {
        makePK(e, c);
        return save(e, c);
    }
    /**
     * Enregister un objet de mapping avec initialisation de connexion interne
     * @param e : objet de mapping
     * @return l'objet de mapping enregistré
     * @throws Exception
     */
    public static ClassMAPTable save(ClassMAPTable e) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return save(e, c);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * Enregistrer un objet de mapping
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @return l'objet de mapping enregistré
     * @throws Exception
     */
    public static int insertBatch(ClassMAPTable[] liste,Connection c)throws Exception
    {
        Statement cmd=null;
        try
        {
            cmd=c.createStatement();
            for(ClassMAPTable obj:liste)
            {
                obj.construirePK(c);
                String req=obj.getComandeInsert(c);
                cmd.addBatch(req);
            }
            return cmd.executeBatch().length;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(cmd!=null)cmd.close();
        }
    }
    
    public static ClassMAPTable save(ClassMAPTable e, Connection c) throws Exception {
        try {
            Statement cmd = null;
            Champ[] listeC = ListeColonneTable.getFromListe(e, c);
            //makePkRang(e, dt);
            String comande = "insert into " + e.getNomTable() + " values (";
            comande = comande + "'" + getValeurFieldByMethod(e, listeC[0].getNomColonne()) + "'";
            for (int i = 1; i < listeC.length; i++) {
                comande = comande + ",";
                if (getValeurFieldByMethod(e, listeC[i].getNomColonne()) == null) {
                    comande = comande + " null ";
                } else {
                    comande = comande + "'" + getValeurFieldByMethod(e, listeC[i].getNomColonne()) + "'";
                }
            }
            comande = comande + ")";
            cmd = c.createStatement();

            cmd.executeUpdate(comande);
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
    /**
     * Rechercher un objet de mapping filtré
     * @param e : objet de mapping
     * @param colInt : colonnes avec intervalles
     * @param valInt : valeurs des intervalles
     * @param apresWhere : where statement personalisé
     * @return tableau de l'objet de mapping filtré
     * @throws Exception
     */
    public static Object[] rechercher(ClassMAPTable e, String[] colInt, String[] valInt, String apresWhere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercher(e, colInt, valInt, c, apresWhere);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * Rechercher un objet de mapping avec une requête
     * @param e : objet de mapping
     * @param requete : requete SQL pour faire la selection
     * @return tableau de l'objet de mapping filtré
     * @throws Exception
     */
	public static Object[] rechercher(ClassMAPTable e, String requete) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercher(e, requete, c);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
	/**
     * Rechercher un objet de mapping avec filtre
     * @param e : objet de mapping
     * @param requete : requête SQL pour la recherche
     * @param c : connexion ouverte à la base de données
     * @return tableau de l'objet de mapping filtré
     * @throws Exception
     */
	public static Object[] rechercher(ClassMAPTable e, String requete, Connection c) throws Exception {
        ResultSet dr = null;
        Statement cmd = null;
        boolean ifconnnull = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ifconnnull = true;
            }
            cmd = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dr = cmd.executeQuery(requete);
            return transformeDataReader(dr, e, c);
        } catch (Exception x) {
            x.printStackTrace();
            throw new Exception("Erreur durant la recherche " + x.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (cmd != null) {
                cmd.close();
            }
            if (ifconnnull && c != null) {
                c.close();
            }
        }
    }
	/**
     * rechercher  un objet de mapping avec groupage et somme de certaines colonnes
     * @param e : objet de mapping 
     * @param requete : requête SQL pour la selection
     * @param nomColSomme : liste de colonnes à sommer
     * @return instance avec les objets de mapping et les sommes de certaines colonnes
     * @throws Exception
     */
    public static ResultatEtSomme rechercher(ClassMAPTable e, String requete, String[] nomColSomme) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercher(e, requete, c, nomColSomme);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * rechercher  un objet de mapping avec groupage et somme de certaines colonnes
     * @param e : objet de mapping
     * @param requete : requête SQL pour la selection
     * @param c : connexion ouverte à la base de données
     * @param nomColSomme : liste des colonnes à sommer
     * @return instance avec les objets de mapping et les sommes de certaines colonnes
     * @throws Exception
     */
    public static ResultatEtSomme rechercher(ClassMAPTable e, String requete, Connection c, String[] nomColSomme) throws Exception {
        ResultSet dr = null;
        Statement cmd = null;
        boolean ifconnnull = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ifconnnull = true;
            }
            cmd = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dr = cmd.executeQuery(requete);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(requete, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            return rs;
            
        } catch (Exception x) {
            x.printStackTrace();
            throw new Exception("Erreur durant la recherche " + x.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (cmd != null) {
                cmd.close();
            }
            if (ifconnnull && c != null) {
                c.close();
            }
        }
    }
    /**
     * 
     * @param e
     * @param colInt
     * @param valInt
     * @param attr2D
     * @param c
     * @param apresWhere
     * @return
     * @throws Exception
     */
    public static HashMap<String,Vector> rechercher2D(ClassMAPTable e, String[] colInt, String[] valInt, String attr2D, Connection c, String apresWhere) throws Exception {
        ResultSet dr = null;
        Statement cmd = null;
        boolean ifconnnull = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ifconnnull = true;
            }
            String comandeInterne = "select * from " + e.getNomTableSelect();
            //System.out.println("tablea : "+e.getNomTableSelect());
            if(apresWhere==null||apresWhere.compareToIgnoreCase("")==0)apresWhere=" order by "+attr2D+" asc";
            comandeInterne = comandeInterne + " where " + makeWhere(e, c) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere;
            //if(e.getNomTableSelect().compareToIgnoreCase("talon")==0) 
			//System.out.println("REQUETE : " + comandeInterne);
            cmd = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dr = cmd.executeQuery(comandeInterne);
            HashMap<String,Vector> ret= transformeDataReader2D(dr, e,attr2D, c);
            //System.out.println("RETOUUUUUUUR   "+ret.length);
            return ret;
        } catch (Exception x) {
            x.printStackTrace();
            throw new Exception("Erreur durant la recherche " + x.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (cmd != null) {
                cmd.close();
            }
            if (ifconnnull && c != null) {
                c.close();
            }
        }
    }
    /**
     * Rechercher un objet de mapping avec filtre
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : valeurs des colonnes d'intervalles
     * @param c : connexion ouverte à la base de données
     * @param apresWhere : filtre SQL spécial hors logique de mapping et d'intervalles
     * @return liste d'objet de mapping filtré
     * @throws Exception
     */
    public static Object[] rechercher(ClassMAPTable e, String[] colInt, String[] valInt, Connection c, String apresWhere) throws Exception {
        ResultSet dr = null;
        Statement cmd = null;
        boolean ifconnnull = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ifconnnull = true;
            }
            String comandeInterne = "select * from " + e.getNomTableSelect();
            comandeInterne = comandeInterne + " where " + makeWhere(e, c) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere;
            cmd = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dr = cmd.executeQuery(comandeInterne);
            Object[] ret= transformeDataReader(dr, e, c);
            return ret;
        } catch (Exception x) {
            x.printStackTrace();
            throw new Exception("Erreur durant la recherche " + x.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (cmd != null) {
                cmd.close();
            }
            if (ifconnnull && c != null) {
                c.close();
            }
        }
    }
    
    /**
     * Rechercher les objets avec filtre avec support de l'opérateur OR
     * @param e : objet de mapping
     * @param colInt : liste de colonne d'intervalles
     * @param valInt : liste de valeur pour les colonne d'intervalles
     * @param c : connexion ouverte à base de données
     * @param colOr : liste de colonne de filtre avec l'opérateur OR
     * @param valOr : liste de valeurs possibles pour chaque colonne
     * @param apresWhere : filtre SQL spécial hors logique de mapping, intervalles et de l'opérateur OR
     * @return liste d'objets filtré
     * @throws Exception
     */
    public static Object[] rechercherANDOR(ClassMAPTable e, String[] colInt, String[] valInt, Connection c, String[] colOr, String[][] valOr, String apresWhere) throws Exception {
        ResultSet dr = null;
        Statement cmd = null;
        try {
            String comandeInterne = "select * from " + e.getNomTableSelect();
            //System.out.println("tablea : "+e.getNomTableSelect());
            comandeInterne = comandeInterne + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " and (" + makeOr(colOr, valOr) + ") " + apresWhere;
            //if(e.getNomTableSelect().compareToIgnoreCase("AvancementCompletId")==0)
            //System.out.println(comandeInterne);
            cmd = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dr = cmd.executeQuery(comandeInterne);
            return transformeDataReader(dr, e, c);
        } catch (Exception x) {
            x.printStackTrace();
            throw new Exception("Erreur durant la recherche " + x.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (cmd != null) {
                cmd.close();
            }
        }
    }
    /**
     * recherche paginée d'objets avec filtre et des sommes de certains champs
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalle
     * @param valInt : liste des valeurs  des colonnes d'intervalles
     * @param numPage : numéro de page actuel
     * @param apresWhere : filtre SQL spécial hors logique de mapping et d'intervalles
     * @param nomColSomme : liste des colonnes à sommer
     * @param colOrdre : colonne d'ordre
     * @param ordre : ASC ou DESC
     * @return instance avec les objets filtrés/paginés et les sommes des colonnes précisées
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String colOrdre, String ordre) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            if ((colOrdre == null) || (colOrdre.compareToIgnoreCase("") == 0)) {
                colOrdre = e.getAttributIDName();
                ordre = "DESC";
            }
            apresWhere = apresWhere + " order by " + colOrdre + " " + ordre;

            return rechercherPage(e, colInt, valInt, numPage, apresWhere, nomColSomme, c);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * recherche paginée d'objets avec filtre et des sommes de certains champs
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalle
     * @param valInt : liste des valeurs  des colonnes d'intervalles
     * @param numPage : numéro de page
     * @param apresWhere : filtre SQL spécial hors logique de mapping et d'intervalles, doit commencer par " AND"
     * @param nomColSomme : liste des colonnes à sommer
     * @return instance avec les objets filtrés/paginés et les sommes des colonnes précisées
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercherPage(e, colInt, valInt, numPage, apresWhere, nomColSomme, c);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * recherche paginée d'objets avec filtre et des sommes de certains champs en précisant le nombre de pages
     * @param e : objet de mapping
     * @param requete : requête SQL pour la recherche
     * @param numPage : numéro de page actuel
     * @param nomColSomme : liste des colonnes à sommer
     * @param nbPage : nombre de pages
     * @return instance avec les objets filtrés/paginés et les sommes des colonnes précisées
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String requete, int numPage, String[] nomColSomme, int nbPage) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercherPage(e, requete, numPage, nomColSomme, c, nbPage);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * Rechercher des objets avec filtre 
     * @param e : objet de mapping
     * @param apresWhere : requête SQL de filtre 
     * @return instance avec les resultats de recherches
     * @throws Exception
     */
    public static ResultatEtSomme rechercherParCritere(ClassMAPTable e, String apresWhere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercherPCritere(e, apresWhere, c);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    /**
     * prendre des objets dans une intervalle d'indice
     * @param o : liste d'objets
     * @param iInf : indice de début (inclus)
     * @param iSup : indice de fin (exclus)
     * @return Objets entre les indices données
     */
    public static Object[] copierPartiel(Object[] o, int iInf, int iSup) {
        Object[] retour = new Object[iSup - iInf + 1];
        for (int i = iInf; i < iSup || i < o.length; i++) {
            retour[i] = o[i];
        }
        return retour;
    }
    /**
     * séparer la requête après where distinctivement en la clause where et la clause order by
     * @param apresWhere : requête SQL après WHERE
     * @return listes avec premier élèment la clause where et le second élèment la clause order by
     */
    public static String[] separerOrderBy(String apresWhere) {
        String[] retour = new String[2];
        retour[0] = "";
        retour[1] = apresWhere;
        char[] cv = apresWhere.toCharArray();
        if (apresWhere == null || apresWhere.compareTo("") == 0) {
            /*retour[0]="";
             colOrdre=e.getAttributIDName();
             ordre="DESC";*/
        }
        for (int i = 0; i < cv.length; i++) {
            if (utilitaire.UtilitaireString.testMotDansUnePhrase(apresWhere, i, "order") == true) {
                retour[0] = apresWhere.substring(0, i);
                retour[1] = apresWhere.substring(i, apresWhere.length());
                return retour;
            }
        }
        return retour;
    }
    /**
     * Recherche paginée avec  des sommes de colonnes en récapitulation
     * @param e : objet de mapping
     * @param requete : requête SQL pour la recherche
     * @param numPage : numéro de page à prendre
     * @param nomColSomme : noms des colonnes à sommer
     * @param c : connexion ouverte à la base de données
     * @param nbPage : nombre d'élèments dans une pages
     * @return instance avec les resultats de la rech
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String requete, int numPage, String[] nomColSomme, Connection c, int nbPage) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            int npp = 0;
            if (nbPage <= 0) {
                npp = Parametre.getNbParPage();//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nbPage;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String commande = "select * from (" + requete + ") where r between " + indiceInf + " and " + indiceSup + "";
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(commande);
			
            //System.out.println("requete 1 foana ve oo: "+commande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombre(e, "", null, null, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            return rs;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Recherche paginée avec nombre d'élèments de la page fixé au nombre configuré si nombre d'élèments donnés invalides
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page actuel
     * @param apresWhere : partie de requête après where
     * @param nomColSomme : liste des colonnes de somme
     * @param c : connexion ouverte à la base de données
     * @param nbPage : nombre d'élèment dans une page
     * @return instance avec les résultas de la recherches et les sommes des colonnes
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageMax(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int nbPage) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            int npp = 0;
            if (nbPage <= 0) {
                npp = Parametre.maxRecherche;//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nbPage;
            }
             if ((apresWhere == null) || (apresWhere.compareToIgnoreCase("") == 0)) {
                String colOrdre = e.getAttributIDName();
                String ordre = "DESC";
                apresWhere =  " order by " + colOrdre + " " + ordre;
            }
            

            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String comande = "select * from (select " + getListeChampPage(e, c) + " from (select * from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ")) where rownum <="+ indiceSup;
            //String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ") where r between " + indiceInf + " and " + indiceSup;
            //System.out.println(" ========== " + comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            //System.out.println("heure apres transformer data reader=="+Utilitaire.heureCouranteHMS());
            double[] somme = calculSommeNombre(e, apresWhere, colInt, valInt, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            // ResultatEtSomme rs= new ResultatEtSomme(resultat);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    /**
     * Recherche paginée avec nombre d'élèments de la page fixé au nombre configuré si nombre d'élèments donnés invalides, puis les colonnes de sommes ignorées
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page actuel
     * @param apresWhere : partie de requête après where
     * @param nomColSomme : liste des colonnes de somme
     * @param c : connexion ouverte à la base de données
     * @param nbPage : nombre d'élèment dans une page
     * @return instance avec les résultas de la recherches
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageMaxSansRecap(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int nbPage) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            int npp = 0;
            if (nbPage <= 0) {
                npp = Parametre.maxRecherche;//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nbPage;
            }
            if ((apresWhere == null) || (apresWhere.compareToIgnoreCase("") == 0)) {
                String colOrdre = e.getAttributIDName();
                String ordre = "DESC";
                apresWhere =  " order by " + colOrdre + " " + ordre;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String comande = "select * from (select " + getListeChampPage(e, c) + " from (select * from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ")) where rownum <="+ indiceSup;
            //String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ") where r between " + indiceInf + " and " + indiceSup;
            //System.out.println(" ========== commmande " + comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            //System.out.println("heure apres transformer data reader=="+Utilitaire.heureCouranteHMS());
            //double[] somme = calculSommeNombre(e, apresWhere, colInt, valInt, nomColSomme, c);
            
            double[] somme = null;
            if (nomColSomme == null) {
                somme = new double[1];
            } else {
                somme = new double[nomColSomme.length + 1];
            }
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            // ResultatEtSomme rs= new ResultatEtSomme(resultat);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Recherche paginée avec des sommes de colonnes en tant que récapitulation
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page à prendre
     * @param apresWhere : filtre SQL après where
     * @param nomColSomme : noms des colonnes à sommer
     * @param c : connexion ouverte à la base de données
     * @param nbPage : nombre d'élèments dans une pages
     * @return instance avec les resultats de la recherches et les sommes des colonnes spécifiées
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int nbPage) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            int npp = 0;
            if (nbPage <= 0) {
                if(nbPage==-2)
                {
                double[] sommeNeg = {0};
                Object[] valiny=rechercher(e, colInt, valInt, c, apresWhere);
                return new ResultatEtSomme(valiny,sommeNeg);
                }else npp=Parametre.getNbParPage();
            } else {
                npp = nbPage;
            }
            if ((apresWhere == null) || (apresWhere.compareToIgnoreCase("") == 0)) {
                String colOrdre = e.getAttributIDName();
                String ordre = "DESC";
                apresWhere =  " order by " + colOrdre + " " + ordre;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from (select * from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ")) where r between " + indiceInf + " and " + indiceSup;
            //String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ") where r between " + indiceInf + " and " + indiceSup;
            //System.out.println(" requete requete ========== totra" + comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
		//	System.out.println("requete 2: " + comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombre(e, apresWhere, colInt, valInt, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            // ResultatEtSomme rs= new ResultatEtSomme(resultat);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Recherche paginée 
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page à prendre
     * @param apresWhere : filtre SQL après where
     * @param c : connexion ouverte à la base de données
     * @param nbPage : nombre d'élèments dans une page
     * @return liste de resultats de recherche
     * @throws Exception
     */
    public static Object[] rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, Connection c, int nbPage) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            int npp = 0;
            if (nbPage <= 0) {
                if(nbPage==-2)
                {
                double[] sommeNeg = {0};
                return rechercher(e, colInt, valInt, c, apresWhere);
                }else npp=Parametre.getNbParPage();
            } else {
                npp = nbPage;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from (select * from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ")) where r between " + indiceInf + " and " + indiceSup;
            //String comande = "select * from (select " + getListeChampPage(e, c) + ",rowNum as r from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + ") where r between " + indiceInf + " and " + indiceSup;
            //System.out.println(" requete requete ========== totra" + comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
		//	System.out.println("requete 2: " + comande);
            return  transformeDataReader(dr, e, c);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Recherche paginée avec initialisation de connexion à la base de données implicite implicite
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page à prendre
     * @param apresWhere : filtre SQL après where
     * @param nbPage : nombre d'élèments dans une page
     * @return liste de resultats de recherche
     * @throws Exception
     */
    public static Object[] rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, int nbPage) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            return rechercherPage(e, colInt, valInt, numPage, apresWhere, c, nbPage);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c!=null) {
                c.close();
            }
        }
        return null;
    }
    /**
     * Rechercher des objets avec filtre directement à partir de la requête après where
     * @param e : objet de mapping
     * @param apresWhere : requête SQL après where pour filtrer
     * @param c : connexion ouverte à la base de données
     * @return instance avec les objets filtrés
     * @throws Exception
     */
    public static ResultatEtSomme rechercherCritere(ClassMAPTable e, String apresWhere, Connection c) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        try {
            String comande = "select * from " + e.getNomTableSelect() + " where " + apresWhere;
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche pagine de la table " + e.getNomTableSelect() + ex.getMessage());
        } finally {
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Recherche paginée avec nombre d'élèments de la page fixé au nombre configuré
     * @param e : objet de mapping
     * @param colInt : liste de colonnes d'intervalles
     * @param valInt : liste des valeurs des colonnes d'intervalle
     * @param numPage : numéro de page actuel
     * @param apresWhere : partie de requête après where
     * @param nomColSomme : liste des colonnes de somme
     * @param c : connexion ouverte à la base de données
     * @return instance avec les resultats de la recherches et les sommes des colonnes spécifiées
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c) throws Exception {
        return rechercherPage(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, 0);
    }
    /**
     * Rechercher des objets avec filtre directement à partir de la requête après where
     * @param e : objet de mapping
     * @param apresWhere : requête SQL après where pour filtrer
     * @param c : connexion ouverte à la base de données
     * @return instance avec les objets filtrés
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPCritere(ClassMAPTable e, String apresWhere, Connection c) throws Exception {
        return rechercherCritere(e, apresWhere, c);
    }
    /**
     * recherche groupée paginée avec récapitulation avec nombre d'élèment dans une page fixé au valeur de configuration
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c) throws Exception {
        return rechercherPageGroupe(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c, 0);
    }
    /**
     * recherche groupée paginée avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @param nppa : nombre d'élèment par page
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int nppa) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        try {
            e.setMode("groupe");
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            int npp;
            if (nppa <= 0) {
                npp = Parametre.getNbParPage();//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nppa;
            }
            if (((apresWhere == null) || (apresWhere.compareToIgnoreCase("") == 0))&&(ordre==null||ordre.compareToIgnoreCase("")==0)) {
                boolean test = false;
                String groupeInit = "";
                for(String objet : groupe){
                    if(objet.equalsIgnoreCase(e.getAttributIDName())) test =  true;
                    groupeInit = objet;
                }
                String colOrdre = test ? e.getAttributIDName() : groupeInit;
                String ordre2 = "DESC";
                ordre =  " order by " + colOrdre + " " + ordre2;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + ", COUNT(*) as nombrepargroupe from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + ordre;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",nombrepargroupe,rowNum as r from (" + selectInterne + ")) where r between " + indiceInf + " and " + indiceSup;
            //if (e.getNomTable().compareToIgnoreCase("opPayelc")==0) 
            //System.out.println("La comande groupe SQL = "+comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
            if (ouvertInterne == true && c != null) {
                c.close();
            }
        }
    }
    @Deprecated
    /**
     * recherche groupée paginée avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @param nppa : nombre d'élèment par page
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupeMoyenneCompte(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] moygroupe, String[] cptgroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int nppa) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            int npp;
            if (nppa <= 0) {
                npp = Parametre.getNbParPage();//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nppa;
            }
            if (((apresWhere == null) || (apresWhere.compareToIgnoreCase("") == 0))&&(ordre==null||ordre.compareToIgnoreCase("")==0)) {
                String colOrdre = e.getAttributIDName();
                String ordre2 = "DESC";
                apresWhere =  " order by " + colOrdre + " " + ordre2;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + " from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + ordre;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",rowNum as r from (" + selectInterne + ")) where r between " + indiceInf + " and " + indiceSup;
            //if (e.getNomTable().compareToIgnoreCase("opPayelc")==0) System.out.println("La comande groupe SQL = "+comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            if (ouvertInterne == true && c != null) {
                c.close();
            }
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * Permet de définir la valeur d'un champ donné
     * @param e : objet à mettre à jour
     * @param nomChamp : nom du champs
     * @param valeur : valeur à définir
     * @throws Exception
     */
    public static void setValChamp(ClassMAPTable e, Field nomChamp, Object valeur) throws Exception {
        //System.out.println(nomChamp);
        String nomMethode = "set" + Utilitaire.convertDebutMajuscule(nomChamp.getName());
         Class[] paramT = {nomChamp.getType()};
        Object[] args = {valeur};
        try {
            if (e.getMode().compareToIgnoreCase("modif")!=0 && valeur != null)
            {
                Object o = e.getClass().getMethod(nomMethode, paramT).invoke(e, args);
            }
            if (e.getMode().compareToIgnoreCase("modif")==0)
            {
                Object o = e.getClass().getMethod(nomMethode, paramT).invoke(e, args);
            }
        } catch (NoSuchMethodException ex) {
            Method[] list = e.getClass().getMethods();
            for (int i = 0; i < list.length; i++) {
//                System.out.println("LIST : "+list[i].getName()+" === "+nomMethode);
                if (list[i].getName().compareToIgnoreCase(nomMethode) == 0) {
                    list[i].invoke(e, args);
                    return;
                }
            }
            throw new Exception("Methode non trouve");
        } catch (InvocationTargetException x) {
            throw new Exception(x.getTargetException().getMessage());
        }
    }
    /**
     * Permet de prendre la valeur d'un champ donné
     * @param e : objet donné
     * @param nomChamp : nom du champs à invoquer
     * @return valeur en chaine de caractère du champs de l'objet
     * @throws Exception
     */
    public static String getValeurInsert(ClassMAPTable e, String nomChamp) throws Exception {
        String nomMethode = "get" + Utilitaire.convertDebutMajuscule(nomChamp);
        return e.getClass().getMethod(nomMethode, null).invoke(e, null).toString();
    }
    /**
     * Permet de prendre la méthode getter d'un attribut
     * @param e : objet donné
     * @param nomChamp : nom de l'attribut
     * @return méthode getter de l'attribut
     * @throws Exception
     */
    public static Method getMethod(ClassMAPTable e, String nomChamp) throws Exception {
        String nomMethode = "get" + Utilitaire.convertDebutMajuscule(nomChamp);
        Method ret = null;
        try {
            ret = e.getClass().getMethod(nomMethode, null);
            return ret;
        } catch (NoSuchMethodException ex) {
            Method[] list = e.getClass().getMethods();
            for (int i = 0; i < list.length; i++) {
                if (list[i].getName().compareToIgnoreCase(nomMethode) == 0) {
                    ret = list[i];
                    return ret;
                }
            }
            throw new Exception("Methode non trouve " + nomChamp);
        }
    }
    /**
     * Permet d'accéder à la valeur encapsulée d'un attribut
     * @param e : objet à extraire la valeur
     * @param nomChamp : nom de l'attribut
     * @return valeur de l'attribut donné
     * @throws Exception
     */
    public static Object getValeurFieldByMethod(ClassMAPTable e, String nomChamp) throws Exception {
        String nomMethode = "get" + Utilitaire.convertDebutMajuscule(nomChamp);
        Object ret = null;
        try {
            ret = e.getClass().getMethod(nomMethode, null).invoke(e, null);
            return ret;
        } catch (NoSuchMethodException ex) {
            Method[] list = e.getClass().getMethods();
            for (int i = 0; i < list.length; i++) {
                if (list[i].getName().compareToIgnoreCase(nomMethode) == 0) {
                    ret = list[i].invoke(e, null);
                    return ret;
                }
            }
            throw new Exception("Methode non trouve " + nomMethode);
        }
    }
    /**
     * Permet d'accéder à la valeur encapsulée d'un attribut
     * @param e : objet à extraire la valeur
     * @param g : attribut 
     * @return valeur de l'attribut donné
     * @throws Exception
     */
    public static Object getValeurFieldByMethod(ClassMAPTable e, Field g) throws Exception {
        return getValeurFieldByMethod(e, g.getName());
    }
    /**
     * construire requête après clause where pour les champs d'intervalles
     * @param numChamp : liste des champs d'intervalles
     * @param val : liste des valeurs de champs d'intervalles
     * @return requête après clause where pour les champs d'intervalles
     * @throws Exception
     */
    public static String makeWhereIntervalle(String[] numChamp, String[] val) throws Exception {
        String retour = "";
        if (numChamp == null) {
            return "";
        }

        for (int i = 0; i < numChamp.length; i++) {
            boolean isChampDate = numChamp[i].toUpperCase().contains("DATE") || numChamp[i].toUpperCase().contains("DATY");
            boolean isvalidData_1 = (!isChampDate && Utilitaire.isStringNumeric(val[2 * i])) || (isChampDate && Utilitaire.isValidDate(val[2 * i], "dd/MM/yyyy"));
            boolean isvalidData_2 = (!isChampDate && Utilitaire.isStringNumeric(val[(2 * i) + 1])) || (isChampDate && Utilitaire.isValidDate(val[(2 * i) + 1], "dd/MM/yyyy"));
            if ((val[2 * i] != "" && isvalidData_1) && (val[(2 * i) + 1] == "")) {
                retour = retour + " AND " + numChamp[i] + " >= '" + val[2 * i] + "'";
            }
            if ((val[2 * i] == "") && (val[(2 * i) + 1] != "" && isvalidData_2)) {
                retour = retour + " AND " + numChamp[i] + " <= '" + val[(2 * i) + 1] + "'";
            }
            if ((val[2 * i] != "" && isvalidData_1) && (val[(2 * i) + 1] != "" && isvalidData_2)) {
                retour = retour + " AND " + numChamp[i] + " >= '" + val[2 * i] + "'" + " AND " + numChamp[i] + " <= '" + val[(2 * i) + 1] + "'";
            }
            if((val[2 * i] != "" && !isvalidData_1) || (val[(2 * i) + 1] != "" && !isvalidData_2)){
                retour = " AND 1>2";
            }
        }
        return retour;
    }
    /**
     * Permet de prendre l'attribut d'un objet à partir du nom de l'attribut
     * @param ec : objet en question
     * @param nomChamp : nom de l'attribut
     * @return attribut recherché
     * @throws ClassMAPTableException
     */
    public static Field getField(ClassMAPTable ec, String nomChamp) throws ClassMAPTableException {
        try {

            Field fieldlist[] = ListeColonneTable.getFieldListeHeritage(ec);
            for (int i = 0; i < fieldlist.length; i++) {
                if (fieldlist[i].getName().compareToIgnoreCase(nomChamp) == 0) {
                    return fieldlist[i];
                }
            }
            return null;
        } catch (Exception e) {
            throw new ClassMAPTableException(e.getMessage());
        }
    }
    /**
     * Permet de prendre les attributs déclarés d'un objet
     * @param ec : objet en question
     * @return liste des attributs déclarés
     * @throws ClassMAPTableException
     */
    public static Field[] getFieldList(ClassMAPTable ec) throws ClassMAPTableException {
        try {
            Class cls = ec.getClass();
            Field retour[] = new Field[ec.getNombreChamp()];
            Field fieldlist[] = cls.getDeclaredFields();
            for (int i = 0; i < fieldlist.length; i++) {
                //if()
                retour[i] = fieldlist[i];
            }
            return retour;
        } catch (Exception e) {
            throw new ClassMAPTableException(e.getMessage());
        }
    }
    /**
     * Construire une partie de la requête SQL après la clause WHERE avec support de l'opérateur WHERE
     * @param ch : liste des colonnes 
     * @param valOr : liste des valeurs des colonnes
     * @return partie de la requête SQL avec OR
     * @throws Exception
     */
    public static String makeOr(String[] ch, String[][] valOr) throws Exception {
        String retour = " ";
        if (ch == null || ch.length == 0 || valOr == null || valOr.length == 0) {
            return " 1<2";
        }
        String or = "";
        for (int k = 0; k < ch.length; k++) {
            for (int j = 0; j < valOr[k].length; j++) {
                if (j > 0) {
                    or = " or ";
                }
                retour = retour + or + " upper(" + ch[k] + ") like upper('" + Utilitaire.replaceChar(valOr[k][j]) + "')";
            }

        }

        return retour;
    }
    /**
     * Construire une partie de la clause WHERE à partir des valeurs des attributs d'un objet de mapping 
     * @param e : objet de mapping
     * @return clause WHERE représentant le filtre de l'objet de mapping
     * @throws Exception
     */
    public static String makeWhere(ClassMAPTable e) throws Exception {
        String retour = "1<2";
        Champ[] ch = ListeColonneTable.getFromListe(e, null);
        for (int k = 0; k < ch.length; k++) {
            //if (!e.getListeAttributsIgnores().Contains(((String)g[k].Name).ToLower()))
            {
                Object valeurk = getValeurFieldByMethod(e, ch[k].getNomClasse().getName());
                boolean testDaty = ((ch[k].getNomClasse().getType().toString() == "System.DateTime"));
                boolean testNombre = ((ch[k].getNomClasse().getType().getName().compareToIgnoreCase("double") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("int") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("short") == 0));
                //if(ch[k].getNomClasse().getType().getName().compareToIgnoreCase("double")==0) System.out.println(testNombre+" and "+ch[k].getNomClasse().getName());
                if ((testNombre == false) && (testDaty == false) && (valeurk != null) && (valeurk.toString().compareToIgnoreCase("") != 0) && (valeurk.toString().compareToIgnoreCase("%") != 0)) //&& (ch[k].getNomClasse().getName() != "nomTableSelect") && (ch[k].getNomClasse().getName() != "nomTableInsert") && (ch[k].getNomClasse().getName() != "apresCle"))
                {
                    // retour = retour + " and " + g[k].Name + " like '" + g[k].GetValue(e, null).ToString().Replace("'", "''") + "'";
                    if(ch[k].getNomColonne().compareToIgnoreCase("id")==0){
                        retour = retour + " and " + ch[k].getNomClasse().getName() + " like '%" + Utilitaire.replaceChar(valeurk.toString()) + "%'";
                    }
                    else{
                        retour = retour + " and upper(" + ch[k].getNomClasse().getName() + ") like upper('%" + Utilitaire.replaceChar(valeurk.toString()) + "%')";
                
                    }
                }    
            }

        }
        return retour;
    }
     /**
     * Construire une partie de la clause WHERE à partir des valeurs d'un objet de mapping, avec les chaines de caractères en capital
     * @param e : objet de mapping
     * @return clause WHERE représentant le filtre de l'objet de mapping
     * @throws Exception
     */
    public static String makeWhereUpper(ClassMAPTable e) throws Exception {
        String retour = "1<2";
        Champ[] ch = ListeColonneTable.getFromListe(e, null);
        for (int k = 0; k < ch.length; k++) {
            {
                Object valeurk = getValeurFieldByMethod(e, ch[k].getNomClasse().getName());
                boolean testDaty = ((ch[k].getNomClasse().getType().toString() == "System.DateTime"));
                boolean testNombre = ((ch[k].getNomClasse().getType().getName().compareToIgnoreCase("double") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("int") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("short") == 0));
                if ((testNombre == false) && (testDaty == false) && (valeurk != null) && (valeurk.toString().compareToIgnoreCase("") != 0) && (valeurk.toString().compareToIgnoreCase("%") != 0)) //&& (ch[k].getNomClasse().getName() != "nomTableSelect") && (ch[k].getNomClasse().getName() != "nomTableInsert") && (ch[k].getNomClasse().getName() != "apresCle"))
                {
                    if(ch[k].getNomColonne().compareToIgnoreCase("id")==0){
                        retour = retour + " and " + ch[k].getNomClasse().getName() + " like '%" + Utilitaire.replaceChar(valeurk.toString()) + "%'";
                    }
                    else{
                        retour = retour + " and " + ch[k].getNomClasse().getName() + " like '%" + Utilitaire.replaceChar(valeurk.toString()) + "%'";
                
                    }
                }    
            }

        }
        return retour;
    }

    /**
     * Construire une partie de la clause WHERE à partir des valeurs des attributs d'un objet de mapping 
     * @param e : objet de mapping
     * @param con : connexion ouverte à la base de données
     * @return clause WHERE représentant le filtre de l'objet de mapping
     * @throws Exception
     */
    
    public static String makeWhere(ClassMAPTable e, Connection con) throws Exception {
        try {
            String retour = "1<2";
            Champ[] ch = ListeColonneTable.getFromListe(e, con);
            for (int k = 0; k < ch.length; k++) {
                {
                    Object valeurk = getValeurFieldByMethod(e, ch[k].getNomClasse().getName());
                    boolean testDaty = ((ch[k].getNomClasse().getType().toString() == "System.DateTime"));
                    boolean testNombre = ((ch[k].getNomClasse().getType().getName().compareToIgnoreCase("double") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("int") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("short") == 0));
                    if ((testNombre == false) && (testDaty == false) && (valeurk != null) && (valeurk.toString().compareToIgnoreCase("") != 0) && (valeurk.toString().compareToIgnoreCase("%") != 0)) //&& (ch[k].getNomClasse().getName() != "nomTableSelect") && (ch[k].getNomClasse().getName() != "nomTableInsert") && (ch[k].getNomClasse().getName() != "apresCle"))
                    {
                    if(ch[k].getNomColonne().compareToIgnoreCase("id")==0){
                        retour = retour + " and " + ch[k].getNomClasse().getName() + " ='" + Utilitaire.replaceChar(valeurk.toString()) + "'";
                    }else{
                        retour = retour + " and " + ch[k].getNomClasse().getName() + " like '%" + Utilitaire.replaceChar(valeurk.toString()) + "%'";
                
                    }                    }

                }
            }
            return retour;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur makewhere : " + ex.getMessage());
        }
    }
    /**
     * transformer le resultat de la requête de base de données en objet de mapping donné
     * @param dr : pointeur vers les données de la base
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @param iInf : indice de début (inclus)
     * @param iSup : indice de fin (exclus)
     * @return liste d'objets mappés aux données de la base de données
     * @throws Exception
     */
    public static Object[] transformeDataReader(ResultSet dr, ClassMAPTable e, Connection c, int iInf, int iSup) throws Exception {
        Vector retour = new Vector();
        String nomTable = e.getNomTableSelect();
        Champ[] listeChamp = ListeColonneTable.getFromListe(e, c);
        int ii = 0;
        while (dr.next()) {
            if (ii >= iInf && ii <= iSup) {
                Object o = Class.forName(e.getClassName()).newInstance();
                ClassMAPTable ex = (ClassMAPTable) o;
                ex.setMode("select");
                for (int i = 0; i < listeChamp.length; i++) {
                    try {
                        int g = dr.findColumn(listeChamp[i].getNomColonne());
                    } catch (SQLException sq) {
                        continue;
                    }
                    if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() == 0)) {
                        setValChamp(ex, listeChamp[i].getNomClasse(), new Integer(dr.getInt(listeChamp[i].getNomColonne())));
                    }
                    if (((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() > 0)) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("FLOAT") == 0)) {
                        setValChamp(ex, listeChamp[i].getNomClasse(), new Double(dr.getDouble(listeChamp[i].getNomColonne())));
                    }
                    if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("VARCHAR2") == 0) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("CHAR") == 0)) {
                        setValChamp(ex, listeChamp[i].getNomClasse(), dr.getString(listeChamp[i].getNomColonne()));
                    }
                    if (listeChamp[i].getTypeColonne().compareToIgnoreCase("DATE") == 0) {
                        setValChamp(ex, listeChamp[i].getNomClasse(), dr.getDate(listeChamp[i].getNomColonne()));
                    }
                    //System.out.println("nom colonne=="+listeChamp[i].getNomColonne()+" == "+getValeurFieldByMethod(ex, listeChamp[i].getNomColonne()));
                }
                ex.setNomTable(e.getNomTableSelect());
                retour.add(ex);
            }
            ii++;
        }
        Object[] temp = (Object[]) java.lang.reflect.Array.newInstance(e.getClass().newInstance().getClass(), retour.size());
        retour.copyInto(temp);
        return temp;
    }
    /**
     * transformer le resultat de la requête de base de données en dictionnaire d'un objet de mapping donné
     * @param dr : pointeur vers les données de la base
     * @param e : instance de l'objet de mapping
     * @param attr2D : attribut de clé pour le dictionnaire
     * @param c : connexion ouverte à la base de données
     * @return dictionnaire avec  liste d'objets mappés aux données de la base de données
     * @throws Exception
     */
    public static HashMap<String,Vector> transformeDataReader2D(ResultSet dr, ClassMAPTable e,String attr2D, Connection c) throws Exception {
        Vector retour = new Vector();
        Vector retour2D=new Vector();
        String nomTable = e.getNomTableSelect();
        String ancienneValeur="tsy mitovy yyyyyyy yyy";
        Champ[] listeChamp = ListeColonneTable.getFromListe(e, c);
        int compteur=0;
        
        HashMap<String,Vector> map = new HashMap<String,Vector>();
        
        while (dr.next()) 
        {
            Object o = Class.forName(e.getClassName()).newInstance();
            ClassMAPTable ex = (ClassMAPTable) o;
            ex.setMode("select");
            for (int i = 0; i < listeChamp.length; i++) {
                try {
                    int g = dr.findColumn(listeChamp[i].getNomColonne());
                } catch (SQLException sq) {
                    continue;
                }
                if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() == 0) && (listeChamp[i].getTypeJava().compareToIgnoreCase("java.lang.String") != 0)) {

                    setValChamp(ex, listeChamp[i].getNomClasse(), new Integer(dr.getInt(listeChamp[i].getNomColonne())));
                }
                if ((((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() > 0)) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("FLOAT") == 0)) && (listeChamp[i].getTypeJava().compareToIgnoreCase("java.lang.String") != 0)) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), new Double(dr.getDouble(listeChamp[i].getNomColonne())));
                }
                if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("VARCHAR2") == 0) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("CHAR") == 0)) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getString(listeChamp[i].getNomColonne()));
                }
                if (listeChamp[i].getTypeColonne().compareToIgnoreCase("DATE") == 0) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getDate(listeChamp[i].getNomColonne()));
                }
                if (listeChamp[i].getTypeColonne().contains("Timestamp")) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getTimestamp(listeChamp[i].getNomColonne()));
                }
            }
            ex.setNomTable(e.getNomTableSelect());
            if(map.containsKey(dr.getObject(attr2D).toString())){
                map.get(dr.getObject(attr2D).toString()).add(ex);
            }else{
                Vector v = new Vector();
                v.add(ex);
                map.put(dr.getObject(attr2D).toString(),v);
            }
        }
        return map;
    }
    /**
     * transformer le resultat de la requête de base de données en objet de mapping donné
     * @param dr : pointeur vers les données de la base
     * @param e : objet de mapping
     * @param c : connexion ouverte à la base de données
     * @return liste d'objets mappés aux données de la base de données
     * @throws Exception
     */
    public static Object[] transformeDataReader(ResultSet dr, ClassMAPTable e, Connection c) throws Exception {
        Vector retour = new Vector();
        String nomTable = e.getNomTableSelect();
        Champ[] listeChamp = ListeColonneTable.getFromListe(e, c);
        while (dr.next()) {
            Object o = Class.forName(e.getClassName()).newInstance();
            ClassMAPTable ex = (ClassMAPTable) o;
            ex.setMode("select");
            for (int i = 0; i < listeChamp.length; i++) {
                try {
                    int g = dr.findColumn(listeChamp[i].getNomColonne());
                } catch (SQLException sq) {
                    continue;
                }
                if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() == 0) && (listeChamp[i].getTypeJava().compareToIgnoreCase("java.lang.String") != 0)) {

                    setValChamp(ex, listeChamp[i].getNomClasse(), new Integer(dr.getInt(listeChamp[i].getNomColonne())));
                }
                if ((((listeChamp[i].getTypeColonne().compareToIgnoreCase("NUMBER") == 0) && (listeChamp[i].getPrecision() > 0)) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("FLOAT") == 0)) && (listeChamp[i].getTypeJava().compareToIgnoreCase("java.lang.String") != 0)) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), new Double(dr.getDouble(listeChamp[i].getNomColonne())));
                }
                if ((listeChamp[i].getTypeColonne().compareToIgnoreCase("VARCHAR2") == 0) || (listeChamp[i].getTypeColonne().compareToIgnoreCase("CHAR") == 0)) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getString(listeChamp[i].getNomColonne()));
                }
                if (listeChamp[i].getTypeColonne().compareToIgnoreCase("DATE") == 0) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getDate(listeChamp[i].getNomColonne()));
                }
                if (listeChamp[i].getTypeColonne().contains("Timestamp")) {
                    setValChamp(ex, listeChamp[i].getNomClasse(), dr.getTimestamp(listeChamp[i].getNomColonne()));
                }
            }
            ex.setNomTable(e.getNomTableSelect());
            retour.add(ex);

        }
        Object[] temp = (Object[]) java.lang.reflect.Array.newInstance(e.getClass().newInstance().getClass(), retour.size());
        retour.copyInto(temp);
        return temp;
    }
    /**
     * Enregistrer un objet et son historique à la base de données
     * @param e : objet à enregistrer
     * @param histo : objet respresentant l'historique à enregistrer
     * @param c : connexion ouverte à la base de données
     * @return les objets après enregistrements
     * @throws Exception
     */
    public static ClassMAPTable[] saveWithoutPkRangHisto(ClassMAPTable e, ClassMAPTable histo, Connection c) throws Exception {
        Connection cnx = null;
        ClassMAPTable[] retour = new ClassMAPTable[2];
        int estOuvert=0;
        try {
            if (c == null) {
                cnx = new UtilDB().GetConn();
                cnx.setAutoCommit(false);
                c = cnx;
                estOuvert=1;
            }
            retour[0] = saveWithoutPkRang(e, c);
            retour[1] = saveWithoutPkRang(histo, c);
            if (cnx != null && estOuvert==1) {
                cnx.commit();
            }
            return retour;
        } catch (Exception ex) {
            if (cnx != null && estOuvert==1) {
                cnx.rollback();
            }
            throw new Exception("ERREUR INSERT_TABLEtttttttttttttttttt " + ex.getMessage());
        } finally {
            if (cnx != null && estOuvert==1) {
                cnx.close();
            }
        }
    }
    /**
     * Enregistrer un objet de mapping sans le pk rang et avec une entrée d'historique
     * @param e : objet à enregistrer
     * @param refUser : id de l'utilisateur courant
     * @param c : connexion ouverte à la base de données
     * @return objet enregistré et son historique enregistré
     * @throws Exception
     */
    public static ClassMAPTable[] saveWithoutPkRangHisto(ClassMAPTable e, String refUser, Connection c) throws Exception {
        MapHistorique histo = new MapHistorique(e.getNomTable(), "insert", refUser, e.getTuppleID());
        return saveWithoutPkRangHisto(e, histo, c);
    }
    /**
     * Enregistrement sans pk rang d'un objet
     * @param e : objet à enregistrer
     * @param c : connexion ouverte à la base de données, si null spécifié, une connexion interne sera ouverte
     * @return Objet après enregistrement
     * @throws Exception
     */
    public static ClassMAPTable saveWithoutPkRang(ClassMAPTable e, Connection c) throws Exception {
        Connection cnx = null;
        int estOuvert=0;
        try {
            if (c == null) {
                cnx = new UtilDB().GetConn();
                c = cnx;
                estOuvert=1;
            }
            Champ[] listeC = ListeColonneTable.getFromListe(e, c);
            String comande = "insert into " + e.getNomTable() + " values (";
            comande = comande + "'" + getValeurInsert(e, listeC[0].getNomClasse().getName()) + "'";
            for (int i = 1; i < listeC.length; i++) {
                comande = comande + ",";
                if (getValeurInsert(e, listeC[i].getNomClasse().getName()) == null) {
                    comande = comande + " null ";
                } else {
                    comande = comande + "'" + getValeurInsert(e, listeC[i].getNomClasse().getName()) + "'";
                }
            }
            comande = comande + ")";
            Statement st = c.createStatement();
            st.executeUpdate(comande);
            return e;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (cnx != null && estOuvert==1) {
                cnx.close();
            }
        }

    }
    /**
     * recherche groupée  avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, String apresWhere, String[] nomColSomme, String ordre, Connection c) throws Exception {
        e.setMode("select");
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + " from " + e.getNomTableSelect() + " where " + makeWhere(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + ordre;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",rowNum as r from (" + selectInterne + "))";
			st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            if (ouvertInterne == true && c != null) {
                c.close();
            }
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
     /**
     * recherche groupée paginée avec une ligne par page avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @param nppa nombre par page
     * @param count
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupeM(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int nppa, String count) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            int npp;
            if (nppa <= 0) {
                npp = Parametre.getNbParPage();//(ParamDossier.getParam("%")).getNombrePage();
            } else {
                npp = nppa;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String apresWR = "";
            String orderby = "";
            char[] ch = {' '};
            String[] tabstr = Utilitaire.split(apresWhere, ch);
            int tmp = 0;
            for (int i = 0; i < tabstr.length; i++) {
                //System.out.println(" === "+tabstr[i].trim()+" ===");
                if (tabstr[i].trim().compareToIgnoreCase("order") != 0 && tmp == 0) {
                    apresWR += " " + tabstr[i].trim();
                } else {
                    tmp = 1;
                    orderby += " " + tabstr[i].trim();
                }
            }
            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + ", " + getSelectNombreGroupe(e, groupe, count) + " as nombrepargroupe from " + e.getNomTableSelect() + " p where " + makeWhereOr(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + orderby;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",nombrepargroupe,rowNum as r from (" + selectInterne + ")) where r between " + indiceInf + " and " + indiceSup;
            //System.out.print(" ==== +++AMBOARA+++ ===== " + comande);
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            //rs.setResultatCaste(resultat);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            if (ouvertInterne == true && c != null) {
                c.close();
            }
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public static String getSelectNombreGroupe(ClassMAPTable e, String[] groupe, String groupby) {
        String ret = "(select count(count(*)) from " + e.getNomTableSelect() + " where 1< 2";
        for(String gr : groupe) {
            ret += " and upper(" + gr + ") like (p." + gr + ")";
        }
        ret += " group by " + groupby + ")";
        return ret;
    }
    
    
    /**
     * recherche groupée paginée avec une ligne par page avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupeM(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            int npp;
            npp = 1;

            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);

            String pagination = "";
            if (numPage >= 0) {
                pagination = "where r between " + indiceInf + " and " + indiceSup;
            }

            String apresWR = "";
            String orderby = "";
            char[] ch = {' '};
            String[] tabstr = Utilitaire.split(apresWhere, ch);
            int tmp = 0;
            for (int i = 0; i < tabstr.length; i++) {
                if (tabstr[i].trim().compareToIgnoreCase("order") != 0 && tmp == 0) {
                    apresWR += " " + tabstr[i].trim();
                } else {
                    tmp = 1;
                    orderby += " " + tabstr[i].trim();
                }
            }

            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + ", COUNT(*) as nombrepargroupe from " + e.getNomTableSelect() + " where " + makeWhereOr(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + ordre;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",nombrepargroupe,rowNum as r from (" + selectInterne + ")) where r between " + indiceInf + " and " + indiceSup;
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            if (ouvertInterne == true && c != null) {
                c.close();
            }
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    /**
     * recherche groupée paginée avec récapitulation 
     * @param e : objet de mapping
     * @param groupe : liste de colonnes de groupages sans somme
     * @param sommeGroupe : liste de colonnes avec sommes
     * @param colInt : colonnes d'intervalle de filtre
     * @param valInt : liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage : numéro de page actuel
     * @param apresWhere : requête SQL après where pour le filtre
     * @param nomColSomme : liste de colonnes de somme pour la récapitulation
     * @param ordre : requête d'ordre
     * @param c : connexion à la base de données, ouverte implicitement si valeur null renseignée
     * @param nppa : nombre d'élèment par page, si négatif le nombre d'élèment par page de la configuration sera pris
     * @return instance avec les resultats de recherche et les valeurs des sommes de récapitulation
     * @throws Exception
     */
    public static ResultatEtSomme rechercherPageGroupeM(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int nppa) throws Exception {
        ResultSet dr = null;
        Statement st = null;
        boolean ouvertInterne = false;
        String compteLigne="count(*)";
        if(e.getNomTable().contains("PAIE_EDITION")) 
        {
            compteLigne="count(distinct idPersonnel)";
        }
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                ouvertInterne = true;
            }
            int npp;
            if (nppa <= 0) {
                npp = Parametre.getNbParPage();
            } else {
                npp = nppa;
            }
            int indiceInf = (npp * (numPage - 1)) + 1;
            int indiceSup = npp * (numPage);
            String apresWR = "";
            String orderby = "";
            char[] ch = {' '};
            String[] tabstr = Utilitaire.split(apresWhere, ch);
            int tmp = 0;
            for (int i = 0; i < tabstr.length; i++) {
                if (tabstr[i].trim().compareToIgnoreCase("order") != 0 && tmp == 0) {
                    apresWR += " " + tabstr[i].trim();
                } else {
                    tmp = 1;
                    orderby += " " + tabstr[i].trim();
                }
            }
            String selectInterne = "select " + getListeChampGroupeSomme(groupe, sommeGroupe) + ", "+ compteLigne + "as nombrepargroupe from " + e.getNomTableSelect() + " where " + makeWhereOr(e) + makeWhereIntervalle(colInt, valInt) + " " + apresWhere + " group by " + getListeChampGroupeSomme(groupe, null) + " " + orderby;
            String comande = "select * from (select " + getListeChampGroupeSimple(groupe, sommeGroupe) + ",nombrepargroupe,rowNum as r from (" + selectInterne + ")) where r between " + indiceInf + " and " + indiceSup;
            st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            dr = st.executeQuery(comande);
            Object[] resultat = transformeDataReader(dr, e, c);
            double[] somme = calculSommeNombreGroupe(selectInterne, nomColSomme, c);
            ResultatEtSomme rs = new ResultatEtSomme(resultat, somme);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur durant la recherche groupee " + ex.getMessage());
        } finally {
            if (ouvertInterne == true && c != null) {
                c.close();
            }
            if (dr != null) {
                dr.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     * Formatter la partie après where pour supporter la recherche de chaine de caractère
     * @param e : objet de mapping 
     * @return partie de la requête après where
     * @throws Exception
     */
    public static String makeWhereOr(ClassMAPTable e) throws Exception {
        String retour = "1<2";
        Champ[] ch = ListeColonneTable.getFromListe(e, null);
        for (int k = 0; k < ch.length; k++) {
            Object valeurk = getValeurFieldByMethod(e, ch[k].getNomClasse().getName());
            boolean testDaty = ((ch[k].getNomClasse().getType().toString() == "System.DateTime"));
            boolean testNombre = ((ch[k].getNomClasse().getType().getName().compareToIgnoreCase("double") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("int") == 0) || (ch[k].getNomClasse().getType().getName().compareToIgnoreCase("short") == 0));
            if ((testNombre == false) && (testDaty == false) && (valeurk != null) && (valeurk.toString().compareToIgnoreCase("") != 0) && (valeurk.toString().compareToIgnoreCase("%") != 0)) {
                String temp = valeurk.toString();
                String concat = "like ('";
                String after = "')";
                if (temp.lastIndexOf(",") != -1) {
                    concat = "in ('";
                    after = "')";
                }
                retour = retour + " and upper(" + ch[k].getNomClasse().getName() + ") " + concat + "" + valeurk.toString().toUpperCase() + "" + after;
            }
        }
        return retour;
    }



    public static String makeWhereMotsCles(ClassMAPTable e, String motCleValeur) {
        String motCleModifie = motCleValeur.replace(" ", "%").toUpperCase();
        String[] motCles = e.getMotCles();
        String whereClause = "";
        if (motCles.length > 0) {  
            for (int i = 0; i < motCles.length; i++) {
                whereClause += "UPPER(" + motCles[i] + ") LIKE '%" + motCleModifie + "%'";
                if (i < motCles.length - 1) {
                    whereClause += " OR ";
                }
            }
        }
        return whereClause;
    }
    public static String getValeurFieldByMotCle(ClassMAPTable e) throws Exception{
        return getValeurFieldByMotCle(e, e.getValMotCles(), " - ");
    }
    public static String getValeurFieldByMotCle(ClassMAPTable e,String[] mts,String separator) throws Exception  {
        String valeurAffiche="";
        if (mts.length > 0) {  
            for (int j = 0; j < mts.length; j++) {
                Object val=getValeurFieldByMethod(e, mts[j]);
                if (val==null){
                    valeurAffiche+="N/A";
                }else{
                    valeurAffiche+=(val.toString()).replace("\n", " ");
                }
                if (j < mts.length - 1) {
                    valeurAffiche += separator;
                }
            }
        }
        return valeurAffiche;
    }




}
