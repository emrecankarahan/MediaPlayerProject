package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homework1.databinding.ActivityCreatePlaylistBinding;

import java.util.ArrayList;
public class CreatePlaylistActivity extends AppCompatActivity {
    private ActivityCreatePlaylistBinding binding;
    ArrayList<Music> musicArrayList = new ArrayList<>();
    ArrayList<ArrayList<Music>> playlistArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePlaylistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        playlistArrayList =(ArrayList<ArrayList<Music>>) getIntent().getSerializableExtra("BUYUK");
        musicArrayList= (ArrayList<Music>) getIntent().getSerializableExtra("YOLLA");

        if(musicArrayList.size() == 0){
            Toast.makeText(CreatePlaylistActivity.this, "No music is existed!!", Toast.LENGTH_SHORT).show();
        }else{
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setAdapter(new MusicAdapter2(playlistArrayList,binding,musicArrayList,getApplicationContext()));
        }


    }
}