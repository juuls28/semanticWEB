package com.htwk.jseiffer.finance;

import java.time.LocalDate;

public class Stock {
    private String id;
    private LocalDate date;
    private Double value;

    public Stock(LocalDate date, Double value) {
        this.id = "DAX"+date.toString();
        this.date = date;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", value=" + value +
                "}\n";
    }
}
