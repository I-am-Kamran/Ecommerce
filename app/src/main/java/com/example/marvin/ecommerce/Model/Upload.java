package com.example.marvin.ecommerce.Model;

public class Upload
{


    public String productId;
    public String name;
    public String price;
    public String photoUrl;
    public String description;
    public String quantity;


    public Upload(){}

    public Upload(String name, String price, String photoUrl, String description,String quantity,String productId)
    {
        this.name = name;
        this.price = price;
        this.photoUrl = photoUrl;
        this.description = description;
        this.quantity=quantity;
        this.productId=productId;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }



}
