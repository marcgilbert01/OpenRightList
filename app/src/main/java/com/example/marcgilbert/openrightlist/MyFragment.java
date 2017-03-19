package com.example.marcgilbert.openrightlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "dataIdParam";
    private Activity parentActivity;

    // TODO: Rename and change types of parameters
    private int articleId;
    private Article article;
    private ArticleApi articleApi;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewAuthor;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;


    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(int dataId ) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dataId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleId = getArguments().getInt(ARG_PARAM1);
        }
        articleApi = ArticleApi.getInstance();
        article = articleApi.getArticle(articleId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        textViewTitle = (TextView) view.findViewById(R.id.fTextViewTitle);
        textViewDescription = (TextView) view.findViewById(R.id.fTextViewDescription);
        textViewAuthor = (TextView) view.findViewById(R.id.fTtextViewAuthor);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        imageView = (ImageView) view.findViewById(R.id.fImageView);

        textViewTitle.setText( article.getTitle() );
        textViewDescription.setText( article.getDescription() );
        textViewAuthor.setText( article.getAuthor() );
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseFragment();
            }
        });
        Picasso.with( parentActivity ).load( article.getUrlToImage() ).into( imageView );


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
