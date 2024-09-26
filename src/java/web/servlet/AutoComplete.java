/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;

import bean.CGenUtil;
import bean.ClassMAPTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 26134
 */
@WebServlet(name = "autocomplete", urlPatterns = {"/autocomplete"})
public class AutoComplete extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Autocomplete</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Autocomplete at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            PrintWriter out=null;
            response.setContentType("application/json");
        try
        {
            out = response.getWriter();
            String libelle=request.getParameter("libelle");
            String nomTable=request.getParameter("nomTable");
            String classe=request.getParameter("classe");
            String colFiltre=request.getParameter("colFiltre");
            String valeur=request.getParameter("valeur");
            String affiche=request.getParameter("affiche");
            boolean useMotcle = Boolean.valueOf(request.getParameter("useMotcle"));
            String valeurRetour = request.getParameter("champRetour");
            out.println(getContenu(libelle,nomTable,classe,colFiltre,valeur,affiche,useMotcle,valeurRetour));
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
    
    public static String getContenu(String valeurFiltre,String nomTable,String nomClasse,String nomColoneFiltre,String nomColvaleur,String nomColAffiche,boolean useMotcle,String colRetour) throws Exception
    {
        ClassMAPTable filtre=(ClassMAPTable)Class.forName(nomClasse).newInstance();
        filtre.setNomTable(nomTable);
        String rekety ="";
        String requete;
        if (useMotcle) {
            rekety ="SELECT * FROM " + nomTable + " WHERE "+ CGenUtil.makeWhereMotsCles(filtre, valeurFiltre);
        } else {
            rekety = "SELECT * FROM " + nomTable + " WHERE UPPER(" + nomColoneFiltre + ") LIKE '%" + valeurFiltre.toUpperCase() + "%'";
        }
        System.out.println(rekety);
        System.out.println(filtre);
        ClassMAPTable listee[]=(ClassMAPTable[])CGenUtil.rechercher(filtre , rekety);
        System.out.println(listee.length);
        String valeur="[";
        for(int i=0;i<listee.length;i++)
        {
            ClassMAPTable to=listee[i];
            String[] mts=to.getMotCles();
            String valeurId=(String)CGenUtil.getValeurFieldByMethod(to, nomColvaleur);
            String valeurAffiche="";
            String valeurRetour="";
            if (useMotcle) {
                valeurAffiche=CGenUtil.getValeurFieldByMotCle(to);
		  if(colRetour!=null && !colRetour.isEmpty())
		  {
		      valeurRetour=CGenUtil.getValeurFieldByMotCle(to, colRetour.split(";") , ";");
		  }
            } else {
               Object val=CGenUtil.getValeurFieldByMethod(to, nomColAffiche);
                valeurAffiche=(val.toString()).replace("\n", " ");
            }
            
            valeur+="{\"id\":\"" + valeurId+"\",\"valeur\":\"" + valeurAffiche+"\",\"retour\":\"" + valeurRetour+"\"}";
            if(i<listee.length-1)valeur+=",";
        }
        valeur+="]";
        return("{\"valeure\":" + valeur +"}");
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
