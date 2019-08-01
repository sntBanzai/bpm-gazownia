/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.pgnig.serwis.bpm.fs.exc.NoParentDirectoryException;

/**
 *
 * @author jerzy.malyszko
 */
@Component
@SessionScope
public class FileSystemService {

    @Autowired
    private FileSystemProxy proxx;

    @Autowired
    private FileSystemUtil fsUtil;

    @Autowired
    private FilesystemDatabaseSynchronizer sync;

    private Path currentPath;

    @RegisterFileSystemState
    public Path touch(String file, byte[] bytes) {
        Path resolve = currentPath.resolve(file);
        try {
            if (Files.exists(resolve)) {
                Files.setLastModifiedTime(resolve, FileTime.from(Instant.now()));
            } else {
                Files.createFile(resolve);
                if (bytes != null) {
                    try (FileChannel fc = FileChannel.open(resolve, StandardOpenOption.WRITE)) {
                        fc.write(ByteBuffer.wrap(bytes));
                    }
                }
                sync.persistBrandNewPath(resolve);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return resolve;
    }

    @PostConstruct
    public void init() {
        currentPath = proxx.getRootDirectories().iterator().next();
    }

    public String pwd() {
        return currentPath.toAbsolutePath().toString();
    }

    public void mkdir(String dirName) {
        Path newDirName = currentPath.resolve(dirName);
        try {
            proxx.createDirectory(newDirName);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Path> ll() {
        try {
            List<Path> retVal = new ArrayList<>();
            proxx.newDirectoryStream(currentPath, ent -> true).forEach(retVal::add);
            return retVal;
        } catch (IOException ex) {
            Logger.getLogger(FileSystemService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void cd(String dest) {
        if (dest == null) {
            if (currentPath.getParent() == null) {
                throw new RuntimeException(new NoParentDirectoryException());
            }
            currentPath = currentPath.getParent();
            return;
        }
        Path resolve = currentPath.resolve(dest);
        if (Files.exists(resolve) && Files.isDirectory(resolve)) {
            currentPath = resolve;
        } else {
            throw new RuntimeException(new NoSuchFileException(dest));
        }
    }

    @RegisterFileSystemState
    public Path touch(String file) {
        return touch(file, null);
    }

    public Map<String, Object> stat(String file) {
        Path resolved = currentPath.resolve(file);
        if (Files.exists(resolved)) {
            try {
                Map<String, Object> retVal = fsUtil.readAttributes(resolved);
                return retVal;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException(file);
        }
    }

    public void chattr(Path target, Map<String, Object> attrMap) {
        attrMap.forEach((k, v) -> chattr(target, k, v));
    }

    @RegisterFileSystemState
    public void chattr(Path target, String attribute, Object object) {
        try {
            Files.setAttribute(target, attribute, object);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
