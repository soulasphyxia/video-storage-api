#!/bin/bash

# Входной файл
input_file="$1"
output_path="$2"

# Получение разрешения входного видео
resolution=$(ffprobe -v error -select_streams v:0 -show_entries stream=width,height -of csv=p=0:s=x "$input_file")

# Разбор ширины и высоты
IFS='x' read -r width height <<< "$resolution"

# Параметры для разрешений и битрейтов
case "$width" in
    1920)
        resolutions=("1920:1080" "1280:720" "854:480" "640:360" "426:240" "256:144")
        bitrates=("5M" "3M" "1.5M" "800k" "400k" "200k")
        bufsize=("10M" "6M" "3M" "1.6M" "800k" "400k")
        ;;
    1280)
        resolutions=("1280:720" "854:480" "640:360" "426:240" "256:144")
        bitrates=("3M" "1.5M" "800k" "400k" "200k")
        bufsize=("6M" "3M" "1.6M" "800k" "400k")
        ;;
    854)
        resolutions=("854:480" "640:360" "426:240" "256:144")
        bitrates=("1.5M" "800k" "400k" "200k")
        bufsize=("3M" "1.6M" "800k" "400k")
        ;;
    640)
        resolutions=("640:360" "426:240" "256:144")
        bitrates=("800k" "400k" "200k")
        bufsize=("1.6M" "800k" "400k")
        ;;
    426)
        resolutions=("426:240" "256:144")
        bitrates=("400k" "200k")
        bufsize=("800k" "400k")
        ;;
    256)
        resolutions=("256:144")
        bitrates=("200k")
        bufsize=("400k")
        ;;
    *)
        echo "Unsupported resolution: $resolution"
        exit 1
        ;;
esac

# Создание фильтров и карт для видео и аудио потоков
filter_complex="[0:v]split=${#resolutions[@]}"
for i in "${!resolutions[@]}"; do
    filter_complex+="[v$(($i + 1))]"
done
filter_complex+=";"
for i in "${!resolutions[@]}"; do
    filter_complex+=" [v$(($i + 1))]scale=${resolutions[$i]}[v${i}out];"
done

map=""
var_stream_map=""
for i in "${!resolutions[@]}"; do
    map+=" -map [v${i}out] -c:v:$i libx264 -x264-params nal-hrd=cbr:force-cfr=1 -b:v:$i ${bitrates[$i]} -maxrate:v:$i ${bitrates[$i]} -minrate:v:$i ${bitrates[$i]} -bufsize:v:$i ${bufsize[$i]} -preset slow -g 48 -sc_threshold 0 -keyint_min 48"
    map+=" -map a:0 -c:a:$i aac -b:a:$i 96k -ac 2"
    var_stream_map+="v:$i,a:$i "
done

# Удаление последнего пробела в var_stream_map
var_stream_map=$(echo $var_stream_map | sed 's/ $//')

# Запуск FFmpeg команды
ffmpeg -i "$input_file" -filter_complex "$filter_complex" $map -f hls -hls_time 2 -hls_playlist_type vod -hls_flags independent_segments -hls_segment_type mpegts -hls_segment_filename "${output_path}stream_%v_data%02d.ts" -master_pl_name "master.m3u8" -var_stream_map "$var_stream_map" "${output_path}stream_%v.m3u8"

