package org.adobe.compadmin.utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

public class ParseHTMLUtility {

	private static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
	private static Pattern pattern;
	private static Matcher matcher;
    static Map<String, String> ruleMap = new LinkedHashMap<String, String>();


	public static void main(String[] args) {
		String html = "<div class=\"article__category-block__copy-container\">"
				+ " <h2 class=\"article__category-block__title\">Check in online</h2>"
				+ "<p class=\"article__category-block__description\">"
				+ "Complete your registration online to make your arrival process even quicker. Simply fill in your details ahead of your break then have your booking reference and accommodation number on hand at arrival.\n"
				+ "</p>" +"<img src=\"\"/>"+ "<a class=\"btn btn--book\" href=\"#\" target=\"\">"
				+ "<span>how are you.</span>" + "</a>" + "</div>";
		
		ruleMap.put("a", "attribute,href,pathfield");
		ruleMap.put("img", "attribute,src,image");
	    ruleMap.put("button", "arrtribute,href,pathfield");


		parseHTMLUsingRuleMap(html);

	}

	public static void parseHTML(String html) {
		Document document = Jsoup.parse(html);
		document.traverse(new NodeVisitor() {
			int counter = 0;
			final Map<String, String> myMap = new LinkedHashMap<String, String>();
			
	
	


			public void head(Node node, int depth) {
//				System.out.println(node.nodeName());
				// TextField
				if ((node instanceof TextNode && !((TextNode) node).isBlank())) {
					
					String replacementValue = "${properties.element"
							+ (counter) + "}";
					String xtype = validateTextType(((TextNode) node).text());
					myMap.put(replacementValue, xtype);
					// replace val
					Node myNode = node.parent();
					Element element = (Element) myNode;
					element.text(replacementValue);
					
					counter++;

				} else if ( node.nodeName().equalsIgnoreCase("img") || node.nodeName().equalsIgnoreCase("a")) {
					
					String replacementValue = "${properties.element"
							+ (counter) + "}";
					
					if(node.nodeName().equalsIgnoreCase("a")){
						node.attr("href", replacementValue);
						myMap.put(replacementValue, "pathfield");
					
					}
					if(node.nodeName().equalsIgnoreCase("img")){
						node.attr("src", replacementValue);
						myMap.put(replacementValue, "image");

						
					}
					counter++;

				}

			}

			public void tail(Node node, int depth) {

			}
		});
		
		System.out.println("hello"+document.html());

	}
	
	

	public static void parseHTMLUsingRuleMap(String html) {
		Document document = Jsoup.parse(html);
		document.traverse(new NodeVisitor() {
			int counter = 0;
			final Map<String, String> myMap = new LinkedHashMap<String, String>();
			
	
	


			public void head(Node node, int depth) {
//				System.out.println(node.nodeName());
				// TextField
				if ((node instanceof TextNode && !((TextNode) node).isBlank())) {
					
					String replacementValue = "${properties.element"
							+ (counter) + "}";
					String xtype = validateTextType(((TextNode) node).text());
					myMap.put(replacementValue, xtype);
					// replace val
					Node myNode = node.parent();
					Element element = (Element) myNode;
					element.text(replacementValue);
					
					counter++;

				} else if ( node.nodeName().equalsIgnoreCase("img") || node.nodeName().equalsIgnoreCase("a")) {
					
					String replacementValue = "${properties.element"
							+ (counter) + "}";
					
					if(ruleMap.containsKey(node.nodeName())){
						String val = ruleMap.get(node.nodeName());
						String[] data = val.split(",");
						if(data[0].equalsIgnoreCase("attribute")){
							node.attr(data[1], replacementValue);
							myMap.put(replacementValue, data[2]);

						}
						
					}
						
					counter++;

				}

			}

			public void tail(Node node, int depth) {

			}
		});
		
		System.out.println("hello"+document.html());

	}

	private static String validateTextType(String text) {
		pattern = Pattern.compile(HTML_TAG_PATTERN);
		matcher = pattern.matcher(text);
		if (matcher.matches()) {
			return "richtext";
		} else {
			return "textfield";
		}
	}
}
