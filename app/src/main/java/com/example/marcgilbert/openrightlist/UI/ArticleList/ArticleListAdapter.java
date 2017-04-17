package com.example.marcgilbert.openrightlist.UI.ArticleList;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcgilbert.openrightlist.R;
import com.example.marcgilbert.openrightlist.UI.ArticleDetail.ArticleDetailFragment;
import com.example.marcgilbert.openrightlist.UI.ArticleViewModel;
import com.example.openrightrecyclerview.OpenRightView;
import com.example.openrightrecyclerview.OpenRightViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by marcgilbert on 06/04/2017.
 */

public class ArticleListAdapter extends OpenRightView.OpenRightAdapter<ArticleListAdapter.MyViewHolder>{


    List<ArticleViewModel> articleViewModelList = null;


    public ArticleListAdapter(List<ArticleViewModel> articleViewModelList) {
        this.articleViewModelList = articleViewModelList;
    }

    @Override
    public Fragment getFragment(int position) {

        Fragment openRightFragment = ArticleDetailFragment.newInstance( articleViewModelList.get(position).getArticleId() );
        return openRightFragment;
    }

    @Override
    public MyViewHolder onCreateOpenRightViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.cell_view, null );
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }


    @Override
    public void onBindOpenRightViewHolder(MyViewHolder viewHolder, int position) {

        ArticleViewModel articleViewModel = articleViewModelList.get(position);
        viewHolder.textViewAuthor.setText( articleViewModel.getAuthor() );
        viewHolder.textViewTitle.setText( articleViewModel.getTitle() );
        viewHolder.textViewDescription.setText( articleViewModel.getDescription() );
        Picasso.with(viewHolder.imageView.getContext())
                .load( articleViewModel.getUrlToImage().toString() )
                .into( viewHolder.imageView );

    }

    @Override
    public int getItemCount() {

        return articleViewModelList.size();
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


    public void update(List<ArticleViewModel> articleViewModelList){

        this.articleViewModelList = articleViewModelList;
        notifyDataSetChanged();
    }


}
