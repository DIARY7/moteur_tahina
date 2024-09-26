/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import bean.Constante;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Cette classe permet à l'importaion du fichier 
 * @author BICI
 */
public class UtilUpload {

   /**
    * Cette fonction sert à importer un fichier avec nom et chemin desirée
    * @param request la requete HTTP
    * @param response la réponse HTTP
    * @param fileName_p nom du fichier
    * @param redir chemin pour mettre le fichier
    * @return le nom du fichier vient d'importer
    * @throws IOException
    * @throws ServletException
    */
    public String fileUploadWithDesiredFilePathAndName(HttpServletRequest request,HttpServletResponse response,String fileName_p,String redir) throws IOException,ServletException
        {
          
          InputStream inputStream = null;
          FileOutputStream outputStream =null;
          String fullName="";
           String newFileName="";
           boolean efaNandalo=false;
          try
           {
             for (Part part : request.getParts()) 
               {
                   if(!efaNandalo){
                        inputStream = request.getPart(part.getName()).getInputStream();
                        int i = inputStream.available();
                        byte[] b  = new byte[i];
                        inputStream.read(b);
                        String fileName = "";
                        String partHeader = part.getHeader("content-disposition");
                        for (String temp : part.getHeader("content-disposition").split(";")) 
                           {
                             if (temp.trim().startsWith("filename")) 
                              {
                                 fileName=temp.substring(temp.indexOf('=') + 1).trim().replace("", "");
                              }
                           }
                      if(fileName_p!=null && !fileName_p.isEmpty()){
                          String extension= fileName.substring(fileName.indexOf(".")).replace("\"", "");
                           newFileName = (fileName_p+extension).replace("\"", "");
                      }else{
                          newFileName = fileName.replace("\"", "");
                      }
                        String uploadDir=Constante.uploadDirLocation;
                        fullName=uploadDir +"/"+newFileName;
                        outputStream = new FileOutputStream(fullName);
                        outputStream.write(b);
                        inputStream.close();
                     }
                   efaNandalo=true;
                     System.out.println("File Uploaded successfully in "+fullName);
                   }
                  
            }
          catch(Exception e)
            {
               e.printStackTrace();
            }
          finally
            {
                if(inputStream!=null)
                   {   
                      try{ inputStream.close(); } catch(Exception e){ e.printStackTrace(); } 
                   }
                if(outputStream!=null)
                   {   
                      try{ outputStream.close(); } catch(Exception e){ e.printStackTrace(); } 
                   }
                if(redir!=null && !redir.isEmpty()){
                     response.sendRedirect(redir);
                }
               
            }
          return newFileName;
        }

   /**
    * Cette fonction permet d'avoir l'url de l'image dans le serveur 
    * @param urlImage 
    * @return
    */     
    public static String getImagePathServ(String urlImage){
        return Constante.uploadedDir + "/"+urlImage;
    }
}
