package com.example.news_collector;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerviewItem_news_topic {
    private String news_topic = "";
    private ArrayList<recyclerviewItem_news_data> allItemsInSection;
    private Boolean favorite = false;



    public String getNews_topic() {
        return news_topic;
    }

    public void setNews_topic(String news_topic) {
        this.news_topic = news_topic;
    }

    public ArrayList<recyclerviewItem_news_data> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<recyclerviewItem_news_data> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
