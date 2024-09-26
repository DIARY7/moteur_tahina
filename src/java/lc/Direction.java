package lc;

import bean.ClassMAPTable;
import java.sql.Connection;

public class Direction extends ClassMAPTable {
	
	public String idDir;
	public String libelledir;
	public String descdir;
	public double idDirecteur;
	public String abbrevDir;
	public Direction() {
		super.setNomTable("direction");
               
	}
	
	public Direction(String libelledir, String descdir, double idDirecteur, String abbrevDir) throws Exception {
		this.libelledir = libelledir;
		this.descdir = descdir;
		this.idDirecteur = idDirecteur;
		this.abbrevDir = abbrevDir;
		
		super.setNomTable("direction");
               
	}

	public Direction(String idDir, String libelledir, String descdir, double idDirecteur, String abbrevDir) throws Exception {
		this.idDir = idDir;
		this.libelledir = libelledir;
		this.descdir = descdir;
		this.idDirecteur = idDirecteur;
		this.abbrevDir = abbrevDir;		
		super.setNomTable("direction");
               
	}

        @Override
	public String getAttributIDName() {
		return "idDir";
	}

        @Override
	public String getTuppleID() {
		return String.valueOf(idDir);
	}


	public String getAbbrevDir() {
		return abbrevDir;
	}

	public void setAbbrevDir(String abbrevDir) {
		this.abbrevDir = abbrevDir;
	}

	public String getDescdir() {
		return descdir;
	}

	public void setDescdir(String descdi) {
		
		this.descdir = descdi;
	}

	public String getIdDir() {
		return idDir;
	}

	public void setIdDir(String id) {
		this.idDir = id;
	}

	public double getIdDirecteur() {
		return idDirecteur;
	}

	public void setIdDirecteur(double idDirecteur) {
		this.idDirecteur = idDirecteur;
	}

	public String getLibelledir() {
		return libelledir;
	}

	public void setLibelledir(String libelledir) {
		this.libelledir = libelledir;
	}
        
        @Override
         public void construirePK(Connection c) throws Exception{
        super.setNomTable("direction");
        this.preparePk("DIR", "getSeqDirection");
        this.setIdDir(makePK(c));
    }
}