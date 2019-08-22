package com.htwk.jseiffer;

import com.htwk.jseiffer.finance.Stock;
import com.htwk.jseiffer.finance.StockCrawler;
import com.htwk.jseiffer.poll.PollCrawler;
import com.htwk.jseiffer.terror.TerrorCrawler;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

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
        ModelBuilder builder = new ModelBuilder(terror.getAttacks(), stockCrawler.getStocks());
        builder.createModel();
        builder.writeModel();
        builder.saveModel();

        Model model = builder.getModel();



        String queryString = "SELECT ?s  " +
                "WHERE { ?s <http://www.terror.org#happenedOn> \"2015-09-19\"  }" ;

        System.out.println("Query: " + queryString);
        Query query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;

            for ( ; results.hasNext() ; )
            {
                System.out.println("Number: " + results.getRowNumber());
                QuerySolution soln = results.nextSolution() ;
                RDFNode x = soln.get("varName") ;       // Get a result variable by name.
                Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
                Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
                System.out.println(soln.toString());
            }
        }
    }
}
