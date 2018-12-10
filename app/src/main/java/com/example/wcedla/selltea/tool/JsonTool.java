package com.example.wcedla.selltea.tool;

import android.util.Log;

import com.example.wcedla.selltea.database.BannerHotImageTable;
import com.example.wcedla.selltea.gson.AdressDetial;
import com.example.wcedla.selltea.gson.AdressGson;
import com.example.wcedla.selltea.gson.BuyCarDetial;
import com.example.wcedla.selltea.gson.BuyCarGson;
import com.example.wcedla.selltea.gson.GoodsDetialGson;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.gson.SearchShowGson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class JsonTool {

    public static boolean getStatus(String jsonData)
    {

        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            String data=jsonObject.getString("status");
            //Log.d(TAG, "内容"+data);
            if(data.equals("ok"))
                return  true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getRegisterResult(String jsonData)
    {

        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            String data=jsonObject.getString("status");
            //Log.d(TAG, "内容"+data);
            if(data.equals("ok"))
                return  0;
            else if(data.equals("exist"))
                return -1;
            else
                return 1;
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int getHotImage(String jsonData)
    {
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            if(jsonObject.has("status"))
            {
                return 0;
            }
            else
            {
                LitePal.deleteAll(BannerHotImageTable.class);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject results=jsonArray.getJSONObject(i);
                    BannerHotImageTable bannerHotImageTable=new BannerHotImageTable();
                    bannerHotImageTable.setImgResource(results.getString("resource"));
                    bannerHotImageTable.setTitle(results.getString("title"));
                    bannerHotImageTable.save();
                }
                return 1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(TAG, "错误 "+e);
        }
        return -1;
    }

    public static List<SearchShowDetial> getShowResult(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has("status")) {
                return new ArrayList<>();
            } else {
                Gson gson = new Gson();
                SearchShowGson searchShowGson = gson.fromJson(jsonData, SearchShowGson.class);
                //Log.d(TAG, "结果"+searchShowGson.showDetialList.size());
                //searchShowDetialList=searchShowGson.showDetialList;
                return searchShowGson.showDetialList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static GoodsDetialGson getGoodsDetial(String jsonData)
    {
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            if (jsonObject.has("status")) {
                return null;
            } else {
                Gson gson = new Gson();
                GoodsDetialGson goodsDetialGson = gson.fromJson(jsonData, GoodsDetialGson.class);
                return goodsDetialGson;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<AdressDetial> getAdressInfo(String jsonData)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has("status")) {
                return new ArrayList<>();
            } else {
                Gson gson = new Gson();
                AdressGson adressGson = gson.fromJson(jsonData, AdressGson.class);
                //Log.d(TAG, "结果"+searchShowGson.showDetialList.size());
                //searchShowDetialList=searchShowGson.showDetialList;
                return adressGson.adressDetialList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public static long getAdressMaxId(String jsonData)
//    {
//        String idStr;
//        try {
//            JSONObject jsonObject=new JSONObject(jsonData);
//            idStr=jsonObject.getString("id");
//            //Log.d(TAG, "id号码"+idStr);
//            return Integer.valueOf(idStr);
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        }
//        return 0;
//    }

    public static List<BuyCarDetial> getBuyCarDetial(String jsonData)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has("status")) {
                return new ArrayList<>();
            } else {
                Gson gson = new Gson();
                BuyCarGson buyCarGson = gson.fromJson(jsonData, BuyCarGson.class);
                return buyCarGson.buyCarDetialList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
