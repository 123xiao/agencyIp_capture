package cn.just.spider.action;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import cn.just.spider.entity.CollectIp;
import cn.just.spider.util.SpiderHttpUtils;

public class CollectIpSpider {
	private final static String URL = "http://www.youdaili.net/";
	private final static Executor executor = Executors.newCachedThreadPool();

	private static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/sdhkdata";
		String username = "root";
		String password = "root";
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void getDailiPage() {
		final String[] ipTypeUrl = getIpTypeUrl();
		executor.execute(new Runnable() {

			public void run() {
				for (int i = 0; i < ipTypeUrl.length; i++) {
					final Document parse = Jsoup.parse(SpiderHttpUtils.readHtml(ipTypeUrl[i])); // 进入类型详情页
					Integer page = Integer.valueOf(parse
							.select("body > div.con.PT20 > div.conl > div.lbtc.l > div.page > div > ul > li:nth-child(9) > span > strong:nth-child(1)")
							.text());// 获取类型有多少页
					System.err.println("肖" + ipTypeUrl[i] + "有" + page + "页");
					for (int j = 1; j <= page; j++) {
						final Document parse2 = Jsoup
								.parse(SpiderHttpUtils.readHtml(ipTypeUrl[i] + "/list_" + j + ".html"));// 分页打开
						executor.execute(new Runnable() {

							public void run() {
								System.out.println(Thread.currentThread().getName() + "正在执行。。。");
								Elements select = parse2
										.select("body > div.con.PT20 > div.conl > div.lbtc.l > div.chunlist");
								Elements select2 = select.select("a");// 获取数据详情页

								for (Element element : select2) {

									String text = Jsoup.parse(SpiderHttpUtils.readHtml(element.attr("href")))
											.select("body > div.con.PT20 > div.conl > div.lbtc.l > div.arc > div.pagebreak > li:nth-child(1) > a")
											.text();
									Integer valueOf = 0;
									if (text != null && !"".equals(text)) {
										try {
											valueOf = Integer.valueOf(text.substring(1, text.length() - 2));// 截取多少页
										} catch (Exception e) {
											System.out.println(text + "125814");
										}

									}

									String attr = element.attr("href");
									attr = attr.substring(attr.lastIndexOf("/"), attr.indexOf(".html"));// 获取到分页链接

									for (int k = 1; k <= valueOf; k++) {
										Document parse3 = null;
										if (k == 1) {
											parse3 = Jsoup.parse(SpiderHttpUtils.readHtml(element.attr("href")));
										} else {
											parse3 = Jsoup.parse(SpiderHttpUtils
													.readHtml(element.attr("href").replace(attr, attr + "_" + k)));
										}
										Elements select3 = parse3.select(
												"body > div.con.PT20 > div.conl > div.lbtc.l > div.arc > div.content");// 获取到数据
										for (Element element2 : select3) {
											String[] split = element2.html().split("<br> ");
											for (int l = 0; l < split.length; l++) {
												if (l == 0) {
													continue;
												}
												System.out.println(split[l]);
											}
										}

									}

								}

							}
						});
					}

				}
			}
		});

	}

	/**
	 * 获取有代理四个类型IP
	 * 
	 * @return
	 */
	public static String[] getIpTypeUrl() {
		Document parse = Jsoup.parse(SpiderHttpUtils.readHtml(URL));
		Elements select = parse.select("body > div.m > div > ul:nth-child(2)");
		Elements select2 = select.select("a");
		String urls = "";
		int count = 0;
		for (Element element : select2) {
			count++;
			if (count <= 4) {
				urls += element.attr("href") + ",";
			}

		}
		return urls.split(",");
	}

	private static int insert(CollectIp collectIp) {
		Connection conn = getConn();
		int i = 0;
		String sql = "INSERT INTO collectip(id,pid,remarks,localhost,prot,ip_type,ip_address,operator) VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, UUID.randomUUID().toString());
			pstmt.setString(2, "");
			pstmt.setString(3, collectIp.getRemarks());
			pstmt.setString(4, collectIp.getLocalhost());
			pstmt.setInt(5, collectIp.getProt());
			pstmt.setString(6, collectIp.getIpType());
			pstmt.setString(7, collectIp.getIpAddress());
			pstmt.setString(8, collectIp.getOperator());

			i = pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static void main(String[] args) {
		getDailiPage();
	}
}
