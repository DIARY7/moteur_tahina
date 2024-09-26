
package bean;
import bean.CGenUtil;
/**
 *  Objet de mapping générique pour les tables de catégorie ou de type simple.
 * Ci-dessous un snippet de code pour accéder à une table de type.
 * <pre>
 * <pre>{@code
 *  
 *    TypeObjet critere = new TypeObjet();
 *   critere.setNomTable("categorieproduit");
 *   critere.setId("CAT0001");
 *   TypeObjet[] reponses = (TypeObjet[]) CGenUtil.rechercher(critere,null,null,"");
 *   System.out.println("id de la categorie"+reponses[0].getId());
 *   System.out.println("valeur de la categorie"+reponses[0].getVal());
 * }
 * </pre>
 * 
 * </pre>
 * Au cas où on veut ajouter d'autres champs à cette classe. Il faudrait l'hériter et ajouter les attributs voulus.
 * @author BICI
 */
public class TypeObjet extends ClassMAPTable
{

    public TypeObjet(String nomTable, String nomProcedure, String suff, String vale, String desc)
    {
        setNomTable(nomTable);
        setNomProcedureSequence(nomProcedure);
        setIndicePk(suff);
        id = makePK();
        val = vale;
        setDesce(desc);
    }

    public TypeObjet(String nomTable, String ide, String vale, String desc)
    {
        setNomTable(nomTable);
        id = ide;
        val = vale;
        setDesce(desc);
    }

    public TypeObjet(String ide, String vale, String desc)
    {
        id = ide;
        val = vale;
        setDesce(desc);
    }
    /**
     * Constructeur avec nom de la table
     * @param nt : nom de la table
     */
    public TypeObjet(String nt)
    {
      setNomTable(nt);
    }
    public TypeObjet()
    {

    }

    public String getAttributIDName()
    {
        return "id";
    }

    public String getTuppleID()
    {
        return id;
    }

    public void setVal(String val)
    {
        this.val = val;
    }
    /**
     * @return la valeur du type/categorie
     */
    public String getVal()
    {
        return val;
    }

    public void setDesce(String desc)
    {
        if(desc == null)
            desce = "-";
        else
            desce = desc;
    }

    public String getDesce()
    {
        return desce;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * 
     * id de la catégorie
     */
    public String getId()
    {
        return id;
    }

    public String val;
    public String desce;
    public String id;
    /**
     * @deprecated fais la même chose que getVal
     * @return la valeur du type/categorie
     */
    public String getValColLibelle() {
        return val;
    }
    /**
     *  rechercher toutes les categories
     * @param nom_Table nom de la table
     * @return liste de toutes les categories
     */
    public static TypeObjet[] get(String nom_Table){
        try{
            TypeObjet to = new TypeObjet();
            to.setNomTable(nom_Table);
            TypeObjet[] liste = (TypeObjet[])CGenUtil.rechercher(to, null, null, "");
            return liste;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
