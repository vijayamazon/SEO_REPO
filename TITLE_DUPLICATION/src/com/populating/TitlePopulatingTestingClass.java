package com.populating;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TitlePopulatingTestingClass {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/title_duplication.properties";

	private static int counter = 0;

	public static void main(String[] args) throws IOException {
		// Reading the property of our database
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
			props.load(in);
		} catch (IOException ex) {
			Logger lgr = Logger.getLogger(TitlePopulatingTestingClass.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				Logger lgr = Logger.getLogger(TitlePopulatingTestingClass.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		// the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");
		// the following properties have been identified for our files to parse 
		// and insert into a database
		String csvFile = "/home/sduprey/My_Data/My_GWT_Extracts/22012015.csv";
		//String csvFile = "/home/sduprey/My_Data/My_GWT_Extracts/extract_dupli_20_oct.csv";
		BufferedReader br = null;
		String line = "";
		String header = null;
		String[] column_names = null;
		String cvsSplitBy = ",|;";
		int nb_line=1;
		// last error
		br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));	
		// we skip the first line : the headers
		header = br.readLine();
		column_names= header.split(cvsSplitBy);
		while ((line = br.readLine()) != null) {
			if (nb_line >= counter){
				// we have to fing the very last comma of the sentence		
				try{
					String title = line.substring(0,line.lastIndexOf(","));
					String remaining = line.substring(line.lastIndexOf(",")+1,line.indexOf(";",line.lastIndexOf(",")+1));
					System.out.println("Inserting line number :"+nb_line);
					String[] splitted_line = remaining.split("\\|");
					String protocol = "";
					String magasin = "";
					String rayon = "";
					String produit = "";
					String domain = "";
					String current_url="";
					if (splitted_line.length>=2){
						StringTokenizer tokenize = new StringTokenizer(splitted_line[1],"|");
						int counter=0;
						while (tokenize.hasMoreTokens()) {
							counter++;
							current_url=tokenize.nextToken();
							StringTokenizer tokenizebis = new StringTokenizer(current_url,"/");
							if (tokenizebis.hasMoreTokens()){
								protocol = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								domain = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								magasin = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								rayon = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								produit = tokenizebis.nextToken();
							}
						}
						System.out.println("Title : "+title);
						System.out.println("counter : "+counter);
						System.out.println("URL : "+splitted_line[1]);
						System.out.println("Magasin : "+magasin);
						System.out.println("Rayon : "+rayon);
						System.out.println("Produit : "+produit);
						if ((magasin == null )||("".equals(magasin)) ){
							System.out.println("Warning : empty magasin");
							System.out.println("URL" + current_url);
							System.out.println("Line" + line);
						}
					}					
				}catch (Exception e){
					e.printStackTrace();
					System.out.println("Trouble with line : " + line);
				}
				nb_line++;
			}
		}
	}
}