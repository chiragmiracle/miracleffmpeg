package com.miracleffmpeg.video;

import android.content.Context;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;


public class Videoffmpeg {

    public static String executeCutVideoCommand(Context context, String inputFilePath, String outputDirPath, String startTime, String duration, String outputFileName) {
        File outputDir = new File(outputDirPath);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, outputFileName + inputFilePath.substring(inputFilePath.lastIndexOf(".")));
        String outputPath = outputFile.getAbsolutePath();
        String[] ffmpegCommand = new String[]{"-y", "-i", inputFilePath, "-ss", startTime, "-t", duration, "-c", "copy", outputPath};

        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            Log.e("FFmpeg", "Error executing FFmpeg command: " + execute);
            return null;
        }
    }

    public static String executeCutVideoCommand(Context context, String filePath,String startTime, String duration) {
        File outputDir = new File(context.getFilesDir() + "/" + "Miracle_ffmpeg");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, "Cutter" + System.currentTimeMillis() + filePath.substring(filePath.lastIndexOf(".")));
        String outputPath = outputFile.getAbsolutePath();
        String[] ffmpegCommand = new String[]{"-y", "-i", filePath, "-ss", startTime, "-t", duration, "-c", "copy", outputPath};

        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            Log.e("FFmpeg", "Error executing FFmpeg command: " + execute);
            return null;
        }
    }

    public static String executeSlowMotionCommand(Context context, String filePath, String startTime, String duration) {
        File outputDir = new File(context.getFilesDir() + "/" + "Miracle_ffmpeg");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, "video" + System.currentTimeMillis() + filePath.substring(filePath.lastIndexOf(".")));
        String outputPath = outputFile.getAbsolutePath();
        int startMillis = Integer.parseInt(startTime);
        int durationMillis = Integer.parseInt(duration);
        double speedFactor = 0.5;

        String[] ffmpegCommand = new String[]{"-y", "-i", filePath,
                "-filter_complex",
                "[0:v]trim=0:" + startMillis / 1000 + ",setpts=PTS-STARTPTS[v1];" +
                        "[0:a]atrim=0:" + (startMillis / 1000) + ",asetpts=PTS-STARTPTS[a1];" +
                        "[0:v]trim=" + startMillis / 1000 + ":" + durationMillis / 1000 + ",setpts=PTS-STARTPTS," +
                        "setpts=" + (1 / speedFactor) + "*PTS[v2];" +
                        "[0:a]atrim=" + (startMillis / 1000) + ":" + (durationMillis / 1000) + ",asetpts=PTS-STARTPTS," +
                        "atempo=" + speedFactor + "[a2];" +
                        "[0:v]trim=" + (durationMillis / 1000) + ",setpts=PTS-STARTPTS[v3];" +
                        "[0:a]atrim=" + (durationMillis / 1000) + ",asetpts=PTS-STARTPTS[a3];" +
                        "[v1][a1][v2][a2][v3][a3]concat=n=3:v=1:a=1",
                "-b:v", "2097k",
                "-vcodec", "mpeg4",
                "-crf", "0",
                "-preset", "superfast",
                outputPath};

        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            return null;
        }
    }

    public static String executeReverseCommand(Context context, String filePath) {
        File outputDir = new File(context.getFilesDir() + "/" + "Miracle_ffmpeg");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, "Reverse" + System.currentTimeMillis() + ".mp4");
        String outputPath = outputFile.getAbsolutePath();
        String[] ffmpegCommand = new String[]{"-y", "-i", filePath, "-vf", "reverse", "-b:v", "2097k", "-vcodec", "mpeg4", "-crf", "0", "-preset", "superfast", outputPath};
        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            return null;
        }
    }

    public static String executeFastCommand(Context context, String filePath) {
        File outputDir = new File(context.getFilesDir() + "/" + "Miracle_ffmpeg");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, "Fast" + System.currentTimeMillis() + ".mp4");
        String outputPath = outputFile.getAbsolutePath();

        String[] ffmpegCommand = new String[]{
                "-y", "-i", filePath,
                "-vf", "setpts=0.5*PTS",  // Fast motion effect by halving the presentation time stamp
                "-af", "atempo=2.0",  // Double the audio speed
                "-preset", "ultrafast", "-crf", "28",  // Preset and quality settings
                outputPath
        };
        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            return null;
        }
    }

    public static String executeVideoCompressCommand(Context context, String filePath) {
        File outputDir = new File(context.getFilesDir() + "/" + "Miracle_ffmpeg");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outputFile = new File(outputDir, "compressed_video" + System.currentTimeMillis() + ".mp4");
        String outputPath = outputFile.getAbsolutePath();
        String[] ffmpegCommand = new String[]{
                "-y", "-i", filePath,
                "-c:v", "libx264", "-preset", "superfast", "-b:v", "1.5M", // Video codec, preset, and bitrate
                "-c:a", "aac", "-b:a", "256k", // Audio codec and bitrate
                outputPath
        };

        int execute = FFmpeg.execute(ffmpegCommand);
        if (execute == Config.RETURN_CODE_SUCCESS) {
            return outputPath;
        } else {
            return null;
        }
    }


}
