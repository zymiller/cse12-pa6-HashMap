import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;


public class FileSystemTest {

    FileSystem testSystem;

    @Before
    public void setup() {
        testSystem = new FileSystem("src/input.txt");
    }

    @Test
    public void testMultiDir() {
        ArrayList<FileData> outputFiles = new ArrayList<FileData>();
        outputFiles.add(new FileData("mySample.txt", "/home", "02/01/2021"));
        outputFiles.add(new FileData("mySample.txt", "/root", "02/01/2021"));
        testSystem.add("mySample.txt", "/root", "02/01/2021");
        ArrayList<FileData> testFiles = testSystem.findFilesInMultDir("02/01/2021");
        //testSystem.getDate("02/01/2021");
        System.out.println("Test file: " + testFiles.toString());
        System.out.println("Output file: " + outputFiles.toString());
        System.out.println(testSystem.dateMap.size());
        System.out.println(testSystem.dateMap.containsKey("02/01/2021"));
        //testSystem.dateMap.printMap();
        System.out.println("AList: " + outputFiles.equals(testFiles));
        System.out.println("String: " + outputFiles.toString().equals(testFiles.toString()));
        assertEquals(outputFiles.toString(), testFiles.toString());
    }

    @Test
    public void testFindFile() {
        FileData fileFind = testSystem.findFile("mySample.txt", "/home");
        FileData expectedFind = new FileData("mySample.txt", "/home", "02/01/2021");
        System.out.println(testSystem.findFile("mySample.txt", "/home"));
        System.out.println(testSystem.removeFile("mySample.txt", "/home"));
        System.out.println(testSystem.findFile("mySample.txt", "/home"));
        System.out.println(testSystem.findFile("mySample.txt", "/root"));
        System.out.println(testSystem.findFile("mySample.txt", "/user"));
        System.out.println(testSystem.findFilesByName("mySample.txt"));
        System.out.println(testSystem.findFilesByDate("02/01/2021"));
        System.out.println(testSystem.dateMap.keys());
        System.out.println(testSystem.nameMap.keys());
        assertEquals(expectedFind.toString(), fileFind.toString());
    }

}
