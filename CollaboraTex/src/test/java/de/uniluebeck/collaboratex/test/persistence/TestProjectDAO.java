/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test.persistence;

import de.uniluebeck.collaboratex.dao.ProjectDAO;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import java.util.List;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestProjectDAO extends TestBase {

    private final ProjectDAO dao = new ProjectDAO();
    
    @BeforeMethod
    public void setUpMethod() throws Exception {
        /* clean db for each test */
        for(ProjectDTO dto : dao.findAll()){
            dao.delete(dto.getId());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testPersistProject(ProjectDTO dto) {
        /* save and assert is in DB */
        dto = dao.save(dto);
        assertEquals("DTOs are not the same!", dao.findById(dto.getId()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testUpdateProject(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* current time */
        long now = System.currentTimeMillis();
        
        /* retrieve DTO back from DB and set project name to current time */
        ProjectDTO retrievedDto = dao.findById(dto.getId());
        retrievedDto.setName(String.valueOf(now));

        /* update and assert was changed in DB */
        dao.update(retrievedDto);
        assertEquals("changed DTO was not updated in DB!", dao.findById(dto.getId()).getName(), String.valueOf(now));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testDeleteProject(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* retrieve saved DTO back from DB */
        assertNotNull("DTO was not found!", dao.findById(dto.getId()));

        /* delete and assert is not in DB anymore */
        dao.delete(dto.getId());
        assertNull("DTO was still found!", dao.findById(dto.getId()));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testFindByName(ProjectDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* assert DTO can be found in DB by name */
        assertEquals("changed DTO was not updated in DB!", dao.findByName(dto.getName()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dtos")
    public void testFindAll(List<ProjectDTO> dtos) {
        for(ProjectDTO dto : dtos){
            dto = dao.save(dto);
        }
        
        /* assert all objects where persisted in DB */
        assertEquals("expected numbers of jobs in database could not be found!", dao.findAll().size(), dtos.size());
    }
    
    @Test
    public void testFindAllEmpty() {
        /* assert no projects are in DB */
        assertTrue("expected numbers of jobs in database could not be found!", dao.findAll().isEmpty());
    }
}