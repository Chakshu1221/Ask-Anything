package org.askanything;

public class AppointmentModel {

    String imageUrl;
    String price;
    String tattoName;
    String date;
    String time;
    String paymentStatus;
    String status;
    String cusomerName;
    String cusomerEmail;
    String uerid;

    public AppointmentModel() {
    }

    public AppointmentModel(String imageUrl, String price, String tattoName, String date, String time, String paymentStatus,
                            String status, String cusomerName, String cusomerEmail, String uerid) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.tattoName = tattoName;
        this.date = date;
        this.time = time;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.cusomerName = cusomerName;
        this.cusomerEmail = cusomerEmail;
        this.uerid = uerid;
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

    public String getTattoName() {
        return tattoName;
    }

    public void setTattoName(String tattoName) {
        this.tattoName = tattoName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCusomerName() {
        return cusomerName;
    }

    public void setCusomerName(String cusomerName) {
        this.cusomerName = cusomerName;
    }

    public String getCusomerEmail() {
        return cusomerEmail;
    }

    public void setCusomerEmail(String cusomerEmail) {
        this.cusomerEmail = cusomerEmail;
    }

    public String getUerid() {
        return uerid;
    }

    public void setUerid(String uerid) {
        this.uerid = uerid;
    }
}