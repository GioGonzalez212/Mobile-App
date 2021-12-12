package com.example.finalproject;

public class News {
    // variable for currency name,
    // currency symbol and price.
    private String name;
    private String symbol;
    private String imageURL;
    private double price;
    private double LastHour;
    private double LastWeek;
    private String UpdateDate;

    public News(String name, String symbol, double LastHour, double LastWeek, String UpdateDate) {
        this.name = name;
        this.symbol = symbol;
        this.LastHour = LastHour;
        this.LastWeek = LastWeek;
        this.UpdateDate = UpdateDate;
    }

    public double getLastHour(){
        return LastHour;
    }

    public void setLastHour(double LastHour){
        this.LastHour = LastHour;
    }

    public double getLastWeek(){
        return LastWeek;
    }

    public void setLastWeek(double LastWeek){
        this.LastWeek = LastWeek;
    }

    public String getUpdateDate(){
        return UpdateDate;
    }

    public void setUpdateDate(String UpdateDate){
        this.UpdateDate = UpdateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getImageURL() { return imageURL; }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
