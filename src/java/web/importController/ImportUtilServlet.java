/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.importController;

import affichage.PageUpload;
import bean.ClassMAPTable;
import importUtils.importable.Importable;
import importUtils.Importer;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import user.UserEJB;
import utilitaire.UtilDB;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 * @author Soa
 */
@WebServlet(name = "ImportUtilServlet", urlPatterns = {"/ImportUtilServlet"})
public class ImportUtilServlet extends HttpServlet {

    DiskFileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ClassNotFoundException, ServletException, IOException, FileUploadException, Exception {
        Importer importer = new Importer();
        Importable importable = null;
        Connection c = null;
        HttpSession session = request.getSession(true);
        boolean connexionOuvert = false;
        PrintWriter out = response.getWriter();
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
                connexionOuvert = true;
            }
            UserEJB u = (UserEJB) session.getAttribute("u");
            String bute = "";
            String lien = (String) session.getValue("lien");
           
            List<FileItem> items = upload.parseRequest(request);
            DiskFileItem[] listeChamp = new DiskFileItem[items.size()];
            items.toArray(listeChamp);
            
            PageUpload p = new PageUpload(listeChamp);
            
            FileItem file = null;
            
            ClassMAPTable objet = p.getObjectAvecValeur();
            importable = (Importable) objet;
            
            bute = p.getValeur("bute");
            file = p.getFileItems().get(0);
            
            importer.importFile(importable, file,String.valueOf(u.getUser().getRefuser()), c);
            c.commit();
            response.sendRedirect("pages/"+lien+"?but="+bute);

        }
        catch (Exception e) {
            if (connexionOuvert) {
                try {
                    c.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    out.println("<script language='JavaScript'> alert('" + ex.getMessage() + "');history.back(); </script>");
                }
            }
            e.printStackTrace();
            out.println("<script language='JavaScript'> alert('" + e.getMessage() + "');history.back(); </script>");
            out.println("<script language=\"JavaScript\">history.back()</script>");
        } 
        finally {
            if (connexionOuvert) {
                try {
                    c.close();
                } catch (SQLException ex) {
                    out.println("<script language='JavaScript'> alert('" + ex.getMessage() + "');history.back(); </script>");
                }
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            out.println("<script language=\"JavaScript\">alert('" + ex.getMessage() + "')</script>");
            out.println("<script language=\"JavaScript\">history.back()</script>");
        } catch (NoClassDefFoundError notf) {
            out.println("<script language=\"JavaScript\">alert('" + notf.getMessage() + "')</script>");
            out.println("<script language=\"JavaScript\">history.back()</scr    ipt>");
        } catch (Exception ex) {
            System.out.println("OOOO");
            out.println("<script language=\"JavaScript\">alert('" + ex.getMessage() + "')</script>");
            out.println("<script language=\"JavaScript\">history.back()</script>");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
