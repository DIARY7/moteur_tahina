package qrcode.generator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;

/**
 * Responsable de la créeation de l'image de code QR
 * <h3>Exemple d'utilisation</h3>
 * <pre>
 * GeneratorQrCode g = new GeneratorQrCode(qrcode);
	g.createImage(g.getQrcode()+"qrCodeAccuse.jpg");
 * </pre>
 * @author BICI
 */
public class GeneratorQrCode extends JPanel{
	private String text=""; 
        private BufferedImage image;
        java.util.Properties prop = configuration.CynthiaConf.load();
        private String qrcode = prop.getProperty("cdnQrCode");

		/**
		 * L'emplacement du QrCode
		 * @return le QrCode
		 */
        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
           
		/**
		 * Constructeur
		 */
        public GeneratorQrCode() {
        }
	
		/**
		 * Générer un QR code
		 * @param text qui représente le texte qui doit être crypté utilisé dans la
		 * methode <pre>Cryptee</pre>
		 * @throws Exception 
		 */
        public GeneratorQrCode(String text) throws Exception{
			this.text=Cryptee(text);
			this.setSize(300,300);
			try {          
					image = ImageIO.read(new File(qrcode+"logo.jpg"));
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
                
		}
	
		/**
		 * Cette méthode génère une image de code QR en fonction des données d'entrée et de la taille
		 * @param data represente les données d'entrée à encoder dans le code QR 
		 * @param size la taille de l'image du code QR
		 * @return représentant une image de codeQr encodée
		 * @throws WriterException
		 */
		public BitMatrix generationQR(String data,int size) throws WriterException{
			BitMatrix b=new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
			return b;
		}

	/**
	 * Permet de crypté une texte
	 * @param txtClair qui représente le texte qui doit être crypté
	 * @return le texte crypté
	 */
	public String Cryptee(String txtClair){
        String textCrypte="";
        for (int i = 0; i < txtClair.length(); i++)
        {
        	if (i==txtClair.length()-1) {
                    Cryptage s = new Cryptage((int)txtClair.charAt(i)-65, 13, 899);
                    textCrypte += "" + s.calculate();
			}
        	else{
                    Cryptage s = new Cryptage((int)txtClair.charAt(i)-65, 13, 899);
                    textCrypte += "" + s.calculate() + ",";
        	}
        }
        return textCrypte;
	}

	/**
	 *  Responsable du colorartion du rendu de l'image du code QR généré
	 *  @param g un contexte graphique
	 */
	public void paint(Graphics g){
		 super.paint(g);
                 
		 BitMatrix bM=null;
			try {
				bM=generationQR(this.text, 300);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			this.setBackground(Color.WHITE);
			g.setColor(Color.BLACK);
			for (int i = 0; i <bM.getWidth() ; i++) {
				for (int j = 0; j < bM.getHeight(); j++) {
					if (bM.get(i, j)) {
						g.fillRect(j, i, 1, 1);
                                            
					}
				}
			}         
	 	}

	/**
	 * Création d'image du code QR et l'enregistre dans un chemin spécifié
	 * @param path chemin de direction pour placer l'image
	 */	
	public void createImage(String path){
		BufferedImage bi=new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g=bi.createGraphics();
		paint(g);
		File outputfile=new File(path);
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode est souvent utilisée dans les applications Swing
	 * pour créer des images de composants à imprimer ou à exporter dans d'autres formats.
	 * @return  l'objet BufferedImage complété
	 */
	public BufferedImage createImages(){
		BufferedImage bi=new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g=bi.createGraphics();
		paint(g);
		return bi;
	}
}
