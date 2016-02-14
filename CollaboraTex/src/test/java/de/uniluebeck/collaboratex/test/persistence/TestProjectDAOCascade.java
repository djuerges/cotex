/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.collaboratex.test.persistence;

import de.uniluebeck.collaboratex.dao.ProjectDAO;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import java.util.Objects;
import java.util.logging.Logger;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test all operations on files owned by project are cascaded
 * 
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestProjectDAOCascade extends TestBase {
    
    private final ProjectDAO dao = new ProjectDAO();

    @BeforeMethod
    public void setUpMethod() throws Exception {
        /* clean db for each test */
        for(ProjectDTO dto : dao.findAll()){
            dao.delete(dto.getId());
        }
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testAddFile(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* retrieve saved DTO back from DB */
        ProjectDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* add file and update in DB */
        retrievedDTO.addFile(new ProjectFileDTO(TestDataProvider.generateBlobKey(), "testFile", "testParentFolder", 23443, System.currentTimeMillis(), true));
        dao.update(retrievedDTO);
        
        /* retrieve from DB and assert added file was persisted */
        retrievedDTO = dao.findById(dto.getId());
        assertNotSame("file was not deleted in database!", retrievedDTO.getFiles().size(), dto.getFiles().size());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testUpdateFile(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);
        
        /* retrieve saved DTO back from DB */
        ProjectDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* change name of file and update in DB */
        ProjectFileDTO fileDTO = retrievedDTO.getFiles().iterator().next();
        fileDTO.setName("recentlychangedname");
        dao.update(retrievedDTO);
        
        /* retrieve from DB and assert name change was persisted */
        retrievedDTO = dao.findById(dto.getId());
        for(ProjectFileDTO retrievedFileDTO : retrievedDTO.getFiles()){
            if(Objects.equals(retrievedFileDTO.getId(), dto.getId())){
                assertTrue("file was not updated in database!", retrievedDTO.getFiles().iterator().next().getName().equals("recentlychangedname"));
            }
        }
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testDeleteFile(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* retrieve saved DTO back from DB */
        ProjectDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* delete file from job and update in DB */
        retrievedDTO.getFiles().remove(retrievedDTO.getFiles().iterator().next());
        dao.update(retrievedDTO);
        
        /* retrieve from DB and assert number of files is decreased by one */
        retrievedDTO = dao.findById(dto.getId());
        assertNotSame("file was not deleted in database!", retrievedDTO.getFiles().size(), dto.getFiles().size());
    }
}