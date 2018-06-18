package com.chenhj.mytool.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
	
	public static Locale defaultLocale = Locale.getDefault();// 设置系统语言

	public static final String xJavaFxToolVersions = "V0.1.2";// xJavaFxTool版本信息
	public static final int xJavaFxToolVersionsInteger = 3;// xJavaFxTool更新信息
	
	@Value("${pdfImgPath}")
	private String pdfImgPath;

	public String getPdfImgPath() {
		return pdfImgPath;
	}
}
