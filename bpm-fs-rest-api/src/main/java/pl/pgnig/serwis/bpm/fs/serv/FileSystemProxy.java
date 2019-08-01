/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jerzy.malyszko
 */
@Component
public class FileSystemProxy extends FileSystemProvider {

    private FileSystem fs;

    @Autowired
    FileSystemSupplier sup;
    
    @Autowired
    private FilesystemDatabaseSynchronizer synchronizer;

    public FileSystemProxy() {

    }

    @PostConstruct
    public void init() {
        this.fs = sup.get();
    }

    public Iterable<Path> getRootDirectories() {
        return fs.getRootDirectories();
    }

    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return fs.getUserPrincipalLookupService();
    }

    private FileSystemProvider p() {
        return fs.provider();
    }

    @Override
    public String getScheme() {
        return p().getScheme();
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return p().getFileSystem(uri);
    }

    @Override
    public Path getPath(URI uri) {
        return p().getPath(uri);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return p().newByteChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return p().newDirectoryStream(dir, filter);
    }

    @Override
    @RegisterFileSystemState
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        p().createDirectory(dir, attrs);
        synchronizer.persistBrandNewPath(dir);
    }

    @Override
    @RegisterFileSystemState
    public void delete(Path path) throws IOException {
        p().delete(path);
    }

    @Override
    @RegisterFileSystemState
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        p().copy(source, target, options);
    }

    @Override
    @RegisterFileSystemState
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        p().move(source, target, options);
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return p().isSameFile(path, path2);
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return p().isHidden(path);
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return p().getFileStore(path);
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        p().checkAccess(path, modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        return p().getFileAttributeView(path, type, options);
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return p().readAttributes(path, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return p().readAttributes(path, attributes, options);
    }

    @Override
    @RegisterFileSystemState
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        p().setAttribute(path, attribute, value, options);
    }

}
