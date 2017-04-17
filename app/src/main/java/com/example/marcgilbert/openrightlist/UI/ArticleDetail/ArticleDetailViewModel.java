package com.example.marcgilbert.openrightlist.UI.ArticleDetail;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;
import com.example.marcgilbert.openrightlist.Model.Article;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

/**
 * Created by marcgilbert on 13/04/2017.
 */

public class ArticleDetailViewModel extends ArticleViewModel {

    int articleId;
    ArticleApi articleApi;
    Observable<Article> articleObservable;


    public ArticleDetailViewModel(ArticleApi articleApi, int articleId) {

        this.articleApi = articleApi;
        this.articleId = articleId;

        articleObservable = Observable.create(new ObservableOnSubscribe<Article>() {
            @Override
            public void subscribe(ObservableEmitter<Article> e) throws Exception {
                e.onNext( articleApi.getArticle(  articleId  ) );
            }
        });

    }

    public Observable<Article> getArticleObservable() {
        return articleObservable;
    }

}
