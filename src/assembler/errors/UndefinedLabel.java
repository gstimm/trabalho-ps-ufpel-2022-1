package assembler.errors;

public class UndefinedLabel extends Exception {
    public UndefinedLabel(String reason) {
        super(reason);
    }
}
