package com.chenhj.mytool.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {
	/**
	 * @param PdfFilePath  PDF文件
	 * @param dstImgFolder 图片文件夹
	 * @param dpi 默认296 dpi越大转换后越清晰，相对转换速度越慢
	 * @return 返回压缩包地址
	 * @throws IOException 
	 */
	 public static String pdf2Image(File PdfFilePath, String dstImgFolder, int dpi) throws IOException {  
	        //List<File> list = new ArrayList<File>();
		    
		    PDDocument pdDocument;  
	        try {  
				//File imgP = new File(dstImgFolder);
				if(!createDirectory(dstImgFolder))throw new IOException(dstImgFolder+"创建失败...");
	          //  String imgPDFPath = PdfFilePath.getParent();  
	            int dot = PdfFilePath.getName().lastIndexOf('.');  
	            //文件名称
	            String imagePDFName = PdfFilePath.getName().substring(0, dot); // 获取图片文件名  
	            String basic = dstImgFolder+File.separator+DateUtils.getNowTime("yyyyMMdd");  
	            //图片文件夹
	            String imgFolderPath =basic+File.separator+"pdf2img"+File.separator+imagePDFName;  
	            if (createDirectory(imgFolderPath)) {  
	                pdDocument = PDDocument.load(PdfFilePath);  
	                PDFRenderer renderer = new PDFRenderer(pdDocument);  
	                /* dpi越大转换后越清晰，相对转换速度越慢 */  
	                int pages = pdDocument.getNumberOfPages();
	                StringBuffer imgFilePath = null;  
	                for (int i = 0; i < pages; i++) {  
	                    String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;  
	                    imgFilePath = new StringBuffer();  
	                    imgFilePath.append(imgFilePathPrefix);  
	                    imgFilePath.append("_");  
	                    imgFilePath.append(String.valueOf(i + 1));  
	                    imgFilePath.append(".png");  
	                    File dstFile = new File(imgFilePath.toString());  
	                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);  
	                    ImageIO.write(image, "png", dstFile);  
	                }  
	                //转换完成弄成压缩包
	                String zipPath = basic+File.separator+"zip";
	                if (createDirectory(zipPath)) {
	                	FileOutputStream fos1 = new FileOutputStream(new File(zipPath+ File.separator +imagePDFName+".zip"));
	                	ZipUtils.toZip(imgFolderPath, fos1,true);
	                	System.out.println("PDF文档转PNG图片成功！");  
	                	return zipPath;
	                }
	            } else {  
	                System.out.println("PDF文档转PNG图片失败：" + "创建" + imgFolderPath + "失败");  
	                return null;
	            }  
	        } catch (IOException e) {  
	            throw e;
	        }  
	        return null;
	    } 
	 	/**
	     * 图片转PDF
	     * @param imageFolderPath
	     *            图片文件夹地址
	     * @param pdfPath
	     *            PDF文件保存地址
	     * @throws IOException 
	     * 
	     */
	    public static void toPdf(List<File> files, String pdfPath) throws Exception {
	        try {
	            // 图片文件夹地址
	            // String imageFolderPath = "D:/Demo/ceshi/";
	            // 图片地址
	            String imagePath = null;
	            // PDF文件保存地址
	            // String pdfPath = "D:/Demo/ceshi/hebing.pdf";
	            // 输入流
	            FileOutputStream fos = new FileOutputStream(pdfPath);
	            // 创建文档
	            Document doc = new Document(null, 0, 0, 0, 0);
	            //doc.open();
	            // 写入PDF文档
	            PdfWriter.getInstance(doc, fos);
	            // 读取图片流
	            BufferedImage img = null;
	            // 实例化图片
	            Image image = null;
	            // 获取图片文件夹对象
	           // File file = new File(imageFolderPath);
	           // if(!file.exists()){
	            //	System.out.println("不存在");
	            //	return;
	            //}
	           // File[] files = file.listFiles();
	            // 循环获取图片文件夹内的图片
	            for (File file1 : files) {
	                if (file1.getName().toLowerCase().endsWith(".png")
	                        || file1.getName().toLowerCase().endsWith(".jpg")
	                        || file1.getName().toLowerCase().endsWith(".gif")
	                        || file1.getName().toLowerCase().endsWith(".jpeg")
	                        || file1.getName().toLowerCase().endsWith(".tif")) {
	                    // System.out.println(file1.getName());
	                   // imagePath = imageFolderPath +File.separator+ file1.getName();
	                   // File imgfile = new File(imagePath);
	                    if(!file1.exists())return;
	                    System.out.println(file1.getName());
	                    // 读取图片流
	                    img = ImageIO.read(file1);
	                    // 根据图片大小设置文档大小
	                    doc.setPageSize(new Rectangle(img.getWidth(),img.getHeight()));
	                    // 实例化图片
	                    image = Image.getInstance(imagePath);
	                    // 添加图片到文档
	                    doc.open();
	                    doc.add(image);
	                }
	            }
	            // 关闭文档
	          //  doc.close();
	        } catch (IOException e) {
	            throw e;
	        } catch (BadElementException e) {
	        	throw e;
	        } catch (DocumentException e) {
	        	throw e;
	        }
	}
	 	/**
	     * 图片转PDF
	     * @param imageFolderPath
	     *            图片文件夹地址
	     * @param pdfPath
	     *            PDF文件保存地址
	     * @throws IOException 
	     * 
	     */
	    public static void toPdf(String imageFolderPath, String pdfPath) throws Exception {
	        try {
	            // 图片文件夹地址
	            // String imageFolderPath = "D:/Demo/ceshi/";
	            // 图片地址
	            String imagePath = null;
	            // PDF文件保存地址
	            // String pdfPath = "D:/Demo/ceshi/hebing.pdf";
	            // 输入流
	            FileOutputStream fos = new FileOutputStream(pdfPath);
	            // 创建文档
	            Document doc = new Document(null, 0, 0, 0, 0);
	            //doc.open();
	            // 写入PDF文档
	            PdfWriter.getInstance(doc, fos);
	            // 读取图片流
	            BufferedImage img = null;
	            // 实例化图片
	            Image image = null;
	            // 获取图片文件夹对象
	            File file = new File(imageFolderPath);
	            if(!file.exists()){
	            	System.out.println("不存在");
	            	return;
	            }
	            File[] files = file.listFiles();
	            // 循环获取图片文件夹内的图片
	            for (File file1 : files) {
	                if (file1.getName().toLowerCase().endsWith(".png")
	                        || file1.getName().toLowerCase().endsWith(".jpg")
	                        || file1.getName().toLowerCase().endsWith(".gif")
	                        || file1.getName().toLowerCase().endsWith(".jpeg")
	                        || file1.getName().toLowerCase().endsWith(".tif")) {
	                    // System.out.println(file1.getName());
	                    imagePath = imageFolderPath +File.separator+ file1.getName();
	                    File imgfile = new File(imagePath);
	                    if(!imgfile.exists())return;
	                    System.out.println(file1.getName());
	                    // 读取图片流
	                    img = ImageIO.read(imgfile);
	                    // 根据图片大小设置文档大小
	                    doc.setPageSize(new Rectangle(img.getWidth(), img
	                            .getHeight()));
	                    // 实例化图片
	                    image = Image.getInstance(imagePath);
	                    // 添加图片到文档
	                    doc.open();
	                    doc.add(image);
	                }
	            }
	            // 关闭文档
	          //  doc.close();
	        } catch (IOException e) {
	            throw e;
	        } catch (BadElementException e) {
	        	throw e;
	        } catch (DocumentException e) {
	        	throw e;
	        }
	}
	public static void pdf2Word(File pdffile,String docpath) throws Exception{
		PDDocument doc=PDDocument.load(pdffile);
        int pagenumber=doc.getNumberOfPages();
        String pdfName = pdffile.getName();
        pdfName = pdfName.substring(0, pdfName.lastIndexOf("."));
//      String dirName = "D:\\pdf\\";// 创建目录D:\\pdf\\a.doc
        String dirName = docpath;// 创建目录D:\\pdf\\a.doc
        //createDir(dirName);// 调用方法创建目录
        String fileName = dirName+File.separator+pdfName + ".doc";// 创建文件
        createFile(fileName);
        FileOutputStream fos=new FileOutputStream(fileName);
        Writer writer=new OutputStreamWriter(fos,"UTF-8");
        PDFTextStripper stripper=new PDFTextStripper();

//      doc.addSignature(arg0, arg1, arg2);

        stripper.setSortByPosition(true);//排序
        stripper.setWordSeparator("");//pdfbox对中文默认是用空格分隔每一个字，通过这个语句消除空格（视频是这么说的）
        stripper.setStartPage(1);//设置转换的开始页
        stripper.setEndPage(pagenumber);//设置转换的结束页
        stripper.writeText(doc,writer);
        writer.close();
        doc.close();
        System.out.println("pdf转换word成功！");
	}
	 private static boolean createFile(String folder) throws IOException {  
	        File file = new File(folder);  
	        if (file.exists()) {  
	            return true;  
	        } else {  
	            return file.createNewFile();
	        }  
	    } 
	 private static boolean createDirectory(String folder) {  
	        File dir = new File(folder);  
	        if (dir.exists()) {  
	            return true;  
	        } else {  
	            return dir.mkdirs();  
	        }  
	    } 
	public static void main(String[] args) throws Exception {
		String aa = "D:/111.pdf";
		File file = new File(aa);
//		System.out.println(file.exists());
//		if(file.exists()){
		//	System.out.println(toImg(file,"F:/test"));
//		}else{
//			//file.createNewFile();
//			System.out.println(file.getAbsolutePath());
//		}
		pdf2Image(file,"F:/test",296);
		
//		long time1 = System.currentTimeMillis();
        toPdf("E:/BaiduNetdiskDownload", "D:/222.pdf");
//        long time2 = System.currentTimeMillis();
//        int time = (int) ((time2 - time1)/1000);
//        System.out.println("执行了："+time+"秒！");
		//pdf2Word(file,"F:/test");
		
	}
}
