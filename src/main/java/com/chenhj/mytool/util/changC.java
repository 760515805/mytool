package com.chenhj.mytool.util;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chenhj.mytool.constant.PDFConstants;
/**
 * 
 * @author chenhj
 *
 */
public class changC extends PDFConstants{
	
	private static final Logger LOG = LoggerFactory.getLogger(changC.class);
	
	//合并JPG到PDF
	
	//public static void MCombineJPG2PDF(String[] jpgPath, String pdfPath) {
	public static void MCombineJPG2PDF(List<File> imgList, String pdfPath) throws IOException {
		File outFile = null;
		File jpgFile = null;
		//File tmpFile = null;
		int i = 0;
		int j = 0;
		long fileLen = 0L;
		int pdfCount = 0;
		//int count = imgList.size();
		StringBuffer imgID = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		StringBuffer buf = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		int objectOffset[] = new int[1024];
		int totalLen = 0, objectCount = 1;
		int width = 0, height = 0;
		double scale = 1.0;
		outFile = new File(pdfPath);
		try {
			outFile.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		// write pdf header
		sprintf(tmp, PDF_HEADER, PDF_VERESION);
		totalLen += tmp.length();
		buffer.append(tmp);
		tmp = new StringBuffer();
		// write pdf catalog
		sprintf(tmp, PDF_CATALOG, objectCount, PDF_VERESION, objectCount + 2);
		objectOffset[objectCount++] = totalLen;
		totalLen += tmp.length();
		buffer.append(tmp);
		tmp = new StringBuffer();
		// write pdf info
		sprintf(tmp, PDF_INFO, objectCount, PDF_AUTHOR, PDF_CREATER,
				PDF_PRODUCER);
		objectOffset[objectCount++] = totalLen;
		totalLen += tmp.length();
		buffer.append(tmp);
		tmp = new StringBuffer();
		
		List<String> flist = new ArrayList<String>();
		List<String> tlist = new ArrayList<String>();
		List<String> dlist = new ArrayList<String>();
		//在合成之前，jpgPath中所有的pdf进行jpg转换后保存到List列表中备用
		//for(i = 0; i < count; i++){
		for(File tmpFile:imgList){
			//tmpFile = new File(jpgPath[i]);
			if(getMIMEType(tmpFile).equals("application/pdf")){
				tlist = JPDF2JPG(tmpFile.getAbsolutePath());
				for(j = 0; j < tlist.size(); j++){
					flist.add(tlist.get(j));
					dlist.add(tlist.get(j));
				}
			}else if(getMIMEType(tmpFile).equals("image/*")){
				flist.add(tmpFile.getAbsolutePath());
			}else{
				LOG.info("Can't combine!");
			}
		}
		// write pdf page
		pdfCount = flist.size();
		for (j = 0; j < pdfCount; j++) {
			sprintf(buf, "%d 0 R ", j * 4 + 4);
		}
		sprintf(tmp, PDF_PAGES, objectCount, buf, pdfCount);
		objectOffset[objectCount++] = totalLen;
		totalLen += tmp.length();
		buffer.append(tmp);
		tmp = new StringBuffer();
		buf = new StringBuffer();
		
		for (j = 0; j < pdfCount; j++) {
			jpgFile = new File(flist.get(j));
			if (!jpgFile.exists()) {
				LOG.info(jpgFile.getName()+"jpg file is no exist\n");
			} else {
				/*if(Util.getMIMEType(jpgFile).equals("application/pdf")){
					//String jpgtmp = jpgFile.getAbsolutePath().substring(0,jpgFile.getAbsolutePath().lastIndexOf("."))+".jpg";
					//System.out.println("jpgtmp="+jpgtmp);
					flist = JPDF2JPG(jpgFile.getAbsolutePath());
					count += flist.size()-1;
					System.out.println(jpgtmp+" is creat ...");
					jpgPath[j]=jpgtmp;
					flist.add(jpgPath[j]);
				}*/
				sprintf(imgID, "Im%d", j + 1);
				// write pdf kids
				sprintf(tmp, PDF_PAGES_KID, objectCount, 3, objectCount + 1,
						imgID, objectCount + 3);
				objectOffset[objectCount++] = totalLen;
				totalLen += tmp.length();
				buffer.append(tmp);
				tmp = new StringBuffer();
				Map<String, Integer> map = getWidthAndHeight(flist.get(j));
				width = (Integer) map.get("width");
				height = (Integer) map.get("height");
				if (width > PDF_X_PIXELS || height > PDF_Y_PIXELS) {
					if (PDF_X_PIXELS * 1.0 / width < PDF_Y_PIXELS * 1.0
							/ height) {
						scale = PDF_X_PIXELS * 1.0 / width;
					} else {
						scale = PDF_Y_PIXELS * 1.0 / height;
					}
				} else {
					 scale = getScale(height,width);
				}

				// write pdf contents
				sprintf(tmp, PDF_CONTENTS, objectCount, objectCount + 1, width
						* scale, height * scale,
						(PDF_X_PIXELS - width * scale) / 2,
						(PDF_Y_PIXELS - height * scale) / 2, imgID);
				objectOffset[objectCount++] = totalLen;
				totalLen += tmp.length();
				buffer.append(tmp);
				tmp = new StringBuffer();

				// write pdf object length
				// PDF_LENGTH "%d 0 obj\n%d\nendobj\n"
				sprintf(tmp, PDF_LENGTH, objectCount, 60);
				objectOffset[objectCount++] = totalLen;
				totalLen += tmp.length();
				buffer.append(tmp);
				tmp = new StringBuffer();
				fileLen = jpgFile.length();

				// write data header
				sprintf(tmp, PDF_JPG_DATA_HEADER, objectCount, fileLen, imgID,
						width, height, 8);
				objectOffset[objectCount++] = totalLen;
				totalLen += tmp.length();
				buffer.append(tmp);
				tmp = new StringBuffer();
				appendMethodA(pdfPath, buffer.toString());
				buffer = new StringBuffer();
				imgID = new StringBuffer();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(flist.get(j));
					byte[] bytes = new byte[fis.available()];
					totalLen += bytes.length;
					fis.read(bytes);
					fis.close();
					appendMethodB(pdfPath, bytes);
				} catch (Exception e2) {
					e2.printStackTrace();
				}finally {
					if(fis!=null)fis.close();
				}

				/**************************************************************/
				// write data ender
				sprintf(tmp, PDF_JPG_DATA_ENDER);
				totalLen += tmp.length();
				appendMethodA(pdfPath, tmp.toString());
				tmp = new StringBuffer();
			}
		}
		sprintf(buf, "%010d 65535 f\r\n", objectOffset[0], 0);

		for (i = 1; i < objectCount; i++) {
			sprintf(buf, "%010d %05d n\r\n", objectOffset[i], 0);
		}

		// write pdf xref
		sprintf(tmp, PDF_XREF, objectCount, buf, objectCount, 1, 2, totalLen);
		objectOffset[objectCount++] = totalLen;
		totalLen += tmp.length();
		appendMethodA(pdfPath, tmp.toString());
		//清除临时生成的图片
		/*for(int k = 0; k <dlist.size(); k++){
			File f = new File(dlist.get(k).toString());
			f.delete();
			System.out.println(dlist.get(k).toString()+" is deleting ...");
		}*/
		tmp = new StringBuffer();
		buf = new StringBuffer();
		LOG.info("CombineJPG2PDF:->"+pdfPath);
	}
	
	
	
	
	//public static  boolean CombineJPGToPDF(String[] jpgPath,String pdfPath,int pdfCount){
	public static  boolean CombineJPGToPDF(String[] jpgPath,String pdfPath,int pdfCount){
		boolean flag = false;
		File outFile = null;
		File jpgFile = null;
		int i = 0;
		int j = 0;
		long fileLen = 0L;
		StringBuffer imgID = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		int objectOffset[] = new int[1024];
		int temp,totalLen = 0,objectCount = 1;
		int width = 0,height = 0,offset;
		StringBuffer buf = new StringBuffer();
		double scale = 1.0;
		InputStream in = null;  
		outFile = new File(pdfPath);
		try {
			outFile.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		//write pdf header
		sprintf(buffer,PDF_HEADER, PDF_VERESION);
		totalLen += buffer.length();
		appendMethodA(pdfPath,buffer.toString());
        buffer = new StringBuffer();
		//write pdf catalog
		//PDF_CATALOG     "%d 0 obj\n<<\n/Type /Catalog\n/Version /%1.1f\n/Pages %d 0 R\n>>\nendobj\n"
        sprintf(buffer, PDF_CATALOG, objectCount, PDF_VERESION, objectCount+2);
        objectOffset[objectCount++] = totalLen;
        totalLen +=buffer.length();
        appendMethodA(pdfPath,buffer.toString());
		buffer = new StringBuffer();
        //write pdf info
		//PDF_INFO        "%d 0 obj\n<<\n/Author (%s)\n/Creator (%s)\n/Producer (%s)\n>>\nendobj\n"
        sprintf(buffer, PDF_INFO, objectCount, PDF_AUTHOR, PDF_CREATER, PDF_PRODUCER);
        objectOffset[objectCount++] = totalLen;
        totalLen += buffer.length();
        appendMethodA(pdfPath,buffer.toString());
        buffer = new StringBuffer();
        //write pdf page
        for(j = 0; j < pdfCount; j++)
        {
            sprintf(buf, "%d 0 R ", j*4+4);
        }
		//PDF_PAGES       "%d 0 obj\n<<\n/Type /Pages\n/Kids [%s]\n/Count %d\n>>\nendobj\n"
        sprintf(buffer, PDF_PAGES, objectCount, buf, pdfCount);
        objectOffset[objectCount++] = totalLen;
        totalLen += buffer.length();
        appendMethodA(pdfPath,buffer.toString());
        buffer = new StringBuffer();
        buf = new StringBuffer();
		
        for(j = 0; j < pdfCount; j++)
        {
            jpgFile = new File(jpgPath[j]);
            if(!jpgFile.exists())
            {
            	LOG.info("jpg file is no exist\n");
            }
            else
            {
                //GetImageResolutions(jpgPath[0], &width, &height);
                sprintf(imgID, "Im%d", j+1);

                //write pdf kids
				//PDF_PAGES_KID   "%d 0 obj\n<<\n/Type /Page\n/Parent %d 0 R\n/MediaBox [0 0 595.276 841.890]\n/CropBox [0 0 595.276 841.890]\n/Contents %d 0 R\n/Resources <<\n/XObject <<\n/%s %d 0 R >>\n/ProcSet [/ImageC]\n>>\n>>\nendobj\n"
                sprintf(buffer, PDF_PAGES_KID, objectCount, 3, objectCount+1, imgID, objectCount+3);
                objectOffset[objectCount++] = totalLen;
                totalLen += buffer.length();
                appendMethodA(pdfPath,buffer.toString());
                buffer = new StringBuffer();
                Map map = getWidthAndHeight(jpgPath[j]);
                width = (Integer) map.get("width");
                height = (Integer) map.get("height");
                
                //System.out.println("Width : Height->"+width+" : "+height);
                //scale = getPercent2(height,width);
                if(width > PDF_X_PIXELS || height > PDF_Y_PIXELS)
                {
                    if(PDF_X_PIXELS*1.0/width < PDF_Y_PIXELS*1.0/height)
                    {
                        scale = PDF_X_PIXELS*1.0/width;
                    }
                    else
                    {
                        scale = PDF_Y_PIXELS*1.0/height;
                    }
                }
                else
                {
                    scale = getScale(height,width);
                }
                LOG.info("scale = "+scale);
                //write pdf contents
				//PDF_CONTENTS    "%d 0 obj\n<<\n/Length %d 0 R\n>>\nstream\nq  %3.4f 0.0000 0.0000 %3.4f %3.4f %3.4f cm /%s Do Q\nendstream\nendobj\n"
                LOG.info(width*scale+" 0.0000 0.0000 "+height*scale+" "+(PDF_X_PIXELS-width*scale)/2+" "+(PDF_Y_PIXELS-height*scale)/2);
                LOG.info("x = "+(width*scale*width+0*height+(PDF_X_PIXELS-width*scale)/2)+" y = "+(0*width+height*scale*height+(PDF_Y_PIXELS-height*scale)/2));
                sprintf(buffer, PDF_CONTENTS, objectCount, objectCount+1, width*scale, height*scale, (PDF_X_PIXELS-width*scale)/2, (PDF_Y_PIXELS-height*scale)/2, imgID);
                objectOffset[objectCount++] = totalLen;
                totalLen += buffer.length();
                
                //System.out.println("PDF_CONTENTS->"+buffer.toString());
                
                appendMethodA(pdfPath,buffer.toString());
                buffer = new StringBuffer();
                
                //write pdf object length
				//PDF_LENGTH      "%d 0 obj\n%d\nendobj\n"
                sprintf(buffer, PDF_LENGTH, objectCount, 60);
                objectOffset[objectCount++] = totalLen;
                totalLen += buffer.length();
                appendMethodA(pdfPath,buffer.toString());  
                buffer = new StringBuffer();
                fileLen= jpgFile.length();
                
                
                //write data header
				//PDF_JPG_DATA_HEADER "%d 0 obj\n<<\n/Length %l\n/Type /XObject\n/Subtype /Image\n/Name /%s\n/Width %d\n/Height %d\n/BitsPerComponent %d\n/ColorSpace /DeviceRGB\n/Filter /DCTDecode >>\nstream\n"
                
                sprintf(buffer, PDF_JPG_DATA_HEADER, objectCount, fileLen, imgID, width, height, 8);
                objectOffset[objectCount++] = totalLen;
                totalLen += buffer.length();
                
                //System.out.println("PDF_JPG_DATA_HEADER->"+buffer.toString());
                
                appendMethodA(pdfPath,buffer.toString());
                buffer = new StringBuffer();
                imgID = new StringBuffer();
                
                /**********************************************************/
                //写放文件流
                
                
                /*in = new FileInputStream(path);
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                in.close();*/
                
               /* FileInputStream fis = new FileInputStream("D:\\test.bmp");
                FileOutputStream fos = new FileOutputStream("D:\\test.bmp");
                BufferedImage img = ImageIO.read(fis);
                Graphics g = img.getGraphics();
                g.drawLine(0, 0, 60, 60);
                //你的其它绘图代码...
                img.flush();
                g.dispose();
                ImageIO.write(img, "BMP", fos);*/
                //bufferStream = new ByteArrayInputStream(buffer.toByteArray());
                
               /* ：把输入流转换byte[]
                * public static byte[] InputStreamTOByte(InputStream in) throws IOException{  
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
                    byte[] data = new byte[BUFFER_SIZE];  
                    int count = -1;  
                    while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
                        outStream.write(data, 0, count);  
                    //data = null;  
                    return outStream.toByteArray();  
                }*/


               // 把文件流转换成文件
               /* public static void inputstreamtofile(InputStream ins,File file){
                    OutputStream os=null;
                    try {
                        os = new FileOutputStream(file);
                        int bytesRead = 0;
                        byte[] buffer = new byte[8192];
                        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        os.close();
                        ins.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                
                
               /* 重写图片文件. 

                File source = new File("E://1.jpg"); 
                String type = "gif"; 
                BufferedImage inImg = ImageIO.read(source); 
                int wideth = inImg.getWidth(null); 
                int height = inImg.getHeight(null); 
                BufferedImage bimage = new BufferedImage(wideth,height,BufferedImage.SCALE_SMOOTH); 
                bimage.getGraphics().drawImage(inImg,0,0,wideth,height,null); 
                File outfile = new File("E://2.gif"); 
                OutputStream out = new FileOutputStream(outfile); 
                ImageIO.write(bimage,type,out); 
                source.delete(); 
                out.close(); */
                
                FileInputStream fis;
                //FileOutputStream fos;
				try {
					//System.out.println("jpg"+j+"->"+jpgPath[j]);
					fis = new FileInputStream(jpgPath[j]);
					//fos = new FileOutputStream(pdfPath);
					 byte[] bytes = new byte[fis.available()];   
					 totalLen +=bytes.length;
	                 fis.read(bytes);   
	                 fis.close();
	                
	                 appendMethodB(pdfPath,bytes);
		                //writer.write(new String(bytes));
				} catch (Exception e2) {
					e2.printStackTrace();
				}   
				
                /**************************************************************/
                //write data ender
				//PDF_JPG_DATA_ENDER  "\nendstream\nendobj\n"
                sprintf(buffer, PDF_JPG_DATA_ENDER);
                totalLen += buffer.length();
                appendMethodA(pdfPath,buffer.toString());
                buffer = new StringBuffer();
            }
        }
        sprintf(buf, "%010d 65535 f\r\n", objectOffset[0], 0);
        
        for(i = 1; i < objectCount; i++)
        {
            sprintf(buf, "%010d %05d n\r\n", objectOffset[i], 0);
        }
        
        //write pdf xref
		// PDF_XREF        "xref\r\n0 %d\r\n%s\r\ntrailer\r\n<<\r\n/Size %d\r\n/Root %d 0 R\r\n/Info %d 0 R\r\n/ID[<2900000023480000FF180000FF670000><2900000023480000FF180000FF670000>]\r\n>>\r\nstartxref\r\n%d\r\n%%%%EOF"
        sprintf(buffer, PDF_XREF, objectCount, buf, objectCount, 1, 2, totalLen);
        objectOffset[objectCount++] = totalLen;
        totalLen += buffer.length();
        appendMethodA(pdfPath,buffer.toString());
        buffer = new StringBuffer();
        buf = new StringBuffer();
		return flag;
	}
	
	public static boolean ParsePDFImage(String pdfPath, String jpgPath) {
		boolean result = false;
		File pdf = new File(pdfPath);
		if (!pdf.exists()) {
			System.out.println("file is not exist!");
		}
		try{
		FileReader fr= new FileReader(pdf);

		BufferedReader bf = new BufferedReader(fr);

		String context = null;
		String s[] = new String[2];
		int total = 0;
		int count = 0;
		do {
			context = bf.readLine();
			total +=context.length()+1;
			if (context.startsWith("/Length")) {
				s = context.split(" ");
				//System.out.println(context+"-->"+s[0]+" - "+s[1]);
			}
			if(context.startsWith("stream")){
				if(count++!=0){
					break;
				}
			}
			//System.out.println(context +"  -->  "+total);
		} while (context != null);
		FileInputStream fis = new FileInputStream(pdfPath);
		//RandomAccessFile randomFile = new RandomAccessFile(pdfPath, "rw");
		RandomAccessFile jpgFile = new RandomAccessFile(jpgPath, "rw");
		System.out.println("s[1]-->"+s[1]+"--"+Integer.parseInt(s[1]));
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);   
        fis.close();
		// 将写文件指针移到指定位置
		jpgFile.write(bytes, total, Integer.parseInt(s[1]));
		jpgFile.close();
		bf.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		 /*try {
			Scanner scner = new Scanner(new File("C:\\900dpi_2.pdf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		 
		 /*File file=new File("text.txt");
		    RandomAccessFile fileAccess=new RandomAccessFile(file,"rw");
		    fileAccess.seek(1);//将文件指针指向第一个字符
		    System.out.println((char)fileAccess.read());//读取第二个字节位置上的字符  结果:8
		    fileAccess.seek(1);//再将文件指针指向第一个字符
		    fileAccess.write("5".getBytes());//写入一个字符并覆盖刚才字符
		    fileAccess.seek(0);//将文件指针指向首个字符
		    System.out.println(fileAccess.readLine());//读取一行直到遇到换行符  结果:95624
*/
		 /*RandomAccessFile r = new RandomAccessFile(new File("c:/1.txt", "r"));//只读方式打开文件
		 r.seek(100);//指定下一次的开始位置
		 byte[] bs = new byte[1024];
		 r.read(bs);
		 r.readChar();
		 r.readInt();//读取一个整数
*/
		 
		return result;
	}
	/**
	 * 将多页pdf转换为jpg并压缩进压缩包,返回压缩包路径
	 * @param pdfPath PDF文件
	 * @param dstImgFolder 图片文件夹
	 * @return
	 * @throws IOException 
	 */
	public static String MPDF2JPG(File pdfPath, String dstImgFolder) throws IOException {
		if (!pdfPath.exists()) {
			LOG.info("file is not exist!");
		}
		BufferedReader bf = null;
		FileInputStream fis =null;
		try {
			FileReader fr = new FileReader(pdfPath);
			bf = new BufferedReader(fr);
			String context = null;
			String s[] = new String[2];
			int total = 0;
			int scount = 0;
			int ecount = 0;
			int count = 0;
			boolean sflag = false;
			boolean eflag = false;
		    fis = new FileInputStream(pdfPath);
			byte[] bytes = new byte[fis.available()];
			//将pdf数据全部读入types.
			fis.read(bytes);
			
			if(!createDirectory(dstImgFolder))throw new IOException(dstImgFolder+"创建失败...");
	          //  String imgPDFPath = PdfFilePath.getParent();  
	            int dot = pdfPath.getName().lastIndexOf('.');  
	            //文件名称
	            String imagePDFName = pdfPath.getName().substring(0, dot); // 获取图片文件名  
	            String basic = dstImgFolder+File.separator+DateUtils.getNowTime("yyyyMMdd");  
	            //图片文件夹
	            String imgFolderPath =basic+File.separator+"pdf2img"+File.separator+imagePDFName;  
	        if (createDirectory(imgFolderPath)) {  
	        	 StringBuffer imgFilePath = null;  
	        	do {
				context = bf.readLine();
				
				if(context!=null){
					//System.out.println("total->"+total);
					//获到图片的长度
					if (context.startsWith("/Length")) {
						s = context.split(" ");
					}
					//读取pdf开头部分字符信息直到遇到第二个stream停止
					if(!sflag){
						total += context.length()+1;
					}
				
					//pdf信息中共有两个stream,我们需要从第二个结束后开始读取数据
					if (context.startsWith("stream")) {
						if (scount++%2 != 0) {
							sflag = true;
							eflag = false;
							
		                    String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;  
		                    imgFilePath = new StringBuffer();  
		                    imgFilePath.append(imgFilePathPrefix);  
		                    imgFilePath.append("_");  
		                    imgFilePath.append(String.valueOf(scount/2));  
		                    imgFilePath.append(".jpg");  
		                    File dstFile = new File(imgFilePath.toString());  
							
							RandomAccessFile jpgFile = new RandomAccessFile(dstFile, "rw");
							//将bytes中的数据从total开始,长度为s[1]的内容写入jpgFile.
							LOG.info("total="+total+"  s[1]="+s[1]);
							if(count++==0){
								jpgFile.write(bytes, total, Integer.parseInt(s[1]));
							}else{
								//每读取一个文件指针的位置就会停留在stream之前，所以累积起来就为7*(scount/2-1)，另外回车为scount/2-1
								jpgFile.write(bytes, total+7*(scount/2-1)+scount/2-1, Integer.parseInt(s[1])+1);
							}
							//System.out.println("s[1]"+s[1]);
							jpgFile.close();
							//偶数位上的stream开始读取二进制文件，长度需加上二进制文件的长度．
							total += Integer.parseInt(s[1]);
							LOG.info("PDF2JPG:"+pdfPath+"->"+imgFilePath);
						}
					}
					//读取完一个二进制文件后，做一个标志，开始按行读取字符记数
					if(context.startsWith("endstream")){
						if(ecount++%2 != 0){
							eflag = true;
						}
					}
					//按行读取字符记数开始
					if(eflag){
						total += context.length()+1;
					}
				  }
				} while (context != null);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e+"");
		}finally {
			if(bf!=null)bf.close();
			if(fis!=null)fis.close();
		}
		return null;
	}
	
	
	//将多页pdf转换为jpg整理逻辑
	public static List<String> JPDF2JPG(String pdfPath) {
		List<String> list = new ArrayList<String>();
		File pdf = new File(pdfPath);
		if (!pdf.exists()) {
			System.out.println("file is not exist!");
		}
		try {
			FileReader fr = new FileReader(pdf);
			BufferedReader bf = new BufferedReader(fr);
			String context = null;
			String s[] = new String[2];
			int total = 0;
			int scount = 0;
			int ecount = 0;
			int count = 0;
			boolean sflag = false;
			boolean eflag = false;
			FileInputStream fis = new FileInputStream(pdfPath);
			byte[] bytes = new byte[fis.available()];
			//将pdf数据全部读入types.
			fis.read(bytes);
			fis.close();
			do {
				context = bf.readLine();
				if(context!=null){
					//System.out.println("total->"+total);
					//获到图片的长度
					if (context.startsWith("/Length")) {
						s = context.split(" ");
					}
					//读取pdf开头部分字符信息直到遇到第二个stream停止
					if(!sflag){
						total += context.length()+1;
					}
					//pdf信息中共有两个stream,我们需要从第二个结束后开始读取数据
					if (context.startsWith("stream")) {
						if (scount++%2 != 0) {
							//遇到偶数stream时，把stream和回车的长度加入进来
							//if(sflag){
							//	total += context.length()+1;
							//}
							//标志开始读取文件
							sflag = true;
							//
							eflag = false;
							RandomAccessFile jpgFile = new RandomAccessFile("C:\\img"+scount/2+".jpg", "rw");
							list.add("C:\\img"+scount/2+".jpg");
							//将bytes中的数据从total开始,长度为s[1]的内容写入jpgFile.
							System.out.println("total="+total+"  s[1]="+s[1]);
							if(count++==0){
								jpgFile.write(bytes, total, Integer.parseInt(s[1]));
							}else{
								//每读取一个文件指针的位置就会停留在stream之前，所以累积起来就为7*(scount/2-1)，另外回车为scount/2-1
								jpgFile.write(bytes, total+7*(scount/2-1)+scount/2-1, Integer.parseInt(s[1])+1);
							}
							//System.out.println("s[1]"+s[1]);
							jpgFile.close();
							//偶数位上的stream开始读取二进制文件，长度需加上二进制文件的长度．
							total += Integer.parseInt(s[1]);
							System.out.println("PDF2JPG:"+pdfPath+"->"+"C:\\img"+scount/2+".jpg");
						}
					}
					//读取完一个二进制文件后，做一个标志，开始按行读取字符记数
					if(context.startsWith("endstream")){
						if(ecount++%2 != 0){
							eflag = true;
						}
					}
					//按行读取字符记数开始
					if(eflag){
						total += context.length()+1;
					}
				}
			} while (context != null);
			/**/
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private static void sprintf(StringBuffer sb, String formats, Object... replaceString){
		  Object[] objects = new Object[replaceString.length];
		  for(int i=0;i<replaceString.length;i++){
		   objects[i] = (Object) replaceString[i];
		  }
		  ByteArrayOutputStream baos=new ByteArrayOutputStream();
		  PrintStream out = new PrintStream(baos);
		  PrintStream old = System.out;
		  System.setOut(out);
		  LOG.info(formats,objects);
		  byte[] byteArray = baos.toByteArray();
		  System.setOut(old);
		  sb.append(new String(byteArray));
	}

	 
	public static void appendMethodA(String fileName,String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void appendMethodB(String fileName,byte content[]) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.write(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取图片的高宽
	 * @param imgpath
	 * @return
	 */
	public static Map<String, Integer> getWidthAndHeight(String imgpath){
		Map<String, Integer> map = new HashMap<String, Integer>();
		File source = new File(imgpath); 
        BufferedImage inImg = null;
		try {
			inImg = ImageIO.read(source);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        int width = inImg.getWidth(null); 
        int height = inImg.getHeight(null); 
        map.put("width", width);
        map.put("height", height);
        System.out.println("width : height->"+width+" : "+height);
        return map;
	}
	
    /**
     * 获得文件类型
     * @param file
     * @return
     */
	public static String getMIMEType(File file) {
		// TODO Auto-generated method stub
		String type="";
		String fName=file.getName();
		String endName1= (String) fName.subSequence(fName.lastIndexOf(".")+1, fName.length());
		String endName = endName1.toLowerCase();
		if(!endName.equals("apk")){
			if(endName.equals("mp3")||endName.equals("m4a")||endName.equals("mid")||endName.equals("xmf")
					||endName.equals("ogg")||endName.equals("wav")){
				type="audio";
			}else if (endName.equals("3gp")||endName.equals("mp4")) {
				type="video";
			}else if (endName.equals("jpg")||endName.equals("gif")||endName.equals("png")
					||endName.equals("jpeg")||endName.equals("bmp")) {
				type="image";
			}else if(endName.equals("pdf")){
				type="application/pdf";
				return type;
			}
			else {
				type="*";
			}
			type+="/*";
		}
		else{
			type="application/vnd.android.package-archive";
		}
		
		return type;
	}
	
	
	 public static int FileRead(String path) throws IOException{
			
			LOG.info("该文件的内容如下：");
			
			File file = new File(path);
			FileInputStream fis=null;
			try {
				fis=new FileInputStream(file);
				byte[] cFis = new byte[1024];
				
				int n= fis.read(cFis, 0, 1024);
				
				while(n!=-1){
					
					System.out.println(new String(cFis,0,n,"gbk"));
					n= fis.read(cFis, 0, n);
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			finally{
				if(fis!=null)
					fis.close();
			}
			return 0;
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
	
//	public static void testReadFile(){
//		RandomAccessFile raf = null;
//		try {
//			raf = new RandomAccessFile("c:/out.txt", "r");
//			//跳过字节数
//			raf.skipBytes(32);
//			//读取一个数字
//			int num = raf.readInt();
//			System.out.println(num);
//			raf.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	} 
	
	
	/**
	 * 获取高宽百分比
	 * @param h
	 * @param w
	 * @return
	 */
//	private static int getPercent2(float h,float w)
//	{
//		int p=0;
//		float p2=0.0f;
//		p2=530/w*100;
//		p=Math.round(p2);
//		return p;
//	}
//	private static int getPercent(float h,float w)
//	{
//		int p=0;
//		float p2=0.0f;
//		if(h>w)
//		{
//			p2=297/h*100;
//		}
//		else
//		{
//			p2=210/w*100;
//		}
//		p=Math.round(p2);
//		return p;
//	}
	
	private static int getScale(int h,int w){
		int p = 0;
		float p2=0.0f;
		p2=PDF_X_PIXELS/w;
		p=Math.round(p2);
		return p;
	}
	public static void main(String[] args) throws Exception {
		String aa = "D:/OpenCV入门教程.pdf";
	//	File file = new File("D:/111.pdf");
		
//		System.out.println(file.exists());
//		if(file.exists()){
		//	System.out.println(toImg(file,"F:/test"));
//		}else{
//			//file.createNewFile();
//			System.out.println(file.getAbsolutePath());
//		}
		
		String aa1[]={"F:/test/temp/20180615/1.jpg","F:/test/temp/20180615/1i8a.jpg"};
		File file = new File("F:/test/temp/20180615/1.jpg");
		File file1 = new File("F:/test/temp/20180615/1i8a.jpg");
		if(!file.exists()){
			System.out.println("不存在");
			return;
		}
		//pdf2Imae(file,"F:/test",296);
		List<File> list = new ArrayList<File>();
		list.add(file);
		list.add(file1);
		//list.add(file);
//		long time1 = System.currentTimeMillis();
      //  toPdf(list, "D:/");
		//MPDF2JPG(file,"F:/test");
		changC.MCombineJPG2PDF(list,"D:/111.pdf");
//        long time2 = System.currentTimeMillis();
//        int time = (int) ((time2 - time1)/1000);
//        System.out.println("执行了："+time+"秒！");
		//pdf2Word(file,"F:/test");
		/*StringBuffer sb=new StringBuffer();
		sprintf(sb, "%d, %s, asdfasdf, %s", new Object[]{1,"你","好"});
		System.out.println(sb.toString());*/
		/*String[] jpgPath = {"C:\\0.jpg","C:\\1.jpg","C:\\2.jpg","C:\\3.jpg","C:\\4.jpg","C:\\5.jpg","C:\\6.jpg","C:\\7.jpg","C:\\8.jpg","C:\\9.jpg"};
		String pdfPath = "C:\\pdfcombine09.pdf";
		int pdfCount =9;
		CombineJPGToPDF(jpgPath,pdfPath,pdfCount);*/
		
		//String[] jpgPath = {"C:\\xxx.pdf","C:\\2.jpg"};
		//String pdfPath = "C:\\mtest.pdf";
		//MCombineJPG2PDF(jpgPath,pdfPath);
		
		//MPDF2JPG("C:\\pdfcombine09.pdf");
		//JPDF2JPG("C:\\pdfcombine09.pdf");
		/*File f = new File("C:\\sd.jpg");
		ParsePDFImage("C:\\900dpi_2.pdf","C:\\sd.jpg");*/
		/*try {
			FileRead("C:\\900dpi_2.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
