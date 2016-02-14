/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template projectfile, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test.utilities;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.dto.UserDTO;
import de.uniluebeck.collaboratex.entity.Project;
import de.uniluebeck.collaboratex.entity.ProjectFile;
import de.uniluebeck.collaboratex.entity.User;
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
                                                "/src/test/java/de/uniluebeck/collaboratex/test/utilities";
    
    public static Key generateKey(String entityName) {
        return KeyFactory.createKey(entityName, (long) (Math.random() * 9000 + 1000));
    }
    
    public static BlobKey generateBlobKey() {
        return new BlobKey("R2aV_meJWzkhdQTbTg" + (long) (Math.random() * 9000 + 1000));
    }

    private static Set<Key> generateKeys(String entityName, int num) {
        Set<Key> set = new HashSet<>();
        for (int i = 0; i < num; i++) {
            set.add(generateKey(entityName));
        }
        return set;
    }

    @DataProvider(name = "user-entity")
    public static Object[][] userEntityDataProvider() {
        return new Object[][]{
            {new User("testuser", generateKeys(Project.class.getSimpleName(), 1), generateKeys(Project.class.getSimpleName(), 3))}
        };
    }

    @DataProvider(name = "user-dto")
    public static Object[][] userDTODataProvider() {
        return new Object[][]{
            {new UserDTO("testuser", generateKeys(Project.class.getSimpleName(), 1), generateKeys(Project.class.getSimpleName(), 3))}
        };
    }
    
    @DataProvider(name = "user-dtos")
    public static Object[][] userDTOsDataProvider() {
        return new Object[][]{
            {new ArrayList<UserDTO>() {
                {
                    add(new UserDTO("testuser1", generateKeys(Project.class.getSimpleName(), 1), generateKeys(Project.class.getSimpleName(), 3)));
                    add(new UserDTO("testuser2", generateKeys(Project.class.getSimpleName(), 1), generateKeys(Project.class.getSimpleName(), 3)));
                    add(new UserDTO("testuser3", generateKeys(Project.class.getSimpleName(), 1), generateKeys(Project.class.getSimpleName(), 3)));
                }
            }}
        };
    }

    @DataProvider(name = "project-entity")
    public static Object[][] projectEntityDataProvider() {
        return new Object[][]{
            {new Project("testFile", generateProjectFileEntities(3))}
        };
    }

    @DataProvider(name = "project-dto")
    public static Object[][] projectDTODataProvider() {
        return new Object[][]{
            {new ProjectDTO("testFile", generateProjectFileDTOs(3))}
        };
    }

    @DataProvider(name = "project-dtos")
    public static Object[][] projectDTOsDataProvider() {
        return new Object[][]{
            {new ArrayList<ProjectDTO>() {
                {
                    add(new ProjectDTO("testFile", generateProjectFileDTOs(3)));
                    add(new ProjectDTO("testFile", generateProjectFileDTOs(3)));
                    add(new ProjectDTO("testFile", generateProjectFileDTOs(3)));
                }
            }}
        };
    }

    @DataProvider(name = "projectfile-entity")
    public static Object[][] projectfileEntityDataProvider() {
        return new Object[][]{
            {new ProjectFile(generateBlobKey(), "testFile", "testParentFolder", 23443, System.currentTimeMillis(), true)}
        };
    }

    @DataProvider(name = "projectfile-dto")
    public static Object[][] projectfileDTODataProvider() {
        return new Object[][]{
            {new ProjectFileDTO(generateBlobKey(), "testFile", "testParentFolder", 23443, System.currentTimeMillis(), true)}
        };
    }

    @DataProvider(name = "projectfile-dtos")
    public static Object[][] projectfileDTOsDataProvider() {
        return new Object[][]{
            {new ArrayList<ProjectFileDTO>() {
                {
                    add(new ProjectFileDTO(generateBlobKey(), "testFile1", "testParentFolder", 23443, System.currentTimeMillis(), true));
                    add(new ProjectFileDTO(generateBlobKey(), "testFile2", "testParentFolder", 23443, System.currentTimeMillis(), true));
                    add(new ProjectFileDTO(generateBlobKey(), "testFile3", "testParentFolder", 23443, System.currentTimeMillis(), true));
                }
            }}
        };
    }

    private static Set<ProjectFileDTO> generateProjectFileDTOs(int num) {
        Set<ProjectFileDTO> list = new HashSet<>();
        for (int i = 0; i < num; i++) {
            list.add(generateProjectFileDTO());
        }
        return list;
    }

    private static Set<ProjectFile> generateProjectFileEntities(int num) {
        Set<ProjectFile> set = new HashSet<>();
        for (int i = 0; i < num; i++) {
            set.add(generateProjectFileEntity());
        }
        return set;
    }

    private static ProjectFileDTO generateProjectFileDTO() {
        return new ProjectFileDTO(generateBlobKey(), "testFile", "testContentType", 23443, System.currentTimeMillis(), true);
    }

    private static ProjectFile generateProjectFileEntity() {
        return new ProjectFile(generateBlobKey(), "testFile", "testContentType", 23443, System.currentTimeMillis(), true);
    }
    
    @DataProvider(name = "tex-file")
    public static Object[][] texFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.tex")}
        };
    }
    
    @DataProvider(name = "broken-tex-file")
    public static Object[][] brokenTexFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "broken.tex")}
        };
    }
    
    @DataProvider(name = "pdf-file")
    public static Object[][] pdfFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "sample.pdf")}
        };
    }
    
    @DataProvider(name = "broken-pdf-file")
    public static Object[][] brokenPdfFileDataProvider() throws URISyntaxException {
        return new Object[][]{
            {new File(DATA_DIR, "broken.pdf")}
        };
    }
}