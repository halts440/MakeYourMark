package com.humayunafzal.makeyourmark;

public class Product {
    private String productId;
    private String name;
    private String price;
    private String stock;
    private String image;
    private String storeId;
    private String category;
    private String description;

    public Product() {
    }

    public Product(String productId, String name, String price, String stock, String image, String storeId, String category, String description) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.storeId = storeId;
        this.category = category;
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
