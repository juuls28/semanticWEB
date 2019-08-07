package com.htwk.jseiffer;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class TerrorCrawler {
    private String url;
    private String path;
    private int startYear;
    private int endYear;

    public TerrorCrawler(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        this. url ="https://www.start.umd.edu/gtd/search/ResultsCSV.aspx?csv=1&casualties_type=b&casualties_max=&start_year="
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
}
