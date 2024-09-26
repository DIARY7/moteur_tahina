/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

import bean.ClassMAPTable;
import java.sql.Connection;

/**
 * Objet representant la valeur précedente d'un objet avant suppression/mise à jour
 * Table : historique_valeur
 * 
 *  
 * @author BICI
 */

public class Historique_valeur extends ClassMAPTable {

    private String id;
    private String idhisto;
    private String refhisto;
    private String nom_table;
    private String nom_classe;
    private String val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26, val27, val28, val29, val30, val31, val32, val33, val34, val35, val36, val37, val38, val39, val40;
    private String ipuser;
    /**
     * Implementation de la methode qui doit donner la valeur de la cle
     * primaire (tjrs pour update)
     */
    @Override
    public String getTuppleID() {
	return getId();
    }
    /**
     * Implementation de la methode qui doit donner le nom du champ de la cle
     * primaire (tjrs pour update)
     */
    @Override
    public String getAttributIDName() {
	return "id";
    }

    public void construirePK(Connection c) throws Exception {
	this.preparePk("VAL", "getSeqhistovaleur");
	this.setId(makePK(c));
    }

    public Historique_valeur() {
	this.setNomTable("historique_valeur");
    }

    public Historique_valeur(String idhisto, String refhisto, String nom_table, String nom_classe) {
	this.setNomTable("historique_valeur");
	this.setIdhisto(idhisto);
	this.setRefhisto(refhisto);
	this.setNom_table(nom_table);
	this.setNom_classe(nom_classe);
    }

    public Historique_valeur(String idhisto, String refhisto, String nom_table, String nom_classe, String val, String val1, String val2, String val3, String val4, String val5, String val6, String val7, String val8, String val9, String val10, String val11, String val12, String val13, String val14, String val15, String val16, String val17, String val18, String val19, String val20, String val21, String val22, String val23, String val24, String val25, String val26, String val27, String val28, String val29, String val30, String val31, String val32, String val33, String val34, String val35, String val36, String val37, String val38, String val39, String val40) {
	this.setNomTable("historique_valeur");
	this.setIdhisto(idhisto);
	this.setRefhisto(refhisto);
	this.setNom_table(nom_table);
	this.setNom_classe(nom_classe);
	this.setVal1(val1);
	this.setVal2(val2);
	this.setVal3(val3);
	this.setVal4(val4);
	this.setVal5(val5);
	this.setVal6(val6);
	this.setVal7(val7);
	this.setVal8(val8);
	this.setVal9(val9);
	this.setVal10(val10);
	this.setVal11(val11);
	this.setVal12(val12);
	this.setVal13(val13);
	this.setVal14(val14);
	this.setVal15(val15);
	this.setVal16(val16);
	this.setVal17(val17);
	this.setVal18(val18);
	this.setVal19(val19);
	this.setVal20(val20);
	this.setVal21(val21);
	this.setVal22(val22);
	this.setVal23(val23);
	this.setVal24(val24);
	this.setVal25(val25);
	this.setVal26(val26);
	this.setVal27(val27);
	this.setVal28(val28);
	this.setVal29(val29);
	this.setVal30(val30);
	this.setVal31(val31);
	this.setVal32(val32);
	this.setVal33(val33);
	this.setVal34(val34);
	this.setVal35(val35);
	this.setVal36(val36);
	this.setVal37(val37);
	this.setVal38(val38);
	this.setVal39(val39);
	this.setVal40(val40);
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the idhisto
     */
    public String getIdhisto() {
	return idhisto;
    }

    /**
     * @param idhisto the idhisto to set
     */
    public void setIdhisto(String idhisto) {
	this.idhisto = idhisto;
    }

    /**
     * @return the refhisto
     */
    public String getRefhisto() {
	return refhisto;
    }

    /**
     * @param refhisto the refhisto to set
     */
    public void setRefhisto(String refhisto) {
	this.refhisto = refhisto;
    }

    /**
     * @return the nom_table
     */
    public String getNom_table() {
	return nom_table;
    }

    /**
     * @param nom_table the nom_table to set
     */
    public void setNom_table(String nom_table) {
	this.nom_table = nom_table;
    }

    /**
     * @return the nom_classe
     */
    public String getNom_classe() {
	return nom_classe;
    }

    /**
     * @param nom_classe the nom_classe to set
     */
    public void setNom_classe(String nom_classe) {
	this.nom_classe = nom_classe;
    }

    /**
     * @return the val1
     */
    public String getVal1() {
	return val1;
    }

    /**
     * @param val1 the val1 to set
     */
    public void setVal1(String val1) {
	this.val1 = val1;
    }

    /**
     * @return the val2
     */
    public String getVal2() {
	return val2;
    }

    /**
     * @param val2 the val2 to set
     */
    public void setVal2(String val2) {
	this.val2 = val2;
    }

    /**
     * @return the val3
     */
    public String getVal3() {
	return val3;
    }

    /**
     * @param val3 the val3 to set
     */
    public void setVal3(String val3) {
	this.val3 = val3;
    }

    /**
     * @return the val4
     */
    public String getVal4() {
	return val4;
    }

    /**
     * @param val4 the val4 to set
     */
    public void setVal4(String val4) {
	this.val4 = val4;
    }

    /**
     * @return the val5
     */
    public String getVal5() {
	return val5;
    }

    /**
     * @param val5 the val5 to set
     */
    public void setVal5(String val5) {
	this.val5 = val5;
    }

    /**
     * @return the val6
     */
    public String getVal6() {
	return val6;
    }

    /**
     * @param val6 the val6 to set
     */
    public void setVal6(String val6) {
	this.val6 = val6;
    }

    /**
     * @return the val7
     */
    public String getVal7() {
	return val7;
    }

    /**
     * @param val7 the val7 to set
     */
    public void setVal7(String val7) {
	this.val7 = val7;
    }

    /**
     * @return the val8
     */
    public String getVal8() {
	return val8;
    }

    /**
     * @param val8 the val8 to set
     */
    public void setVal8(String val8) {
	this.val8 = val8;
    }

    /**
     * @return the val9
     */
    public String getVal9() {
	return val9;
    }

    /**
     * @param val9 the val9 to set
     */
    public void setVal9(String val9) {
	this.val9 = val9;
    }

    /**
     * @return the val10
     */
    public String getVal10() {
	return val10;
    }

    /**
     * @param val10 the val10 to set
     */
    public void setVal10(String val10) {
	this.val10 = val10;
    }

    /**
     * @return the val11
     */
    public String getVal11() {
	return val11;
    }

    /**
     * @param val11 the val11 to set
     */
    public void setVal11(String val11) {
	this.val11 = val11;
    }

    /**
     * @return the val12
     */
    public String getVal12() {
	return val12;
    }

    /**
     * @param val12 the val12 to set
     */
    public void setVal12(String val12) {
	this.val12 = val12;
    }

    /**
     * @return the val13
     */
    public String getVal13() {
	return val13;
    }

    /**
     * @param val13 the val13 to set
     */
    public void setVal13(String val13) {
	this.val13 = val13;
    }

    /**
     * @return the val14
     */
    public String getVal14() {
	return val14;
    }

    /**
     * @param val14 the val14 to set
     */
    public void setVal14(String val14) {
	this.val14 = val14;
    }

    /**
     * @return the val15
     */
    public String getVal15() {
	return val15;
    }

    /**
     * @param val15 the val15 to set
     */
    public void setVal15(String val15) {
	this.val15 = val15;
    }

    /**
     * @return the val16
     */
    public String getVal16() {
	return val16;
    }

    /**
     * @param val16 the val16 to set
     */
    public void setVal16(String val16) {
	this.val16 = val16;
    }

    /**
     * @return the val17
     */
    public String getVal17() {
	return val17;
    }

    /**
     * @param val17 the val17 to set
     */
    public void setVal17(String val17) {
	this.val17 = val17;
    }

    /**
     * @return the val18
     */
    public String getVal18() {
	return val18;
    }

    /**
     * @param val18 the val18 to set
     */
    public void setVal18(String val18) {
	this.val18 = val18;
    }

    /**
     * @return the val19
     */
    public String getVal19() {
	return val19;
    }

    /**
     * @param val19 the val19 to set
     */
    public void setVal19(String val19) {
	this.val19 = val19;
    }

    /**
     * @return the val20
     */
    public String getVal20() {
	return val20;
    }

    /**
     * @param val20 the val20 to set
     */
    public void setVal20(String val20) {
	this.val20 = val20;
    }

    /**
     * @return the val21
     */
    public String getVal21() {
	return val21;
    }

    /**
     * @param val21 the val21 to set
     */
    public void setVal21(String val21) {
	this.val21 = val21;
    }

    /**
     * @return the val22
     */
    public String getVal22() {
	return val22;
    }

    /**
     * @param val22 the val22 to set
     */
    public void setVal22(String val22) {
	this.val22 = val22;
    }

    /**
     * @return the val23
     */
    public String getVal23() {
	return val23;
    }

    /**
     * @param val23 the val23 to set
     */
    public void setVal23(String val23) {
	this.val23 = val23;
    }

    /**
     * @return the val24
     */
    public String getVal24() {
	return val24;
    }

    /**
     * @param val24 the val24 to set
     */
    public void setVal24(String val24) {
	this.val24 = val24;
    }

    /**
     * @return the val25
     */
    public String getVal25() {
	return val25;
    }

    /**
     * @param val25 the val25 to set
     */
    public void setVal25(String val25) {
	this.val25 = val25;
    }

    /**
     * @return the val26
     */
    public String getVal26() {
	return val26;
    }

    /**
     * @param val26 the val26 to set
     */
    public void setVal26(String val26) {
	this.val26 = val26;
    }

    /**
     * @return the val27
     */
    public String getVal27() {
	return val27;
    }

    /**
     * @param val27 the val27 to set
     */
    public void setVal27(String val27) {
	this.val27 = val27;
    }

    /**
     * @return the val28
     */
    public String getVal28() {
	return val28;
    }

    /**
     * @param val28 the val28 to set
     */
    public void setVal28(String val28) {
	this.val28 = val28;
    }

    /**
     * @return the val29
     */
    public String getVal29() {
	return val29;
    }

    /**
     * @param val29 the val29 to set
     */
    public void setVal29(String val29) {
	this.val29 = val29;
    }

    /**
     * @return the val30
     */
    public String getVal30() {
	return val30;
    }

    /**
     * @param val30 the val30 to set
     */
    public void setVal30(String val30) {
	this.val30 = val30;
    }

    /**
     * @return the val31
     */
    public String getVal31() {
	return val31;
    }

    /**
     * @param val31 the val31 to set
     */
    public void setVal31(String val31) {
	this.val31 = val31;
    }

    /**
     * @return the val32
     */
    public String getVal32() {
	return val32;
    }

    /**
     * @param val32 the val32 to set
     */
    public void setVal32(String val32) {
	this.val32 = val32;
    }

    /**
     * @return the val33
     */
    public String getVal33() {
	return val33;
    }

    /**
     * @param val33 the val33 to set
     */
    public void setVal33(String val33) {
	this.val33 = val33;
    }

    /**
     * @return the val34
     */
    public String getVal34() {
	return val34;
    }

    /**
     * @param val34 the val34 to set
     */
    public void setVal34(String val34) {
	this.val34 = val34;
    }

    /**
     * @return the val35
     */
    public String getVal35() {
	return val35;
    }

    /**
     * @param val35 the val35 to set
     */
    public void setVal35(String val35) {
	this.val35 = val35;
    }

    /**
     * @return the val36
     */
    public String getVal36() {
	return val36;
    }

    /**
     * @param val36 the val36 to set
     */
    public void setVal36(String val36) {
	this.val36 = val36;
    }

    /**
     * @return the val37
     */
    public String getVal37() {
	return val37;
    }

    /**
     * @param val37 the val37 to set
     */
    public void setVal37(String val37) {
	this.val37 = val37;
    }

    /**
     * @return the val38
     */
    public String getVal38() {
	return val38;
    }

    /**
     * @param val38 the val38 to set
     */
    public void setVal38(String val38) {
	this.val38 = val38;
    }

    /**
     * @return the val39
     */
    public String getVal39() {
	return val39;
    }

    /**
     * @param val39 the val39 to set
     */
    public void setVal39(String val39) {
	this.val39 = val39;
    }

    /**
     * @return the val40
     */
    public String getVal40() {
	return val40;
    }

    /**
     * @param val40 the val40 to set
     */
    public void setVal40(String val40) {
	this.val40 = val40;
    }

    public String getIpuser() {
	return ipuser;
    }

    public void setIpuser(String ipuser) {
	this.ipuser = ipuser;
    }
}
