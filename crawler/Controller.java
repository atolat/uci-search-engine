/*    */ package com.ir.crawler;
/*    */ 
/*    */ import edu.uci.ics.crawler4j.crawler.CrawlConfig;
/*    */ import edu.uci.ics.crawler4j.crawler.CrawlController;
/*    */ import edu.uci.ics.crawler4j.fetcher.PageFetcher;
/*    */ import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
/*    */ import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
/*    */ 
/*    */ public class Controller {
/*    */   public static void startCrawler() {
/* 11 */     crawlStorageFolder = "data/crawl/root";
/* 12 */     int numberOfCrawlers = 7;
/* 13 */     CrawlConfig config = new CrawlConfig();
/* 14 */     config.setCrawlStorageFolder(crawlStorageFolder);
/* 15 */     config.setPolitenessDelay(300);
/* 16 */     config.setMaxDepthOfCrawling(20);
/*    */     
/* 18 */     config.setIncludeBinaryContentInCrawling(false);
/* 19 */     config.setResumableCrawling(true);
/* 20 */     System.out.println("User Agent " + config.getUserAgentString());
/*    */ 
/*    */ 
/*    */     
/* 24 */     PageFetcher pageFetcher = new PageFetcher(config);
/* 25 */     RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
/* 26 */     RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
/* 27 */     CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     controller.addSeed("http://www.ics.uci.edu/~lopes/");
/* 34 */     controller.addSeed("http://www.ics.uci.edu/~welling/");
/* 35 */     controller.addSeed("http://www.ics.uci.edu/");
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     long startTime = System.currentTimeMillis();
/* 41 */     controller.start(Crawler.class, numberOfCrawlers);
/* 42 */     long endTime = System.currentTimeMillis();
/* 43 */     long timeTaken = endTime - startTime;
/* 44 */     System.out.println("Time - " + timeTaken);
/*    */   }
/*    */ }


/* Location:              /Users/arpan/Documents/SearchEngine_final/build/classes/!/com/ir/crawler/Controller.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */