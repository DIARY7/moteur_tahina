package historique;

import javax.ejb.*;
import java.util.*;
import bean.*;

@Remote
public interface HistoriqueRemote {

    public MapHistorique[] findHistorique(String refuser, String refObjet, String objet, String action, String daty1, String daty2) throws Exception;

    public MapHistorique[] findHistoriquePage(String refuser, String refObjet, String objet, String action, String daty1, String daty2, int numPage) throws Exception;

    public int calculHistoriquePage(String refuser, String refObjet, String objet, String action, String daty1, String daty2) throws Exception;

    public Objet[] findObjet(String obj, String des) throws Exception;

    public MapRoles[] findRole(String rol);

    public String createUtilisateurs(String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole, String refUser) throws Exception;

    public String updateUtilisateurs(String iduser, String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole, String refUser) throws Exception;

    public int deleteUtilisateurs(String iduser, String refUser);

    public MapUtilisateur[] findUtilisateurs(String refuser, String loginuser, String pwduser, String nomuser, String adruser, String teluser, String idrole) throws Exception;

    public String createHistorique(String dateHistorique, String heure, String objet, String action, String refuser, String refObjet);

    public String updateHistorique(String idHistorique, String dateHistorique, String heure, String objet, String action, String refuser, String refObjet);

    public int deleteHistorique(String idHistorique, String refUser);

    public String createHistoriqueEtat(String idMere, String idUser, String idEtat, String dateModification);

    public String updateHistoriqueEtat(String idHistoriqueEtat, String idMere, String idUser, String idEtat, String dateModification);

    public int deleteHistoriqueEtat(String idHistoriqueEtat, String refUser);

    public HistoriqueEtat[] findHistoriqueEtat(String idHistoriqueEtat, String idMere, String idUser, String idEtat, String dateModification);

    public int desactiveUtilisateur(String ref, String refUser) throws Exception;

    public int activeUtilisateur(String ref, String refUser) throws bean.ErreurDAO;

    public MapUtilisateur testeValide(String user, String pass, String interim, String service) throws Exception;

    public int annulerSortie(String idSortie);

    public int testException() throws bean.ErreurDAO;

    public int create(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception;

    public int update(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception;

    public int delete(ClassMAPTable value1, MapUtilisateur value2) throws java.lang.Exception;
}
