package com.chenhj.mytool.constant;

public class PDFConstants {
	public static float PDF_VERESION = 1.5f;
	public static String PDF_AUTHOR = "duxk";
	public static String PDF_CREATER = "santa";
	public static String PDF_PRODUCER = "skylight";
	public static int PDF_X_PIXELS = 595;
	public static int PDF_Y_PIXELS = 841;
	//version
	public static String PDF_HEADER = "%%PDF-%1.1f\n";
	
	//index version pageObjectIndex
	public static String PDF_CATALOG = "%d 0 obj\n<<\n/Type /Catalog\n/Version /%1.1f\n/Pages %d 0 R\n>>\nendobj\n";
	
	//index author creator producer
	public static String PDF_INFO = "%d 0 obj\n<<\n/Author (%s)\n/Creator (%s)\n/Producer (%s)\n>>\nendobj\n";
	
	//index kids count
	public static String PDF_PAGES = "%d 0 obj\n<<\n/Type /Pages\n/Kids [%s]\n/Count %d\n>>\nendobj\n";
	
	//index parentIndex contensIndex xobjectID xobjectIndex
	public static String PDF_PAGES_KID = "%d 0 obj\n<<\n/Type /Page\n/Parent %d 0 R\n/MediaBox [0 0 595.276 841.890]\n/CropBox [0 0 595.276 841.890]\n/Contents %d 0 R\n/Resources <<\n/XObject <<\n/%s %d 0 R >>\n/ProcSet [/ImageC]\n>>\n>>\nendobj\n";
	
	//index lengthIndex width height xoffset yoffset xobjectID
	public static String PDF_CONTENTS = "%d 0 obj\n<</Length %d 0 R\n>>\nstream\nq  %3.3f 0.0000 0.0000 %3.3f %3.3f %3.3f cm /%s Do Q\nendstream\nendobj\n";
	
	//index value
	public static String PDF_LENGTH = "%d 0 obj\n%d\nendobj\n";
	
	//objectCount objectInfo size rootIndex InfoIndex totaoSize
	public static String PDF_XREF = "xref\r\n0 %d\r\n%s\r\ntrailer\r\n<<\r\n/Size %d\r\n/Root %d 0 R\r\n/Info %d 0 R\r\n/ID[<2900000023480000FF180000FF670000><2900000023480000FF180000FF670000>]\r\n>>\r\nstartxref\r\n%d\r\n%%%%EOF";
	
	//index dataLen xobjectID width height BitsPerComponent
	public static String PDF_JPG_DATA_HEADER = "%d 0 obj\n<<\n/Length %d\n/Type /XObject\n/Subtype /Image\n/Name /%s\n/Width %d\n/Height %d\n/BitsPerComponent %d\n/ColorSpace /DeviceRGB\n/Filter /DCTDecode >>\nstream\n";
	
	public static String PDF_JPG_DATA_ENDER =  "\nendstream\nendobj\n";
	
}
