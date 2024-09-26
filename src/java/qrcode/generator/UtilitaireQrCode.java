/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcode.generator;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utilitaire.UtilDB;

/**
 * Relation entre le QRCode et la base de donée 
 * <h3>Exmple d'utilisation</h3>
 * <pre>
 * UtilitaireQrCode u = new UtilitaireQrCode("id","texte");
 * </pre>
 * @author BICI
 */
public class UtilitaireQrCode {
    public enum TypeQrCode {
        CRYPTE, SIMPLE
    }

    /**
     * Constructeur qui appelle la fonction 
     * <pre>insertQrCode(String id, String texte, TypeQrCode typeQrCode)</pre>
     * et avec TypeQrCode = CRYPTE
     * @param id l'indetifiant du QRCode
     * @param texte le texte à encoder dans le code QR
     * @throws Exception
     */
    public void insertQrCode(String id, String texte) throws Exception{
        insertQrCode(id, texte, TypeQrCode.CRYPTE);
    }

    /**
     * Cette méthode  insère un code QR dans une table de base de données.
     * @param id l'indetifiant du QRCode
     * @param texte le texte à encoder dans le code QR
     * @param typeQrCode le type de code QR à générer
     * @throws Exception
     */
    public void insertQrCode(String id, String texte, TypeQrCode typeQrCode) throws Exception{
        Connection connection=null;
        PreparedStatement statement=null;
        try{
            if(testIfExist(id)) updateQrCode(id, texte, typeQrCode);
            else{
                String qrcode = texte;
                if(typeQrCode.equals(TypeQrCode.CRYPTE)){
                    GeneratorQrCode g = new GeneratorQrCode(qrcode);
                    g.createImage(g.getQrcode()+"qrCodeAccuse.jpg");
                    connection = new UtilDB().GetConn();
                    connection.setAutoCommit(false);
                    statement = connection.prepareStatement("INSERT INTO CNAPS_QR_CODE(ID, OBJET, QRCODE) VALUES(?, ?, ?)");
                    File file = new File(g.getQrcode()+"qrCodeAccuse.jpg");// get path and file name from text fields
                    FileInputStream fIn = new FileInputStream(file);
                    statement.setString(1, id);
                    statement.setString(2, texte);
                    statement.setBlob(3, fIn); 
                    statement.executeQuery();
                    connection.commit();
                }
                else{
                    SimpleGenerator g = new SimpleGenerator(qrcode);
                    g.createImage(g.getQrcode()+"qrCodeAccuse.jpg");
                    connection = new UtilDB().GetConn();
                    connection.setAutoCommit(false);
                    statement = connection.prepareStatement("INSERT INTO CNAPS_QR_CODE(ID, OBJET, QRCODE) VALUES(?, ?, ?)");
                    File file = new File(g.getQrcode()+"qrCodeAccuse.jpg");// get path and file name from text fields
                    FileInputStream fIn = new FileInputStream(file);
                    statement.setString(1, id);
                    statement.setString(2, texte);
                    statement.setBlob(3, fIn); 
                    statement.executeQuery();
                    connection.commit();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(connection!=null) connection.close();
            if(statement!=null) statement.close();
        }
    }

    /**
     * Cette méthode  met à jour un code QR dans une table de base de données.
     * @param id l'indetifiant du QRCode
     * @param texte le texte à encoder dans le code QR
     * @param typeQrCode le type de code QR à générer
     * @throws Exception
     */
    private void updateQrCode(String id, String texte, TypeQrCode typeQrCode) throws Exception{
        Connection connection=null;
        PreparedStatement statement=null;
        try{
            String qrcode = texte;
            if(typeQrCode.equals(TypeQrCode.CRYPTE)){
                GeneratorQrCode g = new GeneratorQrCode(qrcode);
                g.createImage(g.getQrcode()+"qrCodeAccuse.jpg");
                connection = new UtilDB().GetConn();
                connection.setAutoCommit(false);
                statement = connection.prepareStatement("UPDATE CNAPS_QR_CODE SET OBJET = ? , QRCODE = ? WHERE ID = ?");
                File file = new File(g.getQrcode()+"qrCodeAccuse.jpg");// get path and file name from text fields
                FileInputStream fIn = new FileInputStream(file);
                statement.setString(1, texte);
                statement.setBlob(2, fIn); 
                statement.setString(3, id);
                statement.executeQuery();
                connection.commit();
            }
            else{
                SimpleGenerator g = new SimpleGenerator(qrcode);
                g.createImage(g.getQrcode()+"qrCodeAccuse.jpg");
                connection = new UtilDB().GetConn();
                connection.setAutoCommit(false);
                statement = connection.prepareStatement("UPDATE CNAPS_QR_CODE SET OBJET = ? , QRCODE = ? WHERE ID = ?");
                File file = new File(g.getQrcode()+"qrCodeAccuse.jpg");// get path and file name from text fields
                FileInputStream fIn = new FileInputStream(file);
                statement.setString(1, texte);
                statement.setBlob(2, fIn); 
                statement.setString(3, id);
                statement.executeQuery();
                connection.commit();
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(connection!=null) connection.close();
            if(statement!=null) statement.close();
        }
    }

    /**
     * Permet de voir si un QrCode existe déjà dans une base de donnée
     * @param id l'indetifiant du QRCode
     * @return true or false
     * @throws Exception
     */
    public boolean testIfExist(String id) throws Exception{
        Connection con = null;
        Statement s =null;
        ResultSet rs =null;
        try {
            con = new UtilDB().GetConn();
            String query = "SELECT QRCODE FROM CNAPS_QR_CODE WHERE ID = '" + id + "'";
            s = con.createStatement();
            rs = s.executeQuery(query);
            if(rs.next()) return true;
            else return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(rs!=null) rs.close();
            if(s!=null) s.close();
            if (con != null)con.close();
        }
        return false;
    }

    /**
     * Cette méthode  récupère un tableau binaire (octets) d'une image 
     * de code QR à partir d'une table de base de données
     * @param id l'indetifiant du QRCode
     * @return un tableau d'octets représentant l'image
     * @throws SQLException
     */
    public byte[] getByte(String id) throws SQLException {
        Connection con = null;
        try {
            con = new UtilDB().GetConn();
            byte[] res = null;

            String query = "SELECT QRCODE FROM CNAPS_QR_CODE WHERE ID = '" + id + "'";

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            if (rs.next()) {
                Blob signatureBlob = rs.getBlob("QRCODE");
                res = signatureBlob.getBytes(1, (int) signatureBlob.length());
            }
            s.close();
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
}
