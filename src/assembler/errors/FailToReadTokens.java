package assembler.errors;

public class FailToReadTokens extends Exception {
    public FailToReadTokens(String reason){
        super(reason);
    }
}
