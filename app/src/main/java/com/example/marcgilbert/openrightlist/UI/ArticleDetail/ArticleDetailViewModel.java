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

public class ArticleDetailViewModel {

    int articleId;
    ArticleApi articleApi;
    Observable<ArticleDetailViewModel> articleObservable;


    public ArticleDetailViewModel(ArticleApi articleApi, int articleId) {

        this.articleApi = articleApi;
        this.articleId = articleId;

    }



}
