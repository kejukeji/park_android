package com.parkmecn.android.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 * 
 * @author Zhoujun 说明：处理一下字符串的常用操作，字符串校验等
 */
public class StringUtil {

	/**
	 * 验证邮箱输入是否合法
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		// String strPattern =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * MD5加密
	 * 
	 * @param secret_key
	 * @return
	 */
	public static String createSign(String secret_key) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(secret_key.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 将网络图片路径md5加密作为文件名
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String createImageName(String imageUrl) {
		if(imageUrl.toLowerCase().endsWith("png")){
			return createSign(imageUrl) + ".png";
		}else{
			return createSign(imageUrl) + ".jpg";
		}
	}

	/**
	 * 将网络图片路径md5加密作为文件名,可以设置图片类型
	 * 
	 * @param imageUrl
	 * @param imgSuffix
	 * @return
	 */
	public static String createImageName(String imageUrl, String imgSuffix) {
		return createSign(imageUrl) + imgSuffix;
	}

	/**
	 * 计算地球上两点间的距离；
	 * 
	 * @param lat1
	 * @param longt1
	 * @param lat2
	 * @param longt2
	 * @return
	 */
	public static double getDistance(double lat1, double longt1, double lat2, double longt2) {
		double PI = 3.14159265358979323; // 圆周率
		double R = 6371229; // 地球的半径

		double x, y, distance;
		x = (longt2 - longt1) * PI * R * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);

		return Math.rint(distance);
	}

	public static String getExceptionInfo(Exception e) {
		return null;
	}
}
