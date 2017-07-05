#!/bin/sh

mkdir -p ./log_output/
java -Xmx2G -jar anomaly_detection.jar ./log_input/batch_log.json ./log_input/stream_log.json ./log_output/flagged_purchases.json
