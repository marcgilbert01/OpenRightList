package com.example.marcgilbert.openrightlist;

import android.os.Handler;
import android.preference.PreferenceActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcgilbert on 16/03/2017.
 */

public class ArticleApi {
    private static final ArticleApi ourInstance = new ArticleApi();

    public static ArticleApi getInstance() {
        return ourInstance;
    }
    //c300aa702ada4a4f9ad71e791ab10889

    static String API_URL = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=c300aa702ada4a4f9ad71e791ab10889";

    Map<Integer,Article> articleMap = new HashMap<>();


    private ArticleApi() {

    }


    public void getArticles(final Listener listener){

        final Handler handler = new Handler();
        new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    articleMap = readArticles();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onArticleLoaded(articleMap);
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError( e.getMessage() );
                        }
                    });
                }
            }
        }.start();

    }


    public Article getArticle(int articleId  ){
        return articleMap.get(articleId);
    }


    public interface Listener{

        void onArticleLoaded(Map<Integer,Article> articleMap);

        void onError(String errorMessage);
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

                Article article = response.articles[a];
                article.setId(a+1);
                articleMap.put( article.getId() , article );
            }
        }

        return articleMap;
    }



}
