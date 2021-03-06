package crawl4j.parser.testing;

import java.io.IOException;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AttributesParsingTestingClass {
	public static void main(String[] args){
		//String my_url_to_fetch = "http://www.cdiscount.com/electromenager/tous-nos-accessoires/joint-hublot-d-30-30-cm/f-11029-ind3662734065501.html#mpos=2|mp";
		String my_url_to_fetch = "http://www.cdiscount.com/le-sport/vetements-de-sport/kappa-survetement-armor-homme/f-121020526-3025ej0005.html#mpos=1|cd";
		// fetching data using jQuery
		org.jsoup.nodes.Document doc;
		try{
			// we wait between 30 and 70 seconds
			doc =  Jsoup.connect(my_url_to_fetch)
					.userAgent("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)")
					.referrer("accounterlive.com")
					.ignoreHttpErrors(true)
					.timeout(0)
					.get();
			Elements attributes = doc.select(".fpDescTb tr");
			int nb_arguments = 0 ;
			StringBuilder arguments_text = new StringBuilder();
			for (Element tr_element : attributes){
				Elements td_elements = tr_element.select("td");
				System.out.println(td_elements.size()+"\n");
				if (td_elements.size() == 2){
					nb_arguments++;
					String category = td_elements.get(0).text();
					arguments_text.append(category+"|||");	
					String description = td_elements.get(1).text();                                    
					arguments_text.append(description);		
					arguments_text.append("@@");
				}
			}
			System.out.println("Number of arguments "+ nb_arguments);
			System.out.println("Arguments "+ arguments_text.toString());
			parse_arguments(arguments_text.toString());

		}
		catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public static void parse_arguments(String arguments_listing){
		StringTokenizer arguments_tokenizer = new StringTokenizer(arguments_listing,"@@");
		while(arguments_tokenizer.hasMoreTokens()){
			String argument_pair = arguments_tokenizer.nextToken();
			StringTokenizer pair_tokenizer = new StringTokenizer(argument_pair,"|||");
			if(pair_tokenizer.hasMoreTokens()){
				String value = pair_tokenizer.nextToken();
				String description = pair_tokenizer.nextToken();
				System.out.println("Value : "+value+"\n");
				System.out.println("Description : "+description+"\n");
			}
		}

	}
}


