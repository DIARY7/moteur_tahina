package affichage;

import bean.CGenUtil;
import bean.ClassMAPTable;

/**
 * Classe representant une liste multiple en affichage va génerer un:
 * <pre>{@code
 * <select name="nom" multiple>
 * 		<option></option>
 * 	</select>
 * }
 * </pre>
 * 
 * <p>
 * 	On l'utilise dans les JSP comme ci:
 * 	<pre>
 * 		ListeMultiple[] listes = new ListeMultiple[1];
 * 		TypeObjet categorie = new TypeObjet();
 * 		categorie.setNomTable("categorie");
 * 		liste[0] = new ListeMultiple("categorieId",categorie,"val","id");
 * 		pi.getFormu().changerEnChamp(listes);
 *  </pre>
 * </p>
 * @author BICI 
 */
public class ListeMultiple extends Liste{
    private String javascript;

	/**
	 * Constructeur  
	 * @param nom nom du champs 
	 * @param type Classe de mapping à la base de données
	 * @param colAff valeur à afficher 
	 * @param colVal valeur dans la base
	 */
	public ListeMultiple(String nom, ClassMAPTable type, String colAff, String colVal){
		setNom(nom);
		setSingleton(type);
		setColAffiche(colAff);
		setColValeur(colVal);
	}

	/**
	 * Constructeur 
	 * @param nom nom du champs 
	 * @param ajax code javascript à mettre sur le html à la generation(inline javascript)
	 * @param type Classe de mapping à la base de données
	 * @param colAff valeur à afficher 
	 * @param colVal valeur dans la base
	 */
	public ListeMultiple(String nom, String ajax, ClassMAPTable type, String colAff, String colVal){
		setNom(nom);
		setSingleton(type);
		setColAffiche(colAff);
		setColValeur(colVal);
		setJavascript(ajax);
	}
	

	public String getJavascript(){
		return this.javascript;
	}

	/**
	 * @param javascript code javascript
	 */
	public void setJavascript(String javascript){
		this.javascript = javascript;
	}
    
	/**
	* Fonction sert à générer un select avec html
	* @throws Exception
	 */
	public void makeHtml()throws Exception{
		String temp="";
		temp="<select name="+getNom()+" class="+getCss()+" id="+getNom()+" "+getJavascript()+" "+getAutre()+"multiple=\"multiple\">";
		if(this.getBase()==null||this.getBase().length==0)
		{
		  if( getValeurBrute()!=null && getValeurBrute().length>0)
		  temp=makeHtmlValeur(temp);
		}
		else
		{
		  for(int i=0;i<this.getBase().length;i++)
		  {
			String champValeur=String.valueOf(CGenUtil.getValeurFieldByMethod((getBase()[i]),getColValeur()));
			String champAffiche=String.valueOf(CGenUtil.getValeurFieldByMethod((getBase()[i]),getColAffiche()));
			String selected="";
			if((getValeur()==null || getValeur().compareToIgnoreCase("")==0)&&getDefaultSelected()!=null)
			{
			  if(champValeur.compareToIgnoreCase(getDefaultSelected())==0)selected="selected";
			}
			else if(champValeur.compareToIgnoreCase(getValeur())==0)
			{
			  selected="selected";
			}
			temp+="<option value='"+champValeur+"' "+selected+">"+champAffiche+"</option>";
		  }
		}
		temp+="</select>";
		setHtml(temp);
	}
}