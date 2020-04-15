package com.example.dict.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dict.databinding.ListDictBinding;
import com.example.dict.room.entity.Word;
import java.util.List;

public class DictAdapter extends RecyclerView.Adapter<DictAdapter.VH> {
    private Context mContext;
    private List<Word> mList;
    public DictAdapter(Context ctx, List<Word> list) {
        mContext = ctx;
        mList = list;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListDictBinding binding = ListDictBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.binding.tvWord.setText(mList.get(position).getWord());
        holder.binding.tvTranslate.setText(mList.get(position).getTranslate());
        holder.binding.btnPlayMusic.setOnClickListener((v -> {
            Log.d("TAG","onclick"+position);
            onItemClickListence.onClick(position);
        }));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ListDictBinding binding;
        VH(@NonNull ListDictBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
    private OnItemClickListence onItemClickListence;

    public void setOnItemClickListence(OnItemClickListence listence) {
        onItemClickListence = listence;
    }

    public interface OnItemClickListence {
        void onClick(int index);
    }
}
