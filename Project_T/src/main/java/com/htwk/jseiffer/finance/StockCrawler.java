package com.htwk.jseiffer.finance;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockCrawler {

    private String url = "https://de.finance.yahoo.com/quote/%5EGDAXI/history?period1=1534370400&period2=1565906400&interval=1d&filter=history&frequency=1d";


    public StockCrawler(LocalDate start, LocalDate end) {

        url = "https://de.finance.yahoo.com/quote/%5EGDAXI/history?period1="
                + start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
                + "&period2="
                + end.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
                + "&interval=1d&filter=history&frequency=1d";

        //this.downloadCSV();


    }

    private void downloadCSV() {
        Document doc;
        try {
            System.out.println(url);
            doc = Jsoup.connect(url).get();
            //select links whose href attribute ends with "t=60"
            //should only be one but select returns an Elements collection
            Elements links = doc.select("a[href]"); //LINK is the unique identifier of "t=60"
            int linksSize = links.size();
            if (linksSize > 0) {
                if (linksSize > 1) {
                    System.out.println("Warning: more than one link found.  Downloading first match.");
                }
                Element link = links.first();
                System.out.println(link.text());
                //this returns an absolute URL
                String linkUrl = link.attr("abs:href");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();
        List<CSVRecord> records = null;

        try {
            Reader in = new FileReader("data\\finance.csv");
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT);
            records = parser.getRecords();
            //remove Header
            records.remove(0);

            for (CSVRecord r : records){

                try {

                    List<String> recordList = new ArrayList<>();

                    for (int i = 0; i < r.size(); i++) {
                        if (r.get(i).trim().isEmpty()) {
                            recordList.add("0");
                        } else {
                            recordList.add(r.get(i));
                        }
                    }

                    Stock newStock = new Stock(LocalDate.parse(recordList.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")), Double.parseDouble(recordList.get(1)));

                    stocks.add(newStock);
                }catch (Exception e){
                    System.err.println("Formatting error");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }




}