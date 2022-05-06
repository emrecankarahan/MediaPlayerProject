package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.example.homework1.databinding.ActivityMusicPlayerBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    private ActivityMusicPlayerBinding binding;
    ArrayList<Music> musicArrayList;
    Music currentMusic;
    boolean isPaused;
    MediaPlayer mediaPlayer = MusicPlayer.getInstance();
    int x=0;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    Bitmap bitmap;
    byte[] image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        musicArrayList = (ArrayList<Music>)getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    binding.textViewStart.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                }
                new Handler().postDelayed(this,100);

            }
        });
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setResourcesWithMusic(){
        currentMusic = musicArrayList.get(MusicPlayer.currentIndex);
        binding.textViewSong.setText(currentMusic.getTitle());
        binding.textViewFinish.setText(convertToMMSS(currentMusic.getDuration()));
        mediaMetadataRetriever.setDataSource(currentMusic.getSource());
        image=mediaMetadataRetriever.getEmbeddedPicture();
        bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        binding.imageSong.setImageBitmap(bitmap);
        binding.imageStart.setOnClickListener(view -> playMusic());
        binding.imageViewStop.setOnClickListener(view -> playStop());
        binding.imagePause.setOnClickListener(view ->pauseMusic());
        binding.imageNext.setOnClickListener(view -> playNextSong());
        binding.imagePrevious.setOnClickListener(view -> playPreviousSong());
        playMusic();

    }
    private void playStop(){
        mediaPlayer.reset();
        isPaused = false;
    }
    private void playMusic(){
        if(isPaused == true){
            mediaPlayer.start();
            isPaused = false;
        }
        else{
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(currentMusic.getSource());
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPaused = false;
                binding.seekBar.setProgress(0);
                binding.seekBar.setMax(mediaPlayer.getDuration());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void pauseMusic(){
        mediaPlayer.pause();
        isPaused = true;

    }

    private void playNextSong(){
        if(MusicPlayer.currentIndex == musicArrayList.size()-1)
            return;
        MusicPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MusicPlayer.currentIndex == 0)
            return;
        MusicPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) %TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) %TimeUnit.MINUTES.toSeconds(1));
    }
}
