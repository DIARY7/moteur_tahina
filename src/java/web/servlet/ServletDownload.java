package web.servlet;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import bean.ClassMAPTable;
import java.lang.reflect.Field;
import bean.CGenUtil;
import affichage.TableauRecherche;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.PageSize;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.List;
//import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.commons.io.FileUtils;
import utilitaire.ChiffreLettre;
import utilitaire.Utilitaire;

/**
 *
 * @author Ravaka
 */
@WebServlet(name = "ServletDownload", urlPatterns = {"/download"})
public class ServletDownload extends HttpServlet {

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
        OutputStream os = arg1.getOutputStream();
        try {
            String ext = arg0.getParameter("ext");
            System.out.println("****** ext"+ext);
            String awhere = arg0.getParameter("awhere");
            if (awhere == null) {
                awhere = "";
            }else{
               System.out.println("================Tonga ato ilay awhere");
               System.out.println("awhere = " + awhere);
            }
            arg1.setContentType("text/plain");
            SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd");
            String d = fd.format(new Date());
            String titre = "export";
            if(arg0.getParameter("titre")!=null)
                titre = arg0.getParameter("titre");
            String fileName = titre+"-" + d + "." + ext;
            arg1.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = arg1.getOutputStream();
            System.out.println(""+arg0.getParameter("titre")+"-" + d + "." + ext);
            String type = arg0.getParameter("donnee");
            ///spat/assets/img/logo-spat-p.png
            String logo = getServletContext().getRealPath(File.separator + "assets" + File.separator + "img" + File.separator + "logo-spat-p.png");
            String logo_min = getServletContext().getRealPath(File.separator + "assets" + File.separator + "img" + File.separator + "logo-spat-p.png");

            if (type.compareTo("0") == 0) {
                System.out.println("0 : ");
                String htmlStringBasPage = "";

                if (arg0.getParameter("recap") != null && arg0.getParameter("nombreEncours") != null) {
                    int nombre = Utilitaire.stringToInt(arg0.getParameter("nombreEncours"));
                    double montant = Utilitaire.stringToDouble(arg0.getParameter("recap"));
                    htmlStringBasPage = UtilitaireFormat.generateBasPage(nombre, montant);
                }
                if (ext.compareTo("xls") == 0) {
                    arg1.setContentType("application/vnd.ms-excel");
                    String htmlStringEntete = UtilitaireFormat.generateEntete(arg0.getParameter("titre"), logo_min);
                    os.write((htmlStringEntete + "" + arg0.getParameter("table").replace('*', '"').replaceAll("\u00A0", " ") + "" + htmlStringBasPage).getBytes());
                }
                else if (ext.compareTo("pdf") == 0) {
                    int estPaysage = 0;
                    arg1.setContentType("application/pdf");
                    if(arg0.getParameter("landscape").equals("1")) estPaysage = 1;
                    //arg1.setHeader("Content-disposition", "attachment; filename=fichepersonne.pdf");
                    File fichier = new File(getServletContext().getRealPath("") + fileName);
                    Document document = null;
//                    ITextRenderer renderer = new ITextRenderer();
//                    renderer.setDocumentFromString(htmlString);
//                    renderer.layout();
//
//                    FileOutputStream fos = new FileOutputStream(fileNameWithPath);
//                    renderer.createPDF(fos);
//                    fos.close();
                    String htmlStringEntete = UtilitaireFormat.generateEntete(arg0.getParameter("titre"), logo);
                    htmlStringEntete += arg0.getParameter("table").replace('*', '"') + "</body></html>";

                    String fileNameWithPath = getServletContext().getRealPath("") + fileName;
                    OutputStream file = new FileOutputStream(new File(fileNameWithPath));
                    if(estPaysage == 1){
                        document = new Document(PageSize.A4.rotate());
                    }else{
                        document = new Document();
                    }
                    try {
                        PdfWriter.getInstance(document, file);
                        document.open();
                        HTMLWorker htmlWorker = new HTMLWorker(document);
                        htmlWorker.parse(new StringReader(htmlStringEntete));
                        htmlWorker.parse(new StringReader(htmlStringBasPage));
                        document.close();
                        file.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("PDF Created!");

                    // This should send the file to browser
                    OutputStream out = arg1.getOutputStream();
                    FileInputStream in = new FileInputStream(fileNameWithPath);

                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.flush();
                    fichier.delete();
                } else {
                    String donnee = arg0.getParameter(ext);
                    //System.out.println(donnee);
                    os.write((donnee).replace('*', '"').replaceAll("\u00A0", " ").getBytes());
                    
                }
                os.flush();

            } else if (type.compareTo("1") == 0) {
                System.out.println("1 : ");
                
                String[] entete = Utilitaire.split(arg0.getParameter("entete"), ";");
                String[] entetelibelle = Utilitaire.split(arg0.getParameter("entetelibelle"), ";");

                ClassMAPTable o = (ClassMAPTable) arg0.getSession().getAttribute("critere");

                /*
                Field[] field = o.getFieldList();
                String entete[] = new String[field.length];
                for (int i = 0; i < entete.length; i++) {
                    entete[i] = field[i].getName();
                    System.out.println(entete[i]);
                }*/
                ClassMAPTable[] c = (ClassMAPTable[]) CGenUtil.rechercher(o, null, null, awhere);
                //System.out.println("c.length:"+c.length);
                TableauRecherche tr = new TableauRecherche(c, entete);
                tr.setLibelleAffiche(entetelibelle);
                //tr.makeHtmlPDF();
                tr.makeHtml();
                String htmlStringBasPage = "";

                if (arg0.getParameter("recap") != null) {
                    int nombre = tr.getData().length;
                    double montant = Utilitaire.stringToDouble(arg0.getParameter("recap"));
                    htmlStringBasPage = UtilitaireFormat.generateBasPage(nombre, montant);
                }

                //System.out.println(donn);
                else if (ext.compareToIgnoreCase("xml") == 0) {
                    os.write(tr.getExpxml().getBytes());
                    //System.out.println(donn+tr.getExpxml());
                }
                else if (ext.compareToIgnoreCase("pdf") == 0) {
                    tr.makeHtmlPDF();
                    int estPaysage = 0;
                    Document document = null;
                    if(arg0.getParameter("landscape").equals("1")) estPaysage = 1;
                    //System.out.println(body);
                    arg1.setContentType("application/pdf");
                    File fichier = new File(getServletContext().getRealPath("") + fileName);

                    String htmlStringEntete = UtilitaireFormat.generateEntete(arg0.getParameter("titre"), logo);
                    htmlStringEntete += tr.getHtmlPDF();

                    String fileNameWithPath = getServletContext().getRealPath("") + fileName;
                    OutputStream file = new FileOutputStream(new File(fileNameWithPath));
                    if(estPaysage == 1){
                        document = new Document(PageSize.A4.rotate());
                    }else{
                        document = new Document();
                    }
                    try {
                        PdfWriter.getInstance(document, file);
                        document.open();
                        HTMLWorker htmlWorker = new HTMLWorker(document);
                        htmlWorker.parse(new StringReader(htmlStringEntete));
                        htmlWorker.parse(new StringReader(htmlStringBasPage));

                        document.close();
                        file.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("PDF Created!");

                    // This should send the file to browser
                    OutputStream out = arg1.getOutputStream();
                    FileInputStream in = new FileInputStream(fileNameWithPath);

                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.flush();
                    fichier.delete();
                } else if (ext.compareToIgnoreCase("csv") == 0) {
                    System.out.println(tr.getExpcsv());
                    System.out.println("OS :: "+os);
                    os.write(tr.getExpcsv().getBytes());
                } else if (ext.compareToIgnoreCase("xls") == 0) {
                    arg1.setContentType("application/vnd.ms-excel");
                    String htmlStringEntete = UtilitaireFormat.generateEntete(arg0.getParameter("titre"), logo_min);
                    os.write((htmlStringEntete + "" + tr.getHtml() + "" + htmlStringBasPage).getBytes());
                }
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
