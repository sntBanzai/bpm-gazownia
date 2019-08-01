/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.serv;

import java.nio.file.FileSystemException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author jerzy.malyszko
 */
@Aspect
@Component
public class FileSystemAspect {

    private static final String CORRUPTED_FILE_SYSTEM = "Corrupted file system";

    private AtomicBoolean corrupted = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        Logger.getLogger(FileSystemAspect.class.getCanonicalName()).info("Initialized");
    }

    @Before("@annotation(RegisterFileSystemState)")
    public void checkConciseness() {
        if (corrupted.get()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, CORRUPTED_FILE_SYSTEM);
        }
    }

    @AfterThrowing(pointcut = "bean(filesystemDatabaseSynchronizer)", throwing = "ex")
    public void markCorruped(Throwable ex) {
        corrupted.set(true);
        Logger.getLogger(FileSystemAspect.class.getCanonicalName()).severe(ex.toString());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, CORRUPTED_FILE_SYSTEM, ex);
    }

    @AfterThrowing(pointcut = "bean(fileSystemRestEndpoint)", throwing = "ex")
    public void respondAppriopriately(JoinPoint jp, Throwable ex) {
        if(RuntimeException.class.equals(ex.getClass()) && (ex.getCause() instanceof FileSystemException)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getCause().getClass().getSimpleName().replace("Exception", ""));
        }
        
    }

}
