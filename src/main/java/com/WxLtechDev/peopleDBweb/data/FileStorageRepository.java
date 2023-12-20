package com.WxLtechDev.peopleDBweb.data;

import com.WxLtechDev.peopleDBweb.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FileStorageRepository {

    @Value("${STORAGE_FOLDER}")
    private String storageFolder;
    public void save(final String originalFilename, final InputStream inputStream){
        // Combine the original file name to the path.
        // And normalize the path, that means eliminate the special character.
        try {
            Path filePath = Path.of(storageFolder).resolve(originalFilename).normalize();
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            throw new StorageException();
        }
    }

    public Resource findByName(String fileName){
        try {
            Path filePath = Path.of(storageFolder).resolve(fileName).normalize();
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new StorageException();
        }
    }

    public void deleteAllByName(final Collection<String> fileNames) {
        List<String> collect = fileNames.stream().filter(f -> f != null).collect(Collectors.toList());
        for (String fileName: collect) {
            Path filePath = Path.of(storageFolder).resolve(fileName).normalize();
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
    }
}
