package com.htwk.jseiffer;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.htwk.jseiffer.finance.Stock;
import com.htwk.jseiffer.poll.Party;
import com.htwk.jseiffer.poll.Poll;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.Terror;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder {
    private Model model;
    private List<Terror> attacks;
    private List<Stock> stocks;

    public ModelBuilder(List<Terror> attacks, List<Stock> stocks) {
        this.attacks = attacks;
        this.stocks = stocks;
    }

    //Print model
    public void writeModel(){
        if(model == null){
            System.out.println("There is no model");
        }else{
            model.write(System.out, "N-TRIPLE");
        }
    }

    private void createTerrorModel(){
        String domain = "http://www.example-terror.org";
        String ns = "http://www.terror.org#";

        Property date = model.createProperty(ns,"happenedOn");
        Property city = model.createProperty(ns,"inCity");
        Property country = model.createProperty(ns,"inCountry");
        Property fatalities = model.createProperty(ns,"hasFatalities");
        Property injuries = model.createProperty(ns, "hasInjuries");

        for (Terror t : attacks) {
            Resource res = model.createResource(domain + "/terror/" + t.getId());

            res.addProperty(date,t.getDate().toString());
            res.addProperty(country,t.getCountry());
            res.addProperty(city,t.getCity());
            res.addProperty(fatalities, String.valueOf(t.getFatalities()));
            res.addProperty(injuries, String.valueOf(t.getInjured()));

//            addStatement(domain+"/terror/"+t.getId(),ns+"inCity",t.getCity());
//            addStatement(domain+"/terror/"+t.getId(),ns+"inCountry",t.getCountry());
//            addStatement(domain+"/terror/"+t.getId(),ns+"happenedOn",t.getDate().toString());
//            addStatement(domain+"/terror/"+t.getId(),ns+"hasFatalities",String.valueOf(t.getFatalities()));
//            addStatement(domain+"/terror/"+t.getId(),ns+"hasInjured",String.valueOf(t.getInjured()));
        }
    }

    private void createStockModel(){
        String domain = "http://www.example-stock.org";
        String ns = "http://www.stock.org#";

        Property date = model.createProperty(ns,"happenedOn");
        Property value = model.createProperty(ns,"value");

        for (Stock s : stocks) {
            Resource res = model.createResource(domain+"/stock/"+s.getId());

            res.addProperty(date, s.getDate().toString());
            res.addProperty(value, String.valueOf(s.getValue()));

            //addStatement(domain+"/stock/"+s.getId(),ns+"happenedOn",s.getDate().toString());
            //addStatement(domain+"/stock/"+s.getId(),ns+"value",String.valueOf(s.getValue()));
        }
    }

    private void createPollModel(HashMap<LocalDate, Poll> map){
        String domain = "http://www.example-poll.org";
        String ns = "http://www.poll.org#";

        Property date = model.createProperty(ns,"happenedOn");
        Property partyName = model.createProperty(ns,"partyName");
        Property partyPercent = model.createProperty(ns,"partyPercent");
        Property outcomes = model.createProperty(ns,"outcomes");


        for (Map.Entry<LocalDate, Poll> entry: map.entrySet()) {
            Resource poll = model.createResource(domain+"/poll/"+entry.getKey().toString());
            poll.addProperty(date, String.valueOf(entry.getKey()));

            for(Party p : entry.getValue().getOutcomes()){
                Resource party = model.createResource(domain+"/party/"+entry.getKey().toString()+p.getName());

                party.addProperty(partyName, p.getName());
                party.addProperty(partyPercent, String.valueOf(p.getPercent()));

                poll.addProperty(outcomes, party);

            }

        }

    }

    public void createModel(){
        PollCrawler polls = new PollCrawler();
        model = ModelFactory.createDefaultModel();
        this.createTerrorModel();
        this.createPollModel(polls.getPolls());
        this.createStockModel();


    }

    public void saveModel(){
        FileOutputStream fop = null;
        File file;

        try {

            file = new File("data\\model.txt");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            RDFDataMgr.write(fop, this.getModel(), Lang.RDFXML);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Model getModel() {
        return model;
    }
}
