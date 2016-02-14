/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.utilities;

import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.shell.Shell;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class FileOperations {

    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator;
    private static final Logger LOGGER = Logger.getLogger(FileOperations.class.getName());

    public static File getPdf(JobDTO jobDTO) {
        Path path = getPathToPdf(jobDTO);
        if (Files.exists(path)) {
            return path.toFile();
        }
        return null;
    }

    public static Path getPathToPdf(JobDTO jobDTO) {
        Path path = null;
        for (JobFileDTO fileDTO : jobDTO.getFiles()) {
            if (fileDTO.isMainTex()) {
                
                String nameBase = fileDTO.getName().split("\\.(?=[^\\.]+$)")[0];
                String pdfFile = nameBase.concat(".pdf");
                path = getPathToFile(jobDTO.getDirectory(), pdfFile);
            }
        }
        return path;
    }

    public static String getLog(JobDTO jobDTO) throws IOException {
        Path path = getPathToLog(jobDTO);
		if (path != null) {
            if (Files.exists(path)) {
            	return readFile(path);
        	}
        }
        return "";
    }

    public static String getHtmlLog(JobDTO jobDTO) throws IOException {
        String log = "";
        Path path = getPathToLog(jobDTO);
        if (path != null) {
            if (Files.exists(path)) {
                log = readFile(path);

                /* mark warnings */
                ArrayList<String> warnings = RegEx.findWarnings(log);
                for (String line : warnings) {
                    log = log.replace(line, "<span class=\"warning\">" + line + "</span>");
                }

                /* mark errors */
                ArrayList<String> errors = RegEx.findErrors(log);
                for (String line : errors) {
                    log = log.replace(line, "<span class=\"error\">" + line + "</span>");
                }

                /* add linebreaks */
                log = log.replaceAll("\n", "<br>\n");
            }
        }
        return log;
    }

    public static Path getPathToLog(JobDTO jobDTO) {
        Path path = null;
        for (JobFileDTO fileDTO : jobDTO.getFiles()) {
            if (fileDTO.isMainTex()) {
                path = getPathToFile(jobDTO.getDirectory(), fileDTO.getName().replaceAll(".tex", ".log"));
            }
        }
        return path;
    }

    public static Path getPathToFile(String directory, String filename) {
        return Paths.get(UPLOAD_DIRECTORY + directory, filename);
    }

    public static Path saveFile(String directory, String filename, InputStream inputStream) throws IOException {
        /* get correct path for file */
        Path path = getPathToFile(directory, filename);

        int read = 0;
        byte[] bytes = new byte[1024];

        /* create directories */
        Files.createDirectories(path.getParent());

        /* write to file */
        OutputStream outputStream = new FileOutputStream(path.toFile());
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();

        return path;
    }

    public static String readFile(Path path) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
    }

    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }
}
