package utilitaire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UtilDownload {
    
    public static String[] getFolderNameFromFoldersPath(String[] foldersList){
        String[] foldersName = new String[foldersList.length];
        for(int i=0;i<foldersList.length;i++){
            Path path = Paths.get(foldersList[i]);
            String pathRelativized = path.toString().replace("\\","/");
            String[] splitBySeparator = pathRelativized.split("/");
            foldersName[i] = splitBySeparator[splitBySeparator.length-1];
        }
        return foldersName;
    }
    public static File zipDirectories(String[] foldersList) throws IOException{
        String[] foldersName = getFolderNameFromFoldersPath(foldersList);
        return zipDirectories(foldersList,foldersName);
    }
    public static File zipDirectories(String[] foldersList,String[] foldersName) throws IOException{
        File tempZipFile = File.createTempFile("tempZip", ".zip");
        try (FileOutputStream fos = new FileOutputStream(tempZipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
                for(int i = 0;i<foldersList.length;i++) {
                    String folderPath = foldersList[i];
                    String folderName = foldersName[i];
                    Path sourceDir = Paths.get(folderPath);
                    addFolderToZip(sourceDir, sourceDir, zos,folderName);
                }
            zos.finish();
        }
        return tempZipFile;
    }

    public static void addFolderToZip(Path rootDir, Path sourceDir, ZipOutputStream zos,String folderName) throws IOException {
        if(sourceDir.toFile().exists()){
            Files.walk(sourceDir)
            .filter(path -> !Files.isDirectory(path))
            .forEach(path -> {
                try {
                    Path relativePath = rootDir.relativize(path);
                    ZipEntry zipEntry = new ZipEntry(folderName+"/"+relativePath.toString().replace("\\", "/"));
                    zos.putNextEntry(zipEntry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
