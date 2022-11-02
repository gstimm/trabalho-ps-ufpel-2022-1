package assembler.errors;

public class InvalidFileFormat extends Exception {
    public InvalidFileFormat(String reason){
        super(reason);
    }
}