package com.kucingapes.simplequicknote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListNotes extends AppCompatActivity {

    private List<ModelHistory> stringList;
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

        adapter = new HistoryAdapter(stringList, this);
        Collections.reverse(stringList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
