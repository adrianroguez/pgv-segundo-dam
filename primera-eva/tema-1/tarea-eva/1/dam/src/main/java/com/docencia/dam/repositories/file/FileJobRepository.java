package com.docencia.dam.repositories.file;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.docencia.dam.repositories.interfaces.JobRepository;

@Repository
public class FileJobRepository implements JobRepository {
    private static Logger logger = LoggerFactory.getLogger(FileJobRepository.class);
    static String fileName;
    static Path path;

    public FileJobRepository() {
        if (fileName == null) {
            fileName = "mis_procesos.txt";
        }
        fileName = "mis_procesos.txt";
        URL resource = getClass().getClassLoader().getResource(fileName);
        path = Paths.get(fileName);
    }

    @Override
    public boolean save(String content) {
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            logger.error("Se ha producido un error almacenando el fichero");
        }
        return false;
    }
}
