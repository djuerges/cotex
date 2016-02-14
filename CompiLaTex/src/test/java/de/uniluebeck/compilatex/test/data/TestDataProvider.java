/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.test.data;

import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.entity.JobFile;
import de.uniluebeck.compilatex.entity.Job;
import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.testng.annotations.DataProvider;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestDataProvider {
    
    private final static String DATA_DIR = System.getProperty("user.dir") + 
                                                "/src/test/java/de/uniluebeck/compilatex/test/data/";

    public static Long generateId() {
        return (long) (Math.random() * 9000 + 1000);
    }

    @DataProvider(name = "id")
    public static Object[][] idDataProvider() {
        return new Object[][]{
            {generateId()}, {generateId()}, {generateId()}
        };
    }

    @DataProvider(name = "tex-files")
    public static Object[][] texFilesDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.tex")}, {new File(DATA_DIR, "broken.tex")}
        };
    }
    
    @DataProvider(name = "tex-file")
    public static Object[][] texFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.tex")}
        };
    }
    
    @DataProvider(name = "tex-file-log")
    public static Object[][] texLogDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.log")}
        };
    }

    @DataProvider(name = "broken-tex-file")
    public static Object[][] brokenTexFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "broken.tex")}
        };
    }
    
    @DataProvider(name = "broken-tex-log")
    public static Object[][] brokenTexLogDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "broken.log")}
        };
    }

    @DataProvider(name = "pdf-file")
    public static Object[][] pdfFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.pdf")}
        };
    }
    
    @DataProvider(name = "fake-application-names")
    public static Object[][] fakeApplicationNamesDataProvider() {
        return new Object[][]{
            {"nonexistingapplication"}, {"yetanotherfalse"}, {"chromium"}
        };
    }

    @DataProvider(name = "latex-environments")
    public static Object[][] latexEnvironmentsDataProvider() {
        return new Object[][]{
            {LatexEnvironment.LUATEX}, {LatexEnvironment.PDFLATEX}, {LatexEnvironment.XETEX}
        };
    }

    @DataProvider(name = "file-entity")
    public static Object[][] fileEntityDataProvider() {
        return new Object[][]{
            {new JobFile(generateId(), "testFile", "testParentFolder", System.currentTimeMillis(), true)}
        }; 
    }

    @DataProvider(name = "file-dto")
    public static Object[][] fileDTODataProvider() {
        return new Object[][]{
            {new JobFileDTO(generateId(), "testFile", "testParentFolder", System.currentTimeMillis(), true)}
        };
    }

    @DataProvider(name = "job-entity")
    public static Object[][] jobEntityDataProvider() {
        return new Object[][]{
            {new Job(generateId(), "testFile", generateFileEntities(3))}
        };
    }

    @DataProvider(name = "job-dto")
    public static Object[][] jobDTODataProvider() {
        return new Object[][]{
            {new JobDTO(generateId(), "testFile", generateFileDTOs(3))}
        };
    }

    @DataProvider(name = "job-dtos")
    public static Object[][] jobDTOsDataProvider() {
        return new Object[][]{
            {
                new ArrayList<JobDTO>() {{
                    add(new JobDTO(generateId(), "testFile", generateFileDTOs(3)));
                    add(new JobDTO(generateId(), "testFile", generateFileDTOs(3)));
                    add(new JobDTO(generateId(), "testFile", generateFileDTOs(3)));
                }}
            }
        };
    }

    private static Set<JobFileDTO> generateFileDTOs(int num) {
        Set<JobFileDTO> list = new HashSet<>();
        for (int i = 0; i < num; i++) {
            list.add(generateFileDTO());
        }
        return list;
    }

    private static Set<JobFile> generateFileEntities(int num) {
        Set<JobFile> set = new HashSet<>();
        for (int i = 0; i < num; i++) {
            set.add(generateFileEntity());
        }
        return set;
    }

    private static JobFileDTO generateFileDTO() {
        return new JobFileDTO(generateId(), "testFile", "testParentFolder", System.currentTimeMillis(), true);
    }

    private static JobFile generateFileEntity() {
        return new JobFile(generateId(), "testFile", "testParentFolder", System.currentTimeMillis(), true);
    }
}
