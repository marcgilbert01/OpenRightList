package com.example.marcgilbert.openrightlist;

import android.app.Fragment;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openrightrecyclerview.OpenRightView;
import com.example.openrightrecyclerview.OpenRightViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity implements MyFragment.OnFragmentInteractionListener{


    private Context context;
    private OpenRightView openRightView;
    private MyOpenRightAdapter myOpenRightAdapter;
    private ArticleApi articleApi;
    private FloatingActionButton floatingActionButton;



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




        articleApi = ArticleApi.getInstance();
        Observable observable = articleApi.getObservable();
        observable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread() )
            .subscribe(new Observer<Map<Integer,Article>>() {

                @Override
                public void onSubscribe(Disposable d) {

                    List<Article> articleList = new ArrayList<Article>();
                    myOpenRightAdapter = new MyOpenRightAdapter( articleList );
                    openRightView.setAdapter( myOpenRightAdapter );
                }

                @Override
                public void onNext(Map<Integer,Article> articleMap) {

                    List<Article> articleList = new ArrayList<Article>(articleMap.values());
                    myOpenRightAdapter.update( articleList );
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete() {
                }
        });






    }

    @Override
    public void onCloseFragment() {
        openRightView.closeFragment();
    }


    public class MyOpenRightAdapter extends OpenRightView.OpenRightAdapter<MyOpenRightAdapter.MyViewHolder>{


        List<Article> articleList = null;

        public MyOpenRightAdapter(List<Article> articleList) {
            this.articleList = articleList;
        }

        @Override
        public Fragment getFragment(int position) {

            Fragment openRightFragment = MyFragment.newInstance( articleList.get(position).getId() );

            return openRightFragment;
        }

        @Override
        public MyViewHolder onCreateOpenRightViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate( R.layout.cell_view, null );
            MyViewHolder myViewHolder = new MyViewHolder(view);

            return myViewHolder;
        }

        @Override
        public void onBindOpenRightViewHolder(MyViewHolder viewHolder, int position) {

            Article article = articleList.get(position);
            viewHolder.textViewAuthor.setText( article.author );
            viewHolder.textViewTitle.setText( article.title );
            viewHolder.textViewDescription.setText( article.description );
            Picasso.with(context)
                    .load( article.getUrlToImage() )
                    .into( viewHolder.imageView );

        }

        @Override
        public int getItemCount() {

            return articleList.size();
        }

        public class MyViewHolder extends OpenRightViewHolder {

            View itemView;
            TextView textViewTitle;
            TextView textViewDescription;
            TextView textViewAuthor;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
                textViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
                textViewAuthor = (TextView) itemView.findViewById(R.id.textView_author);
                imageView = (ImageView) itemView.findViewById(R.id.imageViewInCell);

            }
        }


        public void update(List<Article> articleList){

            this.articleList = articleList;
            notifyDataSetChanged();
        }


    }


    @Override
    public void onBackPressed() {
        openRightView.closeFragment();
    }
}
