package main.errors;

public class StackOverflow extends Exception{
    public StackOverflow(String reason){
        super(reason);
    }
}
