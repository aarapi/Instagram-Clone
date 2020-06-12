package com.example.annoyingprojects.data;

import java.io.Serializable;
import java.util.ArrayList;

public class PostModel implements Serializable {
    private int postId;
    private String userName;
    private ArrayList<String> linkImages;
    private String linkUserImg;
    private String productName;
    private String productPrice;
    private String productDescription;
    private boolean isLikeChecked;
    private int likedByNo;
    private int categoryId;
    private String country;
    private String city;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getLinkUserImg() {
        return linkUserImg;
    }

    public ArrayList<String> getLinkImages() {
        return linkImages;
    }

    public void setLinkImages(ArrayList<String> linkImages) {
        this.linkImages = linkImages;
    }

    public void setLinkUserImg(String linkUserImg) {
        this.linkUserImg = linkUserImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean isLikeChecked() {
        return isLikeChecked;
    }

    public void setLikeChecked(boolean likeChecked) {
        isLikeChecked = likeChecked;
    }

    public int getLikedByNo() {
        return likedByNo;
    }

    public void setLikedByNo(int likedByNo) {
        this.likedByNo = likedByNo;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
