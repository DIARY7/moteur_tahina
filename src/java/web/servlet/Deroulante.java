/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;

import bean.CGenUtil;
import bean.TypeObjet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.*;
import affichage.Page;
/**
 *
 * @author tahina
 */
@WebServlet(name = "Deroulante", urlPatterns = {"/Deroulante"})
public class Deroulante extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out=null;
        try
        {
            processRequest(request, response);
            out = response.getWriter();
            
            String nomClasse=request.getParameter("nomClasse");
            if(nomClasse==null||nomClasse.compareToIgnoreCase("")==0)nomClasse="bean.TypeObjet";
            
            String nomTable=request.getParameter("nomTable");
            if(nomTable==null||nomTable.compareToIgnoreCase("")==0)nomTable="place";
            
            String nomColoneFiltre=request.getParameter("nomColoneFiltre");
            if(nomColoneFiltre==null||nomColoneFiltre.compareToIgnoreCase("")==0)nomColoneFiltre="desce";
            
            String valeurFiltre=request.getParameter("valeurFiltre");
            if(valeurFiltre==null||valeurFiltre.compareToIgnoreCase("")==0)valeurFiltre=request.getParameter("desce");
            
            String nomColvaleur=request.getParameter("nomColvaleur");
            if(nomColvaleur==null||nomColvaleur.compareToIgnoreCase("")==0)nomColvaleur="id";
            
            String nomColAffiche=request.getParameter("nomColAffiche");
            if(nomColAffiche==null||nomColAffiche.compareToIgnoreCase("")==0)nomColAffiche="val";
            
            String action = request.getParameter("action");
            
            if(action != null && action.equalsIgnoreCase("wittiret")){
                out.println(Page.getContenuServletWithTiret(valeurFiltre,nomTable,nomClasse,nomColoneFiltre,nomColvaleur, nomColAffiche, true));
            }else{
                out.println(Page.getContenuServlet(valeurFiltre,nomTable,nomClasse,nomColoneFiltre,nomColvaleur, nomColAffiche));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(out!=null)out.close();
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
        processRequest(request, response);
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
