package com.htwk.jseiffer.terror;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TerrorCrawler {
    private String url;
    public TerrorCrawler(int startYear, int endYear) {

        this.url ="https://www.start.umd.edu/gtd/search/ResultsCSV.aspx?csv=1&casualties_type=b&casualties_max=&start_year="
                + startYear
                + "&start_month=1&start_day=1&end_year="
                + endYear
                + "&end_month=1&end_day=31&dtp2=all&country=75";
        this.downloadCSV();

    }

    private void downloadCSV(){
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
            FileOutputStream fileOS = new FileOutputStream("data\\terror.csv");
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Terror> getAttacks() {
        List<Terror> attacks = new ArrayList<Terror>();
        List<CSVRecord> records = null;

        try {
            Reader in = new FileReader("data\\terror.csv");
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

                    Terror terr = new Terror(recordList.get(0), LocalDate.parse(recordList.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")), recordList.get(2), recordList.get(3), Integer.valueOf(recordList.get(10)), Integer.valueOf(recordList.get(11)));

                    attacks.add(terr);
                }catch (Exception e){
                    System.err.println("Formatting error");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return attacks;
    }


}
