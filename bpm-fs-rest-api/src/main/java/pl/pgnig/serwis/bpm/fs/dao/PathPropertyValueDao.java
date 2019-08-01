/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.dao;

import org.springframework.data.repository.CrudRepository;
import pl.pgnig.serwis.bpm.fs.entity.PathPropertyValue;

/**
 *
 * @author jerzy.malyszko
 */
public interface PathPropertyValueDao extends CrudRepository<PathPropertyValue, Long>{
    
}
