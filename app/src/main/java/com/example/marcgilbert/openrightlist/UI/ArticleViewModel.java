package com.example.marcgilbert.openrightlist.UI;

import com.example.marcgilbert.openrightlist.Model.Article;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by marcgilbert on 06/04/2017.
 */

public class ArticleViewModel {

    int articleId;
    String author;
    String title;
    String description;
    URL url;
    URL urlToImage;
    String publishedAt;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public void setArticle(Article article){

        articleId = article.getId();
        author = article.getAuthor();
        title = article.getTitle();
        description = article.getDescription();
        url = article.getUrl();
        urlToImage = article.getUrlToImage();
        Date publishedDate = article.getPublishedAt();
        publishedAt = publishedDate!=null ? simpleDateFormat.format(publishedDate) : "";

    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(URL urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


}
