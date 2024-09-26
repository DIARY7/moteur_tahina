/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporting;

import bean.CGenUtil;
import historique.MapUtilisateur;
import java.io.FileNotFoundException;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import user.UserEJB;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;

/**
 * Cette classe contient des fonctions utilitaires pour génerer des noms de fichiers et les enregistrer
 *  pour des PDF de même objets
 * <p>Prenons un exemple, on veut enregistrer un rapport associé à un utilisateur <code>User</code>:</p>
 * <pre>
 *  User user = new User();
 *  user.setId("US001");
 *  int nomPDF = UtilitaireServeurPDF.getNumeroSequentiel("user",user.getId());
 *  OutputStream stream = new FileOutputStream(new String(nomPDF)+".pdf");
 *  Map<String,Object> userData = new HashMap<String,Object>();
 *  //Prendre les informations de l'utilisation
 *  userData.put(....);
 *  ReportingUtils.PDF("rapport_user_janvier.jasper",userData,new ArrayList(),stream);
 * </pre>
 * @author BICI
 */

public class UtilitaireServeurPDF {

    /**
     * Cette fonction permet de voir si l'objet  existe dejà
     * @param nom_objet nom de l'objet
     * @param idobjet id de l'objet
     * @param etat etat de l'objet
     * @return true or false
     * @throws Exception
     */
    public static boolean testIfExist(String nom_objet, String idobjet, int etat) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' AND IDOBJET = '"+idobjet+"' and etat="+etat+"");
        if(spdfs.length>0) return true;
        return false;
    }

    /**
     * Cette fonction permet de voir si l'objet  existe dejà
     * @param nom_objet nom de l'objet
     * @param idobjet id de l'objet
     * @return true or false
     * @throws Exception
     */
    public static boolean testIfExist(String nom_objet, String idobjet) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' AND IDOBJET = '"+idobjet+"'");
        if(spdfs.length>0) return true;
        return false;
    }

    /**
     * Cette fonction permet de faire une mis à jour de l'objet 
     * @param nom_objet nom de l'objet
     * @param etat etat de l'objet
     * @throws Exception
     */
    public static void updateWhenNotAnneeEnCours(String nom_objet, int etat) throws Exception{
        Connection c = new UtilDB().GetConn();
        try{
            ServeurPDF spdf = new ServeurPDF();
            ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"'");
            if(spdfs.length>0) return;
            spdfs[0].setEtat(etat);
            spdfs[0].setNom_objet("");
            spdfs[0].setRemarque("0");
            spdfs[0].upDateToTable();
            c.commit();
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }

    /**
     * Cette fonction verifie si l'etat se corresponde
     * @param fileName nom du fichier
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT
     * @param typeFile type de fichier
     * @param id  id de l'objet
     * @param etat  etat pour la comparaison
     * @return true or false
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static boolean checkEtatServeur(String fileName, ReportingCdn.Fonctionnalite fonctionnalite, ReportingUtils.ReportType typeFile, String id, int etat) throws FileNotFoundException, Exception{
        if(ReportingCdn.checkExistingFile(fonctionnalite, fileName+"-"+id+ReportingCdn.getExtension(typeFile))) if(etat==getEtatServeur(fileName, id)) return true;
        return false;
    }

    /**
     * Cette fonction permet d'avoir l'etat de l'objet 
     * @param nom_objet nom de l'objet 
     * @param idobjet id de l'objet
     * @return l'etat de l'objet
     * @throws Exception
     */
    public static int getEtatServeur(String nom_objet, String idobjet) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' AND IDOBJET = '"+idobjet+"'");
        if(spdfs.length>0) spdf = spdfs[0];
        return spdf.getEtat();
    }

    /**
     * Sert à supprimer l'objet 
     * @param idobjet idifiant de l'objet  à supprimer 
     * @throws Exception
     */
    public static void deleteFromServeur(String idobjet) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        spdf.setIdobjet(idobjet);
        spdf.deleteToTable();
    }

    /**
     * Cette fonction fait la mis à jour de l'etat
     * @param nom_objet nom de l'objet pour faire l'update
     * @param idobjet idenfiant de l'objet pour faire l'update
     * @param etat le nouveau valeur de l'etat
     * @throws Exception
     */
    private static void updateEtatServeur(String nom_objet, String idobjet, int etat) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' AND IDOBJET = '"+idobjet+"'");
        if(spdfs.length>0) spdf = spdfs[0];
        spdf.setEtat(etat);
        spdf.upDateToTable();
    }

    /**
     * 
     * @param nom_objet nom de l'objet pour faire l'update
     * @param idobjet lidenfiant de l'objet pour faire l'update
     * @param etat le nouveau valeur de l'etat
     * @param remarque le nouveau valeur du remarque 
     * @throws Exception
     */
    private static void updateEtatServeurWithRemarque(String nom_objet, String idobjet, int etat, String remarque) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' AND IDOBJET = '"+idobjet+"'");
        if(spdfs.length>0) spdf = spdfs[0];
        spdf.setEtat(etat);
        spdf.setRemarque(remarque);
        spdf.upDateToTable();
    }
    

    /**
     * Cette fonction appelle la fonction
     * <pre> getNumerosequentiel(Connection c, String nom_objet, String idObjet) </pre>
     * @param nom_objet nom de l'objet
     * @param idObjet idenfiant de l'objet 
     * @return un nombre de type Integer
     * @throws Exception
     */
    public static int getNumerosequentiel(String nom_objet, String idObjet) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            int toReturn =  getNumerosequentiel(c, nom_objet, idObjet);
            c.commit();
            return toReturn;
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }

    /**
     * Créer des numéros sequentiels pour les PDFs selon le dernier numéro du nom d'objet
     * @param c connection à la base de donnée
     * @param nom_objet nom de l'objet
     * @param idObjet idenfiant de l'objet
     * @return un nombre de type Integer
     * @throws Exception
     */
    public static int getNumerosequentiel(Connection c, String nom_objet, String idObjet) throws Exception{
        ServeurPDF spdf = new ServeurPDF();
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"' ORDER BY id DESC");
        if(spdfs.length>0 && spdfs[0].getIdobjet().equals(idObjet)){
            spdfs[0].updateToTable(c);
            return Integer.valueOf(spdfs[0].getRemarque());
        }
        spdf.construirePK(c);
        spdf.setNom_objet(nom_objet);
        spdf.setIdobjet(idObjet);
        if(spdfs.length>0){
            spdf.setRemarque(String.valueOf(Integer.valueOf(spdfs[0].getRemarque())+1));
            spdf.insertToTable(c);
            return Integer.valueOf(spdfs[0].getRemarque())+1;
        }
        else{
            spdf.setRemarque(String.valueOf(1));
            spdf.insertToTable(c);
            return 1;
        }
        
    }


    /**
     * Ajouter le numéro du pdf de 1
     * @param nom_objet nom de l'objet pour faire la mis à jour 
     * @param idObjet l'indenfiant pour faire la mis à jour
     * @throws Exception
     */
    public static void updateServeurPDF(String nom_objet, String idObjet) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ServeurPDF spdf = new ServeurPDF();
            ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, c, " AND nom_objet = '"+nom_objet+"' and idobjet='"+idObjet+"'");
            if(spdfs.length==0) throw new Exception("Exception lors de la generation du numero sequentiel");
            spdf = spdfs[0];
            spdf.setRemarque(String.valueOf(Integer.valueOf(spdf.getRemarque())+1));
            spdf.updateToTable(c);
            c.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }

    /**
     * Insertion de l'objet ServeurPDF mais si l'objet existe déjà la fonction fait une mis à jour
     * @param nom_objet nom de l'objet 
     * @param idObjet idenfiant de l'objet 
     * @param etat etat de l'objet
     * @throws Exception
     */
    public static void insertHistoPDF(String nom_objet, String idObjet, int etat) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            if(testIfExist(nom_objet, idObjet, etat)){
                updateEtatServeur(nom_objet, idObjet, etat);
                c.commit();
                return;
            }
            ServeurPDF spdf = new ServeurPDF();
            spdf.construirePK(c);
            spdf.setNom_objet(nom_objet);
            spdf.setIdobjet(idObjet);
            spdf.setEtat(etat);
            spdf.insertToTable(c);
            c.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }


    /**
     * Insertion de l'objet ServeurPDF avec remarque mais si l'objet existe déjà la fonction fait une mis à jour
     * @param nom_objet nom de l'objet 
     * @param idObjet idenfiant de l'objet 
     * @param etat  etat de l'objet
     * @param remarque le nouveau remarque
     * @throws Exception
     */
    public static void insertHistoPDFWithRemarque(String nom_objet, String idObjet, int etat, String remarque) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            if(testIfExist(nom_objet, idObjet, etat)){
                updateEtatServeurWithRemarque(nom_objet, idObjet, etat, remarque);
                c.commit();
                return;
            }
            ServeurPDF spdf = new ServeurPDF();
            spdf.construirePK(c);
            spdf.setNom_objet(nom_objet);
            spdf.setIdobjet(idObjet);
            spdf.setEtat(etat);
            spdf.setRemarque(remarque);
            spdf.insertToTable(c);
            c.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }

    /**
     * Mis à jour de la numero de notification dans le nom de l'objet et l'identifiant de l'objet donnée
     * @param nom_objet nom de l'objet 
     * @param idObjet idenfiant de l'objet 
     * @throws Exception
     */
    public static void updateNumeroNotificationAt(String nom_objet, String idObjet) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            ServeurPDF spdf = new ServeurPDF();
            ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(spdf, null, null, null, " AND NOM_OBJET ='"+nom_objet+"'");
            if(spdfs.length>0) spdf = spdfs[0];
            spdf.setIdobjet(idObjet);
            spdf.setRemarque(String.valueOf(Integer.valueOf(spdf.getRemarque())+1));
            spdf.upDateToTable();
            c.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }
    }

    /**
     * Sert à tester si c'est null ou vide la valeur donné en paramètre
     * @param value la valeur à tester
     * @return true or false
     */
    public static boolean testNullEtVide(String value){
        if(value==null || value.equalsIgnoreCase("") || value.equalsIgnoreCase("null")) return false;
        return true;
    }

    /**
     * Cette fonction sert à générer un critère apres where avec valeur null
     * @param attribut
     * @param value
     * @param critere
     * @return une chaine de caractère 
     */
    public static String makeCritereStringWithTestNull(String attribut, String value, String critere){
        if(!testNullEtVide(value)) return "";
        return " AND "+attribut.toUpperCase()+critere+"'"+value+"'";
    }

    /**
     * Sert à obtenir la valeur qui n'est pas null et vide 
     * @param valeur
     * @return la valeur qui n'est ni null ni vide 
     */
    public static String champNullZero(String valeur){
        if(!testNullEtVide(valeur)) return "0";
        return valeur;
    }

    /**
     * Sert à replir des 0 dans un nombre 
     * @param nombre nombre à remplir
     * @param isany le nombre de 0 à remplir
     * @return le nombre avec 0 de type string
     */
    public static String remplirParZero(int nombre, int isany){
        String nombreString = String.valueOf(nombre);
        for(int i=nombreString.length();i<isany;i++){
            nombreString = "0" + nombreString;
        }
        return nombreString;
    }

    /**
     * Cette fonction permet d'obtenir le numero d'identification PDF
     * @param u user actuelle 
     * @param nomObjet nom de l'objet 
     * @param identification identification de l'objet
     * @param c connection pour la base 
     * @return  le numero d'identification PDF de type string
     * @throws Exception
     */
    public static String getNumeroIdentificationPDF(UserEJB u, String nomObjet, String identification, Connection c) throws Exception{
        boolean test = false;
        try{
            if(c==null){
                c = new UtilDB().GetConn();
                test = true;
            }
            String retour="";
            MapUtilisateur pers = u.getUser();
            String initiale = pers.getTuppleID();
            if (initiale == null || initiale.isEmpty()) initiale = creerInitiale(pers.getNomuser(), "");
            retour+=getNumeroIdentificationPDF(nomObjet, c)+identification+initiale+"/"+Utilitaire.getAnneeEnCours();
            return retour;
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(test && c!=null) c.close();
        }
    }

    /**
     * Permet d'avoir le numéro d'indentification du PDF 
     * @param nomObjet nom de l'objet
     * @param c connection pour la base 
     * @return
     * @throws Exception
     */
    public static String getNumeroIdentificationPDF(String nomObjet, Connection c) throws Exception{
        ServeurPDF[] spdfs = (ServeurPDF[]) CGenUtil.rechercher(new ServeurPDF(), null, null, c, " AND NOM_OBJET ='" + nomObjet + "'");
        String numero = "";
        if(spdfs.length>0){
            boolean test = testWithUpdateNumeroChrono(nomObjet, spdfs[0].getEtat());
            updateNumeroNotificationAt(nomObjet, "");
            if(test) numero = "1";
            else{
                numero = String.valueOf(Integer.valueOf(spdfs[0].getRemarque())+1);
            }
        }
        else{
            insertHistoPDFWithRemarque(nomObjet, nomObjet, Utilitaire.getAneeEnCours()-2000, "1");
            numero = "1";
        }
        return numero;
    }

    /**
     * Cette fonction permet de créer une initiale 
     * @param nom nom pou créer l'initiale
     * @param prenom prénom pou créer l'initiale
     * @return l'initiale 
     * @throws Exception
     */
    public static String creerInitiale(String nom, String prenom) throws Exception {
        String ret = "";
        String[] n = Utilitaire.champNull(nom).split(" ");
        String[] p = Utilitaire.champNull(prenom).split(" ");
        if (n.length > 0) {
            for (int i = 0; i < n.length; i++) {
                if (n[i] != null && !n[i].isEmpty()) {
                    ret += n[i].charAt(0);
                }
            }
        }
        if (p.length > 0) {
            for (int i = 0; i < p.length; i++) {
                if (p[i] != null && !p[i].isEmpty()) {
                    ret += p[i].charAt(0);
                }
            }
        }
        return ret.toUpperCase();
    }

    /**
     * Cette fonction permet de savoir si l'année en paramètre  est egal à l'année en cours ou pas .
     * Sinon elle appelle la fonction : <pre> updateWhenNotAnneeEnCours </pre>
     * @param nomObjet nom de l'objet
     * @param annee l'année à tester
     * @return true or false
     * @throws Exception
     */
    public static boolean testWithUpdateNumeroChrono(String nomObjet, int annee) throws Exception{
        boolean test = false;
        if((annee+2000)==Utilitaire.getAneeEnCours()) test = false;
        else{
            updateWhenNotAnneeEnCours(nomObjet, annee);
            test = true;
        }
        return test;
    }
}
