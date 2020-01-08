/*     */ package com.ir.textprocessor;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class Tokenize {
/*     */   public static void FileWriter(String subDomainsFile, TreeMap<String, Integer> sortedSubDomains) {
/*     */     try {
/*  20 */       PrintWriter writer = new PrintWriter(subDomainsFile, "UTF-8");
/*  21 */       writer.println(sortedSubDomains);
/*  22 */       writer.close();
/*  23 */     } catch (FileNotFoundException e) {
/*  24 */       e.printStackTrace();
/*  25 */     } catch (UnsupportedEncodingException e) {
/*  26 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public String textFile;
/*     */   
/*     */   public static ArrayList<String> tokenizeFileSWR(String data) throws Exception {
/*  32 */     ArrayList<String> tokenList = new ArrayList<String>();
/*  33 */     ArrayList<String> stopwords = stopwordsList();
/*  34 */     Scanner scanner = new Scanner(data);
/*  35 */     while (scanner.hasNextLine()) {
/*  36 */       String s = scanner.nextLine();
/*  37 */       Pattern checkRegex = Pattern.compile("[A-Za-z0-9']{1,100}");
/*  38 */       Matcher regexMatcher = checkRegex.matcher(s);
/*  39 */       while (regexMatcher.find()) {
/*  40 */         if (regexMatcher.group().length() != 0) {
/*  41 */           tokenList.add(regexMatcher.group().trim().toLowerCase());
/*     */         }
/*  43 */         tokenList.removeAll(stopwords);
/*     */       } 
/*     */     } 
/*  46 */     scanner.close();
/*  47 */     return tokenList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ArrayList<String> tokenizeFile(String data) throws Exception {
/*  52 */     ArrayList<String> tokenList = new ArrayList<String>();
/*  53 */     Scanner scanner = new Scanner(data);
/*  54 */     while (scanner.hasNextLine()) {
/*  55 */       String s = scanner.nextLine();
/*  56 */       Pattern checkRegex = Pattern.compile("[A-Za-z0-9']{1,100}");
/*  57 */       Matcher regexMatcher = checkRegex.matcher(s);
/*  58 */       while (regexMatcher.find()) {
/*  59 */         if (regexMatcher.group().length() != 0) {
/*  60 */           tokenList.add(regexMatcher.group().trim().toLowerCase());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     scanner.close();
/*  66 */     return tokenList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashMap<String, Integer> computeWordFrequencies(ArrayList<String> resultList) {
/*  71 */     HashMap<String, Integer> m = new HashMap<String, Integer>();
/*  72 */     for (String a : resultList) {
/*  73 */       Integer freq = (Integer)m.get(a);
/*  74 */       m.put(a, Integer.valueOf((freq == null) ? 1 : (freq.intValue() + 1)));
/*     */     } 
/*  76 */     return m;
/*     */   }
/*     */ 
/*     */   
/*     */   public static TreeMap<String, Integer> sort(HashMap<String, Integer> frequencyMap) {
/*  81 */     ValueComparator vc = new ValueComparator(frequencyMap);
/*  82 */     TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
/*  83 */     sortedMap.putAll(frequencyMap);
/*  84 */     return sortedMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ArrayList<String> stopwordsList() throws FileNotFoundException, IOException {
/*  89 */     list = new FileInputStream("C:/Users/Vacha Shah/Documents/ir/stopwords.txt");
/*  90 */     BufferedReader br1 = new BufferedReader(new InputStreamReader(list));
/*  91 */     int k = 0;
/*  92 */     String s1 = null;
/*  93 */     ArrayList<String> stopwords = new ArrayList<String>();
/*  94 */     while ((s1 = br1.readLine()) != null) {
/*  95 */       stopwords.add(s1);
/*  96 */       k++;
/*     */     } 
/*  98 */     return stopwords;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<String> threeGramBuilder(ArrayList<String> resultList) {
/* 104 */     ArrayList<String> threeGrams = new ArrayList<String>();
/* 105 */     for (int i = 0; i < resultList.size() - 3 + 1; i++) {
/* 106 */       StringBuilder sb = new StringBuilder();
/* 107 */       for (int j = i; j < i + 3; j++)
/* 108 */         sb.append(((j > i) ? " " : "") + (String)resultList.get(j)); 
/* 109 */       threeGrams.add(sb.toString());
/*     */     } 
/* 111 */     return threeGrams;
/*     */   }
/*     */   
/*     */   public static TreeMap<String, Integer> subDomains(ArrayList<String> urls) throws Exception {
/* 115 */     HashMap<String, Integer> subDomainsResult = new HashMap<String, Integer>();
/* 116 */     String subDomainsFile = "subDomains.txt";
/* 117 */     ArrayList<String> domains = new ArrayList<String>();
/* 118 */     System.out.println(domains);
/*     */     try {
/* 120 */       for (String url : urls) {
/* 121 */         String domain = url;
/* 122 */         domain = domain.replace("http://www.", "");
/* 123 */         domain = domain.replace(domain.substring(domain.indexOf('/') + 1), "");
/* 124 */         domains.add(domain);
/*     */       } 
/* 126 */       for (String domain : domains) {
/* 127 */         int value = Collections.frequency(domains, domain);
/* 128 */         if (domain != "") {
/* 129 */           subDomainsResult.put(domain, Integer.valueOf(value));
/*     */         }
/*     */       } 
/* 132 */     } catch (Exception e) {
/* 133 */       e.printStackTrace();
/*     */     } 
/* 135 */     System.out.println(subDomainsResult);
/* 136 */     TreeMap<String, Integer> sortedSubDomains = new TreeMap<String, Integer>(subDomainsResult);
/* 137 */     sortedSubDomains.putAll(subDomainsResult);
/* 138 */     System.out.println(subDomainsResult);
/* 139 */     FileWriter(subDomainsFile, sortedSubDomains);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     return sortedSubDomains;
/*     */   }
/*     */   
/*     */   static class ValueComparator extends Object implements Comparator<String> {
/*     */     Map<String, Integer> map;
/*     */     
/* 151 */     public ValueComparator(Map<String, Integer> base) { this.map = base; }
/*     */     
/*     */     public int compare(String a, String b) {
/* 154 */       if (((Integer)this.map.get(a)).intValue() >= ((Integer)this.map.get(b)).intValue()) {
/* 155 */         return -1;
/*     */       }
/* 157 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/textprocessor/Tokenize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */