package user;

import bean.ClassEtat;
import bean.ClassFille;
import bean.ClassMAPTable;
import bean.ClassMere;
import bean.ResultatEtSomme;
import bean.TypeObjet;
import bean.ValidationException;
import historique.MapRoles;
import historique.MapUtilisateur;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.ejb.Local;
import mg.cnaps.configuration.Configuration;
import lc.Direction;
import mg.cnaps.utilisateur.CNAPSUser;

/**
 * Interface specifiant les propriétés de base qu'un bean UserEJB devrait avoir.
 */
@Local
public interface UserEJB {
    
    public String getHome_page();
    public void ejbRemove();
    
    public CNAPSUser getCnapsUser();
    
    public ValidationException getValidation();

    public void setValidation(ValidationException validation);

    public String insertMereLierFilles(ClassMere mere , ClassFille fille , String[] filles , String colonneFille, String colonneMere) throws Exception;
    
    public String insertMereLierFilles(ClassMere mere , ClassFille fille , String[] filles , String colonneFille, String colonneMere , Connection c) throws Exception ;
    
    public Direction[] findDirection(String idDir, String libelledir, String descdir, String abbrevDir, String idDirecteur) throws Exception ;
    
    public Configuration[] findConfiguration() throws Exception;
    
    public Object updateObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille, Connection c) throws Exception;
        
    public ResultatEtSomme getDataPageMax(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c) throws Exception;

    public ResultatEtSomme getDataPageMax(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception;

//    public int terminerInterimUtilisateur(String ref) throws Exception;

    public int cloturerObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;

    public TypeObjet[] getListeSource() throws Exception;


    public void transfererNotification(String idNotification, String idpers, String service, String direction) throws Exception;


    public void deleteObjetFille(ClassMAPTable o, Connection conn) throws Exception;

    public void deleteObjetFille(ClassMAPTable o) throws Exception;

    public ResultatEtSomme getDataPageMaxSansRecap(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception;


    public String getMaxId(String table, String[] colonne, String[] listeCritere) throws Exception;

    public String getMaxId(String table) throws Exception;

    public void updateEtat(ClassMAPTable e, int valeurEtat, String id) throws Exception;

    public String createTypeObjet(String nomTable, String proc, String pref, String typ, String desc) throws Exception;

    public String updateTypeObjet(String table, String id, String typ, String desc) throws Exception;

    public TypeObjet[] findTypeObjet(String nomTable, String id, String typ) throws Exception;

    public int deleteTypeObjet(String nomTable, String id) throws Exception;


    public MapUtilisateur[] findUtilisateurs(String refuser, String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole) throws Exception;

    public MapRoles[] findRole(String rol);

    public MapUtilisateur getUser();

    public int viserObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;

    public int marquerLuMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;

    public int retournerObjectMultiple(ClassEtat o, String[] listeIdObjet, String motif) throws Exception;

    public int recuObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;

    public int validerObjectMultiple(ClassEtat[] o) throws Exception;

    public Object finaliser(ClassMAPTable o, Connection c) throws Exception;

    public Object finaliser(ClassMAPTable o) throws Exception;

    public String updateUtilisateurs(String refuser, String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole) throws Exception;

    public int deleteUtilisateurs(String refuser) throws Exception;

    public String createUtilisateurs(String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole) throws Exception;

    public int desactiveUtilisateur(String ref) throws Exception;

    public int activeUtilisateur(String ref) throws Exception;

    public void testLogin(String user, String pass, String interim, String service) throws Exception;


    public String findHomePageServices(String codeService) throws Exception;


    public String mapperMereFille(ClassMAPTable e, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception;

    public String mapperMereFille(ClassMAPTable e, String nomTable, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception;

    public void deleteMereFille(ClassMAPTable e, String idMere, String[] liste_id_fille) throws Exception;

    public void deleteMereFille(ClassMAPTable e, String nomTable, String idMere, String[] liste_id_fille) throws Exception;

    public ResultatEtSomme getDataPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c) throws Exception;

    public ResultatEtSomme getDataPage(ClassMAPTable e, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, Connection c, int npp) throws Exception;

    public ResultatEtSomme getDataPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c) throws Exception;

    public ResultatEtSomme getDataPageGroupe(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int npp) throws Exception;

    public Object[] getData(ClassMAPTable e, String[] colInt, String[] valInt, Connection c, String apresWhere) throws Exception;

    public Object createObject(ClassMAPTable o) throws Exception;

    public Object createObject(ClassMAPTable o, Connection c) throws Exception;

    public Object updateObject(ClassMAPTable o) throws Exception;
    
    public Object updateObjectSimple(ClassMAPTable o) throws Exception;

    public Object validerObject(ClassMAPTable o) throws Exception;

    public Object rejeterObject(ClassMAPTable o, ClassMAPTable mere) throws Exception;

    public Object cloturerObject(ClassMAPTable o) throws Exception;

    public Object payerObject(ClassMAPTable o) throws Exception;

    public void deleteObject(ClassMAPTable o) throws Exception;

    public void deleteObjectMultiple(ClassMAPTable[] o) throws Exception;

    public boolean isSuperUser();

    public void setIdDirection(String idDirection);

    public String getIdDirection();


    public int testRestriction(String user, String permission, String table, Connection con) throws Exception;

    public String[] getAllTable() throws Exception;


    public int deletePJ(String idDossier, String idPj) throws Exception;

    public int deletePJ(String idDossier, String idPj, Connection con) throws Exception;

    public int ajouterPJInfo(String info_valeur, String pieces_id, String info_id, String info_ok) throws Exception;

    public int ajouterPJInfo(String info_valeur, String pieces_id, String info_id, String info_ok, Connection con) throws Exception;

    public int getMessagesNonLu() throws Exception;

    public int getNotificationNonLus() throws Exception;


    public Object createObjectMultiple(ClassMAPTable[] o) throws Exception;

    public Object updateObjectMultiple(ClassMAPTable[] o) throws Exception;

    public void createUploadedPj(String nomtable, String nomprocedure, String libelle, String chemin, String mere) throws Exception;//Upload generaliser

    public void deleteUploadedPj(String nomtable, String id) throws Exception;//delete d'upload generaliser

    public void ajoutrestriction(String[] table, String idrole, String idaction, String direction) throws Exception;

    public void ajoutrestriction(String[] val, String idrole, String idaction, String direction, Connection c) throws Exception;

    public String envoyerObjectNotification(String objetDeLaNotification, String message, String destinataire, String idobjet, String lien, Connection c) throws Exception;

    public String envoyerObjectNotification(String objetDeLaNotification, String message, String personnel, String destinataire, String idobjet, String lien, Connection c) throws Exception;

    public String lireNotification(String idnotif) throws Exception;

    public String mapperMereToFilleMetier(ClassMAPTable e, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception;

    public String mapperMereToFilleMetier(ClassMAPTable e, String nomTable, String idMere, String[] idFille, String rem, String montant, String etat) throws Exception;

    public void createNotification(java.sql.Date daty, String objetDeLaNotification, String message, String destinataire, String idobjet, String lien, int priorite, MapUtilisateur u) throws Exception;

    public Object updateObject(ClassMAPTable o, Connection c) throws Exception;

    public String getAddrIp();

    public void setAddrIp(String addrIp);

    public void deleteObject(ClassMAPTable o, Connection c) throws Exception;


    public void lireMessage(String sender, String receiver) throws Exception;

    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c) throws Exception;

    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int npp) throws Exception;

    public ResultatEtSomme getDataPageGroupeMultiple(ClassMAPTable e, String[] groupe, String[] sommeGroupe, String[] colInt, String[] valInt, int numPage, String apresWhere, String[] nomColSomme, String ordre, Connection c, int npp, String count) throws Exception;


    public Object annulerObject(ClassMAPTable o) throws Exception;

    public Object annulerObject(ClassMAPTable o, Connection c) throws Exception;

    public void updateEtat(ClassMAPTable e, int valeurEtat, String id, Connection c) throws Exception;

    public void annulerVisa(ClassMAPTable o) throws Exception;

    public void annulerVisa(ClassMAPTable o, Connection c) throws Exception;

    public Object createObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille) throws Exception;
    
    public void createObjectFilleMultiple(String idMere, String colonneMere, ClassMAPTable[] fille) throws Exception;

    public void createUploadedPjService(String iddossier, HashMap<String, String> listeVal, Iterator it, String nomtable, String nomprocedure, String mere) throws Exception;

    public void ajouterMenuUtilisateur(String utilisateur, String menus, String role, String acces) throws Exception;


    public Object dupliquerObject(ClassMAPTable o, String mapFille, String nomColonneMere) throws Exception;

    public Object dupliquerObject(ClassMAPTable o, String mapFille, String nomColonneMere, Connection c) throws Exception;


    public Object createObject(ClassMAPTable o, String action) throws Exception;
    public Object[] createObjectMultiple(ClassMAPTable[] o, Connection c) throws Exception;

    public void createTypeObjetMultiple(String nomTable, String proc, String pref, String[] typ, String[] desc) throws Exception;

    public String createTypeObjet(String nomTable, String proc, String pref, String typ, String desc, Connection c) throws Exception;

    public Object updateObjectMultiple(ClassMAPTable mere, String colonneMere, ClassMAPTable[] fille) throws Exception;
    
    public HashMap<String, String> getMapAutoComplete();
    public void setMapAutoComplete(HashMap<String, String> mapAutoComplete);
    
    
    public Object[] annulerObjectMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception;
    public Object[] annulerObjectMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;
    

    public Object createObjectValidation(ClassMAPTable o, Connection c) throws Exception;

    public Object createObjectValidation(ClassMAPTable o) throws Exception;

    public Object updateObjectValidation(ClassMAPTable o) throws Exception;

    public Object updateObjectValidation(ClassMAPTable o, Connection c) throws Exception;

    public void deleteObjectValidation(ClassMAPTable o) throws Exception;

    /* Changement Compta */
    public Object[] annulerVisaMultiple(ClassEtat o, String[] listeIdObjet, Connection c) throws Exception;

    public Object[] annulerVisaMultiple(ClassEtat o, String[] listeIdObjet) throws Exception;

    public void validerObjectTous(ClassEtat etat, Connection c)throws Exception;

    public void validerObjectTous(ClassEtat etat)throws Exception;
    public void changeState(ClassMAPTable o,String acte)throws Exception;
}
