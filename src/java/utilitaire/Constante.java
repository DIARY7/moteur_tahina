package utilitaire;

public class Constante {

    private static String idTypeLCRecette = "TLC00001";
    private static String idTypeLCDepense = "TLC00002";
    public static String idRoleDirecteur = "directeur";
    public static String idRoleUtilisateur = "utilisateur";
    public static String idRoleAdmin = "admin";
    public static String idRoleAdminFacture = "adminFacture";
    public static String idRoleAdminCompta = "compta";
    public static String idRoleDg = "dg";
    public static String idRoleSaisie = "saisie";
    public static String tableOpFfLc = "OpFfLc";
    private static String mois[] = {"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
    private static String moisRang[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private static String objetDepense = "depense";
    private static String objetRecette = "recette";
    private static String objetFacture = "facture";
    private static String objetFactureF = "factureF";
    static private String tableRecetteLC = "recettesLC";
    static private String tableFactureLC = "factureLigneCredit";
    static private String tableFactureFLC = "FactureFournisseurLC";
    static private String tableDepenseLC = "sortieFF";
    private static String idFournAuto = "clt1";
    static private String objFactureProf = "FactureProfFournisseur";
    static private String objetBc = "BC";
    static private String objetCDetailFactProf = "detFactProf";
    static private String objetDetailBC = "detBC";
    static private String objetDetailFactFourn = "detFactF";
    static private String dedFactProfFourn = "DedFACTUREProfFOURNISSEUR";
    static private String DetailLcCree = "0";
    static private String DetailLcValide = "1";
    static private String objetDedBc = "DedBc";
    static private String objetLcDetMvt = "LcDetailMvtCaisse";
    static private String objetLcDetDed = "LcDetailDed";
    static private String objetLcDetOp = "LcDetailop";
    static private String objetOpFf = "OPFACTUREFOURNISSEUR";
    static private String objetDed = "ded";
    static private String objetOp = "ORDONNERPAYEMENT";
    static private String objetLcDetail = "LcDetails";
    static private String tableDetailBC = "detailBC";
    private static String objetMvtCaisse = "mvtCaisse";
    static private String objectDetailFPF = "detailFPF";
    public static String objetFactureFournisseur = "FactureFournisseur";
    static private String opFactureFournisseur = "OPFACTUREFOURNISSEUR";
    static private String objetLigneCredit = "LigneCredit";
    static private String objetLigneCreditRecette = "LigneCreditRecette";
    static private String visaOp = "VISAORDREPAYEMENT";
    static private String visaOr = "VISAOR";
    static public String typeOpFacture = "facture";
    static public String typeOpNormale = "normale";
    static public String tableVisaOp = "VISAORDREPAYEMENT";
    static public String objetFactureClient = "FACTURECLIENT";
    public static String tableOrFcLc = "ORFCLC";
    static public String tableFactureCLC = "FactureClientLC";
    static public String factureClient = "FACTURECLIENT";
    public static final String constanteTiersStock = "TIERS1";
    public static final String constantePeriode = "T";
    public static final String idDirectionTamatave = "DIR3";
    public static final String idDirectionMarovintsy = "DIR00001";
    public static final String idDirectionAntsirakambo = "DIR00002";
      public static final String roleadminarticle = "adminarticle";
    
    public static final String typeInventaire = "tpi1";
    public static final String constanteEntreeStock = "MVT000002";
    public static final String constanteSortieStock = "MVT000003";
    public static final String constanteReintegrStock = "MVT000001";
    public static final String conge = "CONGE";
    public static final String mouvement = "MOUVEMENT";
    public static final String depart = "DEPART";
    public static final String deplacement = "DEPLACEMENT";
    public static final String deces = "DECES";
    public static final String etatCreer = "1";
    
    public static final String DEFAULT_TYPE_MVT = "tpmvt2";
    public static final String DEFAULT_TYPE_MVT_ENTREE = "tpmvt1";
    
    public static final Double  IndiceNonAgri= 0.8681;
    public static final Double  HeureNonAgri= 173.33;
    
    public static int COMPTA_COMPTE_GEN_MAX_CHAR = 13;
    public static int COMPTA_COMPTE_ANAL_MAX_CHAR = 17;
    public static String COMPTA_TYPE_COMPTE_GENERAL = "1";
    public static String COMPTA_TYPE_COMPTE_AUTRE = "2";
    public static String COMPTA_TYPE_COMPTE_ANALYTIQUE = "3";
        
    public static Double getHeureNonAgri() {
        return HeureNonAgri;
    }  
    
    public static Double getIndiceNonAgri() {
        return IndiceNonAgri;
    }    
    
    public static String getIdTypeLCDepense() {
        return idTypeLCDepense;
    }

    public static String getDetailLcCree() {
        return DetailLcCree;
    }

    public static String getDetailLcValide() {
        return DetailLcValide;
    }

    public static String getIdTypeLCRecette() {
        return idTypeLCRecette;
    }

    public static String getIdRoleDirecteur() {
        return idRoleDirecteur;
    }

    public static String[] getMois() {
        return mois;
    }

    public static String[] getMoisRang() {
        return moisRang;
    }

    public static String getObjetDepense() {
        return objetDepense;
    }

    public static String getObjetRecette() {
        return objetRecette;
    }

    public static String getObjetFacture() {
        return objetFacture;
    }

    public static String getObjetFactureF() {
        return objetFactureF;
    }

    public static String getTableRecetteLC() {
        return tableRecetteLC;
    }

    public static String getTableFactureLC() {
        return tableFactureLC;
    }

    public static String getTableFactureFLC() {
        return tableFactureFLC;
    }

    public static String getTableDepenseLC() {
        return tableDepenseLC;
    }

    public static String getIdFournAuto() {
        return idFournAuto;
    }

    public static String getObjFactureProf() {
        return objFactureProf;
    }

    public static String getObjetBc() {
        return objetBc;
    }

    public static String getObjetCDetailFactProf() {
        return objetCDetailFactProf;
    }

    public static String getObjetDetailBC() {
        return objetDetailBC;
    }

    public static String getObjetDetailFactFourn() {
        return objetDetailFactFourn;
    }

    public static String getDedFactProfFourn() {
        return dedFactProfFourn;
    }

    public static void setObjetDedBc(String objetDedBce) {
        objetDedBc = objetDedBce;
    }

    public static String getObjetDedBc() {
        return objetDedBc;
    }

    public static void setObjetLcDetMvt(String objetLcDetMvte) {
        objetLcDetMvt = objetLcDetMvte;
    }

    public static String getObjetLcDetMvt() {
        return objetLcDetMvt;
    }

    public static String getObjetLcDetDed() {
        return objetLcDetDed;
    }

    public static String getObjetLcDetOp() {
        return objetLcDetOp;
    }

    public static String getObjetOpFf() {
        return objetOpFf;
    }

    public static String getObjetDed() {
        return objetDed;
    }

    public static String getObjetOp() {
        return objetOp;
    }

    public static String getObjetLcDetail() {
        return objetLcDetail;
    }

    public static String getTableDetailBC() {
        return tableDetailBC;
    }

    public static String getObjetMvtCaisse() {
        return objetMvtCaisse;
    }

    public static String getObjectDetailFPF() {
        return objectDetailFPF;
    }

    public static String getObjetFactureFournisseur() {
        return objetFactureFournisseur;
    }

    public static String getOpFactureFournisseur() {
        return opFactureFournisseur;
    }

    public static String getObjetLigneCredit() {
        return objetLigneCredit;
    }

    public static String getObjetLigneCreditRecette() {
        return objetLigneCreditRecette;
    }

    public static String getVisaOp() {
        return visaOp;
    }

    public static String getVisaOr() {
        return visaOr;
    }

}
