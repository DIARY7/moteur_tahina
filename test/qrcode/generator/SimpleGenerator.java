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

public class SimpleGenerator extends JPanel{
	private String text=""; 
        private BufferedImage image;
        java.util.Properties prop = configuration.CynthiaConf.load();
        private String qrcode = prop.getProperty("cdnQrCode");
        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
	public SimpleGenerator(String text) throws Exception{
            this.text=text;
            this.setSize(300,300);
            try {          
                    // image = ImageIO.read(new File(getClass().getResource("/Users/anthonyandrianaivoravelona/NetBeansProjects/QRGenerator/src/qrcode/generator/logo.jpg").getFile()));
                        image = ImageIO.read(new File(qrcode+"logo.jpg"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception(ex.getMessage());
                }
	}
	public BitMatrix generationQR(String data,int size) throws WriterException{
		BitMatrix b=new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
		return b;
	}
	
	 public void paint(Graphics g){
		 super.paint(g);
		 BitMatrix bM=null;
			try {
				bM=generationQR(this.text, 300);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                        //g.drawImage(image,bM.getWidth()/2-image.getWidth()/2,bM.getHeight()/2-image.getHeight()/2+10, null);
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
         
	 public BufferedImage createImages(){
			BufferedImage bi=new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g=bi.createGraphics();
			paint(g);
                        return bi;
		}
}
