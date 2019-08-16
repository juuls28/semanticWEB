package com.htwk.jseiffer;

import com.htwk.jseiffer.finance.Stock;
import com.htwk.jseiffer.finance.StockCrawler;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.TerrorCrawler;

import java.time.LocalDate;
import java.util.List;


public class App 
{
    public static void main( String[] args )
    {
        LocalDate start = LocalDate.parse("2015-07-28");
        LocalDate end = LocalDate.parse("2018-07-28");
        StockCrawler stockCrawler = new StockCrawler(start, end);
        TerrorCrawler terror = new TerrorCrawler(2008,2017);
        ModelBuilder model = new ModelBuilder(terror.getAttacks(), stockCrawler.getStocks());
        model.createModel();
        model.writeModel();
    }
}
