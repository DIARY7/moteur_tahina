/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CGenUtil;
import bean.UploadPj;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mg.spat.AttacherFichier;
import utilitaire.UtilDB;

/**
 *
 * @author JESSI
 */
public class UploadService {
    public static AttacherFichier[] getUploadFile(String idmere)throws Exception{
      Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            AttacherFichier criteriaConsultation = new AttacherFichier();
            AttacherFichier[] fichierConsultation = (AttacherFichier[]) CGenUtil.rechercher(criteriaConsultation, null, null, c, " and mere ='"+idmere+"'");
            return fichierConsultation;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
       
    }
    
      public static void createUploadedPj(String iddossier, String iduser, HashMap<String, String> listeVal, Iterator it, String nomtable, String nomprocedure, String mere) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            while (it.hasNext()) {
                Map.Entry item = (Map.Entry) it.next();
                String key = item.getKey().toString();
                String value = item.getValue().toString();
                String valI = value.substring(8, value.length());
                String libelle = listeVal.get("libelle" + valI);
                UploadPj fichier = new UploadPj(nomtable, nomprocedure, "FLE", libelle, key, mere);
                fichier.construirePK(c);
                fichier.insertToTableWithHisto(iduser, c);
            }

            /*if (iddossier != null && iddossier.length() > 3) {
                Dossiers temp = new Dossiers();
                if (String.valueOf(iddossier.charAt(4)).compareToIgnoreCase("E") == 0) {
                    temp.setNomTable("sig_dossiers_employeurs");
                }
                Dossiers[] doss = (Dossiers[]) CGenUtil.rechercher(temp, null, null, c, " and id = '" + iddossier + "'");
                if (doss.length == 0) {
                    throw new Exception("Erreur dosier introuvable");
                }
                if (doss[0].getDossier_agent_numerisation() == null) {
                    doss[0].setDossier_agent_numerisation(iduser);
                }
                if (doss[0].getDossier_date_numerisation() == null) {
                    doss[0].setDossier_date_numerisation(Utilitaire.dateDuJourSql());
                }
                doss[0].updateToTable(c);
            }*/
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }
}
