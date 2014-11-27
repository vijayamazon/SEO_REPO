package crawl4j.corpus.amazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import crawl4j.continuous.CrawlDataManagement;

public class CorpusCrawlDataManagement {

	private Connection con;
	private static String find_statement="select NB_DOCUMENTS, DOCUMENTS from CORPUS_WORDS where WORD=?";
	private static String insert_statement="INSERT INTO CORPUS_WORDS(WORD,NB_DOCUMENTS,DOCUMENTS) values(?,?,?)";
	private static String update_statement="UPDATE CORPUS_WORDSSET WORD=?,NB_DOCUMENTS=?,DOCUMENTS=?,LAST_UPDATE=? WHERE WORD=?";
	private int totalProcessedPages;
	private long totalTextSize;


	public CorpusCrawlDataManagement() {
		//		Properties props = new Properties();
		//		FileInputStream in = null;      
		//		try {
		//			in = new FileInputStream("database.properties");
		//			props.load(in);
		//		} catch (IOException ex) {
		//			Logger lgr = Logger.getLogger(BenchmarkingController.class.getName());
		//			lgr.log(Level.FATAL, ex.getMessage(), ex);
		//		} finally {
		//			try {
		//				if (in != null) {
		//					in.close();
		//				}
		//			} catch (IOException ex) {
		//				Logger lgr = Logger.getLogger(BenchmarkingController.class.getName());
		//				lgr.log(Level.FATAL, ex.getMessage(), ex);
		//			}
		//		}
		String url="jdbc:postgresql://localhost/CRAWL4J";
		String user="postgres";
		String passwd="mogette";
		try{
			con = DriverManager.getConnection(url, user, passwd);
		} catch (Exception e){
			System.out.println("Error instantiating either database or solr server");
			e.printStackTrace();
		}
	}

	public void updateWord(String word){
		try{
			// finding if the world is already present in our database dictionary
			PreparedStatement select_st = con.prepareStatement(find_statement);
			select_st.setString(1,word);
			ResultSet rs = select_st.executeQuery();
			boolean found = false;
			String document_list="";
			int nb_documents = 0;
			if (rs.next()) {
				nb_documents =rs.getInt(1);
				document_list=rs.getString(2);
				found = true;
			}
			select_st.close();
			
			if (found){	
				// we found it
				// we check if the current url is already in the list
				boolean isAlreadyPresent=checkIfAlreadyThere(document_list);
				
				// if the document is already listed, the frequency is up to date, we do nothing
				if (!isAlreadyPresent){
					// else if the document is not present, we must update the database row by adding the document
					PreparedStatement update_st = con.prepareStatement(update_statement);
				
				}
			}else{
				// we did not find it
				// we have to add it 
				PreparedStatement insert_st = con.prepareStatement(insert_statement);
				
			}

			// preparing the statement

			//					st.setString(2,info.getTitle());
			//					st.setInt(3,info.getLinks_size());
			//					st.setString(4,info.getOut_links());
			//					st.setString(5,info.getH1());
			//					st.setString(6,info.getFooter());
			//					st.setString(7,info.getZtd());
			//					st.setString(8,info.getShort_desc());
			//					st.setString(9,info.getVendor());
			//					st.setString(10,info.getAtt_desc());
			//					st.setInt(11,info.getAtt_number());
			//					st.setInt(12,info.getStatus_code());
			//					st.setString(13,info.getResponse_headers());		
			//					st.setInt(14,info.getDepth());
			//					st.setString(15, info.getPage_type());
			//					st.setString(16, info.getMagasin());
			//					st.setString(17, info.getRayon());
			//					st.setString(18, info.getProduit());
			//					java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
			//					st.setDate(19,sqlDate);
			//					st.setString(20,url);
//			int affected_row = st.executeUpdate();
//			// if the row has not been updated, we have to insert it !
//			if(affected_row == 0){
//				PreparedStatement insert_st = con.prepareStatement(insert_statement);
//				insert_st.setString(1,url);
//				insert_st.setString(2,info.getText());
//				insert_st.setString(3,info.getTitle());
//				insert_st.setInt(4,info.getLinks_size());
//				insert_st.setString(5,info.getOut_links());
//				insert_st.setString(6,info.getH1());
//				insert_st.setString(7,info.getFooter());
//				insert_st.setString(8,info.getZtd());
//				insert_st.setString(9,info.getShort_desc());
//				insert_st.setString(10,info.getVendor());
//				insert_st.setString(11,info.getAtt_desc());
//				insert_st.setInt(12,info.getAtt_number());
//				insert_st.setInt(13,info.getStatus_code());
//				insert_st.setString(14,info.getResponse_headers());		
//				insert_st.setInt(15,info.getDepth());
//				insert_st.setString(16, info.getPage_type());
//				insert_st.setString(17, info.getMagasin());
//				insert_st.setString(18, info.getRayon());
//				insert_st.setString(19, info.getProduit());
//				insert_st.setDate(20,sqlDate);
//				insert_st.executeUpdate();
//			}

			System.out.println(Thread.currentThread()+"Committed " +  " updates");

		} catch (SQLException e){
			//System.out.println("Line already inserted : "+nb_lines);
			e.printStackTrace();  
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex1) {
					Logger lgr = Logger.getLogger(CrawlDataManagement.class.getName());
					lgr.log(Level.ERROR, ex1.getMessage(), ex1);
				}
			}

			Logger lgr = Logger.getLogger(CrawlDataManagement.class.getName());
			lgr.log(Level.ERROR, e.getMessage(), e);
		}	

	}
	
	
	public boolean checkIfAlreadyThere(String documentList){
		return false;
	}

	//	public void updateSolrData() {
	//		try{
	//			Iterator<Entry<String, CORPUSinfo>> it = crawledContent.entrySet().iterator();
	//			int local_counter = 0;
	//			if (it.hasNext()){
	//				local_counter++;
	//				do {
	//					local_counter ++;
	//					Map.Entry<String, CORPUSinfo> pairs = it.next();
	//					String url=(String)pairs.getKey();
	//					CORPUSinfo info = (CORPUSinfo)pairs.getValue();
	//					SolrInputDocument doc = new SolrInputDocument();
	//					doc.addField("id",url.replace("http://www-history.mcs.st-andrews.ac.uk/",""));
	//					doc.addField("url",url);
	////					doc.addField("whole_text",info.getText());
	////					doc.addField("title",info.getTitle());
	////					doc.addField("links_size",info.getLinks_size());
	////					doc.addField("links",info.getOut_links());
	////					doc.addField("h1",info.getH1());
	////					doc.addField("short_description",info.getShort_description());
	////					doc.addField("birth_date",info.getBirth_date());
	////					doc.addField("birth_place",info.getBirth_location());
	////					doc.addField("death_date",info.getDeath_date());
	////					doc.addField("death_place",info.getDeath_location());
	//					java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
	//					doc.addField("last_update", sqlDate.toString());	
	//					try{
	//						solr_server.add(doc);
	//					}catch (Exception e){
	//						System.out.println("Trouble inserting : "+url);
	//						e.printStackTrace();  
	//					}
	//				}while (it.hasNext());	
	//				solr_server.commit();
	//				System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
	//			}
	//		} catch (Exception e){
	//			//System.out.println("Line already inserted : "+nb_lines);
	//			e.printStackTrace();  
	//		}
	//	}

	// we here perform upsert to keep up to date our crawl referential
	//	public void updateData(){
	//		updateSolrData();
	//		// clear cache
	//		crawledContent.clear();
	//	}

	//	public Map<String, CORPUSinfo> getCrawledContent() {
	//		return crawledContent;
	//	}
	//
	//	public void setCrawledContent(Map<String, CORPUSinfo> crawledContent) {
	//		this.crawledContent = crawledContent;
	//	}
	
	
	public int getTotalProcessedPages() {
		return totalProcessedPages;
	}

	public void setTotalProcessedPages(int totalProcessedPages) {
		this.totalProcessedPages = totalProcessedPages;
	}

	public long getTotalTextSize() {
		return totalTextSize;
	}

	public void setTotalTextSize(long totalTextSize) {
		this.totalTextSize = totalTextSize;
	}
}