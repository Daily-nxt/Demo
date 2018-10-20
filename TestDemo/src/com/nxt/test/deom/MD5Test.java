package com.nxt.test.deom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Test {
	
	private static byte[] digesta;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = CalcMD5("admin", 15);
		System.out.println(str);
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param myinfo
	 *            需要加密的字符串
	 * @return 通过MD5加密后的字符串
	 */
	public static String CalcMD5(String myinfo, int length) {
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(myinfo.getBytes());
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		System.out.println(new String(digesta));
		return byte2hex(digesta, length);

	}
	
	private static String byte2hex(byte[] b, int length) { // 二行制转字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		System.out.println(hs);
		return hs.substring(0, length);
	}
}
