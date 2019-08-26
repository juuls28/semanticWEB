package com.htwk.jseiffer;

import com.htwk.jseiffer.finance.Stock;
import com.htwk.jseiffer.finance.StockCrawler;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.TerrorCrawler;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class App 
{
    public static void main( String[] args )
    {
        App app = new App();

        String startDate = "2016-02-02";
        String endDate = "2017-02-02";

        String attackDate = "";

        String firstPollDate;
        String secPollDate;

        String firstStockDate;
        String secStockDate;

        HashMap<Integer, String> attacks = new HashMap<>();

        int attackNumber;

        Scanner scan = new Scanner(System.in);

        System.out.printf("Willkommen zur Terrorauswertung.\n" +
                "Bitte geben Sie einen Zeitraum an, aus dem Sie Anschläge auswählen können." +
                "Ab welchen Datum? (yyyy-MM-dd)\n");
        startDate = scan.nextLine();
        System.out.printf("Bis zu welchem Datum? (yyyy-MM-dd)\n");
        endDate = scan.nextLine();


        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        StockCrawler stockCrawler = new StockCrawler(start, end);
        TerrorCrawler terror = new TerrorCrawler(start.getYear(),end.getYear());
        ModelBuilder builder = new ModelBuilder(terror.getAttacks(), stockCrawler.getStocks());
        builder.createModel();
        builder.saveModel();

        Model model = builder.getModel();

        // Starting querying the data
        String queryString;
        Query query;

        //Let the user see the attacks in given time interval
        queryString = "Prefix ns: <https://github.com/juuls28/semanticWEB#>\n" +
                "SELECT ?s ?c ?d ?o\n" +
                             "WHERE { ?s ns:hasInjuries ?o . \n" +
                "?s ns:inCity ?c .\n" +
                "?s ns:happenedOn ?d .\n" +
                "}\n" +
                "ORDER BY ASC(?d)" ;

        System.out.println("Query: " + queryString);
        query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;

            for ( ; results.hasNext() ; )
            {
                System.out.println("Number: " + results.getRowNumber());
                QuerySolution soln = results.nextSolution() ;
                System.out.println("Id: " + soln.get("?s") + "\tDate: " + soln.get("?d") + "\tCity: " + soln.get("?c"));
                //add attack to map
                attacks.put(results.getRowNumber(), soln.get("?s").toString());
            }
        }

        //Ask User for the attack
        System.out.println("Welcher Terrorangriff soll ausgewertet werden? (Tippe eine Zahl)");
        attackNumber = scan.nextInt();

        //Query the date for the attack again
        queryString = "Prefix ns: <https://github.com/juuls28/semanticWEB#>\n" +
                "SELECT ?d\n" +
                "WHERE { <" + attacks.get(attackNumber) + "> ns:happenedOn ?d . \n" +
                "}" ;

        System.out.println("Query: " + queryString);
        query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;

            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                System.out.println("Date: " + soln.get("?d"));
                attackDate = soln.get("?d").toString();
            }
        }

        queryString = "Prefix ns: <https://github.com/juuls28/semanticWEB#>\n" +
                "\n" +
                "SELECT ?firstStock ?secondStock ?vF ?vS ?firstPoll ?secondPoll ?p1Name ?p1Percent ?p2Name ?p2Percent\n" +
                "WHERE { \n" +
                "  {Select ?firstStock\n" +
                "    WHERE{\n" +
                "      ?firstStock ns:happenedOn ?date .\n" +
                "      ?firstStock ns:value ?o .\n" +
                "      Filter(?date < \"" + attackDate + "\")\n" +
                "    } Order By DESC (?date)\n" +
                "    Limit 1\n" +
                "  }\n" +
                "  {Select ?secondStock\n" +
                "    WHERE{\n" +
                "      ?secondStock ns:happenedOn ?date .\n" +
                "      ?secondStock ns:value ?o .\n" +
                "      Filter(?date > \"" + attackDate + "\")\n" +
                "    } Order By ASC (?date)\n" +
                "    Limit 1\n" +
                "  }\n" +
                "  ?firstStock ns:value ?vF .\n" +
                "  ?secondStock ns:value ?vS .\n" +
                "  {Select ?firstPoll\n" +
                "    WHERE{\n" +
                "      ?firstPoll ns:happenedOn ?date .\n" +
                "      ?firstPoll ns:outcomes ?o .\n" +
                "      Filter(?date < \"" + attackDate + "\")\n" +
                "    } Order By DESC (?date)\n" +
                "    Limit 1\n" +
                "  }\n" +
                "  {Select ?secondPoll\n" +
                "    WHERE{\n" +
                "      ?secondPoll ns:happenedOn ?date .\n" +
                "      ?secondPoll ns:outcomes ?o .\n" +
                "      Filter(?date > \"" + attackDate + "\")\n" +
                "    } Order By ASC (?date)\n" +
                "    Limit 1\n" +
                "  }\n" +
                "  ?firstPoll ns:outcomes ?outF .\n" +
                "  ?outF ns:partyName ?p1Name .\n" +
                "  ?outF ns:partyPercent ?p1Percent .\n" +
                "  ?secondPoll ns:outcomes ?outS .\n" +
                "  ?outS ns:partyName ?p2Name .\n" +
                "  ?outS ns:partyPercent ?p2Percent .\n" +
                "  Filter(?p1Name = ?p2Name) \n" +
                "}";

        System.out.println("Query: " + queryString);
        query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;

            for ( ; results.hasNext() ; )
            {
                System.out.println("Number: " + results.getRowNumber());
                QuerySolution soln = results.nextSolution() ;
                System.out.println(soln);
            }

        }
    }
}
