/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template projectfile, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test.dto;

import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.dto.UserDTO;
import de.uniluebeck.collaboratex.entity.ProjectFile;
import de.uniluebeck.collaboratex.entity.Project;
import de.uniluebeck.collaboratex.entity.User;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class EntityDTOConversionTest extends TestBase {

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-entity")
    public void testUserEntityToDTO(User entity) {
        UserDTO dto = entity.toDTO();
        User convertedEntity = dto.toEntity();
        assertTrue("entities are not the same!", convertedEntity.equals(entity));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "user-dto")
    public void testUserDTOToEntity(UserDTO dto) {
        User entity = dto.toEntity();
        UserDTO convertedDto = entity.toDTO();
        assertTrue("DTOs are not the same!", convertedDto.equals(dto));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-entity")
    public void testProjectEntityToDTO(Project entity) {
        ProjectDTO dto = entity.toDTO();
        Project convertedEntity = dto.toEntity();
        assertTrue("entities are not the same!", convertedEntity.equals(entity));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "project-dto")
    public void testProjectDTOToEntity(ProjectDTO dto) {
        Project entity = dto.toEntity();
        ProjectDTO convertedDto = entity.toDTO();
        assertTrue("DTOs are not the same!", convertedDto.equals(dto));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-entity")
    public void testFileEntityToDTO(ProjectFile entity) {
        ProjectFileDTO dto = entity.toDTO();
        ProjectFile convertedEntity = dto.toEntity();
        assertTrue("entities are not the same!", convertedEntity.equals(entity));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "projectfile-dto")
    public void testFileDTOToEntity(ProjectFileDTO dto) {
        ProjectFile entity = dto.toEntity();
        ProjectFileDTO convertedDto = entity.toDTO();
        assertTrue("DTOs are not the same!", convertedDto.equals(dto));
    }
}
