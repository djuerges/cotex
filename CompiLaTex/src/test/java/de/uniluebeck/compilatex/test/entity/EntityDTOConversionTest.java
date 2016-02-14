/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.test.entity;

import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.entity.JobFile;
import de.uniluebeck.compilatex.entity.Job;
import de.uniluebeck.compilatex.test.data.TestDataProvider;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class EntityDTOConversionTest {

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "file-entity")
    public void testFileEntityToDTO(JobFile entity) {
        JobFileDTO dto = entity.toDTO();
        JobFile convertedEntity = dto.toEntity();
        assertTrue("entities are not the same!", convertedEntity.equals(entity));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "file-dto")
    public void testFileDTOToEntity(JobFileDTO dto) {
        JobFile entity = dto.toEntity();
        JobFileDTO convertedDto = entity.toDTO();
        assertTrue("DTOs are not the same!", convertedDto.equals(dto));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-entity")
    public void testJobEntityToDTO(Job entity) {
        JobDTO dto = entity.toDTO();
        Job convertedEntity = dto.toEntity();
        assertTrue("entities are not the same!", convertedEntity.equals(entity));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "job-dto")
    public void testJobDTOToEntity(JobDTO dto) {
        Job entity = dto.toEntity();
        JobDTO convertedDto = entity.toDTO();
        assertTrue("DTOs are not the same!", convertedDto.equals(dto));
    }
}
