package com.example.homework1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework1.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    ArrayList<ArrayList<Music>> playlistArrayList;
    Context context;

    public PlaylistAdapter(ArrayList<ArrayList<Music>> playlistArrayList, Context context) {
        this.playlistArrayList = playlistArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, @SuppressLint("RecyclerView") int position) {
        ArrayList<Music> music = playlistArrayList.get(position);
        holder.binding.recyclerViewTextView.setText("Playlist No:"+(position+1));
        holder.binding.recyclerViewTextView2.setVisibility(View.INVISIBLE);
        holder.binding.imageView7.setVisibility(View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MusicListActivity.class);
                intent.putExtra("GEL",playlistArrayList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.binding.imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(),"Deleted("+holder.binding.recyclerViewTextView.getText()+")",Toast.LENGTH_SHORT).show();
                playlistArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder {
        private RecyclerRowBinding binding;

        public PlaylistHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
