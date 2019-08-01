/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pgnig.serwis.bpm.fs.entity.PathPropertyType;

/**
 *
 * @author jerzy.malyszko
 */
@Repository
public interface PathPropertyTypeDao extends CrudRepository<PathPropertyType, Long> {

    List<PathPropertyType> findByNameIn(Collection<String> fattrsNamesSet);
    
    PathPropertyType findByName(String fattrName);
    
}
