package assembler.errors;

public class NotEnoughOperands extends Exception {
    public NotEnoughOperands(String reason){
        super(reason);
    }
}