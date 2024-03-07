package com.miracle.ffmpeg.Creation;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miracle.ffmpeg.R;

import java.io.File;
import java.util.ArrayList;

public class StoreSaveVideo extends AppCompatActivity {

    RecyclerView recycler_view;
    LinearLayout nodatamsg;
    public ArrayList<FileModel> videoArrayList;
    CreationVideoAdapter creationVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_save_video);

        recycler_view = findViewById(R.id.recycler_view);
        nodatamsg = findViewById(R.id.nodatamsg);

        videoList();
    }

    private void videoList() {
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        videoArrayList = new ArrayList<>();
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        nodatamsg.setVisibility(View.GONE);
        File[] listFiles = new File(getFilesDir() + "/Miracle_ffmpeg").listFiles();
        if (listFiles == null || listFiles.length < 1) {
            nodatamsg.setVisibility(View.VISIBLE);
        } else {
            for (File file : listFiles) {
                if (file.isFile()) {
                    FileModel fileModel = new FileModel();
                    fileModel.setVideoTitle(file.getName());
                    fileModel.setVideoPath(file.getAbsolutePath());
                    fileModel.setVideoDuration(String.valueOf(getvideofiledration(this, file.getAbsolutePath())));
                    this.videoArrayList.add(fileModel);
                }
            }
        }
        creationVideoAdapter = new CreationVideoAdapter(this, videoArrayList);
        recycler_view.setAdapter(creationVideoAdapter);
    }

    public static long getvideofiledration(Context context, String str) {
        try {
            if (!str.contains(".mp4") && !str.contains(".mp3")) {
                if (!str.contains(".m4a")) {
                    return 0;
                }
            }
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, Uri.fromFile(new File(str)));
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            mediaMetadataRetriever.release();
            return parseLong;
        } catch (Exception unused) {
            return 0;
        }
    }

}