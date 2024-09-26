/*
 * utils est un package regroupant les classes sur les différentes utilitaires 
 * pour jouer sur les valeurs(formattage, extraction de valeur,...)
 */
package utilitaire;

import bean.ClassMAPTable;
import bean.TypeObjet;
import bean.CGenUtil;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import utilitaire.UtilDB;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

/**
 *
 * Classe utilitaire: Service regroupant des fonctions courantes pour le
 * traitement des données dans un projet
 *
 * @author BICI
 *
 */
public class Utilitaire extends Parametre {

    public Utilitaire() {

    }

    /**
     * *
     * Liste de séparateurs de chaînes de caractères utilisables
     */
    final static char[] listSeparator = { ' ', '-', '_', ',', '/', ';' };

    /**
     * Méthode utilisée pour générer les champs retournés utilisés par un popup pour
     * une formulaire
     * multiple
     *
     * @param champRet    liste séparé par ;
     * @param numeroLigne numéro de ligne à concatener
     * @return
     */
    public static String genererChampReturnMultiple(String champRet, int numeroLigne) {
        String[] retTab = champRet.split(";");
        String retour = "";
        for (int i = 0; i < retTab.length; i++) {
            retour += retTab[i] + "_" + numeroLigne + ";";
        }
        return retour;
    }

    public static String heureCouranteHMSSansSeparateur() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11)))).concat("");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(12))).append("")));
        retour = String.valueOf(String.valueOf(
                (new StringBuffer(String.valueOf(String.valueOf(retour)))).append(completerInt(2, a.get(13)))));
        return retour;
    }

    /**
     * Méthode qui convertit un double (chaîne de caractères) en entier
     *
     * @param string : un double au format String
     * @return
     */
    public static int doubleToInt(String string) {
        Double d = new Double(string);
        int i = d.intValue();
        return i;
    }

    /**
     * Grouper un tableau de chaînes de caractères en ne tenant pas compte des
     * chaînes vides et null
     *
     * @param val tableau de chaînes de caractères
     * @return tableau de chaines de caractès sans vide et null
     * @throws Exception
     */
    public static String[] formerTableauGroupe(String[] val) throws Exception {
        String retour[] = null;
        Vector r = new Vector();
        for (int i = 0; i < val.length; i++) {
            if (val[i] != null && val[i].compareToIgnoreCase("") != 0 && val[i].compareToIgnoreCase("-") != 0) {
                r.add(val[i]);
            }
        }
        if (r.size() > 0) {
            retour = new String[r.size()];
            r.copyInto(retour);
        }
        return retour;
    }

    /**
     * Méthode pour crypter un mois (ex: 01 - Janvier pour A)
     *
     * @param rangMois
     * @return
     */
    public static String getRangMoisLettre(int rangMois) {
        String[] r = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z" };
        return r[rangMois - 1];
    }

    /**
     * Retrouve le séparateur dans une chaîne de caractères, parmi la liste de
     * séparateurs listSeparator (attribut de la classe Utilitaire)
     *
     * @param text
     * @return
     */
    public static String findSeparator(String text) {
        String ret = " ";
        for (int i = 0; i < text.length(); i++) {
            if (isSeparator(text.charAt(i))) {
                ret = text.charAt(i) + "";
            }
        }
        return ret;
    }

    /**
     * Définit si un caractère est un séparateur ou non
     *
     * @param caractere
     * @return
     */
    public static boolean isSeparator(char caractere) {
        for (int k = 0; k < listSeparator.length; k++) {
            if (caractere == listSeparator[k]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le rang d'une lettre dans l'alphabet, retourne 30 si le
     * caractère ne se situe pas dans le tableau de caractères
     *
     * @param caract
     * @return
     */
    public static int valeurCharEnChiffre(String caract) {
        String[] r = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z", " ", "-", "\"" };

        for (int i = 0; i < r.length; i++) {
            if (caract.compareToIgnoreCase(caract) == 0) {
                return i + 1;
            }
        }
        return 30;
    }

    /**
     * Méthode permettant d'obtenir l'année en cours
     *
     * @return
     */
    public static String getAnneeEnCours() {
        String daty = Utilitaire.dateDuJour();
        String an = Utilitaire.getAnnee(daty);
        return an;
    }

    /**
     * Convertit un entier de secondes en format:heure - minute - seconde
     * (HH:MM:SS)
     *
     * @param msecondes
     * @return
     */
    public static String secondesToTime(int msecondes) {
        int secondes = 0;
        int minutes = 0;
        int heures = 0;

        secondes = msecondes % 60;
        msecondes = msecondes / 60;
        minutes = msecondes % 60;
        msecondes = msecondes / 60;
        heures = msecondes;
        return heures + ":" + minutes + ":" + secondes;
    }

    /**
     * Transforme une chaîne de caractères HH:MM:SS en millisecondes
     *
     * @param time
     * @return
     */
    public static int timeToMillisecondes(String time) {

        int secondes = 0;
        int minutesMS = 0;
        int heuresMS = 0;

        int sec = 0;
        if (time != null || time.compareToIgnoreCase("") != 0) {
            String[] splt = time.split(":");
            if (splt.length > 2) {
                secondes = stringToInt(splt[2]);
                minutesMS = stringToInt(splt[1]) * 60;
                heuresMS = stringToInt(splt[0]) * 3600;
            } else {
                minutesMS = stringToInt(splt[1]) * 60;
                heuresMS = stringToInt(splt[0]) * 3600;
            }

            sec = secondes + minutesMS + heuresMS;
        }
        return sec;
    }

    /**
     * Ajoute les secondes à un Srting de format HH:MM:SS
     *
     * @param heuredebut
     * @param seconde
     * @return
     */
    public static int diffDeuxheures(String heuredebut, int seconde) {
        // une fonction qui calcule la difference de deux heures en secondes
        int result = 0;

        String[] debut = split(heuredebut, ":");

        int hmsd = Utilitaire.stringToInt(debut[0]) * 3600;
        int mmsd = Utilitaire.stringToInt(debut[1]) * 60;

        result = (hmsd + mmsd) + seconde;
        return result;
    }

    /**
     * Retourne la différence entre 2 heures
     *
     * @param heuredebut
     * @param heurefin
     * @return
     */
    public static int diffDeuxheures(String heuredebut, String heurefin) {
        // une fonction qui calcule la difference de deux heures en secondes
        int result = 0;

        String[] debut = split(heuredebut, ":");
        String[] fin = split(heurefin, ":");

        int hmsd = Utilitaire.stringToInt(debut[0]) * 3600;
        int mmsd = Utilitaire.stringToInt(debut[1]) * 60;

        int hmsf = Utilitaire.stringToInt(fin[0]) * 3600;
        int mmsf = Utilitaire.stringToInt(fin[1]) * 60;

        result = (hmsf + mmsf) - (hmsd + mmsd);
        return result;
    }

    /**
     * Fonction de déchiffrement
     *
     * @param caractere
     * @return
     */
    public static double dechiffrer(String caractere) {
        int var_long;
        String var_car_act = "";
        int var_val_act;
        int var_coeff;
        double var_total = 0;
        String temp = caractere.trim();
        var_long = temp.length();

        for (int i = 0; i < var_long; i++) {
            var_car_act = temp.substring(i, (i + 1));
            var_val_act = valeurCharEnChiffre(var_car_act);
            var_coeff = var_long - i;
            var_total += var_coeff * var_val_act;
        }

        return var_total * var_long;
    }

    /**
     * Convertit une classe de type java.sql.Date en java.util.Date
     *
     * @param sqlDate
     * @return
     */
    public static java.util.Date convertFromSQLDateToUtilDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static String getLastDayOfDate(String daty) {
        String ret = "";
        try {
            Date dt = stringDate(daty);
            ret = getLastDayOfDate(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Fonction permettant d'obtenir le trimestre d'une date donnée (ex:
     * 16/04/2021 est dans le trimestre 2 de l'année 2021)
     *
     * @param d
     * @return
     */
    public static int getTrimestre(java.sql.Date d) {
        int month = d.getMonth();
        int reste = (month + 1) % 3;
        if (reste != 0) {
            return (((month + 1) / 3) + 1);
        }
        if (reste == 0) {
            return ((month + 1) / 3);
        }
        return 0;
    }

    /**
     * Obtenir la dernière date d'un trimestre en String
     *
     * @param trimestre
     * @param annee
     * @return
     */
    public static String getDateFinAPartirTrimestre(int trimestre, int annee) {
        int mois = trimestre * 3;
        if (mois == 12) {
            return "31/" + mois + "/" + annee;
        }
        if (mois == 9) {
            return "30/" + mois + "/" + annee;
        }
        if (mois == 6) {
            return "30/" + mois + "/" + annee;
        }
        if (mois == 3) {
            return "31/" + mois + "/" + annee;
        }
        return "";
    }

    /**
     * Obtenir la date de fin d'un trimestre en Date
     *
     * @param d
     * @return
     */
    public static Date getLastDateInTrimestre(java.sql.Date d) {
        if (d.after(stringDate("01/01/" + getAnnee(d))) && d.before(stringDate("31/03/" + getAnnee(d)))) {
            d = stringDate("31/03/" + getAnnee(d));
        }
        if (d.after(stringDate("01/04/" + getAnnee(d))) && d.before(stringDate("30/06/" + getAnnee(d)))) {
            d = stringDate("30/06/" + getAnnee(d));
        }
        if (d.after(stringDate("01/07/" + getAnnee(d))) && d.before(stringDate("30/09/" + getAnnee(d)))) {
            d = stringDate("30/09/" + getAnnee(d));
        }
        if (d.after(stringDate("01/10/" + getAnnee(d))) && d.before(stringDate("31/12/" + getAnnee(d)))) {
            d = stringDate("31/12/" + getAnnee(d));
        }
        return d;
    }

    /**
     * Retourne le trimestre suivi de l'année de la date donnée (ex: 16/04/2021,
     * "-" résultera 02-2021)
     *
     * @param d
     * @param separateur
     * @return
     */
    public static String getTrimestreAnnee(java.sql.Date d, String separateur) {
        int trim = getTrimestre(d);
        int annee = getAnnee(d);
        return trim + separateur + annee;
    }

    /**
     * Retourne la date de fin d'un trimestre
     *
     * @param trimAnnee
     * @param separateur
     * @return
     */
    public static String getLastDateInTrimestre(String trimAnnee, String separateur) {
        String[] g = split(trimAnnee, separateur);
        return getDateFinAPartirTrimestre(stringToInt(g[0]), stringToInt(g[1]));
    }

    public static String moisEtAnneeToDate2(int month, int year, String minOuMax) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        switch (minOuMax) {
            case "min":
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
                break;
            case "max":
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                break;
        }
        java.util.Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        return DATE_FORMAT.format(date);
    }
    /**
     * Retourne le dernier jour d'un mois par rapport au jour et année donnés
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getLastJourInMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    /**
     * Retourne le dernier jour du mois (ex: 12/01/2021 va donner comme résultat
     * 31)
     *
     * @param d
     * @return
     */
    public static int getLastJourInMonth(java.sql.Date d) {
        int year = d.getYear() + 1900;
        int month = d.getMonth();
        int day = d.getDate();
        return getLastJourInMonth(year, month, day);
    }

    /**
     * Fonction ayant pour résultat le jour de la semaine correspondant à la
     * date donnée en entier (1 pour dimanche, 2 pour lundi, ...)
     *
     * @param daty
     * @return
     */
    public static int dayOfDate(Date daty) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(daty);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return day;
    }

    /**
     * Fonction ayant pour résultat le jour de la semaine correspondant à la
     * date donnée en entier (lundi, mardi, mercredi, ...)
     *
     * @param daty
     * @return
     */
    public static String getJourDate(Date daty) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(daty);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case 1:
                return "Dimanche";
            case 2:
                return "Lundi";
            case 3:
                return "Mardi";
            case 4:
                return "Mercredi";
            case 5:
                return "Jeudi";
            case 6:
                return "Vendredi";
            case 7:
                return "Samedi";
        }
        return null;
    }

    /**
     * Retourne le dernier jour du mois par rapport à une date au format
     * yyyy-MM-dd
     *
     * @param daty
     * @return
     */
    public static String getLastDayOfDate(Date daty) {
        String ret = "";
        try {
            java.util.Date dt = convertFromSQLDateToUtilDate(daty);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            java.util.Date lastDayOfMonth = calendar.getTime();
            java.text.DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            return sdf.format(lastDayOfMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Retourne le dernier jour du mois par rapport à une date au format
     * java.sql.Date
     *
     * @param daty
     * @return
     */
    public static java.sql.Date getLastDayOfDateSQL(Date daty) {
        java.sql.Date ret = null;
        try {
            java.util.Date dt = convertFromSQLDateToUtilDate(daty);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            java.util.Date lastDayOfMonth = calendar.getTime();
            ret = new java.sql.Date(lastDayOfMonth.getTime());
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Convertit un nombre en hexadécimal
     *
     * @param number
     * @return
     */
    public static String decimalToHexa(double number) {
        int puissance = 1;
        String petit_hexa = "";
        String val_prov;
        double nombre_dec1;

        while (1 < 2) {
            if (number / (Math.pow(16, puissance)) >= 16) {
                puissance++;
            } else {
                break;
            }
        }

        nombre_dec1 = number;
        while (1 < 2) {

            double temp = nombre_dec1 / (Math.pow(16, puissance));
            val_prov = String.valueOf(temp);
            nombre_dec1 = number % (Math.pow(16, puissance));

            if (val_prov.compareToIgnoreCase("10") == 0) {
                val_prov = "A";
            } else if (val_prov.compareToIgnoreCase("11") == 0) {
                val_prov = "B";
            } else if (val_prov.compareToIgnoreCase("12") == 0) {
                val_prov = "C";
            } else if (val_prov.compareToIgnoreCase("13") == 0) {
                val_prov = "D";
            } else if (val_prov.compareToIgnoreCase("14") == 0) {
                val_prov = "E";
            } else {
                val_prov = "F";
            }

            petit_hexa = petit_hexa + val_prov;
            puissance--;

            if (puissance < 0) {
                break;
            }
        }

        return petit_hexa;
    }

    /**
     * Définit si une chaîne de caractères est numérique ou non
     *
     * @param str
     * @return
     */
    public static boolean isStringNumeric(String str) {
        if (str == null || str.compareTo("") == 0) {
            return false;
        }
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();
        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound && c != ' ') {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;

    }

    /**
     * Change le format d'une date yyyy-mm-dd en dd/mm/yyyy
     *
     * @param daty
     * @return
     */
    public static String convertDatyFormtoRealDatyFormat(String daty) {
        if (daty == null || daty.compareToIgnoreCase("") == 0) {
            return "";
        }
        String[] tableau = new String[3];
        tableau = split(daty, "-");
        String result = tableau[2] + "/" + tableau[1] + "/" + tableau[0];
        return result;
    }

    /**
     * Réunit 2 tableaux de String
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String[] ajouterTableauString(String[] s1, String[] s2) {
        String retour[] = new String[s1.length + s2.length];
        int i = 0;
        for (i = 0; i < s1.length; i++) {
            retour[i] = s1[i];
        }
        for (int j = 0; j < s2.length; j++) {
            retour[i + j] = s2[j];
        }
        return retour;
    }

    /**
     * Réunit 2 tableaux de String
     *
     * @param t1
     * @param t2
     * @return
     */
    public static String[] concatener(String[] t1, String[] t2) {
        int taille = t1.length + t2.length;
        String retour[] = new String[taille];
        for (int i = 0; i < t1.length; i++) {
            retour[i] = t1[i];
        }
        for (int j = t1.length; j < taille; j++) {
            retour[j] = t2[j - t1.length];
        }
        return retour;
    }

    /**
     * Retourne le genre d'une personne; 1 pour la Femme et 0 pour Homme
     *
     * @param sexe
     * @return
     */
    public static String getGenre(String sexe) {
        if (sexe.compareTo("1") == 0) {
            return "Femme";
        }
        if (sexe.compareTo("0") == 0) {
            return "Homme";
        }
        return null;
    }

    /**
     * Convertit un String null (ou vide) en chaîne de caractères vide ("")
     *
     * @param nul
     * @return
     */
    public static String champNull(String nul) {
        if (nul == null) {
            return "";
        } else if (nul.compareToIgnoreCase("null") == 0) {
            return "";
        } else if (nul.compareToIgnoreCase("") == 0) {
            return "";
        } else {
            return nul;
        }
    }

    /**
     * Enlève les caractères qui ne sont pas des chiffres et des points dans une
     * chaîne donnée
     *
     * @param s
     * @return
     */
    public static String enleverNonChiffre(String s) {
        String listevalide = "-0123456789.";
        char[] lc = listevalide.toCharArray();
        String ch = "";
        int l = s.length();
        char c;
        for (int i = 0; i < l; i++) {
            c = s.charAt(i);
            for (int j = 0; j < lc.length; j++) {
                if (c == lc[j]) {
                    ch += c;
                }
            }
        }
        return ch;
    }

    /**
     * Enlève les espaces d'un String
     *
     * @param s
     * @return
     */
    public static String enleverEspace(String s) {
        String ch = "";
        int l = s.length();
        char c;
        for (int i = 0; i < l; i++) {
            c = s.charAt(i);
            if (c != ' ') {
                ch += c;
            }
        }
        return ch;
    }

    /**
     * Fonction qui remplace les '*' et ',' en '%' dans une chaîne de caractères
     *
     * @param s
     * @return
     */
    public static String replaceChar(String s) {
        s = s.replace('*', '%');
        s = s.replace(',', '%');
        return s;
    }

    /**
     *
     * @param text
     * @param toReplace
     * @param substitute
     * @return
     */
    public static String replaceChar(String text, String toReplace, String substitute) {
        String ret = text.replace(toReplace.charAt(0), substitute.charAt(0));
        return ret;
    }

    /**
     * Différence entre 2 heures au format HH:MM:SS
     *
     * @param heured
     * @param heuref
     * @return
     */
    public static String diffDeuxheures(String[] heured, String[] heuref) {
        String result = new String();
        int minutes;
        int hours;
        String h;
        String mn;
        if (stringToInt(heured[1]) > stringToInt(heuref[1])) {
            minutes = stringToInt(heured[1]) - stringToInt(heuref[1]);
            hours = stringToInt(heuref[0]) - stringToInt(heured[0]) - 1;
        } else {
            minutes = stringToInt(heuref[1]) - stringToInt(heured[1]);
            hours = stringToInt(heuref[0]) - stringToInt(heured[0]);
        }
        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = "" + hours;
        }
        if (minutes < 10) {
            mn = "0" + minutes;
        } else {
            mn = "" + minutes;
        }
        result = "" + h + ":" + mn + "";
        return result;
    }

    /**
     * Etablit la somme de 2 à plusieurs heures: les heures en String doivent
     * être au format HH:MMSS
     *
     * @param heure
     * @return
     */
    public static String sommeHeures(String[] heure) {
        String result = new String();
        int sommeh = 0;
        int sommemn = 0;
        String[] g;
        String hours = "";
        String mn = "";
        for (int i = 0; i < heure.length; i++) {
            g = split(heure[i], ":");
            sommeh = sommeh + stringToInt(g[0]);
            sommemn = sommemn + stringToInt(g[1]);
        }
        int x = sommemn / 60;
        if (x > 0) {
            sommeh = sommeh + x;
            sommemn = sommemn % 60;
        }
        if (sommeh < 10) {
            hours = "0" + sommeh;
        } else {
            hours = "" + sommeh;
        }
        if (sommemn < 10) {
            mn = "0" + sommemn;
        } else {
            mn = "" + sommemn;
        }
        result = "" + hours + ":" + mn + "";
        return result;
    }

    /**
     * Retourne le premier jour de l'année
     *
     * @param annee
     * @return
     */
    public static String getDebutAnnee(String annee) {
        return "01/01/" + annee;
    }

    /**
     * Split une chaîne de caractères par une autre
     *
     * @param mot
     * @param sep
     * @return
     */
    public static String[] split(String mot, String sep) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(mot, sep);
        Vector v = new Vector();
        while (tokenizer.hasMoreTokens()) {
            v.add(tokenizer.nextToken());
        }
        String retour[] = new String[v.size()];
        v.copyInto(retour);
        return retour;
    }

    /**
     * Retourne le dernier jour de l'année
     *
     * @param annee
     * @return
     */
    public static String getFinAnnee(String annee) {
        return "31/12/" + annee;
    }

    /**
     * Retourne dans un tableau le début et la fin de l'année en cours
     *
     * @return
     */
    public static String[] getDebutFinAnnee() {
        Parametre.getParametre();
        String[] retour = new String[2];
        retour[0] = getDebutAnnee(String.valueOf(getAneeEnCours()));
        retour[1] = getFinAnnee(String.valueOf(getAneeEnCours()));
        return retour;
    }

    /**
     * Extrait parmi un tableau de ClassMAPTable la valeur d'une certaine
     * colonne (ex: pour une classe "Personne", dont le numero de la colonne
     * "Sexe" est 2, et la valeur est "0"; on aura pour résultat la première
     * occurence de Personne de Sexe 0
     *
     * @param c
     * @param numCol
     * @param val
     * @return
     */
    public static ClassMAPTable extraire(ClassMAPTable c[], int numCol, String val) {
        try {
            for (int i = 0; i < c.length; i++) {
                String valeur = String.valueOf(c[i].getValField(c[i].getFieldList()[numCol]));
                if (valeur.compareToIgnoreCase(val) == 0) {
                    return c[i];
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Remplacer les valeur null par le remplaçant donné
     *
     * @param val
     * @param remplacant
     * @return
     */
    public static String[] remplacerNullParBlanc(String[] val, String remplacant) {
        for (int i = 0; i < val.length; i++) {
            if (val[i] == null) {
                val[i] = remplacant;
            }
        }
        return val;
    }

    /**
     * Extrait parmi un Vector de ClassMAPTable la valeur d'une certaine colonne
     * (ex: pour une classe "Personne", dont le numero de la colonne "Sexe" est
     * 2, et la valeur est "0"; on aura pour résultat la première occurence de
     * Personne de Sexe 0
     *
     * @param v
     * @param numCol
     * @param val
     * @return
     */
    public static ClassMAPTable extraire(Vector v, int numCol, String val) {
        try {
            for (int i = 0; i < v.size(); i++) {
                ClassMAPTable c = (ClassMAPTable) v.elementAt(i);
                String valeur = (String) c.getValField(c.getFieldList()[numCol]);
                if (valeur.compareToIgnoreCase(val) == 0) {
                    return c;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extrait parmi un Vector de ClassMAPTable la valeur d'une à plusieurs
     * colonne(s)
     *
     * @param v
     * @param numColVect
     * @param numCol
     * @param val
     * @return
     */
    public static ClassMAPTable extraireMultiple(Vector v, int numColVect, int[] numCol, String[] val) {
        try {
            for (int i = 0; i < v.size(); i++) {
                ClassMAPTable c = (ClassMAPTable) v.elementAt(i);
                int test = 1;
                String[] valeurT = (String[]) (c.getValField(c.getFieldList()[numColVect]));
                for (int j = 0; j < numCol.length; j++) {
                    String valeur = valeurT[j];
                    if (valeur.compareToIgnoreCase(val[j]) != 0) {
                        test = 0;
                        break;
                    }
                }
                if (test == 1) {
                    return c;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Vérifie si la valeur d'une colonne est présente dans le tableau d'objets
     * (pour un tableau de Personne, cette fonction retournera 1 si la colonne 2
     * a pour valeur 0; sinon le résultat est 0)
     *
     * @param c
     * @param numCol
     * @param val
     * @return
     */
    public static int estIlDedans(ClassMAPTable c[], int numCol, String val) {
        try {
            for (int i = 0; i < c.length; i++) {
                String valeur = (String) c[i].getValField(c[i].getFieldList()[numCol]);
                if (valeur.compareToIgnoreCase(val) == 0) {
                    return 1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Vérifie si test est dans le tableau de String
     *
     * @param test
     * @param c
     * @return
     */
    public static int estIlDedans(String test, String c[]) {
        try {
            if (c == null) {
                return -1;
            }
            for (int i = 0; i < c.length; i++) {
                if (c[i].compareToIgnoreCase(test) == 0) {
                    return i;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Le début de la chaîne de caractères sera en majuscule
     *
     * @param autre
     * @return
     */
    public static String convertDebutMajuscule(String autre) {
        char[] c = autre.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    /**
     * Retourne toutes les valeurs d'une colonne dans une table (BDD)
     *
     * @param nomTable
     * @param col
     * @return
     */
    public static String[] getvalCol(String nomTable, String col) {
        UtilDB util = new UtilDB();
        Connection c = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        String[] retour = null;
        try {
            try {
                c = util.GetConn();
                cs = c.prepareStatement("select distinct(" + col + ") from " + nomTable);
                rs = cs.executeQuery();
                Vector v = new Vector();
                while (rs.next()) {
                    v.add(rs.getString(1));
                }
                retour = new String[v.size()];
                v.copyInto(retour);
                return retour;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (cs != null) {
                    cs.close();
                }
                if (rs != null) {
                    rs.close();
                }
                util.close_connection();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Retourne la borne maximale d'un tableau d'objets
     *
     * @param page
     * @param list
     * @return
     */
    public static int[] getBornePage(int page, Object list[]) {
        int ret[] = new int[2];
        ret[0] = (page - 1) * Parametre.getNbParPage();
        if ((ret[0] + Parametre.getNbParPage()) - 1 < list.length) {
            ret[1] = ret[0] + Parametre.getNbParPage();
        } else {
            ret[1] = list.length;
        }
        return ret;
    }

    /**
     * Retourne la borne maximale d'un tableau d'objets
     *
     * @param page
     * @param list
     * @return
     */
    public static int[] getBornePage(String page, Object list[]) {
        return getBornePage(stringToInt(page), list);
    }

    /**
     * Retourne le nombre de page par rapport à une taille des objets
     *
     * @param tailleObjet
     * @return
     */
    public static int calculNbPage(double tailleObjet) {
        int ret = 0;
        Double d = new Double(tailleObjet);
        ret = d.intValue() / Parametre.getNbParPage();
        if (d.intValue() % Parametre.getNbParPage() > 0) {
            ret++;
        }
        return ret;
    }

    /**
     * Retourne le nombre de page par rapport à une taille des objets et le
     * nombre d'éléments à afficher par page
     *
     * @param tailleObjet
     * @param nbParPage
     * @return
     */
    public static int calculNbPage(double tailleObjet, int nbParPage) {
        int ret = 0;
        int nb = Parametre.getNbParPage();
        if (nbParPage > 0) {
            nb = nbParPage;
        }
        Double d = new Double(tailleObjet);
        ret = d.intValue() / nb;
        if (d.intValue() % nb > 0) {
            ret++;
        }
        return ret;
    }

    /**
     * Nombre initial de pagination
     *
     * @param i
     * @param pageSize
     * @return
     */
    public static int calculInitial(int i, int pageSize) {
        int initial = 0;
        if (i == 1) {
            initial = 0;
        } else {
            initial = pageSize * (i - 1);
        }
        return initial;
    }

    /**
     * Retourne le nombre de page par rapport à la taille du tableau d'objets
     * donné
     *
     * @param list
     * @return
     */
    public static int calculNbPage(Object list[]) {
        return calculNbPage(list.length);
    }

    /**
     * Effectue la somme des chaînes de caractères en double
     *
     * @param val
     * @return
     * @throws Exception
     */
    public static double calculSomme(String[] val) throws Exception {
        double retour = 0;
        for (int i = 0; i < val.length; i++) {
            retour = retour + Utilitaire.stringToDouble(val[i]);
        }
        return retour;
    }

    /**
     * Retourne la somme des doubles dans le tableau
     *
     * @param val
     * @return
     * @throws Exception
     */
    public static double calculSomme(double[] val) throws Exception {
        double retour = 0;
        for (int i = 0; i < val.length; i++) {
            retour = retour + (val[i]);
        }
        return retour;
    }

    /**
     * Convertit une chaîne de caractères en entier
     *
     * @param s
     * @return
     */
    public static int stringToInt(String s) {
        int j;
        try {
            Integer ger = new Integer(s);
            int i = ger.intValue();
            int k = i;
            return k;
        } catch (NumberFormatException ex) {
            j = 0;
        }
        return j;
    }

    /**
     * Remplace un String null par ""
     *
     * @param valNull
     * @return
     */
    public static String remplacerNull(String valNull) {
        if ((valNull == null) || valNull.compareToIgnoreCase("null") == 0) {
            return "";
        }
        return valNull;
    }

    /**
     * Remplace underscore par un tiret haut (-)
     *
     * @param mot
     * @return
     */
    public static String remplacerUnderscore(String mot) {
        String nouveau = new String(mot.toCharArray());
        nouveau.replace('_', '-');
        return nouveau;
    }

    /**
     * Remplace les 'mot1' par 'mot2' dans 'valeur'
     *
     * @param valeur
     * @param mot1
     * @param mot2
     * @return
     */
    public static String remplaceMot(String valeur, String mot1, String mot2) {
        StringBuffer result = new StringBuffer();
        int startIdx = 0;
        int idxOld = 0;
        while ((idxOld = valeur.indexOf(mot1, startIdx)) >= 0) {
            result.append(valeur.substring(startIdx, idxOld));
            result.append(mot2);
            startIdx = idxOld + mot1.length();
        }
        result.append(valeur.substring(startIdx));
        return result.toString();
    }

    /**
     * Retourne le rang d'un caractère dans un tableau de caractères
     *
     * @param liste
     * @param c
     * @return
     */
    public static int getRang(char[] liste, char c) {
        for (int i = 0; i < liste.length; i++) {
            if (Character.toLowerCase(liste[i]) == Character.toLowerCase(c)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Fonction de codgae de mot de passe
     *
     * @param entree
     * @return
     */
    public static String coderPwd(String entree) {
        char[] listeMot = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        char[] chiffre = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        char[] retr = new char[entree.length() + 1];
        retr[0] = listeMot[entree.length() % 5];
        char[] entr = entree.toCharArray();
        for (int i = 0; i < entr.length; i++) {
            int rL = getRang(listeMot, entr[i]);
            int rC = getRang(chiffre, entr[i]);
            if (rL > -1) {
                retr[i + 1] = listeMot[(listeMot.length - rL - i)];
            } else if (rC > -1) {
                retr[i + 1] = chiffre[(chiffre.length + rC - i)];
            } else {
                retr[i + 1] = entr[i];
            }
        }
        return new String(retr);
    }

    /**
     * Fonction de décodage ito, fa tsy mandeha
     *
     * @param entree
     * @return
     */
    public static String decode(String entree) {
        char[] listeMot = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        char[] chiffre = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        return null;
    }

    public static String getDebutmot(String mot) {
        String retour = "";
        char motChar[] = new char[mot.length()];
        motChar = mot.toCharArray();
        retour = retour.concat(String.valueOf(motChar[0]));
        int i = 0;
        do {
            if (i >= mot.length()) {
                break;
            }
            if (motChar[i] == ' ') {
                retour = retour.concat(String.valueOf(motChar[i + 1]));
                break;
            }
            i++;
        } while (true);
        return retour.toUpperCase();
    }

    public static String getDebutmot(String mot, int nombre) {
        if (mot == null) {
            return "";
        }

        if (nombre >= mot.length()) {
            return mot.toUpperCase();
        }

        String retour = "";
        if (nombre <= 0) {
            return retour;
        }

        char motChar[] = new char[mot.length()];
        motChar = mot.toCharArray();
        // retour = retour.concat(String.valueOf(motChar[0]));

        for (int n = 0; n < nombre; n++) {
            if (motChar[n] == ' ') {
                retour = retour;
            } else {
                retour = retour.concat(String.valueOf(motChar[n]));
            }
        }
        return retour.toUpperCase();
    }

    /**
     * Prend les 3 premieres lettres d'un String si c'est compose d'un seul mot,
     * sinon prend les premieres lettres de chaque mot
     *
     * @param mot
     * @return
     */
    public static String getDebutMots(String mot) {
        String retour = "";
        if (mot.compareTo("-") == 0) {
            return "NON";
        }
        int multiple = 0;
        int indice = 3;
        if (mot.length() < 3) {
            indice = 2;
        }
        char[] motChar = new char[mot.length()];
        motChar = mot.toCharArray();
        // retour=retour.concat(String.valueOf(motChar[0]));
        for (int i = 0; i < mot.length(); i++) {
            if (motChar[i] == ' ') {
                multiple = 1;
                break;
            }
        }
        if (multiple == 1) {
            retour = getDebutmot(mot);
        } else {
            for (int i = 0; i < indice; i++) {
                retour = retour.concat(String.valueOf(motChar[i]));
            }
        }
        return retour.toUpperCase();
    }

    /**
     * Retourne un prix de vente % à la marge de vente donnée
     *
     * @param pu
     * @param marge
     * @return
     */
    public static double getPvente(int pu, int marge) {
        return (double) pu * ((double) 1 + (double) marge / (double) 100);
    }

    /**
     * Convertit une chaîne de caractères en float
     *
     * @param s
     * @return
     */
    public static float stringToFloat(String s) {
        float f1;
        try {
            Float ger = new Float(s);
            float f = ger.floatValue();
            return f;
        } catch (NumberFormatException ex) {
            f1 = 0.0F;
        }
        return f1;
    }

    public static String[] getBorneDatyMoisAnnee(String mois, String an) {
        String retour[] = new String[2];
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        String moisF=completerInt(2,mois);
        retour[0] = "01/" + moisF + "/" + an;
        Date daty1 = string_date("dd/MM/yyyy", retour[0]);
        eD.setTime(daty1);
        eD2.setTime(daty1);
        eD2.add(5, 26);
        do {
            eD2.add(5, 1);
        } while (eD.get(2) == eD2.get(2));
        eD2.add(5, -1);
        retour[1] = String.valueOf(String.valueOf(completerInt(2, eD2.get(5)))).concat("/");
        retour[1] = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour[1]))))
                .append(completerInt(2, eD2.get(2) + 1)).append("/")));
        retour[1] = String.valueOf(retour[1]) + String.valueOf(completerInt(4, eD2.get(1)));
        return retour;
    }

    /**
     * Retourne l'année en cours
     *
     * @return
     */
    public static int getAneeEnCours() {
        Calendar a = Calendar.getInstance();
        return a.get(1);
    }

    /**
     * Compte le nombre de carctèes dans une chaîne
     *
     * @param lettre
     * @param c
     * @return
     */
    public static int compterCar(String lettre, char c) {
        char[] mot = lettre.toCharArray();
        int nb = 0;
        for (int i = 0; i < mot.length; i++) {
            if (mot[i] == c) {
                nb++;
            }
        }
        return nb;
    }

    /**
     * Spliter une chaîne par un caractère
     *
     * @param lettre
     * @param sep
     * @return
     */
    public static String[] split(String lettre, char sep) {
        Vector v = new Vector();
        char[] mot = lettre.toCharArray();
        char part[] = new char[400];
        int indicePart = 0;
        for (int i = 0; i < mot.length; i++) {
            if (mot[i] == sep) {
                indicePart = 0;
                v.add(String.valueOf(part).trim());
                part = new char[400];
            } else {
                part[indicePart] = mot[i];
                indicePart++;
            }
            if (i == mot.length - 1) {
                v.add(String.valueOf(part).trim());
            }
        }
        String[] retour = new String[v.size()];
        v.copyInto(retour);
        return retour;
    }

    /**
     * Vérifie si un caractère est présent dans le tableau
     *
     * @param liste
     * @param car
     * @return
     */
    public static boolean estIlDedans(char[] liste, char car) {
        for (int i = 0; i < liste.length; i++) {
            if (liste[i] == car) {
                return true;
            }
        }
        return false;
    }

    /**
     * Spliter
     *
     * @param lettre
     * @return
     */
    public static String[] splitMultiple(String lettre) {
        return (split(lettre, listSeparator));
    }

    public static String[] split(String lettre, char[] sep) {
        Vector v = new Vector();
        char[] mot = lettre.toCharArray();
        char part[] = new char[100];
        int indicePart = 0;
        for (int i = 0; i < mot.length; i++) {
            if (estIlDedans(sep, mot[i])) {
                indicePart = 0;
                v.add(String.valueOf(part).trim());
                part = new char[100];
            } else {
                part[indicePart] = mot[i];
                indicePart++;
            }
            if (i == mot.length - 1) {
                v.add(String.valueOf(part).trim());
            }
        }
        String[] retour = new String[v.size()];
        v.copyInto(retour);
        return retour;
    }

    /**
     * Retourne l'année d'une chaîne au format DD/MM/YYYY
     *
     * @param daty
     * @return
     */
    public static String getAnnee(String daty) {
        // daty.
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return String.valueOf(eD.get(1));
        return split(daty, "/")[2];
    }

    public static String getAnnee(String daty, String separateur) {
        // daty.
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return String.valueOf(eD.get(1));
        return split(daty, separateur)[0];
    }

    public static int getAnnee(Date daty) {
        GregorianCalendar eD = new GregorianCalendar();
        eD.setTime(daty);
        return eD.get(1);
    }

    public static int getMois(Date daty) {
        GregorianCalendar eD = new GregorianCalendar();
        eD.setTime(daty);
        return eD.get(2) + 1;
    }

    public static String getMois(String daty) {
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return completerInt(2, eD.get(2) + 1);
        return completerInt(2, split(daty, "/")[1]);
    }

    public static String getJour(String daty) {
        // GregorianCalendar eD = new GregorianCalendar();
        // eD.setTime(string_date("dd/MM/yyyy", daty));
        // return completerInt(2, eD.get(5));
        return completerInt(2, split(daty, "/")[0]);
    }

    public static int getMoisEnCours() {
        Calendar a = Calendar.getInstance();
        return a.get(2);
    }

    public static int getMoisEnCoursReel() {
        Calendar a = Calendar.getInstance();
        return a.get(2) + 1;
    }

    /**
     * Fonction qui compare 2 dates, retourne 1 si supe{@literal >}inf; sinon -1 et
     * 0 si
     * égal
     *
     * @param supe
     * @param infe
     * @return
     */
    public static int compareDaty(Date supe, Date infe) {
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        Date sup = string_date("dd/MM/yyyy", formatterDaty(supe));
        Date inf = string_date("dd/MM/yyyy", formatterDaty(infe));
        eD.setTime(sup);
        eD2.setTime(inf);
        if (eD.getTime().getTime() > eD2.getTime().getTime()) {
            return 1;
        }
        return eD.getTime().getTime() >= eD2.getTime().getTime() ? 0 : -1;
    }

    /**
     * Retourne la différence en jours entre 2 dates
     *
     * @param dMaxe
     * @param dMine
     * @return
     */
    public static int diffJourDaty(Date dMaxe, Date dMine) {
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        Date dMax = string_date("dd/MM/yyyy", formatterDaty(dMaxe));
        Date dMin = string_date("dd/MM/yyyy", formatterDaty(dMine));
        eD.setTime(dMax);
        eD2.setTime(dMin);
        if (dMaxe.equals(dMine)) {
            return 0;
        }
        double resultat = eD.getTime().getTime() - eD2.getTime().getTime();
        BigDecimal result = new BigDecimal(String.valueOf(eD.getTime().getTime() - eD2.getTime().getTime()));
        BigDecimal retour = result.divide(new BigDecimal(String.valueOf(0x5265c00)), 4);
        return 1 + retour.intValue();
    }

    public static int diffJourDaty2(Date dMaxe, Date dMine) {
        GregorianCalendar eD = new GregorianCalendar();
        GregorianCalendar eD2 = new GregorianCalendar();
        Date dMax = string_date("dd/MM/yyyy", formatterDaty(dMaxe));
        Date dMin = string_date("dd/MM/yyyy", formatterDaty(dMine));
        eD.setTime(dMax);
        eD2.setTime(dMin);
        if (dMaxe.equals(dMine)) {
            return 1;
        }
        double resultat = eD.getTime().getTime() - eD2.getTime().getTime();
        BigDecimal result = new BigDecimal(String.valueOf(eD.getTime().getTime() - eD2.getTime().getTime()));
        BigDecimal retour = result.divide(new BigDecimal(String.valueOf(0x5265c00)), 4);
        return 1 + retour.intValue();
    }

    /**
     * Différence en mois entre 2 dates
     *
     * @param dMaxe
     * @param dMine
     * @return
     */
    public static int diffMoisDaty(Date dMaxe, Date dMine) {
        int result = 0, diffAnnee = 0, yMax = 0, yMin = 0, mMax = 0, mMin = 0;
        GregorianCalendar calMax, calMin;
        if (dMaxe.getTime() < dMine.getTime()) {
            Date temp = dMaxe;
            dMaxe = dMine;
            dMine = temp;
        }
        calMax = new GregorianCalendar();
        calMin = new GregorianCalendar();
        calMin.setTime(dMine);
        calMax.setTime(dMaxe);
        mMin = calMin.get(GregorianCalendar.MONTH);
        mMax = calMax.get(GregorianCalendar.MONTH);
        yMin = calMin.get(GregorianCalendar.YEAR);
        yMax = calMax.get(GregorianCalendar.YEAR);
        diffAnnee = yMax - yMin;
        if (mMax < mMin) {
            diffAnnee--;
            result = 12 - (mMin - mMax);
        } else {
            result = mMax - mMin;
        }
        result += diffAnnee * 12;
        return result;
    }

    public static int diffJourDaty(String dMax, String dMin) {
        return diffJourDaty(string_date("dd/MM/yyyy", dMax), string_date("dd/MM/yyyy", dMin));
    }

    public static int diffMoisDaty(String dMax, String dMin) {
        return diffMoisDaty(string_date("dd/MM/yyyy", dMax), string_date("dd/MM/yyyy", dMin));
    }

    /**
     * Remplace les virgules par un point
     *
     * @param s
     * @return
     */
    public static String replaceVirgule(String s) {

        // s = s.replace('\'', '\''');
        s = s.replace(',', '.');

        return s;
    }

    /**
     * Supprime les espaces
     *
     * @param s
     * @return
     */
    public static String supprimerEspace(String s) {

        // s = s.replace('\'', '\''');
        s = s.trim();

        return s;
    }

    public static String[] splitPeriode(String periode) {
        String[] ret = new String[2];
        ret[0] = periode.substring(0, 4);
        ret[1] = periode.substring(4);
        return ret;
    }

    /**
     * Enlever les espaces pour les montants insérés dans les bases de données
     *
     * @param montantBase
     * @return
     */
    public static String enleverEspaceDoubleBase(String montantBase) {
        String montant = "";
        for (int i = 0; i < montantBase.length(); ++i) {
            char c = montantBase.charAt(i);
            int j = (int) c;
            if (j != 160) {
                montant += c;
            }
        }
        return montant;
    }

    /**
     * Convertit une chaîne en double
     *
     * @param s
     * @return
     */
    public static double stringToDouble(String s) {
        double d1;
        try {
            String ns = replaceVirgule(s);

            String nso = enleverNonChiffre(ns);
            Double ger = new Double(nso);
            double d = ger.doubleValue();
            return d;
        } catch (NumberFormatException ex) {
            d1 = 0.0D;
        }
        return d1;
    }

    /**
     * Convertit une chaîne en long
     *
     * @param s
     * @return
     */
    public static long stringToLong(String s) {
        try {
            Long ger = new Long(s);
            long l = ger.longValue();
            return l;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        long l1 = 0L;
        return l1;
    }

    /**
     * Retourne les unités et dizaines
     *
     * @param nb
     * @return
     */
    public static int[] findUniteDizaine(int nb) {
        try {
            int[] ret = new int[2];
            ret[0] = nb % 10;
            ret[1] = (nb - ret[0]);
            return ret;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Formater un nombre
     *
     * @param montant
     * @return
     */
    public static String formaterAr(String montant) {
        if (montant.contains("E")) {
            return formaterAr(Double.valueOf(montant).doubleValue());
        }
        return formaterAr(stringToDouble(montant));
    }

    /**
     * Supprime l'exponentiel dans un double en String
     *
     * @param val
     * @return
     */
    public static String doubleWithoutExponential(double val) {
        String vals = String.format("%.2f", val);
        String[] temp = vals.split(",");
        if (temp.length > 1 && temp[1].compareToIgnoreCase("00") == 0) {
            vals = temp[0];
        } else if (temp.length > 1 && temp[1].endsWith("0")) {
            vals = temp[0] + "," + temp[1].substring(0, 1);
        }
        return Utilitaire.replaceChar(vals, ",", ".");
    }

    /**
     * formatter un double pour avoir un nombre sans exponentielle
     * 
     * @param montant valeur double
     * @return chaine de charactère representant le nombre sans exponentiel
     */
    public static String enleverExponentielleDouble(double montant) {
        String ret = "0";
        ret = new DecimalFormat("#").format(montant);
        return ret;
    }

    /**
     * Formater un double
     *
     * @param montant
     * @return
     */
    public static String formaterAr(double montant) {
        try {
            if (montant == 0) {
                return "0";
            }
            NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
            // nf = new DecimalFormat("### ###,##");
            // nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String s = nf.format(montant);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cnaps: formater sans virgule
     *
     * @param montant
     * @return
     */
    public static String formaterSansVirgule(double montant) {
        try {
            if (montant == 0) {
                return "0";
            }
            NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
            // nf = new DecimalFormat("### ###,##");
            // nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(0);
            String s = nf.format(montant);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s1 = null;
        return s1;
    }

    /**
     * Formater un long
     *
     * @param montant
     * @return
     */
    public static String formaterAr(long montant) {
        return formaterAr(String.valueOf(montant));
    }

    /**
     * Formater une date en local
     *
     * @param daty
     * @return
     */
    public static String formatterDaty(String daty) {
        if ((daty == null) || (daty.compareToIgnoreCase("null") == 0) || (daty.compareToIgnoreCase("") == 0)) {
            return "";
        }
        String valiny = (daty.substring(8, 10) + "/" + (daty.substring(5, 7)) + "/" + (daty.substring(0, 4)));
        return valiny;
    }

    /**
     * Tsy tokony ho ato
     *
     * @return
     * @throws Exception
     */
    public static Date getDateRentreSemestre() throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        Date max = null;
        try {
            c = util.GetConn();
            String param = "Select MAX(RENTRE) FROM RENTRESEMESTRE";
            sta = c.createStatement();
            rs = sta.executeQuery(param);
            rs.next();
            max = rs.getDate(1);
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
        return max;
    }

    /**
     * Retourne en string la date du lendemain
     *
     * @return
     */
    public static String getTomorowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime());
    }

    /**
     * Formatte la date en dd/MM/yyyy
     *
     * @param date
     * @return
     */
    public static String format(java.util.Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy");
        String dateFormatted = fmt.format(date);

        return dateFormatted;
    }

    /**
     * Arrondis un double au 'apr' centième près
     *
     * @param a
     * @param apr
     * @return
     */
    public static double arrondir(double a, int apr) {
        double d;
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setMaximumFractionDigits(apr);
            Number retour = nf.parse(nf.format(a));
            double d1 = retour.doubleValue();
            return d1;
        } catch (Exception e) {
            d = 1.0D;
        }
        return d;
    }

    public static String formatterDaty(Date daty) {
        String retour = null;
        return formatterDaty(String.valueOf(daty));
    }

    /**
     * Formate en java.sql.Date en Strinf
     *
     * @param daty
     * @return
     */
    public static String formatterDatySql(java.sql.Date daty) {
        String retour = null;
        return formatterDaty(String.valueOf(daty));
    }

    /**
     * Ajout de 'nbDay' jours à la date donnée
     *
     * @param aDate
     * @param nbDay
     * @return
     */
    public static Date ajoutJourDateOuvrable(Date aDate, int nbDay) {
        try {
            Date date = string_date("dd/MM/yyyy", ajoutJourDateStringOuvrable(aDate, nbDay));
            return date;
        } catch (Exception e) {
            System.out.println("Error string_date :".concat(String.valueOf(String.valueOf(e.getMessage()))));
            e.printStackTrace();
        }
        Date date1 = null;
        return date1;
    }

    public static String ajoutJourDateStringOuvrable(Date aDatee, int nbDay) {
        try {
            GregorianCalendar eD = new GregorianCalendar();
            Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
            eD.setTime(aDate);
            int offset = 1;
            int offsetSunday = 1;
            int offsetSaturday = 2;
            if (nbDay < 0) {
                offset = -1;
                offsetSunday = -2;
                offsetSaturday = -1;
            }
            for (int i = 1; i <= Math.abs(nbDay); i++) {
                eD.add(5, offset);
                if (eD.get(7) == 7) {
                    eD.add(5, offsetSaturday);
                    continue;
                }
                if (eD.get(7) == 1) {
                    eD.add(5, offsetSunday);
                }
            }

            String retour = null;
            retour = String.valueOf(String.valueOf(completerInt(2, eD.get(5)))).concat("/");
            retour = String.valueOf(retour) + String.valueOf(completerInt(2, String.valueOf(
                    String.valueOf((new StringBuffer(String.valueOf(String.valueOf(eD.get(2) + 1)))).append("/")))));
            retour = String.valueOf(retour) + String.valueOf(completerInt(4, eD.get(1)));
            String s1 = retour;
            return s1;
        } catch (Exception e) {
            System.out.println("Error string_date :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String s = null;
        return s;
    }

    /**
     * Ajouter des mois à la date donnée
     *
     * @param aDatee
     * @param nbMois
     * @return
     */
    public static String ajoutMoisDateString(Date aDatee, int nbMois) {
        try {
            GregorianCalendar eD = new GregorianCalendar();
            GregorianCalendar eD2 = new GregorianCalendar();
            Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
            eD.setTime(aDate);
            int offset = 1;
            int offsetSunday = 1;
            int offsetSaturday = 2;
            if (nbMois < 0) {
                offset = -1;
                offsetSunday = -2;
                offsetSaturday = -1;
            }
            for (int i = 1; i <= Math.abs(nbMois); i++) {
                eD.add(2, offset);
            }

            eD2.setTime(eD.getTime());
            if (eD.get(2) == eD2.get(2) && testFinDuMois(aDate)) {
                do {
                    eD2.add(5, 1);
                } while (eD.get(2) == eD2.get(2));
                eD2.add(5, -1);
            }
            String retour = null;
            retour = String.valueOf(String.valueOf(completerInt(2, eD2.get(5)))).concat("/");
            retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                    .append(completerInt(2, eD2.get(2) + 1)).append("/")));
            retour = String.valueOf(retour) + String.valueOf(completerInt(4, eD2.get(1)));
            String s1 = retour;
            return s1;
        } catch (Exception e) {
            System.out.println("Error string_date :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String s = null;
        return s;
    }

    /**
     * Vérifie si la date donnée est la fin du mois
     *
     * @param aDatee
     * @return
     */
    public static boolean testFinDuMois(Date aDatee) {
        GregorianCalendar eD = new GregorianCalendar();
        Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
        eD.setTime(aDate);
        GregorianCalendar eD2 = new GregorianCalendar();
        eD2.setTime(eD.getTime());
        eD2.add(5, 1);
        return eD.get(2) != eD2.get(2);
    }

    /**
     * Retourne le maximum parmi le tableau de double
     *
     * @param liste
     * @return
     */
    public static double getMaxListeDouble(double[] liste) {
        double max = liste[0];
        for (int i = 1; i < liste.length; i++) {
            if (liste[i] >= max) {
                max = liste[i];
            }
        }
        return max;
    }

    /**
     * Ajoute le nombre de jours à la date donnée
     *
     * @param aDatee
     * @param nbDay
     * @return
     */
    public static String ajoutJourDateString(Date aDatee, int nbDay) {
        try {
            GregorianCalendar eD = new GregorianCalendar();
            Date aDate = string_date("dd/MM/yyyy", formatterDaty(aDatee));
            eD.setTime(aDate);
            int offset = 1;
            int offsetSunday = 1;
            int offsetSaturday = 2;
            if (nbDay < 0) {
                offset = -1;
                offsetSunday = -2;
                offsetSaturday = -1;
            }
            for (int i = 1; i <= Math.abs(nbDay); i++) {
                eD.add(5, offset);
            }

            String retour = null;
            retour = String.valueOf(String.valueOf(completerInt(2, eD.get(5)))).concat("/");
            retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                    .append(completerInt(2, eD.get(2) + 1)).append("/")));
            retour = String.valueOf(retour) + String.valueOf(completerInt(4, eD.get(1)));
            String s1 = retour;
            return s1;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDateString :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String s = null;
        return s;
    }

    /**
     * Soustrait 'nbDay' à la date donnée
     *
     * @param nbDay
     * @return
     */
    public static String soustraireJourDate(int nbDay) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -nbDay);
            return Utilitaire.datetostring(cal.getTime());
        } catch (Exception e) {
            System.out.println("Error ajoutJourDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        String date1 = "";
        return date1;
    }

    public static Date ajoutJourDate(Date aDate, int nbDay) {
        try {
            Date date = string_date("dd/MM/yyyy", ajoutJourDateString(aDate, nbDay));
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    public static Date ajoutMoisDate(Date aDate, int nbMois) {
        try {
            Date date = string_date("dd/MM/yyyy", ajoutMoisDateString(aDate, nbMois));
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutMoisDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    public static Date ajoutJourDate(String daty, int jour) {
        try {
            Date date = ajoutJourDate(string_date("dd/MM/yyyy", daty), jour);
            return date;
        } catch (Exception e) {
            System.out.println("Error ajoutJourDate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date1 = null;
        return date1;
    }

    public static Date string_date(String patterne, String daty) {
        try {
            if (daty == null || daty.compareTo("") == 0) {
                return null;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(patterne);
            formatter.applyPattern(patterne);
            formatter.setTimeZone(TimeZone.getTimeZone("EUROPE"));
            String annee = getAnnee(daty);
            int anneecours = getAneeEnCours();
            int siecl = anneecours / 100;
            if (annee.toCharArray().length == 2) {
                annee = String.valueOf(siecl) + annee;
            }
            daty = getJour(daty) + "/" + getMois(daty) + "/" + annee;
            Date hiredate = new Date(formatter.parse(daty).getTime());
            Date date1 = hiredate;
            return date1;
        } catch (Exception e) {
            System.out.println("Error string_date wawawawa :" + e.getMessage());
        }
        Date date = dateDuJourSql();
        return date;
    }

    public static java.util.Date stringToDate(String pattern, String daty) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            java.util.Date hiredate = formatter.parse(daty);
            java.util.Date date1 = hiredate;
            return date1;
        } catch (Exception e) {
            System.out.println("Error stringTodate :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        java.util.Date date = null;
        return date;
    }

    /**
     * Retourne un nombre aléatoire entre 0 et 'max'
     *
     * @param max
     * @return
     */
    public int randomizer(int max) {
        int retour;
        for (retour = 0; retour <= 0; retour = r.nextInt(max))
            ;
        return retour;
    }

    public String randomizer_daty(int annee) {
        int mois = r.nextInt(13);
        int jour = r.nextInt(31);
        String retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(jour))))
                .append("/").append(mois).append("/").append(annee)));
        return retour;
    }

    /**
     * Retourne le nb de tuples dans une table
     *
     * @param nomTable
     * @return
     */
    public static int getNbTuple(String nomTable) {
        Connection c = null;
        UtilDB util = new UtilDB();
        try {
            try {
                c = util.GetConn();
                String param = "select count(*) from ".concat(String.valueOf(String.valueOf(nomTable)));
                Statement sta = c.createStatement();
                ResultSet rs = sta.executeQuery(param);
                rs.next();
                int i = rs.getInt(1);
                return i;
            } catch (SQLException e) {
                System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            util.close_connection();
        }
    }

    /**
     * ??
     *
     * @param nomTable
     * @param critere
     * @param apwhere
     * @return
     */
    public static int getNbEliminatoire(String nomTable, String critere, String apwhere) {
        Connection c = null;
        UtilDB util = new UtilDB();
        try {
            try {
                c = util.GetConn();
                String param = String.valueOf(String.valueOf((new StringBuffer("select count(")).append(critere)
                        .append(") from ").append(nomTable).append(" where ").append(apwhere)));
                Statement sta = c.createStatement();
                ResultSet rs = sta.executeQuery(param);
                rs.next();
                int i = rs.getInt(1);
                return i;
            } catch (SQLException e) {
                System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            util.close_connection();
        }
    }

    /**
     * ??
     *
     * @param daty
     * @return
     */
    public static int getMaxColonneFactFin(String daty) {
        UtilDB util = new UtilDB();
        Connection c = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            try {
                String an = getAnnee(daty);
                c = null;
                c = util.GetConn();
                cs = c.prepareStatement(
                        String.valueOf(String.valueOf((new StringBuffer("select * from  seqFact where daty<='31/12/"))
                                .append(an).append("' and daty>='01/01/").append(an).append("'"))));
                rs = cs.executeQuery();
                int i = 0;
                if (rs.next()) {
                    i++;
                }
                if (i == 0) {
                    int k = 0;
                    return k;
                }
                int l = (new Integer(rs.getString(1))).intValue();
                return l;
            } catch (SQLException e) {
                System.out.println("getMaxSeq : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (cs != null) {
                    cs.close();
                }
                if (rs != null) {
                    rs.close();
                }
                util.close_connection();
            } catch (SQLException e) {
                System.out.println(
                        "Erreur Fermeture SQL RechercheType ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }

    /**
     * Retourne la valeur courante de la séquence
     *
     * @param nomProcedure
     * @param c
     * @return
     * @throws Exception
     */
    public static int getMaxSeq(String nomProcedure, Connection c) throws Exception {
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = c.prepareCall(String
                    .valueOf(String.valueOf((new StringBuffer("select ")).append(nomProcedure).append(" from dual"))));
            rs = cs.executeQuery();
            rs.next();
            int i = rs.getInt(1);
            return i;
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.close();
            }
        }
    }

    /**
     * Retourne la valeur courante de la séquence
     *
     * @param nomProcedure
     * @return
     */
    public static int getMaxSeq(String nomProcedure) {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            c = util.GetConn();
            return getMaxSeq(nomProcedure, c);
        } catch (Exception eu) {
            eu.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Retourne la valeur maximale d'une colonne d'une table
     *
     * @param nomTable
     * @param nomColonne
     * @param where
     * @return
     * @throws Exception
     */
    public static int getMaxNum(String nomTable, String nomColonne, String where) throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        try {
            try {
                c = util.GetConn();

                String param = "select max(" + nomColonne + ") from " + nomTable + " where " + where;
                sta = c.createStatement();
                rs = sta.executeQuery(param);
                int i = 0;
                if (rs.next()) {
                    i = rs.getInt(1);
                }
                return i;
            } catch (SQLException e) {
                System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
    }

    public static int getMaxNum(String nomTable, String nomColonne) throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        try {
            try {
                c = util.GetConn();
                String param = String.valueOf(String.valueOf(
                        (new StringBuffer("select max(")).append(nomColonne).append(") from ").append(nomTable)));
                sta = c.createStatement();
                rs = sta.executeQuery(param);
                rs.next();
                int i = 1 + rs.getInt(1);
                return i;
            } catch (SQLException e) {
                System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
    }

    /**
     * idem maxNum
     *
     * @param nomTable
     * @param nomColonne
     * @param nomCritere
     * @param attributCritere
     * @return
     * @throws Exception
     */
    public static String getMaxColonne(String nomTable, String nomColonne, String nomCritere, String attributCritere)
            throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        String max = "";
        try {
            c = util.GetConn();
            String param = String.valueOf(String
                    .valueOf((new StringBuffer("select max(")).append(nomColonne).append(") from ").append(nomTable)
                            .append(" where ").append(nomCritere).append("='").append(attributCritere).append("'")));
            sta = c.createStatement();
            rs = sta.executeQuery(param);
            rs.next();
            max = rs.getString(1);
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
        return max;
    }

    /**
     * idem maxNum, mais avec plusieurs critères
     *
     * @param nomTable
     * @param nomColonne
     * @param whereCritereContact
     * @return
     * @throws Exception
     */
    public static String getMaxColonneMultiCritere(String nomTable, String nomColonne, String whereCritereContact)
            throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        String max = "";
        try {
            c = util.GetConn();
            String param = String.valueOf(String.valueOf((new StringBuffer("select max(")).append(nomColonne)
                    .append(") from ").append(nomTable).append(whereCritereContact)));
            sta = c.createStatement();
            rs = sta.executeQuery(param);
            rs.next();
            max = rs.getString(1);
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
        return max;
    }

    /**
     * Mettre à jour une colonne
     *
     * @param nomTable
     * @param nomColonne
     * @param critere
     * @param val
     * @param valCritere
     * @return
     * @throws Exception
     */
    public static String updateColonne(String nomTable, String nomColonne, String critere, String val,
            String valCritere) throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        int rs = 0;
        String max = "";
        try {
            c = util.GetConn();
            c.setAutoCommit(false);
            String param = String.valueOf(String.valueOf((new StringBuffer("update ")).append(nomTable).append(" set ")
                    .append(nomColonne).append("='").append(val).append("' where ").append(critere).append("='")
                    .append(valCritere).append("'")));
            sta = c.createStatement();
            rs = sta.executeUpdate(param);
            c.commit();
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (c != null) {
                c.close();
            }
        }
        return max;
    }

    /**
     * Etablit la somme des tuples d'une colonne
     *
     * @param nomTable
     * @param nomColonne
     * @param whereCritereContact
     * @return
     * @throws Exception
     */
    public static int getSommeColonneMultiCritere(String nomTable, String nomColonne, String whereCritereContact)
            throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        int sum = 0;
        try {
            c = util.GetConn();
            String param = String.valueOf(String.valueOf((new StringBuffer("select sum(")).append(nomColonne)
                    .append(") from ").append(nomTable).append(" where ").append(whereCritereContact)));
            sta = c.createStatement();
            rs = sta.executeQuery(param);
            rs.next();
            sum = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
        return sum;
    }

    /**
     * Retourne le nombre de jour d'un mois d'une année
     *
     * @param mois
     * @param an
     * @return
     */
    public static int getNombreJourMois(String mois, String an) {
        try {
            String datyInf = getBorneDatyMoisAnnee(mois, an)[0];
            String datySup = getBorneDatyMoisAnnee(mois, an)[1];
            int j = diffJourDaty(datySup, datyInf);
            return j;
        } catch (Exception e) {
            System.out.println("getNombreJourMois : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        int i = 0;
        return i;
    }

    /**
     * Retourne le nombre de jours dans un mois d'une année, par rapport à la
     * date donnée
     *
     * @param daty
     * @return
     */
    public static int getNombreJourMois(String daty) {
        try {
            String mois = getMois(daty);
            String an = getAnnee(daty);
            int j = getNombreJourMois(mois, an);
            return j;
        } catch (Exception e) {
            System.out.println("getNombreJourMois : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        int i = 0;
        return i;
    }

    /**
     * Convertit une date en chaîne dd/MM/yyyy en java.sql.Date
     *
     * @param daty
     * @return
     */
    public static java.sql.Date stringDate(String daty) {
        if (daty == null || daty.compareTo("") == 0) {
            return null;
        }
        java.sql.Date sqlDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = sdf.parse(daty);
            sqlDate = new Date(date.getTime());
        } catch (Exception e) {
            System.out.println("Error stringDate :" + e.getMessage());
        }
        return sqlDate;
    }

    /**
     * Compléter un entier (ex: 67, 4 devient 0067)
     *
     * @param longuerChaine
     * @param nombre
     * @return
     */
    public static String completerInt(int longuerChaine, int nombre) {
        String zero = null;
        zero = "";
        for (int i = 0; i < longuerChaine - String.valueOf(nombre).length(); i++) {
            zero = String.valueOf(String.valueOf(zero)).concat("0");
        }

        return String.valueOf(zero) + String.valueOf(String.valueOf(nombre));
    }

    public static String completerInt(int longuerChaine, String nombre2) {
        int nombre = stringToInt(nombre2);
        return completerInt(longuerChaine, nombre);
    }

    /**
     * Retourne la valeur de l'heure actuelle en chaîne de caractères
     * HH:MM:SS:MS
     *
     * @return
     */
    public static String heureCourante() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11) + 1))).concat(":");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(12))).append(":")));
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(13))).append(":")));
        retour = String.valueOf(retour) + String.valueOf(completerInt(2, a.get(14) / 10));
        return retour;
    }

    /**
     * Retourne la valeur de l'heure actuelle en chaîne de caractères HH:MM:SS
     *
     * @return
     */
    public static String heureCouranteHMS() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11)))).concat(":");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(12))).append(":")));
        retour = String.valueOf(String.valueOf(
                (new StringBuffer(String.valueOf(String.valueOf(retour)))).append(completerInt(2, a.get(13)))));
        return retour;
    }

    /**
     * Retourne la valeur de l'heure actuelle en chaîne de caractères HH:MM
     *
     * @return
     */
    public static String heureCouranteHM() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(11)))).concat(":");
        retour = String.valueOf(String.valueOf(
                (new StringBuffer(String.valueOf(String.valueOf(retour)))).append(completerInt(2, a.get(12)))));
        return retour;
    }

    /**
     * Retourne la date du jour au format dd/MM/yyyy
     *
     * @return
     */
    public static String dateDuJour() {
        Calendar a = Calendar.getInstance();
        String retour = null;
        retour = String.valueOf(String.valueOf(completerInt(2, a.get(5)))).concat("/");
        retour = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(retour))))
                .append(completerInt(2, a.get(2) + 1)).append("/")));
        retour = String.valueOf(retour) + String.valueOf(completerInt(4, a.get(1)));
        return retour;
    }

    public static Date dateDuJourSql() {
        return string_date("dd/MM/yyyy", dateDuJour());
    }

    /**
     *
     * @param nombre
     * @return
     */
    public static String annulerZero(int nombre) {
        if (nombre == 0) {
            return " ";
        } else {
            return String.valueOf(nombre);
        }
    }

    /**
     * Retourne l'intersection des 2 tableaux
     *
     * @param objet1
     * @param objet2
     * @return
     */
    public static Vector intersecter(ClassMAPTable objet1[], ClassMAPTable objet2[]) {
        Vector retour = new Vector();
        int dim1 = objet1.length;
        int dim2 = objet2.length;
        int nbEgaux = 0;
        for (int i = 0; i < dim1; i++) {
            String cle1 = objet1[i].getTuppleID();
            for (int j = 0; j < dim2; j++) {
                String cle2 = objet2[j].getTuppleID();
                if (cle1.compareTo(cle2) == 0) {
                    retour.add(nbEgaux, objet2[j]);
                    nbEgaux++;
                }
            }

        }
        return retour;
    }

    /**
     * Retourne l'intersection des 2 vecteurs
     *
     * @param objet1
     * @param objet2
     * @return
     */
    public static Vector intersecter(Vector objet1, Vector objet2) {
        Vector retour = new Vector();
        int dim1 = objet1.size();
        int dim2 = objet2.size();
        int nbEgaux = 0;
        for (int i = 0; i < dim1; i++) {
            ClassMAPTable temp = (ClassMAPTable) objet1.elementAt(i);
            String cle1 = temp.getTuppleID();
            for (int j = 0; j < dim2; j++) {
                ClassMAPTable temp2 = (ClassMAPTable) objet2.elementAt(j);
                String cle2 = temp2.getTuppleID();
                if (cle1.compareTo(cle2) == 0) {
                    retour.add(nbEgaux, temp2);
                    nbEgaux++;
                }
            }

        }

        return retour;
    }

    public static boolean intersecterIgnoreCase(String ref, String valeur, ClassMAPTable objet1[]) {
        int dim1 = objet1.length;
        int nbEgaux = 0;
        for (int i = 0; i < dim1; i++) {
            String cle1 = objet1[i].getTuppleID();
            if (ref.compareTo(cle1) == 0) {
                return true;
            }
        }

        return false;
    }

    public static boolean intersecter(String ref, ClassMAPTable objet1[]) {
        int dim1 = objet1.length;
        int nbEgaux = 0;
        for (int i = 0; i < dim1; i++) {
            String cle1 = objet1[i].getTuppleID();
            if (ref.compareTo(cle1) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retourne true si l'id d'un objet dans le vecteur est égal à ref
     *
     * @param ref
     * @param objet1
     * @return
     */
    public static boolean intersecter(String ref, Vector objet1) {
        int dim1 = objet1.size();
        int nbEgaux = 0;
        if (objet1 != null) {
            for (int i = 0; i < dim1; i++) {
                ClassMAPTable temp = (ClassMAPTable) objet1.elementAt(i);
                String cle1 = temp.getTuppleID();
                if (ref.compareTo(cle1) == 0) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Convertit un vecteur en tableau d'objets
     *
     * @param v
     * @return
     */
    public static Object[] toArray(Vector v) {
        Object retour[] = new Object[v.size()];
        for (int i = 0; i < v.size(); i++) {
            retour[i] = v.elementAt(i);
        }

        return retour;
    }

    public static String getRequest(String temp) {
        if (temp == null || temp.compareTo("") == 0) {
            return "";
        } else {
            return temp;
        }
    }

    /**
     * Convertit temp en % si la chaîne est null ou vide
     *
     * @param temp
     * @return
     */
    public static String getValeurNonNull(String temp) {
        if (temp == null || temp.compareTo("") == 0) {
            return "%";
        } else {
            return temp;
        }
    }

    /**
     * Construit une clé primaire
     *
     * @param longPK
     * @param indPk
     * @param nomProcedureSequence
     * @return
     * @throws Exception
     */
    public static String makePK(int longPK, String indPk, String nomProcedureSequence) throws Exception {
        int maxSeq = getMaxSeq(nomProcedureSequence);
        String nombre = completerInt(longPK, maxSeq);
        return String.valueOf(indPk) + String.valueOf(nombre);
    }

    /**
     * Retourne le nom des champs d'un objet
     *
     * @param a
     * @return
     */
    public static String[] getNomColonne(Object a) {
        String retour[] = null;
        Field f[] = a.getClass().getDeclaredFields();
        retour = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            retour[i] = f[i].getName();
        }

        return retour;
    }

    /**
     * Retourne les champs 'nombre' ou 'chain' d'un objet
     *
     * @param a
     * @param typ
     * @return
     */
    public static String[] getNomColonne(Object a, String typ) {
        String retour[] = null;
        Field f[] = a.getClass().getFields();
        Vector v = new Vector();
        for (int i = 0; i < f.length; i++) {
            if (typ.compareToIgnoreCase("nombre") == 0) {
                if ((f[i].getType().getName().compareToIgnoreCase("int") == 0)
                        || (f[i].getType().getName().compareToIgnoreCase("double") == 0)
                        || (f[i].getType().getName().compareToIgnoreCase("float") == 0)
                        || (f[i].getType().getName().compareToIgnoreCase("short") == 0)) {
                    v.add(f[i].getName());
                }
            }
            if (typ.compareToIgnoreCase("chaine") == 0) {
                if (f[i].getType().getName().compareToIgnoreCase("String") == 0) {
                    v.add(f[i].getName());
                }
            }
        }
        retour = new String[v.size()];
        v.copyInto(retour);
        return retour;
    }

    public static String cryptWord(String mot) {
        int niveau = (int) Math.round(Math.random() * 10);
        int sens = (int) Math.round(Math.random());
        if (niveau == 0) {
            niveau = -5;
        }
        return (cryptWord(mot, niveau, sens));
    }

    public static String cryptWord(String mot, int niveauCrypt, int croissante) {
        if (croissante == 0) {
            return cryptWord(mot, niveauCrypt, true);
        } else {
            return cryptWord(mot, niveauCrypt, false);
        }
    }

    public static String cryptWord(String mot, int niveauCrypt, boolean croissante) {
        char[] ar = mot.toCharArray();
        char[] retour = new char[ar.length];

        if (croissante) {
            for (int i = 0; i < ar.length; i++) {
                int k = Character.getNumericValue(ar[i]);
                if (k < (Character.MAX_RADIX - niveauCrypt)) {
                    retour[i] = Character.forDigit(k + niveauCrypt, Character.MAX_RADIX);
                } else {
                    retour[i] = ar[i];
                }
            }
        } else {
            for (int i = 0; i < ar.length; i++) {
                int k = Character.getNumericValue(ar[i]);
                if (k > (niveauCrypt - 1)) {
                    retour[i] = Character.forDigit(k - niveauCrypt, Character.MAX_RADIX);
                } else {
                    retour[i] = ar[i];
                }
            }
        }

        return new String(retour);
    }

    public static String unCryptWord(String mot, int niveauCrypt, boolean croissante) {
        char[] ar = mot.toCharArray();
        char[] retour = new char[ar.length];

        if (croissante) {
            for (int i = 0; i < ar.length; i++) {
                int k = Character.getNumericValue(ar[i]);
                if (k < (Character.MAX_RADIX - niveauCrypt)) {
                    retour[i] = Character.forDigit(k - niveauCrypt, Character.MAX_RADIX);
                } else {
                    retour[i] = ar[i];
                }
            }
        } else {
            for (int i = 0; i < ar.length; i++) {
                int k = Character.getNumericValue(ar[i]);
                if (k > (niveauCrypt - 1)) {
                    retour[i] = Character.forDigit(k + niveauCrypt, Character.MAX_RADIX);
                } else {
                    retour[i] = ar[i];
                }
            }
        }
        return new String(retour);
    }

    public static int[] transformerMoisAnnee(int mois, int annee) {
        int[] retour = new int[2];
        retour[1] = annee + mois / 12;
        retour[0] = mois % 12;
        if (retour[0] == 0) {
            retour[0] = 12;
            retour[1] = retour[1] - 1;
        }
        return retour;
    }

    /**
     * Retourne le mois correspondant au nombre; 1 pour Janvier, ...
     *
     * @param nombre
     * @return
     */
    public static String nbToMois(int nombre) {
        String mois = null;
        switch (nombre) {
            case 1: // '\001'
                mois = "janvier";
                break;

            case 2: // '\002'
                mois = "fevrier";
                break;

            case 3: // '\003'
                mois = "mars";
                break;

            case 4: // '\004'
                mois = "avril";
                break;

            case 5: // '\005'
                mois = "mai";
                break;

            case 6: // '\006'
                mois = "juin";
                break;

            case 7: // '\007'
                mois = "juillet";
                break;

            case 8: // '\b'
                mois = "aoï¿½t";
                break;

            case 9: // '\t'
                mois = "septembre";
                break;

            case 10: // '\n'
                mois = "octobre";
                break;

            case 11: // '\013'
                mois = "novembre";
                break;

            case 12: // '\f'
                mois = "decembre";
                break;

            default:
                mois = null;
                break;
        }
        return mois;
    }

    /**
     * Tsy tokony ho ato ito
     *
     * @return
     * @throws Exception
     */
    public static Date getDatePayementEcolage() throws Exception {
        Connection c = null;
        UtilDB util = new UtilDB();
        Statement sta = null;
        ResultSet rs = null;
        Date max = null;
        try {
            c = util.GetConn();
            String param = "Select MAX(DATEFINPAYMENTECOLAGE2TRANCHE) FROM RENTRESEMESTRE";
            sta = c.createStatement();
            rs = sta.executeQuery(param);
            rs.next();
            max = rs.getDate(1);
        } catch (SQLException e) {
            System.out.println("getNbTuple : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
            util.close_connection();
        }
        return max;
    }

    /**
     *
     * @param d
     * @return
     */
    public static String datetostring(java.sql.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("dd/MM/yyyy");
        daty = dateJava.format(d);
        return daty;
    }

    /**
     *
     * @param d
     * @return
     */
    public static String datetostring(java.util.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("dd/MM/yyyy");
        daty = dateJava.format(d);
        return daty;
    }

    /**
     * Retourne une date en toutes lettes (ex: 12 Avril 2021)
     *
     * @param dat
     * @return
     */
    public static String datedujourlettre(String dat) {
        String jour = getJour(dat);
        String mois = convertDebutMajuscule(nbToMois(Utilitaire.stringToInt(Utilitaire.getMois(dat))));
        String annee = getAnnee(dat);
        String daty = jour + " " + mois + " " + annee;
        return daty;
    }

    public static String getIdByCb(String cb) {
        char[] dd = cb.toCharArray();
        char[] id = new char[9];
        int j = 8;
        for (int i = cb.length() - 1; i > cb.length() - 10; i--) {
            id[j] = dd[i];
            j--;
        }
        String idString = new String(id);
        return idString;
    }

    public static String getIdByCbEns(String cb) {
        // 2PROFENS2
        return cb.substring(5, cb.length());
    }

    static Random r = new Random();

    public static String getAnneeParam(String param, String daty) {
        String annee = daty.split(param)[0];
        return annee;
    }

    public static String getJourParam(String param, String daty) {
        return completerInt(2, split(daty, param)[2]);
    }

    public static String getMoisParam(String param, String daty) {
        return completerInt(2, split(daty, param)[1]);
    }

    public static Date string_dateParam(String param, String patterne, String daty) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(patterne);
            formatter.applyPattern(patterne);
            formatter.setTimeZone(TimeZone.getTimeZone("EUROPE"));
            String annee = getAnneeParam(param, daty);
            int anneecours = getAneeEnCours();
            int siecl = anneecours / 100;
            if (annee.toCharArray().length == 2) {
                annee = String.valueOf(siecl) + annee;
            }
            daty = getJourParam(param, daty) + "/" + getMoisParam(param, daty) + "/" + annee;
            Date hiredate = new Date(formatter.parse(daty).getTime());
            Date date1 = hiredate;
            return date1;
        } catch (Exception e) {
            System.out.println("Error string_date :".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Date date = dateDuJourSql();
        return date;
    }

    public static String verifNumerique(String s) {
        String res = s;
        res = res.replace(',', '.');
        String[] temp = split(res, ' ');
        res = "";
        for (int i = 0; i < temp.length; i++) {
            res += temp[i];
        }
        try {
            Float.valueOf(res);
            return res;
        } catch (Exception e) {
            return s;
        }

    }

    /**
     * Upload de fichier dans un CDN (Content Delivery Network ou réseau de
     * diffusion de contenu)
     *
     * @param f
     * @param filename
     * @throws Exception
     */
    public static void uploadFileToCdn(InputStream f, String filename) throws Exception {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            java.util.Properties prop = configuration.CynthiaConf.load();
            HttpPost httppost = new HttpPost(prop.getProperty("cdnUri"));

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("nom", new StringBody(filename));
            entity.addPart("fichiers", new InputStreamBody(f, filename));
            httppost.setEntity(entity);

            System.out.println("EXECUTING REQUEST : " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                resEntity.consumeContent();
            }

            httpclient.getConnectionManager().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Suppression de fichier dans un CDN
     *
     * @param filename
     */
    public static void deleteFileFromCdn(String filename) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            java.util.Properties prop = configuration.CynthiaConf.load();
            HttpPost httppost = new HttpPost(prop.getProperty("cdnDeleteUri"));

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("filename", new StringBody(filename));
            httppost.setEntity(entity);
            System.out.println("EXECUTING REQUEST : " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                resEntity.consumeContent();
            }

            httpclient.getConnectionManager().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retourne l'extension d'un fichier
     *
     * @param nomfichier
     * @return
     */
    public static String getExtensionFichier(String nomfichier) {
        String text = nomfichier;
        String[] val = text.split("\\.");
        return val[val.length - 1];
    }

    /**
     * Retourne une heure valide
     *
     * @param heure
     * @return
     * @throws Exception
     */
    public static String testHeureValide(String heure) throws Exception {
        if (heure.contains(":")) {
            return transformeHeure(heure);
        }
        if (heure.contains("H")) {
            String replace = heure.replace("H", ":");
            if (replace.contains("M")) {
                replace = replace.replace("M", "");
            }
            return transformeHeure(replace);
        }
        if (heure.contains("h")) {
            String replace = heure.replace("h", ":");
            if (replace.contains("m")) {
                replace = replace.replace("m", "");
            }
            return transformeHeure(replace.toString());
        }
        throw new Exception("Heure non valide.");
    }

    public static String transformeHeure(String heure) throws Exception {
        String[] str = heure.split(":");
        if (str == null || str.length < 2) {
            throw new Exception("Heure non valide.");
        } else {
            int hr = -1;
            int min = -1;
            try {
                hr = Integer.parseInt(str[0]);
                min = Integer.parseInt(str[1]);
            } catch (Exception e) {
                throw new Exception("Heure non valide.");
            }
            if (hr >= 24 || hr < 0 || min < 0 || min >= 60) {
                throw new Exception("Heure non valide.");
            }

            for (int i = 0; i < str.length; i++) {
                if (str[i].length() < 2) {
                    str[i] = "0" + str[i];
                }
            }
            heure = str[0] + ":" + str[1];
        }
        return heure;
    }

    /**
     * Retourne une chaîne de caractères: séparée par 'virgule' et limitée par
     * 'quote'
     *
     * @param s
     * @param quote
     * @param virgule
     * @return
     */
    public static String tabToString(String[] s, String quote, String virgule) {
        String res = "";
        try {
            res = quote + s[0] + quote;
            for (int i = 1; i < s.length; i++) {
                res = res + virgule + quote + s[i] + quote;
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Retourne un tableau de chaîne donc 'text' est séparé par un 'séparateur'
     *
     * @param text
     * @param separateur
     * @return
     */
    public static String[] stringToTab(String text, String separateur) {
        String temps = text.trim();
        String[] ret = split(temps, separateur.charAt(0));
        return ret;
    }

    public static boolean isPeriodString(String value, String toReplace) {
        boolean ret = false;

        try {

            String tempp = replaceChar(value, toReplace, "1");
            int val = stringToInt(tempp.trim());
            if (val > 0) {
                ret = true;
            }
        } catch (Exception ex) {
            ret = false;
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Retourne la date maximale entre d1 et d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static java.sql.Date dateMax(java.sql.Date d1, java.sql.Date d2) {
        if (d1 == null && d2 == null) {
            return null;
        }
        if (d1 == null) {
            return d2;
        }
        if (d2 == null) {
            return d1;
        }
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Retourne la date minimale entre d1 et d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static java.sql.Date dateMin(java.sql.Date d1, java.sql.Date d2) {
        if (d1 == null && d2 == null) {
            return null;
        }
        if (d1 == null) {
            return d2;
        }
        if (d2 == null) {
            return d1;
        }
        return (d1.before(d2)) ? d1 : d2;
    }

    public static String dateMax(String d1, String d2) {
        java.sql.Date retour = dateMax(stringDate(d1), stringDate(d2));
        return datetostring(retour);
    }

    public static String dateMin(String d1, String d2) {
        java.sql.Date retour = dateMin(stringDate(d1), stringDate(d2));
        return datetostring(retour);
    }

    public static String getHeureFromTimestamp(java.sql.Timestamp heure) {
        String ora = completerInt(2, heure.getHours());
        String min = completerInt(2, heure.getMinutes());
        String sec = completerInt(2, heure.getSeconds());
        return ora + ":" + min + ":" + sec;
    }

    /**
     * Retourne la différence entre 2 dates en année
     *
     * @param first
     * @param last
     * @return
     */
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH)
                || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String getAnneePeriode(String periode) throws Exception {
        if (periode.length() != 6) {
            throw new Exception("Format periode invalide");
        }
        return periode.substring(0, 4);
    }

    public static String getMoisPeriode(String periode) throws Exception {
        if (periode.length() != 6) {
            throw new Exception("Format periode invalide");
        }
        return periode.substring(4, 6);
    }

    /**
     * idem getSemestre
     *
     * @param daty
     * @return
     */
    public static String getPeriode(Date daty) {
        int mois = getMois(daty);
        String periode = "";
        if (mois >= 1 && mois <= 3) {
            periode += getAnnee(daty) + "01";
        }
        if (mois >= 4 && mois <= 6) {
            periode += getAnnee(daty) + "02";
        }
        if (mois >= 7 && mois <= 9) {
            periode += getAnnee(daty) + "03";
        }
        if (mois >= 10 && mois <= 12) {
            periode += getAnnee(daty) + "04";
        }
        return periode;
    }

    /**
     * idem getSemestre
     *
     * @param periode
     * @return
     */
    public static String[] getMoisPeriode2(String periode) {
        String[] ret = new String[3];
        String trimestre = periode.substring(4);
        switch (trimestre) {
            case "01":
                ret[0] = "01";
                ret[1] = "02";
                ret[2] = "03";
                break;
            case "02":
                ret[0] = "04";
                ret[1] = "05";
                ret[2] = "06";
                break;
            case "03":
                ret[0] = "07";
                ret[1] = "08";
                ret[2] = "09";
                break;
            case "04":
                ret[0] = "10";
                ret[1] = "11";
                ret[2] = "12";
                break;
        }
        return ret;
    }

    /**
     * Compare 2 heures: 0 si égaux, 1 si debut {@literal <}fin; -1 sinon @param
     *
     * heureDebut
     * 
     * @param heureFin
     * @return
     * @throws Exception
     */
    public static int comparerHeure(String heureDebut, String heureFin) throws Exception {
        int h1, h2;
        String[] HMDebut, HMFin;
        testHeureValide(heureDebut);
        testHeureValide(heureFin);
        HMDebut = heureDebut.split(":");
        HMFin = heureFin.split(":");
        h1 = Integer.valueOf(HMDebut[0] + HMDebut[1]);
        h2 = Integer.valueOf(HMFin[0] + HMFin[1]);
        if (h1 < h2) {
            return 1;
        }
        if (h2 < h1) {
            return -1;
        }
        return 0;
    }

    /**
     *
     * @param periode
     * @param diff
     * @return
     * @throws Exception
     */
    public static int ajoutMoisPeriode(int periode, int diff) throws Exception {
        String daty = "01/" + getMoisPeriode(String.valueOf(periode)) + "/" + getAnneePeriode(String.valueOf(periode));
        java.sql.Date datySql = stringDate(daty);
        java.sql.Date dt = ajoutMoisDate(datySql, diff);
        String annee = getAnnee(datetostring(dt));
        String mois = getMois(datetostring(dt));
        int retour = stringToInt(annee + mois);
        return retour;
    }

    /**
     *
     * @param periode
     * @param diff
     * @return
     * @throws Exception
     */
    public static String ajoutMoisPeriode(String periode, int diff) throws Exception {
        int retour = ajoutMoisPeriode(stringToInt(periode), diff);
        return String.valueOf(retour);
    }

    /**
     *
     * @param lettreInit
     * @return
     */
    public static String incrementLettre(char[] lettreInit) {
        char[] lettre = lettreInit;
        for (int i = lettre.length - 1; i >= 0; i--) {
            for (char j = 'a'; j <= 'z'; j++) {
                if (lettre[i] == j && j == 'z') {
                    lettre[i] = 'a';
                } else if (lettre[i] == j && j < 'z') {
                    char tmp = j;
                    tmp = (char) (tmp + 1);
                    lettre[i] = tmp;
                    return new String(lettre);
                }
            }
        }
        return null;
    }

    /**
     * Calculer l'âge selon la date donnée
     *
     * @param naissance
     * @return
     */
    public static int calculeAge(java.sql.Date naissance) {
        int age = getAneeEnCours() - getAnnee(naissance);
        java.sql.Date temp = new java.sql.Date(naissance.getYear(), naissance.getMonth(), naissance.getDay());
        temp.setYear(getAneeEnCours() - 1900);
        if (dateDuJourSql().compareTo(temp) < 0) {
            age--;
        }
        return age;
    }

    public static int calculeAge(String date) {
        return calculeAge(stringDate(date));
    }

    /**
     * Véeifie si le tableau de chaînes possède un doublon
     *
     * @param input
     * @return
     */
    public static boolean possedeDoublon(String[] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input.length; j++) {
                if (input[i].equals(input[j]) && i != j) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * idem getTrimestre (ity no fohy indrindra)
     *
     * @param mois
     * @return
     */
    public static int getTrimestreByMois(int mois) {
        if (mois <= 3) {
            return 1;
        } else if (mois > 3 && mois <= 6) {
            return 2;
        } else if (mois > 6 && mois <= 9) {
            return 3;
        } else if (mois > 9 && mois <= 12) {
            return 4;
        }
        return 0;
    }

    /**
     * Enlève les chaînes null ou vides
     *
     * @param array
     * @return
     */
    public static String[] enleverNulouVide(String[] array) {
        List<String> list = new ArrayList<String>();
        for (String s : array) {
            if (s != null && s.length() > 0) {
                list.add(s);
            }
        }
        array = list.toArray(new String[list.size()]);
        return array;
    }

    /**
     * Calcul hormis jour fériés
     *
     * @param mois
     * @param annee
     * @return
     */
    public static int calculNbreJourOuvrable(String mois, String annee) {

        java.util.Date startDate = new java.util.Date(Integer.valueOf(annee) - 1900, Integer.valueOf(mois) - 1, 1);
        String dates = Utilitaire.getLastDayOfDate("01/" + mois + "/" + annee);
        java.util.Date endDate = Utilitaire.stringToDate("yyyy-MM-dd", dates);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workDays++;
            }
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workDays;
    }

    /**
     * Retourne la date Avant d
     *
     * @param d
     * @param ajout
     * @return
     */
    public static Date getDateAvant(Date d, int ajout) {
        GregorianCalendar c = new GregorianCalendar(d.getYear() + 1900, d.getMonth(), d.getDate());
        c.set(GregorianCalendar.DATE, c.get(GregorianCalendar.DATE) + ajout);
        java.util.Date dt = c.getTime();
        return new Date(dt.getTime());
    }

    /**
     *
     * @param dmin
     * @param dmax
     * @return
     */
    public static java.sql.Date[] convertIntervaleToListDate(java.sql.Date dmin, java.sql.Date dmax) {
        Vector v = new Vector();
        int i = 0;
        while (1 < 2) {
            Date d1 = getDateAvant(dmin, i);
            if (Utilitaire.compareDaty(d1, dmax) != 0) {
                v.add(d1);
                i++;
            } else {
                v.add(dmax);
                break;
            }
        }
        Date[] res = new Date[v.size()];
        v.copyInto(res);
        return res;
    }

    /**
     * Retourne la période d'échéance par rapport au mois donné
     *
     * @param mois
     * @return
     */
    public static int getEcheance(int mois) {
        int ret = 0;
        if (mois == 1 || mois == 4 || mois == 7 || mois == 10) {
            ret = 1;
        } else if (mois == 2 || mois == 5 || mois == 8 || mois == 11) {
            ret = 2;
        } else if (mois == 3 || mois == 6 || mois == 9 || mois == 12) {
            ret = 3;
        }
        return ret;
    }

    /**
     * Retourne a date en français
     *
     * @param date
     * @return
     */
    public static String dateEnFrancais(Date date) {
        DateFormat format = DateFormat.getInstance();
        DateFormat format_fr = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH);
        return format_fr.format(date);
    }

    /**
     * Retourne true si l'un des objets de la liste correspond au premier
     * élément
     *
     * @param liste
     * @return
     */
    public static boolean comparerObjet(Object[] liste) {
        boolean ret = true;
        for (int i = 0; i < liste.length; i++) {
            if (!liste[0].equals(liste[i])) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static boolean validerHeureMinute(String timeString) {
        if (timeString.length() != 5) {
            return false;
        }
        if (!timeString.substring(2, 3).equals(":")) {
            return false;
        }
        int hour = validateNumber(timeString.substring(0, 2));
        int minute = validateNumber(timeString.substring(3, 5));
        if (hour < 0 || hour >= 24) {
            return false;
        }
        if (minute < 0 || minute >= 60) {
            return false;
        }
        return true;
    }

    private static int validateNumber(String numberString) {
        try {
            int number = Integer.valueOf(numberString);
            return number;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Calculer l'âge par rapport à la date de naissance et une date de repère
     *
     * @param naissance
     * @param dateRepere
     * @return
     */
    public static int calculeAgeDate(java.sql.Date naissance, java.sql.Date dateRepere) {
        int age = getAneeEnCours() - getAnnee(naissance);
        java.sql.Date temp = new java.sql.Date(naissance.getYear(), naissance.getMonth(), naissance.getDay());
        temp.setYear(getAneeEnCours() - 1900);
        if (dateRepere.compareTo(temp) < 0) {
            age--;
        }
        return age;
    }

    /**
     * Tronquer un double
     *
     * @param number
     * @param numDigits
     * @return
     */
    public static double truncateDouble(double number, int numDigits) {
        double result = number;
        String arg = "" + number;
        int idx = arg.indexOf('.');
        if (idx != -1) {
            if (arg.length() > idx + numDigits) {
                arg = arg.substring(0, idx + numDigits + 1);
                result = Double.parseDouble(arg);
            }
        }
        return result;
    }

    /**
     * Arrondir un décimal
     *
     * @param a
     * @param pattern
     * @param mode
     * @return
     */
    public static double arrondirDecimalWithMode(double a, String pattern, RoundingMode mode) {// pattern deux chffires
                                                                                               // aprï¿½s la virgule
                                                                                               // #.##
        DecimalFormat df = new DecimalFormat(pattern);
        df.setRoundingMode(mode); // RoundingMode.HALF_UP, RoundingMode.HALF_DOWN
        String format = df.format(a).replace(",", ".");
        return Double.valueOf(format);
    }

    /**
     * Convertit le nombre de jours en HH:MM:SS, dont 1jour = 8h
     *
     * @param jour
     * @return
     */
    public static String convertJour8hEnJourHeureMinute(double jour) {
        String result = "";
        if (jour > 0) {
            int j_part_ent = (int) jour;
            result = j_part_ent + "j";
            double j_part_dec = jour - j_part_ent;
            double heure = j_part_dec * 8;
            int h_part_ent = (int) heure;
            result = result + " " + h_part_ent + "h";
            double h_part_dec = heure - h_part_ent;
            double minute = h_part_dec * 60;
            int m_part_ent = (int) minute;
            result = result + " " + m_part_ent + "min";
        }
        return result;
    }

    /**
     * Convertit une chaîne en Timestamp
     *
     * @param val
     * @param separator
     * @return
     * @throws Exception
     */
    public static Timestamp convertStringToTimestampHour(String val, String separator) throws Exception {
        String[] tab = val.split(separator);
        if (tab.length < 3) {
            throw new Exception("Format heure invalide");
        }
        int hour = Integer.valueOf(tab[0]);
        int min = Integer.valueOf(tab[1]);
        int sec = Integer.valueOf(tab[2]);
        return new Timestamp(0, 0, 0, hour, min, sec, 0);
    }

    /**
     * Retourne l'heure courante
     *
     * @return
     */
    public static String getCurrentHeure() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String ret = sdf.format(cal.getTime()).toString();
        return ret;
    }

    /**
     * Vérifie si les 2 dates appartiennent au même mois
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean testMemeMois(java.sql.Date d1, java.sql.Date d2) {
        return Utilitaire.getMois(d1) == Utilitaire.getMois(d2) && Utilitaire.getAnnee(d1) == Utilitaire.getAnnee(d2);
    }

    /**
     * Retourne la date sans séparateur
     *
     * @param daty
     * @return
     */
    public static String getDateSansSeparateur(String daty) {
        String jour = getJour(daty);
        String mois = getMois(daty);
        String annee = getAnnee(daty);
        return jour + mois + annee;
    }

    /**
     * idem datedujourlettre
     *
     * @param daty
     * @return
     */
    public static String getDateEnLettre(String daty) {
        String jour = getJour(daty);
        jour = ChiffreLettre.convertIntToString(Integer.valueOf(jour)).toUpperCase();
        String mois = nbToMois(Integer.valueOf(getMois(daty))).toUpperCase();
        String annee = getAnnee(daty);
        return jour + " " + mois + " " + annee;
    }

    /**
     * Vérifie si une date est valide ou non
     * 
     * @param input
     * @param format
     * @return
     */
    public static boolean isValidDate(String input, String format) {
        boolean valid = false;
        try {
            if (!input.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                java.util.Date output = dateFormat.parse(input);
                valid = dateFormat.format(output).equals(input);
            }
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
    }

    /**
     * Retourne l'heure actuelle en toutes lettres
     *
     * @param heure
     * @return
     */
    public static String getHeureLettre(String heure) {
        String[] heuresplit = heure.split(":");
        if (heuresplit[0].compareToIgnoreCase("01") == 0 || heuresplit[0].compareToIgnoreCase("00") == 0) {
            heuresplit[0] = ChiffreLettre.convertIntToString(Integer.valueOf(heuresplit[0])).toUpperCase() + "E HEURE ";
        } else if (heuresplit[0].compareToIgnoreCase("21") == 0) {
            heuresplit[0] = ChiffreLettre.convertIntToString(Integer.valueOf(heuresplit[0])).toUpperCase()
                    + "E HEURES ";
        } else {
            heuresplit[0] = ChiffreLettre.convertIntToString(Integer.valueOf(heuresplit[0])).toUpperCase() + " HEURES ";
        }
        if (heuresplit[1].compareToIgnoreCase("00") == 0) {
            heuresplit[1] = "";
        } else if (heuresplit[1].compareToIgnoreCase("01") == 0) {
            heuresplit[1] = "ET " + ChiffreLettre.convertIntToString(Integer.valueOf(heuresplit[1])).toUpperCase()
                    + "E MINUTE";
        } else {
            heuresplit[1] = "ET " + ChiffreLettre.convertIntToString(Integer.valueOf(heuresplit[1])).toUpperCase()
                    + " MINUTES";
        }
        return heuresplit[0] + heuresplit[1];
    }

    /**
     * Arrondis à l'inférieur
     *
     * @param k
     * @return
     */
    public static double arrondiInf(double k) {
        return arrondirDecimalWithMode(k, "#", RoundingMode.DOWN);
    }

    /**
     * Convertit un String en java.util.Date
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static java.util.Date castString2Date(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = formatter.parse(dateString);
        return date;
    }

    /**
     * Différence entre 2 dates
     *
     * @param date1
     * @param date2
     * @param timeUnit
     * @return
     */
    public static long getDateDiff(java.util.Date date1, java.util.Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Modifie le format d'une date en yyyy-MM-dd
     *
     * @param inputDate
     * @return
     * @throws Exception
     */
    public static java.util.Date modifyDate(String inputDate) throws Exception {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return date;
    }

    /**
     * Transforme une date en chaîne yyyy/mm/dd au format dd-mm-yyyy
     *
     * @param date
     * @return
     */
    public static String splitDate(String date) {
        String[] split = date.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getListDateBetween2Date(String date1, String date2) throws Exception {

        ArrayList<String> result = new ArrayList<>();
        java.util.Date fromDate = modifyDate(splitDate(date1));
        java.util.Date toDate = modifyDate(splitDate(date2));

        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        result.add(date1);
        while (cal.getTime().before(toDate)) {
            cal.add(Calendar.DATE, 1);
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                result.add(new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime()));
            }
        }
        return result;
    }

    /**
     * Retourne la liste des jours de la semaine
     *
     * @return
     */
    public static String[] listJours() {
        return new String[] { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" };
    }

    /**
     * Convertit champ null, vide en '-'
     *
     * @param nul
     * @return
     */
    public static String champNullToTiret(String nul) {
        if (nul == null) {
            return "-";
        } else if (nul.compareToIgnoreCase("null") == 0) {
            return "-";
        } else if (nul.compareToIgnoreCase("") == 0) {
            return "-";
        } else {
            return nul;
        }
    }

    /**
     * Retourne la date du jour + 1 an
     *
     * @return
     */
    public static String anneeSuivantDateDuJour() {
        String daty = Utilitaire.dateDuJour();
        String[] split = daty.split("/");
        int anneeSuiv = Integer.parseInt(split[2]) + 1;
        String val = split[0] + "/" + split[1] + "/" + anneeSuiv;
        return val;
    }

    /**
     * Convertit un mètre cube en litre
     *
     * @param q
     * @return
     */
    public static double convertirM3enLitre(String q) {
        if (q.contains("mc")) {
            String[] val = Utilitaire.split(q, "mc");
            if (val.length >= 1) {
                return Utilitaire.stringToDouble(val[0]) * 1000;
            }
        }

        return Utilitaire.stringToDouble(q);
    }

    /**
     *
     * @param s
     * @return
     */
    public static String escapeSQLString(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder escapedText = new StringBuilder();
        char currentChar;
        for (int i = 0; i < s.length(); i++) {
            currentChar = s.charAt(i);
            switch (currentChar) {
                case '\'':
                    escapedText.append("''");
                    break;
                default:
                    escapedText.append(currentChar);
            }
        }
        return escapedText.toString();
    }

    /**
     * Séparer les milliers par espace
     *
     * @param valeur
     * @return
     */
    public static String separateurMillier(String valeur) {
        return separateurMillier(' ', valeur);
    }

    /**
     * Séparateur de millier
     *
     * @param separateur
     * @param valeur
     * @return
     */
    public static String separateurMillier(char separateur, String valeur) {
        double val = 0;
        if (valeur.matches("\\p{Digit}+")) {
            val = Double.valueOf(valeur);
        } else {
            return valeur;
        }
        DecimalFormat format = new DecimalFormat("000,000"); // c'est pas necessaire de mettre 3 blocs mais je me
                                                             // rappelle plus la syntaxe exacte
        DecimalFormatSymbols s = format.getDecimalFormatSymbols();
        s.setGroupingSeparator(separateur);
        format.setDecimalFormatSymbols(s);
        return format.format(val);
    }

    /**
     * Différence entre 2 dates en mois
     *
     * @param dateMax
     * @param dateMin
     * @return
     * @throws Exception
     */
    public static int getDiffMoisPrecis(Date dateMax, Date dateMin) throws Exception {
        try {
            int moisdifference = diffMoisDaty(dateMax, dateMin);
            // LocalDate localDateMax = dateMax.toLocalDate();
            // LocalDate localDateMin = dateMin.toLocalDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateMax);
            int dayMax = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(dateMin);
            int dayMin = cal.get(Calendar.DAY_OF_MONTH);
            int jour = dayMax - dayMin;
            if (jour <= 0) {
                // moisdifference--;
            }
            return moisdifference;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Retourne le nom de la classe sans les packages
     *
     * @param arg
     * @return
     * @throws Exception
     */
    public static String splitClassName(String arg) throws Exception {
        try {
            String[] res = arg.split("\\.");
            return res[res.length - 1];
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * idem getListDateBetween2Date
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static ArrayList<String> genererDatesEntre(String date1, String date2) throws Exception {

        String[] jourFerie = {};
        Date startdate = new java.sql.Date((Utilitaire.stringToDate("dd/MM/yyyy", date1)).getTime());
        Date enddate = new java.sql.Date((Utilitaire.stringToDate("dd/MM/yyyy", date2)).getTime());
        Calendar enddateone = Calendar.getInstance();
        enddateone.setTime(enddate);
        enddateone.add(Calendar.DATE, 1);

        List<Date> jourFerieDate = new ArrayList<Date>();
        for (int i = 0; i < jourFerie.length; i++) {
            jourFerieDate.add(new java.sql.Date((Utilitaire.stringToDate("dd/MM/yyyy", jourFerie[i])).getTime()));
        }
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);
        while (calendar.getTime().before(enddateone.getTime())) {
            java.util.Date result = calendar.getTime();
            java.sql.Date dateToAdd = new java.sql.Date(result.getTime());
            if (Utilitaire.dayOfDate(dateToAdd) != 1 && Utilitaire.dayOfDate(dateToAdd) != 7
                    && !jourFerieDate.contains(dateToAdd)) {
                dates.add(dateToAdd);
            }
            calendar.add(Calendar.DATE, 1);
        }
        ArrayList<String> listResult = new ArrayList<String>();
        for (int i = 0; i < dates.size(); i++) {
            listResult.add(Utilitaire.formatterDaty(dates.get(i)));
        }
        return listResult;
    }

    /**
     * Générer un script sql de la forme " and column in (liste des val) "
     *
     * @param val
     * @param column
     * @return
     * @throws Exception
     */
    public static String getAWhereIn(String[] val, String column) throws Exception {
        String query = "";
        try {
            if (val.length == 0) {
                throw new Exception("La valeur de la selection multiple est vide.");
            }
            query += " AND " + column + " IN (";
            // tsy ity ve no fohy kokoa? query += tabToString(val, "'", ",");
            query += "'" + val[0] + "'";
            for (int i = 1; i < val.length; i++) {
                query += ",'" + val[i] + "'";
            }
            query += ") ";
        } catch (Exception exc) {
            throw exc;
        }
        return query;
    }
    /**
     * Générer un script sql de la forme " and column in (liste des val) "
     *
     * @param val
     * @param column
     * @return
     * @throws Exception
     */
    public static String getAWhereIn(ClassMAPTable[] val, String column,String attributDansVal) throws Exception {
        String query = "";
        try {
            if (val.length == 0) {
                throw new Exception("La valeur de la selection multiple est vide.");
            }
            query += " AND " + column + " IN (";
            query += "'" + CGenUtil.getValeurFieldByMethod(val[0], attributDansVal).toString() + "'";
            for (int i = 1; i < val.length; i++) {
                query += ",'" + CGenUtil.getValeurFieldByMethod(val[i], attributDansVal).toString() + "'";
            }
            query += ") ";
        } catch (Exception exc) {
            throw exc;
        }
        return query;
    }

    /**
     * Vérifie si la date est un jour ouvrable
     *
     * @param daty
     * @return
     * @throws Exception
     */
    public static boolean estJourOuvrable(Date daty) throws Exception {
        boolean result = false;
        if (Utilitaire.dayOfDate(daty) != 1 && Utilitaire.dayOfDate(daty) != 7) {
            result = true;
        }
        return result;
    }

    /**
     * Tsy mandeha ito
     *
     * @param daty
     * @return
     * @throws Exception
     */
    public String getJourDeLaSemaineDate(String daty) throws Exception {
        try {
            Date d = this.stringDate(daty);
            int jourdelasemaine = d.getDay();
            String result = "-";
            switch (jourdelasemaine) {
                case 1:
                    result = "Lundi";
                    break;
                case 2:
                    result = "Lundi";
                    break;
                case 3:
                    result = "Lundi";
                    break;
                case 4:
                    result = "Lundi";
                    break;
                case 5:
                    result = "Lundi";
                    break;
            }
            return result;
        } catch (Exception exc) {
            exc.printStackTrace();
            throw exc;
        }
    }

    /**
     * Retourne la date donnée + 1 an
     *
     * @return
     */
    public static Date getDateAnneeProchaine(Date arg) throws Exception {
        Date result = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(arg);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            int mois = Utilitaire.getMoisEnCoursReel();
            String date_s = (day - 1) + "/" + mois + "/" + (year + 1);
            result = Utilitaire.string_date("dd/MM/yyyy", date_s);
        } catch (Exception exc) {
            throw exc;
        }
        return result;
    }

    /**
     *
     * @param date
     * @return
     */
    public static String formatDateRecherche(String date) {
        String result = "";
        if (date.contains("/") & stringDate(date) != null) {
            String[] split = date.split("/");
            result = split[2] + "-" + split[1] + "-" + split[0];
            return result;
        } else {
            return date;
        }
    }

    /**
     * Retourne true si la lettreChiffre est numérique, false sinon
     *
     * @param lettreChiffre
     * @return
     */
    public static boolean estNumerique(String lettreChiffre) {
        char[] lc = lettreChiffre.toCharArray();
        for (char c : lc) {
            if (Character.isDigit(c) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne le dernier jour du mois
     *
     * @param daty
     * @return
     * @throws Exception
     */
    public static Date lastDayOfMonth(Date daty) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(daty);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new java.sql.Date(c.getTimeInMillis());
    }

    /**
     * idem separateurMillier
     *
     * @param nombre
     * @return
     */
    public static String separerEnMillier(String nombre) {
        String s = "";
        char[] tab = nombre.toCharArray();
        char t;
        int k = 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            t = tab[i];
            s = String.valueOf(t) + s;
            if (k == 3) {
                s = " " + s;
                k = 1;
            } else {
                k++;
            }
        }
        return s;
    }

    /**
     *
     * @param indice
     * @param n
     * @return
     */
    public static int getNumeroPage(int indice, int n) {
        int k = 1;
        while (k > 0) {
            if (indice < k * n) {
                return k + 1;
            }
            k++;
        }
        return 2;
    }

    /**
     *
     * @param indice
     * @param n
     * @return
     */
    public static int genererNouveauNumero(int indice, int n) {
        int k = 1;
        while (k > 0) {
            if (indice < k * n) {
                return k * n + 2;
            }
            k++;
        }
        return 2;
    }

    /**
     * Retourne la configuration pour l'année en cours
     *
     * @return
     */
    public static String getParametreAnnee() {
        Connection conn = null;
        try {
            conn = (new UtilDB()).GetConn();
            TypeObjet o = new TypeObjet();
            o.setNomTable("configuration");
            // atao anaty constante ito
            TypeObjet[] list = (TypeObjet[]) CGenUtil.rechercher(o, null, null, conn, " AND ID = 'PRM00001'");
            if (list != null && list.length != 0) {
                return list[0].getVal();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return getAnneeEnCours();
    }

    public static int getMillenismeAnnee(String annee) {
        return stringToInt(annee.substring(2));
    }

    public static String[] formerTableauGroupe(String val1, String val2) throws Exception {
        String retour[] = null;
        if ((val1 == null || val1.compareToIgnoreCase("") == 0 || val1.compareToIgnoreCase("-") == 0)
                && (val2 != null && val2.compareToIgnoreCase("") != 0)) {
            retour = new String[1];
            retour[0] = val2;
            return retour;
        } else if ((val2 == null || val2.compareToIgnoreCase("") == 0)
                && (val1 != null && val1.compareToIgnoreCase("") != 0)) {
            retour = new String[1];
            retour[0] = val1;
            return retour;
        } else if ((val2 == null || val2.compareToIgnoreCase("") == 0)
                && (val1 == null || val1.compareToIgnoreCase("") == 0)) {
            return null;
        } else {
            retour = new String[2];
            retour[0] = val1;
            retour[1] = val2;
            return retour;
        }
    }

    /**
     *
     * @param mots
     * @return
     */
    public static String TraitementMots(String mots) {
        String motsApres = "";
        int longueurMots = mots.length();
        int resteDivision = longueurMots % 4;
        int nbDivision = (longueurMots / 4) + 1;
        for (int i = 0; i < nbDivision; i++) {
            if (resteDivision == 0 && i < (nbDivision - 1)) {
                motsApres = i == (nbDivision - 2) ? motsApres + "<div>" + mots.substring(i * 4, longueurMots) + "</div>"
                        : motsApres + "<div>" + mots.substring(i * 4, i * 4 + 4) + "-</div>";
            }
            if (resteDivision > 0) {
                motsApres = i == (nbDivision - 1)
                        ? motsApres + "<div>" + mots.substring(i * 4, i * 4 + resteDivision) + "</div>"
                        : motsApres + "<div>" + mots.substring(i * 4, i * 4 + 4) + "-</div>";
            }
        }
        return motsApres;
    }

    /**
     *
     * @param mots
     * @return
     */
    public static String TraitementMotsVerticale(String mots) {
        String motsApres = "";
        int longueurMots = mots.length();
        int nbDivision = longueurMots / 4 + 1;
        for (int i = 0; i < nbDivision; i++) {
            motsApres += motsApres + "<div>" + mots.substring(i, i + 1) + "</div>";
        }
        return motsApres;
    }

    /**
     *
     * @param na
     * @param nc
     * @param ma
     * @param mc
     * @return
     */
    public static double[] calculValeur(double na, double nc, double ma, double mc) {
        double[] coef = new double[2];
        while (na > ma & nc < mc) {
            nc = nc + 0.01;
            na = na - 0.01;
        }
        coef[0] = na;
        coef[1] = nc;
        return coef;
    }

    public static String getValPourcentage(String valeur) {
        return null;
    }

    public static String remplacePourcentage(String valeur) {
        String retour = "";
        if (valeur == null) {
            return "";
        }
        char val[] = new char[valeur.length()];
        val = valeur.toCharArray();
        int taille = val.length;
        if (valeur.compareToIgnoreCase("") == 0) {
            return "";
        }
        if (valeur.compareToIgnoreCase("%") == 0 || valeur == null) {
            return "%25";
        }
        /*
         * if(val[0] == '%' && val[taille - 1] == '%')
         * {
         * retour = retour.concat("%25");
         * retour = retour.concat(valeur.substring(1, taille - 1));
         * retour = retour.concat("%25");
         * }
         * if(val[0] != '%' && val[taille - 1] == '%')
         * {
         * retour = valeur.substring(0, taille - 1);
         * retour = retour.concat("%25");
         * }
         * if(val[0] == '%' && val[taille - 1] != '%')
         * {
         * retour = retour.concat("%25");
         * retour = retour.concat(valeur.substring(1, taille));
         * }
         */
        retour = remplaceMot(valeur, "%", "%25");
        retour = remplaceMot(retour, " ", "%20");
        return retour;
    }

    public static String[] getBorneAnneeEnCours() {
        return null;
    }

    /**
     * tester si une chaine est numérique
     * 
     * @param requested chaine de caractère à tester
     * @return vrai si chaine de caractère numérique sinon faux
     * @throws Exception
     */
    public static boolean checkNumber(String requested) throws Exception {
        boolean numeric = true;
        try {
            Double num = Double.parseDouble(requested);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    /**
     * formatter chaine de caractères
     * 
     * @param ac
     * @return
     */
    public static String getStringAC(String ac) {
        String finals = "";
        if (ac.indexOf("_") > 0) {
            String[] tab = ac.split("_");
            for (int i = 0; i < tab.length - 1; i++) {
                finals += tab[i] + "_";
            }
        }
        return finals;
    }

    public static String completerIntFin(int longuerChaine, int nombre) {
        String zero = null;
        zero = "";
        for (int i = 0; i < longuerChaine - String.valueOf(nombre).length(); i++) {
            zero = String.valueOf(String.valueOf(zero)).concat("0");
        }

        return String.valueOf(String.valueOf(nombre)) + String.valueOf(zero);
    }

    public static String completerIntFin(int longuerChaine, String nombre2) {
        String zero = null;
        zero = "";
        for (int i = 0; i < longuerChaine - nombre2.length(); i++) {
            zero = String.valueOf(String.valueOf(zero)).concat("0");
        }

        return nombre2 + String.valueOf(zero);
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static String[] transformerLangue(String[] mots, String lang) {
        ResourceBundle RB = ResourceBundle.getBundle("text", new Locale(lang));
        String[] ret = new String[mots.length];
        for (int i = 0; i < mots.length; i++) {
            ret[i] = RB.getString(mots[i]);
        }
        return ret;
    }

    public static String completerIntFin(int longuerChaine, String nombre2, int nbr) {
        String rpad = "";
        for (int i = 0; i < longuerChaine - nombre2.length(); i++) {
            rpad = rpad.concat(String.valueOf(nbr));
        }

        return nombre2 + String.valueOf(rpad);
    }
}
