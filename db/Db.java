/*    */ package com.ir.db;
/*    */ 
/*    */ import com.mongodb.DB;
/*    */ import com.mongodb.MongoClient;
/*    */ 
/*    */ public class Db
/*    */ {
/*  8 */   static MongoClient crawlerClient = null;
/*  9 */   static MongoClient textProcClient = null;
/* 10 */   static MongoClient indexClient = null;
/* 11 */   static DB db = null;
/* 12 */   static DB textDb = null;
/* 13 */   static DB indexDb = null;
/*    */   public static DB connect() {
/* 15 */     if (crawlerClient != null) {
/*    */       try {
/* 17 */         return crawlerClient.getDB("Crawler_final");
/* 18 */       } catch (Exception e) {
/* 19 */         e.printStackTrace();
/* 20 */         System.exit(1);
/* 21 */         return null;
/*    */       } 
/*    */     }
/*    */     
/*    */     try {
/* 26 */       crawlerClient = new MongoClient("localhost", 27017);
/* 27 */       db = crawlerClient.getDB("Crawler_final");
/* 28 */       System.out.println("Connect to database successfully");
/* 29 */     } catch (NullPointerException e) {
/* 30 */       e.printStackTrace();
/* 31 */       System.exit(1);
/* 32 */     } catch (Exception e) {
/* 33 */       System.err.println(e.getClass().getName() + ": " + e.getMessage());
/* 34 */       return null;
/*    */     } 
/*    */     
/* 37 */     return db;
/*    */   }
/*    */   public static DB connectToTextDb() {
/* 40 */     if (textProcClient != null) {
/*    */       try {
/* 42 */         return textProcClient.getDB("TextProcessor");
/* 43 */       } catch (Exception e) {
/* 44 */         e.printStackTrace();
/* 45 */         System.exit(1);
/* 46 */         return null;
/*    */       } 
/*    */     }
/*    */     
/*    */     try {
/* 51 */       textProcClient = new MongoClient("localhost", 27017);
/* 52 */       textDb = textProcClient.getDB("TextProcessor");
/* 53 */       System.out.println("Connect to text database successfully");
/* 54 */     } catch (NullPointerException e) {
/* 55 */       e.printStackTrace();
/* 56 */     } catch (Exception e) {
/* 57 */       System.err.println(e.getClass().getName() + ": " + e.getMessage());
/* 58 */       return null;
/*    */     } 
/*    */     
/* 61 */     return textDb;
/*    */   }
/*    */   public static DB connectToIndexerDb() {
/* 64 */     if (indexClient != null) {
/*    */       try {
/* 66 */         return indexClient.getDB("InvertedIndex");
/* 67 */       } catch (Exception e) {
/* 68 */         e.printStackTrace();
/* 69 */         System.exit(1);
/* 70 */         return null;
/*    */       } 
/*    */     }
/*    */     
/*    */     try {
/* 75 */       indexClient = new MongoClient("localhost", 27017);
/* 76 */       indexDb = indexClient.getDB("InvertedIndex");
/* 77 */       System.out.println("Connect to index database successfully");
/* 78 */     } catch (NullPointerException e) {
/* 79 */       e.printStackTrace();
/* 80 */     } catch (Exception e) {
/* 81 */       System.err.println(e.getClass().getName() + ": " + e.getMessage());
/* 82 */       return null;
/*    */     } 
/*    */     
/* 85 */     return indexDb;
/*    */   }
/*    */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/db/Db.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */