/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.bpm.fs.dao.FileSystemDao;
import pl.pgnig.serwis.bpm.fs.dao.PathContentDao;
import pl.pgnig.serwis.bpm.fs.dao.PathDao;
import pl.pgnig.serwis.bpm.fs.entity.Filesystem;
import pl.pgnig.serwis.bpm.fs.entity.PathContent;

/**
 *
 * @author jerzy.malyszko
 */
@Service
@Async
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FilesystemDatabaseSynchronizer {

    @Autowired
    private FileSystemDao fsDao;

    @Autowired
    private PathDao pathDao;

    @Autowired
    private PathContentDao contentDao;

    public void persistBrandNewPath(Path path) throws IOException {
        Path parentPath = path.getParent();
        pl.pgnig.serwis.bpm.fs.entity.Path dbParentPath = pathDao.findByValueAndFilesystemName(parentPath.toAbsolutePath().toString(), FileSystemSupplier.FILESYSTEM_ID);
        pl.pgnig.serwis.bpm.fs.entity.Path newPath = new pl.pgnig.serwis.bpm.fs.entity.Path();
        Filesystem dbFs = fsDao.findByName(FileSystemSupplier.FILESYSTEM_ID);
        newPath.setFilesystem(dbFs);
        newPath.setParentPath(dbParentPath);
        newPath.setDirectory(Files.isDirectory(path));
        newPath.setValue(path.toAbsolutePath().toString());
        pathDao.save(newPath);
        if (!newPath.getDirectory()) {
            byte[] contentArray = FileSystemUtil.getPathByteContent(path);
            persistPathContent(newPath, contentArray);
        }
    }

    private void persistPathContent(pl.pgnig.serwis.bpm.fs.entity.Path dbPath, byte[] content) {
        PathContent pc = new PathContent();
        pc.setPath(dbPath);
        pc.setContent(content);
        pc.setValidFrom(new Date());
        contentDao.save(pc);
        dbPath.setPathContent(pc);
        pathDao.save(dbPath);
    }

    public void persistPathContent(Path path) throws IOException {
        pl.pgnig.serwis.bpm.fs.entity.Path existingPath = pathDao.findByValueAndFilesystemName(path.toAbsolutePath().toString(), FileSystemSupplier.FILESYSTEM_ID);
        PathContent upToDateContent = existingPath.getPathContent();
        upToDateContent.setValidTo(new Date());
        contentDao.save(upToDateContent);
        persistPathContent(existingPath, FileSystemUtil.getPathByteContent(path));
    }

}
