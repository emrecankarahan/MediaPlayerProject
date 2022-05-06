package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.homework1.databinding.ActivityMusicListBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicListActivity extends AppCompatActivity {
    ArrayList<Music> musicArrayList;
    private ActivityMusicListBinding binding;
    byte[] image;
    Bitmap bitmap;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        super.onCreate(savedInstanceState);
        binding = ActivityMusicListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        musicArrayList = new ArrayList<>();

        if(checkPermission() == false){
            requestPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        if(getIntent().hasExtra("GEL")){
            musicArrayList = (ArrayList<Music>) getIntent().getSerializableExtra("GEL");
            binding.musicListActivityButton.setVisibility(View.INVISIBLE);
        }else{
            Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,MediaStore.Audio.Media.DISPLAY_NAME);
            while(cursor.moveToNext()){
                if(cursor.getString(0).contains(".mp3")){
                    Music songData = new Music(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                    musicArrayList.add(songData);
                }
            }
        }


        if(musicArrayList.size() != 0){
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setAdapter(new MusicAdapter(musicArrayList,getApplicationContext(),MusicListActivity.this));
        }else{
            Toast.makeText(MusicListActivity.this,"No music is existed !.",Toast.LENGTH_SHORT).show();
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new MusicAdapter(musicArrayList,getApplicationContext(),MusicListActivity.this));
        binding.musicListActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicListActivity.this,PlaylistActivity.class);
                intent.putExtra("GEL",musicArrayList);
                startActivity(intent);
            }
        });
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MusicListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MusicListActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MusicListActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MusicListActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }


    /*
    @Override
    protected void onResume() {
        super.onResume();
        if(binding.recyclerView!=null){
            binding.recyclerView.setAdapter(new MusicAdapter(musicArrayList,getApplicationContext()));
        }
    }*/
}