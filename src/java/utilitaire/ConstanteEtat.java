/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

public class ConstanteEtat {

    public static final String journalConstatation = "46";
    public static final int etatNotificationLue = 2;
    public static final int etatNotificationNonLue = 1;
    public static final int etatTVAPaye = 8;
    private static final int etatPrivilegerRetourRelance = 2;
    private static final int etatRadie = 2;
    private static final int etatDossierAnnuler = -1;
    private static final int etatAnnuler = 0;
    private static final int etatEnAttente = 7;
    private static final int etatCreer = 1;
    private static final int etatMedicamentNonLivre = 1;
    private static final int etatMedicamentLivre = 2;
    private static final int etatDossierRecue = 2;
    public static final int constanteEtatFinaliser = 4;
    private static final int etatSituationdroitSuspendue = 8;
    private static final int etatSituationdroitBloquer = 9;
    private static final int etatCloture = 9;
    private static final int etatOvCloture = 9;
    private static final int etatTefCloture = 9;
    private static final int etatTefRejeter = 10;
    private static final int etatRejeter = 10;
    private static final int etatObjetRejeter = 10;
    private static final int etatRejetSupOp = 10;
    private static final int etatObjetValider = 11;
    private static final int etatImmoCondamne = 20;
    private static final int etatImmoCede = 30;
    private static final int etatValider = 11;
    private static final int etatValiderSupOp = 11;
    private static final int etatValiderSupOv = 11;
    private static final int etatValiderSupTef = 11;
    private static final int etatEcritureValider = 11;
    private static final int etatSituationdroitOK = 11;
    private static final int etatOpInclusOvCree = 12;
    private static final int etatEcritureGenerer = 15;
    private static final int etatEcritureCessionGenerer = 16;
    private static final int etatDemandePriseEnChargeDejaPrise = 17;
    private static final int etatDemandeConsultationDejaConsulte = 17;
    private static final int etatSituationdroitRappel = 18;
    private static final int etatOpInclusOvCloture = 19;
    private static final int etatTefEnCasOvCloturer = 19;
    private static final int etatOpInclusOvRejeter = 20;
    private static final int etatTefEnCasOpRejeter = 20;
    private static final int etatDnok = 21;
    private static final int etatOpInclusOvValide = 21;
    public static final int etatExpedition = 21;
    private static final int etatTefEnCasOpValider = 21;
    private static final int etatFinPlacement = 22;
    private static final int etatSituationdroitSuspensionAutoPMD = 28;
    private static final int etatTefEnCasOvRejeter = 30;
    private static final int etatTefEnCasOvValider = 31;
    private static final int etatSituationdroitSuspensionAutoBAF = 38;
    private static final int etatTalonDNOk = 41;
    private static final int etatTalonVentillationValider = 21;
    private static final int etatSituationdroitSuspensionAutoCIE = 48;
    private static final int etatPaye = 51;
    private static final int etatTefFictif = 61;
    private static final int etatEcritureContre = 100;
    private static final int etatPensSusp = 8;
    //private static final int etatDemandeCreer=1;
    private static final int etatDemandeEnvoye = 3;
    private static final int etatDemandeRecue = 5;
    private static final int etatDemandeVise = 7;
    private static final int etatDemandeLivre = 9;
    private static final int etatDemandeAccepte = 11;

    private static final int etatDemandeLivraisonPrepare = 3;
    private static final int etatDemandeLivraisonEnvoye = 5;
    private static final int etatDemandeLivraisonRecue = 11;

    public static final int etatProforma = 8;
    public static final int etatBCTefCreer = 17;
    private static final int etatOvInclusTitreValider = 21;
    private static final int etatOvInclusTitreRejeter = 20;
    private static final int etatOvInclusTitreCree = 12;
    private static final int etatOpInclusTitreRejeter = 30;
    private static final int etatOpInclusTitreValider = 31;
    private static final int etatOpRecuParLeBeneficiaire = 31;
    private static final int etatOpMdpEspeceVerifier = 13;
    private static final int etatOpMdpEspeceValiderService = 23;

    private static final int etatEditionRecalculee = 15;
    private static final int etatEditionCloturee = 19;
    private static final String changement_indice = "TYD000005";

    private static final int etatSTCCreer = 24;
    private static final int etatSTCPayer = 34;

    private static final int etatRappelGenerer = 44;
    private static final int etatDebauche = -2;
    
    private static final String compteTva = "4456100";
    
    public static final int etatValideNonApprouve = 5;
    public static final String JournalReportNouveau= "COMP000015";

    public static String getJournalReportNouveau() {
        return JournalReportNouveau;
    }
    public static String getCompteTVA(){
        return compteTva;
    }

    public static int getEtatDebauche(){
        return etatDebauche;
    }
    
    public static int getEtatEditionCloturee() {
        return etatEditionCloturee;
    }

    public static String getChangement_indice() {
        return changement_indice;
    }

    public static int getEtatSTCCreer() {
        return etatSTCCreer;
    }

    public static int getEtatSTCPayer() {
        return etatSTCPayer;
    }

    public static int getEtatOpMdpEspeceVerifier() {
        return etatOpMdpEspeceVerifier;
    }

    public static int getEtatOpMdpEspeceValiderService() {
        return etatOpMdpEspeceValiderService;
    }
    private static final int etatOpMdpEspeceValiderSup = 33;

    public static int getEtatOpMdpEspeceValiderSup() {
        return etatOpMdpEspeceValiderSup;
    }
    private static int etatPretretour = 100;

    public static int getEtatOpRecuParLeBeneficiaire() {
        return etatOpRecuParLeBeneficiaire;
    }
    private static final int etatOpInclusTitreCreer = 32;
    private static final int etatOpRetournee = 41;
    public static final int etatOvValideSmsEnvoye = 61;

    public static int getEtatOpRetournee() {
        return etatOpRetournee;
    }

    private static final int etatTefInclusTitreValider = 41;
    private static final int etatTefInclusTitreRejeter = 40;

    private static final int etatVehiculeDisponible = 0;
    private static final int etatVehiculeMaintenance = 1;
    private static final int etatVehiculeEnDeplacement = 2;

    private static final int etatPlanningEntretienNonRealise = 0;
    private static final int etatPlanningEntretienRealise = 1;

    private static final int etatImmoRemiseMagasin = 16;
    public static final int paiePersonnelDebauche = 12;
    public static final int paiePersonnelDeces = 13;
    public static final int paiePersonnelDemission = 14;
    public static final int paiePersonnelRenvoi = 15;
    public static final int paiePersonnelRetraite = 16;
    public static final int paiePersonnelSuspension = 17;

    public static final int paieRubriqueCreer = 1;
    public static final int paieRubriqueValider = 11;
    public static final int paieRubriqueAnnuler = 0;

    private static final int etatretourner = 16;
    private static final int etatReleveConstater = 20;
    private static final int etatRDeclarationConstater = 20;
    private static final int etatORPaye = 20;

    private static final int etatCmViseParDg = 9;
    private static final int etatCmViseParDc = 11;
    private static final int etatCmEnregistre = 15;
    private static final int etatJournalFermer = 15;
    private static final int etatGrandLivreDejaRepporte = 15;
    private static final int etatDossierRetourne = 8;

    private static final int etatTVACloturee = 9;

    private static final int etatArchiveDetruiser = 0;
    private static final int Etatdispatcher = 12;
    private static final int etatNonRecue = -1;
    private static final int etatEvalue = 9;
    private static final int etatCourrierRetourner = 30;
    private static final int etatCourrierRecu = 31;
    private static final int etatCourrierEntrant = 21;
    private static final int etatCourrierSortant = 20;

    public static final int etatOpRepayer = -1;
    public static final int etatFormationRealise = 12;

    public static final int etatDossierValider = 9;
    public static final int etatRenteRadier = 3;

    private static final int etatLivreDisponible = 11;
    private static final int etatLivreIndisponible = 12;
    private static final int etatSupprimer = -11;
    private static final int etatCongeDemandeApprouve = 9;
    private static final int etatMissionTermine = 21;
    private static final int etatMissionApprouve = 9;

    public static int getEtatCourrierRetourner() {
        return etatCourrierRetourner;
    }

    public static int getEtatTVACloturee() {
        return etatTVACloturee;
    }

    public static int getEtatCongeDemandeApprouve() {
        return etatCongeDemandeApprouve;
    }

    public static int getEtatMissionApprouve() {
        return etatMissionApprouve;
    }

    public static int getEtatDossierValider() {
        return etatDossierValider;
    }

    public static int getEtatCourrierRecu() {
        return etatCourrierRecu;
    }

    public static int getEtatOpRepayer() {
        return etatOpRepayer;
    }

    public static int getEtatPrivilegerRetourRelance() {
        return etatPrivilegerRetourRelance;
    }

    public static int getEtatEvalue() {
        return etatEvalue;
    }

    public static int getEtatGrandLivreDejaRepporte() {
        return etatGrandLivreDejaRepporte;
    }

    public static int getEtatJournalFermer() {
        return etatJournalFermer;
    }

    public static int getEtatCmViseParDg() {
        return etatCmViseParDg;
    }

    public static int getEtatCmViseParDc() {
        return etatCmViseParDc;
    }

    public static int getEtatCmEnregistre() {
        return etatCmEnregistre;
    }

    public static int getEtatReleveConstater() {
        return etatReleveConstater;
    }
    public static final int etatDemandeRechargeCarteValiderSup = 110;

    public static int getEtatImmoRemiseMagasin() {
        return etatImmoRemiseMagasin;
    }

    public static int getEtatDemandeLivre() {
        return etatDemandeLivre;
    }

    public static int getEtatDemandeAccepte() {
        return etatDemandeAccepte;
    }

    public static int getEtatDemandeLivraisonPrepare() {
        return etatDemandeLivraisonPrepare;
    }

    public static int getEtatDemandeLivraisonEnvoye() {
        return etatDemandeLivraisonEnvoye;
    }

    public static int getEtatDemandeLivraisonRecue() {
        return etatDemandeLivraisonRecue;
    }

    public static int getEtatDemandeEnvoye() {
        return etatDemandeEnvoye;
    }

    public static int getEtatDemandeRecue() {
        return etatDemandeRecue;
    }

    public static int getEtatDemandeVise() {
        return etatDemandeVise;
    }

    public static int getEtatTefInclusTitreValider() {
        return etatTefInclusTitreValider;
    }

    public static int getEtatTefInclusTitreRejeter() {
        return etatTefInclusTitreRejeter;
    }

    public static int getEtatOpInclusTitreRejeter() {
        return etatOpInclusTitreRejeter;
    }

    public static int getEtatOpInclusTitreValider() {
        return etatOpInclusTitreValider;
    }

    public static int getEtatOpInclusTitreCreer() {
        return etatOpInclusTitreCreer;
    }

    public static int getEtatOvInclusTitreValider() {
        return etatOvInclusTitreValider;
    }

    public static int getEtatOvInclusTitreRejeter() {
        return etatOvInclusTitreRejeter;
    }

    public static int getEtatOvInclusTitreCree() {
        return etatOvInclusTitreCree;
    }

    public static int getEtatPensSusp() {
        return etatPensSusp;
    }

    public static int getEtatImmoCondamne() {
        return etatImmoCondamne;
    }

    public static int getEtatImmoCede() {
        return etatImmoCede;
    }

    public static int getEtatMedicamentLivre() {
        return etatMedicamentLivre;
    }

    public static int getEtatMedicamentNonLivre() {
        return etatMedicamentNonLivre;
    }

    public static int getEtatDossierAnnuler() {
        return etatDossierAnnuler;
    }

    public static int getEtatEcritureContre() {
        return etatEcritureContre;
    }

    public static int getEtatDossierRecue() {
        return etatDossierRecue;
    }

    public static int getEtatPaye() {
        return etatPaye;
    }

    public static int getEtatTalonDNOk() {
        return etatTalonDNOk;
    }

    private static final int etatImmoAttribuer = 17;

    public static int getEtatEcritureCessionGenerer() {
        return etatEcritureCessionGenerer;
    }

    public static int getEtatDemandePriseEnChargeDejaPrise() {
        return etatDemandePriseEnChargeDejaPrise;
    }

    public static int getEtatDemandeConsultationDejaConsulte() {
        return etatDemandeConsultationDejaConsulte;
    }

    public static int getEtatImmoAttribuer() {
        return etatImmoAttribuer;
    }

    public static int getEtatDnok() {
        return etatDnok;
    }

    public static String getSignificationSituationDroit(int situation) {
        if (situation == 28 || situation == 38 || situation == 48) {
            return "Suspension automatique";
        }
        if (situation == 11) {
            return "Situation ok";
        }
        if (situation == 8) {
            return "Suspendue";
        }
        if (situation == 9) {
            return "Bloqu�e";
        }
        if (situation == 18) {
            return "Rappel";
        } else {
            return "";
        }
    }

    public static int getEtatEcritureGenerer() {
        return etatEcritureGenerer;
    }

    public static int getEtatCreer() {
        return etatCreer;
    }

    public static int getEtatRejeter() {
        return etatRejeter;
    }

    public static int getEtatObjetRejeter() {
        return etatObjetRejeter;
    }

    public static int getEtatEcritureValider() {
        return etatEcritureValider;
    }

    public static int getEtatSituationdroitOK() {
        return etatSituationdroitOK;
    }

    public static int getEtatSituationdroitSuspendue() {
        return etatSituationdroitSuspendue;
    }

    public static int getEtatSituationdroitBloquer() {
        return etatSituationdroitBloquer;
    }

    public static int getEtatSituationdroitRappel() {
        return etatSituationdroitRappel;
    }

    public static int getEtatTefEnCasOvCloturer() {
        return etatTefEnCasOvCloturer;
    }

    public static int getEtatTefEnCasOvValider() {
        return etatTefEnCasOvValider;
    }

    public static int getEtatTefRejeter() {
        return etatTefRejeter;
    }

    public static int getEtatObjetValider() {
        return etatObjetValider;
    }

    public static int getEtatTefEnCasOvRejeter() {
        return etatTefEnCasOvRejeter;
    }

    public static int getEtatAnnuler() {
        return etatAnnuler;
    }

    public static int getEtatTefEnCasOpValider() {
        return etatTefEnCasOpValider;
    }

    public static int getEtatValiderSupOp() {
        return etatValiderSupOp;
    }

    public static int getEtatCloture() {
        return etatCloture;
    }

    public static int getEtatTefCloture() {
        return etatTefCloture;
    }

    public static int getEtatTefEnCasOpRejeter() {
        return etatTefEnCasOpRejeter;
    }

    public static int getEtatOpInclusOvCree() {
        return etatOpInclusOvCree;
    }

    public static int getEtatOpInclusOvCloture() {
        return etatOpInclusOvCloture;
    }

    public static int getEtatOvCloture() {
        return etatOvCloture;
    }

    public static int getEtatOpInclusOvRejeter() {
        return etatOpInclusOvRejeter;
    }
    private static final int etatRejetSupOv = 10;

    public static int getEtatRejetSupOv() {
        return etatRejetSupOv;
    }

    public static int getEtatOpInclusOvValide() {
        return etatOpInclusOvValide;
    }

    public static int getEtatValiderSupOv() {
        return etatValiderSupOv;
    }

    public static int getEtatValiderSupTef() {
        return etatValiderSupTef;
    }

    public static int getEtatRejetSupOp() {
        return etatRejetSupOp;
    }

    public static int getEtatValider() {
        return etatValider;
    }

    /**
     * @return the etatSituationdroitSuspensionAuto
     */
    public static int getEtatSituationdroitSuspensionAutoPMD() {
        return etatSituationdroitSuspensionAutoPMD;
    }

    public static int getEtatFinPlacement() {
        return etatFinPlacement;
    }

    public static int getEtatTefFictif() {
        return etatTefFictif;
    }

    public static String etatToChaine(String valeur) {
        int val = Utilitaire.stringToInt(valeur);
        if (val == ConstanteEtat.getEtatAnnuler()) {
            return "<b style='color:orange'>ANNUL&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatCloture()) {
            return "<b style='color:blue'>CLOTUR&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatDebauche()) {
            return "<b style='color:grey'>DEBAUCH&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatEditionCloturee()) {
            return "<b style='color:blue'>CL&Ocirc;TUR&Eacute;E</b>";
        }
        if (val == ConstanteEtat.getEtatEditionRecalculee()) {
            return "<b style='color:grey'>RECALCUL&Eacute;E</b>";
        }
        if (val >= ConstanteEtat.getEtatValider()) {
            return "<b style='color:green'>VIS&Eacute;(E)</b>";
        }
        if (val >= ConstanteEtat.getEtatEnAttente()) {
            return "<b style='color:red'>VIS&Eacute;(E)</b>";
        }
        if (val >= ConstanteEtat.getEtatPaye()) {
            return "<b style='color:red'>PAY&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatRejeter()) {
            return "<b style='color:red'>REJET&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatCreer()) {
            return "<b style='color:lightskyblue'>CR&Eacute;&Eacute;(E)</b>";
        }

        //ecriture
        if (val == ConstanteEtat.getEtatEcritureGenerer()) {
            return "<b>ECRITURE ACQUISITION GENER&Eacute;(E)</b>";
        }
        if (val == ConstanteEtat.getEtatEcritureCessionGenerer()) {
            return "<b>ECRITURE CESSION GENER&Eacute;(E)</b>";
        }
        //cms
        if (val == ConstanteEtat.getEtatDemandeConsultationDejaConsulte()) {
            return "<b>CONSULTATION GENER&Eacute;E POUR CETTE DEMANDE</b>";
        }
        if (val == ConstanteEtat.getEtatMedicamentLivre()) {
            return "<b>Livr&eacute;</b>";
        }
        if (val == ConstanteEtat.getEtatMedicamentNonLivre()) {
            return "<b>Non Livr&eacute;</b>";
        }
        if (val == ConstanteEtat.getEtatFinPlacement()) {
            return "<b>Cl&ocirc;tur&eacute;</b>";
        }
        if (val == ConstanteEtat.getEtatRappelGenerer()) {
            return "<b style='color:blue'>RAPPEL GENER&Eacute;</b>";
        }

        if (val == ConstanteEtat.getEtatretourner()) {
            return "<b style='color:blue'>Retourn�/Rendu</b>";
        }
//        if (val == ConstanteEtat.getEtatTefEnCasOvValider()) {
//            return "<b style='color:blue'> OV VALID&Eacute; POUR CETTE TEF</b>";
//        }
//        if (val == ConstanteEtat.getEtatTefEnCasOvRejeter()) {
//            return "<b style='color:blue'>OV REJET&Eacute; POUR CETTE TEF</b>";
//        }
//        if (val == ConstanteEtat.getEtatTefEnCasOpRejeter()) {
//            return "<b style='color:blue'>OP REJET&Eacute; POUR CETTE TEF</b>";
//        }
//        if (val == ConstanteEtat.getEtatTefEnCasOpValider()) {
//            return "<b style='color:blue'>OP VALID&Eacute; POUR CETTE TEF</b>";
//        }
//        //etat op
//        if (val == ConstanteEtat.getEtatOpInclusOvCree()) {
//            return "<b style='color:blue'>OP INCLUS DANS UN OV CR&Eacute;&Eacute;</b>";
//        }
//        if (val == ConstanteEtat.getEtatOpInclusOvCloture()) {
//            return "<b style='color:blue'>OP INCLUS DANS UN OV CLOTUR&Eacute;</b>";
//        }
//        if (val == ConstanteEtat.getEtatOpInclusOvRejeter()) {
//            return "<b style='color:blue'>OP INCLUS DANS UN OV REJET&Eacute;</b>";
//        }
//        if (val == ConstanteEtat.getEtatOpInclusOvValide()) {
//            return "<b style='color:blue'>OP INCLUS DANS UN OV VALID&Eacute;</b>";
//        }
//        if (val == ConstanteEtat.getEtatRejetSupOp()) {
//            return "<b style='color:blue'>OP REJET&Eacute; PAR LE DIRECTEUR FINANCIER</b>";
//        }
//        if (val == ConstanteEtat.getEtatValiderSupOp()) {
//            return "<b style='color:blue'>OP VALID&Eacute; PAR LE DIRECTEUR FINANCIER</b>";
//        }
        if (val == getEtatTalonDNOk()) {
            return "<b style='color:blue'>TALON DN OK</b>";
        }
        return "<b>AUTRE</b>";
    }

    /**
     * @return the etatSituationdroitSuspensionAutoBAF
     */
    public static int getEtatSituationdroitSuspensionAutoBAF() {
        return etatSituationdroitSuspensionAutoBAF;
    }

    /**
     * @return the etatSituationdroitSuspensionAutoCIE
     */
    public static int getEtatSituationdroitSuspensionAutoCIE() {
        return etatSituationdroitSuspensionAutoCIE;
    }

    /**
     * @return the etatEnAttente
     */
    public static int getEtatEnAttente() {
        return etatEnAttente;
    }

    public static int getEtatProforma() {
        return etatProforma;
    }

    /**
     * @return the etatVehiculeDisponible
     */
    public static int getEtatVehiculeDisponible() {
        return etatVehiculeDisponible;
    }

    /**
     * @return the etatVehiculeMaintenance
     */
    public static int getEtatVehiculeMaintenance() {
        return etatVehiculeMaintenance;
    }

    /**
     * @return the etatVehiculeEnDeplacement
     */
    public static int getEtatVehiculeEnDeplacement() {
        return etatVehiculeEnDeplacement;
    }

    /**
     * @return the etatTalonVentillationValider
     */
    public static int getEtatTalonVentillationValider() {
        return etatTalonVentillationValider;
    }

    public static int getConstanteEtatFinaliser() {
        return constanteEtatFinaliser;
    }

    public static int getEtatBCTefCreer() {
        return etatBCTefCreer;
    }

    public static int getEtatPlanningEntretienNonRealise() {
        return etatPlanningEntretienNonRealise;
    }

    public static int getEtatPlanningEntretienRealise() {
        return etatPlanningEntretienRealise;
    }

    public static int getEtatretourner() {
        return etatretourner;
    }

    /**
     * @return the etatRadie
     */
    public static int getEtatRadie() {
        return etatRadie;
    }

    public static int getEtatDossierRetourne() {
        return etatDossierRetourne;
    }

    /**
     * @return the etatArchiveDetruiser
     */
    public static int getEtatArchiveDetruiser() {
        return etatArchiveDetruiser;
    }

    /**
     * @return the Etatdispatcher
     */
    public static int getEtatdispatcher() {
        return Etatdispatcher;
    }

    /**
     * @return the etatNonRecue
     */
    public static int getEtatNonRecue() {
        return etatNonRecue;
    }

    public static int getEtatLivreDisponible() {
        return etatLivreDisponible;
    }

    public static int getEtatLivreIndisponible() {
        return etatLivreIndisponible;
    }

    public static int getEtatSupprimer() {
        return etatSupprimer;
    }

    public static int getEtatPretRetour() {
        return etatPretretour; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the etatRDeclarationConstater
     */
    public static int getEtatRDeclarationConstater() {
        return etatRDeclarationConstater;
    }

    public static int getEtatMissionTermine() {
        return etatMissionTermine;
    }

    /**
     * @return the etatCourrierEntrant
     */
    public static int getEtatCourrierEntrant() {
        return etatCourrierEntrant;
    }

    /**
     * @return the etatCourrierEntrant
     */
    public static int getEtatCourrierSortant() {
        return etatCourrierSortant;
    }

    /**
     * @return the etatORPaye
     */
    public static int getEtatORPaye() {
        return etatORPaye;
    }

    public static int getEtatNotificationLue() {
        return etatNotificationLue;
    }

    public static int getEtatNotificationNonLue() {
        return etatNotificationNonLue;
    }

    public static int getEtatRappelGenerer() {
        return etatRappelGenerer;
    }

    public static int getEtatEditionRecalculee() {
        return etatEditionRecalculee;
    }

    public static int getEtatValideNonApprouve() {
        return etatValideNonApprouve;
    }

}
