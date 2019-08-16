package com.htwk.jseiffer.poll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Poll {
    private LocalDate date;
    private List<Party> outcomes = new ArrayList<Party>();

    public Poll(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate(){
        return date;
    }

    public void addOutcome(Party party){
        outcomes.add(party);
    }

    public List<Party> getOutcomes() {
        return outcomes;
    }

    @Override
    public String toString() {
        return  date +
                "\t" + outcomes;
    }
}
