package assembler.errors;

public class InvalidDigit extends Exception {
    public InvalidDigit(String reason){
        super(reason);
    }
}