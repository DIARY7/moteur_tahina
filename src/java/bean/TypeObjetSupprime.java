package bean;

/**
 * @deprecated
 * Classe à utiliser pour garder l'historique des objets supprimés. 
 * Pour plus d'option veuillez utiliser {@link historique.MapHistorique}
 */

public class TypeObjetSupprime extends ClassMAPTable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -253144357449988256L;
public java.lang.String ID;
   public java.lang.String HEURE;
   public java.lang.String RMQ;

   /*FK*/
  public java.lang.String IDOBJET;
  public java.sql.Date DATY;
  public java.lang.String IDUTILISATEUR;

/*Utile pour l'entree dans la BDD*/
  public TypeObjetSupprime(String nomtable,String nomProcedure,String suff,String idobjet,String iduser,java.sql.Date daty, String heure,String rmq) {
    this.setNomTable(nomtable);
    this.setNomProcedureSequence(nomProcedure);
    this.setIndicePk(suff);
    ID=this.makePK();

    this.setIDOBJET(idobjet);
    this.setIDUTILISATEUR(iduser);
    this.setDATY(daty);
    this.setHEURE(heure);
    this.setRMQ(rmq);
  }

  public TypeObjetSupprime(String nomtable,String id,String idobjet,String iduser,java.sql.Date daty, String heure,String rmq) {
    this.setID(id);
    this.setNomTable(nomtable);
    this.setIDOBJET(idobjet);
    this.setIDUTILISATEUR(iduser);
    this.setDATY(daty);
    this.setHEURE(heure);
    this.setRMQ(rmq);
  }

/*Utile pour la sortie vers la base de donnees*/
  public TypeObjetSupprime(String id,String idobjet,String iduser,java.sql.Date daty, String rmq,String utilBDD) {
    this.setID(id);
    this.setIDOBJET(idobjet);
    this.setIDUTILISATEUR(iduser);
    this.setDATY(daty);
    this.setRMQ(rmq);
  }


  public String getAttributIDName() {
    return "ID";
  }
  public String getTuppleID() {
    return ID;
  }
  public void setDATY(java.sql.Date DATY) {
    if (String.valueOf(DATY).compareTo("")==0 || DATY==null){
      this.DATY=utilitaire.Utilitaire.dateDuJourSql();
    }
    else this.DATY= DATY;
  }

  public void setID(String ID) {
    this.ID = ID;
  }
  public void setIDOBJET(String IDOBJET) {
    this.IDOBJET = IDOBJET;
  }
  public void setIDUTILISATEUR(String IDUTILISATEUR) {
    this.IDUTILISATEUR = IDUTILISATEUR;
  }
  public void setRMQ(String RMQ) {
    if (RMQ==null || String.valueOf(RMQ).compareTo("")==0){
      this.RMQ="-";
    }
    else this.RMQ= RMQ;
  }
  public String getRMQ() {
    return RMQ;
  }
  public String getIDUTILISATEUR() {
    return IDUTILISATEUR;
  }
  public String getIDOBJET() {
    return IDOBJET;
  }
  public String getID() {
    return ID;
  }
  public String getHEURE() {
    return HEURE;
  }
  public java.sql.Date getDATY() {
    return DATY;
  }
  public void setHEURE(String HEURE) {
    if (String.valueOf(HEURE).compareTo("")==0 || DATY==null){
      this.HEURE=utilitaire.Utilitaire.heureCourante();
    }
    else this.HEURE= HEURE;
  }
}
