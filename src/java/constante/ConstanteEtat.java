/*
 * constante est un package de classe regroupant les différentes classes de constante
 * selon leur utilisation
 */
package constante;

import utilitaire.Utilitaire;

/**
 * Classe contenant états des d'un objet
 * @author BICI
 */

public class ConstanteEtat { 
    public static final int etatNotificationLue = 2;
    public static final int etatNotificationNonLue = 1;
    private static final int etatLivraison = 20;
    private static final int etatPayer= 19;
    private static final int etatAnnuler = 0;
    private static final int etatCreer = 1;
    private static final int etatCloture = 9;
    private static final int etatRejeter = 10;
    private static final int etatValider = 11;
    public static final int etatProforma = 8;
    private static final int etatInterviewe = 7;
    public static final String ETAT_PAYE = "PAYE";
    public static final String ETAT_NON_PAYE = "NON PAYE";
    public static final String ETAT_LIVRE = "LIVRE";
    
    public static final int paiePersonnelDebauche = 12;
    public static final int paiePersonnelDeces = 13;
    public static final int paiePersonnelDemission = 14;
    public static final int paiePersonnelRenvoi = 15;
    public static final int paiePersonnelRetraite = 16;
    public static final int paiePersonnelSuspension = 17;

    public static final int paieRubriqueCreer = 1;
    public static final int paieRubriqueValider = 11;
    public static final int paieRubriqueAnnuler = 0;

        
  
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
	    return "Bloquï¿½e";
	}
	if (situation == 18) {
	    return "Rappel";
	} else {
	    return "";
	}
    }
    public static int getEtatCreer() {
	return etatCreer;
    }

    public static int getEtatRejeter() {
	return etatRejeter;
    }

   
    public static int getEtatAnnuler() {
	return etatAnnuler;
    }

    public static int getEtatCloture() {
	return etatCloture;
    }

   
    private static final int etatRejetSupOv = 10;

    public static int getEtatRejetSupOv() {
	return etatRejetSupOv;
    }

   
    public static int getEtatValider() {
	return etatValider;
    }

    public static int getEtatInterviewe() {
        return etatInterviewe;
    }
   
    public static String etatToChaine(String valeur) {
	int val = Utilitaire.stringToInt(valeur);
	if (val == ConstanteEtat.getEtatAnnuler()) {
	    return "<b style='color:orange'>ANNUL&Eacute;(E)</b>";
	}
	if (val == ConstanteEtat.getEtatCloture()) {
	    return "<b style='color:blue'>CLOTUR&Eacute;(E)</b>";
	}
	if (val >= ConstanteEtat.getEtatValider()) {
	    return "<b style='color:green'>VIS&Eacute;(E)</b>";
	}
	
	if (val == ConstanteEtat.getEtatRejeter()) {
	    return "<b style='color:red'>REJET&Eacute;(E)</b>";
	}
	if (val == ConstanteEtat.getEtatCreer()) {
	    return "<b style='color:lightskyblue'>CR&Eacute;&Eacute;(E)</b>";
	}
        if(val == ConstanteEtat.getEtatInterviewe()){
            return "<b style='color:lightskyblue'>INTERVIEW&Eacute;(E)</b>";
        }
	return "<b>AUTRE</b>";
    }

    public static int getEtatProforma() {
	return etatProforma;
    }

 
      
    public static int getEtatNotificationLue() 
    {
        return etatNotificationLue;
    }public static int getEtatNotificationNonLue() 
    {
        return etatNotificationNonLue;
    }

  
    public static final int etatPlanifie = 17;

    public static int getEtatPlanifie() {
        return etatPlanifie;
    }

    public static int getEtatPayer() {
	 return etatPayer;
    }

   

    
    
}
