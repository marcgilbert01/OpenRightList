package com.example.marcgilbert.openrightlist;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

/**
 * Created by marcgilbert on 16/03/2017.
 */

public class ArticleApi{

    private static final ArticleApi ourInstance = new ArticleApi();

    public static ArticleApi getInstance() {
        return ourInstance;
    }
    //c300aa702ada4a4f9ad71e791ab10889

    static String API_URL = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=c300aa702ada4a4f9ad71e791ab10889";

    Map<Integer,Article> articleMap = new HashMap<>();
    Observable<Map<Integer,Article>> mapObservable = null;


    private ArticleApi() {

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

                Article article = response.articles[a];
                article.setId(a+1);
                articleMap.put( article.getId() , article );
            }
        }

        return articleMap;
    }




    public Observable<Map<Integer,Article>> getObservable(){

        if( mapObservable==null ){
            mapObservable = createObservableForArticleMap();
        }

        return mapObservable;

    }

    private Observable<Map<Integer,Article>> createObservableForArticleMap(){


        return Observable.create(new ObservableOnSubscribe<Map<Integer, Article>>() {
            @Override
            public void subscribe(final ObservableEmitter<Map<Integer, Article>> e) throws Exception {

                articleMap = readArticles();
                e.onNext( articleMap );

                /*
                single.subscribe(new SingleObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Object o) {

                        try {
                            e.onNext( readArticles() );
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
                */


            }
        });

    }







}
