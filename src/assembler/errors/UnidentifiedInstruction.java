package assembler.errors;

public class UnidentifiedInstruction extends Exception {
    public UnidentifiedInstruction(String reason){
        super(reason);
    }
}
