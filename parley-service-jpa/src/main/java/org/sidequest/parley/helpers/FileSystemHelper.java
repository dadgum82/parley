package org.sidequest.parley.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FileSystemHelper {

    private static final Logger logger = Logger.getLogger(FileSystemHelper.class.getName());

    // Save a file to a specified directory and return the path of the saved file
    public String saveFile(MultipartFile file, String directoryPath) {
        if (file == null || directoryPath == null) {
            logger.warning("File or directoryPath is null.");
            throw new IllegalArgumentException("File and directoryPath cannot be null.");
        }

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean dirsCreated = directory.mkdirs();
                if (!dirsCreated) {
                    logger.warning("Failed to create directories: " + directoryPath);
                    throw new IOException("Failed to create directories: " + directoryPath);
                }
            }

            File destinationFile = new File(directory, file.getOriginalFilename());
            file.transferTo(destinationFile);
            logger.info("File saved successfully: " + destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save file: " + file.getOriginalFilename(), e);
            throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
        }
    }

    // Delete a file
    public boolean deleteFile(String filePath) {
        if (filePath == null) {
            logger.warning("filePath is null.");
            throw new IllegalArgumentException("filePath cannot be null.");
        }

        try {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                logger.info("File deleted successfully: " + filePath);
                return true;
            } else {
                logger.warning("File not found or could not be deleted: " + filePath);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete file: " + filePath, e);
            throw new RuntimeException("Failed to delete file: " + filePath, e);
        }
    }

    // List all files in a directory
    public List<String> listFiles(String directoryPath) {
        if (directoryPath == null) {
            logger.warning("directoryPath is null.");
            throw new IllegalArgumentException("directoryPath cannot be null.");
        }

        List<String> fileList = new ArrayList<>();
        try {
            File directory = new File(directoryPath);
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        fileList.add(file.getAbsolutePath());
                    }
                }
                logger.info("Listed files in directory: " + directoryPath);
            } else {
                logger.warning("Directory not found or is not a directory: " + directoryPath);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to list files in directory: " + directoryPath, e);
            throw new RuntimeException("Failed to list files in directory: " + directoryPath, e);
        }
        return fileList;
    }

    // Check if a file or directory exists
    public boolean exists(String path) {
        if (path == null) {
            logger.warning("path is null.");
            throw new IllegalArgumentException("path cannot be null.");
        }

        try {
            File file = new File(path);
            boolean exists = file.exists();
            logger.info("File or directory " + (exists ? "exists" : "does not exist") + ": " + path);
            return exists;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to check existence of path: " + path, e);
            throw new RuntimeException("Failed to check existence of path: " + path, e);
        }
    }

    // Create a directory
    public boolean createDirectory(String directoryPath) {
        if (directoryPath == null) {
            logger.warning("directoryPath is null.");
            throw new IllegalArgumentException("directoryPath cannot be null.");
        }

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                logger.info("Directory " + (created ? "created" : "not created") + ": " + directoryPath);
                return created;
            } else {
                logger.warning("Directory already exists: " + directoryPath);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create directory: " + directoryPath, e);
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

    // Read the content of a file as a string
    public String readFileContent(String filePath) {
        if (filePath == null) {
            logger.warning("filePath is null.");
            throw new IllegalArgumentException("filePath cannot be null.");
        }

        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            logger.info("Read file content: " + filePath);
            return content;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read file content: " + filePath, e);
            throw new RuntimeException("Failed to read file content: " + filePath, e);
        }
    }

    // Write content to a file
    public void writeFileContent(String filePath, String content) {
        if (filePath == null || content == null) {
            logger.warning("filePath or content is null.");
            throw new IllegalArgumentException("filePath and content cannot be null.");
        }

        try {
            Path path = Paths.get(filePath);
            Files.writeString(path, content);
            logger.info("Wrote content to file: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write file content: " + filePath, e);
            throw new RuntimeException("Failed to write file content: " + filePath, e);
        }
    }
}
