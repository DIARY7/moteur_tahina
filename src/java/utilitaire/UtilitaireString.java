/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

/**
 * Utilitaire suplementaire sur les chaines de caractère
 * 
 * @author BICI
 */
public class UtilitaireString {
    /**
     * Verifier si un mot est dans une phrase
     * @param phrase chaine de caractère à tester
     * @param indice 
     * @param mot mot à rechercher
     * @return vrai si dans le string sinon faux
     */
    public static boolean testMotDansUnePhrase(String phrase, int indice, String mot) {
        char[] ph = phrase.toCharArray();
        char[] mo = mot.toCharArray();
        for (int i = 0; i < mo.length; i++) {
            if ((i + indice > ph.length - 1) || (Character.toLowerCase(mo[i]) != Character.toLowerCase(ph[indice + i]))) {
                return false;
            }
        }
        return true;
    }
}
