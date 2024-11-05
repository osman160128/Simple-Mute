package com.example.simplemute.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplemute.Mute;
import com.example.simplemute.databinding.ItemMuteBinding;
import com.example.simplemute.fragments.MuteListFragment;
import com.example.simplemute.util.OnToggleMuteListener;

import java.util.ArrayList;
import java.util.List;

public class MuteRecylerViewAdapter extends RecyclerView.Adapter<MuteViewHolder> {

    List<Mute> mutes;
   OnToggleMuteListener listener;

    ItemMuteBinding itemMuteBinding;
    public MuteRecylerViewAdapter(OnToggleMuteListener listener) {
        this.mutes = new ArrayList<Mute>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemMuteBinding=ItemMuteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MuteViewHolder(itemMuteBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MuteViewHolder holder, int position) {
        Mute mute = mutes.get(position);
        holder.bind(mute,listener);

    }

    @Override
    public int getItemCount() {
        return mutes.size();
    }

    public void setMutes(List<Mute> mutes) {
    this.mutes = mutes;
    notifyDataSetChanged();
    }
}
