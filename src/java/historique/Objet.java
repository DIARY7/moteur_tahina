package historique;

import bean.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Objet extends ClassMAPTable {

  public String objet;
  public String desce;
  public Objet(String objete,String desc) {
//    this.setObjet(objete);
    if (objete.compareTo("")==0 || objete==null){
      this.setObjet("-");
    }
    else this.setObjet(objete);

//    this.setDesc(desc);
    if (desc.compareTo("")==0 || desc==null) {
      this.setDesc("-");
    }
    else this.setDesc(desc);

  }
  public String getAttributIDName() {
    return "objet";
  }
  public String getTuppleID() {
    return objet;
  }
  public void setObjet(String objet) {
    this.objet = objet;
  }
  public String getObjet() {
    return objet;
  }
  public void setDesc(String desc) {
    if (desc.compareTo("")==0 || desc==null) {
      this.desce ="-";
    }
    else this.desce = desc;
  }
  public String getDesc() {
    return desce;
  }
}