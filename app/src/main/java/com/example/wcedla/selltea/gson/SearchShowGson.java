package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchShowGson {

    @SerializedName("SearchResult")
    public List<SearchShowDetial> showDetialList;
}
