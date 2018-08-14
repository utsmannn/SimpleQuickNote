package com.kucingapes.simplequicknote.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kucingapes.simplequicknote.Model.ModelHistory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedList {

    private String PREFS_NAME = "ganteng";
    private String KEY = "listhistory";


    public ArrayList<ModelHistory> getFavorites(Context context) {
        SharedPreferences settings;
        List<ModelHistory> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);

        if (settings.contains(KEY)) {
            String jsonFavorites = settings.getString(KEY, null);
            Gson gson = new Gson();

            Type type = new TypeToken<List<ModelHistory>>() {}.getType();
            favorites = gson.fromJson(jsonFavorites, type);

        } else
            return null;

        return (ArrayList<ModelHistory>) favorites;
    }

    public void addFavorite(Context context, ModelHistory item) {
        List<ModelHistory> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();

        favorites.add(item);
        saveFavorites(context, favorites);
    }

    public void clearFavorite(Context context) {
        List<ModelHistory> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();

        favorites.clear();
        saveFavorites(context, favorites);
    }


    public void saveFavorites(Context context, List<ModelHistory> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(KEY, jsonFavorites);
        editor.apply();
    }

    public void removeFavorite(Context context, int pos) {
        List<ModelHistory> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();

        favorites.remove(pos);
        saveFavorites(context, favorites);
    }


    public List<ModelHistory> getObject(Context context, List<ModelHistory> list) {
        list = getFavorites(context);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

}
