package dev.alexissdev.crudapp.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ImageUrlFactory {

    private static final Logger LOGGER = Logger.getLogger(ImageUrlFactory.class.getName());
    private static final String EMPTY = "";
    private static final String IMAGE_URL = "/uploads/%s";

    /**
     * Generates the URL for an uploaded image after saving it to the server's file system.
     * If the specified upload directory does not exist, it is created.
     * The image is stored with a timestamp included in the file name to ensure uniqueness.
     *
     * @param imageFile the {@link MultipartFile} containing the image to be uploaded
     * @return the URL of the uploaded image as a {@link String}
     * @throws IOException if there is an error during file creation or writing
     */

    public static String getImageUrl(MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    LOGGER.info("Directory is created!");
                }
            }
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return String.format(IMAGE_URL, fileName);
        }

        return EMPTY;
    }
}
