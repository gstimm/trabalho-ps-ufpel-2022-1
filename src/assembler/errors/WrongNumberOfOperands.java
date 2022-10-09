package assembler.errors;

public class WrongNumberOfOperands extends Exception {
    public WrongNumberOfOperands(String reason){
        super(reason);
    }
}