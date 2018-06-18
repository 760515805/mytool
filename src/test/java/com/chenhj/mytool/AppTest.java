package com.chenhj.mytool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Unit test for simple App.
 */
public class AppTest {
	 public static void main(String[] args) {  
	        try {  
	            String imagePath = "F:/test/temp/20180615/1.jpg";  
	            
	            String imagePath1 = "F:/test/temp/20180615/1i8a.jpg";  
	            
	            String pdfPath = "D:\\test.pdf";  
	            BufferedImage img = ImageIO.read(new File(imagePath));  
	            FileOutputStream fos = new FileOutputStream(pdfPath);  
	            Document doc = new Document(null, 0, 0, 0, 0);  
	            
	            
	            doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));  
	            Image image = Image.getInstance(imagePath);  
	            float scalePercentage = (72 / 300f) * 100.0f;  
	            image.scalePercent(scalePercentage, scalePercentage);  
	            PdfWriter.getInstance(doc, fos);  
	            doc.open();  
	            doc.add(image);  
	            
                // 读取图片流
                img = ImageIO.read(new File(imagePath1));
                // 根据图片大小设置文档大小
                doc.setPageSize(new Rectangle(img.getWidth(),img.getHeight()));
                // 实例化图片
                image = Image.getInstance(imagePath1);
	            image.scalePercent(scalePercentage, scalePercentage);  
                // 添加图片到文档
                doc.open();
                doc.add(image);
	            
	            
	            
	            
	            doc.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } catch (BadElementException e) {  
	            e.printStackTrace();  
	        } catch (DocumentException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
