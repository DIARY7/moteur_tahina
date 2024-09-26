package user;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import bean.ClassUser;
import bean.ListeColonneTable;
import bean.ClassEtat;
import bean.ClassMAPTable;
import bean.ResultatEtSomme;
import bean.TypeObjet;
import utilitaire.UtilDB;
import historique.MapUtilisateur;
import historique.MapHistorique;
import historique.MapUtilisateurServiceDirection;
import historique.HistoriqueEJBClient;
import historique.UtilisateurUtil;
import bean.ClassMere;
import bean.ClassFille;
import bean.CGenUtil;
import bean.ErreurDAO;
import bean.TypeObjetUtil;
import bean.UploadPj;
import bean.ValidationException;
import bean.exceptions.AlertException;
import historique.HistoriqueLocal;
import historique.MapRoles;
import mg.cnaps.configuration.Configuration;
import mg.cnaps.notification.NotificationService;
import mg.cnaps.notification.NotificationLibelle;
import mg.cnaps.notification.NotificationMessage;
import mg.cnaps.notification.NotificationLibelle;
import mg.cnaps.log.LogService;
import mg.cnaps.utilisateur.CNAPSUser;
import utilitaire.UtilitaireMetier;
import utilisateur.UtilisateurInterim;
import utilitaire.Utilitaire;
import utilisateur.HomePageURL;
import utilisateur.ConstanteUtilisateur;
import lc.Direction;
import lc.DirectionUtil;
import constante.ConstanteEtat;
import bean.Constante;
import javax.ejb.AccessTimeout;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import utilisateur.UtilisateurRole;
import modules.GestionRole;
import mg.cnaps.messagecommunication.Message;
import service.UploadService;

/**
 * Bean implémentant UserEJB représentant l'EJB de session d'un utilisateur. Ce
 * bean va être initialisé pour chaque session d'utilisateur ouverte niveau
 * client. Il garde les informations sur les utilisateurs et permet de réaliser
 * des actions(modifier, supprimer, lire) et de garder les historiques de ces
 * actions.
 */
@Stateful
@AccessTimeout(10000)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserEJBBean implements UserEJB {

    TypeObjet caisse = null;
    TypeObjet[] listeSource = null;
    MapUtilisateurServiceDirection u;
    MapUtilisateur uVue;
    Configuration[] listeConfig;
    String type;
    CNAPSUser cnapsUser;
    String home_page = "accueil.jsp";
    String idDirection;
    private String addrIp;
    private HashMap<String, String> mapAutoComplete = new HashMap<>();
    public ValidationException validation;

    public TypeObjet getCaisse() {
        return caisse;
    }

    public void setCaisse(TypeObjet caisse) {
        this.caisse = caisse;
    }

    public MapUtilisateurServiceDirection getU() {
        return u;
    }

    public void setU(MapUtilisateurServiceDirection u) {
        this.u = u;
    }

    public MapUtilisateur getuVue() {
        return uVue;
    }

    public void setuVue(MapUtilisateur uVue) {
        this.uVue = uVue;
    }

    public Configuration[] getListeConfig() {
        return listeConfig;
    }

    public void setListeConfig(Configuration[] listeConfig) {
        this.listeConfig = listeConfig;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public CNAPSUser getCnapsUser() {
        return cnapsUser;
    }

    public void setCnapsUser(CNAPSUser cnapsUser) {
        this.cnapsUser = cnapsUser;
    }

    public String getHome_page() {
        return home_page;
    }

    public void setHome_page(String home_page) {
        this.home_page = home_page;
    }

    public TypeObjet[] getListeSource() {
        return listeSource;
    }

    public void setListeSource(TypeObjet[] listeSource) {
        this.listeSource = listeSource;
    }

    public String insertMereLierFilles(ClassMere mere , ClassFille fille , String[] filles , String colonneFille, String colonneMere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            String insertMere = insertMereLierFilles(mere, fille , filles, colonneFille , colonneMere , c);
            
            c.commit();
            return insertMere;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    
    public String insertMereLierFilles(ClassMere mere , ClassFille fille , String[] filles , String colonneFille, String colonneMere,Connection c ) throws Exception{        
        try{             
            ClassFille[] listeClassFilles =(ClassFille[])CGenUtil.rechercher(fille, null, null,c," AND ID IN ("+ Utilitaire.tabToString(filles, "'", ",")+")");
            mere.setFille(listeClassFilles);
            mere.createObject(u.getTuppleID(),c);            
            String valeurMere= CGenUtil.getValeurInsert(mere, colonneMere);                        
            for(int i=0 ; i<listeClassFilles.length ; i++ ){
                /*if (listeClassFilles[i] instanceof ComptaSousEcriture) {
                    listeClassFilles[i].setValChamp("lettrage", mere.getTuppleID());
                    listeClassFilles[i].updateToTable(c);
                }*/
                
                CGenUtil.setValChamp(listeClassFilles[i],CGenUtil.getField(listeClassFilles[i],colonneFille ), valeurMere);
            }
            mere.updateToTableWithHisto(u.getTuppleID(),c);
            return mere.getTuppleID();
            
        }catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public int desactiveUtilisateur(String ref, String refUser) throws Exception {
        try {
            historique.AnnulationUtilisateur au = new historique.AnnulationUtilisateur(ref);
            historique.MapHistorique h = new historique.MapHistorique("Utilisateurs", "annule", refUser, ref);
            h.setObjet("historique.AnnulationUtilisateur");
            au.insertToTable(h);
            return 1;
        } catch (ErreurDAO ex) {
            throw new bean.ErreurDAO(ex.getMessage());
        }
    }

    @Override
    public Object updateObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille, Connection c) throws Exception {
        boolean in = false;
        Object ret = null;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
                in = true;
            }
            Object idmere = updateObject(mere, c);
            ret = updateObjectMultiple(fille, colonneMere, ((ClassMAPTable) idmere).getTuppleID(), c);
            ret = idmere;
            if (in) {
                c.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (in && c != null) {
                c.rollback();
            }
            throw ex;
        } finally {
            if (in && c != null) {
                c.close();
            }
        }
        return ret;

    }

    /**
     * Cette fonction sert à modifier ou à inserer un objet selon l'existance de
     * l'identifiant dans l'objet
     *
     * @param o classe de mapping
     * @param colonneMere attribut correspondant au lien de parent
     * @param idmere identifiant de l'objet parent
     * @param c connexion ouverte à la base de données
     * @return les objets après modification ou insertion
     * @throws Exception
     */
    public Object[] updateObjectMultiple(ClassMAPTable[] o, String colonneMere, String idmere, Connection c) throws Exception {
        try {
            Object[] ret = new Object[o.length];
            for (int i = 0; i < o.length; i++) {
                o[i].setValChamp(colonneMere, idmere);
                if (!o[i].getTuppleID().equals("")) {
                    ret[i] = updateObject(o[i], c);
                } else {
                    ret[i] = createObject(o[i], c);
                }
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }

    }

    /**
     * Tester si le nom d'utilisateur et le mot de passe correspondant à un
     * utilisateur
     *
     * @param user nom d'utilisateur
     * @param pass mot de passe non crypté de l'utilisateur
     * @param interim autorisé interim ou pas
     * @param service service où on essaie de se connecter
     * @return utilisateur correspondant au nom d'utilisateur et mot de passe
     * @throws Exception
     */
    public MapUtilisateurServiceDirection testeValide(String user, String pass, String interim, String service) throws Exception {
        try {
            historique.UtilisateurUtil uI = new UtilisateurUtil();
            return (MapUtilisateurServiceDirection) uI.testeValide(user, pass);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Cette fonction sert à réaliser une recherche paginée avec nombre
     * d'élèments de la page fixé au nombre configuré si nombre d'élèments
     * donnés invalides
     *
     * @param e objet de mapping
     * @param colInt liste des intervalles des intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page actuel
     * @param apresWhere partie de requête après where
     * @param nomColSomme liste des colonnes de somme
     * @param c connexion ouverte à la base de données Le nombre d'élèment dans
     * une page n'est pas preciser
     */
    @Override
    public ResultatEtSomme getDataPageMax(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c) throws Exception {
        return getDataPageMax(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, 0);
    }

    /**
     * Cette fonction sert à rechercher une paginée avec nombre d'élèments de la
     * page fixé au nombre configuré si nombre d'élèments donnés invalides
     *
     * @param e objet de mapping
     * @param colInt liste des intervalles des intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page actuel
     * @param apresWhere partie de requête après where
     * @param nomColSomme liste des colonnes de somme
     * @param c connexion ouverte à la base de données
     * @param npp : nombre d'élèment dans une page
     */
    @Override
    public ResultatEtSomme getDataPageMax(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            ClassUser temp = (ClassUser) e;
            //apresWhere = testRangUser(e, c) + apresWhere;
        }
        if (e.getNomTableSelect().compareToIgnoreCase("SIG_TRAVAILLEUR_INFO_COMPLET") == 0) {
            return CGenUtil.rechercherPageMaxSansRecap(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, npp);
        }
        return CGenUtil.rechercherPageMax(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, npp);
    }

//    /**
//     * Cette fonction permet de terminer l'interim de l'utilisateur
//     * @param ref id de l'objet interim
//     * @param refUser id de l'utilisateur qui finit l'interim 
//     * @return 1 si tout se passe bien
//     * @throws Exception
//     */
//    public int terminerInterimUtilisateur(String ref, String refUser) throws Exception {
//        try {
//            String temps = refUser;
//            if (refUser != null && refUser.contains("/")) {
//                String[] g = Utilitaire.split(refUser, "/");
//                refUser = g[0];
//            }
//            UtilisateurInterim[] lui = (UtilisateurInterim[]) CGenUtil.rechercher(new UtilisateurInterim(), null, null, " AND ID='" + ref + "'");
//            if (lui[0].getEtat() == ConstanteEtat.getEtatCreer()) {
//                throw new Exception("Impossible de terminer. Utilisateur interim non activer");
//            }
//            if (lui[0].getEtat() == ConstanteUtilisateur.acteTerminer) {
//                throw new Exception("Impossible de terminer. Utilisateur interim terminer");
//            }
//
//            lui[0].setEtat(ConstanteUtilisateur.acteTerminer);
//            lui[0].setDate_fin(Utilitaire.dateDuJourSql());
//            lui[0].upDateToTable();
//            historique.MapHistorique h = new historique.MapHistorique("utilisateur.UtilisateurInterim", "Utilisateur interim terminer par " + temps, refUser, ref);
//            h.setObjet("utilisateur.UtilisateurInterim");
//            h.insertToTable();
//            return 1;
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }
//
//    /**
//     * Cette fonction permet de terminer l'interim de l'utilisateur
//     * @param ref id de l'objet interim
//     * @return 1 si tout se passe bien
//     * @throws exception si l'utilisateur n'as pas le droit 
//     */
//
//    @Override
//    public int terminerInterimUtilisateur(String ref) throws Exception {
//        try {
//            if (u.getIdrole().compareTo("admin") == 0 || u.getIdrole().compareTo("dg") == 0) {
//
//                int i = terminerInterimUtilisateur(ref, u.getTuppleID());
//                return i;
//            } else {
//                throw new Exception("ERREUR DE DROIT");
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }
    /**
     * @deprecated les paramètres ne sont pas utilisées dans la fonction
     * @param o objet de mapping
     * @param listeIdObjet liste des id de l'objet
     */
    @Override
    public int cloturerObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            c.commit();
            return 0; //r
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Cette fonction permet de modifier la notification comme la detination
     */
    @Override
    public void transfererNotification(String idNotification, String idpers, String service, String direction) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            NotificationService.updateNotif(idNotification, service, direction, idpers, c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Supprimer un objet de mapping
     *
     * @param o objet de mapping concerné
     * @param conn connexion ouverte à la base de données
     */
    @Override
    public void deleteObjetFille(ClassMAPTable o, Connection conn) throws Exception {
        try {
            o.deleteToTableWithHisto(u.getTuppleID(), conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Supprimer un objet de mapping
     *
     * @param o objet de mapping concerné
     * @param conn connexion ouverte à la base de données
     */
    @Override
    public void deleteObjetFille(ClassMAPTable o) throws Exception {
        Connection conn = null;
        try {
            conn = new UtilDB().GetConn();
            conn.setAutoCommit(false);
            deleteObjetFille(o, conn);
            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            ex.printStackTrace();
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Recherche paginée avec nombre d'élèments de la page fixé au nombre
     * configuré si nombre d'élèments donnés invalides, puis les colonnes de
     * sommes ignorées
     *
     * @param e objet de mapping
     * @param colInt liste de colonnes d'intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page actuel
     * @param apresWhere partie de requête après where
     * @param nomColSomme liste des colonnes de somme
     * @param c connexion ouverte à la base de données
     * @param nbPage nombre d'élèment dans une page
     * @return instance avec les résultas de la recherches
     * @throws Exception
     */
    @Override
    public ResultatEtSomme getDataPageMaxSansRecap(ClassMAPTable e, String[] colInt, String[] valInt, int numPage,
            String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            ClassUser temp = (ClassUser) e;
            //apresWhere = testRangUser(e, c) + apresWhere;
        }

        return CGenUtil.rechercherPageMaxSansRecap(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, npp);
    }

    /**
     * Avoir le maximum d'id dans une table avec une critère
     *
     * @param table table dans la base de donnée
     * @param colonne colonne pour le critère
     * @param listeCritere critère dans la partie where
     * @return id du l'objet avec le maximum en ID
     */
    @Override
    public String getMaxId(String table, String[] colonne, String[] listeCritere) throws Exception {
        String retour = "----";
        int tailleCrt = listeCritere.length;
        Connection c = null;
        try {
            String temp = "select max(id) from " + table + " ";
            c = new UtilDB().GetConn();
            java.sql.Statement sta = c.createStatement();
            temp += "where " + colonne[0] + " ='" + listeCritere[0] + "'";
            //System.out.println("TY LE TEMP "+temp);
            if (tailleCrt > 1) {
                for (int i = 1; i < tailleCrt; i++) {
                    temp += " and " + colonne[i] + " = '" + listeCritere[i] + "'";
                }
            }
            java.sql.ResultSet res = sta.executeQuery(temp);
            res.next();
            retour = res.getString(1);
        } catch (Exception e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return retour;
    }

    /**
     * Avoir le dernier id dans une table
     *
     * @param table nom de la table dans la base de donnée
     * @return id du l'objet avec le maximum en ID
     */
    @Override
    public String getMaxId(String table) throws Exception {
        Connection c = null;
        String retour = "---";
        try {
            c = new UtilDB().GetConn();
            java.sql.Statement sta = c.createStatement();
            java.sql.ResultSet res = sta.executeQuery("select max(id) from " + table);
            res.next();
            retour = res.getString(1);
        } catch (Exception e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return retour;
    }

    /**
     * ¨
     *
     * @deprecated ne fait rien
     */
    @Override
    public void updateEtat(ClassMAPTable e, int valeurEtat, String id) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            //updateEtat(e, valeurEtat, id, c);
            c.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Sert à inserer pour une fonction
     *
     * @param nomTable nom du table dans la base
     * @param proc procedure pour génerer l'ID
     * @param pref suffixe de l'ID pour la géneration
     * @param typ valeur du type
     * @param desc description du type d'objet
     * @return id du type créer
     */
    @Override
    public String createTypeObjet(String nomTable, String proc, String pref, String typ, String desc) throws Exception {
        try {
            if (u.getRang() >= 4) {
                TypeObjet to = new TypeObjet(nomTable, proc, pref, typ, desc);
                String refuser = u.getTuppleID();
                if (refuser != null && refuser.contains("/")) {
                    String[] g = Utilitaire.split(u.getTuppleID(), "/");
                    refuser = g[0];
                }

                MapHistorique histo = new MapHistorique(nomTable, "Insertion par " + u.getTuppleID(), refuser, to.getId());
                histo.setObjet("bean.TypeObjet");
                to.insertToTable(histo);
                String s = to.getId();
                return s;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Sert à modifier une table de catégorie ou de type simple.
     *
     * @param table nom du table dans la base
     * @param id identifiant
     * @param typ valeur de la catégorie
     * @param desc description détaillée de la catégorie
     */
    @Override
    public String updateTypeObjet(String table, String id, String typ, String desc) throws Exception {
        try {
            if (u.getRang() >= 4) {
                TypeObjet to = new TypeObjet(table, id, typ, desc);
                String refuser = u.getTuppleID();
                if (refuser != null && refuser.contains("/")) {
                    String[] g = Utilitaire.split(u.getTuppleID(), "/");
                    refuser = g[0];
                }

                MapHistorique histo = new MapHistorique(table, "update", refuser, to.getId());
                histo.setObjet("bean.TypeObjet");
                to.updateToTableWithHisto(histo);
                String s = to.getId();
                return s;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @deprecated rechercher des objets de catégorie ou de type simple
     * @param nomTable nom de la table en base
     * @param id id de l'objet à rechercher
     * @param typ valeur de l'objet à rechercher
     * @return un tableau de type objet
     */
    @Override
    public TypeObjet[] findTypeObjet(String nomTable, String id, String typ) throws Exception {
        try {
            TypeObjetUtil to = new TypeObjetUtil();
            TypeObjet atypeobjet[] = to.findTypeObjet(nomTable, id, typ);
            return atypeobjet;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * permet de supprimer de catégorie ou de type simple et de l'inserer dans
     * l'historique
     *
     * @param nomTable nom de la table en base
     * @param id id de l'objet à supprimer
     * @param typ valeur de l'objet à supprimer
     */
    @Override
    public int deleteTypeObjet(String nomTable, String id) throws Exception {
        try {
            if (u.getRang() >= 4) {
                TypeObjet to = new TypeObjet(nomTable, id, "-", "-");
                String refuser = u.getTuppleID();
                if (refuser != null && refuser.contains("/")) {
                    String[] g = Utilitaire.split(u.getTuppleID(), "/");
                    refuser = g[0];
                }

                MapHistorique h = new MapHistorique(nomTable, "delete", refuser, id);
                h.setObjet("bean.TypeObjet");
                to.deleteToTable(h);
                int i = 1;
                return i;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Rechercher des utilisateurs
     *
     * @param iduser id de l'utilisateur
     * @param loginuser nom d'utilisateur
     * @param pwduser mot de passe de l'utilisateur pas encore crypté
     * @param nomuser nom de l'utilisateur
     * @param adruser adresse de l'utilisateur
     * @param teluser telephone de l'utilisateur
     * @param idrole role associé
     * @return liste des utilisateurs
     */
    @Override
    public MapUtilisateur[] findUtilisateurs(String refuser, String loginuser, String pwduser, String nomuser,
            String adruser, String teluser, String idrole) throws Exception {
        try {
            int[] a = {1, 2, 3, 4, 5, 6, 7}; //Donne le numero des champs sur lesquelles on va mettre des criteres
            String[] val = new String[a.length];
            val[0] = refuser;
            val[1] = loginuser;
            val[2] = pwduser;
            val[3] = nomuser;
            val[4] = adruser;
            val[5] = teluser;
            val[6] = idrole; //Affecte des valeurs aux criteres
            UtilisateurUtil cu = new UtilisateurUtil();
            return (MapUtilisateur[]) cu.rechercher(a, val);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Obtenir l'utilisateur courant
     *
     * @return l'utilisateur courant
     */
    @Override
    public MapUtilisateur getUser() {
        return u;
    }

    /**
     * Cette fonction permet de marquer comme lu(etat 2) un objet
     *
     * @param o de type ClassEtat
     * @param listeIdObjet liste des Ids des objets à marquer comme lu
     * @param c connexion ouverte à la base de données
     * @return 1 si le fonction fonctionne bien .
     * @throws Exception
     */
    public int marquerLuMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception {
        try {
            String script = Utilitaire.tabToString(listeIdObjet, "'", ",");
            String apresSet = " etat = 2 where " + o.getAttributIDName() + " in (" + script + " )";
            o.updateToTable(apresSet, c);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Viser(Valider -> changer état en 11) plusieurs objets en même temps
     *
     * @param o classeEtat
     * @param listeIdObjet liste id des objets à viser
     * @return nombre de ligne modifiée en base
     */
    @Override
    public int viserObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int r = viserObjectMultiple(o, listeIdObjet, c);

            c.commit();
            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public Object validerObject(ClassMAPTable o, Connection c) throws Exception {

        try {
            return ValiderObject.validerObject(c, o, this, u, listeConfig);
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Viser(Valider -> changer état en 11) plusieurs objets en même temps
     *
     * @param o classeEtat
     * @param listeIdObjet liste id des objets à viser
     * @param c connexion ouverte à la base de données
     * @return nombre de ligne modifiée en base
     */
    public int viserObjectMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception {
        try {
            String script = Utilitaire.tabToString(listeIdObjet, "'", ",");
            String table_name = o.getNomTable();

            String apw = " and " + o.getAttributIDName() + " in (" + script + " )";
            //System.out.println("apw = " + apw);
            ClassEtat[] mapTableListe = (ClassEtat[]) CGenUtil.rechercher(o, null, null, c, apw);
            for (int i = 0; i < mapTableListe.length; i++) {
                mapTableListe[i].setMode("modif");
                mapTableListe[i].setNomTable(o.getNomTable());
                this.validerObject(mapTableListe[i], c);
            }
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Cette fonction permet de marquer comme lu(etat 2) un objet
     *
     * @param o de type ClassEtat
     * @param listeIdObjet liste des Ids des objets à marquer comme lu
     * @return 1 si le fonction fonctionne bien .
     * @throws Exception
     */
    @Override
    public int marquerLuMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int r = marquerLuMultiple(o, listeIdObjet, c);

            c.commit();
            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * @deprecated la fonction n'est pas fini jusqu'à la fin
     */
    public int retournerObjectMultiple(ClassEtat o, String[] listeIdObjet, String motif, Connection c) throws Exception {
        try {

            String script = Utilitaire.tabToString(listeIdObjet, "'", ",");
            String apw = " and " + o.getAttributIDName() + " in (" + script + " )";

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * @deprecated Cette fonction appelle la fonction retournerObjectMultiple
     * qui n'est pas fini jusqu'à la fin
     */
    @Override
    public int retournerObjectMultiple(ClassEtat o, String[] listeIdObjet, String motif) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int r = retournerObjectMultiple(o, listeIdObjet, motif, c);
            c.commit();
            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * @deprecated fonction qui n'est pas fini jusqu'à la fin
     */
    public int recuObjectMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception {
        try {

            String script = Utilitaire.tabToString(listeIdObjet, "'", ",");
            String apw = " and " + o.getAttributIDName() + " in (" + script + " )";

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * @deprecated Cette fonction appelle la fonction recuObjectMultiple qui
     * n'est pas fini jusqu'à la fin
     */
    @Override
    public int recuObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int r = recuObjectMultiple(o, listeIdObjet, c);
            c.commit();
            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public void setEtat(ClassEtat o, Integer paramT) throws Exception {
        String nomChamp = "etat";
        o.setValChamp(nomChamp, paramT);
    }

    /**
     * Sert à valider(changer l'état en validé en base de données) des objets en
     * masse
     *
     * @param o classeEtat liste des objets à valider
     * @param c connexion ouverte à la base de données
     * @return si tout est okay on obtient 1 sinon 0
     * @throws Exception
     */
    public int validerObjectMultiple(ClassEtat[] o, Connection c) throws Exception {
        try {
            for (int i = 0; i < o.length; i++) {
                ClassEtat map = ((ClassEtat[]) CGenUtil.rechercher(o[i], null, null, c, ""))[0];
                map.setMode("modif");
                setEtat(map, ConstanteEtat.getEtatValider());
                map.updateToTableWithHisto(u.getTuppleID(), c);
            }
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Sert à valider(changer l'état en validé en base de données) des objets en
     * masse
     *
     * @param o classeEtat liste des objets à valide
     */
    @Override
    public int validerObjectMultiple(ClassEtat[] o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int r = validerObjectMultiple(o, c);
            c.commit();
            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * @deprecated fonction qui n'est pas arrivée au but
     */
    @Override
    public Object finaliser(ClassMAPTable o, Connection c) throws Exception {
        try {
            o.setMode("modif");

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return null;
    }

    /**
     * cette fonction appelle la fonction {@code finaliser(ClassMAPTable o, Connection c)
     * } qui n'est pas arrivée à son but
     */
    @Override
    public Object finaliser(ClassMAPTable map) throws Exception {
        Connection c = null;
        try {
            c = (new UtilDB()).GetConn();
            Object ret = finaliser(map, c);
            //c.commit();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Mettre à jour les informations de l'utilisateur
     *
     * @param iduser id de l'utilisateur
     * @param loginuser nom d'utilisateur
     * @param pwduser mot de passe de l'utilisateur pas encore crypté
     * @param nomuser nom de l'utilisateur
     * @param adruser adresse de l'utilisateur
     * @param teluser telephone de l'utilisateur
     * @param idrole role associé
     * @param refuser id de l'utilisateur qui fait la modification
     * @return id de l'utilisateur qui vient d'être modifié
     */
    @Override
    public String updateUtilisateurs(String refuser, String loginuser, String pwduser, String nomuser, String adruser,
            String teluser, String idrole) throws Exception {
        HistoriqueLocal rl = null;
        try {
            rl = HistoriqueEJBClient.lookupHistoriqueEJBBeanLocal();
            if (u.getIdrole().compareTo("dg") == 0) {
                return rl.updateUtilisateurs(refuser, loginuser, pwduser, nomuser, adruser, teluser, idrole, u.getTuppleID());
            } else if (String.valueOf(u.getRefuser()).compareTo(refuser) == 0) {
                return rl.updateUtilisateurs(refuser, loginuser, pwduser, nomuser, adruser, teluser, u.getIdrole(), u.getTuppleID());
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Supprimer un utilisateur
     *
     * @param refuser identifiant informatique de l'utilisateur à supprimer
     * @return 1 si tout okay sinon 0
     * @throws Exception
     */
    @Override
    public int deleteUtilisateurs(String refuser) throws Exception {
        HistoriqueLocal rl = null;

        try {
            if (u.getIdrole().compareTo("admin") == 0 || u.getIdrole().compareTo("dg") == 0 || u.getIdrole().compareTo("adminFacture") == 0) {
                rl = HistoriqueEJBClient.lookupHistoriqueEJBBeanLocal();
                int i = rl.deleteUtilisateurs(refuser, u.getTuppleID());
                return i;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Créer un utilisateur
     *
     * @param loginuser nom d'utilisateur
     * @param pwduser mot de passe de l'utilisateur pas encore crypté
     * @param nomuser nom de l'utilisateur
     * @param adruser adresse de l'utilisateur
     * @param teluser telephone de l'utilisateur
     * @param idrole role associé
     * @param refuser id de l'utilisateur qui a fait l'insertion
     * @return id de l'utilisateur qui vient d'être créé
     */
    @Override
    public String createUtilisateurs(String loginuser, String pwduser, String nomuser, String adruser, String teluser,
            String idrole) throws Exception {
        HistoriqueLocal rl = null;
        try {
            if (u.getIdrole().compareTo("dg") == 0) {
                rl = HistoriqueEJBClient.lookupHistoriqueEJBBeanLocal();
                String s = rl.createUtilisateurs(loginuser, pwduser, nomuser, adruser, teluser, idrole, u.getTuppleID());
                return s;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Sert à desactiver une compte utilisateur
     *
     * @param ref identifiant de l'utilisateur à supprimer
     */
    @Override
    public int desactiveUtilisateur(String ref) throws Exception {
        try {
            if (u.getIdrole().compareTo("admin") == 0 || u.getIdrole().compareTo("dg") == 0) {

                int i = desactiveUtilisateur(ref, u.getTuppleID());
                return i;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Sert à activer une compte d'utilisateur
     *
     * @param ref
     */
    @Override
    public int activeUtilisateur(String ref) throws Exception {
        try {
            if (u.getIdrole().compareTo("admin") == 0 || u.getIdrole().compareTo("dg") == 0 || u.getIdrole().compareTo("adminFacture") == 0) {

                int i = activeUtilisateur(ref, u.getTuppleID());
                return i;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public int activeUtilisateur(String ref, String refUser) throws Exception {
        try {
            String temps = refUser;
            if (refUser != null && refUser.contains("/")) {
                String[] g = Utilitaire.split(refUser, "/");
                refUser = g[0];
            }
            UtilisateurInterim[] lui = (UtilisateurInterim[]) CGenUtil.rechercher(new UtilisateurInterim(), null, null, " AND ID='" + ref + "'");
            if (lui[0].getEtat() == ConstanteUtilisateur.acteActiver) {
                throw new Exception("Impossible de activer. Utilisateur interim activer");
            }
            lui[0].setEtat(ConstanteUtilisateur.acteActiver);
            lui[0].setDate_debut(Utilitaire.dateDuJourSql());
            lui[0].setDate_fin(null);
            lui[0].upDateToTable();
            historique.MapHistorique h = new historique.MapHistorique("utilisateur.UtilisateurInterim", "Utilisateur interim activer par " + temps, refUser, ref);
            h.setObjet("utilisateur.UtilisateurInterim");
            h.insertToTable();
            return 1;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Direction[] findDirection(String idDir, String libelledir, String descdir, String abbrevDir, String idDirecteur) throws Exception {

        try {
            String afterW = "";
            int[] numChamp = {1, 2, 3, 5};
            String[] val = {idDir, libelledir, descdir, abbrevDir};
            DirectionUtil du = new DirectionUtil();
            du.utiliserChampBase();
            //if(idDirecteur.compareToIgnoreCase("")==0 || idDirecteur.compareToIgnoreCase("%")==0) idDirecteur = "%";
            //afterW=" AND idDirecteur like  '" + idDirecteur +"'";
            return (Direction[]) du.rechercher(numChamp, val, "");

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public Configuration[] findConfiguration() throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            Configuration[] liste = (Configuration[]) CGenUtil.rechercher(new Configuration(), null, null, c, " order by typeconfig desc");
            return liste;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Tester si les credentials sont valides pour accéder à un service et
     * initialiser les valeurs de certains attributs du bean
     *
     * @param user nom d'utilisateur(identifiant)
     * @param pass mot de passe non crypté de l'utilisateur
     * @param interim 1 ou 0 selon interim dispo ou pas
     * @param service service auquel la personne essaie de se connecter
     */
    @Override
    public void testLogin(String user, String pass, String interim, String service) throws Exception {
        Connection c = null;
        try {

            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            u = testeValide(user, pass, interim, service);
            UtilisateurUtil crt = new UtilisateurUtil();
            uVue = crt.testeValide("utilisateurVue", user, pass);
            type = u.getIdrole();
            if (type.compareToIgnoreCase(bean.Constante.getIdRoleDirecteur()) == 0) {
                Direction d[] = findDirection("", "", "", "", String.valueOf(u.getRefuser()));
                if (d.length > 0) {
                    this.setIdDirection(d[0].getIdDir());
                } else {
                    type = u.getIdrole();
                }
            }
            CNAPSUser[] cnp = (CNAPSUser[]) CGenUtil.rechercher(new CNAPSUser(), null, null, c, " and id_utilisateur = '" + u.getRefuser() + "'");

            this.cnapsUser = (cnp != null) ? cnp[0] : null;
            if (interim != null && interim.compareToIgnoreCase("1") == 0 && cnp.length > 0) {
                LogService[] ls = (LogService[]) CGenUtil.rechercher(new LogService(), null, null, c, " AND ID = '" + service + "'");
                if (ls.length == 0) {
                    throw new Exception("SERVICE INTROUVABLE");
                }
                this.cnapsUser = new CNAPSUser(cnp[0].getCode_dr(), cnp[0].getUser_init(), cnp[0].getAgent_mo(), cnp[0].getAgent_disponible(), cnp[0].getUsername(), ls[0].getCode_service(), u.getRefuser() + "");
            }

            this.listeConfig = this.findConfiguration();

            TypeObjet to = new TypeObjet();
            to.setNomTable("SOURCE");
            setListeSource((TypeObjet[]) CGenUtil.rechercher(to, null, null, c, ""));

            u.setPwduser(getAddrIp());
            MapHistorique histo = new MapHistorique("login", "login", String.valueOf(u.getRefuser()), String.valueOf(u.getRefuser()));
            histo.setObjet("mg.cnaps.utilisateur.CNAPSUser");
            histo.setAction(histo.getAction() + "-" + getAddrIp());
            histo.insertToTable();
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Cette fonction sert à chercher l'url de la page d'accueil d'une personne
     * dependant de son service
     *
     * @param codeService service rattaché au role de l'utilisateur
     */
    @Override
    public String findHomePageServices(String codeService) throws Exception {
        Connection connection = null;
        try {
            connection = (new UtilDB()).GetConn();
            HomePageURL[] hommePageList = (HomePageURL[]) CGenUtil.rechercher(new HomePageURL(), null, null, connection, " and idrole = '" + codeService + "'");
            if (hommePageList != null && hommePageList.length > 0) {
                this.home_page = hommePageList[0].getUrlpage();
            }
            return this.home_page;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Lier un objet à plusieurs filles à l'aide de la classe de mapping
     * {@link bean.UnionIntraTable}
     *
     * @param nomtableMappage nom de table en base à utiliser pour stocker les
     * liaisons
     * @param nomProcedure nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille liste des ids à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param etat etat actuel de la liaison
     * @param c connexion ouverte à la base de données
     * @return null
     * @throws Exception
     */
    public String mapperMereFille(String nomtableMappage, String nomProcedure, String suffixeMap, String idMere, String[] idFille, String rem, String montant, String etat, Connection c) throws Exception {
        try {
            for (int i = 0; i < idFille.length; i++) {
                UtilitaireMetier.mapperMereToFille(nomtableMappage, nomProcedure, suffixeMap, idMere, idFille[i], rem, montant, u.getTuppleID(), etat, c);
            }

        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    /**
     * Lier un objet à plusieurs filles à l'aide de la classe de mapping
     * {@link bean.UnionIntraTable}
     *
     * @param e classe de mapping representant l'objet de liaison(avec nom de
     * table, procedure et séquence)
     * @param idMere id de l'objet mère à lier
     * @param idFille liste des ids à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param etat etat actuel de la liaison
     * @param c connexion ouverte à la base de données
     * @return null
     * @throws Exception
     */
    @Override
    public String mapperMereFille(ClassMAPTable e, String idMere, String[] idFille, String rem, String montant,
            String etat) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            mapperMereFille(e.getNomTable(), e.getNomProcedureSequence(), e.getINDICE_PK(), idMere, idFille, rem, montant, etat, c);
            c.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * Lier un objet à plusieurs filles à l'aide de la classe de mapping
     * {@link bean.UnionIntraTable} en utilisant le nom de la table pour génerer
     * le PK
     *
     * @param e pas important
     * @param nomTable nom de la table pour stocker les liaisons
     * @param idMere id de l'objet mère à lier
     * @param idFille liste des ids à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param etat etat actuel de la liaison
     * @param c connexion ouverte à la base de données
     * @return null
     * @throws Exception
     */
    @Override
    public String mapperMereFille(ClassMAPTable e, String nomTable, String idMere, String[] idFille, String rem,
            String montant, String etat) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            String nomProcedure = "getSeq" + Utilitaire.convertDebutMajuscule(nomTable);
            String indicePK = nomTable.substring(0, 3).toUpperCase();
            mapperMereFille(nomTable, nomProcedure, indicePK, idMere, idFille, rem, montant, etat, c);
            c.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * Supprimer la liaison entre un objet et ses filles
     *
     * @param e classe representant la liaison
     * @param idMere id de la mère
     * @param liste_id_fille liste des ids des filles à supprimer
     *
     *
     */
    @Override
    public void deleteMereFille(ClassMAPTable e, String idMere, String[] liste_id_fille) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            deleteMereFille(e.getNomTable(), idMere, liste_id_fille, c);
            c.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Supprimer la liaison entre un objet et ses filles
     *
     * @param nomtableMappage nom de la table stockant les liaisons
     * @param idMere id de la mère
     * @param liste_id_fille liste des ids des filles à supprimer
     * @param c connexion ouverte à la base de données
     *
     */
    public void deleteMereFille(String nomtableMappage, String idMere, String[] idFille, Connection c) throws Exception {
        try {
            for (int i = 0; i < idFille.length; i++) {
                UtilitaireMetier.deleteMereToFille(nomtableMappage, idMere, idFille[i], u.getTuppleID(), c);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Supprimer la liaison entre un objet et ses filles
     *
     * @param e classe representant la liaison
     * @param nomtableMappage nom de la table stockant les liaisons
     * @param idMere id de la mère
     * @param liste_id_fille liste des ids des filles à supprimer
     * @param c connexion ouverte à la base de données
     *
     */
    @Override
    public void deleteMereFille(ClassMAPTable e, String nomTable, String idMere, String[] liste_id_fille)
            throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            deleteMereFille(nomTable, idMere, liste_id_fille, c);

            c.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Réaliser une recherche paginée(recapitulation y compris) avec le nombre
     * d'élèment par page fixé au nombre donné dans le paramètre global
     *
     * @param e : objet de mapping
     * @param colInt liste de colonnes d'intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page à prendre
     * @param apresWhere filtre SQL après where
     * @param nomColSomme noms des colonnes à sommer
     * @param c : connexion ouverte à la base de données
     */
    @Override
    public ResultatEtSomme getDataPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage,
            String apresWhere, String[] nomColSomme, Connection c) throws Exception {
        return getDataPage(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, 0);
    }

    public String testRangUser(ClassMAPTable e, Connection c) throws Exception {
        String retour = "";
        if (e instanceof bean.ClassEtat || e instanceof bean.ClassUser) {
            if (e instanceof mg.cnaps.notification.NotificationLibelle) {
                return NotificationService.conditionLectureNotification(u);
            } else {
                UtilisateurRole[] utilEnCours = (UtilisateurRole[]) CGenUtil.rechercher(new UtilisateurRole(), null, null, c, " and refuser=" + u.getTuppleID());
                if (utilEnCours.length > 0) {
                    if (utilEnCours[0].getRang() < Constante.getRangMahitaNyHafa()) {
                        retour += " and iduser='" + utilEnCours[0].getRefuser() + "' ";
                    }
                }
            }
        }
        return retour;
    }

    /**
     * Sert à avoir les sommes de colonnes en tant que récapitulation
     *
     * @param e objet de mapping
     * @param colInt liste de colonnes d'intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page à prendre
     * @param apresWhere filtre SQL après where
     * @param nomColSomme noms des colonnes à sommer
     * @param c connexion ouverte à la base de données
     * @param npp nombre d'élèments dans une pages
     */
    @Override
    public ResultatEtSomme getDataPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage,
            String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            apresWhere = testRangUser(e, c) + apresWhere;
        }
        return CGenUtil.rechercherPage(e, colInt, valInt, numPage, apresWhere, nomColSomme, c, npp);
    }

    /**
     * Cette fonction fait la recherche groupée paginée avec récapitulation avec
     * nombre d'élèment dans une page fixé au valeur de configuration
     *
     * @param e objet de mapping
     * @param colInt liste de colonnes d'intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page à prendre
     * @param apresWhere filtre SQL après where
     * @param nomColSomme noms des colonnes à sommer
     * @param c connexion ouverte à la base de données
     * @param ordre requête d'ordre
     */
    @Override
    public ResultatEtSomme getDataPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt,
            String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c)
            throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            apresWhere = testRangUser(e, c) + apresWhere;
        }
        return CGenUtil.rechercherPageGroupe(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c);
    }

    /**
     * Cette fonction fait la recherche groupée paginée avec récapitulation
     *
     * @param e objet de mapping
     * @param colInt liste de colonnes d'intervalles
     * @param valInt liste des valeurs des colonnes d'intervalle
     * @param numPage numéro de page à prendre
     * @param apresWhere filtre SQL après where
     * @param nomColSomme noms des colonnes à sommer
     * @param c connexion ouverte à la base de données
     * @param ordre requête d'ordre
     * @param npp nombre d'élèments dans une pages
     */
    @Override
    public ResultatEtSomme getDataPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt,
            String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int npp)
            throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            apresWhere = testRangUser(e, c) + apresWhere;
        }
        return CGenUtil.rechercherPageGroupe(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c, npp);
    }

    /**
     * Permet obtenir le resultat de la recherche de objet de mapping filtré
     *
     * @param e objet de mapping
     * @param colInt colonnes avec intervalles
     * @param valInt valeurs des colonnes d'intervalles
     * @param c: connection
     * @param apresWhere where statement personalisé
     */
    @Override
    public Object[] getData(ClassMAPTable e, String[] colInt, String[] valInt, Connection c, String apresWhere)
            throws Exception {
        e.setMode("select");
        if (ListeColonneTable.getChamp(e, "iduser", c) != null) {
            apresWhere = testRangUser(e, c) + apresWhere;
        }
        if (c == null) {
            return CGenUtil.rechercher(e, colInt, valInt, apresWhere);
        }
        return CGenUtil.rechercher(e, colInt, valInt, c, apresWhere);
    }

    /**
     * Créer un objet avec trace de la personne modifiant
     *
     * @param o objet à créer
     * @return objet après la création
     */
    @Override
    public Object createObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = createObject(o, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

    /**
     * Créer un objet avec trace de la personne modifiant
     *
     * @param o objet à créer
     * @param c connexion ouverte à la base de données
     * @return objet après la création
     */
    @Override
    public Object createObject(ClassMAPTable o, Connection c) throws Exception {
        if (u.getRang() < 1) {
            throw new Exception("ERREUR DE DROIT");
        }
        try {
            if (o.getMode() != null && o.getMode().compareToIgnoreCase("annul") == 0) {
                o.setMode("annul");
                CreateObject.createObject(o, c, u, this, listeConfig, getListeSource());
                return ValiderObject.validerObject(c, o, this, uVue, listeConfig);
            } else {
                o.setMode("modif");
                return CreateObject.createObject(o, c, u, this, listeConfig, getListeSource());
            }
        } catch (Exception ex) {
            //c.rollback();
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Modifier un objet avec trace de la personne modifiant avec initialisation
     * interne de la base de données
     *
     * @param o objet à créer
     * @return objet après la modification
     */
    @Override
    public Object updateObject(ClassMAPTable o) throws Exception {
        Connection c = null;

        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            updateObject(o, c);
            c.commit();
            return o;
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * mettre à jour un objet de mapping
     *
     * @param o objet à mettre à jour
     * @param c connexion ouverte la base de données
     * @return objet après mise à jour
     * @throws Exception
     */
    public Object updateObjectSimple(ClassMAPTable o, Connection c) throws Exception {
        try {
            return UpdateObject.updateObject(o, c, u, this);
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * mettre à jour un objet de mapping Une connexion interne est initialisée
     * pour l'accès à la base de données
     *
     * @param o objet à mettre à jour
     * @return objet après mise à jour
     * @throws Exception
     */
    @Override
    public Object updateObjectSimple(ClassMAPTable o) throws Exception {
        Connection c = null;

        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            updateObjectSimple(o, c);
            c.commit();
            return o;
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Sert à valider(viser) un objet
     *
     * @param o C'est l'objet à valider
     * @return l'objet apres la mis à jour
     */
    @Override
    public Object validerObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = validerObject(o, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * *
     * @deprecated les tests devraient se faire au niveau de ClassEtat Tester la
     * possibilité d'un rejet de la fille selon l'etat actuel de l'objet
     * @param o objet fille à rejeter
     * @param mere objet mère à rejeter
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void testRejectEtat(ClassMAPTable o, ClassMAPTable mere, Connection c) throws Exception {
        try {
            String id = o.getValInsert("mere");
            ClassMAPTable cl = (ClassMAPTable) Class.forName(mere.getClassName()).newInstance();
            cl.setNomTable(mere.getNomTable());
            ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(cl, null, null, c, " and id = '" + id + "'");
            if (liste.length == 0) {
                throw new Exception("Objet inexistante");
            }
            if (mere.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) == ConstanteEtat.getEtatAnnuler()) {
                    throw new Exception(" Impossible de rejeter. Objet annul�");
                }
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) == ConstanteEtat.getEtatRejeter()) {
                    throw new Exception(" Impossible de rejeter. Objet d�j� rejet�");
                }
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) >= ConstanteEtat.getEtatValider()) {
                    throw new Exception(" Impossible de rejeter. Objet d�j� vis�");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * *
     * sert à rejeter un objet de mapping(changer l'etat en rejeté)
     *
     * @param o objet à rejeter
     * @param mere objet mère de l'objet à rejeter
     * @param c connexion ouverte à la base de données
     * @return Objet apres avoir rejeter
     * @throws Exception
     *
     */
    public Object rejeterObject(ClassMAPTable o, ClassMAPTable mere, Connection c) throws Exception {
        if (testRestriction(u.getIdrole(), "ACT000007", o.getNomTable(), c) == 1) {
            throw new Exception("ERREUR DE DROIT");
        }

        testRejectEtat(o, mere, c);
        try {
            o.setMode("modif");

            //this.updateEtat(o, ConstanteEtat.getEtatRejeter(), o.getValInsert("id"), c);
            c.commit();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * *
     * sert à rejeter un objet de mapping(changer l'etat en rejeté)
     *
     * @param o objet à rejeter
     * @param mere objet mère de l'objet à rejeter
     * @return Objet apres avoir rejeter
     * @throws Exception
     *
     */
    @Override
    public Object rejeterObject(ClassMAPTable o, ClassMAPTable mere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = rejeterObject(o, mere, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * *
     * Cette fonction sert à tester si l'objet ne peut pas être cloturer
     *
     * @param o l'objet à cloturer
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void testClotureEtat(ClassMAPTable o, Connection c) throws Exception {
        try {
            String id = o.getValInsert("id");
            ClassMAPTable cl = (ClassMAPTable) Class.forName(o.getClassName()).newInstance();
            cl.setNomTable(o.getNomTable());
            ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(cl, null, null, c, " and id = '" + id + "'");
            if (liste.length == 0) {
                throw new Exception("Objet inexistante");
            }
            if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) == ConstanteEtat.getEtatAnnuler()) {
                    throw new Exception("Impossible de cloturer. Objet annul�");
                }
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) >= ConstanteEtat.getEtatCloture()) {
                    throw new Exception("Impossible de cloturer. Objet d�j� vis� ou rejet� ou clotur�");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * *
     * Cette fonction sert à tester si l'objet ne peut pas être payer
     *
     * @param o l'objet à cloturer
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void ContollerPaiement(ClassMAPTable o, Connection c) throws Exception {
        try {
            String id = o.getValInsert("id");
            ClassMAPTable cl = (ClassMAPTable) Class.forName(o.getClassName()).newInstance();
            cl.setNomTable(o.getNomTable());
            ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(cl, null, null, c, " and "+o.getAttributIDName()+"='"+o.getTuppleID()+"'");
            if (liste.length == 0 || liste==null) {
               throw new Exception("Objet non existant dans " + cl.getNomTable());
            }
            if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) == ConstanteEtat.getEtatAnnuler()) {
                    throw new Exception("Impossible de payer. Objet annule");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * *
     * Fonction permettant de cloturer (changer l'état en cloturé) un objet
     * {@link bean.ClassEtat}
     *
     * @param o l'objet à cloturer
     * @param c connexion ouverte à la base de données
     * @return
     * @throws Exception
     */
    public Object cloturerObject(ClassMAPTable o, Connection c) throws Exception {
        if (u.getRang() < 6) {
            throw new Exception("ERREUR DE DROIT");
        }
        testClotureEtat(o, c);
        try {
            o.setMode("modif");
            if (o instanceof ClassEtat)
            {
               ClassEtat cet=(ClassEtat) o;
               cet.cloturerObject(this.getUser().getTuppleID(), c);
              
            }
            c.commit();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * *
     * Fonction permettant de cloturer (changer l'état en paye) un objet
     * {@link bean.ClassEtat}
     *
     * @param o l'objet à payer
     * @param c connexion ouverte à la base de données
     * @return
     * @throws Exception
     */
    public Object payerObject(ClassMAPTable o, Connection c) throws Exception {
        if (u.getRang() < 6) {
            throw new Exception("ERREUR DE DROIT");
        }
        ContollerPaiement(o, c);
	 ClassEtat []lo=null;
	 Object value=o;
        try {
            o.setMode("modif");
            if (o instanceof ClassEtat)
            {
		  ClassEtat cet=(ClassEtat) o;
		  lo=(ClassEtat [])CGenUtil.rechercher(cet,null,null,c," and "+o.getAttributIDName()+"='"+o.getTuppleID()+"'");
		  if(lo!=null || lo.length>0)
		  {
		      cet=lo[0];
		      value=cet.payerObject(this.getUser().getTuppleID(), c);
		  }
            }
            c.commit();
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * *
     * Fonction permettant de cloturer (changer l'état en payé) un objet
     * {@link bean.ClassEtat} avec initialisation interne de la connexion à la
     * base de données
     *
     * @param o l'objet à payer
     * @return
     * @throws Exception
     */
    @Override
    public Object payerObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = payerObject(o, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * *
     * Fonction permettant de cloturer (changer l'état en cloturé) un objet
     * {@link bean.ClassEtat} avec initialisation interne de la connexion à la
     * base de données
     *
     * @param o l'objet à cloturer
     * @return
     * @throws Exception
     */
    @Override
    public Object cloturerObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = cloturerObject(o, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Supprimer un objet de mapping avec initialisation interne de la connexion
     * à la base de données
     *
     * @param o objet à supprimer
     *
     */
    @Override
    public void deleteObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            deleteObject(o, c);
            c.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Supprimer plusieurs objets de mapping avec initialisation interne de la
     * connexion à la base de données
     *
     * @param o objet à supprimer
     *
     */
    @Override
    public void deleteObjectMultiple(ClassMAPTable[] o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            for (int i = 0; i < o.length; i++) {
                deleteObject(o[i], c);
            }
            c.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * verifier si l'utilisateur associée à la session est un super utilisateur
     *
     * @return <code>true</code> si super utilisateur sinon <code>false</code>
     */
    @Override
    public boolean isSuperUser() {
        return u.isSuperUser();
    }

    /**
     * @param idDirection id de la direction associée à la session
     */
    @Override
    public void setIdDirection(String idDirection) {
        this.idDirection = idDirection;
    }

    /**
     * @return id de la direction associée à la session
     */
    @Override
    public String getIdDirection() {
        return idDirection;
    }

    /**
     * @deprecated
     *
     */
    @Override
    public String[] getAllTable() throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            GestionRole g = new GestionRole();
            return g.getAllTAble(con);
        } catch (Exception e) {
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public int deletePJ(String idDossier, String idPj) throws Exception {
        return 0;
    }

    @Override
    public int deletePJ(String idDossier, String idPj, Connection con) throws Exception {
        return 0;
    }

    @Override
    public int ajouterPJInfo(String info_valeur, String pieces_id, String info_id, String info_ok) throws Exception {
        return 0;
    }

    @Override
    public int ajouterPJInfo(String info_valeur, String pieces_id, String info_id, String info_ok, Connection con)
            throws Exception {
        return 0;
    }

    /**
     * Compter le nombre de message non lu pour l'utilisateur
     *
     * @return nombre de message non lus
     */
    @Override
    public int getMessagesNonLu() throws Exception {
        Connection con = null;
        int ret = 0;
        try {
            con = new UtilDB().GetConn();
            Message msg = new Message();
            msg.setStatut("nonlu");
            Message[] liste = (Message[]) CGenUtil.rechercher(msg, null, null, con, " AND RECEIVER = '" + u.getTeluser() + "' AND STATUT = 'nonlu'");
            if (liste != null && liste.length > 0) {
                ret = liste.length;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Cette fonction permet d'avoir le nombre de notifications non lus
     *
     * @return nombre de notification non lus
     */
    @Override
    public int getNotificationNonLus() throws Exception {
        Connection con = null;
        int ret = 0;
        try {
            con = new UtilDB().GetConn();
            //UtilisateurRole[] currentUser = (UtilisateurRole[]) CGenUtil.rechercher(new UtilisateurRole(), null, null, con, " and refuser=" + u.getTuppleID());
            //if (currentUser != null && currentUser.length > 0) {
            String where = NotificationService.conditionLectureNotification(u);
            where = where + " AND ETAT = 1";
            NotificationLibelle[] liste = (NotificationLibelle[]) this.getData(new NotificationLibelle(), null, null, con, where);
            if (liste != null && liste.length > 0) {
                ret = liste.length;
            }
            //}
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Créer plusieurs objets en même temps
     *
     * @param o liste des objets à créer
     * @return liste des objets enregistrés
     */
    @Override
    public Object createObjectMultiple(ClassMAPTable[] o) throws Exception {
        Connection c = null;
        Object[] ret = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ret = createObjectMultiple(o, c);
            c.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ret;
    }

    /**
     * Mettre à jour plusieurs objets en même temps avec initialisation interne
     * de la connexion à la base de données
     *
     * @param o liste des objets à modifier
     * @return liste des objets modifier
     */
    @Override
    public Object updateObjectMultiple(ClassMAPTable[] o) throws Exception {
        Connection c = null;
        Object[] ret = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ret = updateObjectMultiple(o, c);
            c.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ret;
    }

    public Object[] updateObjectMultiple(ClassMAPTable[] o, Connection c) throws Exception {
        try {
            Object[] ret = new Object[o.length];
            //System.out.println("o.length = " + o.length);
            for (int i = 0; i < o.length; i++) {
                ret[i] = updateObject(o[i], c);
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }

    }

    /**
     * Inserer les informations sur les pièces jointes ajoutées
     *
     * @param nomTable nom de table gardant les informations
     * @param nomprocedure nom de la procédure pour génerer le primary key
     * @param libelle libelle de la pièce jointe
     * @param chemin chemin absolu vers la pièce jointe
     * @param mere id de l'objet mère de la pièce jointe
     */
    @Override
    public void createUploadedPj(String nomtable, String nomprocedure, String libelle, String chemin, String mere)
            throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();

            UploadPj fichier = new UploadPj(nomtable, nomprocedure, "FLE", libelle, chemin, mere);
            fichier.construirePK(con);
            fichier.insertToTableWithHisto(u.getTuppleID(), con);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Supprimer un upload
     *
     * @param nomTable nom de la table
     * @param id id de la pièce jointe
     */
    @Override
    public void deleteUploadedPj(String nomtable, String id) throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            con.setAutoCommit(false);
            UploadPj up = new UploadPj(nomtable);
            UploadPj[] fichiers = (UploadPj[]) CGenUtil.rechercher(up, null, null, con, " AND ID = '" + id + "'");
            //  Utilitaire.deleteFileFromCdn(fichiers[0].getChemin());
            MapHistorique h = new MapHistorique(nomtable, "delete", u.getTuppleID(), id);
            h.setObjet("bean.UploadPj");
            fichiers[0].deleteToTable(con);
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            con.rollback();
            throw ex;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Ajouter une restriction pour un role donné sur une action sur différentes
     * tables
     *
     * @param val liste de table à restreindre
     * @param idrole id du role concerné
     * @param idaction id de l'action à restreindre
     * @param direction direction où la restriction est valide(si pour toutes
     * les directions, mettre null)
     * @throws Exception
     */
    @Override
    public void ajoutrestriction(String[] val, String idrole, String idaction, String direction) throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            ajoutrestriction(val, idrole, idaction, direction, con);
        } catch (Exception e) {
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Ajouter une restriction pour un role donné sur une action sur différentes
     * tables
     *
     * @param val liste de table à restreindre
     * @param idrole id du role concerné
     * @param idaction id de l'action à restreindre
     * @param direction direction où la restriction est valide(si pour toutes
     * les directions, mettre null)
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    @Override
    public void ajoutrestriction(String[] val, String idrole, String idaction, String direction, Connection c)
            throws Exception {
        try {
            GestionRole g = new GestionRole();
            g.ajoutrestriction(val, idrole, idaction, direction, c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Cette fonction gère la création de la notification
     *
     * @param objetDeLaNotification
     * @param message
     * @param destinataire
     * @param idobjet
     * @param lien
     * @param c connexion à la base de donnée
     */
    @Override
    public String envoyerObjectNotification(String objetDeLaNotification, String message, String destinataire,
            String idobjet, String lien, Connection c) throws Exception {
        int indice = 0;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                indice = 1;
                c.setAutoCommit(false);
            }
            String retour = NotificationService.createNotification(u.getTuppleID(), objetDeLaNotification, message, null, destinataire, null, idobjet, lien, c);
            if (indice == 1) {
                c.commit();
            }
            return retour;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && indice == 1) {
                c.close();
            }
        }
    }

    /**
     * Cette fonction gère la création de la notification
     *
     * @param objetDeLaNotification
     * @param message
     * @param personnel
     * @param destinataire
     * @param idobjet
     * @param lien
     * @param c connection
     */
    @Override
    public String envoyerObjectNotification(String objetDeLaNotification, String message, String personnel,
            String destinataire, String idobjet, String lien, Connection c) throws Exception {
        int indice = 0;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                indice = 1;
                c.setAutoCommit(false);
            }
            String retour = NotificationService.createNotification(u.getTuppleID(), objetDeLaNotification, message, personnel, destinataire, null, idobjet, lien, c);
            if (indice == 1) {
                c.commit();
            }
            return retour;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && indice == 1) {
                c.close();
            }
        }
    }

    /**
     * Cette fonction permet de lire une notification
     *
     * @param idnotif de type String
     * @return l'id de la notification
     */
    @Override
    public String lireNotification(String idnotif) throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            con.setAutoCommit(false);
            NotificationMessage[] notif = (NotificationMessage[]) CGenUtil.rechercher(new NotificationMessage(), null, null, con, " and id = '" + idnotif + "'");
            if (notif.length != 0) {
                notif[0].setMode("modif");
                notif[0].setIduser_recevant(u.getTuppleID());
                notif[0].updateToTable(con);
            } else {
                throw new Exception("Erreur notification non trouv?e");
            }
            con.commit();
            return notif[0].getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Cette fonction permet de creer une notification
     *
     * @param daty de type Date
     * @param objetDeLaNotification de type String
     * @param message de type String
     * @param destinataire de type String
     * @param idobjet d etype String
     * @param lien de type String
     * @param priorite de type int
     * @param u utilisateur qui va recevoir la notification
     */
    @Override
    public void createNotification(Date daty, String objetDeLaNotification, String message, String destinataire,
            String idobjet, String lien, int priorite, MapUtilisateur u) throws Exception {
        Connection c = null;
        try {
            c = (new UtilDB()).GetConn();
            NotificationService.createNotification(daty, objetDeLaNotification, message, destinataire, idobjet, lien, u, c);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            c.close();
        }
    }

    /**
     * Modifier un objet avec trace de la personne modifiant avec initialisation
     * interne de la base de données
     *
     * @param o objet à créer
     * @return objet après la modification
     */
    @Override
    public Object updateObject(ClassMAPTable o, Connection c) throws Exception {
        try {
            return UpdateObject.updateObject(o, c, u, this);
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public String getAddrIp() {
        return addrIp;
    }

    @Override
    public void setAddrIp(String addrIp) {
        this.addrIp = addrIp;
    }

    public void testDelete(ClassMAPTable o, Connection c) throws Exception {
        try {
            if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                String id = o.getValInsert("id");
                ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(o, null, null, c, "");
                if (liste.length == 0) {
                    throw new Exception("Objet inexistant");
                }
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) >= ConstanteEtat.getEtatValider()) {
                    throw new Exception("IMPOSSIBLE DE SUPPRIMER. OBJET DEJA VISER");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Cette fonction supprime l'objet qui est trouvé selon le paramètre o
     *
     * @param o classe mapping
     * @param c connexion ouverte à la base de données
     */
    @Override
    public void deleteObject(ClassMAPTable o, Connection c) throws Exception {
        try {
            /*
             * if (u.isSuperUser() == false) { throw new Exception("Pas de
             * droit"); }
             */
            //System.out.println("objet supprimer: " + o.getTuppleID());
            testDelete(o, c);
            ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(o, null, null, c, "");
            if (liste.length > 0) {
                liste[0].deleteToTableWithHisto(u.getTuppleID(), c);

            } else {
                throw new Exception("Erreur durant la suppression, Objet introuvable");
            }
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * *
     * Cette fonction appelle la fonction
     * <pre>lireMessage(String sender, String receiver, Connection c)</pre>
     */
    @Override
    public void lireMessage(String sender, String receiver) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            lireMessage(sender, receiver, c);
        } catch (Exception d) {
            throw d;
        }
    }

    /**
     * Marquer en lu tous les messages non lus
     *
     * @param sender id de l'utilisateur qui envoie les messages
     * @param receiver id de l'utilisateur qui reçoit les messages
     * @param c connexion ouverte à la base de données
     * @throws Exception
     */
    public void lireMessage(String sender, String receiver, Connection c) throws Exception {
        try {
            String awhere = " and (sender='" + sender + "' and receiver='" + receiver + "') ";
            Message[] alire = (Message[]) CGenUtil.rechercher(new Message(), null, null, c, awhere);
            if (alire != null && alire.length > 0) {
                for (int l = 0; l < alire.length; l++) {
                    alire[l].setStatut("Vu");
                    alire[l].updateToTable(c);
                }
            }
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * recherche groupée paginée avec une ligne par page avec récapitulation
     *
     * @param e objet de mapping
     * @param groupe liste de colonnes de groupages sans somme
     * @param sommeGroupe liste de colonnes avec sommes
     * @param colInt colonnes d'intervalle de filtre
     * @param valInt liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage numéro de page actuel
     * @param apresWhere requête SQL après where pour le filtre
     * @param nomColSomme liste de colonnes de somme pour la récapitulation
     * @param ordre requête d'ordre
     * @param c connexion à la base de données, ouverte implicitement si valeur
     * null renseignée
     * @return instance avec les resultats de recherche et les valeurs des
     * sommes de récapitulation
     * @throws Exception
     */
    @Override
    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe,
            String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre,
            Connection c) throws Exception {
        return CGenUtil.rechercherPageGroupeM(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c);

    }

    /**
     * recherche groupée paginée avec récapitulation
     *
     * @param e objet de mapping
     * @param groupe liste de colonnes de groupages sans somme
     * @param sommeGroupe liste de colonnes avec sommes
     * @param colInt colonnes d'intervalle de filtre
     * @param valInt liste des valeurs de colonnes d'intervalle de filtre
     * @param numPage numéro de page actuel
     * @param apresWhere requête SQL après where pour le filtre
     * @param nomColSomme liste de colonnes de somme pour la récapitulation
     * @param ordre requête d'ordre
     * @param c connexion à la base de données, ouverte implicitement si valeur
     * null renseignée
     * @param nppa nombre d'élèment par page, si négatif le nombre d'élèment par
     * page de la configuration sera pris
     * @return instance avec les resultats de recherche et les valeurs des
     * sommes de récapitulation
     * @throws Exception
     */
    @Override
    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe,
            String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre,
            Connection c, int npp) throws Exception {
        return CGenUtil.rechercherPageGroupeM(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c, npp);
    }

    /**
     *
     */
    @Override
    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe,
            String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre,
            Connection c, int npp, String count) throws Exception {
        return CGenUtil.rechercherPageGroupeM(e, groupe, sommeGroupe, colInt, valInt, numPage, apresWhere, nomColSomme, ordre, c, npp, count);

    }

    /**
     * *
     * Annulation d'un objet(modifié l'état en annulé) avec initialisation
     * interne de la base de données
     *
     * @param o l'objet à annuler
     * @return l'objet qui vient d'etre annuler
     */
    @Override
    public Object annulerObject(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            o = o.getById(o.getTuppleID(), null, c);
            if (o instanceof ClassEtat) {
                ((ClassEtat) o).annuler(this.getUser().getTuppleID(), c);
            }
            c.commit();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public void testValidationEtat(ClassMAPTable o, Connection c) throws Exception {
        try {
            String id = o.getValInsert("id");
            ClassMAPTable cl = (ClassMAPTable) Class.forName(o.getClassName()).newInstance();
            cl.setNomTable(o.getNomTable());
            ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(cl, null, null, c, " and id = '" + id + "'");

            if (liste.length == 0) {
                throw new Exception("Objet inexistant");
            }
            if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                int ret = Utilitaire.stringToInt(liste[0].getValInsert("etat")) % 10;
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) == ConstanteEtat.getEtatAnnuler()) {
                    throw new Exception("IMPOSSIBLE DE VISER. OBJET ANNULE");
                }
                if (ret == 0 && Utilitaire.stringToInt(liste[0].getValInsert("etat")) > 0) {
                    throw new Exception("IMPOSSIBLE DE VISER. OBJET REJETE");
                }
                if (Utilitaire.stringToInt(liste[0].getValInsert("etat")) >= ConstanteEtat.getEtatValider()) {
                    throw new Exception("IMPOSSIBLE DE VISER. OBJET DEJA VISE");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * *
     * Annulation d'un objet(modifié l'état en annulé)
     *
     * @param o l'objet à annuler
     * @param c connexion ouverte à la base de donnés
     * @return l'objet qui vient d'etre annuler
     */
    @Override
    public Object annulerObject(ClassMAPTable o, Connection c) throws Exception {
        if (testRestriction(u.getIdrole(), "ACT000005", o.getNomTable(), c) == 1) {
            throw new Exception("ERREUR DE DROIT");
        }
        testValidationEtat(o, c);
        try {
            o.setMode("modif");
            if (o instanceof ClassEtat) {
                ((ClassEtat) o).annuler(this.getUser().getTuppleID(), c);
            }
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }
    }

    /**
     * Sert à updater l'etat qui prend comme paramètre
     *
     * @param e classe mapping
     * @param valeurEtat valeur de l'etat
     * @param id identifiant de l'objet à modifier
     * @param c connexion ouverte à la base de données
     */
    @Override
    public void updateEtat(ClassMAPTable e, int valeurEtat, String id, Connection c) throws Exception {
        try {
            e.setValChamp("etat", valeurEtat);
            e.updateToTableWithHisto(this.getUser().getTuppleID(), c);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Annuler le visa d'un objet(modifié l'etat visé en annulé)
     *
     * @param o objet de mapping visé à annuler
     */
    @Override
    public void annulerVisa(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            this.annulerVisa(o, c);
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

    /**
     * Annuler le visa d'un objet(modifié l'etat visé en annulé)
     *
     * @param o objet de mapping visé à annuler
     * @param c connexion ouverte à la base de données
     */
    @Override
    public void annulerVisa(ClassMAPTable o, Connection c) throws Exception {
        try {
            boolean defaultVisa = true;
            String id = o.getValInsert("id");
            o.setValChamp("id", id);

            if (o instanceof ClassEtat) {
                ClassEtat[] liste = (ClassEtat[]) CGenUtil.rechercher(o, null, null, c, "");

                if (liste.length == 0) {
                    throw new Exception("Objet inexistante");
                }

                liste[0].annulerValidation("" + u.getRefuser(), c);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Enregistrer une classe mère avec ses fille avec initialisation interne de
     * base de données
     *
     * @param mere objet mère
     * @param colonneMere nom de l'attribut dans la classe fille qui correspond
     * au foreign key vers la mère
     * @param fille liste des objets filles à enregistrer
     * @return l'objet mère après enregistrement
     */
    @Override
    public Object createObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille) throws Exception {
        Connection c = null;
        Object ret = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            if (mere instanceof ClassMere && fille.length > 0 && fille[0] instanceof ClassFille) {
                if (mere instanceof bean.ClassEtat) {
                    mere.setValChamp("etat", ConstanteEtat.getEtatCreer());
                    mere.setValChamp("iduser", u.getTuppleID());
                }
                ((ClassMere) mere).setFille((ClassFille[]) fille);
            }
            Object idmere = createObject(mere, c);
            String id = null;
            if(idmere!=null){
                id = ((ClassMAPTable) idmere).getTuppleID();
            }
            ret = createObjectMultiple(fille, colonneMere, id, c);
            ret = idmere;

            c.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ret;
    }

    /**
     * Enregistrer une liste de classe fille en liant l'id de la mère
     *
     * @param o liste des objets filles à enregistrer
     * @param colonneMere nom de l'attribut dans la classe fille qui correspond
     * au foreign key vers la mère
     * @param c connexion ouverte à la base de données
     * @param idmere id de l'objet mère
     * @return les objets filles après enregistrement
     * @throws Exception
     */
    public Object[] createObjectMultiple(ClassMAPTable[] o, String colonneMere, String idmere, Connection c) throws Exception {
        try {
            Object[] ret = new Object[o.length];  
            for (int i = 0; i < o.length; i++) {
                o[i].setValChamp(colonneMere, idmere);
                ret[i] = createObject(o[i], c);
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }

    }

    /**
     * Enregistrer une liste de classe fille en liant l'id de la mère avec
     * initialisation interne de la base de données
     *
     * @param idMere
     * @param colonneMere nom de l'attribut dans la classe fille qui correspond
     * au foreign key vers la mère
     * @param fille liste des objets filles à enregistrer
     * @throws Exception
     */
    @Override
    public void createObjectFilleMultiple(String idMere, String colonneMere, ClassMAPTable[] fille) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);

            createObjectMultiple(fille, colonneMere, idMere, c);

            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            c.rollback();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public void createUploadedPjService(String iddossier, HashMap<String, String> listeVal, Iterator it,
            String nomtable, String nomprocedure, String mere) throws Exception {
        try {
            UploadService.createUploadedPj(iddossier, u.getTuppleID(), listeVal, it, nomtable, nomprocedure, mere);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Cette fonction permet d'ajouter un Menu pour l'utilisateur
     *
     * @param utilisateur ids des utilisateurs concernés splité par des virgules
     * @param menus ids des menu concernés splité par des virgules
     * @param role id du role où le menu est accessible
     * @param acces si accès au menu 1 sinon 0
     */
    @Override
    public void ajouterMenuUtilisateur(String utilisateur, String menus, String role, String acces) throws Exception {
        Connection c = null;
        try {
            c = (new UtilDB()).GetConn();

            String[] user = utilisateur.split(";");
            String[] menu = menus.split(";");

            String req = Utilitaire.tabToString(menu, "'", ",");
            String req2 = Utilitaire.tabToString(user, "'", ",");
            utilisateur.UserMenu[] listE = (utilisateur.UserMenu[]) CGenUtil.rechercher(new utilisateur.UserMenu(), null, null, c, " and IDMENU in (" + req + ") and REFUSER in (" + req2 + ")");
            if (user != null && user.length > 0) {
                for (int i = 0; i < user.length; i++) {
                    if (menu != null && menu.length > 0) {
                        for (int j = 0; j < menu.length; j++) {
                            String idmenu = isExistMenu(listE, menu[j], user[i]);
                            if (idmenu == null) {
                                utilisateur.UserMenu um = new utilisateur.UserMenu(role, menu[j], "", "", Integer.parseInt(acces), user[i]);
                                um.setNomTable("usermenu");
                                um.construirePK(c);
                                um.insertToTableWithHisto(u.getTuppleID(), c);
                            } else {
                                utilisateur.UserMenu um = new utilisateur.UserMenu(role, menu[j], "", "", Integer.parseInt(acces), user[i]);
                                um.setNomTable("usermenu");
                                um.setId(idmenu);
                                um.updateToTableWithHisto(u.getTuppleID(), c);
                            }

                        }
                    } else {
                        throw new Exception("Veuillez choisir au moin un menu");
                    }
                }
            } else {
                throw new Exception("Veuillez choisir au moin un utilisateur");
            }

            c.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public String isExistMenu(utilisateur.UserMenu[] listE, String menu, String refuser) throws Exception {
        for (int i = 0; i < listE.length; i++) {
            if (listE[i].getIdmenu().compareToIgnoreCase(menu) == 0 && listE[i].getRefuser() == refuser) {
                return listE[i].getId();
            }
        }
        return null;
    }

    /**
     * Permet de dupliquer un objet avec ses filles en option avec
     * initialisation de base de données interne
     *
     * @param o l'objet à dupliquer
     * @param mapFille nom complet(avec package) de la classe fille si existe
     * sinon ""
     * @param nomColonneMere nom de l'attribut associé au colonne mère dans la
     * classe fille
     * @param c connexion ouverte à la base de donnée
     * @return String de l'id de la mère
     */
    @Override
    public Object dupliquerObject(ClassMAPTable o, String mapFille, String nomColonneMere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = dupliquerObject(o, mapFille, nomColonneMere, c);
            c.commit();
            return ob;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Permet de dupliquer un objet avec ses filles en option
     *
     * @param o l'objet à dupliquer
     * @param mapFille nom complet(avec package) de la classe fille si existe
     * sinon ""
     * @param nomColonneMere nom de l'attribut associé au colonne mère dans la
     * classe fille
     * @param c connexion ouverte à la base de donnée
     * @return String de l'id de la mère
     */
    @Override
    public Object dupliquerObject(ClassMAPTable o, String mapFille, String nomColonneMere, Connection c)
            throws Exception {
        try {
            o.setMode("modif");
            String id = o.getValInsert("id");
            if (o instanceof bean.ClassMere) {
                ClassMere mere = (ClassMere) (Class.forName(o.getClassName()).newInstance());
                mere.setValChamp("id", id);
                mere.setNomTable(o.getNomTable());

                ClassMere[] liste = (ClassMere[]) CGenUtil.rechercher(mere, null, null, c, "");

                if (liste.length == 0) {
                    throw new Exception("Objet inexistant");
                }

                o = liste[0].dupliquer(u.getTuppleID(), c);

                return o.getValInsert("id");
            }
            if (o.getClass().getSuperclass().getSimpleName().compareToIgnoreCase("ClassEtat") == 0) {
                ClassEtat te = (ClassEtat) (Class.forName(o.getClassName()).newInstance());
                te.setValChamp("id", id);
                te.setNomTable(o.getNomTable());
                ClassEtat[] liste = (ClassEtat[]) CGenUtil.rechercher(te, null, null, c, "");
                liste[0].setEtat(1);
                liste[0].construirePK(c);
                liste[0].insertToTableWithHisto(u.getTuppleID(), c);

                if (liste.length == 0) {
                    throw new Exception("Objet inexistant");
                }
                if (mapFille.compareToIgnoreCase("") != 0) {
                    ClassMAPTable mf = (ClassMAPTable) (Class.forName(mapFille).newInstance());
                    ClassMAPTable[] liste_mf = (ClassMAPTable[]) CGenUtil.rechercher(mf, null, null, c, " and " + nomColonneMere + " = '" + te.getValInsert("id") + "'");
                    for (int i = 0; i < liste_mf.length; i++) {
                        liste_mf[i].setValChamp(nomColonneMere, liste[0].getValInsert("id"));
                        liste_mf[i].construirePK(c);
                        liste_mf[i].insertToTableWithHisto(u.getTuppleID(), c);
                    }
                }
                return liste[0].getValInsert("id");
            } else {
                ClassMAPTable t = (ClassMAPTable) (Class.forName(o.getClassName()).newInstance());
                t.setValChamp("id", id);
                ClassMAPTable[] liste = (ClassMAPTable[]) CGenUtil.rechercher(t, null, null, c, "");
                liste[0].construirePK(c);
                liste[0].insertToTableWithHisto(u.getTuppleID(), c);

                if (liste.length == 0) {
                    throw new Exception("Objet inexistant");
                }
                if (mapFille.compareToIgnoreCase("") != 0) {
                    ClassMAPTable mf = (ClassMAPTable) (Class.forName(mapFille).newInstance());
                    ClassMAPTable[] liste_mf = (ClassMAPTable[]) CGenUtil.rechercher(mf, null, null, c, " and " + nomColonneMere + " = '" + t.getValInsert("id") + "'");
                    for (int i = 0; i < liste_mf.length; i++) {
                        liste_mf[i].setValChamp(nomColonneMere, liste[0].getValInsert("id"));
                        liste_mf[i].construirePK(c);
                        liste_mf[i].insertToTableWithHisto(u.getTuppleID(), c);
                    }
                }
                return liste[0].getValInsert("id");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }
    }

    /**
     * Créer des objets avec trace des personnes modifiant
     *
     * @param o objet à créer
     * @param c connexion ouverte à la base de données
     * @return objet après la création
     */
    @Override
    public Object[] createObjectMultiple(ClassMAPTable[] o, Connection c) throws Exception {
        try {
            Object[] ret = new Object[o.length];
            for (int i = 0; i < o.length; i++) {
                ret[i] = createObject(o[i], c);
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        }
    }

    /**
     * Créer plusieurs objets de catégorie({@link bean.TypeObjet})
     *
     * @param nomTable nom de la table pour stocker la catégorie
     * @param proc nom de la procédure pour génerer l'ID
     * @param pref suffixe à utiliser pour génerer l'ID
     * @param typ valeur de l'objet
     * @param desc description détaillée
     * @return ID de l'objet qui vient d'être créer
     */
    @Override
    public void createTypeObjetMultiple(String nomTable, String proc, String pref, String[] typ, String[] desc)
            throws Exception {
        Connection c = null;
        int verif = 0;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
                verif = 1;
            }
            for (int i = 0; i < typ.length; i++) {
                this.createTypeObjet(nomTable, proc, pref, typ[i], desc[i], c);
            }
        } catch (Exception e) {
            if (c != null && verif == 1) {
                c.rollback();
            }
            throw e;
        } finally {
            if (c != null && verif == 1) {
                c.close();
            }
        }
    }

    /**
     * Créer un objet de catégorie({@link bean.TypeObjet})
     *
     * @param nomTable nom de la table pour stocker la catégorie
     * @param proc nom de la procédure pour génerer l'ID
     * @param pref suffixe à utiliser pour génerer l'ID
     * @param typ valeur de l'objet
     * @param desc description détaillée
     * @param c connexion ouverte à la base de données
     * @return ID de l'objet qui vient d'être créer
     */
    @Override
    public String createTypeObjet(String nomTable, String proc, String pref, String typ, String desc, Connection c)
            throws Exception {
        try {
            if (u.getRang() >= 4) {
                if (typ.compareTo("") == 0) {
                    throw new Exception("Le champs Libelle est obligatoire");
                }
                TypeObjet to = new TypeObjet(nomTable, proc, pref, typ, desc);
                String refuser = u.getTuppleID();
                if (refuser != null && refuser.contains("/")) {
                    String[] g = Utilitaire.split(u.getTuppleID(), "/");
                    refuser = g[0];
                }

                MapHistorique histo = new MapHistorique(nomTable, "Insertion par " + u.getTuppleID(), refuser, to.getId());
                histo.setObjet("bean.TypeObjet");
                to.insertToTable(histo, c);
                String s = to.getId();
                return s;
            } else {
                throw new Exception("ERREUR DE DROIT");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Object updateObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille) throws Exception {
        return updateObjectMultiple(mere, colonneMere, fille, null);
    }

    @Override
    public HashMap<String, String> getMapAutoComplete() {
        return mapAutoComplete;
    }

    @Override
    public void setMapAutoComplete(HashMap<String, String> mapAutoComplete) {
        this.mapAutoComplete = mapAutoComplete;
    }

    /**
     * *
     * Permet d'annuler des objets
     *
     * @param o type de l'objet
     * @param listeIdObjet liste des ids à annuler
     * @param c connexion ouverte à la base de données
     * @return liste des objets supprimés
     */
    @Override
    public Object[] annulerObjectMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception {
        Object[] ret = null;

        try {
            String ids = Utilitaire.tabToString(listeIdObjet, "'", ",");
            ClassEtat[] objs = (ClassEtat[]) CGenUtil.rechercher(o, null, null, c, " and id in (" + ids + ")");

            for (ClassEtat obj : objs) {
                annulerObject(obj, c);
            }

            ret = objs;
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            throw ex;
        }

        return ret;
    }

    /**
     * *
     * Permet d'annuler des objets
     *
     * @param o type de l'objet
     * @param listeIdObjet liste des ids à annuler
     * @param c connexion ouverte à la base de données
     * @return liste des objets supprimés
     */
    @Override
    public Object[] annulerObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        Object[] ret = null;

        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ret = annulerObjectMultiple(o, listeIdObjet, c);
            c.commit();
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return ret;
    }

    /**
     * Créer un objet et si aucune validation n'a été faite on jette une
     * exception de validation qu'on devrait gérer à l'affichage
     *
     * @param o objet de mapping à enregistrer
     * @param c connexion ouverte à la base de données
     * @return objet qui vient d'être enregistré
     */
    @Override
    public Object createObjectValidation(ClassMAPTable o, Connection c) throws Exception {
        try {
            boolean nandalo = false;
            o.setMode("insert");
            /*if (o.getClassName().compareToIgnoreCase("jeton.MouvementJeton") == 0) {
             return null;
             }*/
            o.controler(c);
            o.construirePK(c);

            if (getValidation() == null) {
                ValidationException validat = new ValidationException(" Voulez vous continuez? ");
                setValidation(validat);
                nandalo = true;
            }

            if (getValidation().isResponse() == false && nandalo == true) {
                getValidation().setObjet(o);
                throw getValidation();
            }
            if (getValidation() == null || getValidation() != null && getValidation().isResponse() == true) {
                o.insertToTable(c);
            }
            setValidation(null);
            return o;
        } catch (ValidationException valid) {
            throw valid;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Créer un objet et si aucune validation n'a été faite on jette une
     * exception de validation qu'on devrait gérer à l'affichage avec
     * initialisation de base de données interne
     *
     * @param o objet de mapping à enregistrer
     * @return objet qui vient d'être enregistré
     */
    @Override
    public Object createObjectValidation(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);

            Object ob = createObjectValidation(o, c);
            c.commit();
            return ob;
        } catch (ValidationException valid) {
            throw valid;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Mettre à jour un objet et si aucune validation n'a été faite on jette une
     * exception de validation qu'on devrait gérer à l'affichage avec
     * initialisation de base de données interne
     *
     * @param o objet de mapping à enregistrer
     * @return objet qui vient d'être enregistré
     */
    @Override
    public Object updateObjectValidation(ClassMAPTable o) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Object ob = updateObjectValidation(o, c);
            c.commit();
            return ob;
        } catch (ValidationException valid) {
            throw valid;
        } catch (Exception ex) {
            ex.printStackTrace();
            c.rollback();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Mettre à jour un objet et si aucune validation n'a été faite on jette une
     * exception de validation qu'on devrait gérer à l'affichage
     *
     * @param o objet de mapping à enregistrer
     * @param c connexion ouverte à la base de données
     * @return objet qui vient d'être enregistré
     */
    @Override
    public Object updateObjectValidation(ClassMAPTable o, Connection c) throws Exception {
        try {
            o.setMode("update");
            boolean nandalo = false;
            if (getValidation() == null) {
                ValidationException validat = new ValidationException(" Voulez vous continuez l'update ? ");
                setValidation(validat);
                nandalo = true;
            }

            if (getValidation().isResponse() == false && nandalo == true) {
                getValidation().setObjet(o);
                throw getValidation();
            }
            if (getValidation() == null || getValidation() != null && getValidation().isResponse() == true) {
                o.controlerUpdate(c);
                o.updateToTableWithHisto(u.getTuppleID(), c);
            }
            setValidation(null);
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Supprimer un objet et si aucune validation n'a été faite on jette une
     * exception de validation qu'on devrait gérer à l'affichage
     *
     * @param o objet de mapping à supprimer
     * @param c connexion ouverte à la base de données
     * @return objet qui vient d'être enregistré
     */
    @Override
    public void deleteObjectValidation(ClassMAPTable o) throws Exception {
        Connection c = null;
        boolean nandalo = false;
        try {
            if (u.isSuperUser() == false) {
                throw new Exception("Pas de droit");
            }
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            o.controler(c);
            o.construirePK(c);

            if (getValidation() == null) {
                ValidationException validat = new ValidationException(" Voulez vous continuez? ");
                setValidation(validat);
                nandalo = true;
            }

            if (getValidation().isResponse() == false && nandalo == true) {
                getValidation().setObjet(o);
                throw getValidation();
            }
            if (getValidation() == null || getValidation() != null && getValidation().isResponse() == true) {
                o.deleteToTableWithHisto(u.getTuppleID(), c);
                c.commit();
            }
            setValidation(null);

        } catch (ValidationException valid) {
            throw valid;
        } catch (Exception ex) {
            c.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public MapRoles[] findRole(String rol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int testRestriction(String user, String permission, String table, Connection con) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String mapperMereToFilleMetier(ClassMAPTable e, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String mapperMereToFilleMetier(ClassMAPTable e, String nomTable, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object createObject(ClassMAPTable o, String action) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValidationException getValidation() {
        return validation;
    }

    @Override
    public void setValidation(ValidationException validation) {
        this.validation = validation;
    }

    @Override
    public void ejbRemove() {
       
    }

    /* Changement Compta */
    @Override
    public Object[] annulerVisaMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception {
        Object[] ret = null;

        try {
            String ids = Utilitaire.tabToString(listeIdObjet, "'", ",");
            ClassEtat[] objs = (ClassEtat[]) CGenUtil.rechercher(o, null, null, c, " and id in (" + ids + ")");

            for (ClassEtat obj : objs) {
                obj.annulerVisa(this.getUser().getTuppleID(), c);
            }

            ret = objs;
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            throw ex;
        }

        return ret;
    }

    @Override
    public Object[] annulerVisaMultiple(ClassEtat o, String[] listeIdObjet) throws Exception {
        Connection c = null;
        Object[] ret = null;

        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ret = annulerVisaMultiple(o, listeIdObjet, c);
            c.commit();
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ret;
    }

    @Override
    public void validerObjectTous(ClassEtat etat, Connection c)throws Exception{
        try{
            String[] colInt = {"etat"};
            String[] valInt = {"1","1"};

            ClassEtat[] liste = (ClassEtat[])CGenUtil.rechercher(etat, colInt, valInt, c, "");

            for(int i=0;i<liste.length;i++){
                liste[i].validerObject(this.getUser().getTuppleID(), c);
            }

        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void validerObjectTous(ClassEtat etat)throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            validerObjectTous(etat,c);
            c.commit();
        }catch(Exception e){
            if (c != null) {
                c.rollback();
            }
            throw e;
        }finally{
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public void changeState(ClassMAPTable o, String acte) throws Exception {
        if (o instanceof ClassEtat) {
            Connection con = null;
            try {
                con=new UtilDB().GetConn();
                con.setAutoCommit(false);
                o = o.getById(o.getTuppleID(),null,con);
                ((ClassEtat) o).changeState(acte, u.getTuppleID(), con);
                con.commit();
//                return o;
            }
            catch(AlertException e){
                throw e;
            }
             catch (Exception e) {
                if(con!=null){
                    con.rollback();
                }
                e.printStackTrace();
                throw e;
            } finally {
                if (con != null) {
                    con.close();
                }
            }
        }

        else throw new Exception("L'entité à changer d'état n'est pas un ClassEtat");
    }
}
