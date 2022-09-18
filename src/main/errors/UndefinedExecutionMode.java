package main.errors;

public class UndefinedExecutionMode extends Exception {
    public UndefinedExecutionMode(String reason){
        super(reason);
    }
}
