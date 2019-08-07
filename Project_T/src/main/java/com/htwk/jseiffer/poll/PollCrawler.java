package com.htwk.jseiffer.poll;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PollCrawler {

    private String urls[] = {   "https://www.wahlrecht.de/umfragen/forsa.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2013.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2008.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2007.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2006.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2005.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2004.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2003.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2002.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2001.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/2000.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/1999.htm",
                                "https://www.wahlrecht.de/umfragen/forsa/1998.htm",
                            };

    private HashMap<LocalDate,Poll> polls = new HashMap<>();

    public PollCrawler() {
        crawlWebsites();
    }

    private void crawlWebsites(){
        for (int i = 0; i < urls.length; i++){
            System.out.println(i);
            getWebsite(urls[i]);

        }
    }

    private void getWebsite(String url){
        Document document;
        List<String> parties;
        try {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect(url).get();
            //Get partyNames of the poll
            Element partyNames = document.select("thead").get(0);
            parties = getPartiesOfPoll(partyNames);
            //Get Poll
            Elements percentRows = document.select("tbody").get(1).select("tr");
            for(Element e : percentRows){
                try {
                    Poll poll;
                    poll = getPoll(e, parties);
                    polls.put(poll.getDate(), poll);
                }catch (Exception exc){
                    System.err.println("Poll was null");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String> getPartiesOfPoll(Element table){
        List<String> parties = new ArrayList<String>();

        Element partyRow = table.selectFirst("tr");
        Elements name = partyRow.select(".part");
        for (Element e: name) {
            parties.add(e.text());
        }

        return parties;
    }

    private Poll getPoll(Element row, List<String> parties) throws Exception{

        LocalDate date;
        DateTimeFormatter formatter;
        Poll poll = null;


        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        date = LocalDate.parse(row.selectFirst("td").text().replace("*",""), formatter);
        poll = new Poll(date);

        for(int i = 0; i < parties.size(); i++){

                float percentage = Float.valueOf(row.select("td").get(2 + i).text().replace("%", "").replace(",", ".").replace("–", "0").trim());
                poll.addOutcome(new Party(parties.get(i), percentage));
        }

        return poll;
    }

    public HashMap<LocalDate, Poll> getPolls() {
        return polls;
    }
}
