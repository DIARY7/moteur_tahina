package reporting;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdtReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimplePptxReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * Cette fonction sert à exporter un rapport 
 * vers différents formats de fichier(PDF, XLSX,DOCX, ODT)
 * à l'aide de JasperPrint.
 * <p>Par exemple on a un template fichier <code>carte_etudiant.jasper</code>, on va génerer cette carte en pdf
 * avec les informations comme suit:</p>
 * <pre>
 *  OutputStream fichierDest = new FileOutputStream("chemin/vers/resultat.pdf");
 *  Map<String,Object> map = new HashMap<String,Object>();
 *  map.put("NOM","RAKOTONAIVO");
 *  map.put("PRENOM","Tiako");
 *  map.put("NUMERO_ETUDIANT","ETU000999");
 *  List datasource = new ArrayList();
 *  new ReportingUtils().PDF("/chemin/vers/carte_etudiant.jasper",datasource,map,fichierDest);
 * </pre>
 * 
 * @author BICI
 */
    public class ReportingUtils {

    /**
     * Constructeur par defaut
     */
    public ReportingUtils() {
    }

    private JasperPrint jasperPrint = null;


    /**
     * Sert à exporter au format format PDF
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @throws JRException
     * @throws IOException
     */
    public void PDF(String reportPath, List dataSource, Map param, OutputStream out) throws JRException, IOException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, ReportType.PDF, out);
    }

    /**
     * Sert à exporter au format DOCX
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @throws JRException
     * @throws IOException
     */
    public void DOCX(String reportPath, List dataSource, Map param, OutputStream out) throws JRException, IOException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, ReportType.DOCX, out);
    }

    /**
     * Sert à exporter au format XLSX
      * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @throws JRException
     * @throws IOException
     */
    public void XLSX(String reportPath, List dataSource, Map param, OutputStream out) throws JRException, IOException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, ReportType.XLSX, out); 
        //exportReport(jasperPrint, ReportType.XLSX, out);
    }

     /**
     * Sert à exporter au format ODT
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @throws JRException
     * @throws IOException
     */
    public void ODT(String reportPath, List dataSource, Map param, OutputStream out) throws JRException, IOException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, ReportType.ODT, out);
    }

    /**
     * Sert à exporter un format PPT
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @throws JRException
     * @throws IOException
     */
    public void PPT(String reportPath, List dataSource, Map param, OutputStream out) throws JRException, IOException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, ReportType.PPT, out);
    }


    public void export(String reportPath, List dataSource, Map param, OutputStream out, ReportType reportType) throws JRException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReport(jasperPrint, reportType, out);
    }

    /**
     * Cette fonction appelle la methode :
     * <pre>exportReportSpecPageStart(JasperPrint jasperPrint, OutputStream out, int pageStart)</pre>
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables
     * @param out flux de sortie IO pour l'exportation
     * @param pageIndex page de début pour la géneration
     * @throws JRException
     */
    public void export(String reportPath, List dataSource, Map param, OutputStream out, int pageIndex) throws JRException {
        jasperPrint = fillReport(reportPath, dataSource, param);
        exportReportSpecPageStart(jasperPrint, out, pageIndex);
    }

    /**
     * Permet à remplir le rapport
     * @param reportPath chemin pour mettre le rapport apres avoir exporter
     * @param dataSource données bouclées pour les détails on peut mettre vide si simple report sans detail
     * @param param mappage des paramètres et objets pour les variables 
     * @return objet Jasper avec les données mis en place
     * @throws JRException
     */
    public JasperPrint fillReport(String reportPath, List dataSource, Map param) throws JRException {
        if (dataSource == null || dataSource.isEmpty()) {
            return JasperFillManager.fillReport(reportPath, param, new JREmptyDataSource());
        } else {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataSource);
            return JasperFillManager.fillReport(reportPath, param, beanCollectionDataSource);
        }
    }


    /**
     * Exporter un rapport JasperPrint vers différents formats de fichier (PDF, DOCX, XLSX, PPT, ODT)
     * @param jasperPrint objet jasper avec les données initialisées
     * @param reporType les types de report(PDF, DOCX, XLSX, PPT, ODT)
     * @param out L'objet OutputStream dans lequel le rapport exporté sera écrit.
     * @throws JRException
     */
    public void exportReport(JasperPrint jasperPrint, ReportType reporType, OutputStream out) throws JRException {
        try{ 
            Exporter jRExporter = null;
            switch (reporType) {
                case PDF:
                    jRExporter = new JRPdfExporter();
                    ReportExportConfiguration configuration = new SimplePdfReportConfiguration();
                    jRExporter.setConfiguration(configuration);
                    break;

                case DOCX:
                    jRExporter = new JRDocxExporter();
                    SimpleDocxReportConfiguration configurationDocx = new SimpleDocxReportConfiguration();
                    break;

                case XLSX:
                    jRExporter = new JRXlsxExporter();
                    SimpleXlsxReportConfiguration configurationXLS = new SimpleXlsxReportConfiguration();
                    configurationXLS.setDetectCellType(true);//Set configuration as you like it!!
                    configurationXLS.setCollapseRowSpan(false);
                    jRExporter.setConfiguration(configurationXLS);
                    break;

                case PPT:
                    jRExporter = new JRPptxExporter();
                    SimplePptxReportConfiguration configurationPpt = new SimplePptxReportConfiguration();
                    break;

                case ODT:
                    jRExporter = new JROdtExporter();
                    SimpleOdtReportConfiguration configurationOdt = new SimpleOdtReportConfiguration();
                    break;

            }
            jRExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jRExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            jRExporter.exportReport();
        }
        catch(JRException jreException){
            jreException.printStackTrace();
            throw jreException;
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        
    }

    /**
     * Il s'agit d'une méthode Java qui exporte un objet JasperPrint
     * sous forme de rapport PDF à partir d'un numéro de page spécifié.
     * @param jasperPrint L'objet JasperPrint qui contient le rapport à exporter.
     * @param out L'objet OutputStream dans lequel le rapport exporté sera écrit.
     * @param pageStart le numéro de page où l'exportation doit commencer.
     * @throws JRException
     */
    public void exportReportSpecPageStart(JasperPrint jasperPrint, OutputStream out, int pageStart) throws JRException {    	
    	try {
    		Exporter jRExporter = new JRPdfExporter();
    		SimplePdfReportConfiguration configuration = new SimplePdfReportConfiguration();
            configuration.setStartPageIndex(pageStart);
            jRExporter.setConfiguration(configuration);   
            jRExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jRExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            jRExporter.exportReport();
    	} catch(JRException jreException){
            jreException.printStackTrace();
            throw jreException;
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public enum ReportType {
        PDF, DOCX, XLSX, ODT, PPT, XLS
    }
    /**
     * @deprecated
     * @param reportType
     * @return
     */
    private String setName(ReportType reportType) {

        return null;

    }
}
