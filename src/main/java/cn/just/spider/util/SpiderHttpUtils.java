package cn.just.spider.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * ������ҳ�Ĺ��߷���
 */
public class SpiderHttpUtils {

	/**
	 * �����ȡ��ҳ�ı����ʽ
	 * 
	 * @param myurl
	 *            ��ҳ��ַ
	 * @return
	 */
	public static String readHtml(String myurl) {
		StringBuffer sb = new StringBuffer("");
		URL url;
		try {
			url = new URL(myurl);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
