package org.sssj.com.dogecoin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Athu on 3/28/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    List<News> news;
    Context context;

    public NewsAdapter(List<News> e, Context context) {
        this.news = e;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.news_Sub.setText(news.get(position).getSubject());
        holder.news_Details.setText(news.get(position).getDetails());

    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_Sub, news_Details;

        public MyViewHolder(View itemView) {
            super(itemView);
            news_Sub = (TextView) itemView.findViewById(R.id.txtSubject);
            news_Details = (TextView) itemView.findViewById(R.id.txtDetails);
        }
    }
}
