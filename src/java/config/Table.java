/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import bean.ClassMAPTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import utilitaire.UtilDB;

/**
 *
 * @author Jetta
 */
public class Table extends ClassMAPTable {

    private String table_name;

    public Table() {
        this.setNomTable("table");
    }

    public Table(String name) {
        this.setNomTable("table");
        this.table_name = name;
    }

    @Override
    public String getTuppleID() {
        return null;
    }

    @Override
    public String getAttributIDName() {
        return null;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Table[] getListeTable(String role) throws SQLException, Exception {
        Connection con = new UtilDB().GetConn();
        try {
            String req = "SELECT * FROM matable where 1<2 and  table_name not in(select r.nomtable from restriction r where r.idrole='" + role + "')";
            System.out.print(req);
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(req);
            Vector v = new Vector();
            while (res.next()) {
                Table st = new Table();
                st.setTable_name(res.getString(1));
                v.add(st);
            }
            Table[] valin = new Table[v.size()];
            v.copyInto(valin);
            return valin;
        } catch (Exception e) {
            throw e;
        }
         finally{
            if(con!=null)
              con.close();
          }
    }
     public Table[] getListeTable(String role,String adr) throws SQLException, Exception {
        Connection con = new UtilDB().GetConn();

         try {
           String req = "SELECT * FROM matable where 1<2 and  table_name not in(select r.tablename from restriction r where r.idrole='" + role + "' and r.iddirection='"+adr+")";
           
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(req);
            Vector v = new Vector();
            while (res.next()) {
                Table st = new Table();
                st.setTable_name(res.getString(1));
                v.add(st);
            }
            Table[] valin = new Table[v.size()];
            v.copyInto(valin);
            return valin;
        } catch (Exception e) {
            throw e;
        }
         finally{
             if(con!=null)con.close();
         }
    }
}
