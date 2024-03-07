package com.miracle.ffmpeg.Creation;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.miracle.ffmpeg.R;

public class OpenVideo extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_video);

        videoView = findViewById(R.id.videoView);
        String videoPath = getIntent().getStringExtra("VIDEO_PATH");

        if (videoPath.isEmpty() || videoPath == null) {
            Toast.makeText(this, "Video not found!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            videoView.setVideoURI(Uri.parse(videoPath));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }

}