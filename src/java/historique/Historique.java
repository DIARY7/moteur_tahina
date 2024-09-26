package historique;

import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import bean.*;
/**
 * Interface définissant les propriétes que les beans manipulant 
 * l'historique devraient surdefinir pour avoir une utilité cohérente
 * @author BICI
 */
public interface Historique extends javax.ejb.EJBObject {
    
    public MapHistorique[] findHistorique(String refuser, String refObjet, String objet, String action, String daty1, String daty2) throws Exception, RemoteException;

    public MapHistorique[] findHistoriquePage(String refuser, String refObjet, String objet, String action, String daty1, String daty2, int numPage) throws Exception, RemoteException;

    public int calculHistoriquePage(String refuser, String refObjet, String objet, String action, String daty1, String daty2) throws Exception, RemoteException;

    public Objet[] findObjet(String obj, String des) throws Exception, RemoteException;

    public MapRoles[] findRole(String rol) throws RemoteException;

    public int deleteUtilisateurs(String iduser, String refUser) throws RemoteException;

    public String createHistorique(String dateHistorique, String heure, String objet, String action, String refuser, String refObjet) throws RemoteException;

    public String updateHistorique(String idHistorique, String dateHistorique, String heure, String objet, String action, String refuser, String refObjet) throws RemoteException;

    public int deleteHistorique(String idHistorique, String refUser) throws RemoteException;

    public String createHistoriqueEtat(String idMere, String idUser, String idEtat, String dateModification) throws RemoteException;

    public String updateHistoriqueEtat(String idHistoriqueEtat, String idMere, String idUser, String idEtat, String dateModification) throws RemoteException;

    public int deleteHistoriqueEtat(String idHistoriqueEtat, String refUser) throws RemoteException;

    public HistoriqueEtat[] findHistoriqueEtat(String idHistoriqueEtat, String idMere, String idUser, String idEtat, String dateModification) throws RemoteException;

    public int desactiveUtilisateur(String ref, String refUser) throws Exception, RemoteException;

    public int activeUtilisateur(String ref, String refUser) throws bean.ErreurDAO, RemoteException;

    public MapUtilisateur testeValide(String user, String pass) throws Exception, RemoteException;

    public int annulerSortie(String idSortie) throws RemoteException;

    public int testException() throws bean.ErreurDAO, RemoteException;

    public int create(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception, RemoteException;

    public int update(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception, RemoteException;

    public int delete(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception, RemoteException;
}
