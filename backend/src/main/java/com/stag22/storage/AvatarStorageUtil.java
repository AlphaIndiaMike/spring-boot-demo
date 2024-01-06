package com.stag22.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

import com.stag22.exception.ResourceNotFoundException;


public class AvatarStorageUtil {
    private static final String IMAGE_DIRECTORY = "avatars";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB, for example

    public static Path getAvatarDirectoryPath() throws IOException {
        Path rootPath = Paths.get("").toAbsolutePath();
        Path avatarDirectory = rootPath.resolve(IMAGE_DIRECTORY);
        
        if (!Files.exists(avatarDirectory)) {
            Files.createDirectories(avatarDirectory);
        }

        return avatarDirectory;
    }
    
    @SuppressWarnings("unused")
	private static void deleteDirectory(File directory) {

        // if the file is directory or not
        if(directory.isDirectory()) {
          File[] files = directory.listFiles();

          // if the directory contains any file
          if(files != null) {
            for(File file : files) {

              // recursive call if the subdirectory is non-empty
              deleteDirectory(file);
            }
          }
        }

        if(directory.delete()) {
          System.out.println(directory + " is deleted");
        }
        else {
          System.out.println("Directory not deleted");
        }
      }
    
    public static void drop_avatars() throws IOException {
    	deleteDirectory(getAvatarDirectoryPath().toFile());
    }
    

    public static void uploadCustomerImage(Integer customerId, MultipartFile file) throws IOException {
        // Check if the file is an image
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the limit");
        }

        Path avatarDirectory = getAvatarDirectoryPath();
        
        // Use a secure method to construct the filename
        String filename = constructSafeFilename(customerId, file.getOriginalFilename());
        Path filePath = avatarDirectory.resolve(filename);

        // Save the file, avoiding overwriting existing files
        saveFileAvoidingOverwrite(file, filePath);
    }

    public static byte[] getCustomerImage(Integer customerId) throws IOException {
        Path avatarDirectory = getAvatarDirectoryPath();
        File folder = new File(avatarDirectory.toString());
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith(customerId.toString() + "_")) {
                    Path filePath = avatarDirectory.resolve(file.getName());
                    return Files.readAllBytes(filePath);
                }
            }
        }

        throw new ResourceNotFoundException(
            "Avatar for customer with id [%s] not found".formatted(customerId));
    }

    private static String constructSafeFilename(Integer customerId, String originalFilename) {
        String safeFilename = Paths.get(originalFilename).getFileName().toString(); // Avoid path traversal
        return customerId + "_" + safeFilename;
    }

    private static void saveFileAvoidingOverwrite(MultipartFile file, Path filePath) throws IOException {
        int counter = 0;
        while (Files.exists(filePath)) {
            String newFilename = String.format("%s_%d", filePath.getFileName().toString(), ++counter);
            filePath = filePath.resolveSibling(newFilename);
        }
        Files.copy(file.getInputStream(), filePath);
    }
}
