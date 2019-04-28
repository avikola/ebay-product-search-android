package com.example.hw9_productsearch;

class SimilarItem
{
    private String item_urls;
    private String title;
    private String shipping_cost;
    private int timeLeft;
    private double price;
    private int total;
    private String imageurls;

    public SimilarItem() {}

    public SimilarItem(String item_urls, String title, String shipping_cost,
                       int timeLeft, double price, int total, String imageurls)
    {
        this.item_urls = item_urls;
        this.title = title;
        this.shipping_cost = shipping_cost;
        this.timeLeft = timeLeft;
        this.price = price;
        this.total = total;
        this.imageurls = imageurls;
    }

    public String getItem_urls() {
        return item_urls;
    }

    public String getTitle() {
        return title;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public double getPrice() {
        return price;
    }

    public int getTotal() {
        return total;
    }

    public void setItem_urls(String item_urls) {
        this.item_urls = item_urls;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }
}
