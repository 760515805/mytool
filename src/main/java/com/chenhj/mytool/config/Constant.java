package com.chenhj.mytool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
	@Value("${pdfImgPath}")
	private String pdfImgPath;

	public String getPdfImgPath() {
		return pdfImgPath;
	}
}
