package affichage;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @deprecated
 */

public class ListeAffichage{
HttpServletRequest request;
  private String[] listeFiltre;
  private String[] valeurFiltre;
  private int nbFiltre;
  public ListeAffichage(HttpServletRequest req,String[] listeF) {
    request=req;
    setListeFiltre(listeF);
  }
  public String[] getListeFiltre() {
    return listeFiltre;
  }
  public void setListeFiltre(String[] listeFiltre) {
    this.listeFiltre = listeFiltre;
  }
  public void setValeurFiltre(String[] valeurFiltre) {
    this.valeurFiltre = valeurFiltre;
  }
  public String[] getValeurFiltre() {
    return valeurFiltre;
  }
  public void setNbFiltre(int nbFiltre) {
    this.nbFiltre = nbFiltre;
  }
  public int getNbFiltre() {
    return nbFiltre;
  }
  public void getValeur()
  {
    for(int i=0;i<listeFiltre.length;i++)
    {
      String temp=request.getParameter(listeFiltre[i]);
      if((temp!=null)&&temp.compareToIgnoreCase("")!=0)
      valeurFiltre[i]=temp;
    }
  }
}