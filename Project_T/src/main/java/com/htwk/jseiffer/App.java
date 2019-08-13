package com.htwk.jseiffer;

import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.TerrorCrawler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        //PollCrawler poll = new PollCrawler();
        TerrorCrawler terror = new TerrorCrawler(2008,2017);
        ModelBuilder model = new ModelBuilder(terror.getAttacks());
        model.createModel();
        model.writeModel();
    }
}
