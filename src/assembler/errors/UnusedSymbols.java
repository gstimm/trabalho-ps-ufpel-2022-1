package assembler.errors;

public class UnusedSymbols extends Exception {
    public UnusedSymbols(String reason){
        super(reason);
    }
}
