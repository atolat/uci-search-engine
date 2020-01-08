/*    */ package com.ir.crawler;
/*    */ 
/*    */ import edu.uci.ics.crawler4j.crawler.Page;
/*    */ import edu.uci.ics.crawler4j.crawler.WebCrawler;
/*    */ import edu.uci.ics.crawler4j.parser.HtmlParseData;
/*    */ import edu.uci.ics.crawler4j.url.WebURL;
/*    */ import java.util.Set;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ public class Crawler
/*    */   extends WebCrawler
/*    */ {
/* 14 */   private static final Pattern FILTERS = Pattern.compile(".*\\.(bmp|gif|jpe?g|png|tiff?|pdf|ico|xaml|pict|rif|pptx?|ps|mid|mp2|mp3|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox|avi|mov|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha|js|tex|ppt|doc|docx|xls|csv|exe|xlsx|tar|iso|epub)(\\?.*)?$");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldVisit(Page referringPage, WebURL url) {
/* 34 */     String href = url.getURL().toLowerCase();
/* 35 */     return (!FILTERS.matcher(href).matches() && href
/* 36 */       .contains(".ics.uci.edu/") && 
/* 37 */       !href.startsWith("http://archive.ics.uci.edu/") && 
/* 38 */       !href.startsWith("http://calendar.ics.uci.edu/") && 
/* 39 */       !href.startsWith("https://calendar.ics.uci.edu/") && 
/* 40 */       !href.startsWith("http://flamingo.ics.uci.edu/releases") && 
/* 41 */       !href.startsWith("http://ironwood.ics.uci.edu/") && 
/* 42 */       !href.startsWith("http://drzaius.ics.uci.edu/") && 
/* 43 */       !href.startsWith("http://duttgroup.ics.uci.edu/") && 
/* 44 */       !href.startsWith("https://duttgroup.ics.uci.edu/") && 
/* 45 */       !href.startsWith("http://fano.ics.uci.edu/ca/") && 
/* 46 */       !href.startsWith("http://djp3-pc2.ics.uci.edu/LUCICodeRespository/") && 
/* 47 */       !href.startsWith("https://www.ics.uci.edu/prospective/") && 
/* 48 */       !href.startsWith("http://www.ics.uci.edu/prospective/"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(Page page) {
/* 59 */     String url = page.getWebURL().getURL();
/* 60 */     System.out.println("URL: " + url);
/* 61 */     if (page.getParseData() instanceof HtmlParseData) {
/* 62 */       int docId = page.getWebURL().getDocid();
/* 63 */       String subDomain = page.getWebURL().getSubDomain();
/* 64 */       HtmlParseData htmlParseData = (HtmlParseData)page.getParseData();
/* 65 */       String text = htmlParseData.getText();
/* 66 */       String html = htmlParseData.getHtml();
/* 67 */       Set<WebURL> links = htmlParseData.getOutgoingUrls();
/* 68 */       System.out.println("Text length: " + text.length());
/*    */ 
/*    */       
/*    */       try {
/* 72 */         Storage.add(url, text, docId, subDomain, html);
/* 73 */       } catch (Exception e) {
/* 74 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/crawler/Crawler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */