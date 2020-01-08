/*     */ package com.ir.search;
/*     */ 
/*     */ import com.ir.crawler.Storage;
/*     */ import com.ir.indexer.Indexer;
/*     */ import com.ir.ranking.NDCG;
/*     */ import com.mongodb.BasicDBObject;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.TreeMap;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.select.Elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*  36 */   static String mainQuery = "security";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  42 */     Indexer index = new Indexer();
/*     */ 
/*     */     
/*  45 */     String searchQuery = "security";
/*  46 */     searchQuery = searchQuery.toLowerCase();
/*  47 */     String trimmed = searchQuery.trim();
/*  48 */     int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
/*  49 */     System.out.println(words);
/*  50 */     HashMap<String, Double> urlMap = new HashMap<String, Double>();
/*  51 */     if (words == 1) {
/*  52 */       getTfIdf(searchQuery, index, urlMap);
/*     */     } else {
/*  54 */       Scanner scanner = new Scanner(searchQuery);
/*  55 */       while (scanner.hasNext()) {
/*  56 */         String s = scanner.next();
/*  57 */         System.out.println(s);
/*  58 */         getTfIdf(s, index, urlMap);
/*     */       } 
/*  60 */       scanner.close();
/*     */     } 
/*  62 */     ArrayList<String> rankedResults = sortTopTenUrls(index, urlMap);
/*  63 */     System.out.println(rankedResults);
/*  64 */     Elements links = getGoogleResults(searchQuery.concat(" site:ics.uci.edu"));
/*  65 */     double ndcg = NDCG(links, rankedResults);
/*  66 */     System.out.println(ndcg);
/*     */   }
/*     */   
/*     */   private static Elements getGoogleResults(String searchQuery) throws IOException {
/*  70 */     String google = "http://www.google.com/search?num=10&q=";
/*  71 */     String charset = "UTF-8";
/*  72 */     String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";
/*     */     
/*  74 */     Elements links = Jsoup.connect(google + URLEncoder.encode(searchQuery, charset)).userAgent(userAgent).get().select(".g>.r>a");
/*     */     
/*  76 */     for (Element link : links) {
/*  77 */       String title = link.text();
/*  78 */       String url = link.absUrl("href");
/*  79 */       url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
/*  80 */       if (!url.startsWith("http")) {
/*     */         continue;
/*     */       }
/*     */       
/*  84 */       System.out.println("URL: " + url);
/*     */     } 
/*     */     
/*  87 */     return links;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double NDCG(Elements links, ArrayList<String> rankedResults) throws UnsupportedEncodingException {
/*  92 */     int i = 0;
/*  93 */     String[] urls = new String[links.size()];
/*  94 */     for (Element el : links) {
/*  95 */       urls[i] = el.absUrl("href");
/*  96 */       urls[i] = URLDecoder.decode(urls[i].substring(urls[i].indexOf('=') + 1, urls[i].indexOf('&')), "UTF-8");
/*  97 */       if (!urls[i].startsWith("http")) {
/*     */         continue;
/*     */       }
/* 100 */       i++;
/*     */     } 
/* 102 */     List<String> correctUrlList = Arrays.asList(urls);
/* 103 */     return NDCG.compute(rankedResults, correctUrlList, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashMap<String, Double> getTfIdf(String searchQuery, Indexer index, HashMap<String, Double> urlMap) throws Exception {
/* 108 */     HashMap<String, Double> tfIdf_temp = new HashMap<String, Double>();
/* 109 */     BasicDBObject query = new BasicDBObject("token", new BasicDBObject("$regex", searchQuery));
/* 110 */     tfIdf_temp = index.getTfIdfData("tfIdf", query);
/* 111 */     int count = 0;
/* 112 */     for (Map.Entry<String, Double> entry : tfIdf_temp.entrySet()) {
/* 113 */       String key = (String)entry.getKey();
/* 114 */       System.out.println(key);
/* 115 */       Double value = (Double)entry.getValue();
/* 116 */       if (count > 50) {
/*     */         break;
/*     */       }
/* 119 */       count++;
/* 120 */       BasicDBObject titleQuery = new BasicDBObject();
/* 121 */       titleQuery.put("url", key);
/* 122 */       String html = Storage.getData("html", titleQuery);
/* 123 */       Document doc = Jsoup.parse(html);
/* 124 */       String title = doc.title();
/* 125 */       title = title.toLowerCase();
/* 126 */       boolean contains = title.contains(mainQuery);
/* 127 */       if (urlMap.get(key) == null) {
/* 128 */         if (contains) {
/* 129 */           urlMap.put(key, Double.valueOf(value.doubleValue() + 10.0D)); continue;
/*     */         } 
/* 131 */         urlMap.put(key, value);
/*     */         continue;
/*     */       } 
/* 134 */       double newValue = ((Double)urlMap.get(key)).doubleValue() + value.doubleValue();
/* 135 */       if (contains) {
/* 136 */         urlMap.put(key, Double.valueOf(newValue + 10.0D)); continue;
/*     */       } 
/* 138 */       urlMap.put(key, Double.valueOf(newValue));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     return urlMap;
/*     */   }
/*     */   
/*     */   private static ArrayList<String> sortTopTenUrls(Indexer index, HashMap<String, Double> urlMap) throws Exception {
/* 147 */     ArrayList<String> topTenUrlslist = new ArrayList<String>();
/* 148 */     TreeMap<String, Double> sortedUrlMap = index.sort(urlMap);
/* 149 */     for (Map.Entry<String, Double> entry : sortedUrlMap.entrySet()) {
/* 150 */       if (topTenUrlslist.size() > 9) {
/*     */         break;
/*     */       }
/* 153 */       topTenUrlslist.add(entry.getKey());
/*     */     } 
/*     */     
/* 156 */     return topTenUrlslist;
/*     */   }
/*     */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/search/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */