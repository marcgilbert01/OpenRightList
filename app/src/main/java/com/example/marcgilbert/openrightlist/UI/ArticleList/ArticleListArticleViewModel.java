package com.example.marcgilbert.openrightlist.UI.ArticleList;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;
import com.example.marcgilbert.openrightlist.Model.Article;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by marcgilbert on 06/04/2017.
 */

public class ArticleListArticleViewModel implements ArticleListNavigator{


    BehaviorSubject<List<ArticleViewModel>> articleListBehaviorSubject = BehaviorSubject.create();
    ArticleApi articleApi;



    public ArticleListArticleViewModel(ArticleApi articleApi) {
        this.articleApi = articleApi;
    }


    boolean loading = false;
    @Override
    public void loadArticles(){

        if( !loading ){

            loading = true;

            articleApi.getArticleMapObservable()
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe(new Observer<Map<Integer, Article>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Map<Integer, Article> integerArticleMap) {

                            List<ArticleViewModel> articleListViewModelList = convertModelMapToModelViewList(integerArticleMap);
                            articleListBehaviorSubject.onNext(articleListViewModelList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            articleListBehaviorSubject.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            loading = false;
                        }
                    });

        }
    }



    private List<ArticleViewModel> convertModelMapToModelViewList(Map<Integer,Article> articleMap ){

        List<ArticleViewModel> articleViewModelList = new ArrayList<>();
        for(Map.Entry<Integer,Article> entry: articleMap.entrySet() ) {

            ArticleViewModel articleViewModel = new ArticleViewModel();
            articleViewModel.setArticle( entry.getValue() );
            articleViewModelList.add(articleViewModel);
        }
        return articleViewModelList ;

    }


    public void start() {
        loadArticles();
    }


    public BehaviorSubject<List<ArticleViewModel>> getArticleListBehaviorSubject() {
        return articleListBehaviorSubject;
    }
}
