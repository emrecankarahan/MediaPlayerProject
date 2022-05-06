package com.example.homework1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework1.databinding.RecyclerRowBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    ArrayList<Music> musicArrayList;
    Context context;
    Activity activity;
    public MusicAdapter(ArrayList<Music> musicArrayList, Context context, Activity activity){
    this.musicArrayList = musicArrayList;
    this.context = context;
    this.activity=activity;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MusicHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, @SuppressLint("RecyclerView") int position) {
        Music music = musicArrayList.get(position);
        holder.binding.recyclerViewTextView.setText(musicArrayList.get(position).getSongName());
        holder.binding.recyclerViewTextView2.setText(musicArrayList.get(position).getArtistName() +" ("+convertToMMSS(musicArrayList.get(position).getDuration())+")");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.getInstance().reset();
                MusicPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",musicArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        holder.binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse((musicArrayList.get(position)).getSource()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(Intent.createChooser(intent,"Choose an app for sharing .mp3 audio."));

            }
        });
        holder.binding.imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(),"Deleted("+musicArrayList.get(position).getSongName()+")",Toast.LENGTH_SHORT).show();
                musicArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }
    public class MusicHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;
        public MusicHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) %TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) %TimeUnit.MINUTES.toSeconds(1));
    }
}

