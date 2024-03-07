package com.miracle.ffmpeg.Creation;

public class FileModel {
    String videoDuration;
    String videoPath;
    String videoTitle;

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String str) {
        this.videoTitle = str;
    }

    public String getVideoDuration() {
        return this.videoDuration;
    }

    public void setVideoDuration(String str) {
        this.videoDuration = str;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String str) {
        this.videoPath = str;
    }
}
