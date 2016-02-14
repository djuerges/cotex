/*
 * To change this license header, choose License Headers in User Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test.persistence;

import de.uniluebeck.collaboratex.dao.UserDAO;
import de.uniluebeck.collaboratex.dto.UserDTO;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import java.util.List;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestUserDAO extends TestBase {

    private final UserDAO dao = new UserDAO();
    
    @BeforeMethod
    public void setUpMethod() throws Exception {
        /* clean db for each test */
        for(UserDTO dto : dao.findAll()){
            dao.delete(dto.getId());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dto")
    public void testPersistUser(UserDTO dto) {
        /* save and assert is in DB */
        dto = dao.save(dto);

        assertEquals("DTOs are not the same!", dao.findById(dto.getId()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dto")
    public void testUpdateUser(UserDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* retrieve DTO back from DB and set project name to current time */
        UserDTO retrievedDto = dao.findById(dto.getId());
        retrievedDto.setUsername("changeduser");

        /* update and assert was changed in DB */
        dao.update(retrievedDto);
        assertEquals("changed DTO was not updated in DB!", dao.findById(dto.getId()).getUsername(), retrievedDto.getUsername());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dto")
    public void testDeleteUser(UserDTO dto) {
        /* save */
        dto = dao.save(dto);
        
        /* number of files in db */
        int filesInDB = dao.findAll().size();

        /* retrieve saved DTO back from DB */
        UserDTO retrievedDTO = dao.findById(dto.getId());
        assertNotNull("DTO was not found!", retrievedDTO);
        
        /* delete file from DB */
        dao.delete(retrievedDTO.getId());
        
        /* assert number of files is decreased by one */
        assertSame("file was not deleted in database!", filesInDB-1, dao.findAll().size());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dto")
    public void testFindByUsername(UserDTO dto) {
        /* save */
        dto = dao.save(dto);

        /* assert DTO can be found in DB by name */
        assertEquals("changed DTO was not updated in DB!", dao.findByUsername(dto.getUsername()), dto);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dtos")
    public void testFindAll(List<UserDTO> dtos) {
        for(UserDTO dto : dtos){
            dto = dao.save(dto);
        }
        
        /* assert all objects where persisted in DB */
        assertEquals("expected numbers of jobs in database could not be found!", dao.findAll().size(), dtos.size());
    }
}