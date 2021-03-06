package com.cdiscount.fileparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class MajesticFileParser {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/links_toxicity.properties";
	private static String insert_string ="INSERT INTO MAJESTIC_CISCOUNT_CSV_EXPORT(TARGET_URL,SOURCE_URL,ANCHOR_TEXT,CRAWL_DATE,FIRST_FOUND_DATE,FLAG_NO_FOLLOW,FLAG_IMAGE_LINK,FLAG_REDIRECT,FLAG_FRAME,FLAG_OLD_CRAWL,FLAG_ALT_TEXT,FALG_MENTION,SOURCE_CITATIONFLOW,SOURCE_TRUSTFLOW,TARGET_CITATIONFLOW,TARGET_TRUSTFLOW) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static Map<String, Integer> counting_map = new HashMap<String, Integer>();
	public static void main(String[] args) throws IOException {
		// Reading the property of our database
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
			props.load(in);
		} catch (IOException ex) {
			System.out.println("Trouble fetching database configuration");
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Trouble fetching database configuration");
				ex.printStackTrace();
			}
		}
		// the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");
		// FNAC_file
		//String csvFile = "D:\\My_FNAC_entering_links\\download_fnac_com_16_Jul_14_E832D435DA56AC2656CDF3E3E4C4E1E4.csv";
		// CDS file extracted from MAJESTIC
		// windows path
		//String csvFile = "D:\\My_Link_Data\\test-neo4j.csv";
		String csvFile = "/home/sduprey/My_Data/My_Toxic_Links/cdiscount.csv";
		Connection con = null;
		PreparedStatement pst = null;
		// the csv file variables
		ResultSet rs = null;
		BufferedReader br = null;
		String line = "";
		String header = null;
		String[] column_names = null;
		String cvsSplitBy = ",";
		int nb_line=1;
		// last error
		try {
			con = DriverManager.getConnection(url, user, passwd);
			br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(csvFile), "UTF8"));
			// we skip the first line : the headers
			header = br.readLine();
			column_names= header.split(cvsSplitBy);
			System.out.println(column_names);
			while ((line = br.readLine()) != null) {
				String[] splitted_line = line.split(cvsSplitBy);
				System.out.println("Inserting line number :"+nb_line);
				try {

					pst = con.prepareStatement(insert_string);
					//TARGET_URL
					String targetURL = splitted_line[0];
					pst.setString(1,targetURL);
					//SOURCE_URL
					String sourceURL = splitted_line[1];
					pst.setString(2,sourceURL);
					populateLinksMap(targetURL,sourceURL);
					//ANCHOR_TEXT
					String anchorText = splitted_line[2].replace("\0", "");
					pst.setString(3,anchorText);
					//CRAWL_DATE
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
					Date crawldate = sdf.parse(splitted_line[3]);
					java.sql.Date sqlCrawlDate = new java.sql.Date(crawldate.getTime());
					pst.setDate(4,sqlCrawlDate);		
					//FIRST_FOUND_DATE
					Date firstfounddate = sdf.parse(splitted_line[4]);
					java.sql.Date sqlFirstFoundDate = new java.sql.Date(firstfounddate.getTime());
					pst.setDate(5,sqlFirstFoundDate);	
					//FLAG_IMAGE_LINK,FLAG_REDIRECT,FLAG_FRAME,FLAG_OLD_CRAWL,FLAG_ALT_TEXT,FALG_MENTION
					pst.setBoolean(6,splitted_line[5]=="+"?true:false);
					pst.setBoolean(7,splitted_line[6]=="+"?true:false);
					pst.setBoolean(8,splitted_line[7]=="+"?true:false);
					pst.setBoolean(9,splitted_line[8]=="+"?true:false);
					pst.setBoolean(10,splitted_line[9]=="+"?true:false);
					pst.setBoolean(11,splitted_line[10]=="+"?true:false);
					pst.setBoolean(12,splitted_line[11]=="+"?true:false);
					//SOURCE_CITATIONFLOW,SOURCE_TRUSTFLOW,TARGET_CITATIONFLOW,TARGET_TRUSTFLOW
					pst.setInt(13,Integer.valueOf(splitted_line[12]));
					pst.setInt(14,Integer.valueOf(splitted_line[13]));
					pst.setInt(15,Integer.valueOf(splitted_line[14]));
					pst.setInt(16,Integer.valueOf(splitted_line[15]));
					pst.executeUpdate();
				} catch (Exception exx) {
					exx.printStackTrace();
				}
				nb_line++;
			}
			displayFoundLinks();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void populateLinksMap(String targetURL, String sourceURL){
		//		System.out.println("Target URL : "+targetURL);
		String target_domain=getDomain(targetURL);
		//		System.out.println("Target domain : "+target_domain);
		//		System.out.println("Source URL : "+sourceURL);
		String source_domain=getDomain(sourceURL);
		//		System.out.println("Source domain : "+source_domain);

		if (!"www.cdiscount.com".equals(target_domain)){
			System.out.println("Target domain unknown : "+target_domain);
			//	System.exit(0);
		}	
		Integer local_count = counting_map.get(source_domain);
		if (local_count == null){
			local_count = new Integer(1);
			counting_map.put(source_domain, local_count);
		} else {
			local_count=local_count+1;
			counting_map.put(source_domain,local_count);
		}
	}

	private static void displayFoundLinks(){
		System.out.println("Displaying domain links count");
		Iterator it = counting_map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String domain_name=(String)pairs.getKey();
			Integer count = (Integer)pairs.getValue();
			System.out.println(domain_name+" : "+count);
		}	
	}

	private static String getDomain(String url){
		String target_domain = null;
		if (url.length()>=1 &&url.charAt(url.length()-1)=='\"')
		{
			url = url.replace(url.substring(url.length()-1), "");
		}
		if (url.length()>=1 &&url.charAt(0)=='\"')
		{
			url = url.substring(1);
		}
		url=url.replaceAll("http://","@");
		url=url.replaceAll("https://","@");
		// we hereby construct different tables 

		StringTokenizer target_protoc_tokenize = new StringTokenizer(url,"@");
		while (target_protoc_tokenize.hasMoreTokens()){
			String underTargetURL=target_protoc_tokenize.nextToken();
			StringTokenizer target_tokenize = new StringTokenizer(underTargetURL,"/");
			//					String target_protocol = null;
			//					if (target_tokenize.hasMoreTokens()){
			//						target_protocol = target_tokenize.nextToken();
			//						if (target_protocol.length()>=1 &&target_protocol.charAt(target_protocol.length()-1)=='\"')
			//						{
			//							target_protocol = target_protocol.replace(target_protocol.substring(target_protocol.length()-1), "");
			//						}
			//					}

			if (target_tokenize.hasMoreTokens()){
				target_domain = target_tokenize.nextToken();	
				if (target_domain.length()>=1 &&target_domain.charAt(target_domain.length()-1)=='\"')
				{
					target_domain = target_domain.replace(target_domain.substring(target_domain.length()-1), "");
				}
				if (target_domain.indexOf("?")>=0){
					target_domain=target_domain.substring(0,target_domain.indexOf("?"));
				}
				if (target_domain.indexOf("#")>=0){
					target_domain=target_domain.substring(0,target_domain.indexOf("#"));
				}
				if (target_domain.indexOf(":")>=0){
					target_domain=target_domain.substring(0,target_domain.indexOf(":"));
				}
				if (target_domain.indexOf("&")>=0){
					target_domain=target_domain.substring(0,target_domain.indexOf("&"));
				}
				target_domain=target_domain.trim();
			}	
		}
		return target_domain;
	}


	public static String removeBadChars(String s) {
		if (s == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<s.length();i++){ 
			if (Character.isHighSurrogate(s.charAt(i)) ) continue;	
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}
}
