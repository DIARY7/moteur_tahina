package mg.cnaps.messagecommunication;

import bean.ClassMAPTable;
import java.sql.Connection;
import java.sql.Timestamp;
import utilitaire.Utilitaire;

/**
 *
 * @author NERD
 */
public class Message extends ClassMAPTable{
    private String id;
    private String idconversation;
    private String sender;
    private String receiver;
    private String msg;
    private Timestamp dateheure;
    private String statut;
    
    public Message() {
        super.setNomTable("message");
    }
    
    public String getTuppleID() {
        return id;
    }
    
    public String getAttributIDName() {
        return "id";
    }
    
    public void construirePK(Connection c) throws Exception {
        this.preparePk("MSG", "getSeqmessage");
        this.setId(makePK(c));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return Utilitaire.champNull(sender);
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return Utilitaire.champNull(msg);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatut() {
        return Utilitaire.champNull(statut);
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }    

    public Timestamp getDateheure() {
        return dateheure;
    }

    public void setDateheure(Timestamp dateheure) {
        this.dateheure = dateheure;
    }

    public String getIdconversation() {
        return Utilitaire.champNull(idconversation);
    }

    public void setIdconversation(String idconversation) {
        this.idconversation = idconversation;
    }

    public String getReceiver() {
        return Utilitaire.champNull(receiver);
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
}
