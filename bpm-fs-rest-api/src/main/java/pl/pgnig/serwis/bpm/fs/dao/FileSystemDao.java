/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.dao;

import pl.pgnig.serwis.bpm.fs.entity.Filesystem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jerzy.malyszko
 */
@Repository
public interface FileSystemDao extends CrudRepository<Filesystem, Long> {

    @EntityGraph("Filesystem.wholeGraph")
    Filesystem findByName(String name);
    
    boolean existsFilesystemByName(String name);

}
