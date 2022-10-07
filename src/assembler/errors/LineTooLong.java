package assembler.errors;

public class LineTooLong extends Exception {
    public LineTooLong(String reason){
        super(reason);
    }
}