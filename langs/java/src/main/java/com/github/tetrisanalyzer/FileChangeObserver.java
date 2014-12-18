package com.github.tetrisanalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileChangeObserver implements Runnable {
    private final WatchService watcher;
    private final WatchKey key;
    private final Path dir;

    private final String filename;
    private final FileChangedEvent fileChangedEvent;
    private boolean sendEvent = true;

    public FileChangeObserver(String filename, FileChangedEvent fileChangedEvent) {
        File file = new File(filename);
        this.filename = file.getAbsolutePath();
        this.fileChangedEvent = fileChangedEvent;

        dir = file.toPath().getParent();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            key = dir.register(watcher, ENTRY_MODIFY);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent event: key.pollEvents()) {
                if (event.kind() != OVERFLOW) {
                    Path file = dir.resolve(((WatchEvent<Path>) event).context());
                    if (file.toString().equals(filename)) {
                        if (sendEvent) {
                            fileChangedEvent.changed();
                        }
                        sendEvent = !sendEvent;
                    }
                }
            }
            key.reset();
        }
    }

    public static interface FileChangedEvent {
        public void changed();
    }
}
