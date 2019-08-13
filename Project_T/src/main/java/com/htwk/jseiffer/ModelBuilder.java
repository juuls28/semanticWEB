package com.htwk.jseiffer;

import com.htwk.jseiffer.poll.Party;
import com.htwk.jseiffer.poll.Poll;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.Terror;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder {
    private Model model;
    private List<Terror> attacks;

    public ModelBuilder(List<Terror> attacks) {
        this.attacks = attacks;
    }

    //Print model in turtle format
    public void writeModel(){
        if(model == null){
            System.out.println("There is no model");
        }else{
            model.write(System.out, "TTL");
        }
    }

    //Adds a statement to the model
    public void addStatement(String s, String p, String o){
        Resource subject = model.createResource(s);
        Property predicate = model.createProperty(p);
        RDFNode object = model.createResource(o);
        Statement stmt = model.createStatement(subject, predicate, object);
        model.add(stmt);
    }

    private void createTerrorModel(){
        String ns = "http://www.example.org#";
        String nsRDFS = "http://www.w3.org/2000/01/rdf-schema#";

        for (Terror t : attacks) {
            addStatement(ns+"terror/"+t.getId(),ns+"inCity",t.getCity());
            addStatement(ns+"terror/"+t.getId(),ns+"inCountry",t.getCountry());
            addStatement(ns+"terror/"+t.getId(),ns+"happenedOn",t.getDate().toString());
            addStatement(ns+"terror/"+t.getId(),ns+"hasFatalities",String.valueOf(t.getFatalities()));
            addStatement(ns+"terror/"+t.getId(),ns+"hasInjured",String.valueOf(t.getInjured()));
        }
    }

    private void createPollModel(HashMap<LocalDate, Poll> map){
        String ns = "http://www.example.org#";
        String nsRDFS = "http://www.w3.org/2000/01/rdf-schema#";

        addStatement(ns+"Poll", ns+"dateOf", "xsd:date");
        for (Map.Entry<LocalDate, Poll> entry: map.entrySet()) {
            //addStatement(entry.getKey().toString(), );
        }
        addStatement(ns+"Poll", ns+"dateOf", "xsd:date");
        addStatement(ns+"Party", nsRDFS+"subClassOf", ns+"Poll");
        addStatement(ns+"Party", ns+"percentageOfParty", "xsd:float");
        addStatement(ns+"Party", ns+"name", "xsd:string");



    }

    public void createModel(){
        model = ModelFactory.createDefaultModel();
        this.createTerrorModel();


    }



}
