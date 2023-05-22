package controller;

import java.io.File;
import java.util.*;

import static controller.ClientHandle.s;
import static view.MainScreen.msgField;
import static view.MainScreen.msgRcv;


public class DirectoryMonitor extends Thread{
    private final File directory;
    private File[] previousFiles;
    private List<String> folderInDir = new ArrayList<>();
    private List<String> fileInDir = new ArrayList<>();
    private Map<String, String> modifiedFile = new HashMap<>();
    public DirectoryMonitor(String path) {
        directory = new File(path);
        previousFiles = directory.listFiles();
        for(File file : previousFiles){
            if(file.isDirectory()){
                folderInDir.add(file.getName());
            }
            else{
                fileInDir.add(file.getName());
                modifiedFile.put(file.getName(), String.valueOf(file.lastModified()));
            }
        }
    }

    public void run() {
        File[] currentFiles = new File[0];
        while (true) {
            if (s.isClosed()) {
                System.out.println("Socket closed");
                break;
            }
            currentFiles = directory.listFiles();
            List<String> folderCurrent = new ArrayList<>();
            List<String> fileCurrent = new ArrayList<>();
            Map<String, String> modifiedCurrent = new HashMap<>();
            for (File file : currentFiles) {
                if (file.isDirectory()) {
                    folderCurrent.add(file.getName());
                } else {
                    fileCurrent.add(file.getName());
                    modifiedCurrent.put(file.getName(), String.valueOf(file.lastModified()));
                }
            }
            boolean done = false;
            if (folderCurrent.size() > folderInDir.size()) {
                for (String folder : folderCurrent) {
                    if (!folderInDir.contains(folder)) {
                        msgRcv.append("New folder: ").append(folder).append("\n");
                        msgField.setText(String.valueOf(msgRcv));
                        ClientHandle.sendMsg("created " + folder);
                        done = true;
                        break;
                    }
                }
            } else if (folderCurrent.size() < folderInDir.size()) {
                for (String folder : folderInDir) {
                    if (!folderCurrent.contains(folder)) {
                        msgRcv.append("Folder deleted: ").append(folder).append("\n");
                        msgField.setText(String.valueOf(msgRcv));
                        ClientHandle.sendMsg("deleted " + folder);
                        done = true;
                        break;
                    }
                }
            }

            if (done) {
                folderInDir = folderCurrent;
                previousFiles = currentFiles;
                continue;
            }

            if (fileCurrent.size() > fileInDir.size()) {
                for (String file : fileCurrent) {
                    if (!fileInDir.contains(file)) {
                        msgRcv.append("New file: ").append(file).append("\n");
                        msgField.setText(String.valueOf(msgRcv));
                        ClientHandle.sendMsg("created " + file);
                        break;
                    }
                }
            } else if (fileCurrent.size() < fileInDir.size()) {
                for (String file : fileInDir) {
                    if (!fileCurrent.contains(file)) {
                        msgRcv.append("File deleted: ").append(file).append("\n");
                        msgField.setText(String.valueOf(msgRcv));
                        ClientHandle.sendMsg("deleted " + file);
                        break;
                    }
                }
            } else {
                for (String file : fileCurrent) {
                    if (!modifiedCurrent.get(file).equals(modifiedFile.get(file)) && file != null) {
                        msgRcv.append("File modified: ").append(file).append("\n");
                        msgField.setText(String.valueOf(msgRcv));
                        ClientHandle.sendMsg("modified " + file);
                        break;
                    }
                }
            }
            modifiedFile = modifiedCurrent;
            fileInDir = fileCurrent;
            previousFiles = currentFiles;
        }
    }
}

