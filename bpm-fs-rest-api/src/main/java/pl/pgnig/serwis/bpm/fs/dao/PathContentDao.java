/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pgnig.serwis.bpm.fs.entity.PathContent;

/**
 *
 * @author jerzy.malyszko
 */
@Repository
public interface PathContentDao extends CrudRepository<PathContent, Long>{
    
}
