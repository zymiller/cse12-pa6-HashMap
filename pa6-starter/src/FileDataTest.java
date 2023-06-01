import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Objects;

import org.junit.*;

public class FileDataTest {
    FileData testFile1 = new FileData("name.txt", "/home", "05/14/23");
    FileData testFile2 = new FileData("name.txt", "/home", "05/14/23");
    
    @Test
    public void testPrint() {
        System.out.println(testFile1);
        System.out.println(testFile2);
        System.out.println(Objects.hashCode(testFile1));
        System.out.println(Objects.hashCode(testFile1));
        LinkedHashSet<FileData> testSet = new LinkedHashSet<FileData>(8);
        
        System.out.println("First add: " + addToSet(testSet, testFile1));
        System.out.println("Second add: " + addToSet(testSet, testFile2));
        /**System.out.println(testSet.add(testFile2));
        System.out.println(testSet.add(testFile2)); **/
        System.out.println(Objects.hashCode(testFile2));
        System.out.println(testSet.size());
        System.out.println(testFile1.equals(testFile2));
        System.out.println(testFile1.equalsFile(testFile2));
        assertEquals("{Name: name.txt, Directory: /home, Modified Date: 05/14/23}", testFile1.toString());
    }

    public boolean addToSet (LinkedHashSet<FileData> set, FileData fileToAdd) {
        for (FileData file : set) {
            if (fileToAdd.equalsFile(file)) {
                return false;
            }
        }
        return set.add(fileToAdd);
    }
}
