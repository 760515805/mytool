package com.chenhj.mytool.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;


public class PdfUtil {
	private static final Logger LOG = LoggerFactory.getLogger(PdfUtil.class);
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
	                String zipBasicPath = basic+File.separator+"zip";
	                if (createDirectory(zipBasicPath)) {
	                	String zipPath = zipBasicPath+ File.separator +imagePDFName+".zip";
	                	FileOutputStream fos1 = new FileOutputStream(new File(zipPath));
	                	ZipUtils.toZip(imgFolderPath, fos1,true);
	                	LOG.info("PDF文档转PNG图片成功！");  
	                	return zipPath;
	                }
	            } else {  
	            	LOG.info("PDF文档转PNG图片失败：" + "创建" + imgFolderPath + "失败");  
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
	    public static String toPdf(List<File> files, String basicPath) throws Exception {
	    	FileOutputStream fos = null;
	    	try {
	    		String pdfFilePathPrefix = basicPath +File.separator+DateUtils.getNowTime("yyyyMMdd")+ File.separator + "img2pdf"+File.separator;  
	            if(null==files||files.isEmpty()) return "";
	    		//取文件中第一个文件取为PDF文件的名字
	            String pd = files.get(0).getName();
	            int dot = pd.lastIndexOf('.');  
	            //文件名称
	            String pdfName = pd.substring(0, dot).trim(); // 获取图片文件名  
	            pdfName = pdfName+".pdf";
	            String pdfPath = pdfFilePathPrefix+File.separator+pdfName;
	            LOG.info("PDF地址：" + pdfPath);  
	            File f = new File(pdfPath);
	            if(!f.getParentFile().exists()){
	            	f.getParentFile().mkdirs();
	            }
	    		// 图片地址
	            String imagePath = null;
	            // 输入流
	            fos = new FileOutputStream(pdfPath);
	            // 第一步：创建一个document对象。
	            Document doc = new Document();
	            doc.setMargins(0, 0, 0, 0);
	            // 第二步：
	            // 创建一个PdfWriter实例，
	            PdfWriter.getInstance(doc, fos);
	         // 第三步：打开文档。
	            doc.open();
	            // 循环获取图片文件夹内的图片
	            for (File file1 : files) {
	                if (file1.getName().toLowerCase().endsWith(".png")
	                        || file1.getName().toLowerCase().endsWith(".jpg")
	                        || file1.getName().toLowerCase().endsWith(".gif")
	                        || file1.getName().toLowerCase().endsWith(".jpeg")
	                        || file1.getName().toLowerCase().endsWith(".tif")) {
	                     System.out.println(file1.getName());
	                    imagePath = file1.getAbsolutePath();
	                    if(!file1.exists())continue;
	                    // 读取图片流
	                    // 实例化图片
	                    Image image = Image.getInstance(imagePath);
	    	          //  float scalePercentage = (72 / 300f) * 100.0f;  
	    	          //  image.scalePercent(scalePercentage, scalePercentage);  
	    	            image.setAlignment(Image.ALIGN_CENTER);
	    	            // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
	                    doc.setPageSize(new Rectangle(image.getWidth(),image.getHeight()));
	                    doc.newPage();
	                    // 添加图片到文档
	                    doc.add(image);
	                }
	            }
	            // 关闭文档
	          doc.close();
	            
	            return pdfPath;
	        } catch (IOException e) {
	            throw e;
	        } catch (BadElementException e) {
	        	throw e;
	        } catch (DocumentException e) {
	        	throw e;
	        }finally {
				if(fos!=null)fos.close();
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
        LOG.info("pdf转换word成功！");
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
		//String aa = "D:/111.pdf";
		File file1 = new File("F:/test/temp/20180615/1.jpg");
		File file2 = new File("F:/test/temp/20180615/1i8a.jpg");
//		System.out.println(file.exists());
//		if(file.exists()){
		//	System.out.println(toImg(file,"F:/test"));
//		}else{
//			//file.createNewFile();
//			System.out.println(file.getAbsolutePath());
//		}
		
		String aa1[]={"F:/test/temp/20180615/1.jpg","D:/111.pdf"};
		
		File file3 = new File("D:/\\20180618\\img2pdf\\\\1.pdf");
		
		if(!file1.exists())System.out.println("不存在");
		if(!file3.exists())System.out.println("不存在");
		//pdf2Image(file,"F:/test",296);
		List<File> list = new ArrayList<File>();
		list.add(file1);
		list.add(file2);
//		long time1 = System.currentTimeMillis();
      //  toPdf(list, "D:/");
		//pdf2Word(file3,);
		//changC.MCombineJPG2PDF(aa1,"D:/111.pdf");
//        long time2 = System.currentTimeMillis();
//        int time = (int) ((time2 - time1)/1000);
//        System.out.println("执行了："+time+"秒！");
		//pdf2Word(file,"F:/test");
		
	}
}
