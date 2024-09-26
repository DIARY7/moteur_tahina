package affichage;

/**
 * @deprecated 
 * aucune utilisation
 * Permet de génerer un lien à partir du lien de page, nom de l'argument et valeur de l'argument
 * 
 * @author BICI
 * @version 1.0
 */

public class Lien {

  private String page;
  private String nomArgument;
  private String valeurArg;
  private String html;
  private String apresLien;
  public Lien() {
  }
  public Lien(String p,String nomArg,String valA)
  {
    setPage(p);
    setNomArgument(nomArg);
    setValeurArg(valA);
  }
  public String getHtml(String valeurAffiche)
  {
    String retour="";
    if(valeurAffiche==null || valeurAffiche.compareToIgnoreCase("")==0)
      valeurAffiche=getValeurArg();
    retour="<a href="+getPage()+"&"+getNomArgument()+"="+getValeurArg()+getApresLien()+">";
    retour+=valeurAffiche;
    retour+="</a>";
    return retour;
  }
  public String getPage() {
    return page;
  }
  public void setPage(String page) {
    this.page = page;
  }
  public void setNomArgument(String nomArgument) {
    this.nomArgument = nomArgument;
  }
  public String getNomArgument() {
    return nomArgument;
  }
  public void setValeurArg(String valeurArg) {
    this.valeurArg = valeurArg;
  }
  public String getValeurArg() {
    return valeurArg;
  }
  public void setHtml(String html) {
    this.html = html;
  }
  public String getHtml() {
    return html;
  }
  public void setApresLien(String apresLien) {
    this.apresLien = apresLien;
  }
  public String getApresLien() {
    return apresLien;
  }
}