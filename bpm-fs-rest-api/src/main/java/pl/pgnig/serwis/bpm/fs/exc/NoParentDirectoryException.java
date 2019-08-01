/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.exc;

import java.nio.file.NoSuchFileException;

/**
 *
 * @author jerzy.malyszko
 */
public class NoParentDirectoryException extends NoSuchFileException {
    
    public NoParentDirectoryException() {
        super("");
    }
    
}
