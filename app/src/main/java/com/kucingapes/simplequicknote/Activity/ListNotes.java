package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.kucingapes.simplequicknote.Adapter.HistoryAdapter;
import com.kucingapes.simplequicknote.Model.ModelHistory;
import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.SharedPreferences.SharedList;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListNotes extends AppCompatActivity {

    private ArrayList<ModelHistory> stringList;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        RecyclerView recyclerView = findViewById(R.id.list_note);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LayoutMarginDecoration marginDecoration = new LayoutMarginDecoration(2, 10);
        marginDecoration.setPadding(recyclerView, 10, 10, 10, 10);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(marginDecoration);
        setItemList(recyclerView);

    }

    private void setItemList(RecyclerView recyclerView) {
        SharedList sharedList = new SharedList();
        stringList = sharedList.getFavorites(getApplicationContext());

        if (stringList == null) {
            stringList = new ArrayList<>();
        }

        if (stringList.size() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }



        adapter = new HistoryAdapter(stringList, this);

        if (stringList.size() > 0) {
            sortListByDate(stringList);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void sortListByDate(ArrayList<ModelHistory> arrayList) {
        Collections.sort(arrayList, new Comparator<ModelHistory>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public int compare(ModelHistory modelHistory, ModelHistory t1) {
                String dateString1 = modelHistory.getDate();
                String dateString2 = t1.getDate();

                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd MMMM YYYY / hh:mm").parse(dateString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = new SimpleDateFormat("dd MMMM YYYY / hh:mm").parse(dateString2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long milis1 = 0;
                if (date1 != null) {
                    milis1 = date1.getTime();
                }
                long milis2 = 0;
                if (date2 != null) {
                    milis2 = date2.getTime();
                }

                return milis1 > milis2 ? -1 : 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_backup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup_menu:
                startActivity(new Intent(this, OptionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
