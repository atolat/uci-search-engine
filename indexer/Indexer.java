/*     */ package com.ir.indexer;
/*     */ 
/*     */ import com.ir.db.Db;
/*     */ import com.mongodb.BasicDBObject;
/*     */ import com.mongodb.DB;
/*     */ import com.mongodb.DBCollection;
/*     */ import com.mongodb.DBCursor;
/*     */ import com.mongodb.DBObject;
/*     */ import com.mongodb.util.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.bson.BasicBSONObject;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONObject;
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
/*     */ public class Indexer
/*     */ {
/*     */   static HashMap<String, Posting> invertedIndex;
/*     */   static HashMap<String, Integer> urlWordCount;
/*     */   
/*     */   public Indexer() {
/*  34 */     invertedIndex = new HashMap();
/*  35 */     urlWordCount = new HashMap();
/*     */   }
/*     */   
/*     */   public static int buildIndex() throws Exception {
/*  39 */     db = Db.connectToTextDb();
/*  40 */     if (db != null) {
/*  41 */       DBCollection collection = db.getCollection("data");
/*  42 */       DBCursor cursor = collection.find();
/*  43 */       cursor.addOption(16);
/*  44 */       while (cursor.hasNext()) {
/*  45 */         String c = cursor.next().toString();
/*  46 */         Object obj = JSON.parse(c);
/*  47 */         String url = ((BasicBSONObject)obj).getString("url");
/*  48 */         String tokens = ((BasicBSONObject)obj).getString("tokens");
/*  49 */         String docId = ((BasicBSONObject)obj).getString("docId");
/*  50 */         indexing(url, tokens, docId);
/*     */       } 
/*     */       
/*     */       try {
/*  54 */         storeInDb();
/*  55 */       } catch (Exception e) {
/*  56 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*  59 */     return 0;
/*     */   }
/*     */   
/*     */   private static void storeInDb() {
/*  63 */     db = Db.connectToIndexerDb();
/*  64 */     System.out.println(invertedIndex.size());
/*  65 */     if (db != null) {
/*  66 */       DBCollection collection = db.getCollection("data");
/*  67 */       int count = 0;
/*  68 */       for (Map.Entry<String, Posting> entry : invertedIndex.entrySet()) {
/*  69 */         ArrayList<DBObject> urlDetailsList = new ArrayList<DBObject>();
/*  70 */         HashMap<String, Double> tfIdfMap = new HashMap<String, Double>();
/*  71 */         String token = (String)entry.getKey();
/*  72 */         Posting posting = (Posting)entry.getValue();
/*  73 */         for (Map.Entry<String, Integer> tfMap : posting.getPostings().entrySet()) {
/*  74 */           String url = (String)tfMap.getKey();
/*  75 */           Integer term = (Integer)tfMap.getValue();
/*  76 */           double tf = posting.calculateTf(term, (Integer)urlWordCount.get(url));
/*  77 */           double tfIdf = posting.tfIdf(tf, posting.getTotalNumberOfOccurrences());
/*  78 */           tfIdfMap.put(url, Double.valueOf(tfIdf));
/*     */         } 
/*  80 */         TreeMap<String, Double> tfIdf = sort(tfIdfMap);
/*  81 */         for (Map.Entry<String, Double> resMap : tfIdf.entrySet()) {
/*  82 */           String url = (String)resMap.getKey();
/*  83 */           double tfIdfValue = ((Double)resMap.getValue()).doubleValue();
/*  84 */           BasicDBObject urlDetails = (new BasicDBObject()).append("url", url);
/*  85 */           urlDetails.append("positions", posting.getPositions().get(url));
/*  86 */           urlDetails.append("tfIdf", Double.valueOf(tfIdfValue));
/*  87 */           urlDetailsList.add(urlDetails);
/*     */         } 
/*  89 */         BasicDBObject invertedIndexDetails = (new BasicDBObject("$set", (new BasicDBObject()).append("token", token))).append("$push", new BasicDBObject("details", new BasicDBObject("$each", urlDetailsList)));
/*  90 */         collection.update(new BasicDBObject("token", token), invertedIndexDetails, true, false);
/*  91 */         System.out.println("Inserted in db - " + count);
/*  92 */         count++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static TreeMap<String, Double> sort(HashMap<String, Double> tfIdfMap) {
/*  98 */     ValueComparator vc = new ValueComparator(tfIdfMap, null);
/*  99 */     TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(vc);
/* 100 */     sortedMap.putAll(tfIdfMap);
/* 101 */     return sortedMap;
/*     */   }
/*     */   
/*     */   static class ValueComparator
/*     */     extends Object
/*     */     implements Comparator<String> {
/*     */     Map<String, Double> map;
/*     */     
/* 109 */     private ValueComparator(HashMap<String, Double> base) { this.map = base; }
/*     */ 
/*     */     
/*     */     public int compare(String a, String b) {
/* 113 */       if (((Double)this.map.get(a)).doubleValue() >= ((Double)this.map.get(b)).doubleValue()) {
/* 114 */         return -1;
/*     */       }
/* 116 */       return 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void indexing(String url, String tokens, String docId) {
/* 122 */     String[] arr = tokens.split(", ");
/* 123 */     Integer position = Integer.valueOf(0);
/* 124 */     int N = arr.length;
/* 125 */     urlWordCount.put(url, Integer.valueOf(N));
/* 126 */     for (String token : arr) {
/* 127 */       Posting posting = (Posting)invertedIndex.get(token);
/* 128 */       if (posting == null) {
/* 129 */         posting = new Posting(token);
/*     */       }
/* 131 */       posting.addPosition(url, position);
/* 132 */       posting.addOccurrence(url, Integer.valueOf(N));
/* 133 */       Integer integer1, integer2 = position = (integer1 = position).valueOf(position.intValue() + 1); integer1;
/* 134 */       invertedIndex.put(token, posting);
/*     */     } 
/*     */   }
/*     */   
/*     */   public HashMap<String, Double> getTfIdfData(String dataOption, BasicDBObject query) throws Exception {
/* 139 */     DB database = Db.connectToIndexerDb();
/* 140 */     HashMap<String, Double> urlMap = new HashMap<String, Double>();
/* 141 */     if (database != null) {
/* 142 */       DBCollection collection = database.getCollection("data");
/* 143 */       DBCursor cursor = collection.find(query);
/* 144 */       while (cursor.hasNext()) {
/* 145 */         String c = cursor.next().toString();
/* 146 */         Object obj = JSON.parse(c);
/* 147 */         JSONObject json = new JSONObject((BasicBSONObject)obj);
/* 148 */         JSONArray jsonArray = json.getJSONArray("details");
/* 149 */         for (int i = 0; i < jsonArray.length(); i++) {
/* 150 */           JSONObject jsonobject = jsonArray.getJSONObject(i);
/* 151 */           String url = jsonobject.getString("url");
/* 152 */           double tfIdf = ((Double)jsonobject.get(dataOption)).doubleValue();
/* 153 */           urlMap.put(url, Double.valueOf(tfIdf));
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     return urlMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, Double> getTfIdfDataByUrl(String dataOption, BasicDBObject query, String checkUrl) throws Exception {
/* 161 */     DB database = Db.connectToIndexerDb();
/* 162 */     HashMap<String, Double> urlMap = new HashMap<String, Double>();
/* 163 */     if (database != null) {
/* 164 */       DBCollection collection = database.getCollection("data");
/* 165 */       DBCursor cursor = collection.find(query);
/* 166 */       while (cursor.hasNext()) {
/* 167 */         String c = cursor.next().toString();
/* 168 */         Object obj = JSON.parse(c);
/* 169 */         JSONObject json = new JSONObject((BasicBSONObject)obj);
/* 170 */         JSONArray jsonArray = json.getJSONArray("details");
/* 171 */         for (int i = 0; i < jsonArray.length(); i++) {
/* 172 */           JSONObject jsonobject = jsonArray.getJSONObject(i);
/* 173 */           String url = jsonobject.getString("url");
/* 174 */           if (url.equalsIgnoreCase(checkUrl)) {
/* 175 */             double tfIdf = ((Double)jsonobject.get(dataOption)).doubleValue();
/* 176 */             urlMap.put(url, Double.valueOf(tfIdf));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 181 */     return urlMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, ArrayList<Integer>> getPositionData(String dataOption, BasicDBObject query) throws Exception {
/* 185 */     DB database = Db.connectToIndexerDb();
/* 186 */     HashMap<String, ArrayList<Integer>> urlMap = new HashMap<String, ArrayList<Integer>>();
/* 187 */     if (database != null) {
/* 188 */       DBCollection collection = database.getCollection("data");
/* 189 */       DBCursor cursor = collection.find(query);
/* 190 */       while (cursor.hasNext()) {
/* 191 */         String c = cursor.next().toString();
/* 192 */         Object obj = JSON.parse(c);
/* 193 */         JSONObject json = new JSONObject((BasicBSONObject)obj);
/* 194 */         JSONArray jsonArray = json.getJSONArray("details");
/* 195 */         for (int i = 0; i < jsonArray.length(); i++) {
/* 196 */           JSONObject jsonobject = jsonArray.getJSONObject(i);
/* 197 */           String url = jsonobject.getString("url");
/* 198 */           JSONArray arrayResp = new JSONArray(jsonobject.get(dataOption).toString());
/* 199 */           ArrayList<Integer> myList = new ArrayList<Integer>();
/* 200 */           for (int j = 0; j < arrayResp.length(); j++) {
/* 201 */             myList.add(Integer.valueOf(arrayResp.getInt(j)));
/*     */           }
/* 203 */           urlMap.put(url, myList);
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     return urlMap;
/*     */   }
/*     */   
/*     */   static void printIndex() {
/* 211 */     System.out.println("======================================================================");
/* 212 */     System.out.println("*********************Index LIST*************************************");
/* 213 */     System.out.println("======================================================================");
/* 214 */     for (Map.Entry<String, Posting> entry : invertedIndex.entrySet()) {
/* 215 */       String key = (String)entry.getKey();
/* 216 */       Posting value = (Posting)entry.getValue();
/* 217 */       System.out.println("(String, Index)::\t(" + key + ", " + value.getPostings() + ")");
/* 218 */       System.out.println("(String, Index)::\t(" + key + ", " + value.getPositions() + ")");
/*     */     } 
/* 220 */     System.out.println("\n\n");
/*     */   }
/*     */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/indexer/Indexer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */