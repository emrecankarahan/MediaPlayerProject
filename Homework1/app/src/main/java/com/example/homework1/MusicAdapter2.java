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

import com.example.homework1.databinding.ActivityCreatePlaylistBinding;
import com.example.homework1.databinding.RecyclerRowBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicAdapter2 extends RecyclerView.Adapter<MusicAdapter2.MusicHolder2> {
    ArrayList<ArrayList<Music>> playlistArrayList = new ArrayList<>();
    ArrayList<Music> musicArrayList = new ArrayList<>();
    ArrayList<Music> tempMusicArrayList = new ArrayList<>();
    Context context;
    ActivityCreatePlaylistBinding binding;
    ArrayList<Music> createArrayList = new ArrayList<>();

    public MusicAdapter2(ArrayList<ArrayList<Music>> playlistArrayList,ActivityCreatePlaylistBinding binding,ArrayList<Music> musicArrayList,Context context){
        this.playlistArrayList = playlistArrayList;
        this.binding = binding;
        this.musicArrayList = musicArrayList;
        this.context = context;
        for(int i=0;i<musicArrayList.size();i++){
            tempMusicArrayList.add(musicArrayList.get(i));
        }
    }

    @NonNull
    @Override
    public MusicHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MusicAdapter2.MusicHolder2(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter2.MusicHolder2 holder, @SuppressLint("RecyclerView") int position) {
        Music music = musicArrayList.get(position);
        holder.binding.recyclerViewTextView.setText(musicArrayList.get(position).getSongName());
        holder.binding.recyclerViewTextView2.setText(musicArrayList.get(position).getArtistName() +" ("+convertToMMSS(musicArrayList.get(position).getDuration())+")");
        holder.binding.imageView7.setVisibility(View.INVISIBLE);
        holder.binding.imageView8.setVisibility(View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(),"Adding("+musicArrayList.get(position).getSongName()+")",Toast.LENGTH_SHORT).show();
                createArrayList.add(musicArrayList.get(position));
                musicArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        binding.musicListActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PlaylistActivity.class);
                intent.putExtra("CREATED",createArrayList);
                intent.putExtra("BUYUK",playlistArrayList);
                intent.putExtra("GEL",tempMusicArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }
    public class MusicHolder2 extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;

        public MusicHolder2(RecyclerRowBinding binding) {
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
