
package utilitaire;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe d'utilitaire pour manipuler des htmls.
 * <p>
 * Par exemple j'ai un table en html, j'ai envie d'avoir un tableau representant cette table:
 * </p>
 * <pre>
 *  String html = "<td>..............</td>";
 *  ArrayList<ArrayList> valeurs = UtilitaireExcelMa.convertHTMLTableToArrayList(html);
 * </pre>
 * @author BICI
 */
public class UtilitaireExcelMa {

    public static String[] caractereSpeciaux = {"°", "^", "¨"};

    /**
     * La méthode renvoie un tableau de chaînes contenant toutes les sous-chaînes de
     * s qui apparaissent entre les chaînes de début et de fin.
     * @param s  la chaine à rechercher dans start et end
     * @param start chaînes de début
     * @param end chaînes de fin
     * @return les sous-chaînes extraites
     */
    public static String[] getAllStringBetween(String s, String start, String end) {
        try {
            ArrayList<String> ret = new ArrayList<String>();
            String[] ret1 = null;
            String pattern1 = start;
            String pattern2 = end;
            String text = s.trim();
            System.out.println("avant____"+text);
            text = text.replace("\n", "").trim();
            text = text.replace("\t", "").trim();
            Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
            Matcher m = p.matcher(text);
            while (m.find()) {
                ret.add(m.group(1));
            }
            ret1 = new String[ret.size()];
            for (int i = 0; i < ret.size(); i++) {
                System.out.println("ret.get(i) = " + ret.get(i));
                ret1[i] = ret.get(i);
            }
            return ret1;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * Sert à obtenir une chaîne contenant un élément HTML avec 
     * la balise spécifiée par aChercher, y compris son contenu.
     * @param html élément HTML valide
     * @param aChercher balise
     * @return une chaîne contenant un élément HTML
     */
    public static String convertirEnTete(String html, String aChercher) {
        html = html.substring(html.indexOf("<" + aChercher) + 1);

        //Eto tokony misy vérification Rowspan sy Colspan
        //*************************************************
        String ret = html.substring(html.indexOf(">") + 1);
        //System.out.println("ret______"+ret.length()+" index _____"+ret.indexOf("</"+aChercher+">")+"___istrue____"+(ret.indexOf("</"+aChercher+">")<ret.length()));
        //System.out.println("mipoaka______"+ret.substring(0,ret.indexOf("</"+aChercher+">")));
        ret = "<" + aChercher + ">" + ret.substring(0, ret.indexOf("</" + aChercher + ">")) + "</" + aChercher + ">";
        return ret;
    }

    /**
     *  La méthode renvoie une liste de chaînes contenant toutes les sous-chaînes de
     * s qui apparaissent entre les chaînes de début et de fin.
     * @param s la chaine à rechercher dans start et end
     * @param start chaînes de début
     * @param end chaînes de fin
     * @return les sous-chaînes extraites
     */
    public static ArrayList<String> getAllStringBetweenToArray(String s, String start, String end) {
        ArrayList<String> ret = new ArrayList<String>();
        String[] ret1 = null;
        String pattern1 = start;
        String pattern2 = end;
        String text = s.replace("\t", "").replace("\n", "");
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        while (m.find()) {
            ret.add(m.group(1));
            // System.out.println("ret____"+m.group(1));
        }
        return ret;
    }

    /**
     * Sert à convertir un tableau d'HTML en ArrayList
     * @param html tableau html 
     * @return  liste des 
     * @throws UnsupportedEncodingException
     */
    public static ArrayList<ArrayList> convertHTMLTableIntoArrayList(String html) throws UnsupportedEncodingException {
        try {
            if (html == null || html.compareTo("") == 0) {
                return null;
            }
            html = convertirEnTete(html, "table");
            ArrayList<String> ret = new ArrayList<>();
            ArrayList<ArrayList> retFinal = new ArrayList<>();
            String headerStr = "";
            if (html.contains("thead")) {
                headerStr = convertirEnTete(html, "thead");
                if (headerStr.contains("tr")) {
                    headerStr = convertirEnTete(headerStr, "tr");
                } else {
                    return retFinal;
                }
            } else {
                headerStr = "<tr>" + html.substring(html.indexOf("<tr"), html.indexOf("</tr>")) + "</tr>";
            }
            if (headerStr.length() > 0) {
                ret.add(headerStr);
            }
            if (html.contains("tbody") || (html.indexOf("tr") != html.lastIndexOf("tr"))) {
                if (html.contains("tbody")) {
                    if (html.contains("thead")) {
                        html = html.substring(html.indexOf("</thead>") + 8);
                    }
                    String tbody = convertirEnTete(html, "tbody");
                    while (tbody.indexOf("tr") != -1) {
                        String tmp = convertirEnTete(tbody, "tr");
                        ret.add(tmp);
                        String tmpNouveau = tbody.substring(tbody.indexOf("</tr>") + 4);
                        tbody = tmpNouveau;
                    }
                } else {
                    String tbody = convertirEnTete(html, "tbody");
                    tbody = html.substring(html.indexOf("</tr>") + 3);
                    while (tbody.indexOf("tr") != -1) {
                        ret.add(convertirEnTete(tbody, "tr"));
                        tbody = tbody.substring(0, tbody.indexOf("</tr>"));
                    }
                }
            }
            retFinal = convertTRToArray(ret);
            return retFinal;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * Convertir les <tr> avec des <td> ou <th> en liste de valeurs
     * @param tr html <tr>....</tr>
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ArrayList<ArrayList> convertTRToArray(ArrayList<String> tr) throws UnsupportedEncodingException {
        //   System.out.println("converting tr to array");
        try {
            ArrayList<ArrayList> retFinal = new ArrayList<ArrayList>();
            for (int i = 0; i < tr.size(); i++) {
                String[] between = getAllStringBetween(tr.get(i), "<tr>", "</tr>");
                String tmp = UtilitaireExcelMa.convertirEnTete(tr.get(i), "tr");
                ArrayList<String> ret = new ArrayList<String>();
                if (tmp.contains("<th")) {
                    while (tmp.indexOf("th") != -1) {
                        String tmp2 = convertirEnTete(tmp, "th");
                        for (String caractere : caractereSpeciaux) {
                            if (caractere.length() > 1) {
                                tmp2 = Utilitaire.replaceChar(tmp2, String.valueOf(caractere.charAt(1)), " ");
                            }
                        }
                        String valeur = getAllStringBetween(tmp2, "<th>", "</th>")[0];
                        if (valeur.length() > 0) {
                            String[] temp = getAllStringBetween(valeur, ">", "</");
                            if (temp != null && temp.length > 0) {
                                valeur = temp[0];
                            }
                        }
                        ret.add(valeur.replace("&nbsp;", " "));
                        String tmpNouveau = tmp.substring(tmp.indexOf("</th>") + 5);
                        tmp = tmpNouveau;
                    }
                } else {
                    while (tmp.indexOf("<td") != -1) {
                        String tmp2 = convertirEnTete(tmp, "td");

                        for (String caractere : caractereSpeciaux) {
                            if (caractere.length() > 1) {
                                tmp2 = Utilitaire.replaceChar(tmp2, String.valueOf(caractere.charAt(1)), " ");
                            }
                        }
                        byte[] utf8 = tmp2.getBytes("UTF-8");
                        tmp2 = new String(utf8).replace("\n", "").replace("\t", "");
                        if(!tmp2.endsWith("</td>")){
                            tmp2 += "</td>";
                        }
                        System.out.println("tmp2 " + tmp2);
                        String valeur = getAllStringBetween(tmp2, "<td>", "</td>")[0];
                        if (valeur.length() > 0) {
                            String[] temp = getAllStringBetween(valeur, ">", "</");
                            if (temp != null && temp.length > 0) {
                                valeur = temp[0];
                            }
                        }

                        ret.add(valeur.replace("&nbsp;", " "));
                        String tmpNouveau = tmp.substring(tmp.indexOf("</td>") + 5);
                        tmp = tmpNouveau;
                    }

                }
                retFinal.add(ret);
            }
            return retFinal;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

}
