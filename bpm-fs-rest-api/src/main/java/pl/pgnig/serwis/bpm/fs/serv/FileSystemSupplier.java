/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jerzy.malyszko
 */
public interface FileSystemSupplier extends Supplier<FileSystem>{
    
    public static final String FILESYSTEM_ID = getFileSystemId();
    
     public static String getFileSystemId() {
        if (FILESYSTEM_ID != null) {
            return FILESYSTEM_ID;
        }
        Properties prop = new Properties();
        try {
            prop.load(JimfsSupplier.class.getResourceAsStream("/filesystem.properties"));
            return prop.getProperty("filesystem.id");
        } catch (IOException ex) {
            Logger.getLogger(FileSystemSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new IllegalStateException("Filesystem id should be known at this point.");
    }
     
}
