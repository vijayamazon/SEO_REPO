package dupli.titles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FastTitleCrawler {
	// we here just want to get every URL from the input file and get if the SKU is sold by market place/cdiscount and so on

	private static int nb_lines = 0;
	private static int threshold=1000;
	private static int timeout_milliseconds =10000;

	public static void main(String[] args)  {
		System.setProperty("http.agent", "");
		String fileName="/home/sduprey/My_Data/My_GWT_Extracts/My_Title_To_Fetch/title_to_fetch.csv";
		String outputPathFileName = "/home/sduprey/My_Data/My_Outgoing_Data/My_Title_MP_Extract/results_title_to_fetch.csv";
		try {
			// we print the file just after we crawled the web site
			make_your_job_and_fast(fileName,outputPathFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Trouble saving result flat file : "+outputPathFileName);
			e.printStackTrace();
		}
	}

	private static void make_your_job_and_fast(String fileName,String outputPathFileName) throws IOException{
		System.out.println("Reading line number : "+nb_lines);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		BufferedWriter writer = null;
		// we open the file
		writer=  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPathFileName), "UTF-8"));	
		// we write the header
		writer.write("Title;url_1;offer_1;url_2;offer_2;url_3;offer_3;url_4;offer_4;url_5;offer_5;url_6;offer_6;url_7;offer_7;url_8;offer_8;url_9;offer_9;url_10;offer_10;url_11;offer_11;url_12;offer_12;url_13;offer_13;url_14;offer_14;url_15;offer_15;url_16;offer_16;url_17;offer_17;url_18;offer_18;url_19;offer_19;url_20;offer_20;url_21;offer_21;url_22;offer_22;url_23;offer_23;url_24;offer_24;url_25;offer_25;url_26;offer_26;url_27;offer_27;url_28;offer_28;url_29;offer_29;url_30;offer_30\n");
		String header = br.readLine();
		System.out.println(header);
		String line="";
		while ((line = br.readLine()) != null) {
			String[] pieces=line.split(";");
			LineItem item = new LineItem();
			List<DiscriminedURL> url_to_fetch = new ArrayList<DiscriminedURL>();

			for (int i=0;i<pieces.length;i++){
				if (i==0){
					item.setTitle(pieces[i]);
				} else {

					DiscriminedURL url = new DiscriminedURL();
					url.setUrl(pieces[i]);
					url_to_fetch.add(url);
				}
			}
			item.setUrls(url_to_fetch);
			
			List<DiscriminedURL> urls_to_fetch = item.getUrls();
			for (DiscriminedURL url : urls_to_fetch){
				try {
					if (!"".equals(url.getUrl())){
						System.out.println("Beginning to fetch url : "+url.getUrl());
						String type =fetch_url(url.getUrl());
						url.setOffer(type);
						System.out.println("Type : "+type);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Trouble fetching URL : "+url);
				}
				System.out.println("Fetched URLs number "+nb_lines);
			}
			// writing the results
			String to_write = item.getTitle();
			List<DiscriminedURL> urls = item.getUrls();
			for (DiscriminedURL url : urls){
				to_write=to_write+";"+url.getUrl()+";"+url.getOffer();
			}
			System.out.println("Writing line : "+nb_lines);
			writer.write(to_write+"\n");
			nb_lines++;
		}
//
//		for (LineItem item : items){
//			nb_fetched_lines++;
//			List<DiscriminedURL> urls_to_fetch = item.getUrls();
//			for (DiscriminedURL url : urls_to_fetch){
//				try {
//					if (!"".equals(url.getUrl())){
//						System.out.println("Fetching url : "+url.getUrl());
//						String type =fetch_url(url.getUrl());
//						url.setOffer(type);
//						System.out.println("Type : "+type);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println("Trouble fetching URL : "+url);
//				}
//				System.out.println("Fetched URLs number "+nb_fetched_lines+" over "+nb_lines);
//			}
//			// writing the results
//			String to_write = item.getTitle();
//			List<DiscriminedURL> urls = item.getUrls();
//			for (DiscriminedURL url : urls){
//				to_write=to_write+";"+url.getUrl()+";"+url.getOffer();
//			}
//			System.out.println("Writing line : "+nb_fetched_lines);
//			writer.write(to_write+"\n");
//		}
		writer.close();
	}


	private static String fetch_url(String my_url_to_fetch) throws IOException{
		my_url_to_fetch=my_url_to_fetch.trim();
		URL page = new URL(my_url_to_fetch);
		System.out.println("Opening connection  \n");
		HttpURLConnection conn = (HttpURLConnection) page.openConnection();
		conn.setRequestProperty("User-Agent","CdiscountBot-crawler");
		conn.setReadTimeout(timeout_milliseconds);
		conn.connect();
		InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
		BufferedReader buff = new BufferedReader(in);
		String line="";
		int cdiscount_index=0;
		System.out.println("Reading page  \n");
		int nb_doc_lines = 0;
		while ((line = buff.readLine()) != null && nb_doc_lines<threshold) {
			nb_doc_lines++;
			int global_index = line.indexOf("<p class='fpSellBy'>");
			if (global_index >0){
				cdiscount_index = line.indexOf("<p class='fpSellBy'>Vendu et expédié par <span class='logoCDS'>");
				break;
			}
		};
		conn.disconnect();
		System.out.println("Connection disconnected  \n");
		if (cdiscount_index >0){
			return "Cd";
		}else{
			return "MP";
		}
	}

	static class LineItem {
		private String title;
		private List<DiscriminedURL> urls;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<DiscriminedURL> getUrls() {
			return urls;
		}

		public void setUrls(List<DiscriminedURL> urls) {
			this.urls = urls;
		}

	}

	static class DiscriminedURL {
		String url;
		String offer = "Unknown";
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getOffer() {
			return offer;
		}
		public void setOffer(String offer) {
			this.offer = offer;
		}
	}

}