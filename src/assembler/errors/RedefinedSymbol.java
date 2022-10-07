package assembler.errors;

public class RedefinedSymbol extends Exception {
    public RedefinedSymbol(String reason){
        super(reason);
    }
}
