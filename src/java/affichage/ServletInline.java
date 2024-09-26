package affichage;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import bean.ClassMAPTable;
import java.lang.reflect.Field;
import bean.CGenUtil;
import affichage.TableauRecherche;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unascribed
 */
 
@WebServlet(name = "ServletInline", urlPatterns = {"/pdfInline"})
public class ServletInline extends HttpServlet {


  protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
throws ServletException, IOException {
OutputStream os = arg1.getOutputStream();
        try {
            String id= arg0.getParameter("id");
            String classe = arg0.getParameter("classe");
        }
        catch (Exception e) {
                        e.printStackTrace();
                }
        finally{
                os.close();
                }
        }
   /* private void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException 
	{

		File pdfFile = new File(fileName);

		response.setContentType("application/pdf");
		response.setContentLength((int) pdfFile.length());

		FileInputStream fileInputStream = new FileInputStream(pdfFile);
		OutputStream responseOutputStream = response.getOutputStream();
		int bytes;
		while ((bytes = fileInputStream.read()) != -1) {
			responseOutputStream.write(bytes);
		}
	}*/
protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
throws ServletException, IOException {
}
}