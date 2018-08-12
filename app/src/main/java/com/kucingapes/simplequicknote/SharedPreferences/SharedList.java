package com.kucingapes.simplequicknote.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kucingapes.simplequicknote.Model.ModelHistory;

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

            ModelHistory[] favoriteItems = gson.fromJson(jsonFavorites,
                    ModelHistory[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
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

    private void saveFavorites(Context context, List<ModelHistory> favorites) {
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
        ArrayList<ModelHistory> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(pos);
            saveFavorites(context, favorites);
        }
    }

}
