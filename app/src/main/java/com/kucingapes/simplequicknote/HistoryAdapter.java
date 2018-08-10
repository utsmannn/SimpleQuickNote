package com.kucingapes.simplequicknote;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    private List<ModelHistory> stringList;
    private Context context;
    private View view;

    public HistoryAdapter(List<ModelHistory> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    public HistoryAdapter(){
        super();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        final ModelHistory modelHistory = stringList.get(i);
        holder.item.setText(modelHistory.getText());
        holder.date.setText(modelHistory.getDate());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(modelHistory.getDate(), modelHistory.getText());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cardView.setCardBackgroundColor(modelHistory.getColor());
    }

    public boolean contains(List<ModelHistory> list, String result) {
        for (ModelHistory model : list) {
            if (model.getText().equals(result)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView item, date;
        CardView cardView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_history);
            date = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
