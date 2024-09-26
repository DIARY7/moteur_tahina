/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;

/**
 * @deprecated  {@link bean.UploadPj}
 * @author BICI
 */
public class UploadCv extends  UploadPj
{

    public UploadCv() 
    {
        this.setNomTable("CV_SCAN_UPLOAD");
    }

    
     public void construirePK(Connection c) throws Exception
     {
        this.preparePk("CVSCAN", "getsequploadscancv");
        this.setId(makePK(c));
    }
    
    
    
}
