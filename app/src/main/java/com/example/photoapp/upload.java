package com.example.photoapp;

public class upload {
    String name,imageUrl;
    public upload()
    {}
    public upload(String name,String imageUrl) {
        if (name.trim().equals(""))
        {
            name="No Name";
            this.name=name;
        }
        this.name=name;
        this.imageUrl=imageUrl;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
