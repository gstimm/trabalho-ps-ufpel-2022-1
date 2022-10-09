package assembler.errors;

public class MalformedToken extends Exception{
    public MalformedToken(String reason){
        super(reason);
    }
}
