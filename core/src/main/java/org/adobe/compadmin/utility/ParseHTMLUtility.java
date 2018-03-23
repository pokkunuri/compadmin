package org.adobe.compadmin.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.sling.api.SlingHttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParseHTMLUtility {

       private static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
       private static Pattern pattern;
       private static Matcher matcher;
       private static Map<String, String> ruleMap = new LinkedHashMap<String, String>();
       private static Map<String, String> myMap = new LinkedHashMap<String, String>();

       public static String parseHTMLUsingRuleMap(String html) {
    	     ruleMap.put("a", "attribute,href,pathfield");
             ruleMap.put("img", "attribute,src,image");
             ruleMap.put("button", "arrtribute,href,pathfield");
           
             Document document = Jsoup.parseBodyFragment(html);
             document.traverse(new NodeVisitor() {
                    int counter = 0;
                    public void head(Node node, int depth) {
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

                          } else if (node.nodeName().equalsIgnoreCase("img") || node.nodeName().equalsIgnoreCase("a") || node.nodeName().equalsIgnoreCase("button")) {
                                 
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
             Gson gson = new Gson();
             String jsonString  = gson.toJson(myMap);
             JsonElement jelement = new JsonParser().parse(jsonString);
             JsonObject jobject = jelement.getAsJsonObject();
             jobject.addProperty("componentHTML", document.html());
             String dummyJsonToThrow = gson.toJson(jelement);
             return dummyJsonToThrow;
           

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

    public static String getBody(SlingHttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}
