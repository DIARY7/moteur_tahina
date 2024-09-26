package bean;

import java.sql.Connection;

/**
 *Classe de mapping utilisé pour les fichiers attachés enregistrés dans la base
 * <h3>Exemple d'utilisation</h3>
 * <pre>
 * UploadPj upload = new UploadPj("ATTACHER_FICHIER","GETSEQ_ATTACHER_FICHIER","FLE","Document de test","test.pdf","DOC001");
 * </pre>
 * @author NERD
 */
public class UploadPj extends ClassMAPTable {

    private String id; 
    private String libelle;
    /**Chemin du fichier attaché */
    private String chemin;
    /**Id de l'objet auquel le fichier est attaché */ 
    private String mere;

    /**
     * Constructeur
     * @param nomTable nom de la table
     * @param nomProcedure nom de la procedure à utiliser pour la création de l'id
     * @param suff indice de l'id
     * @param libelle libelle du fichier
     * @param chemin chemin du fichier à attacher
     * @param mere id de l'objet auquel est attaché le fichier
     */
    public UploadPj(String nomTable, String nomProcedure, String suff, String libelle, String chemin, String mere) {
        super.setNomTable(nomTable);
        super.setNomProcedureSequence(nomProcedure);
        super.setIndicePk(suff);
        this.setLibelle(libelle);
        this.setChemin(chemin);
        this.setMere(mere);
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk(this.getINDICE_PK(), this.getNomProcedureSequence());
        this.setId(makePK(c));
    }

    /**
     * Constructeur
     * @param nt nom de la table
     */
    public UploadPj(String nt) {
        super.setNomTable(nt);
    }

    /**
     * Constructeur vide
     */
    public UploadPj() {
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
    }

    public void setLibelle(String libelle) {
        if (libelle == null) {
            this.libelle = "-";
        } else {
            this.libelle = libelle;
        }
    }

    public String getLibelle() {
        return libelle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}