/*     */ package com.ir.crawler;
/*     */ 
/*     */ import com.ir.db.Db;
/*     */ import com.ir.textprocessor.Tokenize;
/*     */ import com.mongodb.BasicDBObject;
/*     */ import com.mongodb.DB;
/*     */ import com.mongodb.DBCollection;
/*     */ import com.mongodb.DBCursor;
/*     */ import com.mongodb.DBObject;
/*     */ import com.mongodb.util.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.TreeMap;
/*     */ import org.bson.BasicBSONObject;
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
/*     */ public class Storage
/*     */ {
/*  29 */   static String crawlerDb = "Crawler_final";
/*  30 */   static String textProcDb = "TextProcessor";
/*     */   public static void add(String url, String text, int docId, String subDomain, String html) throws Exception {
/*  32 */     DB database = Db.connect();
/*  33 */     if (database != null) {
/*  34 */       DBCollection collection = database.getCollection("WebDocs");
/*  35 */       BasicDBObject document = new BasicDBObject();
/*  36 */       document.put("url", url);
/*  37 */       document.put("text", text);
/*  38 */       document.put("html", html);
/*  39 */       document.put("docId", Integer.valueOf(docId));
/*  40 */       document.put("subDomain", subDomain);
/*  41 */       collection.insert(new DBObject[] { document });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void getAllData() {
/*  46 */     database = Db.connect();
/*  47 */     if (database != null) {
/*  48 */       DBCollection collection = database.getCollection("WebDocs");
/*  49 */       DBCursor cursor = collection.find();
/*  50 */       while (cursor.hasNext())
/*  51 */         System.out.println(cursor.next()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getData(String dataOption, BasicDBObject query) throws Exception {
/*  56 */     String result = "";
/*  57 */     DB database = Db.connect();
/*  58 */     if (database != null) {
/*  59 */       DBCollection collection = database.getCollection("WebDocs");
/*  60 */       DBCursor cursor = collection.find(query);
/*  61 */       while (cursor.hasNext()) {
/*  62 */         String c = cursor.next().toString();
/*  63 */         Object obj = JSON.parse(c);
/*  64 */         result = ((BasicBSONObject)obj).getString(dataOption);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  70 */     return result;
/*     */   }
/*     */   
/*     */   public static void textProcessing() {
/*  74 */     database = Db.connect();
/*  75 */     ArrayList<String> totalTokens = new ArrayList<String>();
/*  76 */     ArrayList<String> totalTokensSWR = new ArrayList<String>();
/*  77 */     ArrayList<String> totalThreeGramsSWR = new ArrayList<String>();
/*  78 */     ArrayList<String> totalSubDomains = new ArrayList<String>();
/*  79 */     int maxWords = 0;
/*  80 */     String maxUrl = "";
/*  81 */     long startTime = System.currentTimeMillis();
/*     */     
/*  83 */     if (database != null) {
/*  84 */       DBCollection collection = database.getCollection("WebDocs");
/*  85 */       DBCursor cursor = collection.find();
/*  86 */       cursor.addOption(16);
/*  87 */       cursor.skip(56968);
/*  88 */       while (cursor.hasNext()) {
/*  89 */         String c = cursor.next().toString();
/*  90 */         Object obj = JSON.parse(c);
/*  91 */         String url = ((BasicBSONObject)obj).getString("url");
/*  92 */         String text = ((BasicBSONObject)obj).getString("text");
/*  93 */         String subDomains = ((BasicBSONObject)obj).getString("subDomain");
/*  94 */         String docId = ((BasicBSONObject)obj).getString("docId");
/*  95 */         ArrayList<String> tokens = Tokenize.tokenizeFile(text);
/*  96 */         ArrayList<String> tokensSWR = Tokenize.tokenizeFileSWR(text);
/*  97 */         totalTokens.addAll(tokens);
/*  98 */         totalTokensSWR.addAll(tokensSWR);
/*  99 */         totalSubDomains.add(url);
/* 100 */         ArrayList<String> threeGrams = Tokenize.threeGramBuilder(tokens);
/* 101 */         ArrayList<String> threeGramsSWR = Tokenize.threeGramBuilder(tokensSWR);
/*     */         
/* 103 */         ArrayList<String> newThreeGrams = new ArrayList<String>();
/* 104 */         for (String str : threeGramsSWR) {
/* 105 */           if (threeGrams.contains(str)) {
/* 106 */             newThreeGrams.add(str);
/*     */           }
/*     */         } 
/* 109 */         totalThreeGramsSWR.addAll(newThreeGrams);
/* 110 */         DB tpDatabase = Db.connectToTextDb();
/* 111 */         if (tpDatabase != null) {
/* 112 */           DBCollection dataCollection = tpDatabase.getCollection("data");
/* 113 */           BasicDBObject document = new BasicDBObject();
/* 114 */           document.put("url", url);
/* 115 */           document.put("text", text);
/* 116 */           document.put("tokens", tokens.toString());
/* 117 */           document.put("tokensSWR", tokensSWR.toString());
/* 118 */           document.put("threeGramsSWR", newThreeGrams.toString());
/* 119 */           document.put("docId", docId);
/* 120 */           document.put("subDomain", subDomains);
/* 121 */           dataCollection.insert(new DBObject[] { document });
/*     */         } 
/* 123 */         int words = tokens.size();
/* 124 */         if (maxWords < words) {
/* 125 */           maxUrl = url;
/* 126 */           maxWords = words;
/*     */         } 
/*     */       } 
/* 129 */       HashMap<String, Integer> frequencyMap = Tokenize.computeWordFrequencies(totalTokensSWR);
/* 130 */       TreeMap<String, Integer> sortedMap = Tokenize.sort(frequencyMap);
/* 131 */       Tokenize.FileWriter("CommonWords.txt", sortedMap);
/* 132 */       System.out.println("" + maxWords + "==>" + maxUrl);
/* 133 */       HashMap<String, Integer> frequencyMapthreeGramsSWR = Tokenize.computeWordFrequencies(totalThreeGramsSWR);
/* 134 */       TreeMap<String, Integer> sortedMapthreeGramsSWR = Tokenize.sort(frequencyMapthreeGramsSWR);
/* 135 */       Tokenize.FileWriter("Common3Grams.txt", sortedMapthreeGramsSWR);
/* 136 */       long endTime = System.currentTimeMillis();
/* 137 */       long timeTaken = endTime - startTime;
/* 138 */       System.out.println(timeTaken);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/crawler/Storage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */