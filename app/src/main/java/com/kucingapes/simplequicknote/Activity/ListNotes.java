package com.kucingapes.simplequicknote.Activity;

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

        if (stringList.size() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }

        adapter = new HistoryAdapter(stringList, this);
        Collections.reverse(stringList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
