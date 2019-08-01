/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import com.google.common.jimfs.AttributeProvider;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author jerzy.malyszko
 */
public interface FileSystemUtil {

    public static byte[] getPathByteContent(Path path) throws IOException {
        try (SeekableByteChannel sbc = Files.newByteChannel(path, StandardOpenOption.READ)) {
            ByteBuffer alloc = ByteBuffer.allocate((int) sbc.size());
            sbc.read(alloc);
            return alloc.array();
        }
    }

    public enum PosixFattrs {

        FILE_KEY("posix:fileKey"),
        SIZE("posix:size"),
        CREATION_TIME("posix:creationTime"),
        OWNER("posix:owner"),
        PERMISSIONS("posix:permissions"),
        GROUP("posix:group"),
        LAST_ACCESS_TIME("posix:lastAccessTime"),
        LAST_MODIFIED_TIME("posix:lastModifiedTime");

        private final String attrName;

        private PosixFattrs(String attrName) {
            this.attrName = attrName;
        }

        @Override
        public String toString() {
            return attrName;
        }

    }

    default String buildPermissionString(Set<PosixFilePermission> permissions) {
        StringBuilder eins = new StringBuilder();
        StringBuilder zwei = new StringBuilder();
        Optional<PosixFilePermission> owner = permissions.stream().filter(per -> per.name().startsWith("OWNER")).findAny();
        Optional<PosixFilePermission> group = permissions.stream().filter(per -> per.name().startsWith("GROUP")).findAny();
        Optional<PosixFilePermission> other = permissions.stream().filter(per -> per.name().startsWith("OTHER")).findAny();
        PosixFilePermission[] prm = new PosixFilePermission[3];
        prm[0] = owner.orElse(null);
        prm[1] = group.orElse(null);
        prm[2] = other.orElse(null);
        for (PosixFilePermission posix : prm) {
            String perm = posix.name().split("_")[1];
            if ("EXECUTE".equals(perm)) {
                eins.append(String.valueOf(7));
                zwei.append("rwx");
            } else if ("WRITE".equals(perm)) {
                eins.append(String.valueOf(6));
                zwei.append("rw-");
            } else if ("READ".equals(perm)) {
                eins.append(String.valueOf(4));
                zwei.append("r--");
            } else {
                eins.append(String.valueOf(1));
                zwei.append("---");
            }
        }
        return eins.toString() + '/' + zwei.toString();
    }

    default Map<String, Object> readAttributes(Path p) throws IOException {
        Map<String, Object> retVal = new HashMap<>();
        PosixFileAttributes attrs = Files.readAttributes(p, PosixFileAttributes.class);
        retVal.put(PosixFattrs.FILE_KEY.toString(), attrs.fileKey());
        retVal.put(PosixFattrs.SIZE.toString(), attrs.size());
        retVal.put(PosixFattrs.CREATION_TIME.toString(), attrs.creationTime());
        retVal.put(PosixFattrs.OWNER.toString(), attrs.owner().toString());
        retVal.put(PosixFattrs.PERMISSIONS.toString(), buildPermissionString(attrs.permissions()));
        retVal.put(PosixFattrs.GROUP.toString(), attrs.group().toString());
        retVal.put(PosixFattrs.LAST_ACCESS_TIME.toString(), attrs.lastAccessTime());
        retVal.put(PosixFattrs.LAST_MODIFIED_TIME.toString(), attrs.lastModifiedTime());
        return retVal;
    }

    default Set<String> getFattrsNamesSet() {
        return Stream.of(PosixFattrs.values()).map(PosixFattrs::toString).collect(Collectors.toSet());
    }

    default void translateDbProperty(String propertyTypeName, Object propertyValue, Path target) {
        try {
            if (propertyTypeName.toLowerCase().contains("time")) {
                propertyValue = FileTime.fromMillis(Long.parseLong(propertyValue.toString()));
            }
            Files.setAttribute(target, propertyTypeName, propertyValue);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
