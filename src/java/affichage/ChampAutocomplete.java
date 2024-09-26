package affichage;

import constante.ConstanteAffichage;
/**
 * @deprecated
 * Represente un champ autocomplete. 
 * Les logiques d'autocomplete sont plus complets dans Champ.
 * 
 * @author BICI
 */
public class ChampAutocomplete extends Champ{
	private String idsuggestion; //id élément html pour accueillir le resultat 
	private String idliste; //id de la listebox pour afficher les suggestions
	private String colonne; // colonne à afficher à filtrer
	private String nomclasse; // nom de la classe de mappage
        private String javascript;
	
	/**CONSTRUCTORS**/
	public ChampAutocomplete(String nom, String colonne, String nomclasse){
		setNom(nom);
		setIdSuggestion("sugg"+nom);
		setIdListe("liste"+nom);
		setColonne(colonne);
		setNomClasse(nomclasse);
                String javascript = "onkeyup=\"showSuggestion(";
		javascript += "'"+nom+"', ";
		javascript += "'"+colonne+"',";
		javascript += "'"+nomclasse+"'";
		javascript += ")\"";
                javascript += " onkeypress=\"moveToList(event, 'liste"+nom+"')\"";
                setJavascript(javascript);
	}
	public ChampAutocomplete(String nom, String javascript, String idsuggestion, String idliste, String colonne, String nomclasse){
            setNom(nom);
            setJavascript(javascript);
            setIdSuggestion(idsuggestion);
            setIdListe(idliste);
            setColonne(colonne);
            setNomClasse(nomclasse);
	}
	/**GETTERS**/
	public String getIdSuggestion(){
		return this.idsuggestion;
	}
	public String getIdListe(){
		return this.idliste;
	}
	public String getColonne(){
		return this.colonne;
	}
	public String getNomClasse(){
		return this.nomclasse;
	}
        public String getJavascript(){
            return this.javascript;
        }
	/**SETTERS**/
	public void setIdSuggestion(String idsuggestion){
		this.idsuggestion = idsuggestion;
	}
	public void setIdListe(String idliste){
		this.idliste = idliste;
	}
	public void setColonne(String colonne){
		this.colonne = colonne;
	}
	public void setNomClasse(String nomclasse){
		this.nomclasse = nomclasse;
	}
        public void setJavascript(String javascript){
            this.javascript = javascript;
        }
	/**FONCTIONS**/		
	public void makeHtml() throws Exception{
		String temp="";
		if(getType().compareTo(ConstanteAffichage.textarea)==0)
		{
		  temp="<textarea name="+this.getNom()+" class="+this.getCss()+"id="+getNom()+"maxlength="+getTaille()+" "+getJavascript()+" "+getAutre()+">"+getValeur()+"</textarea>"; //affichage avec écouteur évenement
		}
		else
		{
		  temp="<input name="+getNom()+" type="+getType()+" class="+getCss()+"  id="+getNom()+" value=\""+getValeur()+"\" "+getJavascript()+" "+getAutre()+">"; //affichage avec écouteur évenement 
		}	
		temp += "<div id=\""+getIdSuggestion()+"\"></div>"; //div pour accueillir le resultat
		setHtml(temp);
	}
	
	public static String makeSelectionEvent(String idchamp){
		String javascript = "onclick=\"selection(";
		javascript += "'"+idchamp+"'";
		javascript += ")\"";
                javascript += "onkeydown=\"selectionEnter(event, '"+idchamp+"')\"";
		return javascript;
	}
}