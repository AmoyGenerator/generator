package app.other.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tkb.util.crypto.DES;
import tkb.util.string.Hex;



/**
 * 类说明:md5加密
 */
public class Md5Encrypt {
	

	/**
	 * md5加密32位
	 * @param plainText
	 * @return
	 */
	public static String md5Encrypt32(String plainText) {
		String result = "";
		try {
			//被初始化
			MessageDigest md = MessageDigest.getInstance("MD5");
			//使用 update 方法处理数据
			md.update(plainText.getBytes());
			//用 digest 方法之一完成哈希计算
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
//				System.out.println("i=" + i);
				if (i < 0){
					i += 256;
				}					
				if (i < 16){ //16进制
					buf.append("0");
				}	
//				System.out.println("i2=" + i);
				//以十六进制的无符号整数形式返回一个整数参数的字符串表示形式
//				System.out.println("i2-0X=" + Integer.toHexString(i));
				buf.append(Integer.toHexString(i));
//				System.out.println("buf=" + buf.toString());
			}
			result = buf.toString();//// 32位的加密			
//			System.out.println("32位的加密: " + result);
//			System.out.println("16位的加密: " + result.substring(8, 24));// 16位的加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * md5加密16位
	 * @param plainText
	 * @return
	 */
	public static String md5Encrypt16(String plainText){
		String result32 = md5Encrypt32(plainText);
		String result = result32.substring(10, 26);
//		System.out.println("16位的加密: " + result);
		return result;		
	}
	
	/**
	 * 加密有n位密文（0<n=<32)
	 * @param plainText
	 * @param num
	 * @return
	 */
	public static String md5EncryptNum(String plainText, int num){
		String result32 = md5Encrypt32(plainText);
		String result = result32.substring(0, num);
//		System.out.println(num + "位的加密: " + result);
		return result;		
	}
	
	
	
	
	
	public static void main(String[] args) {
//		Md5Encrypt.md5Encrypt32("h2");
		Md5Encrypt.md5Encrypt16("fjgaokao");
	}

}
