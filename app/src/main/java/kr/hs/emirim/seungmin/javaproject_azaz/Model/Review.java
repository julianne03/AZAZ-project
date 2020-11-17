package kr.hs.emirim.seungmin.javaproject_azaz.Model;

import java.util.Date;

public class Review extends kr.hs.emirim.seungmin.javaproject_azaz.Model.ReviewId {

    public String item_name, item_price, item_brand, item_category, item_image1, user_id, item_good, item_bad, item_recommend, item_etc;
    public Date timestamp;

    public Review() {}

    public Review(Date timestamp) {this.timestamp = timestamp;}

    public Review(String item_name, String item_price, String item_brand, String item_category, String item_image1, String user_id, String item_good, String item_bad, String item_recommend, String item_etc, Date timestamp) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_brand = item_brand;
        this.item_category = item_category;
        this.item_image1 = item_image1;
        this.user_id = user_id;
        this.item_good = item_good;
        this.item_bad = item_bad;
        this.item_recommend = item_recommend;
        this.item_etc = item_etc;
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

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
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

    public String getItem_good() {
        return item_good;
    }

    public void setItem_good(String item_good) {
        this.item_good = item_good;
    }

    public String getItem_bad() {
        return item_bad;
    }

    public void setItem_bad(String item_bad) {
        this.item_bad = item_bad;
    }

    public String getItem_recommend() {
        return item_recommend;
    }

    public void setItem_recommend(String item_recommend) {
        this.item_recommend = item_recommend;
    }

    public String getItem_etc() {
        return item_etc;
    }

    public void setItem_etc(String item_etc) {
        this.item_etc = item_etc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
