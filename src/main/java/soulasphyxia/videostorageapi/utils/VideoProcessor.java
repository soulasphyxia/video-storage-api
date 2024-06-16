package soulasphyxia.videostorageapi.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoProcessor {

    private int getHeight(String url) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(
                "ffprobe",
                "-v",
                "error",
                "-select_streams",
                "v:0",
                "-show_entries",
                "stream=height",
                "-of",
                "default=nw=1:nk=1",
                url
        );
        Process process = pb.start();
        StringBuilder output = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        )){
            String readLine;
            while((readLine = reader.readLine()) != null){
                output.append(readLine);
            }
        }
        int exitCode = process.waitFor();
        System.out.printf("Process %d exited with code %d\n", process.pid(),exitCode);
        return Integer.parseInt(output.toString().trim());
    }

    public static String mp4Script(String url) throws InterruptedException, IOException {
        Pattern regex = Pattern.compile("(?<=8000\\/)[^\\/]+", Pattern.MULTILINE);
        Matcher matcher = regex.matcher(url);
        String bucketName = "";
        if(matcher.find()){
            bucketName = matcher.group(0);
        }
        String tempDir = String.format("/tmp/%s/",bucketName);
        new File(tempDir).mkdir();
        ProcessBuilder pb = new ProcessBuilder("./ffmpeg_script.sh",url,tempDir);
        Process process = pb.start();
        StringBuilder error = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        )){
            String readLine;
            while((readLine = reader.readLine()) != null){
                error.append(readLine);
            }
        }
        System.out.println(error);
        int exitCode = process.waitFor();
        System.out.printf("Process %d exited with code %d, dir: %s %n",process.pid(), exitCode, tempDir);
        return tempDir;
    }

    public static String mp4ToHls(String url) throws IOException, InterruptedException {
        Pattern regex = Pattern.compile("(?<=8000\\/)[^\\/]+", Pattern.MULTILINE);
        Matcher matcher = regex.matcher(url);
        String bucketName = "";
        if(matcher.find()){
            bucketName = matcher.group(0);
        }
        String tempDir = String.format("/tmp/%s/",bucketName);
        new File(tempDir).mkdir();

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(
                "ffmpeg",
                "-i",
                url,
                "-filter_complex",
                "[0:v]split=3[v1][v2][v3]; [v1]copy[v1out]; [v2]scale=w=1280:h=720[v2out]; [v3]scale=w=640:h=360[v3out]",
                "-map",
                "[v1out]",
                "-c:v:0",
                "libx264",
                "-x264-params",
                "nal-hrd=cbr:force-cfr=1",
                "-b:v:0",
                "5M",
                "-maxrate:v:0",
                "5M",
                "-minrate:v:0",
                "5M",
                "-bufsize:v:0",
                "10M",
                "-preset",
                "slow",
                "-g",
                "48",
                "-sc_threshold",
                "0",
                "-keyint_min",
                "48",
                "-map",
                "[v2out]",
                "-c:v:1",
                "libx264",
                "-x264-params",
                "\"nal-hrd=cbr:force-cfr=1\"",
                "-b:v:1",
                "3M",
                "-maxrate:v:1",
                "3M",
                "-minrate:v:1",
                "3M",
                "-bufsize:v:1",
                "3M",
                "-preset",
                "slow",
                "-g",
                "48",
                "-sc_threshold",
                "0",
                "-keyint_min",
                "48",
                "-map",
                "[v3out]",
                "-c:v:2",
                "libx264",
                "-x264-params",
                "\"nal-hrd=cbr:force-cfr=1\"",
                "-b:v:2",
                "1M",
                "-maxrate:v:2",
                "1M",
                "-minrate:v:2",
                "1M",
                "-bufsize:v:2",
                "1M",
                "-preset",
                "slow",
                "-g",
                "48",
                "-sc_threshold",
                "0",
                "-keyint_min",
                "48",
                "-map",
                "a:0",
                "-c:a:0",
                "aac",
                "-b:a:0",
                "96k",
                "-ac",
                "2",
                "-map",
                "a:0",
                "-c:a:1",
                "aac",
                "-b:a:1",
                "96k",
                "-ac",
                "2",
                "-map",
                "a:0",
                "-c:a:2",
                "aac",
                "-b:a:2",
                "48k",
                "-ac",
                "2",
                "-f",
                "hls",
                "-hls_time",
                "2",
                "-hls_playlist_type",
                "vod",
                "-hls_flags",
                "independent_segments",
                "-hls_segment_type",
                "mpegts",
                "-hls_segment_filename",
                tempDir+"stream_%v_data%02d.ts",//stream_%v_data%02d.ts
                "-master_pl_name",
                "master.m3u8",//master.m3u8
                "-var_stream_map",
                "v:0,a:0 v:1,a:1 v:2,a:2",
                tempDir+"stream_%v.m3u8" //stream_%v.m3u8
        );
        Process process = pb.start();
        StringBuilder error = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        )){
            String readLine;
            while((readLine = reader.readLine()) != null){
                error.append(readLine);
            }
        }
        System.out.println(error);
        int exitCode = process.waitFor();
        System.out.printf("Process %d exited with code %d, dir: %s %n",process.pid(), exitCode, tempDir);
        return tempDir;
    }

    public String getSplit(int height){
        switch (height){
            case 1080 -> {
                return "[0:v]split=3[v1][v2][v3];[v1]copy[v1";
            }
            case 720 -> {
                return "720";
            }
            case 360 -> {
                return "360";
            }
            default -> {
                return "123123";
            }
        }
    }

}
