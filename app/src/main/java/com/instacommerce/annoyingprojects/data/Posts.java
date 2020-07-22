package com.instacommerce.annoyingprojects.data;

import java.util.ArrayList;

public class Posts {
    private PostModel postModel;
    private String linkUserImg;
    private ArrayList<String > linkImages;


    public PostModel getPostModel() {
        return postModel;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }

    public String getLinkUserImg() {
        return linkUserImg;
    }

    public void setLinkUserImg(String linkUserImg) {
        this.linkUserImg = linkUserImg;
    }

    public ArrayList<String> getLinkImages() {
        return linkImages;
    }

    public void setLinkImages(ArrayList<String> linkImages) {
        this.linkImages = linkImages;
    }
}
