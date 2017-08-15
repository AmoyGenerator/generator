package app.other.encrypt;


import tkb.util.crypto.DES;
import tkb.util.string.Hex;



/**
 * DES加解密
 */
public class DesEncrypt {
	
	/**
	 * DES加密
	 * @param password
	 * @return
	 */
	public static String desEncrypt(String password, String desKey, String desModeAndPadding){
		DES des = new DES(desKey, desModeAndPadding);
		byte[] encrypted = des.encrypt(password.getBytes());
		String hexedPwd = Hex.hexEncode(encrypted);
		return hexedPwd;
	}
	/**
	 * DES解密
	 * @param password
	 * @return
	 */
	public static String desDecrypt(String password, String desKey, String desModeAndPadding){
		DES des = new DES(desKey, desModeAndPadding);
		byte[] hexedPwd = Hex.hexDecode(password);
		String encrypted = des.decrypt(hexedPwd);		
		return encrypted;
	}
	
	

}
