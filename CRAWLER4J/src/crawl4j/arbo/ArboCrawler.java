package crawl4j.arbo;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawl4j.urlutilities.ArboInfo;
import crawl4j.urlutilities.URL_Utilities;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ArboCrawler extends WebCrawler {

	// that is here that we will build our predictor matrix by scraping the web site
	// we intend to accumulate the following predictors
	// number of inlinks
	// presence of breadcrumbs, 
	// price occurences,
	// number of images,
	// presence of order button,
	// content volume,
	// rich snippet avis 
	// we can add the following as last resort :
	// url, pattern in the url 
	// but the classifier should guess without it 
	// size of the in memory cache per thread (200 default value)

	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpeg" + "|png|tiff|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|ico|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	ArboCrawlDataManagement myCrawlDataManager;

	public ArboCrawler() {
		myCrawlDataManager = new ArboCrawlDataManagement();
	}

	// we don't visit media URLs and we keep inside Cdiscount
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !filters.matcher(href).matches() && href.startsWith("http://www.cdiscount.com/");
	}

	@Override
	public void visit(Page page) {
		// we here parse the html to fill up the cache with the following information


		String url = page.getWebURL().getURL();
		System.out.println(Thread.currentThread()+": Visiting URL : "+url);
		ArboInfo info =myCrawlDataManager.getCrawledContent().get(url);
		if (info == null){
			info =new ArboInfo();
		}		
		info.setUrl(url);
		info.setDepth((int)page.getWebURL().getDepth());
		myCrawlDataManager.incProcessedPages();	

		List<WebURL> links = null;

		if (page.getParseData() instanceof HtmlParseData) {
			// number of inlinks
			// presence of breadcrumbs, 
			// price occurences,
			// number of images,
			// presence of order button,
			// content volume,
			// rich snippet avis 
			// we can add the following as last resort :
			// url, pattern in the url 
			// but the classifier should guess without it 
			// size of the in memory cache per thread (200 default value)
			
			
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			info.setText(htmlParseData.getText());
			String html = htmlParseData.getHtml();
			links = htmlParseData.getOutgoingUrls();
			myCrawlDataManager.incTotalLinks(links.size());
			myCrawlDataManager.incTotalTextSize(htmlParseData.getText().length());	

			// we here filter the outlinks we want to keep (they must be internal and they must respect the robot.txt
			Set<String> filtered_links = filter_out_links(links);
			info.setLinks_size(filtered_links.size());
			info.setOut_links(filtered_links.toString());

			Document doc = Jsoup.parse(html);
			Elements titleel = doc.select("title");
			info.setTitle(titleel.text());
			// fetching the H1 element
			Elements h1el = doc.select("h1");
			info.setH1(h1el.text());
			// finding the footer
			Elements footerel = doc.select("div.ftMention");
			info.setFooter(footerel.text());
			// finding the ztd
			Elements ztdunfolded = doc.select("p.scZtdTxt");
			Elements ztdfolded = doc.select("p.scZtdH");
			info.setZtd((ztdunfolded==null? "":ztdunfolded.text())+(ztdfolded==null? "":ztdfolded.text()));
			// finding the short description
			Elements short_desc_el = doc.select("p.fpMb");
			info.setShort_desc((short_desc_el==null? "":short_desc_el.text()));
			// finding the vendor
			Elements resellers = doc.select(".fpSellBy");
			StringBuilder resellerBuilder = new StringBuilder();
			for (Element reseller : resellers){
				if(reseller.getElementsByTag("a") != null){
					resellerBuilder.append(reseller.getElementsByTag("a").text());
				}
			}
			String vendor = resellerBuilder.toString();
			info.setVendor(vendor);

			// finding the number of attributes
			Elements attributes = doc.select(".fpDescTb tr");
			int nb_arguments = 0 ;
			StringBuilder arguments_text = new StringBuilder();
			for (Element tr_element : attributes){
				Elements td_elements = tr_element.select("td");
				if (td_elements.size() == 2){
					nb_arguments++;
					String category = td_elements.get(0).text();
					arguments_text.append(category+"|||");	
					String description = td_elements.get(1).text();                                    
					arguments_text.append(description);		
					arguments_text.append("@@");
				}
			}
			info.setAtt_number(nb_arguments);
			info.setAtt_desc(arguments_text.toString());
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		StringBuilder conc = new StringBuilder();
		if (responseHeaders != null) {
			for (Header header : responseHeaders) {
				conc.append( header.getName() + ": " + header.getValue()  + "@");
			}
			info.setResponse_headers(conc.toString());	
		}

		myCrawlDataManager.getCrawledContent().put(url,info);

		// we save the cache only at the very end of the crawl
	}

	public Set<String> filter_out_links(List<WebURL> links){
		Set<String> outputSet = new HashSet<String>();
		for (WebURL url_out : links){
			if ((shouldVisit(url_out)) && (getMyController().getRobotstxtServer().allows(url_out))){
				String final_link = URL_Utilities.drop_parameters(url_out.getURL());
				outputSet.add(final_link);
			}
		}
		return outputSet;
	}

	// This function is called by controller to get the local data of this
	// crawler when job is finished
	@Override
	public Object getMyLocalData() {
		return myCrawlDataManager;
	}

	// This function is called by controller before finishing the job.
	// You can put whatever stuff you need here.
	@Override
	public void onBeforeExit() {
		// we only save data from the cache at the very end
		saveData();
		try {
			myCrawlDataManager.getCon().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		String url = webUrl.getURL();
		ArboInfo info =myCrawlDataManager.getCrawledContent().get(url);
		if (info == null){
			info =new ArboInfo();
		}	
		info.setStatus_code(statusCode);
		myCrawlDataManager.getCrawledContent().put(url,info);
	}

	public void saveData(){
		int id = getMyId();
		// This is just an example. Therefore I print on screen. You may
		// probably want to write in a text file.
		System.out.println("Crawler " + id + "> Processed Pages: " + myCrawlDataManager.getTotalProcessedPages());
		System.out.println("Crawler " + id + "> Total Links Found: " + myCrawlDataManager.getTotalLinks());
		System.out.println("Crawler " + id + "> Total Text Size: " + myCrawlDataManager.getTotalTextSize());
		myCrawlDataManager.saveData();	
	}

}