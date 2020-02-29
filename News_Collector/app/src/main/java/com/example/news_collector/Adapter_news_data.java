package com.example.news_collector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_news_data extends RecyclerView.Adapter<Adapter_news_data.ViewHolder> {
    private ArrayList<recyclerviewItem_news_data> newsDataList;

    Adapter_news_data(ArrayList<recyclerviewItem_news_data> dataList){
        this.newsDataList = dataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_data, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_news_data.ViewHolder holder, int position) {
        final int Position = position;

        holder.Headline.setText(newsDataList.get(Position).getTitle());
        holder.Contents.setText(newsDataList.get(Position).getContents());

        holder.linearLayoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("URL: ",newsDataList.get(Position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsDataList.get(Position).getUrl()));
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != newsDataList ? newsDataList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView      Headline;
        protected TextView      Contents;
        protected LinearLayout  linearLayoutData;


        public ViewHolder(View itemView){
            super(itemView);

            this.Headline       = (TextView)itemView.findViewById(R.id.textview_Headline);
            this.Contents       = (TextView)itemView.findViewById(R.id.textview_Comments);
            this.linearLayoutData= (LinearLayout)itemView.findViewById(R.id.linearlayout_Data);
        }
    }
}
