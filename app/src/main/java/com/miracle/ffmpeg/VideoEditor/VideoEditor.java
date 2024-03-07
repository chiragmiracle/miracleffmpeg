package com.miracle.ffmpeg.VideoEditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.miracle.ffmpeg.Creation.StoreSaveVideo;
import com.miracle.ffmpeg.R;
import com.miracle.ffmpeg.Utils.CustomProgressbar;
import com.miracleffmpeg.video.Videoffmpeg;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@SuppressLint("WrongConstant")
public class VideoEditor extends AppCompatActivity {
    Context context;
    TextView current;
    double current_pos;
    Handler handler;
    String inputfilepath;
    boolean isVisible = true;
    public SeekBar lineseekbar;
    Handler mHandler;
    int maxvalue = 0;
    int minvalue = 0;
    ImageView next;
    ImageView pause;
    ImageView prev;
    RangeSlider range;
    RelativeLayout relativeLayout;
    SeekBar seekBar;
    LinearLayout showProgress;
    TextView total;
    double total_duration;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txtprocess;
    VideoView videoView;
    private int video_index;
    TextView video_title;
    TextView texttitle;
    String VideoPath;
    String category;
    String executeCutVideoCommand;
    LinearLayout lay_range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editor);
        CustomProgressbar.showProgressBar(this, false);
        VideoPath = getIntent().getStringExtra("VideoPath");
        category = getIntent().getStringExtra("Category");
        Log.e("CHIRAG", "onCreate -> VideoPath :" + VideoPath);
        video_title = findViewById(R.id.video_title);
        lay_range = findViewById(R.id.lay_range);
        texttitle = findViewById(R.id.texttitle);

        if (category.equals("Cutter")) {
            texttitle.setText("Video Cutter");
        } else if (category.equals("Slow")) {
            texttitle.setText("Video Slow Motion");
        } else if (category.equals("Reverse")) {
            lay_range.setVisibility(View.GONE);
            texttitle.setText("Video Reverse");
        } else if (category.equals("Fast")) {
            lay_range.setVisibility(View.GONE);
            texttitle.setText("Video Fast");
        } else if (category.equals("Compress")) {
            lay_range.setVisibility(View.GONE);
            texttitle.setText("Video Compress");
        }


        this.context = this;
        this.range = findViewById(R.id.range_slider);
        this.inputfilepath = VideoPath;
        this.txtprocess = findViewById(R.id.btnmainconvert);
        this.lineseekbar = findViewById(R.id.lineseekbar);
        this.txt1 = findViewById(R.id.txt1);
        this.txt2 = findViewById(R.id.txt2);
        this.txt3 = findViewById(R.id.txt3);
        int i = (int) getvideofiledration(this, this.inputfilepath);
        if (i < 2000) {
            Toast.makeText(context, "Already short video!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        this.maxvalue = i;
        this.minvalue = 0;
        this.range.setTickCount(i);
        this.range.setRangeIndex(0, i);
        this.range.setRangeChangeListener(new RangeSlider.OnRangeChangeListener() {
            public void onRangeChange(RangeSlider rangeSlider, int i, int i2) {
                maxvalue = i2;
                minvalue = i;
                TextView textView = txt1;
                textView.setText(timeConversion(minvalue));
                TextView textView2 = txt3;
                textView2.setText(timeConversion(maxvalue));
                TextView textView3 = txt2;
                textView3.setText(timeConversion(maxvalue - minvalue));
                videoView.seekTo(minvalue);
            }
        });
        this.txt3.setText(timeConversion(this.maxvalue));
        String path = inputfilepath;
        String filename = path.substring(path.lastIndexOf("/") + 1);
        video_title.setText(filename);
        setVideo();
        setFrames(i);

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

    public void BtnProcess(View view) {
        CustomProgressbar.showProgressBar(this, false);
        if (this.videoView.isPlaying()) {
            this.videoView.stopPlayback();
        }
        ConvertProcess();
    }

    public void ConvertProcess() {
        this.txtprocess.setEnabled(false);
        if (inputfilepath.substring(inputfilepath.lastIndexOf(".")).equals(".mp4")) {
            int i = minvalue;
            int i2 = maxvalue - minvalue;
            Log.e("CHIRAG", "i: " + i);
            Log.e("CHIRAG", "i2: " + i2);

            if (category.equals("Cutter")) {
                executeCutVideoCommand = Videoffmpeg.executeCutVideoCommand(context, inputfilepath, timeConversion1(i), timeConversion1(i2));
                Log.e("CHIRAG", "executeCutVideoCommand: " + executeCutVideoCommand);
            } else if (category.equals("Slow")) {
                long startTimeMillis = getMilliseconds(timeConversion1(i));
                long durationMillis = getMilliseconds(timeConversion1(i2));
                executeCutVideoCommand = Videoffmpeg.executeSlowMotionCommand(context, inputfilepath, String.valueOf(startTimeMillis), String.valueOf(durationMillis));
                Log.e("CHIRAG", "executeCutVideoCommand: " + executeCutVideoCommand);
            } else if (category.equals("Reverse")) {
                executeCutVideoCommand = Videoffmpeg.executeReverseCommand(context, inputfilepath);
                Log.e("CHIRAG", "executeCutVideoCommand: " + executeCutVideoCommand);
            } else if (category.equals("Fast")) {
                executeCutVideoCommand = Videoffmpeg.executeFastCommand(context, inputfilepath);
                Log.e("CHIRAG", "executeCutVideoCommand: " + executeCutVideoCommand);
            } else if (category.equals("Compress")) {
                executeCutVideoCommand = Videoffmpeg.executeVideoCompressCommand(context, inputfilepath);
                Log.e("CHIRAG", "executeCutVideoCommand: " + executeCutVideoCommand);
            }

            if (executeCutVideoCommand != null) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        txtprocess.setEnabled(true);
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, "Video Download Successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VideoEditor.this, StoreSaveVideo.class));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        txtprocess.setEnabled(true);
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, "Error in Conversion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    txtprocess.setEnabled(true);
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, "File should be mp4", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String timeConversion1(long j) {
        return getDate(j);
    }

    public String getDate(long j) {
        Date date = new Date(j);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public long getMilliseconds(String timeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = dateFormat.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setFrames(int i) {
        int i2 = i / 10;
        String videoUri = VideoPath;
        LinearLayout linearLayout = findViewById(R.id.linframe);
        for (int i3 = 1; i3 < 10; i3++) {
            i2 *= i3;
            ImageView imageView = (ImageView) linearLayout.getChildAt(i3 - 1);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoUri);
            Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(1000000 * i2, 2);
            if (!(frameAtTime == null || imageView == null)) {
                imageView.setImageBitmap(frameAtTime);
            }
            CustomProgressbar.hideProgressBar();
        }
    }

    public void setVideo() {
        this.videoView = findViewById(R.id.videoview);
        this.prev = findViewById(R.id.prev);
        this.next = findViewById(R.id.next);
        this.pause = findViewById(R.id.pause);
        this.seekBar = findViewById(R.id.seekbar);
        this.current = findViewById(R.id.current);
        this.total = findViewById(R.id.total);
        this.showProgress = findViewById(R.id.showProgress);
        this.relativeLayout = findViewById(R.id.conplayer);
        this.mHandler = new Handler();
        this.handler = new Handler();
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                CustomProgressbar.hideProgressBar();
            }
        });
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                setVideoProgress();
            }
        });
        playVideo(this.video_index);
        setPause();
        hideLayout();
    }

    public void playVideo(int i) {
        try {
            this.videoView.setVideoPath(VideoPath);
            this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pause.setVisibility(0);
                }
            });
            this.video_index = i;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVideoProgress() {
        this.current_pos = this.videoView.getCurrentPosition();
        double duration = this.videoView.getDuration();
        this.total_duration = duration;
        this.total.setText(timeConversion((long) duration));
        this.current.setText(timeConversion((long) this.current_pos));
        this.seekBar.setMax((int) this.total_duration);
        this.lineseekbar.setMax((int) this.total_duration);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    TextView textView = current;
                    textView.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    lineseekbar.setProgress((int) current_pos);
                    handler2.postDelayed(this, 500);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
        handler2.postDelayed(new Runnable() {
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    Log.d("LogData", "" + current_pos);
                    if (current_pos > ((double) maxvalue)) {
                        videoView.pause();
                        pause.setImageResource(R.drawable.play_icon);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                handler2.postDelayed(this, 50);
            }
        }, 50);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
        CustomProgressbar.hideProgressBar();
    }

    public void setPause() {
        this.pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pause.setImageResource(R.drawable.play_icon);
                    return;
                }
                videoView.seekTo(minvalue);
                videoView.start();
                pause.setImageResource(R.drawable.pause_icon);
            }
        });
    }

    public String timeConversion(long j) {
        int i = (int) j;
        int i2 = i / 3600000;
        int i3 = (i / 60000) % 60000;
        int i4 = (i % 60000) / 1000;
        if (i2 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public void hideLayout() {
        final Runnable r0 = new Runnable() {

            public void run() {
                if (videoView.isPlaying()) {
                    pause.setVisibility(8);
                }
                showProgress.setVisibility(8);
                isVisible = false;
            }
        };
        this.handler.postDelayed(r0, 5000);
        this.relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mHandler.removeCallbacks(r0);
                if (isVisible) {
                    if (!videoView.isPlaying()) {
                        pause.setVisibility(0);
                    } else {
                        pause.setVisibility(8);
                    }
                    showProgress.setVisibility(8);
                    isVisible = false;
                    return;
                }
                pause.setVisibility(0);
                showProgress.setVisibility(0);
                mHandler.postDelayed(r0, 5000);
                isVisible = true;
            }
        });
    }
}