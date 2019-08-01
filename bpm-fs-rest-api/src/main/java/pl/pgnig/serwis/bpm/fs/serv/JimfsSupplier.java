/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import pl.pgnig.serwis.bpm.fs.dao.FileSystemDao;
import pl.pgnig.serwis.bpm.fs.dao.PathDao;
import pl.pgnig.serwis.bpm.fs.dao.PathPropertyDao;
import pl.pgnig.serwis.bpm.fs.dao.PathPropertyTypeDao;
import pl.pgnig.serwis.bpm.fs.dao.PathPropertyValueDao;
import pl.pgnig.serwis.bpm.fs.entity.Filesystem;
import pl.pgnig.serwis.bpm.fs.entity.PathContent;
import pl.pgnig.serwis.bpm.fs.entity.PathProperty;
import pl.pgnig.serwis.bpm.fs.entity.PathPropertyType;
import pl.pgnig.serwis.bpm.fs.entity.PathPropertyValue;

/**
 *
 * @author jerzy.malyszko
 */
@Service
public class JimfsSupplier implements FileSystemSupplier, FileSystemUtil {
    
    @Autowired
    private FileSystemDao fsDao;
    
    @Autowired
    private PathDao pathDao;
    
    @Autowired
    private PathPropertyDao ppropDao;
    
    @Autowired
    private PathPropertyTypeDao pptDao;
    
    @Autowired
    private PathPropertyValueDao ppvDao;
    
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;
    
    private final FileSystem filesystemInstance = Jimfs.newFileSystem(Configuration.unix().toBuilder()
            .setAttributeViews("basic", "owner", "posix", "unix")
            .setWorkingDirectory("/")
            .build());
    
    @PostConstruct
    public void init() {
        Logger.getLogger(JimfsSupplier.class.getCanonicalName()).info("Initializing FS for id " + FILESYSTEM_ID);
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                boolean doesFilesSystemExist = fsDao.existsFilesystemByName(FILESYSTEM_ID);
                Logger.getLogger(JimfsSupplier.class.getCanonicalName()).info("Does fs exist " + doesFilesSystemExist);
                if (!doesFilesSystemExist) {
                    Filesystem newFs = new Filesystem();
                    newFs.setName(FILESYSTEM_ID);
                    fsDao.save(newFs);
                    Iterable<Path> rootDirectories = filesystemInstance.getRootDirectories();
                    
                    List<PathPropertyType> existingPpts = pptDao.findByNameIn(getFattrsNamesSet());
                    List<PathPropertyType> missingFattrs = getFattrsNamesSet().stream()
                            .filter(fattr -> existingPpts.stream().map(PathPropertyType::getName).noneMatch(fattr::equals))
                            .map(PathPropertyType::create).collect(Collectors.toList());
                    pptDao.saveAll(missingFattrs);
                    Map<String, PathPropertyType> pptsMap = Stream.concat(existingPpts.stream(), missingFattrs.stream())
                            .collect(Collectors.toMap(PathPropertyType::getName, Function.identity()));
                    
                    for (Path root : rootDirectories) {
                        pl.pgnig.serwis.bpm.fs.entity.Path dbRoot = new pl.pgnig.serwis.bpm.fs.entity.Path();
                        dbRoot.setFilesystem(newFs);
                        dbRoot.setValue(root.toAbsolutePath().toString());
                        dbRoot.setDirectory(Files.isDirectory(root));
                        pathDao.save(dbRoot);
                        try {
                            Date d = new Date();
                            for (String fileAttr : getFattrsNamesSet().stream().filter(fa -> !fa.contains("size")).collect(Collectors.toSet())) {
                                PathPropertyType thisPpt = pptsMap.get(fileAttr);
                                PathProperty pprop = new PathProperty();
                                pprop.setPath(dbRoot);
                                pprop.setPathPropertyType(thisPpt);
                                ppropDao.save(pprop);
                                Object attrValue = Files.getAttribute(root, fileAttr);
                                PathPropertyValue ppv = new PathPropertyValue();
                                ppv.setPathProperty(pprop);
                                ppv.setValidFrom(d);
                                if(attrValue instanceof FileTime){
                                    attrValue = ((FileTime) attrValue).toMillis();
                                } else if(attrValue instanceof Set){
                                    attrValue = buildPermissionString((Set<PosixFilePermission>) attrValue);
                                }
                                String valueOf = String.valueOf(attrValue);
                                ppv.setValue(valueOf);
                                ppvDao.save(ppv);
                                pprop.setPathPropertyValue(ppv);
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    return;
                }
                Filesystem fsFound = fsDao.findByName(FILESYSTEM_ID);
                List<pl.pgnig.serwis.bpm.fs.entity.Path> rootsBloodyRoots = new ArrayList<>(fsFound.getPathList());
                rootsBloodyRoots.sort((r1, r2) -> Integer.compare(r1.getValue().split("/").length, r2.getValue().split("/").length));
                for (pl.pgnig.serwis.bpm.fs.entity.Path dbPath : rootsBloodyRoots) {
                    final Path path = filesystemInstance.getPath(dbPath.getValue());
                    boolean exists = Files.exists(path);
                    if (!exists) {
                        if (dbPath.getDirectory()) {
                            try {
                                filesystemInstance.provider().createDirectory(path);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            PathContent pathContent = dbPath.getPathContent();
                            if (pathContent == null) {
                                try {
                                    Files.createFile(path);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                continue;
                            }
                            try (SeekableByteChannel sbc = Files.newByteChannel(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                                sbc.write(ByteBuffer.wrap(pathContent.getContent()));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    Map<PathPropertyType, PathPropertyValue> collect = dbPath.getPathPropertyList().stream()
                            .collect(Collectors.toMap(PathProperty::getPathPropertyType, PathProperty::getPathPropertyValue));
                    for (Map.Entry<PathPropertyType, PathPropertyValue> ent : collect.entrySet()) {
                        translateDbProperty(ent.getKey().getName(), ent.getValue().getValue(), path);
                    }
                }
            }
        });
        
    }
    
    @Override
    public FileSystem get() {
        return filesystemInstance;
    }
    
    @Override
    public void translateDbProperty(String propertyTypeName, Object propertyValue, Path target) {
        if (PosixFattrs.PERMISSIONS.toString().equals(propertyTypeName)) {
            String[] split = propertyValue.toString().split(",");
            Set<PosixFilePermission> collect = Stream.of(split).map(String::trim).map(PosixFilePermission::valueOf).collect(Collectors.toSet());
            try {
                Files.setPosixFilePermissions(target, collect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        if (PosixFattrs.OWNER.toString().equals(propertyTypeName)) {
            try {
                propertyValue = filesystemInstance.getUserPrincipalLookupService().lookupPrincipalByName(propertyValue.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (PosixFattrs.GROUP.toString().equals(propertyTypeName)) {
            try {
                propertyValue = filesystemInstance.getUserPrincipalLookupService().lookupPrincipalByGroupName(propertyValue.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        FileSystemUtil.super.translateDbProperty(propertyTypeName, propertyValue, target);
    }
    
}
