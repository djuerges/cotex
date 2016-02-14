/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.test.persistence;

import de.uniluebeck.compilatex.PersistenceManager;
import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.test.data.TestDataProvider;
import static de.uniluebeck.compilatex.test.data.TestDataProvider.generateId;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class PersistenceTest implements Serializable {

    PersistenceManager pm;

    @BeforeClass
    public void setUpClass() throws Exception {
        pm = new PersistenceManager();
    }
    
    @BeforeMethod
    public void setUpMethod() throws Exception {
        /* clean db for each test */
        for(JobDTO dto : pm.findAll()){
            pm.delete(dto.getId());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testPersistJob(JobDTO dto) {
        /* save and assert is in DB */
        dto = pm.save(dto);
        assertEquals("DTOs are not the same!", pm.find(dto.getId()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testUpdateJob(JobDTO dto) {
        /* save */
        dto = pm.save(dto);

        /* current time */
        long now = System.currentTimeMillis();
        
        /* retrieve DTO back from DB and set project name to current time */
        JobDTO retrievedDto = pm.find(dto.getId());
        retrievedDto.setDirectory(String.valueOf(now));

        /* update and assert was changed in DB */
        pm.update(retrievedDto);
        assertEquals("changed DTO was not updated in DB!", pm.find(dto.getId()).getDirectory(), String.valueOf(now));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testUpdateFile(JobDTO dto) {
        /* save */
        dto = pm.save(dto);
        
        /* retrieve saved DTO back from DB */
        JobDTO retrievedDTO = pm.find(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* change name of file and update in DB */
        retrievedDTO.getFiles().iterator().next().setName("recentlychangedname");
        pm.update(retrievedDTO);
        
        /* retrieve from DB and assert name change was persisted */
        retrievedDTO = pm.find(dto.getId());
        assertTrue("file was not updated in database!", retrievedDTO.getFiles().iterator().next().getName().equals("recentlychangedname"));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testAddFile(JobDTO dto) {
        /* save */
        dto = pm.save(dto);

        /* retrieve saved DTO back from DB */
        JobDTO retrievedDTO = pm.find(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* add file and update in DB */
        retrievedDTO.getFiles().add(new JobFileDTO(generateId(), "testFile", "testParentFolder", System.currentTimeMillis(), true));
        pm.update(retrievedDTO);
        
        /* retrieve from DB and assert added file was persisted */
        retrievedDTO = pm.find(dto.getId());
        assertNotSame("file was not deleted in database!", retrievedDTO.getFiles().size(), dto.getFiles().size());
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testDeleteJob(JobDTO dto) {
        /* save */
        dto = pm.save(dto);

        /* retrieve saved DTO back from DB */
        assertNotNull("DTO was not found!", pm.find(dto.getId()));

        /* delete and assert is not in DB anymore */
        pm.delete(dto.getId());
        assertNull("DTO was still found!", pm.find(dto.getId()));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testDeleteFile(JobDTO dto) {
        /* save */
        dto = pm.save(dto);

        /* retrieve saved DTO back from DB */
        JobDTO retrievedDTO = pm.find(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* delete file from job and update in DB */
        retrievedDTO.getFiles().remove(retrievedDTO.getFiles().iterator().next());
        pm.update(retrievedDTO);
        
        /* retrieve from DB and assert number of files is decreased by one */
        retrievedDTO = pm.find(dto.getId());
        assertNotSame("file was not deleted in database!", retrievedDTO.getFiles().size(), dto.getFiles().size());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testFindByName(JobDTO dto) {
        dto.setDirectory(UUID.randomUUID().toString());
        
        /* save */
        dto = pm.save(dto);

        /* assert DTO can be found in DB by name */
        assertEquals("changed DTO was not updated in DB!", pm.find(dto.getId()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dtos")
    public void testFindAll(List<JobDTO> dtos) {
        for(JobDTO dto : dtos){
            dto = pm.save(dto);
        }
        
        /* assert all objects where persisted in DB */
        assertEquals("expected numbers of jobs in database could not be found!", pm.findAll().size(), dtos.size());
    }
}
