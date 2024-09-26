/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilisateur;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 * @deprecated
 * Permet de voir les types d'action possible.
 * cette classe peut être contournée avec {@link bean.TypeObjet}
 * @author Jetta
 */
public class Action extends ClassMAPTable{

    private String id;
    private String val;
    private String desce;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesce() {
        return desce;
    }

    public void setDesce(String desce) {
        this.desce = desce;
    }
    @Override
    public void construirePK(Connection c) throws Exception {
        super.setNomTable("action");
        this.preparePk("ACT", "SEQ_ACT");
        this.setId(makePK(c));
    }
    @Override
    public String getTuppleID() {
        return this.getId();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    
}
