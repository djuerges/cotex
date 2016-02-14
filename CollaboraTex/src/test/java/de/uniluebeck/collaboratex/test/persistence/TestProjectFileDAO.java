/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test.persistence;

import de.uniluebeck.collaboratex.dao.ProjectFileDAO;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import java.util.List;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestProjectFileDAO extends TestBase {

    private final ProjectFileDAO dao = new ProjectFileDAO();
    
    @BeforeMethod
    public void setUpMethod() throws Exception {
        /* clean db for each test */
        for(ProjectFileDTO dto : dao.findAll()){
            dao.delete(dto.getId());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dto")
    public void testAddFile(ProjectFileDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* retrieve saved DTO back from DB */
        ProjectFileDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dto")
    public void testUpdateFile(ProjectFileDTO dto) {
        /* save */
        dto = dao.save(dto);
        
        /* retrieve saved DTO back from DB */
        ProjectFileDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* change name of file and update in DB */
        retrievedDTO.setName("recentlychangedname");
        dao.update(retrievedDTO);
        
        /* retrieve from DB and assert name change was persisted */
        retrievedDTO = dao.findById(dto.getId());
        assertTrue("file was not updated in database!", retrievedDTO.getName().equals("recentlychangedname"));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dto")
    public void testDeleteFile(ProjectFileDTO dto) {
        /* save */
        dto = dao.save(dto);
        
        /* number of files in db */
        int filesInDB = dao.findAll().size();

        /* retrieve saved DTO back from DB */
        ProjectFileDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* delete file from DB */
        dao.delete(retrievedDTO.getId());
        
        /* assert number of files is decreased by one */
        assertSame("file was not deleted in database!", filesInDB-1, dao.findAll().size());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dto")
    public void testFindByName(ProjectFileDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* assert DTO can be found in DB by name */
        assertNotNull("file with name was not found in DB!", dao.findByName(dto.getName()));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dtos")
    public void testFindAll(List<ProjectFileDTO> dtos) {
        for(ProjectFileDTO dto : dtos){
            dto = dao.save(dto);
        }
        
        /* assert all objects where persisted in DB */
        assertEquals("expected numbers of jobs in database could not be found!", dao.findAll().size(), dtos.size());
    }
}