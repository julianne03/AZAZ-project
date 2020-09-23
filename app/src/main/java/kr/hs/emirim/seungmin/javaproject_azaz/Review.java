package kr.hs.emirim.seungmin.javaproject_azaz;

import java.util.Date;

public class Review extends ReviewId {

    public String item_name, item_price, item_brand, item_image1, user_id;
    public Date timestamp;

    public Review() {}

    public Review(Date timestamp) {this.timestamp = timestamp;}

    public Review(String item_name, String item_price, String item_brand, String item_image1, String user_id, Date timestamp) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_brand = item_brand;
        this.item_image1 = item_image1;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_brand() {
        return item_brand;
    }

    public void setItem_brand(String item_brand) {
        this.item_brand = item_brand;
    }

    public String getItem_image1() {
        return item_image1;
    }

    public void setItem_image1(String item_image1) {
        this.item_image1 = item_image1;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
