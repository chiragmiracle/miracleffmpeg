package com.miracle.ffmpeg;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miracle.ffmpeg.Creation.StoreSaveVideo;
import com.miracle.ffmpeg.Utils.CustomProgressbar;
import com.miracle.ffmpeg.VideoEditor.VideoEditor;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_creation).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StoreSaveVideo.class));
        });
        findViewById(R.id.btn_videoCutter).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 101);
        });
        findViewById(R.id.btn_VideoSlow).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 102);
        });
        findViewById(R.id.btn_VideoReverse).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 103);
        });
        findViewById(R.id.btn_Fast).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 104);
        });
        findViewById(R.id.btn_Compress).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 105);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                if (data != null) {
                    Uri videoUri = data.getData();
                    if (videoUri != null) {
                        String inputFilePath = getPathFromUri(videoUri);
                        if (inputFilePath != null) {
                            CustomProgressbar.showProgressBar(this, false);
                            Intent intent = new Intent(this, VideoEditor.class);
                            intent.putExtra("VideoPath", inputFilePath);
                            intent.putExtra("Category", "Cutter");
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Failed to retrieve video path", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to retrieve video URI", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        if (requestCode == 102) {
            if (data != null) {
                Uri videoUri = data.getData();
                if (videoUri != null) {
                    String inputFilePath = getPathFromUri(videoUri);
                    if (inputFilePath != null) {
                        CustomProgressbar.showProgressBar(this, false);
                        Intent intent = new Intent(this, VideoEditor.class);
                        intent.putExtra("Category", "Slow");
                        intent.putExtra("VideoPath", inputFilePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to retrieve video path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve video URI", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 103) {
            if (data != null) {
                Uri videoUri = data.getData();
                if (videoUri != null) {
                    String inputFilePath = getPathFromUri(videoUri);
                    if (inputFilePath != null) {
                        CustomProgressbar.showProgressBar(this, false);
                        Intent intent = new Intent(this, VideoEditor.class);
                        intent.putExtra("Category", "Reverse");
                        intent.putExtra("VideoPath", inputFilePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to retrieve video path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve video URI", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 104) {
            if (data != null) {
                Uri videoUri = data.getData();
                if (videoUri != null) {
                    String inputFilePath = getPathFromUri(videoUri);
                    if (inputFilePath != null) {
                        CustomProgressbar.showProgressBar(this, false);
                        Intent intent = new Intent(this, VideoEditor.class);
                        intent.putExtra("Category", "Fast");
                        intent.putExtra("VideoPath", inputFilePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to retrieve video path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve video URI", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 105) {
            if (data != null) {
                Uri videoUri = data.getData();
                if (videoUri != null) {
                    String inputFilePath = getPathFromUri(videoUri);
                    if (inputFilePath != null) {
                        CustomProgressbar.showProgressBar(this, false);
                        Intent intent = new Intent(this, VideoEditor.class);
                        intent.putExtra("Category", "Compress");
                        intent.putExtra("VideoPath", inputFilePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to retrieve video path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve video URI", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }


}