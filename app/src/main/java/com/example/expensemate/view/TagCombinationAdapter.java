package com.example.expensemate.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagCombinationAdapter extends RecyclerView.Adapter<TagCombinationAdapter.TagCombinationViewHolder> {
    private List<Map.Entry<String, Float>> tagCombinations;
    public TagCombinationAdapter(Map<String, Float> tagCombinations) {
        this.tagCombinations = tagCombinations != null
                ? new ArrayList<>(tagCombinations.entrySet())
                : new ArrayList<>();
    }

    public void setTagCombinations(Map<String, Float> tagCombinations) {
        this.tagCombinations = new ArrayList<>(tagCombinations.entrySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagCombinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_tag_combination, null);
        return new TagCombinationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagCombinationViewHolder holder, int position) {
        Map.Entry<String, Float> entry = tagCombinations.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return tagCombinations.size();
    }

    public static class TagCombinationViewHolder extends RecyclerView.ViewHolder {
        private TextView tagCombo, tagPrice;

        public TagCombinationViewHolder(View itemView) {
            super(itemView);
            tagCombo = itemView.findViewById(R.id.tagComboText);
            tagPrice = itemView.findViewById(R.id.amountText);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Map.Entry<String, Float> entry) {
            tagCombo.setText(entry.getKey());
            tagPrice.setText(String.format("%.2f", entry.getValue()));
        }
    }
}
