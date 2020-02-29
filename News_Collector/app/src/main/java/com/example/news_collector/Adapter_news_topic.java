package com.example.news_collector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.LinkAddress;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_news_topic extends RecyclerView.Adapter<Adapter_news_topic.ViewHolder> {
    private ArrayList<recyclerviewItem_news_topic> newsTopicList;
    private ArrayList<recyclerviewItem_news_data> newsDataList;


    Adapter_news_topic(ArrayList<recyclerviewItem_news_topic> dataList){
        this.newsTopicList = dataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_topic, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_news_topic.ViewHolder holder, int position) {
        final int Position  = position;
        final String topic = newsTopicList.get(Position).getNews_topic();
        ArrayList sectionItems = newsTopicList.get(Position).getAllItemsInSection();
        Boolean favorite = newsTopicList.get(Position).getFavorite();


        holder.Topic.setText(topic);
        holder.favorite = favorite;

        holder.Topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean temp;
                Log.i("main_position = ", Integer.toString(Position));

                temp = newsTopicList.get(Position).getFavorite();

                if(temp){
                    newsTopicList.get(Position).setFavorite(false);
                }else{
                    newsTopicList.get(Position).setFavorite(true);
                }
                notifyDataSetChanged();


            }
        });


        if(holder.favorite){
            holder.imageview_star.setImageResource(R.drawable.yellow_star);
        }else{
            holder.imageview_star.setImageResource(R.drawable.empty_star);
        }

//        holder.recyclerView.setHasFixedSize(false);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), RecyclerView.VERTICAL, false));
        Adapter_news_data adapter_news_data = new Adapter_news_data(sectionItems);
        holder.recyclerView.setAdapter(adapter_news_data);



    }


    @Override
    public int getItemCount() {
        return (null != newsTopicList ? newsTopicList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView      Topic;
        protected RecyclerView  recyclerView;
        protected Boolean       favorite;
        protected ImageView     imageview_star;

        public ViewHolder(View itemView){
            super(itemView);

            this.Topic          = (TextView)itemView.findViewById(R.id.textview_Topic);
            this.recyclerView   = (RecyclerView) itemView.findViewById(R.id.recyclerview_news_data);
            this.favorite       = false;
            this.imageview_star = (ImageView)itemView.findViewById(R.id.imageview_favorite);
        }
    }

    // find Favorite using String type
    protected Boolean getFavorite(String search){
        for(int i = 0 ; i < newsTopicList.size(); i++){

            if(newsTopicList.get(i).getNews_topic().equals(search)) {
                return newsTopicList.get(i).getFavorite();
            }
        }
        return false;
    }
}
