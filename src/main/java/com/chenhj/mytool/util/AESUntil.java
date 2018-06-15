package com.chenhj.mytool.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
* @Title: AESUntil
* @Description: 
* 用于对传输的内容进行AES的加密和解密 对用户名，密码采用欧元符号"€"进行分割（设置用户名密码时请勿采用欧元符号"€"）
 *         默认AES密钥为：@#LyJF5~45&*%x.n 采用GBK编码进行加密解密
* @Version:1.0.0  
* @author pancm
* @date 2018年3月27日
 */
public class AESUntil {
	// 密钥
//	private static final String AESKEY = "!#a~m@$%s^)o(*e&";
//	private static final String AESKEY = "h!)i8*@)!&%s^*9(";
//	private static final String AESKEY = "$y%h^p!@i#t)s~(*";
	private static final String AESKEY = "m!@$%s^&*a)_e+s.";
	

	/***
	 * 梦网AES加密方法
	 * 
	 * @param password
	 *            明文密码
	 * @return
	 * @throws Exception
	 */
	public static String aesEncode(String password) throws Exception {
		String result = null;
		byte[] passwordByte = password.getBytes("GBK");
		result = parseByte2HexStr(aesEncrypt(passwordByte, AESKEY.getBytes("GBK")));
		return result;
	}

	/***
	 * AES解密方法
	 * 
	 * @param password
	 *            密文密码
	 * @return
	 * @throws Exception
	 */
	public static String aesDecode(String password) throws Exception {
		// 默认返回值为null
		String res = null;
		byte[] content = parseHexStr2Byte(password);
		// 进行AES解密
		byte[] decodeBytes = aesDecrypt(content, AESKEY.getBytes("GBK"));
		res = new String(decodeBytes, "GBK");
		// 返回结果
		return res;
	}
	


	/***
	 * AES加密实现方法 采用GBK编码
	 * 
	 * @param content
	 *            加密内容字节数组
	 * @param aesKey
	 *            密钥字节数组
	 * @return 密文字节数组
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] aesEncrypt(byte[] content, byte[] aesKey) throws Exception {
		// 产生AES密钥
		SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
		// 设置加密模式与填充方法
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		// 加密内容字节数组
		byte[] byteContent = content;
		// 加密内容字节数组长度
		int len = byteContent.length;
		// 加密内容字节数组不足16个字节，进行补全
		int l = (len % 16 > 0) ? (16 - len % 16) : 0;
		// 新建补全后长度大小的字节数组
		byte[] tmpContent = new byte[byteContent.length + l];
		// 数组拷贝到补全后数组
		System.arraycopy(byteContent, 0, tmpContent, 0, len);
		// 初始化加密对象
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		// 加密成密文字节数组
		byte[] result = cipher.doFinal(tmpContent);
		// 返回结果
		return result; // 加密
	}

	/***
	 * AES解密实现方法
	 * 
	 * @param content
	 *            密文字节数组
	 * @param aesKey
	 *            密钥字节数组
	 * @return 明文字节数组
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] aesDecrypt(byte[] content, byte[] aesKey) throws Exception {
		// 如果密钥不是16个字节，返回空
		if (aesKey == null || aesKey.length != 16) {
			return null;
		}
		// 产生AES密钥
		SecretKeySpec skp = new SecretKeySpec(aesKey, "AES");
		// 设置加密模式与填充方法
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		// 初始化解密对象
		cipher.init(Cipher.DECRYPT_MODE, skp);
		// 解密成明文字节数组
		byte[] original = cipher.doFinal(content);
		int i = 0;
		// 遍历字节数组
		for (i = 0; i < original.length; i++) {
			// 如果出现以0补全的字节，跳出循环
			if (original[i] == 0) {
				break;
			}
		}
		// 创建除去0补全长度的字节数组
		byte[] bbb = new byte[i];
		// 进行数组拷贝到去除0补全长度的字节数组
		System.arraycopy(original, 0, bbb, 0, i);
		// 返回结果
		return bbb;
	}

	/***
	 * 将字节数组转化成十六进制字符串
	 * 
	 * @param buf
	 *            字节数组
	 * @return 十六进制字符串
	 */
	public static String parseByte2HexStr(byte buf[]) {
		// 字符串缓存
		StringBuffer sb = new StringBuffer();
		// 遍历字节数组，转化成十六进制字符串
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			// 长度为1的前面加0补全
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			// 添加到字符串缓存中
			sb.append(hex);
		}
		// 返回结果
		return sb.toString();
	}

	/**
	 * 将字符串转换成字节数组
	 * 
	 * @param hexStr
	 * @return buf 字节数组
	 * 
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	// 调用测试
	public static void main(String[] args) throws Exception {
		String password = "{\"datatype\":1,\"startlastupdatetm\":\"2018-03-12 09:49:47.000\",\"pagesize\":10,\"index\":1}";
		System.out.println("加密前的密码：" + password);
		String encPassword = aesEncode(password);
		System.out.println("加密后的密码：" + encPassword);

		String decPassword = aesDecode(encPassword);
		System.out.println("解密后的密码：" + decPassword);
		System.out.println("解密后的密码：" + aesDecode("b85dcb9d987b281fc18e06682c5f93dfc9d99d713afff3ef6b11d51614d532e0a0840e308c8f031d466d8906a6f4d42727487e000033b515b12b949505c3bb36"));
	}
}
