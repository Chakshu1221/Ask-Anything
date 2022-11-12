
package org.askanything;


public class TattooModel {


    String imageUrl;

    String price;

    String name;


    public TattooModel() {
    }

    public TattooModel(String imageUrl, String price, String name) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}