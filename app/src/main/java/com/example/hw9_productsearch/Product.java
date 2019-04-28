package com.example.hw9_productsearch;

class Product {

    private String productID;
    private String productTitle;
    private String productZip;
    private String productShipping;
    private String productCondition;
    private String productPrice;
    private String productImageURL;
    private String titleFull;
    private int productWishIcon;

    public Product() {

    }

    public Product(String productID, String productTitle, String productZip, String productShipping,
                   String productCondition, String productPrice, String productImageURL,
                   int productWishIcon, String titleFull)
    {
        this.productID = productID;
        this.productTitle = productTitle;
        this.productZip = productZip;
        this.productShipping = productShipping;
        this.productCondition = productCondition;
        this.productPrice = productPrice;
        this.productImageURL = productImageURL;
        this.productWishIcon = productWishIcon;
        this.titleFull = titleFull;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductZip() {
        return productZip;
    }

    public String getProductShipping() {
        return productShipping;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public int getProductWishIcon() {
        return productWishIcon;
    }

    public String getTitleFull() {
        return titleFull;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductZip(String productZip) {
        this.productZip = productZip;
    }

    public void setProductShipping(String productShipping) {
        this.productShipping = productShipping;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public void setTitleFull(String titleFull) {
        this.titleFull = titleFull;
    }

    public void setProductWishIcon(int productWishIcon) {
        this.productWishIcon = productWishIcon;
    }
}
