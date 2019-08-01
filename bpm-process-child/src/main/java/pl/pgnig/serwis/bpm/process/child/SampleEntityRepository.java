/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.process.child;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.pgnig.serwis.bpm.process.child.entity.SampleEntity;

/**
 *
 * @author jerzy.malyszko
 */
@RepositoryRestResource(path = "sample", collectionResourceRel = "sample")
public interface SampleEntityRepository extends PagingAndSortingRepository<SampleEntity, Long>{
    
}
