package affichage;

import java.util.HashMap;
import java.util.Map;

/**
 * Objet à utiliser à l'ajout de nouvel onglet dans une page .
 * Ci-dessous une exemple de comment créer un onglet dans une page. Cette partie doit être au debut du jsp après les imports.
 * <pre>
 * 
 * {@code
 * 
 *  Onglet onglet = new Onglet("patient");
 *  Map<String, String> numerotab = new HashMap<String, String>();
 *   numerotab.put("1", "acte-a-facturer");
 * 
 *   Map<String, String> nomPage = new HashMap<String, String>();
 *   nomPage.put("acte-a-facturer", "");
 *   onglet.setListeNumero(numerotab);
 *   onglet.setListePage(nomPage); 
 *
 * 
 * }
 * </pre>
 * 
 * Ensuite dans notre HTML où on veut mettre le corps de la fiche, on met: 
 * 
 * <pre>
 * {@code 
 *  <%
 *  <li class="<%=nomPage.get("acte-a-facturer")%>"><a href="<%=baseLien%>&id=<%=id%>&tab=1">Actes à facturer</a></li>
 * 
 * <div class="tab-pane active" id="tab_1-1">
 *  <jsp:include page="<%=onglet.getCurrentPage(tabParam)%>">
 *           <jsp:param name="idsig_personne" value="<%=id%>" />
 *   </jsp:include>
 *  
 *</div>
 * 
 *  %>
 * 
 * }
 *
 * @author BICI
 */
public class Onglet {

    private String defaut;
    private Map<String, String> listeNumero = new HashMap<String, String>();
    private Map<String, String> listePage = new HashMap<String, String>();
    private String dossier = "inc";


    /**
     * Fonction qui sert à avoir la page actuel 
     * @param param nom de la page mais pas des espaces 
     * @return lien pour aller vers la page actuel . 
     */
    public String getCurrentPage(String param) {
//si le nom de la page demandé est dans le registre 
        String tab = "";
        if (listePage.get(param) != null) {
            tab = param;
//si le numero de l'onglet demandé est dans le registre
        } else if (listeNumero.get(param) != null) {
            tab = listeNumero.get(param);
        } else {
            tab = this.getDefaut();
        }
        listePage.put(tab, "active");
        return this.getDossier() + "/" + tab + ".jsp";
    }
    /**
     * Constructeur par défaut
     * @param defaut onglet actif
     */
    public Onglet(String defaut) {
        this.setDefaut(defaut);
    }
    /**
     * 
     * @return page défaut à prendre si aucune page n'est valide
     */
    public String getDefaut() {
        return defaut;
    }

    public void setDefaut(String defaut) {
        this.defaut = defaut;
    }
   
    /**
     * 
     * @return mapping des numéros aux onglets
     */
    public Map<String, String> getListeNumero() {
        return listeNumero;
    }

    public void setListeNumero(Map<String, String> listeNumero) {
        this.listeNumero = listeNumero;
    }
    /**
     * 
     * @return mapping des noms des pages aux des fichiers JSP
     */
    public Map<String, String> getListePage() {
        return listePage;
    }
    
    public void setListePage(Map<String, String> listePage) {
        this.listePage = listePage;
    }

    public String getDossier() {
        return dossier;
    }
    /**
     * 
     * @param dossier nom de dossier où les fichiers de JSP
     */
    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

}
