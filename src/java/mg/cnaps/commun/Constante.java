package mg.cnaps.commun;

import bean.TypeObjet;

public class Constante {

    private static final String dr_siege = "DR42";
    private static final String dr_abrev = "42";
    private static final String id_classe = "CLS0016"; // cab
    public static final String idtype_carte_prepaye = "1";
    public static final String idtype_carte_postpaye = "2";
    public static final String idtype_carte_fille = "3";
    public static final String idtype_carte_mere = "4";
    public static final String ovModePaiementCheque = "MP12";
    public static final String ovModePaiementEspece = "MP1";
    public static String idJovenna = "1";
    public static final String idLiquidite = "1";
    public static final String idBesoinFormation = "FTD2";
    public static final String idDemandeIndividuelle = "FTD2";
    public static final String listeIdArticleVehicule = "'TP000056','TP000024','TP000034','TP000044'";
    public static final String idEntretienVehiculeInterne = "0";
    public static final String libelleEntretienVehiculeInterne = "Interne";
    public static final String idEntretienVehiculeExterne = "1";
    public static final String libelleEntretienVehiculeExterne = "Externe";
    public static final String CODE_SERVICE_FORMATION = "6030";
    public static final String CODE_SERVICE_FORMATION2 = "6050";
    public static final String CODE_SERVICE_PERSONNEL = "6000";
    public static final String CODE_SERVICE_PERSONNEL2 = "6010";
    public static final String ID_TYPE_MOUVEMENT_VEHICULE_CNAPS = "TYM001";
    public static final String ID_ETAT_VEHICULE_DISPONIBLE = "0";
    public static final String ID_ETAT_VEHICULE_EN_MAINTENANCE = "1";
    public static final String ID_ETAT_VEHICULE_EN_DEPLACEMENT = "2";
    public static final String ID_ETAT_VEHICULE_VENDU = "3";
    public static final String ID_ETAT_VEHICULE_NON_DISPONIBLE = "4";
    public static final String ID_IMMO_CATEGORIE_MATERIEL_ROULANT = "CAT000010";
    public static final int diffJourRelance = 9;

    public static String getId_classe() {
	return id_classe;
    }

    public static String getDr_abrev() {
	return dr_abrev;
    }

    public static String getDr_siege() {
	return dr_siege;
    }

    /**
     * @return the idJovenna
     */
    public static String getIdJovenna() {
	return idJovenna;
    }

    /**
     * @param aIdJovenna the idJovenna to set
     */
    public static void setIdJovenna(String aIdJovenna) {
	idJovenna = aIdJovenna;
    }
    
    public static TypeObjet[] getMoisTous(){
        TypeObjet[] mois = new TypeObjet[12];
        mois[0] = new TypeObjet("1","Janvier","");
        mois[1] = new TypeObjet("2","Fevrier","");
        mois[2] = new TypeObjet("3","Mars","");
        mois[3] = new TypeObjet("4","Avril","");
        mois[4] = new TypeObjet("5","Mai","");
        mois[5] = new TypeObjet("6","Juin","");
        mois[6] = new TypeObjet("7","Juillet","");
        mois[7] = new TypeObjet("8","Aout","");
        mois[8] = new TypeObjet("9","Septembre","");
        mois[9] = new TypeObjet("10","Octobre","");
        mois[10] = new TypeObjet("11","Novembre","");
        mois[11] = new TypeObjet("12","Decembre","");
        return mois;
    }

}

