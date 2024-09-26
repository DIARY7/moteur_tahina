/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.lang.reflect.Field;

/**
 * Representation de la colonne d'une base de données 
 * @author BICI
 */
public class Champ {

    public Champ(Field f) {
        nomClasse = f;
        nomColonne = f.getName();
        this.typeJava = f.getType().getName();
        size = 10;
        this.javaToSql();
    }

    public Champ(Field f, int precision) {
        nomClasse = f;
        nomColonne = f.getName();
        this.typeJava = f.getType().getName();
        size = 10;
        this.setPrecision(precision);
        this.javaToSql();
    }

    public Field getField() {
        Field e;
        return null;
    }

    public Champ(String nomCol, String typ, int val) {
        nomColonne = nomCol;
        type = typ;
        size = val;
        sqlToJava();
    }

    public Champ(String nomCol, String typ, int val, int precis) {
        nomColonne = nomCol;
        type = typ;
        size = val;
        this.setPrecision(precis);
        sqlToJava();
    }

    public Champ(String nomCol, String typ) {
        nomColonne = nomCol;
        type = typ;
    }

    public String getNomColonne() {
        return nomColonne;
    }

    public String getTypeColonne() {
        return type;
    }

    public String getTypeJava() {
        return typeJava;
    }

    public int getSize() {
        return size;
    }
    /**
     * convertir le type sql en type java
     */
    public void sqlToJava() {
        if (type.compareToIgnoreCase("character varying") == 0 || type.compareToIgnoreCase("char") == 0) {
            typeJava = "java.lang.String";
        }
        if (type.compareToIgnoreCase("geometry") == 0 ) {
            typeJava = "org.postgis.PGgeometry";
        }
        if ((type.compareToIgnoreCase("Number") == 0) && (precision == 0) && (size < 10)) {
            typeJava = "int";
        }
        if ((type.compareToIgnoreCase("Number") == 0)  && (precision >= 0) && (size >= 10)) {
            typeJava = "double";
        }
        if ((type.compareToIgnoreCase("Number") == 0)  && ((precision >= 0) || (size >= 10))) {
            typeJava = "double";
        }
        if ((type.compareToIgnoreCase("Long") == 0)) {
            typeJava = "double";
        }
        if (type.compareToIgnoreCase("Date") == 0) {
            typeJava = "java.sql.Date";
        }
        if (type.compareToIgnoreCase("blob") == 0) {
            typeJava = "java.io.InputStream";
        }
        if (type.contains("timestamp")) {
            typeJava = "java.sql.Timestamp";
        }
        if ((type.compareToIgnoreCase("Number") == 0)  && ((precision >= 0) || (size >= 10))) {
            typeJava = "double";
        }

    }
    /**
     * convertir un type java en sql
     */
    public void javaToSql() {
        if (typeJava.compareToIgnoreCase("java.lang.String") == 0 || typeJava.compareToIgnoreCase("char") == 0) {
            type = "Varchar2";
        }
        if (typeJava.compareToIgnoreCase("int") == 0 || typeJava.compareToIgnoreCase("float") == 0 || typeJava.compareToIgnoreCase("Double") == 0) {
            type = "Number";
        }
        if (typeJava.compareToIgnoreCase("java.sql.Date") == 0) {
            type = "Date";
        }
        if (typeJava.compareToIgnoreCase("java.io.InputStream") == 0) {
            type = "blob";
        }
        if (typeJava.compareToIgnoreCase("java.sql.Timestamp") == 0) {
            type = "Timestamp(6)";
        }
        if (typeJava.compareToIgnoreCase("org.postgis.PGgeometry") == 0) {
            type = "geometry";
        }
    }
    /**
     * retourne la précision d'une colonne
     */
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public Field getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(Field nomClasse) {
        this.nomClasse = nomClasse;
    }

    public String toString() {
        return getNomColonne();
    }

    String nomColonne;
    String type;
    String typeJava;
    int size;
    int precision;
    Field nomClasse;
}
