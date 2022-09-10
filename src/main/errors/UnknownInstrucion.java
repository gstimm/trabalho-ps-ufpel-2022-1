package main.errors;

public class UnknownInstrucion extends Exception {
    public UnknownInstrucion(String reason){
        super(reason);
    }
}
