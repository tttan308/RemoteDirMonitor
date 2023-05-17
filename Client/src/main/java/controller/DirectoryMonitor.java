package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static view.MainScreen.msgField;
import static view.MainScreen.msgRcv;

public class DirectoryMonitor extends Thread{
    private Path path;
    private WatchService watchService;

    public DirectoryMonitor(Path directoryPath) throws IOException {
        this.path = Paths.get(directoryPath.toUri());
        this.watchService = FileSystems.getDefault().newWatchService();
        this.path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public void run() {
        msgRcv.append("\n").append("Directory monitor started!");
        msgField.setText(String.valueOf(msgRcv));

        while (true) {
            try {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path fileName = (Path) event.context();
                    File file = new File(path + "\\" + fileName);

                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (file.isDirectory()) {
                            msgRcv.append("\n").append("directory added - ").append(fileName);
                            ClientHandle.sendMsg("directory added - " + fileName);
                            registerDirectory(file.toPath());
                        } else {
                            msgRcv.append("\n").append("file created - ").append(fileName);
                            ClientHandle.sendMsg("file created - " + fileName);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        if (!fileName.toString().contains(".")) {
                            msgRcv.append("\n").append("directory deleted - ").append(fileName);
                            ClientHandle.sendMsg("directory deleted - " + fileName);
                        } else {
                            msgRcv.append("\n").append("file deleted - ").append(fileName);
                            ClientHandle.sendMsg("file deleted - " + fileName);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (!file.isDirectory()) {
                            msgRcv.append("\n").append("file modified - ").append(fileName);
                            ClientHandle.sendMsg("file modified - " + fileName);
                        }
                    }

                    msgField.setText(String.valueOf(msgRcv));
                }


                boolean reset = key.reset();
                if (!reset) {
                    continue;
                }

            } catch (IOException | InterruptedException e) {
                msgRcv.append("\n").append("Directory monitor stopped!");
                msgField.setText(String.valueOf(msgRcv));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
    }

    private void registerDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                file.getParent().register(watchService, StandardWatchEventKinds.ENTRY_DELETE);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

