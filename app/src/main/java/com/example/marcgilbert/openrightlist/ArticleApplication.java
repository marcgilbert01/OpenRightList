package com.example.marcgilbert.openrightlist;

import android.app.Application;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;

/**
 * Created by marcgilbert on 10/04/2017.
 */

public class ArticleApplication extends Application {


    ArticleApi articleApi;


    @Override
    public void onCreate() {
        super.onCreate();

        articleApi = new ArticleApi();
    }


    public ArticleApi getArticleApi() {
        return articleApi;
    }
}
