package com.example.marcgilbert.openrightlist;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;
import com.example.marcgilbert.openrightlist.Model.Article;
import com.example.marcgilbert.openrightlist.UI.ArticleList.ArticleListViewModel;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.BehaviorSubject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcgilbert on 19/04/2017.
 */

public class ArticleListViewModelTest {

    Map<Integer,Article> articleMap = new HashMap<>();

    @Mock
    ArticleApi articleApi;

    @Mock
    Consumer<List<Article>> consumer;


    ArticleListViewModel articleListViewModel;


    @Captor
    ArgumentCaptor<Map<Integer,Article>> argumentCaptor;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        // CREATE DUMMY ARTICLES
        Article article3 = new Article();
        article3.setTitle("Article 3 title");
        article3.setDescription("Description for article 3");
        article3.setId(3);
        articleMap.put( article3.getId() , article3 );
        Article article4 = new Article();
        article4.setTitle("Article 4 title");
        article4.setDescription("Description for article 4");
        article4.setId(4);
        articleMap.put( article4.getId() , article4 );

        articleListViewModel = new ArticleListViewModel( articleApi , BehaviorSubject.create()  );



        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };
        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

    }


    @Test
    public void loadArticlesTest(){

        when( articleApi.getArticleMapObservable() ).thenReturn( Observable.create(new ObservableOnSubscribe<Map<Integer, Article>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<Integer, Article>> e) throws Exception {
                e.onNext( articleMap );
            }
        }) );


        articleListViewModel.loadArticles();

        // VERIFY THAT articleListViewModel requested the observable form article api
        verify( articleApi ).getArticleMapObservable();

        articleListViewModel.getSubject().subscribe(articleViewModelList -> {

            assertThat( articleViewModelList.size() , is(articleMap.size()) );

        });


        //ve

    }








}
