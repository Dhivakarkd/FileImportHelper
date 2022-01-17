#!/usr/bin/bash
export source_name="$HOME/Documents/Source_Test_folder"
export target_name="$HOME/Documents/Target/"
./gradlew bootRun --args='--spring.profiles.active=dev'

