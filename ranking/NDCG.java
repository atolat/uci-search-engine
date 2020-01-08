/*    */ package com.ir.ranking;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NDCG
/*    */ {
/*    */   public static double compute(List<String> ranked_items, List<String> correct_items, Collection<Integer> ignore_items) {
/* 14 */     if (ignore_items == null) {
/* 15 */       ignore_items = new HashSet<Integer>();
/*    */     }
/* 17 */     double dcg = 0.0D;
/* 18 */     double idcg = computeIDCG(correct_items.size());
/* 19 */     int left_out = 0;
/*    */     
/* 21 */     for (int i = 0; i < ranked_items.size(); i++) {
/* 22 */       String item_id = (String)ranked_items.get(i);
/* 23 */       if (ignore_items.contains(item_id)) {
/* 24 */         left_out++;
/*    */       
/*    */       }
/* 27 */       else if (correct_items.contains(item_id)) {
/*    */         
/* 29 */         int rank = i + 1 - left_out;
/* 30 */         dcg += Math.log(2.0D) / Math.log((rank + 1));
/*    */       } 
/*    */     } 
/*    */     
/* 34 */     return dcg / idcg;
/*    */   }
/*    */   
/*    */   static double computeIDCG(int n) {
/* 38 */     double idcg = 0.0D;
/* 39 */     for (int i = 0; i < n; i++)
/* 40 */       idcg += Math.log(2.0D) / Math.log((i + 2)); 
/* 41 */     return idcg;
/*    */   }
/*    */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/ranking/NDCG.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */