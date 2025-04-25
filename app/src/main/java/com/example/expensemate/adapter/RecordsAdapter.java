package com.example.expensemate.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.R;
import com.example.expensemate.model.DateRecord;
import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_RECORD = 1;
    private List<Object> itemList;
    private OnItemClickListener onItemClickListener;

    public RecordsAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String recordId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_DATE) {
            View view = inflater.inflate(R.layout.item_date, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_record, parent, false);
            return new RecordViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Object item = itemList.get(position);
        if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).bind((DateRecord) item);
        } else if (holder instanceof RecordViewHolder) {
            ((RecordViewHolder) holder).bind((Record) item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof DateRecord) {
            return VIEW_TYPE_DATE;
        } else {
            return VIEW_TYPE_RECORD;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class DateViewHolder extends RecordViewHolder {
        TextView textViewDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DateRecord dateRecord) {
            Date date = dateRecord.date;
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
            String dayString = dayFormat.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            textViewDate.setText(dayString + " " + getDayOfWeek(dayOfWeek));
        }

        private String getDayOfWeek(int dayOfWeek) {
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    return "星期日";
                case Calendar.MONDAY:
                    return "星期一";
                case Calendar.TUESDAY:
                    return "星期二";
                case Calendar.WEDNESDAY:
                    return "星期三";
                case Calendar.THURSDAY:
                    return "星期四";
                case Calendar.FRIDAY:
                    return "星期五";
                case Calendar.SATURDAY:
                    return "星期六";
                default:
                    return "";
            }
        }
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewPrice;
        ChipGroup chipGroupTags;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            chipGroupTags = itemView.findViewById(R.id.chipGroupTags);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(((Record) itemList.get(position)).getId());
                }
            });
        }

        public void bind(Record record) {
            textViewName.setText(record.getName());
            textViewType.setText(record.getType());
            textViewPrice.setText(String.valueOf(record.getPrice()));

            chipGroupTags.removeAllViews();
            for (Tag tag : record.getTags()) {
                Chip chip = new Chip(itemView.getContext());
                chip.setText(tag.getName());
                chip.setCheckable(false);
                chip.setClickable(true);
                chipGroupTags.addView(chip);
            }
        }
    }
}