package com.example.marcgilbert.openrightlist.UI.ArticleList;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;
import com.example.marcgilbert.openrightlist.ArticleApplication;
import com.example.marcgilbert.openrightlist.R;
import com.example.marcgilbert.openrightlist.UI.ArticleDetail.ArticleDetailFragment;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;
import com.example.openrightrecyclerview.OpenRightView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class ArticleListActivity extends AppCompatActivity implements ArticleDetailFragment.OnFragmentInteractionListener {

    private Context context;
    private OpenRightView openRightView;
    private FloatingActionButton floatingActionButton;
    private ArticleListViewModel articleListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        openRightView = (OpenRightView) findViewById(R.id.openRightView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_refresh);
        floatingActionButton.setOnClickListener( v ->{
            Toast.makeText(context,"Toast",Toast.LENGTH_LONG ).show();
        } );


        List<ArticleViewModel> articleViewModelList = new ArrayList<>();
        ArticleListAdapter articleListAdapter = new ArticleListAdapter( articleViewModelList );
        openRightView.setAdapter( articleListAdapter );

        ArticleApi articleApi = ((ArticleApplication) getApplication()).getArticleApi();
        articleListViewModel = new ArticleListViewModel( articleApi, BehaviorSubject.create() );
        articleListViewModel.getSubject().subscribe(new Observer<List<ArticleViewModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<ArticleViewModel> articleViewModelList) {
                articleListAdapter.update( articleViewModelList );
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText( context , e.getMessage() , Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onComplete() {
            }
        });

        articleListViewModel.loadArticles();

    }

    @Override
    public void onCloseFragment() {
        openRightView.closeFragment();
    }



    @Override
    public void onBackPressed() {
        openRightView.closeFragment();
    }
}
