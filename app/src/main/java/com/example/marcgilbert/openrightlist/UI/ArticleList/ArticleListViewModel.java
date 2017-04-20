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
import io.reactivex.subjects.Subject;

/**
 * Created by marcgilbert on 06/04/2017.
 */

public class ArticleListViewModel {

    ArticleApi articleApi;
    Subject<List<ArticleViewModel>> subject;


    public ArticleListViewModel(ArticleApi articleApi, Subject<List<ArticleViewModel>> subject) {
        this.articleApi = articleApi;
        this.subject = subject;
    }

    boolean loading = false;
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
                            subject.onNext(articleListViewModelList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            subject.onError(e);
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


    public Subject<List<ArticleViewModel>> getSubject() {
        return subject;
    }
}
