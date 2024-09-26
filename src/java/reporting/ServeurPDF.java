/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporting;

import bean.ClassEtat;
import java.sql.Connection;

/**
 * @deprecated
 * Cette classe sert à stocker les informations des pdf d'un objet
 * <p>Par exemple on veut attacher à la classe <code>Facture</code> des pdfs.</p>
 * <p>Pour gérer les pièces jointes, veuillez utiliser {@link service.UploadService} et {@link bean.UploadPj}</p>
 * @author BICI
 */
public class ServeurPDF extends ClassEtat{
    private String id, nom_objet, idobjet, remarque;

    /**
     *  Constructeur par defaut 
     */
    public ServeurPDF() {
        super.setNomTable("SERVEUR_PDF_CNAPS");
    }

    /**
     * Constructeur 
     * @param id identifiant 
     * @param nom_objet nom de l'objet
     * @param idobjet id de l'objet
     */
    public ServeurPDF(String id, String nom_objet, String idobjet) {
        setId(id);
        setNom_objet(nom_objet);
        setIdobjet(idobjet);
        super.setNomTable("SERVEUR_PDF_CNAPS");
    }

    /**
     * Constructeur 
     * @param nom_objet nom de l'objet
     * @param idobjet id de l'objet
     */
    public ServeurPDF(String nom_objet, String idobjet) {
        setNom_objet(nom_objet);
        setIdobjet(idobjet);
        super.setNomTable("SERVEUR_PDF_CNAPS");
    }

    /**
     * Constructeur
     * @param id identifiant
     * @param nom_objet nom de l'objet
     * @param idobjet identifiant de l'objet
     * @param remarque remarque 
     */
    public ServeurPDF(String id, String nom_objet, String idobjet, String remarque) {
        setId(id);
        setNom_objet(nom_objet);
        setIdobjet(idobjet);
        setRemarque(remarque);
        super.setNomTable("SERVEUR_PDF_CNAPS");
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Avoir le nom de l'objet
     * @return nom de l'objet
     */
    public String getNom_objet() {
        return nom_objet;
    }

    public void setNom_objet(String nom_objet) {
        this.nom_objet = nom_objet;
    }

    /**
     * Avoir l'id de l'objet
     * @return l'id de l'objet
     */
    public String getIdobjet() {
        return idobjet;
    }

    public void setIdobjet(String idobjet) {
        this.idobjet = idobjet;
    }

    /**
     * Obtenir le remarque(plupart du temps le nom du pdf)
     * @return remarque
     */
    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    
    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    public void construirePK(Connection c) throws Exception {
        this.preparePk("PEX", "getSeqServeurPdfCnaps");
        this.setId(makePK(c));
    }
}
