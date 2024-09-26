package bean;

public class TypeObjetClasse extends ClassMAPTable {
  /**
	 * 
	 */

  public String val;
  public String desce;
  public String id;

  public String idParent;
/*Utile pour l'entree dans la base de donnes*/
  public TypeObjetClasse(String nomTable,String nomProcedure,String suff,String vale,String desc,String idParent) {
    this.setNomTable(nomTable);
    this.setNomProcedureSequence(nomProcedure);
    this.setIdParent(idParent);
    this.setIndicePk(suff);
    this.id=this.makePK();
    this.val=vale;
    this.setDesce(desc);
  }
  public TypeObjetClasse(String nomTable,String ide,String vale,String desc,String idParent) {
    this.setNomTable(nomTable);
    this.setId(ide);
    this.setVal(vale);
    this.setDesce(desc);
    this.setIdParent(idParent);
  }
  /*Utile pour la sortie vers la base de donnes*/
  public TypeObjetClasse(String ide,String vale,String desc,String idParent) {
    id=ide;
    val=vale;
    this.setDesce(desc);
    this.setIdParent(idParent);
  }

  public TypeObjetClasse(){

  }


  public String getIdParent() {
    return idParent;
  }
  public void setIdParent(String idParent) {
    this.idParent = idParent;
  }
  public String getAttributIDName() {
    return "id";
  }
  public String getTuppleID() {
    return id;
  }
  public void setVal(String val) {
    this.val = val;
  }
  public String getVal() {
    return val;
  }
  public void setDesce(String desc) {
    if(desc == null || desc.compareTo("")==0 ){
      desce = "-";
    }else{
      desce=desc;
    }
  }
  public String getDesce() {
    return desce;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return id;
  }
}
