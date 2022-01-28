<p align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/1037/1037316.png" width="150" title="hover text">
</p>

# File Import Helper
File Import Helper is a file transfer utility to automate transfering particular files from given source and destination using batch jobs

## To Build the Jar

```
./gradlew build 

```

## To Run the Jar

```

java -jar -Dspring.profiles.active=prod file-transfer-helper-0.0.1-SNAPSHOT.jar "{Source Folder}" "{Target Folder}"

```


### To Add Multiple Source Folder

```

java -jar -Dspring.profiles.active=prod file-transfer-helper-0.0.1-SNAPSHOT.jar "{Source Folder1,Source Folder2}" "{Target Folder}"

```
