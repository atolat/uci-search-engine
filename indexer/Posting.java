/*    */ package com.ir.indexer;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Set;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Posting
/*    */ {
/*    */   Set<String> docIds;
/* 19 */   HashMap<String, Integer> postings = new HashMap();
/* 20 */   HashMap<String, ArrayList<Integer>> positions = new HashMap();
/*    */   JSONObject obj;
/*    */   
/*    */   public void addOccurrence(String documentId, Integer N) {
/* 24 */     Integer termFrequency = Integer.valueOf(1);
/* 25 */     if (this.postings.get(documentId) == null) {
/* 26 */       this.postings.put(documentId, termFrequency);
/*    */     } else {
/* 28 */       termFrequency = (Integer)this.postings.get(documentId);
/* 29 */       Integer integer1, integer2 = termFrequency = (integer1 = termFrequency).valueOf(termFrequency.intValue() + 1); integer1;
/* 30 */       this.postings.put(documentId, termFrequency);
/*    */     } 
/*    */   }
/*    */   public Posting(String token) {}
/*    */   
/*    */   int getTotalNumberOfOccurrences() {
/* 36 */     int collectionFrequency = 0;
/* 37 */     Set<String> urls = getDocumentIds();
/* 38 */     for (String url : urls) {
/* 39 */       if (this.postings.get(url) != null) {
/* 40 */         collectionFrequency++;
/*    */       }
/*    */     } 
/* 43 */     return collectionFrequency;
/*    */   }
/*    */ 
/*    */   
/* 47 */   Set<String> getDocumentIds() { return this.postings.keySet(); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   HashMap<String, Integer> getPostings() { return this.postings; }
/*    */ 
/*    */   
/*    */   int getNumberOfOccurrencesInDocument(String documentId) {
/* 55 */     int termFrequency = 0;
/* 56 */     if (this.postings.get(documentId) != null) {
/* 57 */       termFrequency = ((Integer)this.postings.get(documentId)).intValue();
/*    */     }
/* 59 */     return termFrequency;
/*    */   }
/*    */   
/*    */   public void addPosition(String url, Integer pos) {
/* 63 */     ArrayList<Integer> position = new ArrayList<Integer>();
/* 64 */     if (this.positions.get(url) == null) {
/* 65 */       position.add(pos);
/* 66 */       this.positions.put(url, position);
/*    */     } else {
/* 68 */       position = (ArrayList)this.positions.get(url);
/* 69 */       position.add(pos);
/* 70 */       this.positions.put(url, position);
/*    */     } 
/* 72 */     this.positions.put(url, position);
/*    */   }
/*    */ 
/*    */   
/* 76 */   public HashMap<String, ArrayList<Integer>> getPositions() { return this.positions; }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public double tfCalc(double tf2) { return tf2; }
/*    */ 
/*    */   
/*    */   public double idfCalc(int i) {
/* 84 */     double n = 56186.0D;
/* 85 */     return Math.log(n / i);
/*    */   }
/*    */   
/* 88 */   public double tfIdf(double tf2, int i) { return tfCalc(tf2) * idfCalc(i); }
/*    */ 
/*    */ 
/*    */   
/* 92 */   public double calculateTf(Integer term, Integer N) { return term.intValue() / N.intValue(); }
/*    */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/indexer/Posting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */