package com.htwk.jseiffer;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.htwk.jseiffer.finance.Stock;
import com.htwk.jseiffer.poll.Party;
import com.htwk.jseiffer.poll.Poll;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.Terror;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder {
    private Model model;
    private List<Terror> attacks;
    private List<Stock> stocks;

    private String ns = "https://github.com/juuls28/semanticWEB#";

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

        Property date = model.createProperty(ns,"happenedOn");
        Property city = model.createProperty(ns,"inCity");
        Property country = model.createProperty(ns,"inCountry");
        Property fatalities = model.createProperty(ns,"hasFatalities");
        Property injuries = model.createProperty(ns, "hasInjuries");

        for (Terror t : attacks) {
            Resource res = model.createResource(ns + t.getId());

            res.addProperty(date,t.getDate().toString());
            res.addProperty(country,t.getCountry());
            res.addProperty(city,t.getCity());
            res.addProperty(fatalities, String.valueOf(t.getFatalities()));
            res.addProperty(injuries, String.valueOf(t.getInjured()));

        }
    }

    private void createStockModel(){

        Property date = model.createProperty(ns,"happenedOn");
        Property value = model.createProperty(ns,"value");

        for (Stock s : stocks) {
            Resource res = model.createResource(ns + s.getId());

            res.addProperty(date, s.getDate().toString());
            res.addProperty(value, String.valueOf(s.getValue()));

        }
    }

    private void createPollModel(HashMap<LocalDate, Poll> map){

        Property date = model.createProperty(ns,"happenedOn");
        Property partyName = model.createProperty(ns,"partyName");
        Property partyPercent = model.createProperty(ns,"partyPercent");
        Property outcomes = model.createProperty(ns,"outcomes");


        for (Map.Entry<LocalDate, Poll> entry: map.entrySet()) {
            Resource poll = model.createResource(ns + entry.getKey().toString());
            poll.addProperty(date, String.valueOf(entry.getKey()));

            for(Party p : entry.getValue().getOutcomes()){
                Resource party = model.createResource(ns + entry.getKey().toString()+p.getName());

                party.addProperty(partyName, p.getName());
                party.addProperty(partyPercent, String.valueOf(p.getPercent()));

                poll.addProperty(outcomes, party);

            }

        }

    }

    public void createModel(){
        PollCrawler polls = new PollCrawler();
        model = ModelFactory.createDefaultModel();

        model.setNsPrefix("ns",ns);
        this.createTerrorModel();
        this.createPollModel(polls.getPolls());
        this.createStockModel();

        //this.createOWL(polls.getPolls());


    }

    public void saveModel(){
        FileOutputStream fop = null;
        File file;

        try {

            file = new File("data\\model.rdf");
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

//    private void createOWL(HashMap<LocalDate, Poll> map){
//        String se = "https://github.com/juuls28/semanticWEB#";
//        String xsd = "http://www.w3.org/2001/XMLSchema#";
//        String owl = "http://www.w3.org/2002/07/owl#";
//        String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//        String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
//
//        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
//
//        model.setNsPrefix("se", se);
//        model.setNsPrefix("xsd", xsd);
//        model.setNsPrefix("owl", owl);
//        model.setNsPrefix("rdf", rdf);
//        model.setNsPrefix("rdfs", rdfs);
//
//        OntClass terror = model.createClass(se + "terror");
//        OntClass poll = model.createClass(se + "poll");
//        OntClass stock = model.createClass(se + "stock");
//        OntClass party = model.createClass(se + "party");
//
//        DatatypeProperty date = model.createDatatypeProperty(se + "date");
//        date.addDomain(terror);
//        date.addDomain(poll);
//        date.addDomain(stock);
//        date.addRange(XSD.date);
//
//        DatatypeProperty city = model.createDatatypeProperty(se + "city");
//        city.addDomain(terror);
//        city.addRange(XSD.xstring);
//
//        DatatypeProperty country = model.createDatatypeProperty(se + "city");
//        country.addDomain(terror);
//        country.addRange(XSD.xstring);
//
//        DatatypeProperty fatalities = model.createDatatypeProperty(se + "city");
//        fatalities.addDomain(terror);
//        fatalities.addRange(XSD.xint);
//
//        DatatypeProperty injuries = model.createDatatypeProperty(se + "city");
//        injuries.addDomain(terror);
//        injuries.addRange(XSD.xint);
//
//        DatatypeProperty outcomes = model.createDatatypeProperty(se + "outcomes");
//        outcomes.addDomain(poll);
//        outcomes.addRange(party);
//
//        DatatypeProperty partyName = model.createDatatypeProperty(se + "partyName");
//        partyName.addDomain(party);
//        partyName.addRange(XSD.xstring);
//
//        DatatypeProperty partyPercent = model.createDatatypeProperty(se + "partyPercent");
//        partyPercent.addDomain(party);
//        partyPercent.addRange(XSD.xfloat);
//
//        DatatypeProperty value = model.createDatatypeProperty(se + "value");
//        value.addDomain(stock);
//        value.addRange(XSD.xfloat);
//
//        System.out.println("Starting with polls");
//        for (Map.Entry<LocalDate, Poll> entry: map.entrySet()) {
//            Individual pollIndi = model.createIndividual(se + entry.getKey().toString(),poll);
//            poll.addProperty(date, entry.getKey().toString());
//
//            for(Party p : entry.getValue().getOutcomes()){
//                Individual partyIndi = model.createIndividual(se + entry.getKey().toString()+p.getName(),party);
//
//                party.addProperty(partyName, p.getName());
//                party.addProperty(partyPercent, String.valueOf(p.getPercent()));
//
//                poll.addProperty(outcomes, party);
//
//            }
//
//        }
//        System.out.println("Starting with stocks");
//        for (Stock s : stocks) {
//            Individual stockIndi = model.createIndividual( se + s.getId(), stock);
//
//            stockIndi.addProperty(date, s.getDate().toString());
//            stockIndi.addProperty(value, String.valueOf(s.getValue()));
//        }
//        System.out.println("Starting with terror");
//        for (Terror t : attacks) {
//            Individual terrorIndi = model.createIndividual(se + t.getId(),terror);
//
//            terrorIndi.addProperty(date,t.getDate().toString());
//            terrorIndi.addProperty(country,t.getCountry());
//            terrorIndi.addProperty(city,t.getCity());
//            terrorIndi.addProperty(fatalities, String.valueOf(t.getFatalities()));
//            terrorIndi.addProperty(injuries, String.valueOf(t.getInjured()));
//
//        }
//
//        System.out.println("Model built");
//
//        try {
//            model.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data\\ontology.rdf"), "UTF8")));
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//    }
}
