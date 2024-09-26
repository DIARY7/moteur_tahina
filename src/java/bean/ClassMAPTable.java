// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ClassMAPTable.java
package bean;

import static bean.CGenUtil.getValeurFieldByMethod;
import historique.Historique_valeur;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Vector;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import utilitaire.*;
import java.lang.reflect.*;
import historique.MapHistorique;

// Referenced classes of package bean:
//            ClassMAPTableException, ErreurDAO
public abstract class ClassMAPTable implements Serializable {
    
    //public static final boolean isMirroring = false;
    public static final String histoSync = "REQUETE_A_ENVOYER";
    String nomChampDatyDuplique="daty";
    String nomChampTotalPrev=null;
    String sensPrev="credit";
    public String getNomChampTotalPrev() {
        return nomChampTotalPrev;
    }

    public void setNomChampTotalPrev(String nomChampTotalPrev) {
        this.nomChampTotalPrev = nomChampTotalPrev;
    }

    public String getSensPrev() {
        return sensPrev;
    }

    public void setSensPrev(String sensPrev) {
        this.sensPrev = sensPrev;
    }
    

    public String getNomChampDatyDuplique() {
        return nomChampDatyDuplique;
    }

    public void setNomChampDatyDuplique(String nomChampDatyDuplique) {
        this.nomChampDatyDuplique = nomChampDatyDuplique;
    }
    
    public boolean isSynchro()
    {
        return false;
    }
    
    public void appendCsv(CSVPrinter printer, String[] entete) throws Exception{
        printer.printRecord(CGenUtil.getValFromEntete(this, entete));
    }

    public void appendCsv(String filename, String[] entete) throws Exception{
        try (FileWriter fileWriter = new FileWriter(filename,true);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            this.appendCsv(csvPrinter, entete);
        }
    }

    public void saveCsv(CSVPrinter printer,String[] entetesAffiche, String[] entete) throws Exception{
        printer.printRecord((Object[])entetesAffiche);
        printer.printRecord(CGenUtil.getValFromEntete(this, entete));
    }

    public void saveCsv(String filename,String[] entetesAffiche, String[] entete) throws Exception{
        try (FileWriter fileWriter = new FileWriter(filename,false);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            this.saveCsv(csvPrinter,entetesAffiche, entete);
        }
    }

    public static void appendCsv(ClassMAPTable[] objects, String filename, String[] entete) throws Exception{
        try (FileWriter fileWriter = new FileWriter(filename,true);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
                for(int i=0;i<objects.length;i++){
                        objects[i].appendCsv(csvPrinter, entete);
                }
        }
    }
    public static void saveCsv(ClassMAPTable[] objects, String filename, String[] entetesAffiche, String[] entete)throws Exception{
        try (FileWriter fileWriter = new FileWriter(filename,false);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
                for(int i=0;i<objects.length;i++){
                    if(i==0){
                        objects[i].saveCsv(csvPrinter, entetesAffiche, entete);
                    }else{
                        objects[i].appendCsv(csvPrinter, entete);
                    }
                }
        }
    }



    public ClassMAPTable dupliquer(String user,Connection c) throws Exception
    {
        boolean estOuvert=false;
        try{
            if(c==null)
            {
                estOuvert=true;
                c=new UtilDB().GetConn();
            }
            ClassMAPTable retour=(ClassMAPTable)Class.forName(this.getClassName()).newInstance();
            Field[] listeC=this.getFieldList();
            for(Field f:listeC)
            {
                CGenUtil.setValChamp(retour, f, CGenUtil.getValeurFieldByMethod(this, f) );
            }
            retour.construirePK(c);
            return retour;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(c!=null && estOuvert==true)c.close();
        }
    }
    
    
    public ClassMAPTable dupliquer(int freqMois,int qte,String user,Connection c) throws Exception
    {
        return null;
    }
    
    public ClassMAPTable createObject(String u,Connection c)throws Exception
    {
        controler(c);
        if (getTuppleID()==null||getTuppleID().compareToIgnoreCase("")==0||getTuppleID().compareToIgnoreCase("0")==0)construirePK(c);
        insertToTableWithHisto(u, c);
        return this;
    }
    public ClassMAPTable getById(String id,String nTable,Connection c) throws Exception
    {
        boolean estOuvert=false;
        try
        {
            if(nTable!=null)this.setNomTable(nTable);
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            this.setTuppleId(id);
            ClassMAPTable[] valiny= (ClassMAPTable[]) CGenUtil.rechercher(this, null, null, c, "");
            if(valiny.length>0)return valiny[0];
            return null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(c!=null&&estOuvert==true)c.rollback();
            throw e;
        }
        finally
        {
            if(c!=null&&estOuvert==true)c.close();
        }
        
    }

    public ClassMAPTable() {
        INDICE_PK = "EX";
        nomProcedureSequence = "GetSeqExecutions";      
        longuerClePrimaire = 6;
        setNombreChamp();
    }

    public String makePK(Connection c) throws Exception {
        int maxSeq = Utilitaire.getMaxSeq(nomProcedureSequence, c);
        String nombre = Utilitaire.completerInt(longuerClePrimaire, maxSeq);
        if (isSynchro()) {
            return String.valueOf(INDICE_PK) + String.valueOf(nombre) + Utilitaire.heureCouranteHMSSansSeparateur();
        } else {
            return String.valueOf(INDICE_PK) + String.valueOf(nombre);
        }
        //return String.valueOf(INDICE_PK) + String.valueOf(nombre);
    }
    
    public void construirePK(Connection c) throws Exception {
    }

    public void controler(Connection c) throws Exception {

    }
    
    public void controlerCloture(Connection c) throws Exception {

    }

    public void controlerUpdate(Connection c) throws Exception {

    }
    public void setTuppleId(String val)throws Exception
    {
        this.setValChamp(this.getAttributIDName(), val);
    }

    public String makePK() {
        int maxSeq = Utilitaire.getMaxSeq(nomProcedureSequence);
        String nombre = Utilitaire.completerInt(longuerClePrimaire, maxSeq);
        if (isSynchro()) {
                return String.valueOf(INDICE_PK) + String.valueOf(nombre) + Utilitaire.heureCouranteHMSSansSeparateur();
        } else {
                return String.valueOf(INDICE_PK) + String.valueOf(nombre);
        }
        //return String.valueOf(INDICE_PK) + String.valueOf(nombre);
    }
    
    public String makePK(String indicePK, String fonct) throws Exception {
        this.preparePk(indicePK, fonct);
        return makePK();
    }

    public String makePKCFin(String daty) {
        int maxSeq = getMaxColonneFactFin(daty) + 1;
        String nombre = Utilitaire.completerInt(longuerClePrimaire, maxSeq);
        return String.valueOf(INDICE_PK) + String.valueOf(nombre);
    }

    public String makePKCFinEntite(String daty, String entite) {
        int maxSeq = getMaxColonneFactFinEntite(daty, entite) + 1;
        String nombre = Utilitaire.completerInt(longuerClePrimaire, maxSeq);
        return String.valueOf(INDICE_PK) + String.valueOf(nombre);
    }

    public void setIndicePk(String indice) {
        INDICE_PK = indice;
    }

    public void setNomProcedureSequence(String seq) {
        nomProcedureSequence = seq;
    }

    public void setLonguerClePrimaire(int longueur) {
        if (longueur > 0) {
            longuerClePrimaire = longueur;
        }
    }

    public String getNomTable() {
        if (nomTable == null) {
            if (this.nomTableSelect == null) {
                return this.getClassName();
            }
            return nomTableSelect.trim();
        }
        return nomTable.trim();
    }

    public void setNomTable(String table) {
        if (table != null && table.compareTo("") != 0) {
            nomTable = table;
        }
    }

    public abstract String getTuppleID();

    public abstract String getAttributIDName();

    public String getClassName() {
        return getClass().getName();
    }

    public String getValColLibelle(){
        return columnlibelle;
    }
    
    public void setValColLibelle(String columnlib){
        this.columnlibelle = columnlib;
    }
    
    public Field[] getFieldList() throws Exception {
        /*try {
         Class cls = getClass();
         Field retour[] = new Field[nombreChamp];
         Field fieldlist[] = cls.getDeclaredFields();
         if (cls.getSuperclass().getName().endsWith("ClassEtat") == true) {
         retour = new Field[nombreChamp + 2];
         int i = 0;
         for (i = 0; i < nombreChamp; i++) {
         retour[i] = fieldlist[i];
         }
         retour[i] = cls.getSuperclass().getDeclaredField("etat");
         retour[i+1] = cls.getSuperclass().getSuperclass().getDeclaredField("iduser");
         }
         if (cls.getSuperclass().getName().endsWith("ClassUser") == true) {
         retour = new Field[nombreChamp + 1];
         int i = 0;
         for (i = 0; i < nombreChamp; i++) {
         retour[i] = fieldlist[i];
         }
         retour[i] = cls.getSuperclass().getDeclaredField("iduser");
         }
         else{
         for (int i = 0; i < nombreChamp; i++) {
         retour[i] = fieldlist[i];
         }
         }
         return retour;
         } catch (Exception e) {
         throw new ClassMAPTableException(e.getMessage());
         }*/
        return ListeColonneTable.getFieldListeHeritage(this);
    }

    public void setNombreChamp() {
        Class cls = getClass();
        Field fieldlist[] = cls.getDeclaredFields();
        nombreChamp = fieldlist.length;
    }

    public void setNombreChamp(int nouveau) {
        nombreChamp = nouveau;
    }

    public void setFieldList() {
        try {
            cls = getClass();
            Field fieldlist[] = cls.getDeclaredFields();
            for (int i = 0; i < fieldlist.length; i++) {
                champ.add(i, fieldlist[i]);
            }

            nombreChamp = fieldlist.length;
        } catch (Exception e) {
            //System.out.println("SETFIELDLIST ERREUR".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    public void setFieldList(Field a) {
        try {
            champ.add(nombreChamp, a);
            nombreChamp++;
        } catch (Exception e) {
            //System.out.println("SETFIELDLIST ERREUR".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    protected int getTypeMAPField(Field fld) {
        String nomClasse = fld.getType().getName();
        if (nomClasse.equals("boolean")) {
            return 0;
        }
        if (nomClasse.equals("byte")) {
            return 1;
        }
        if (nomClasse.equals("short")) {
            return 2;
        }
        if (nomClasse.equals("int")) {
            return 3;
        }
        if (nomClasse.equals("long")) {
            return 4;
        }
        if (nomClasse.equals("float")) {
            return 5;
        }
        if (nomClasse.equals("Real")) {
            return 5;
        }
        if (nomClasse.equals("double")) {
            return 6;
        }
        if (nomClasse.equals("java.lang.String")) {
            return 10;
        }
        if (nomClasse.equals("java.sql.Date")) {
            return 21;
        }
        if (nomClasse.equals("java.sql.Time")) {
            return 22;
        }
        if (nomClasse.equals("java.sql.Blob")) {
            return 31;
        }
        if (nomClasse.equals("java.sql.Clob")) {
            return 32;
        }
        if (nomClasse.equals("java.lang.Number")) {
            return 34;
        }
        if (nomClasse.equals("java.sql.Timestamp")) {
            return 35;
        }
        return !nomClasse.equals("java.lang.Integer") ? -1 : 33;
    }
    

    public int deleteToTableWithHisto(String refUser, Connection con) throws Exception {
        try {
            String temps = refUser;
            if(refUser!=null && refUser.contains("/")){
                String[] g = Utilitaire.split(refUser, "/");
                refUser= g[0];
            }
            controlerDelete(con);
            MapHistorique histo = new MapHistorique(this.getNomTable(), "Suppression par "+temps, refUser, this.getTuppleID());
            //System.out.println(" ClassMapTable.deleteToTableWithHisto 211" );
            histo.setObjet(this.getClassName());

            ClassMAPTable obj = (ClassMAPTable) (Class.forName(this.getClassName()).newInstance());
            obj.setNomTable(this.getNomTable());
            if (obj != null) {
                //System.out.println("BREAKPOINT DELETEHISTO = " + obj.getNomTable());
               /* ClassMAPTable[] tabObj = (ClassMAPTable[]) CGenUtil.rechercher(obj, null, null, con, " and " + obj.getAttributIDName() + " = '" + histo.getRefObjet() + "'");
                Historique_valeur valeur = new Historique_valeur();
                if (tabObj.length != 0) {
                    Field[] listeField = this.getFieldList();
                    for (int i = 0; i < listeField.length; i++) {
//                    String nomVal = "setVal"+i;
                        int temp = i + 1;
//                    String get = "get"+Utilitaire.convertDebutMajuscule(listeField[i].getName());
//                        //System.out.println(listeField[i].getName() + " : "+ tabObj[0].getValInsert(listeField[i].getName()));
                        valeur.setValChamp("val" + temp, listeField[i].getName() + ":" + tabObj[0].getValInsert(listeField[i].getName()));
                    }
                    valeur.setIdhisto(histo.getIdHistorique());
                    valeur.setRefhisto(histo.getRefObjet());
                    valeur.setNom_table(this.getValInsert("nomTable"));
                    valeur.setNom_classe(this.getClassName());
                    valeur.construirePK(con);
                    //valeur.insertToTable(con);
                }*/
            }

            int r = this.deleteToTable(con);
            if (r >= 1) {
                histo.insertToTable(con);
                return r;
            }
            //System.out.println(" Apres insert to table 214" );
        } catch (Exception ex) {
            if( con != null ){con.rollback();}
            /// //System.out.println(" ClassMapTable.deleteToTableWithHisto 221" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        return 0;
    }

    public int deleteToTableWithHisto(String refUser) throws Exception {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            con.setAutoCommit(false);
            deleteToTableWithHisto(refUser, con);
            con.commit();
            return 1;
        } catch (SQLException e) {
            throw e;
        } catch (Exception ex) {
            if( con != null ){con.rollback();}
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }

    }

    public int deleteToTable(Connection cDb) throws Exception {
        PreparedStatement stmt, insertHistoSync = null;
        String sqlQry;
        int retoure;
        String tableName = getNomTable();
        stmt = null;
        Field fld = null;
        sqlQry = "DELETE FROM " + tableName;
        retoure = 0;
        try {
            //Field fieldlist[] = getFieldList();
            sqlQry = sqlQry + " WHERE " + getAttributIDName() + " = '" + getTuppleID() + "'";
            stmt = cDb.prepareStatement(sqlQry);
            retoure = stmt.executeUpdate();
            if (isSynchro() & retoure == 1) {
                if (tableName.compareTo(histoSync) != 0 && tableName.compareToIgnoreCase("HISTORIQUE") != 0) {
                    String query = "INSERT INTO " + histoSync + " (ID,ACTION,TABLECONCERNE,REQUETE,DATY,HEURE,SERVEUR) VALUES ('RQ'||getSeqRequeteAEnvoyer,?,?,?,?,?,null)";
                    insertHistoSync = cDb.prepareStatement(query);
                    insertHistoSync.setString(1, "DELETE");
                    insertHistoSync.setString(2, tableName);
                    insertHistoSync.setString(3, sqlQry);
                    insertHistoSync.setDate(4, Utilitaire.dateDuJourSql());
                    insertHistoSync.setString(5, Utilitaire.heureCourante());
                    insertHistoSync.executeUpdate();
                }
            }
            cDb.commit();
        } catch (SQLException sqlex) {
            throw new Exception(sqlex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cDb != null) {
                    cDb.rollback();
                }
                throw new Exception("ERREUR deleteToTable " + e.getMessage());
            } catch (Exception ee) {
                throw new Exception("ERREUR SQL AVEC CONN " + ee.getMessage());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if(insertHistoSync!=null){
                    insertHistoSync.close();
                }
                //if(cDb!=null) cDb.close();

            } catch (Exception se) {
                Log.setLog(getClassName() + " - probleme de fermeture d'1 Statement ");
            }
            return retoure;
        }
    }

    public int deleteToTable(String where, Connection cDb) throws Exception {
        PreparedStatement stmt;
        String sqlQry;
        int retoure;
        String tableName = getNomTable();
        stmt = null;
        Field fld = null;
        sqlQry = "DELETE FROM " + tableName;
        retoure = 0;
        try {
            //Field fieldlist[] = getFieldList();
            sqlQry = sqlQry + " WHERE " + where;
            stmt = cDb.prepareStatement(sqlQry);
            //stmt.setString(1, getTuppleID());
//      //System.out.println("sqlQry =========== "+sqlQry + getTuppleID());
            int retour = stmt.executeUpdate();
            // //System.out.println("ClassMapTable.DeleteToTable =========== 259" + sqlQry + getTuppleID());
//            cDb.commit();
            // if (retour == 0)throw new Exception ("Erreur suppression dans la table");
            //retoure= 1;
        } catch (SQLException sqlex) {
            //System.out.println("ClassMapTable.DeleteToTable =========== 266" + sqlex.getMessage());
            throw new Exception(sqlex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cDb != null) {
                    cDb.rollback();
                }
                throw new Exception("ERREUR deleteToTable " + e.getMessage());
            } catch (Exception ee) {
                throw new Exception("ERREUR SQL AVEC CONN " + ee.getMessage());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                //if(cDb!=null) cDb.close();

            } catch (Exception se) {
                Log.setLog(getClassName() + " - probleme de fermeture d'1 Statement ");
            }
            return retoure;
        }
    }

    public int deleteToTableGroupe(Connection cDb)
            throws Exception {
        Statement stmt;
        String sqlQry;
        int retoure;
        String tableName = getNomTable();
        stmt = null;
        Field fld = null;
        sqlQry = "DELETE FROM " + tableName;
        retoure = 0;
        try {
            //Field fieldlist[] = getFieldList();
            sqlQry = sqlQry + " WHERE " + CGenUtil.makeWhere(this);
            stmt = cDb.createStatement();

            int retour = stmt.executeUpdate(sqlQry);
            cDb.commit();
            /*if (retour==0)
             throw new Exception ("Erreur suppression dans la table");*/
            //retoure= 1;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cDb != null) {
                    cDb.rollback();
                }
                throw new Exception("ERREUR deleteToTable " + e.getMessage());
            } catch (Exception ee) {
                throw new Exception("ERREUR SQL AVEC CONN " + ee.getMessage());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                //if(cDb!=null) cDb.close();

            } catch (Exception se) {
                Log.setLog(getClassName() + " - probleme de fermeture d'1 Statement ");
            }
            return retoure;
        }
    }

    public int deleteToTable() {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            try {
                c = util.GetConn();
                c.setAutoCommit(false);
                int i = deleteToTable(c);
                return i;
            } catch (Exception e) {
                if (c != null) {
                    try {
                        c.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                //System.out.println("ERREUR DELETE_TABLE ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                //util.close_connection();
            } catch (Exception exception1) {
            }
        }
    }

    public int deleteToTable(ClassMAPTable histo)
            throws ErreurDAO {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            c = util.GetConn();
            c.setAutoCommit(false);
            histo.insertToTable(c);
            int i = deleteToTable(c);
            c.commit();
            int j = 1;
            return j;
        } catch (Exception e) {
            try {
                if( c != null ){ c.rollback(); };
                throw new ErreurDAO(String.valueOf(String.valueOf((new StringBuffer("ERREUR deleteToTable ")).append(e.getMessage()).append(" ").append(e.getMessage()))));
            } catch (Exception ee) {
                throw new ErreurDAO(String.valueOf(String.valueOf((new StringBuffer("ERREUR SQL ")).append(e.getMessage()).append(" ").append(ee.getMessage()))));
            }
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                //util.close_connection();
            } catch (Exception exception1) {
            }
        }
    }

    public int updateToTable(Connection cDb)
            throws Exception {
        String tableName = getNomTable();
        PreparedStatement stmt = null, insertHistoSync = null;
        Field fld = null;
        String sqlQry = String.valueOf(String.valueOf((new StringBuffer("UPDATE ")).append(tableName).append(" SET ")));
        try {
            Field fieldlist[] = ListeColonneTable.getChampBase(this, cDb);
            //Field fieldlist[] = getFieldList();
            for (int i = 0; i < fieldlist.length; i++) {
                fld = fieldlist[i];
                sqlQry = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(sqlQry)))).append(fld.getName()).append(" = ? ")));
                if (i + 1 < fieldlist.length) {
                    sqlQry = String.valueOf(String.valueOf(sqlQry)).concat(", ");
                }
            }

            sqlQry = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(sqlQry)))).append(" WHERE ").append(getAttributIDName()).append(" = ? ")));
            //System.out.println("requette == "+sqlQry);
            stmt = cDb.prepareStatement(sqlQry);
            String enTete = "UPDATE " + tableName + " SET ";
            for (int i = 0; i < fieldlist.length; i++) {
                fld = fieldlist[i];
                //System.out.println(" ===== " + this.getValField(fld));
                String valStr = "";
                switch (getTypeMAPField(fld)) {
                    case 0: // '\0'
                        stmt.setBoolean(i + 1, ((Boolean) this.getValField(fld)).booleanValue());
                        valStr = "'" + ((Boolean) this.getValField(fld)).booleanValue() + "'";
                        break;

                    case 1: // '\001'
                        stmt.setByte(i + 1, ((Byte) this.getValField(fld)).byteValue());
                        valStr = "'" + ((Byte) this.getValField(fld)).byteValue() + "'";
                        break;

                    case 2: // '\002'
                        stmt.setShort(i + 1, ((Short) this.getValField(fld)).shortValue());
                        valStr = "'" + ((Byte) this.getValField(fld)).byteValue() + "'";
                        break;

                    case 3: // '\003'
                        Integer it = (Integer) this.getValField(fld);
                        stmt.setInt(i + 1, it.intValue());
                        valStr = "" + it.intValue() + "";
                        break;

                    case 4: // '\004'
                        Long lo = (Long) this.getValField(fld);
                        stmt.setLong(i + 1, lo.longValue());
                        valStr = "" + lo.longValue() + "";
                        break;

                    case 5: // '\005'
                        stmt.setFloat(i + 1, ((Float) this.getValField(fld)).floatValue());
                        valStr = "" + ((Float) this.getValField(fld)).floatValue() + "";
                        break;

                    case 6: // '\006'
                        stmt.setDouble(i + 1, ((Double) this.getValField(fld)).doubleValue());
                        valStr = "" + ((Double) this.getValField(fld)).doubleValue() + "";
                        break;

                    case 10: // '\n'
                        stmt.setString(i + 1, (String) this.getValField(fld));
                        valStr = "'" + (Utilitaire.champNull((String) this.getValField(fld))).replace("'", "`") + "'";
                        break;

                    case 21: // '\025'
                        stmt.setDate(i + 1, (Date) this.getValField(fld));
                        if ((Date) this.getValField(fld) != null) {
                            valStr = "'" + Utilitaire.formatterDatySql((Date) this.getValField(fld)) + "'";
                        } else {
                            valStr = "'null'";
                        }
                        break;

                    case 33: // '!'
                        stmt.setInt(i + 1, ((Integer) this.getValField(fld)).intValue());
                        break;
                    case 35: // '!'
                        stmt.setTimestamp(i + 1, ((java.sql.Timestamp) this.getValField(fld)));
                        if((java.sql.Timestamp) this.getValField(fld)!=null){
                            valStr = "TO_TIMESTAMP('" + (java.sql.Timestamp) this.getValField(fld) + "','YYYY-MM-DD HH24:MI:SS:FF')";
                        }else{
                            valStr = "null";
                        }
                        break;
                }
                valStr = (valStr.compareToIgnoreCase("'null'") == 0) ? null : valStr;
                if (i + 1 < fieldlist.length) {
                    enTete += fld.getName() + "=" + valStr + ", ";
                } else {
                    enTete += fld.getName() + "=" + valStr + " ";
                }
            }

            stmt.setString(fieldlist.length + 1, getTuppleID());
//      int retour = 1;
            int retour = stmt.executeUpdate();
            if (retour == 0) {
                throw new Exception("Erreur modification dans la table");
            }
            
            enTete += " WHERE " + getAttributIDName() + " = '" + getTuppleID() + "'";
            if (isSynchro() & retour == 1) {
                if (tableName.compareTo(histoSync) != 0 && tableName.compareToIgnoreCase("HISTORIQUE") != 0) {
                    String query = "INSERT INTO " + histoSync + " (ID,ACTION,TABLECONCERNE,REQUETE,DATY,HEURE,SERVEUR) VALUES ('RQ'||getSeqRequeteAEnvoyer,?,?,?,?,?,null)";
                    insertHistoSync = cDb.prepareCall(query);
                    insertHistoSync.setString(1, "INSERT");
                    insertHistoSync.setString(2, tableName);
                    insertHistoSync.setString(3, enTete);
                    insertHistoSync.setDate(4, Utilitaire.dateDuJourSql());
                    insertHistoSync.setString(5, Utilitaire.heureCourante());
                    //System.out.println("Je vais executer insert into REQUETE_A_ENVOYER");
                    insertHistoSync.executeUpdate();
                }   
            }
            int j = 1;
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            if (cDb != null) {
                cDb.rollback();
            }
            throw new Exception("Erreur update dans la table 4" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - probleme de fermeture d'1 Statement "));
            }
        }
    }
    public int updateToTableMultiple(String col,String valCol,String[]id,Connection c) throws Exception
    {
        String apresSet=col+"='"+valCol+"'";
        String aWhere=Utilitaire.tabToString(id,null,null);
        aWhere=" where id in("+aWhere+")";
        return updateToTable(apresSet+aWhere, c);
    }
    public int updateToTableMultiple(String col,Timestamp date,String[]id,Connection c) throws Exception
    {
        String apresSet=col+"=TO_TIMESTAMP('"+date+"','YYYY-MM-DD HH24:MI:SS:FF')";
        String aWhere=Utilitaire.tabToString(id,null,null);
        aWhere=" where id in("+aWhere+")";
        return updateToTable(apresSet+aWhere, c);
    }
    public int updateToTable(String apresSet, Connection cDb)
            throws Exception {
        String tableName = getNomTable();
        Statement stmt = null;
        try {
            String sqlQry = "UPDATE " + tableName + " SET " + apresSet;
            stmt = cDb.createStatement();
            int retour = stmt.executeUpdate(sqlQry);
            if (retour == 0) {
                throw new Exception("Erreur modification dans la table");
            }
            saveSynchronisation(isSynchro(), cDb, retour, "UPDATE", sqlQry);
            return retour;
        } catch (Exception e) {
            e.printStackTrace();
            if (cDb != null) {
                cDb.rollback();
            }
            throw new Exception("Erreur update dans la table 4" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - probleme de fermeture d'1 Statement "));
            }
        }
    }
    public String getComandeInsert(Connection c)throws Exception
    {
        Champ[] listeC = ListeColonneTable.getFromListe(this, c);
        String comande = "insert into " + this.getNomTable() + " values (";
            comande = comande + "'" + getValeurFieldByMethod(this, listeC[0].getNomColonne()) + "'";
            for (int i = 1; i < listeC.length; i++) {
                comande = comande + ",";
                if (getValeurFieldByMethod(this, listeC[i].getNomColonne()) == null) {
                    comande = comande + " null ";
                } else {
                    comande = comande + "'" + getValeurFieldByMethod(this, listeC[i].getNomColonne()) + "'";
                }
            }
            comande = comande + ")";
            return comande;
    }
    
    public int executeWithRequete(String requete, Connection cDb)
            throws Exception {
        Statement stmt = null;
        try {
            stmt = cDb.createStatement();
            int retour = stmt.executeUpdate(requete);
            if (retour == 0) {
                throw new Exception("Erreur modification dans la table");
            }
            return retour;
        } catch (Exception e) {
            e.printStackTrace();
            if (cDb != null) {
                cDb.rollback();
            }
            throw new Exception("Erreur update dans la table 4" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - probleme de fermeture d'1 Statement "));
            }
        }
    }

    public int updateToTable(String nomAttributValeur, String valeur, String nomAttributFiltre, String[] valeurFiltre, Connection cDb)
            throws Exception {
        String apresSet = nomAttributValeur + "=" + valeur + " where ";
        if (nomAttributFiltre == null || valeurFiltre == null || valeurFiltre.length == 0) {
            return updateToTable(apresSet + "1<2", cDb);
        }
        apresSet = apresSet + nomAttributFiltre + " in (";
        for (int i = 0; i < valeurFiltre.length; i++) {
            apresSet = apresSet + "'" + valeurFiltre[i] + "'";
            if (i < valeurFiltre.length - 1) {
                apresSet = apresSet + ",";
            }
        }
        apresSet = apresSet + ")";
        return updateToTable(apresSet, cDb);
    }

    public int upDateToTable() {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            try {
                c = util.GetConn();
                c.setAutoCommit(false);
                int i = updateToTable(c);
                c.commit();
                return i;
            } catch (Exception e) {
                if (c != null) {
                    try {
                        c.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                //System.out.println(String.valueOf(String.valueOf((new StringBuffer("ERREUR UPDATE_TABLE ")).append(getNomTable()).append(" ").append(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (Exception exception1) {
            }
        }
    }

    public int updateToTableWithHisto(String refUser, Connection c) throws Exception {

        try {
            String temps = refUser;
            if(refUser!=null && refUser.contains("/")){
                String[] g = Utilitaire.split(refUser, "/");
                refUser= g[0];
            }
            MapHistorique histo = new MapHistorique(this.getNomTable(), "Modification par "+temps, refUser, this.getTuppleID(),c);
            histo.setObjet(this.getClassName());
            ClassMAPTable obj = (ClassMAPTable) (Class.forName(this.getClassName()).newInstance());
            if (obj != null) {
                ClassMAPTable[] tabObj = (ClassMAPTable[]) CGenUtil.rechercher(obj, null, null, c, " and " + obj.getAttributIDName() + " = '" + histo.getRefObjet() + "'");
                Historique_valeur valeur = new Historique_valeur();
                if (tabObj.length != 0 && getEstHistorise()) {
                    Field[] listeField = this.getFieldList();
                    for (int i = 0; i < listeField.length; i++) {
//                    String nomVal = "setVal"+i;
                        int temp = i + 1;
//                    String get = "get"+Utilitaire.convertDebutMajuscule(listeField[i].getName());
//                        //System.out.println(listeField[i].getName() + " : "+ tabObj[0].getValInsert(listeField[i].getName()));
                        valeur.setValChamp("val" + temp, listeField[i].getName() + ":" + tabObj[0].getValInsert(listeField[i].getName()));
                    }
                    valeur.setIdhisto(histo.getIdHistorique());
                    valeur.setRefhisto(histo.getRefObjet());
                    valeur.setNom_table(this.getValInsert("nomTable"));
                    valeur.setNom_classe(this.getClassName());
                    valeur.setVal40(getMemo());
                    valeur.construirePK(c);
                    valeur.insertToTable(c);
                }
            }
            return this.updateToTableWithHisto(histo, c);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("ERREUR UPDATE 2" + e.getMessage());
        }

    }

    public int updateToTableWithHisto(String refUser) throws Exception {

        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            c = util.GetConn();
            c.setAutoCommit(false);
            int i = updateToTableWithHisto(refUser, c);
            c.commit();
            return i;
        } catch (Exception e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new Exception("ERREUR Update 1 ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception exception1) {
            }
        }

    }

    public int insertToTableWithHisto(String refUser) throws Exception {
        try {
            String temps = refUser;
            if(refUser!=null && refUser.contains("/")){
                String[] g = Utilitaire.split(refUser, "/");
                refUser= g[0];
            }
            MapHistorique histo = new MapHistorique(this.getNomTable(), "Insertion par "+temps, refUser, this.getTuppleID());
            return insertToTable(histo);
        } catch (Exception e) {
            throw e;
        }

    }

    public void saveSynchronisation(boolean isMirroring, Connection c, int retour, String action, String requette) throws Exception {
        PreparedStatement insertHistoSync = null;
        try {
            if (isSynchro()) {
                //sync.setEtat(ConstanteEtat.getEtatCreer());
                //sync.construirePK(c);

                if (isSynchro() & retour == 1) {
                    //if (getNomTable().compareTo(histoSync) != 0 && getNomTable().compareToIgnoreCase("HISTORIQUE") != 0) {
                    if (getNomTable().compareTo(histoSync) != 0) {
                        String query = "INSERT INTO " + histoSync + " (ID,ACTION,TABLECONCERNE,REQUETE,DATY,HEURE) VALUES ('RAE00'||getSeqRequeteAEnvoyer,?,?,?,?,?)";
                        insertHistoSync = c.prepareCall(query);
                        insertHistoSync.setString(1, action);
                        insertHistoSync.setString(2, getNomTable());
                        insertHistoSync.setString(3, requette);
                        insertHistoSync.setDate(4, Utilitaire.dateDuJourSql());
                        insertHistoSync.setString(5, Utilitaire.heureCouranteHMS());
                        insertHistoSync.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            if (insertHistoSync != null) {
                insertHistoSync.close();
            }
        }
    }
    public int updateToTableDirecte(String req,Connection c)throws Exception
    {
        boolean estOuvert=false;
        try{
        if(c==null)
        {
            c=new UtilDB().GetConn();
            estOuvert=false;
        }
        Statement s=c.createStatement();
        int valiny=s.executeUpdate(req);
        saveSynchronisation(isSynchro(), c, valiny, "UPDATE", req);
        return valiny;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(c!=null&&estOuvert==true)c.close();
        }
        
    }


    public int insertToTableWithHisto(String refUser, java.sql.Connection c) throws Exception {
        try {
            String temps = refUser;
            if(refUser!=null && refUser.contains("/")){
                String[] g = Utilitaire.split(refUser, "/");
                refUser= g[0];
            }
            MapHistorique histo = new MapHistorique(this.getNomTable(), "Insertion par "+temps, refUser, this.getTuppleID(), c);
            histo.setObjet(this.getClassName());
            return insertToTable(histo, c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    

    public int updateToTableWithHisto(ClassMAPTable histo, Connection c)
            throws Exception {
        UtilDB util = new UtilDB();
        try {
            histo.insertToTable(c);
            return (updateToTable(c));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("ERREUR Modification table 3 " + e.getMessage());
        }
    }

    public int updateToTableWithHisto(ClassMAPTable histo)
            throws ErreurDAO {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            c = util.GetConn();
            c.setAutoCommit(false);
            histo.insertToTable(c);

            int i = updateToTable(c);
            c.commit();
            int j = 1;
            return j;
        } catch (Exception e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new ErreurDAO(String.valueOf(String.valueOf((new StringBuffer("ERREUR UPDATE_TABLE ")).append(getNomTable()).append(" ").append(e.getMessage()))));
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (Exception exception1) {
            }
        }
    }

    public int insertToTable(ClassMAPTable histo, Connection c) throws Exception {

        try {
            int i = insertToTable(c);
            return (histo.insertToTable(c));
        } catch (Exception e) {
            if( c != null ){c.rollback();}
            throw e;
        }
    }

    public int insertToTable(ClassMAPTable histo)
            throws Exception {
        UtilDB util = new UtilDB();
        Connection c = null;
        try {
            c = util.GetConn();
            c.setAutoCommit(false);
            int i = insertToTable(histo, c);
            c.commit();
            return i;
        } catch (Exception e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    throw new Exception(e.getMessage());
                }
            }
            throw new Exception("ERREUR INSERT_TABLE ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (Exception exception1) {
            }
        }
    }

    public int insertToTable() throws Exception {
        UtilDB util = new UtilDB();
        Connection c = null;
        int retour = 0;
        try {
            try {
                c = util.GetConn();
                c.setAutoCommit(false);
                retour = insertToTable(c);
                c.commit();
                int i = retour;
                return i;
            } catch (Exception e) {
                if (c != null) {
                    try {
                        c.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                throw e;
            }
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (Exception e) {
                //System.out.println("ERREUR FERMETURE CONNECTION INSERT_TABLE ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }
    
    public int insertToTableWithId(Connection cDb)
            throws Exception {
//        cDb = new ConnectionSpy(cDb);
        String tableName = getNomTable();
        PreparedStatement stmt = null;
        Field fld = null;
        String sqlQry = "INSERT INTO " + tableName + " ( ";
        String sqlTmp = " ) VALUES ( ";
        if (this instanceof ClassEtat) {
            ClassEtat temp = (ClassEtat) this;
            if (temp.getIduser() == null || temp.getIduser().compareTo("") == 0) {
                temp.setIduser("0");
            }
        }
        if (this instanceof ClassUser) {
            ClassUser temp = (ClassUser) this;
            if (temp.getIduser() == null || temp.getIduser().compareTo("") == 0) {
                temp.setIduser("0");
            }
        }
        try {

            Field[] fieldlist = ListeColonneTable.getChampBase(this, cDb);
            for (int i = 0; i < fieldlist.length; i++) {
                fld = fieldlist[i];
                sqlQry = sqlQry + fld.getName() + " ";
                if ((fld.getName().compareToIgnoreCase(this.getAttributIDName()) == 0)) {
                    sqlTmp = sqlTmp + "'" + this.getValInsert(this.getAttributIDName()) +"'";
                } else {
                    sqlTmp = sqlTmp + "? ";
                }
                if (i + 1 < fieldlist.length) {

                    sqlQry = sqlQry + ", ";
                    sqlTmp = sqlTmp + ", ";
                }
            }

            sqlQry = sqlQry + sqlTmp + " ) ";

//             //System.out.println(" maptable ln:790" + sqlQry);
//            //System.out.println(" maptable ln:790" + sqlQry);
            stmt = cDb.prepareStatement(sqlQry);
            int indicePrepared = 0;
            for (int i = 1; i < fieldlist.length; i++, indicePrepared++) {
                fld = fieldlist[i];

                switch (getTypeMAPField(fld)) {
                    case 0: // '\0'
                        stmt.setBoolean(indicePrepared + 1, ((Boolean) this.getValField(fld)).booleanValue());
                        break;

                    case 1: // '\001'
                        stmt.setByte(indicePrepared + 1, ((Byte) this.getValField(fld)).byteValue());
                        break;

                    case 2: // '\002'
                        stmt.setShort(indicePrepared + 1, ((Short) this.getValField(fld)).shortValue());
                        break;

                    case 3: // '\003'
                        Integer it = (Integer) this.getValField(fld);
                        stmt.setInt(indicePrepared + 1, it.intValue());
                        break;

                    case 4: // '\004'
                        Long lo = (Long) this.getValField(fld);
                        stmt.setLong(indicePrepared + 1, lo.longValue());
                        break;

                    case 5: // '\005'
                        stmt.setFloat(indicePrepared + 1, ((Float) this.getValField(fld)).floatValue());
                        break;

                    case 6: // '\006'
                        stmt.setDouble(indicePrepared + 1, ((Double) this.getValField(fld)).doubleValue());
                        break;

                    case 10: // '\n'
                        if ((fld.getName().compareToIgnoreCase(this.getAttributIDName()) != 0) || this.getNomSequenceDirecte() == null || this.getNomSequenceDirecte().compareToIgnoreCase("") == 0) {
                            stmt.setString(indicePrepared + 1, (Utilitaire.champNull((String) this.getValField(fld))).replace("'", "`"));
                        } else {
                            indicePrepared = indicePrepared - 1;
                        }
                        break;

                    case 21: // '\025'
                        stmt.setDate(indicePrepared + 1, (Date) this.getValField(fld));
                        break;

                    case 33: // '!'
                        stmt.setInt(indicePrepared + 1, ((Integer) this.getValField(fld)).intValue());
                        break;

                    case 34: // '!'
                        stmt.setInt(indicePrepared + 1, ((Integer) this.getValField(fld)).intValue());
                        break;
                    case 35: // '!'
                        stmt.setTimestamp(indicePrepared + 1, ((java.sql.Timestamp) this.getValField(fld)));
                        break;
                }
            }
//            //System.out.println(" requette : " + stmt.toString());
            int retour = stmt.executeUpdate();
            if (retour == 0) {
                throw new Exception("Erreur insertion dans la table ");
            }

            int j = 1;
            return j;
        } catch (Exception e) {
            try {
                
                if( cDb != null ){cDb.rollback();};
                e.printStackTrace();

                throw new Exception("Erreur insertion dans la table " + getNomTable() + " " + e.getMessage() + " ref= " + getTuppleID());
            } catch (Exception ex) {
                return 0;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - probleme de fermeture d'1 Statement "));
            }
        }
    }
    
    //insert paie_edition tsy misy reflexion
    public int insertIntoTablePaieEdition(String refUser, java.sql.Connection cDb, String idpersonnel, String idelementpaie, int mois, int annee, Date datedebut, Date datefin, double gain, double retenue) throws Exception {
        PreparedStatement stmt = null;
        try {
            String sqlQry = "INSERT INTO PAIE_EDITION(ID, IDPERSONNEL , IDELEMENTPAIE, MOIS, ANNEE, GAIN, RETENUE, DATEDEBUT, DATEFIN) VALUES (";
            String sqlTmp = "'ELP'||seq_paie_edition.nextval";
            sqlTmp = sqlTmp + " , ? , ? , ? , ? , ? , ? , ? , ? ";
            sqlQry = sqlQry + sqlTmp + ")";

            stmt = cDb.prepareStatement(sqlQry);
            stmt.setString(1, (String) idpersonnel);
            stmt.setString(2, (String) idelementpaie);
            stmt.setInt(3, mois);
            stmt.setInt(4, annee);
            stmt.setDouble(5, (Double) gain);
            stmt.setDouble(6, (Double) retenue);
            stmt.setDate(7, (Date) datedebut);
            stmt.setDate(8, (Date) datefin);

            int retour = stmt.executeUpdate();
            if (retour == 0) {
                throw new Exception("Erreur insertion dans la table paie_edition");
            }

            return insertPaieEditionIntoHistorique(cDb, refUser);
        } catch (Exception e) {
            try {
                cDb.rollback();
                throw new Exception("Erreur insertion dans la table paie_edition ref= " + getTuppleID());
            } catch (Exception ex) {
                return 0;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog("paie_edition - probleme de fermeture d'1 Statement ");
            }
        }
    }
    public Object getValeur(String nomChamp)throws Exception
    {
        return CGenUtil.getValeurFieldByMethod(this, nomChamp);
    }
    public int insertPaieEditionIntoHistorique(Connection cDb, String refUser) throws Exception {
        PreparedStatement stmt = null;
        try {
            String temps = refUser;
            if(refUser!=null && refUser.contains("/")){
                String[] g = Utilitaire.split(refUser, "/");
                refUser= g[0];
            }
            String sqlQry = "INSERT INTO HISTORIQUE(IDHISTORIQUE , DATEHISTORIQUE, HEURE, OBJET, ACTION, IDUTILISATEUR, REFOBJET) VALUES (";
            String sqlTmp = "'ELP'||SEQ_HISTORIQUE.nextval";
            sqlTmp = sqlTmp + " , ? , ? , ? , ? , ? , ? ";
            sqlQry = sqlQry + sqlTmp + ")";

            stmt = cDb.prepareStatement(sqlQry);
            stmt.setDate(1, (Date) Utilitaire.dateDuJourSql());
            stmt.setString(2, (String) Utilitaire.heureCourante());
            stmt.setString(3, (String) this.getClassName());
            stmt.setString(4, (String) "Insertion par "+temps);
            stmt.setString(5, (String) refUser);
            stmt.setString(6, (String) this.getTuppleID());

            int retour = stmt.executeUpdate();
            if (retour == 0) {
                throw new Exception("Erreur insertion dans la table historique");
            }

            int j = 1;
            return j;
        } catch (Exception e) {
            try {
                if( cDb != null ){cDb.rollback();}
                throw new Exception("Erreur insertion dans la table HISTORIQUE ref= " + getTuppleID());
            } catch (Exception ex) {
                return 0;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog("HISTORIQUE - probleme de fermeture d'1 Statement ");
            }
        }
    }
    //mifarana eto ny insert paie_edtion tsy misy reflexion

    public int insertToTable(Connection cDb)
            throws Exception {
//        cDb = new ConnectionSpy(cDb);
        String tableName = getNomTable();
        PreparedStatement stmt = null, insertHistoSync = null;
        Field fld = null;
        String sqlQry = "INSERT INTO " + tableName + " ( ";
        String sqlTmp = " ) VALUES ( ";
        if(this.getTuppleID()==null||this.getTuppleID().compareToIgnoreCase("")==0)this.construirePK(cDb);
        if (this instanceof ClassEtat || this instanceof ClassUser) {
            ClassEtat temp = (ClassEtat) this;
            if (temp.getIduser() == null || temp.getIduser().compareTo("") == 0) {
                temp.setIduser("0");
            }
        }
        
        try {

            Field[] fieldlist = ListeColonneTable.getChampBase(this, cDb);
            for (int i = 0; i < fieldlist.length; i++) {
                fld = fieldlist[i];
                sqlQry = sqlQry + fld.getName() + " ";
                if ((fld.getName().compareToIgnoreCase(this.getAttributIDName()) == 0) && this.getNomSequenceDirecte() != null && this.getNomSequenceDirecte().compareToIgnoreCase("") != 0) {
                    sqlTmp = sqlTmp + "'" + this.getINDICE_PK() + "'||" + getNomSequenceDirecte() + ".nextval";
                } else {
                    sqlTmp = sqlTmp + "? ";
                }
                if (i + 1 < fieldlist.length) {

                    sqlQry = sqlQry + ", ";
                    sqlTmp = sqlTmp + ", ";
                }
            }
            String enTete = sqlQry + ") values ( ";
            sqlQry = sqlQry + sqlTmp + " ) ";

            //System.out.println(" maptable ln:790" + sqlQry);
            //System.out.println(" maptable ln:790" + sqlQry);
            stmt = cDb.prepareStatement(sqlQry);
            int indicePrepared = 0;
            for (int i = 0; i < fieldlist.length; i++, indicePrepared++) {
                fld = fieldlist[i];
                String valStr = "";
                switch (getTypeMAPField(fld)) {
                    case 0: // '\0'
                        stmt.setBoolean(indicePrepared + 1, ((Boolean) this.getValField(fld)).booleanValue());
                        valStr = "'" + ((Boolean) this.getValField(fld)).booleanValue() + "'";
                        break;

                    case 1: // '\001'
                        stmt.setByte(indicePrepared + 1, ((Byte) this.getValField(fld)).byteValue());
                        valStr = "'" + ((Byte) this.getValField(fld)).byteValue() + "'";
                        break;

                    case 2: // '\002'
                        stmt.setShort(indicePrepared + 1, ((Short) this.getValField(fld)).shortValue());
                        valStr = "" + ((Short) this.getValField(fld)).shortValue() + "";
                        break;

                    case 3: // '\003'
                        Integer it = (Integer) this.getValField(fld);
                        stmt.setInt(indicePrepared + 1, it.intValue());
                        valStr = "" + it.intValue() + "";
                        break;

                    case 4: // '\004'
                        Long lo = (Long) this.getValField(fld);
                        stmt.setLong(indicePrepared + 1, lo.longValue());
                        valStr = "" + lo.longValue() + "";
                        break;

                    case 5: // '\005'
                        stmt.setFloat(indicePrepared + 1, ((Float) this.getValField(fld)).floatValue());
                        valStr = "" + ((Float) this.getValField(fld)).floatValue() + "";
                        break;

                    case 6: // '\006'
                        stmt.setDouble(indicePrepared + 1, ((Double) this.getValField(fld)).doubleValue());
                        valStr = "" + ((Double) this.getValField(fld)).doubleValue() + "";
                        break;

                    case 10: // '\n'
                        String val = (String) this.getValField(fld);
                        if (val != null && val.compareTo("") != 0) {
                            val = val.replace("'", "''");
                        }
                        if ((fld.getName().compareToIgnoreCase(this.getAttributIDName()) != 0) || this.getNomSequenceDirecte() == null || this.getNomSequenceDirecte().compareToIgnoreCase("") == 0) {
                            stmt.setString(indicePrepared + 1, (String) this.getValField(fld));
                        } else {
                            indicePrepared = indicePrepared - 1;
                        }
                        valStr = "'" + val + "'";
                        break;

                    case 21: // '\025'
                        stmt.setDate(indicePrepared + 1, (Date) this.getValField(fld));
                        if ((Date) this.getValField(fld) != null) {
                            valStr = "'" + Utilitaire.formatterDatySql((Date) this.getValField(fld)) + "'";
                        } else {
                            valStr = "'null'";
                        }
                        break;

                    case 33: // '!'
                        stmt.setInt(indicePrepared + 1, ((Integer) this.getValField(fld)).intValue());
                        valStr = "'" + ((Integer) this.getValField(fld)).intValue() + "'";
                        break;

                    case 34: // '!'
                        stmt.setInt(indicePrepared + 1, ((Integer) this.getValField(fld)).intValue());
                        break;
                    case 35: // '!'
                        stmt.setTimestamp(indicePrepared + 1, ((java.sql.Timestamp) this.getValField(fld)));
                        if((java.sql.Timestamp) this.getValField(fld)!=null){
                            valStr = "TO_TIMESTAMP('" + (java.sql.Timestamp) this.getValField(fld) + "','YYYY-MM-DD HH24:MI:SS:FF')";
                        }else{
                            valStr = "null";
                        }
                        break;
                }
                valStr = (valStr.compareToIgnoreCase("'null'") == 0) ? null : valStr;
                if (i + 1 < fieldlist.length) {
                    enTete += valStr + ",";
                } else {
                    enTete += valStr;
                }
            }
            
            int retour = stmt.executeUpdate();
            if (retour == 0) {
                throw new Exception("Erreur insertion dans la table ");
            }
            enTete += ")";
            if (isSynchro() & retour == 1) {
                if (tableName.compareTo(histoSync) != 0 && tableName.compareToIgnoreCase("HISTORIQUE") != 0) {
                    String query = "INSERT INTO " + histoSync + " (ID,ACTION,TABLECONCERNE,REQUETE,DATY,HEURE,SERVEUR) VALUES ('RQ'||getSeqRequeteAEnvoyer,?,?,?,?,?,null)";
                    insertHistoSync = cDb.prepareCall(query);
                    insertHistoSync.setString(1, "INSERT");
                    insertHistoSync.setString(2, tableName);
                    insertHistoSync.setString(3, enTete);
                    insertHistoSync.setDate(4, Utilitaire.dateDuJourSql());
                    insertHistoSync.setString(5, Utilitaire.heureCourante());
                    //System.out.println("Je vais executer insert into REQUETE_A_ENVOYER");
                    insertHistoSync.executeUpdate();
                }
            }
            int j = 1;
            return j;
        } catch (Exception e) {
            try {
                e.printStackTrace();
                if( cDb != null ){cDb.rollback();}
                throw new Exception("Erreur insertion dans la table " + getNomTable() + " " + e.getMessage() + " ref= " + getTuppleID());
            } catch (Exception ex) {
                return 0;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - probleme de fermeture d'1 Statement "));
            }
        }
    }

    public void getInfoFromCursor(ResultSet rs)
            throws ClassMAPTableException {
        try {
            Field fieldlist[] = getFieldList();
            int typeClass = 0;
            for (int i = 0; i < fieldlist.length; i++) {
                Field fld = fieldlist[i];
                typeClass = getTypeMAPField(fld);
                switch (getTypeMAPField(fld)) {
                    case 0: // '\0'
                        fld.setBoolean(this, rs.getBoolean(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur boolean : ").append(fld.getBoolean(this)))));
                        break;

                    case 1: // '\001'
                        fld.setByte(this, rs.getByte(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur byte : ").append(fld.getByte(this)))));
                        break;

                    case 2: // '\002'
                        fld.setShort(this, rs.getShort(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur Short : ").append(fld.getShort(this)))));
                        break;

                    case 3: // '\003'
                        fld.setInt(this, rs.getInt(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur int : ").append(fld.getInt(this)))));
                        break;

                    case 4: // '\004'
                        fld.setLong(this, rs.getLong(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur long : ").append(fld.getLong(this)))));
                        break;

                    case 5: // '\005'
                        fld.setFloat(this, rs.getFloat(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur Float : ").append(fld.getFloat(this)))));
                        break;

                    case 6: // '\006'
                        fld.setDouble(this, rs.getDouble(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur double : ").append(fld.getDouble(this)))));
                        break;

                    case 10: // '\n'
                        Log.setLog(String.valueOf(String.valueOf(getClassName())).concat(" - TYPE STRING "));
                        fld.set(this, rs.getString(fld.getName()));
                        Log.setLog(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getClassName())))).append(" - Valeur String : ").append((String) fld.get(this)))));
                        break;
                }
            }

        } catch (Exception e) {
            throw new ClassMAPTableException(e.getMessage());
        }
    }

    public static int getMaxColonneFactFin(String daty) {
        UtilDB util = new UtilDB();
        Connection c = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            try {
                String an = Utilitaire.getAnnee(daty);
                c = null;
                c = util.GetConn();
                cs = c.prepareStatement(String.valueOf(String.valueOf((new StringBuffer("select * from  seqFact where daty<='31/12/")).append(an).append("' and daty>='01/01/").append(an).append("'"))));
                rs = cs.executeQuery();
                int i = 0;
                if (rs.next()) {
                    i++;
                }
                //System.out.println("sasa ".concat(String.valueOf(String.valueOf(i))));
                if (i == 0) {
                    int k = 0;
                    return k;
                }
                int l = (new Integer(rs.getString(1))).intValue();
                return l;
            } catch (SQLException e) {
                //System.out.println("getMaxSeq : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cs != null) {
                    cs.close();
                }
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (SQLException e) {
                //System.out.println("Erreur Fermeture SQL RechercheType ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }

    public static int getMaxColonneFactFinEntite(String daty, String entite) {
        UtilDB util = new UtilDB();
        Connection c = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            try {
                String an = Utilitaire.getAnnee(daty);
                c = null;
                c = util.GetConn();
                cs = c.prepareStatement(String.valueOf(String.valueOf((new StringBuffer("select * from  seqFact where entite='" + entite + "' and daty<='31/12/")).append(an).append("' and daty>='01/01/").append(an).append("'"))));
                rs = cs.executeQuery();
                int i = 0;
                if (rs.next()) {
                    i++;
                }
                if (i == 0) {
                    int k = 0;
                    return k;
                }
                int l = (new Integer(rs.getString(1))).intValue();
                return l;
            } catch (SQLException e) {
                //System.out.println("getMaxSeq : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int j = 0;
            return j;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cs != null) {
                    cs.close();
                }
                if (c != null) {
                    c.close();
                }
                util.close_connection();
            } catch (SQLException e) {
                //System.out.println("Erreur Fermeture SQL RechercheType ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        }
    }

    public int getNombreChamp() {
        return nombreChamp;
    }

    public void preparePk(String indicePK, String fonct) throws Exception {
        setIndicePk(indicePK);
        setNomProcedureSequence(fonct);
    }
    

    public void genererPKInterne() throws Exception {
        String nomMethode = "set" + Utilitaire.convertDebutMajuscule(this.getAttributIDName());
        String[] args = {makePK()};
        Class[] paramT = {new String().getClass()};
        this.getClass().getMethod(nomMethode, paramT).invoke(this, args);
    }

    public void setValChamp(String nomChamp, Object valeur) throws Exception {
        Field f = getFieldByName(nomChamp);
        String nomMethode = "set" + Utilitaire.convertDebutMajuscule(nomChamp);
        Object[] args = {valeur};
        Class[] paramT = {f.getType()};
        if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
            //bean.CGenUtil.setValChamp(getBase(), f, valeur);
        }
        if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0 && valeur instanceof java.lang.String) {
            //System.out.println("La date en sortie = "+utilitaire.Utilitaire.string_date("dd/MM/yyyy",valeur));
            args[0] = utilitaire.Utilitaire.string_date("dd/MM/yyyy", String.valueOf(valeur));
        }
        if (f.getType().getName().compareToIgnoreCase("double") == 0 && valeur instanceof java.lang.String) {
            args[0] = new Double(utilitaire.Utilitaire.stringToDouble(String.valueOf(valeur)));
        }
        if (f.getType().getName().compareToIgnoreCase("int") == 0 && valeur instanceof java.lang.String) {
            if (valeur == null || String.valueOf(valeur).compareToIgnoreCase("") == 0) {
                args[0] = new Integer(0);
            } else {
                args[0] = new Integer(String.valueOf(valeur));
            }
        }
        if (f.getType().getName().compareToIgnoreCase("float") == 0 && valeur instanceof java.lang.String) {
            args[0] = new Float(utilitaire.Utilitaire.stringToFloat(String.valueOf(valeur)));
        }
        if (f.getType().getName().compareToIgnoreCase("java.sql.Timestamp") == 0 && valeur instanceof java.lang.String) {
            args[0] = Utilitaire.convertStringToTimestampHour(String.valueOf(valeur), ":");
        }

        this.getClass().getMethod(nomMethode, paramT).invoke(this, args);
    }
    
    public void setValChamp(Field f, Object valeur) throws Exception {
        String nomMethode = "set" + Utilitaire.convertDebutMajuscule(f.getName());
        Object[] args = {valeur};
        Class[] paramT = {f.getType()};
        if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
            //bean.CGenUtil.setValChamp(getBase(), f, valeur);
        }
        if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0 && valeur instanceof java.lang.String) {
            //System.out.println("La date en sortie = "+utilitaire.Utilitaire.string_date("dd/MM/yyyy",valeur));
            args[0] = utilitaire.Utilitaire.string_date("dd/MM/yyyy", String.valueOf(valeur));
        }
        if (f.getType().getName().compareToIgnoreCase("double") == 0 && valeur==null) {
            args[0] = new Double(0);
        }
        if (f.getType().getName().compareToIgnoreCase("double") == 0 && valeur instanceof java.lang.String) {
            if (valeur == null || String.valueOf(valeur).compareToIgnoreCase("") == 0) {
                args[0] = new Double(0);
            } else {
                args[0] = new Double(utilitaire.Utilitaire.stringToDouble(String.valueOf(valeur)));
            }
        }
        if (f.getType().getName().compareToIgnoreCase("int") == 0 && valeur==null) {
            args[0] = new Integer(0);
        }
        if (f.getType().getName().compareToIgnoreCase("int") == 0 && valeur instanceof java.lang.String) {
            if (valeur == null || String.valueOf(valeur).compareToIgnoreCase("") == 0) {
                args[0] = new Integer(0);
            } else {
                args[0] = new Integer(String.valueOf(valeur));
            }
        }
        if (f.getType().getName().compareToIgnoreCase("float") == 0 && valeur==null) {
            args[0] = new Float(0);
        }
        if (f.getType().getName().compareToIgnoreCase("float") == 0 && valeur instanceof java.lang.String) {
            args[0] = new Float(utilitaire.Utilitaire.stringToFloat(String.valueOf(valeur)));
        }
        if (f.getType().getName().compareToIgnoreCase("java.sql.Timestamp") == 0 && valeur instanceof java.lang.String) {
            args[0] = Utilitaire.convertStringToTimestampHour(String.valueOf(valeur), ":");
        }
        this.getClass().getMethod(nomMethode, paramT).invoke(this, args);
    }

    public Field getFieldByName(String name) throws Exception {
        Field[] t = getFieldList();
        for (int i = 0; i < t.length; i++) {
            if (t[i].getName().compareTo(name) == 0) {
                return t[i];
            }
        }
        return null;
    }

    public Object getValField(Field f) {
        try {
            String nomMethode = "get" + Utilitaire.convertDebutMajuscule(f.getName());
            Object o = this.getClass().getMethod(nomMethode, null).invoke(this, null);
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String getValInsert(String nomCol) {
        String o = null;
        try {
            String nomMethode = "get" + Utilitaire.convertDebutMajuscule(nomCol);
            if (this.getClass().getMethod(nomMethode, null).invoke(this, null) != null) {
                o = this.getClass().getMethod(nomMethode, null).invoke(this, null).toString();
            }
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object getValeur(int numColonne) {
        try {
            Object o = this.getValField(getFieldList()[numColonne]);
            return o;
        } catch (Exception ex) {
            return null;
        }
    }

    public void setNomTableSelect(String nomTableSelect) {
        this.nomTableSelect = nomTableSelect;
    }

    public String getNomTableSelect() {
        if (nomTableSelect == null || nomTableSelect == "") {
            return this.getNomTable();
        }
        return nomTableSelect;
    }

    public String getNomProcedureSequence() {
        return nomProcedureSequence;
    }

    public int getLonguerClePrimaire() {
        return longuerClePrimaire;
    }

    public String getINDICE_PK() {
        return INDICE_PK;
    }

    public void setINDICE_PK(String INDICE_PK) {
        this.INDICE_PK = INDICE_PK;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    static final int TYPE_CLASS_MAP_BOOLEAN = 0;
    static final int TYPE_CLASS_MAP_BYTE = 1;
    static final int TYPE_CLASS_MAP_SHORT = 2;
    static final int TYPE_CLASS_MAP_INT = 3;
    static final int TYPE_CLASS_MAP_LONG = 4;
    static final int TYPE_CLASS_MAP_FLOAT = 5;
    static final int TYPE_CLASS_MAP_DOUBLE = 6;
    static final int TYPE_CLASS_MAP_STRING = 10;
    static final int TYPE_CLASS_MAP_WBYTE = 11;
    static final int TYPE_CLASS_MAP_WSHORT = 12;
    static final int TYPE_CLASS_MAP_WINT = 13;
    static final int TYPE_CLASS_MAP_WLONG = 14;
    static final int TYPE_CLASS_MAP_WFLOAT = 15;
    static final int TYPE_CLASS_MAP_WDOUBLE = 16;
    static final int TYPE_CLASS_MAP_DATE = 21;
    static final int TYPE_CLASS_MAP_TIME = 22;
    static final int TYPE_CLASS_MAP_BLOB = 31;
    static final int TYPE_CLASS_MAP_CLOB = 32;
    static final int TYPE_CLASS_MAP_INTEGER = 33;
    String nomTable;
    Vector champ;
    int nombreChamp;
    Class cls;
    String INDICE_PK;
    String nomProcedureSequence;
    int longuerClePrimaire;
    private String nomTableSelect;
    private String mode = "modif";
    private String columnlibelle = null;
    private boolean groupe = false;
    private int nombrepargroupe;
    String nomSequenceDirecte;
    boolean estHistorise = false;
    String memo = "";
    
    public String getColumnlibelle() {
        return columnlibelle;
    }

    public void setColumnlibelle(String columnlibelle) {
        this.columnlibelle = columnlibelle;
    }

    

    public Vector getChamp() {
        return champ;
    }

    public void setChamp(Vector champ) {
        this.champ = champ;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean getEstHistorise() {
        return estHistorise;
    }

    public void setEstHistorise(boolean estHistorise) {
        this.estHistorise = estHistorise;
    }

    public String getNomSequenceDirecte() {
        return nomSequenceDirecte;
    }

    public void setNomSequenceDirecte(String nomSequenceDirecte) {
        this.nomSequenceDirecte = nomSequenceDirecte;
    }

    public void setNomSequenceDirecte(String nomSequenceDirecte, String indicePK) {
        this.setNomSequenceDirecte(nomSequenceDirecte);
        this.setIndicePk(indicePK);
    }

    public int getNombrepargroupe() {
        return nombrepargroupe;
    }

    public void setNombrepargroupe(int nombrepargroupe) {
        this.nombrepargroupe = nombrepargroupe;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public boolean getGroupe() {
        return groupe;
    }

    public void setGroupe(boolean groupe) {
        this.groupe = groupe;
    }

    public void controlerDelete(Connection c)throws Exception {

    }


  public String[] getMotCles() {
    String[] t = new String[1];
    t[0] = this.getAttributIDName();
    return t;
  }

  public String[] getValMotCles() {
        return getMotCles();
  }
}
