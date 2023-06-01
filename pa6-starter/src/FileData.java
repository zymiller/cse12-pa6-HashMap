public class FileData {

    public String name;
    public String dir;
    public String lastModifiedDate;

    public FileData(String name, String directory, String modifiedDate) {
        this.name = name;
        this.dir = directory;
        this.lastModifiedDate = modifiedDate;

    }

    public String toString() {
        String output = String.format("{Name: %s, Directory: %s, Modified Date: %s}", this.name, this.dir, this.lastModifiedDate);
        return output;
    }

    public boolean equalsFile (FileData compareTo) {
        if (this.name.equals(compareTo.name) && this.dir.equals(compareTo.dir) && this.lastModifiedDate.equals(compareTo.lastModifiedDate)) {
            return true;
        } else {
            return false;
        }
    }
}
