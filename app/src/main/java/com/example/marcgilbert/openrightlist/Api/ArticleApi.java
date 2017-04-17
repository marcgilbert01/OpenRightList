package com.example.marcgilbert.openrightlist.Api;

import com.example.marcgilbert.openrightlist.Model.Article;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by marcgilbert on 16/03/2017.
 */

public class ArticleApi{


    //c300aa702ada4a4f9ad71e791ab10889

    static String API_URL = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=c300aa702ada4a4f9ad71e791ab10889";

    Map<Integer,Article> articleMap = new HashMap<>();
    Observable<Map<Integer,Article>> articleMapObservable = null;


    public ArticleApi() {

    }


    public Article getArticle(int articleId  ){
        return articleMap.get(articleId);
    }



    private Map<Integer,Article> readArticles() throws IOException {

        Map<Integer,Article> articleMap = new HashMap<>();

        URLConnection conn = null;
        InputStream inputStream = null;
        URL url = new URL(API_URL);
        conn = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpConn.getInputStream();
        }

        // READ
        StringBuilder stringBuilder = new StringBuilder();
        int l=0;
        byte[] buffer = new byte[1024];
        while( (l=inputStream.read(buffer))>0 ){
            String str = new String( buffer , 0, l );
            stringBuilder.append( str );
        }
        String json= stringBuilder.toString();

        //
        Response response = new Gson().fromJson( json , Response.class );
        if( response!=null && response.articles!=null && response.articles.length>0 ){

            for(int a=0; a<response.articles.length; a++ ){

                ArticleServerModel articleServerModel = response.articles[a];
                Article article = createArticleFromArticleInResponse(articleServerModel, a+1 );
                articleMap.put( article.getId() , article );
            }
        }

        return articleMap;
    }




    public Observable<Map<Integer,Article>> getArticleMapObservable(){

        if( articleMapObservable ==null ){
            articleMapObservable = createObservableForArticleMap();
        }

        return articleMapObservable;

    }

    private Observable<Map<Integer,Article>> createObservableForArticleMap(){


        return Observable.create(new ObservableOnSubscribe<Map<Integer, Article>>() {
                @Override
                public void subscribe(final ObservableEmitter<Map<Integer, Article>> e) throws Exception {

                    articleMap = readArticles();
                    e.onNext( articleMap );
                }
            }).subscribeOn(Schedulers.newThread()
        );

    }



    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    private Article createArticleFromArticleInResponse(ArticleServerModel articleServerModel, int id){

        Article article = new Article();
        article.setId( id );
        article.setAuthor( articleServerModel.author );
        article.setTitle( articleServerModel.title );
        article.setDescription( articleServerModel.description );
        try {
            article.setUrl( new URL( articleServerModel.url ) );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            article.setUrlToImage( new URL( articleServerModel.urlToImage ) );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            String publishedAt = articleServerModel.publishedAt.replaceAll( "T|Z" , " " );
            Date publishedAtDate = simpleDateFormat.parse( publishedAt );
            article.setPublishedAt( publishedAtDate );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return article;
    }




}
