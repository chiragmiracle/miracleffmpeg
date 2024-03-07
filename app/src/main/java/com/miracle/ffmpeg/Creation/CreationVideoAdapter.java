package com.miracle.ffmpeg.Creation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miracle.ffmpeg.R;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class CreationVideoAdapter extends RecyclerView.Adapter<CreationVideoAdapter.ViewHolder> {
    Activity activity;
    ArrayList<FileModel> videoArrayList;

    public CreationVideoAdapter(Activity activity, ArrayList<FileModel> videoArrayList) {
        this.activity = activity;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.delete0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Are you sure you want to delete?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == -1 && new File(videoArrayList.get(position).videoPath).delete()) {
                            videoArrayList.remove(position);
                            notifyItemRemoved(position);
                            if (videoArrayList.size() < 1) {
                                Toast.makeText(activity, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, OpenVideo.class);
            intent.putExtra("VIDEO_PATH", videoArrayList.get(position).videoPath);
            activity.startActivity(intent);
        });
        holder.title0.setText(videoArrayList.get(position).getVideoTitle());
        Glide.with(activity).load(videoArrayList.get(position).videoPath).thumbnail(0.1f).centerCrop().into(holder.image0);
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete0;
        ImageView image0;
        TextView title0;

        public ViewHolder(View view) {
            super(view);
            title0 = view.findViewById(R.id.title);
            delete0 = view.findViewById(R.id.delete);
            image0 = view.findViewById(R.id.image);

        }
    }
}
