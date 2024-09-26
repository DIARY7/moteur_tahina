
package affichage;

import bean.CGenUtil;

import static bean.CGenUtil.getValeurFieldByMethod;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import bean.ClassMAPTable;
import utilitaire.UtilDB;

/**
 * Objet à utiliser pour fair une graphe dans une page .
 * Ci-dessous une exemple de comment créer une graphe. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 * 
 *  GraphJS graphe = null;
 * 
 *  Map<String, String> numerotab = new HashMap<String, String>();
 *  String[] ordonnee = {"RESULTAT_TEMPERATURE", "GLYCEMIEMOL_POST"};
 *  graphe = new GraphJS("daty", ordonnee, "idpatient", id, "observation_medicale", "mg.amadia.medecine.Observation_medicale", "");  
 *  graphe.setCouleurp("RESULTAT_TEMPERATURE", "#3c8dbc");
 *  graphe.setCouleurp("GLYCEMIEMOL_POST", "#d11c5d");
 *   
 *  graphe.setLibelleChamp("RESULTAT_TEMPERATURE", "Temperature");
 *  graphe.setLibelleChamp("GLYCEMIEMOL_POST", "Taux de glycemie");
 * 
 * }
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de filtre de graph, on met: 
 * 
 * <pre>
 * {@code 
 *  
 *   <%= graphe.construireFiltreGraphe()%>     
 * }
 * </pre>
 * 
 * Ne pas oublier de mettre un script pour dessiner avec le id du canvas dans le html
 * <pre>
 * {@code
 *      <script>
 * 
 *  function dess(){
 *     <%= graphe.getHtmlJs("idcanvas")%>
 *  }
 *  
 *  $(document).ready(function() {
 *      dess();
 *  });
 *      </script>
 * }
 * </pre>
 * @author BICI
 */
public class GraphJS {

    private Map<String, Map<String, Double>> datagraphe;
    private String[] dataOrdonnee;//nom de collone anaty base. Exemple = GLYCEMIEMOL_POST, RESULTAT_TEMPERATURE ...
    private String[] dataOrdonneeDessiner;//nom de collone anaty base. Exemple = GLYCEMIEMOL_POST, RESULTAT_TEMPERATURE ...
    private String dataAbscisse; // nom de collone anaty base. Exemple = daty
    private String[] abscisseValue; // Exemple = "2018-03-11" ,"2018-04-12" ,"2018-05-11" ,"2018-07-12" ,"2018-09-22" ,"2018-10-11" ... 
    private Map<String, String> libelleOrdonnee;
    private Map<String, String> couleurOrdonnee;
    private String apresForm;

    public String[] getDataOrdonneeDessiner() {
        return dataOrdonneeDessiner;
    }

    public void setDataOrdonneeDessiner(String[] dataOrdonneeDessiner) {
        this.dataOrdonneeDessiner = dataOrdonneeDessiner;
    }

    public String getApresForm() {
        return apresForm;
    }

    public void setApresForm(String apresForm) {
        this.apresForm = apresForm;
    }

    public Map<String, String> getCouleurOrdonnee() {
        return couleurOrdonnee;
    }

    public void setCouleurOrdonnee(Map<String, String> couleurOrdonnee) {
        this.couleurOrdonnee = couleurOrdonnee;
    }

    public Map<String, String> getLibelleOrdonnee() {
        return libelleOrdonnee;
    }

    public void setLibelleOrdonnee(Map<String, String> libelleOrdonnee) {
        this.libelleOrdonnee = libelleOrdonnee;
    }

    public String[] getAbscisseValue() {
        return abscisseValue;
    }

    public void setAbscisseValue(String[] abscisseValue) {
        this.abscisseValue = abscisseValue;
    }

    public String[] getDataOrdonnee() {
        return dataOrdonnee;
    }

    public void setDataOrdonnee(String[] dataOrdonnee) {
        this.dataOrdonnee = dataOrdonnee;
    }

    public String getDataAbscisse() {
        return dataAbscisse;
    }

    public void setDataAbscisse(String dataAbscisse) {
        this.dataAbscisse = dataAbscisse;
    }

    public Map<String, Map<String, Double>> getDatagraphe() {
        return datagraphe;
    }

    public void setDatagraphe(Map<String, Map<String, Double>> datagraphe) {
        this.datagraphe = datagraphe;
    }

    /**
     * Constructeur vide 
     */
    public GraphJS() {
    }

    /**
     * Cette fonction permet de  donner un nom  à la colonne dans la base pour l'affichage
     * @param nomCollone : nom du colonne dans la base
     * @param libelle : nom donné apparrue sur l'affichage
     * @throws Exception lorsque le nomCollone donnée et le nom de colonne dans la base ne se correspondent pas  
    */
    public void setLibelleChamp(String nomCollone, String libelle) throws Exception {
        try {
            if (getLibelleOrdonnee() == null) {
                setLibelleOrdonnee(new ConcurrentHashMap<String, String>());
            }
            if (isInside(nomCollone, getDataOrdonnee())) {
                getLibelleOrdonnee().put(nomCollone, libelle);
            } else {
                throw new Exception("Erreur nom de collone incorrect ou pas dans la liste");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Cette fonction permet de  donner une couleur  à la colonne dans la base pour l'affichage
     * @param nomCollone : nom du colonne dans la base
     * @param couleur : couleur apparrue sur l'affichage
     * @throws Exception lorsque le nomCollone donnée et le nom de colonne dans la base ne se correspondent pas  
     */
    public void setCouleurp(String nomCollone, String couleur) throws Exception {
        try {
            if (getCouleurOrdonnee() == null) {
                setCouleurOrdonnee(new ConcurrentHashMap<String, String>());
            }
            if (isInside(nomCollone, getDataOrdonnee())) {
                getCouleurOrdonnee().put(nomCollone, couleur);
            } else {
                throw new Exception("Erreur nom de collone incorrect ou pas dans la liste");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Cette fonction permet de verifier si le paramètre key se trouve dans la liste des string de tab
     * @param key : type string 
     * @param tab : type string[]
     * @return boolean (true or false)
     * @throws Exception
     */
    public static boolean isInside(String key, String[] tab) throws Exception {
        boolean retour = false;
        try {
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].equals(key)) {
                    retour = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;
    }

    //String dataAbscisse : daty, String[] dataOrdonnee = {"POIDS","TEMPERATURE"}, String collonefiltre = "idpatient", String ciolloneValue = "PERS0001", String nomVue, String nomClasse, String apwhere
    /**
     * 
     * @param dataAbscisse donné afficher sur l'axe des x
     * @param dataOrdonnee donné afficher sur l'axe des y
     * @param collonefiltre colonne pour le filtre
     * @param ciolloneValue colonne pour valeur
     * @param nomVue nom de la table utilisée
     * @param nomClasse nom de la classe utilisée 
     * @param apwhere requete SQL apres clause where pour filtre
     * @throws Exception
     */
    public GraphJS(String dataAbscisse, String[] dataOrdonnee, String collonefiltre, String ciolloneValue, String nomVue, String nomClasse, String apwhere) throws Exception {
        try {
            ClassMAPTable[] tab = (ClassMAPTable[]) findDatafonrJS(dataAbscisse, dataOrdonnee, collonefiltre, ciolloneValue, nomVue, nomClasse, apwhere);
            Map<String, Map<String, Double>> data = new ConcurrentHashMap<String, Map<String, Double>>();
            Map dataTemp = new ConcurrentHashMap<String, Double>();
            this.setDataAbscisse(dataAbscisse);
            this.setDataOrdonnee(dataOrdonnee);
            this.setDataOrdonneeDessiner(dataOrdonnee);
            for (int j = 0; j < dataOrdonnee.length; j++) {
                for (int i = 0; i < tab.length; i++) {
                    Object valeurk = getValeurFieldByMethod(tab[i], dataOrdonnee[j]);
                    Double temp_value = Double.valueOf(valeurk.toString());
                    dataTemp.put(String.valueOf(getValeurFieldByMethod(tab[i], dataAbscisse)), temp_value);

                }
                Map<String, Double> treeMap = new TreeMap<String, Double>(dataTemp);
                data.put(dataOrdonnee[j], treeMap);
                dataTemp = new ConcurrentHashMap<String, Double>();

            }
            this.setDatagraphe(data);
            this.setKeyAbscisse();
            for (int i = 0; i < getDataOrdonnee().length; i++) {
                setLibelleChamp(getDataOrdonnee()[i], getDataOrdonnee()[i]);
                setCouleurp(getDataOrdonnee()[i], "#3c8dbc");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } 
    }


    /**
     * Rechercher une classe mapping  avec filtre
     * @param dataAbscisse donné afficher sur l'axe des x
     * @param dataOrdonnee donné afficher sur l'axe des y
     * @param collonefiltre colonne de filtre (obligatoire)
     * @param ciolloneValue valeur du valeur de filtre ( obligatoire)
     * @param nomVue nom de la table utilisée 
     * @param nomClasse nom de la classe utilisée 
     * @param apwhere partie de la requête SQL après la clause where pour filtrer 
     * @return tableau de la classe mapping
     * @throws Exception
     */
    public ClassMAPTable[] findDatafonrJS(String dataAbscisse, String[] dataOrdonnee, String collonefiltre, String ciolloneValue, String nomVue, String nomClasse, String apwhere) throws Exception {
        Connection c = null;
        try {
//            String querry = "select "+dataAbscisse+","+GraphJS.converTableauStrongTovirg(dataOrdonnee)+" from "+nomVue+" where "+collonefiltre+" = '"+ciolloneValue+"'";
            String where = " and " + collonefiltre + " = '" + ciolloneValue + "' " + apwhere + " order by " + dataAbscisse + " asc ";
            c = new UtilDB().GetConn();
            ClassMAPTable t = (ClassMAPTable) (Class.forName(nomClasse).newInstance());
            t.setNomTable(nomVue);
            ClassMAPTable[] tab = (ClassMAPTable[]) CGenUtil.rechercher(t, null, null, c, where);
            return tab;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Cette fonction permet de donner les clé de chaque valeur de abscisse . 
     */
    public void setKeyAbscisse() {
        String[] retour = null;
        try {
            List<String> list_temp = new ArrayList<>();
            if (getDatagraphe() != null && getDataOrdonnee().length > 0) {

                for (String key : getDatagraphe().get(getDataOrdonnee()[0]).keySet()) {
                    list_temp.add(key);
                }
            }
            if (list_temp.size() > 0) {
                retour = new String[list_temp.size()];
                for (int i = 0; i < list_temp.size(); i++) {
                    retour[i] = list_temp.get(i);
                }
            }
            this.setAbscisseValue(retour);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
//        return retour;
    }

    /**
     * Permet de convertir un tableau de string en label js
     * @param tab liste des string
     * @return label js de type string  
     * {@code
     *  [\"A\",\"b\"]
     * }
     * @throws Exception
     */
    public static String convertTabStringToLabelJS(String[] tab) throws Exception {
        String retour = "[";
        try {
            for (int i = 0; i < tab.length; i++) {
                if (i == (tab.length - 1)) {
                    retour += "\"" + tab[i] + "\"";
                } else {
                    retour += "\"" + tab[i] + "\" ,";
                }
            }
            retour += "]";
            System.out.println("retour = " + retour);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;
    }


    /**
     * Permet de construire le datasets du graph
     * @return le datasets du graphe de type string 
     * 
     * @throws Exception
     */
    public String construireDatasets() throws Exception {
        String retour = "[";
        try {
            if (getDataOrdonneeDessiner().length > 0) {
                for (int i = 0; i < getDatagraphe().size(); i++) {
                    String ordonnee_temp = getDataOrdonnee()[i];
                    boolean testMandalo = false;
                    if(isInside(ordonnee_temp, getDataOrdonneeDessiner())){
                        testMandalo = true;
                    }
                    if (testMandalo) {
                        if (i == (getDatagraphe().size() - 1)) {
                            retour += " { label : \"" + getLibelleOrdonnee().get(ordonnee_temp) + "\" , ";
                            retour += " data: [";
                            for (int k = 0; k < getAbscisseValue().length; k++) {
                                Double temp = getDatagraphe().get(ordonnee_temp).get(getAbscisseValue()[k]);
                                if (k == (getAbscisseValue().length - 1)) {
                                    retour += temp;
                                } else {
                                    retour += temp + ",";
                                }

                            }
                            retour += " ],";
                            retour += "borderColor: ["
                                    + "'" + getCouleurOrdonnee().get(ordonnee_temp) + "'"
                                    + "],";
                            retour += "borderWidth: 2 } ";
                        } else {
                            retour += " { label : \"" + getLibelleOrdonnee().get(ordonnee_temp) + "\" , ";
                            retour += " data: [";
                            for (int k = 0; k < getAbscisseValue().length; k++) {
                                Double temp = getDatagraphe().get(ordonnee_temp).get(getAbscisseValue()[k]);
                                if (k == (getAbscisseValue().length - 1)) {
                                    retour += temp;
                                } else {
                                    retour += temp + ",";
                                }

                            }
                            retour += " ],";
                            retour += "borderColor: ["
                                    + "'" + getCouleurOrdonnee().get(ordonnee_temp) + "'"
                                    + "],";
                            retour += "borderWidth: 2 } ,";
                        }
                    }
                }
                retour += "]";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;
    }


    /**
     * Génerer le javascript qui construit le graphe
     * @param idDuCanvas id du div où le graphe va être
     * @return le html du graph de type string 
     * @throws Exception
     */
    public String getHtmlJs(String idDuCanvas) throws Exception {
        String retour = "";
        try {
            if (getDatagraphe() != null) {
                retour += "var ctxL = document.getElementById(\"" + idDuCanvas + "\").getContext('2d');"
                        + "    var myLineChart = new Chart(ctxL, {"
                        + "        type: 'line',"
                        + "        data: {"
                        + "            labels: " + convertTabStringToLabelJS(getAbscisseValue()) + ","
                        + "            datasets: " + construireDatasets()
                        + "        },"
                        + "        options: {"
                        + "            responsive: true"
                        + "        }"
                        + "    });";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;
    }

    /**
     * Cette fonction sert à concatener tous les valeurs avec une virgule
     * @param tab tableau de string 
     * @return un string qui ressemble  les valeur des string en paramètre qui sont separées par des virugle
     * @throws Exception
     */
    public static String converTableauStrongTovirg(String[] tab) throws Exception {
        String retour = "";
        try {
            if (tab != null) {
                if (tab.length > 0) {
                    for (int i = 0; i < tab.length; i++) {
                        if (i == (tab.length - 1)) {
                            retour += tab[i];
                        } else {
                            retour += tab[i] + ",";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return retour;
    }

    /**
     * Sert à construire le html du filtre du graphe 
     * @return L'html pour le filtre de type string 
     * @throws Exception
     */
    public String construireFiltreGraphe() throws Exception {
        String retour = "<form action=\"" + getApresForm() + "\" method=\"post\">";
        try {
            for (int i = 0; i < getDataOrdonnee().length; i++) {
                retour += "<div class=\"custom-control custom-checkbox\">\n"
                        + "                          <label class=\"custom-control-label\" for=\"customCheck1\">" + getLibelleOrdonnee().get(getDataOrdonnee()[i]) + "</label>\n"
                        + "                          <input type=\"checkbox\" class=\"custom-control-input\" name=\"" + getDataOrdonnee()[i] + "\" "+printchecked(getDataOrdonnee()[i], getDataOrdonneeDessiner())+">\n"
                        + "                         </div>";
            }
            retour += "<button class=\"btn btn-success\">Dessiner</button>";
            retour += "</form>";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;
    }

    /**
     * Sert à convertir list en tableau de string 
     * @param liste liste des strings à convertir
     * @return tableau de string 
     * @throws Exception
     */
    public static String[] convertListTotab(List<String> liste) throws Exception {
        String[] retour = null;
        try {

            if (liste.size() > 0) {
                retour = new String[liste.size()];
                for (int i = 0; i < liste.size(); i++) {
                    retour[i] = liste.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return retour;

    }
    
    public static String printchecked(String text, String[] tab)throws Exception{
        String retour = "";
        try{
            if(isInside(text, tab))retour = "checked";
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return retour;
    }
}
