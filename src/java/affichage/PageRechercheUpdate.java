/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import javax.servlet.http.HttpServletRequest;

/**
 * Objet à utiliser à l'affichage pour génerer la page recherche Update avec liste d'un objet de mapping
 * <h5>Exemple d'utilisation. Cette partie doit se trouver au début du jsp : </h5>
 * 
 * <pre>
 *      BonDeCommandeFille a = new BonDeCommandeFille();
        String listeCrt[] = {"id","idbc","unite"};
        String listeInt[] = {};
        String libEntete[] = {"id","produit","quantite","unite","pu","idbc"};

        PageRechercheUpdate pr = new PageRechercheUpdate(a, request, listeCrt, listeInt, 3, libEntete, "id");

        pr.setUtilisateur((user.UserEJB) session.getValue("u"));
        pr.setLien((String) session.getValue("lien"));
        pr.setApres("stock/bc/bon-commande-modif-multiple.jsp");
        pr.getFormuLigne().getChamp("idbc").setAutre("readonly");
        pr.getFormuLigne().getChamp("unite").setAutre("readonly");
 * </pre>
 * 
 * <h5>Ensuite sur le corps du jsp où on veut mettre la barre de recherche, on va imprimer </h5>
 * 
 * <pre>
 * String libEnteteAffiche[] = {"id","Produit","Quantite","Unit�","PU","BC"};
    pr.getTableau().setLibelleAffiche(libEnteteAffiche);
    out.println(pr.getTableau().getHtmlWithCheckboxUpdateMultiple());
 * </pre>
 * 
 * @author tahina
 */
public class PageRechercheUpdate extends PageRecherche {
    Formulaire formuLigne;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Formulaire getFormuLigne() {
        return formuLigne;
    }

    public void setFormuLigne(Formulaire formuLigne) {
        this.formuLigne = formuLigne;
    }
    

    /**
     * Création de formulaire
     * @param tabAffiche liste des noms des colonnes à afficher
     * @throws Exception
     */
    public  void creerFormuLigne(String[]tabAffiche)throws Exception
    {
        formuLigne=new Formulaire();
        formuLigne.setObjet(getBase());
        formuLigne.makeChampFormuLigne(tabAffiche);
    }
    
    /**
     * Création de la formulaire de recherche
     * @param o objet de mapping
     * @param req contexte HTTP de la page
     * @param vrt liste des champs de critère pour le formulaire de recherche
     * @param listInterval liste des champs à prendre en tant qu'intervalle
     * @param nbRange nombre de rangés dans le formulaire de recherche
     * @param tabAff colonne par défaut à afficher pour le tableau
     * @param ids
     * @throws Exception
     */
    public PageRechercheUpdate(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] tabAff,String ids) throws Exception {
        super(o,req,vrt,listInterval,nbRange,tabAff);
        creerFormuLigne(tabAff);
        setId(ids);
    }


    /**
     * Génerer la table de valeur selon colonnes d'entête
     * @param libEnteteDefaut liste des colonnes en tant qu'entête si
     * le formulaire de recherche n'a pas specifié les colonnes à afficher
     * @param colSom colonne à sommer 
     */
    public void creerObjetPage(String libEnteteDefaut[], String[] colSom) throws Exception
    {
        super.creerObjetPage(libEnteteDefaut, colSom);
        this.getTableau().setIds(getId());
        getTableau().setFormu(getFormuLigne());
        
    }
}
