package com.example.expensemate.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.R;
import com.example.expensemate.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
    private List<Map.Entry<String, Record>> recordList;
    private OnItemClickListener onItemClickListener;

    public RecordsAdapter(Map<String, Record> recordList) {
        this.recordList = new ArrayList<>(recordList.entrySet());
    }

    public void setRecordList(Map<String, Record> recordList) {
        this.recordList = new ArrayList<>(recordList.entrySet());
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Record record, String recordId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position).getValue();
        holder.bind(record);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(record, record.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewPrice;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }

        public void bind(Record record) {
            textViewName.setText(record.getName());
            textViewType.setText(record.getType());
            textViewPrice.setText(String.valueOf(record.getPrice()));
        }
    }
}