package com.multirequesting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class ExaleadRequestingWorkerThread implements Runnable {
	
	private static String insert_statement="INSERT INTO EXALEAD_RESULTS(REQUEST, DID, URL, SCORE, OFFER_PRODUCT_ID, OFFER_PRICE, OFFER_SELLER_ID, PROXIMITY, CA1, CATEGORYWEIGHT, CA14, CA7, TERMSCORE)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private List<EntryInfo> thread_fetch_ids = new ArrayList<EntryInfo>();
	private int total_tofetch=0;
	private Connection con;

	public ExaleadRequestingWorkerThread(Connection con ,List<EntryInfo> to_fetch) {
		this.thread_fetch_ids=to_fetch;
		this.total_tofetch=to_fetch.size();
		this.con = con;	
	}

	public void run() {
		List<RequestResults> infos=processCommand();
		updateStatus(infos);
		System.out.println(Thread.currentThread().getName()+" End");
	}

	// batched update
	private void updateStatus(List<RequestResults> infos){
		System.out.println("Adding to batch : " + infos.size() + "ULRs into database");
		try {
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement(insert_statement);   
			int global_counter = 0;
			for (int i=0;i<infos.size();i++){
				List<ULRLineToInsert> all_results = infos.get(i).getResult();
				for (ULRLineToInsert urltoinsert : all_results){
					//(REQUEST, DID, URL, SCORE, OFFER_PRODUCT_ID, OFFER_PRICE, OFFER_SELLER_ID, PROXIMITY, CA1, CATEGORYWEIGHT, CA14, CA7, TERMSCORE)"
					st.setString(1, urltoinsert.getRequest());
					st.setInt(2, urltoinsert.getDid());
					st.setString(3, urltoinsert.getUrl());
					st.setInt(4, urltoinsert.getScore());
					st.setString(5,urltoinsert.getOffer_product_id());
					st.setDouble(6,urltoinsert.getOffer_price());
					st.setInt(7,urltoinsert.getOffer_seller_id());
					st.setDouble(8,urltoinsert.getProximity());
					st.setDouble(9,urltoinsert.getCa1());
					st.setDouble(10,urltoinsert.getCategoryweight());
					st.setDouble(11,urltoinsert.getCa14());				
					st.setDouble(12,urltoinsert.getCa7());
					st.setDouble(13,urltoinsert.getTermscore());
					st.addBatch();
					global_counter++;
				}			
			}      
			//int counts[] = st.executeBatch();
			System.out.println("Beginning to insert : " + global_counter + "ULRs into database");
			st.executeBatch();
			con.commit();
			System.out.println("Having inserted : " + global_counter + "ULRs into database");
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Trouble inserting batch ");
		}
	}

	private List<RequestResults> processCommand() {
		List<RequestResults> my_fetched_infos = new ArrayList<RequestResults>();
		CloseableHttpResponse response2 = null;
		for (int i=0;i<thread_fetch_ids.size();i++){
			RequestResults result = new RequestResults();
			try{
				EntryInfo line_info=thread_fetch_ids.get(i);
				String request = line_info.getRequest();
				int volume = line_info.getVolume();
				result.setRequest(request);
				result.setVolume(volume);
				System.out.println(Thread.currentThread()+" Requesting +"+request+" Volume : "+volume );
				CloseableHttpClient httpclient = HttpClients.createDefault();
				String url_string = "http://exasearchv6.gslb.cdweb.biz:10010/search-api/search";
				HttpPost httpPost = new HttpPost(url_string);		
				//HttpPost httpPost = new HttpPost("http://targethost/login");
				List <NameValuePair> nvps = new ArrayList <NameValuePair>();
				nvps.add(new BasicNameValuePair("lang", "fr"));
				nvps.add(new BasicNameValuePair("sl", "sl0"));
				nvps.add(new BasicNameValuePair("f.20.field", "facet_mut_technical"));
				nvps.add(new BasicNameValuePair("f.20.in_hits", "False"));
				nvps.add(new BasicNameValuePair("f.20.in_synthesis", "False"));
				nvps.add(new BasicNameValuePair("f.20.max_per_level", "10"));
				nvps.add(new BasicNameValuePair("f.20.root", "Top/ClassProperties/is_best_total_offer"));
				nvps.add(new BasicNameValuePair("f.20.sort", "num"));
				nvps.add(new BasicNameValuePair("f.20.type", "category"));
				nvps.add(new BasicNameValuePair("refine", "+f/20/1"));
				nvps.add(new BasicNameValuePair("use_logic_facets", "false"));
				nvps.add(new BasicNameValuePair("use_logic_hit_metas", "false"));
				nvps.add(new BasicNameValuePair("add_hit_meta", "offer_product_id"));
				nvps.add(new BasicNameValuePair("add_hit_meta", "offer_price"));
				nvps.add(new BasicNameValuePair("add_hit_meta", "offer_seller_id"));
				nvps.add(new BasicNameValuePair("add_hit_meta", "title"));
				nvps.add(new BasicNameValuePair("hit_meta.termscore.expr", "100000*@term.score"));
				nvps.add(new BasicNameValuePair("hit_meta.proximity.expr", "@proximity"));
				nvps.add(new BasicNameValuePair("hit_meta.categoryweight.expr", "100000*offer_category_weight"));
				nvps.add(new BasicNameValuePair("hit_meta.ca14.expr", "offer_stats_income14_global"));
				nvps.add(new BasicNameValuePair("hit_meta.ca7.expr", "offer_stats_income7_global"));
				nvps.add(new BasicNameValuePair("hit_meta.ca1.expr", "offer_stats_income1_global"));
				nvps.add(new BasicNameValuePair("output_format", "csv"));
				nvps.add(new BasicNameValuePair("nresults",Integer.toString(volume)));
				nvps.add(new BasicNameValuePair("q", request));
				//http://ldc-exa6-search01.cdweb.biz:10010/search-api/search?lang=fr&sl=sl0&use_logic_facets=false&use_logic_hit_metas=false&add_hit_meta=offer_product_id&add_hit_meta=offer_price:&&hit_meta.proximity.expr=@proximity&hit_meta.categoryweight.expr=100000*offer_category_weight&hit_meta.ca14.expr=offer_stats_income14_global&hit_meta.ca7.expr=offer_stats_income7_global&hit_meta.ca1.expr=offer_stats_income1_global&output_format=csv&nresults=2500&q=canape+angle AND offer_is_best_offer=1
				UrlEncodedFormEntity url_encoding = null;
				url_encoding = new UrlEncodedFormEntity(nvps);
				httpPost.setEntity(url_encoding);
				response2 = httpclient.execute(httpPost);
				//System.out.println(Thread.currentThread() +response2.getStatusLine());
				HttpEntity entity2=response2.getEntity();
				String to_parse = EntityUtils.toString(entity2);
				// do something useful with the response body
				// and ensure it is fully consumed
				List<ULRLineToInsert> toresult = parse_results(to_parse,result.getRequest());
				result.setResult(toresult);
				my_fetched_infos.add(result);
				EntityUtils.consume(entity2);
				System.out.println(Thread.currentThread()+"finished "+i+"over "+total_tofetch);
			} catch (Exception e){
				System.out.println("Trouble requesting the server for "+result.getRequest());
				e.printStackTrace();
				System.out.println("Trouble requesting the server for "+result.getRequest());
			} finally {
				if (response2 != null){
					try {
						response2.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return my_fetched_infos;
	}

	private List<ULRLineToInsert> parse_results(String to_parse, String request){
		List<ULRLineToInsert> all_parsed_results = new ArrayList<ULRLineToInsert>();
		System.out.println(to_parse);
		String[] lines = to_parse.split("\n");
		for (int i=1;i<lines.length;i++){
			//# did,url,buildGroup,source,slice,score,mask,sort,offer_product_id,offer_price,offer_seller_id,proximity,ca1,categoryweight,ca14,ca7,termscore
			//   1   2   3         4       5     6     7    8        9                10          11             12    13      14          15  16     17
			String[] fields = lines[i].split(",");
			if (fields.length != 17){
				System.out.println("Trouble with request : "+request);
			} else {
				ULRLineToInsert toinsert = new ULRLineToInsert();
				toinsert.setRequest(request);
				toinsert.setDid(Integer.valueOf(fields[0]));
				String url = fields[1];
				url=url.replace("\"","");
				toinsert.setUrl(url);
				toinsert.setScore(Integer.valueOf(fields[5]));
				String offer_product_id = fields[8];
				offer_product_id=offer_product_id.replace("\"","");
				toinsert.setOffer_product_id(offer_product_id);
				String offer_price = fields[9];
				offer_price=offer_price.replace("\"","");
				toinsert.setOffer_price(Double.valueOf(offer_price));
				String offer_seller_id = fields[10];
				offer_seller_id=offer_seller_id.replace("\"","");
				toinsert.setOffer_seller_id(Integer.valueOf(offer_seller_id));
				String proximity = fields[11];
				proximity=proximity.replace("\"","");			
				toinsert.setProximity(Double.valueOf(proximity));
				String ca1 = fields[12];
				ca1=ca1.replace("\"","");
				toinsert.setCa1(Double.valueOf(ca1));
				String categoryWeight = fields[13];
				categoryWeight=categoryWeight.replace("\"","");
				toinsert.setCategoryweight(Double.valueOf(categoryWeight));
				String ca14 = fields[14];
				ca14=ca14.replace("\"","");
				toinsert.setCa14(Double.valueOf(ca14));
				String ca7 = fields[15];
				ca7=ca7.replace("\"","");
				toinsert.setCa7(Double.valueOf(ca7));
				String termscore = fields[16];
				termscore=termscore.replace("\"","");
				toinsert.setTermscore(Integer.valueOf(termscore));
				all_parsed_results.add(toinsert);
			}
		}
		return all_parsed_results;
	}

	class ULRLineToInsert{
		public String getRequest() {
			return request;
		}
		public void setRequest(String request) {
			this.request = request;
		}
		public int getDid() {
			return did;
		}
		public void setDid(int did) {
			this.did = did;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		public String getOffer_product_id() {
			return offer_product_id;
		}
		public void setOffer_product_id(String offer_product_id) {
			this.offer_product_id = offer_product_id;
		}
		public double getOffer_price() {
			return offer_price;
		}
		public void setOffer_price(double offer_price) {
			this.offer_price = offer_price;
		}
		public int getOffer_seller_id() {
			return offer_seller_id;
		}
		public void setOffer_seller_id(int offer_seller_id) {
			this.offer_seller_id = offer_seller_id;
		}
		public double getProximity() {
			return proximity;
		}
		public void setProximity(double proximity) {
			this.proximity = proximity;
		}
		public double getCa1() {
			return ca1;
		}
		public void setCa1(double ca1) {
			this.ca1 = ca1;
		}
		public double getCategoryweight() {
			return categoryweight;
		}
		public void setCategoryweight(double categoryweight) {
			this.categoryweight = categoryweight;
		}
		public double getCa14() {
			return ca14;
		}
		public void setCa14(double ca14) {
			this.ca14 = ca14;
		}
		public double getCa7() {
			return ca7;
		}
		public void setCa7(double ca7) {
			this.ca7 = ca7;
		}
		public int getTermscore() {
			return termscore;
		}
		public void setTermscore(int termscore) {
			this.termscore = termscore;
		}
		private String request;
		private int did;
		private String url;
		private int score;
		private String offer_product_id;
		private double offer_price;
		private int offer_seller_id;
		private double proximity;
		private double ca1;
		private double categoryweight;
		private double ca14;
		private double ca7;
		private int termscore;
	}


}