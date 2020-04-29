package com.example.annoyingprojects.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostModel implements Serializable {
    private String ID;
    private String userName;
    private ArrayList<String> linkImages;
    private String linkUserImg;
    private String productName;
    private String productPrice;
    private String productDescription;
    private boolean isLikeChecked;
    private int likedByNo;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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


}
