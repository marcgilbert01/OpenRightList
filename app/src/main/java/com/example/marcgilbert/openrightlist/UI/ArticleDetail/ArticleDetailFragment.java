package com.example.marcgilbert.openrightlist.UI.ArticleDetail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcgilbert.openrightlist.Api.ArticleApi;
import com.example.marcgilbert.openrightlist.ArticleApplication;
import com.example.marcgilbert.openrightlist.Model.Article;
import com.example.marcgilbert.openrightlist.R;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;
import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class ArticleDetailFragment extends Fragment {

    private static final String ARG_ARTICLE_ID = "argArticleId";
    private Activity parentActivity;

    private int articleId;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewAuthor;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;

    ArticleDetailViewModel articleDetailViewModel;
    Observable<Article> articleObservable;


    private OnFragmentInteractionListener mListener;

    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ArticleDetailFragment newInstance(int articleId ) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleId = getArguments().getInt(ARG_ARTICLE_ID);
        }

        ArticleApi articleApi = ((ArticleApplication) parentActivity.getApplication()).getArticleApi();
        articleDetailViewModel = new ArticleDetailViewModel( articleApi , articleId );
        articleObservable = articleDetailViewModel.getArticleObservable();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        textViewTitle = (TextView) view.findViewById(R.id.fTextViewTitle);
        textViewDescription = (TextView) view.findViewById(R.id.fTextViewDescription);
        textViewAuthor = (TextView) view.findViewById(R.id.fTtextViewAuthor);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        imageView = (ImageView) view.findViewById(R.id.fImageView);

        articleObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Article>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Article article) {

                textViewTitle.setText( article.getTitle() );
                textViewDescription.setText( article.getDescription() );
                textViewAuthor.setText( article.getAuthor() );
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onCloseFragment();
                    }
                });
                Picasso.with( parentActivity ).load( article.getUrlToImage().toString() ).into( imageView );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onCloseFragment();
    }
}
