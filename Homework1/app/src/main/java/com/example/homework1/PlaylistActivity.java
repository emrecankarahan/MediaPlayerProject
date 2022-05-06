package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homework1.databinding.ActivityMusicPlayerBinding;
import com.example.homework1.databinding.ActivityPlaylistBinding;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    public ActivityPlaylistBinding binding;
    ArrayList<Music> musicArrayList = new ArrayList<>();
    ArrayList<Music> createdArrayList = new ArrayList<>();
    ArrayList<ArrayList<Music>> playlistArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if(getIntent().hasExtra("GEL"))
        musicArrayList = (ArrayList<Music>) getIntent().getSerializableExtra("GEL");
        if(getIntent().hasExtra("CREATED"))
        createdArrayList = (ArrayList<Music>) getIntent().getSerializableExtra("CREATED");
        if(getIntent().hasExtra("BUYUK"))
        playlistArrayList = (ArrayList<ArrayList<Music>>) getIntent().getSerializableExtra("BUYUK");

        System.out.println("Size"+musicArrayList.size()+createdArrayList.size()+playlistArrayList.size());

        if(createdArrayList.size() != 0 ) {
            System.out.println("Gelenler ---> "+ createdArrayList.size());
            playlistArrayList.add(createdArrayList);
            System.out.println("Size playlist -->"+playlistArrayList.size());
        }

        if(playlistArrayList.size() != 0){
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setAdapter(new PlaylistAdapter(playlistArrayList,getApplicationContext()));
        }else{
            Toast.makeText(PlaylistActivity.this,"No playlist exists!",Toast.LENGTH_SHORT).show();
        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistActivity.this,CreatePlaylistActivity.class);
                intent.putExtra("YOLLA",musicArrayList);
                intent.putExtra("BUYUK",playlistArrayList);
                startActivity(intent);

            }
        });
    }
}