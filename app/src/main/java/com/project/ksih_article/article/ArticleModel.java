package com.project.ksih_article.article;

import java.io.Serializable;

public class ArticleModel implements Serializable {


    public String name;
    public String title;
    public String imageUrl;
    public String weblink;
    public String id;
    public String imageName;





    public ArticleModel(){}

    public ArticleModel(String name, String title, String weblink, String imageUrl, String imageName){

        this.setId(id);
        this.setImageUrl(imageUrl);
        this.setName(name);
        this.setTitle(title);
        this.setWeblink(weblink);
        this.setImageName(imageName);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String id) { this.id = id; }

    public void setImageName(String imageName) { this.imageName = imageName; }


    public String getImageUrl() { return imageUrl; }

    public String getTitle() {
        return title;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    public String getImageName() { return imageName; }
}
