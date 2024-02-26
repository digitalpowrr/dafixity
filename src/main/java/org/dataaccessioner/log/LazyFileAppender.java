package org.dataaccessioner.log;


import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class LazyFileAppender<E> extends FileAppender<E> {
    @Override
    public void openFile(String file_name) throws IOException {
        streamWriteLock.lock();
        try {
            File file = new File(file_name);
            boolean result = FileUtil.createMissingParentDirectories(file);
            if (!result) {
                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }

            LazyFileOutputStream lazyFos = new LazyFileOutputStream(file, append);
            setOutputStream(lazyFos);
        } finally {
            streamWriteLock.unlock();
        }
    }
}
