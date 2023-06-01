import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    // TODO
    public FileSystem() {
        this.nameMap = new MyHashMap<String, ArrayList<FileData>>();
        this.dateMap = new MyHashMap<String, ArrayList<FileData>>();
    }

    // TODO
    public FileSystem(String inputFile) {
        nameMap = new MyHashMap<String, ArrayList<FileData>>();
        dateMap = new MyHashMap<String, ArrayList<FileData>>(); 
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            System.out.print("Adding: ");
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                System.out.println(data[0] + " " + data[1] + " " + data[2]);
                add(data[0], data[1], data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    // TODO
    public boolean add(String fileName, String directory, String modifiedDate) {
        if (nameMap.get(fileName) != null) {
            for (FileData file : nameMap.get(fileName)) {
                if (file.name.equals(fileName) && file.dir.equals(directory)){ 
                    return false;
                }
            }
        }
        // CHECK DUPLICATES
        ArrayList<FileData> dateInputFiles = dateMap.get(modifiedDate);
        if (dateInputFiles == null) {
            dateInputFiles = new ArrayList<>();
        }
        FileData inputFile = new FileData(fileName, directory, modifiedDate);
        boolean dateStatus = false;
        if (addToArray(dateInputFiles, inputFile)) {
            dateMap.set(modifiedDate, dateInputFiles);
            dateStatus = dateMap.get(modifiedDate).equals(dateInputFiles);
            System.out.println("Adding to date: " + inputFile);
            System.out.println(dateStatus);
        }
        ArrayList<FileData> nameInputFiles = nameMap.get(fileName);
        if (nameInputFiles == null) {
            nameInputFiles = new ArrayList<>();
        }
        boolean nameStatus = false;
        if (addToArray(nameInputFiles, inputFile)) {
            nameMap.set(fileName, nameInputFiles);
            nameStatus = nameMap.get(fileName).equals(nameInputFiles);
            System.out.println("Adding to date: " + inputFile);
            System.out.println(nameStatus);
        }
        //boolean nameStatus = nameMap.put(fileName, inputFiles);
        //System.out.println("Adding to name: " + inputFile);
        //System.out.println(nameStatus);
        System.out.println("input: " + dateInputFiles);
        return (dateStatus && nameStatus);
    }

    // TODO
    public FileData findFile(String name, String directory) {
        ArrayList<FileData> returnFiles = nameMap.get(name);
        if (returnFiles == null) {
            return null;
        }
        for (FileData file: returnFiles) {
            if (file.dir.equals(directory)) {
                return file;
            }
        }
        return null;
    }

    // TODO
    public ArrayList<String> findAllFilesName() {
        ArrayList<String> keyList = new ArrayList<>(nameMap.keys());
        keyList.removeAll(Collections.singleton(null));
        return keyList;
    }

    // TODO
    public ArrayList<FileData> findFilesByName(String name) {
        ArrayList<FileData> returnFiles;
        try {
            returnFiles = new ArrayList<>(nameMap.get(name));
        } catch (Exception e) {
            returnFiles = new ArrayList<>();
        }
        returnFiles.removeAll(Collections.singleton(null));
        return returnFiles;
    }

    // TODO
    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
        ArrayList<FileData> returnFiles;
        try {
            returnFiles = new ArrayList<>(dateMap.get(modifiedDate));
        } catch (Exception e) {
            returnFiles = new ArrayList<>();
        }
        returnFiles.removeAll(Collections.singleton(null));
        return returnFiles;
    }

    // TODO
    public ArrayList<FileData> findFilesInMultDir(String modifiedDate) {
        ArrayList<FileData> dateFiles = dateMap.get(modifiedDate);
        if (dateFiles == null) {
            return new ArrayList<FileData>();
        }
        LinkedHashSet<FileData> returnFiles = new LinkedHashSet<>();
        for (FileData file : dateFiles) {
            ArrayList<FileData> filesToCheck = nameMap.get(file.name);
            for (FileData checkFile : filesToCheck) {
                if (checkFile.name == file.name && checkFile.dir != file.dir) {
                    returnFiles.add(file);
                    returnFiles.add(checkFile);
                }
            }
        }
        return new ArrayList<>(returnFiles);
    }

    // TODO
    public boolean removeByName(String name) {
        ArrayList<FileData> nameFiles = nameMap.get(name);
        if (nameFiles == null || name == null) {
            return nameMap.remove(name);
        }
        try {
            for (FileData file : nameFiles) {
                removeArrayList(dateMap.get(file.lastModifiedDate), file);
                if (dateMap.get(file.lastModifiedDate).isEmpty()) {
                    dateMap.remove(file.lastModifiedDate);
                }
            }
        } catch (Exception e) {}
        return nameMap.remove(name);
    }

    // TODO
    public boolean removeFile(String name, String directory) {
        FileData removedFile = null;
        ArrayList<FileData> removeFiles = nameMap.get(name);
        if (removeFiles == null || name == null || directory == null) { 
            return false;
        }
        try {
            for (FileData file : removeFiles) {
                if (file.name.equals(name) && file.dir.equals(directory)) {
                    removedFile = file;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        if (removedFile == null) {
        }else if (removeArrayList(removeFiles, removedFile)) {
            System.out.println("Removed from name map");
            if (removeFiles.isEmpty()) {
                nameMap.remove(name);
            }
            removeFiles = dateMap.get(removedFile.lastModifiedDate);
            removeArrayList(removeFiles, removedFile);
            if (removeFiles.isEmpty()) {
                dateMap.remove(removedFile.lastModifiedDate);
            }
            return true;
        }
        return false;
    }

    private boolean addToArray (ArrayList<FileData> array, FileData fileToAdd) {
        try {
            for (FileData file : array) {
                if (fileToAdd.equalsFile(file)) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return array.add(fileToAdd);
    }

    private boolean removeArrayList (ArrayList<FileData> array, FileData fileToRemove) {
        if (array.isEmpty()) {
            return false;
        }
        for (FileData file : array) {
            if (file.equalsFile(fileToRemove)) {
                array.remove(file);
                return true;
            }
        }
        return false;
    }

    /**public void getDate(String date) {
        System.out.println("keys: " + dateMap.keys());
        System.out.println(date + ": " + dateMap.getAll(date).toString());   
        ArrayList<FileData> dateFile = dateMap.get(date);
        System.out.println(dateFile.hashCode());
        System.out.println("Array to String: " + dateFile.toString());
        System.out.println("get dates");
        for (FileData file : dateMap.get(date)) {
            System.out.println("In " + date + ": " + file);
        }
        
    }**/
}
