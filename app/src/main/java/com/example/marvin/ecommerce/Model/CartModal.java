package com.example.marvin.ecommerce.Model;

public class CartModal
{
    private String productId;
    private String name;
    private String price;
    private String photoUrl;
    private String description;
    private String quantity;
    private String position="1";

    public CartModal(){}

    public CartModal(String productId, String name, String price, String photoUrl, String description, String quantity, String position) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.photoUrl = photoUrl;
        this.description = description;
        this.quantity = quantity;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
