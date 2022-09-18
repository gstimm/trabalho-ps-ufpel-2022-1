package main.gui;
import main.errors.UndefinedExecutionMode;

public enum ExecutionMode {
    CONTINUOUS("CONTINUOUS"), STEP("STEP");
    private final String mode;

    ExecutionMode(String mode){
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }
    public boolean equals(ExecutionMode mode){
        return this.mode.equals(mode.toString());
    }

    public static ExecutionMode stringToExecutionMode(String mode) throws UndefinedExecutionMode {
        if (mode.equals(CONTINUOUS.toString())) return CONTINUOUS;
        if (mode.equals(STEP.toString())) return STEP;
        throw new UndefinedExecutionMode("The string " + mode + " does not correspond to any Execution Mode");
    }

    public static byte getNumber(ExecutionMode mode) throws UndefinedExecutionMode {
        if (mode.equals(CONTINUOUS)) return (byte) 0;
        if (mode.equals(STEP)) return (byte) 1;
        throw new UndefinedExecutionMode("The Execution mode " + mode.toString() + " is not valid!!");
    }
    public static ExecutionMode getExecutionMode(byte number) throws UndefinedExecutionMode {
        switch (number){
            case 0:
                return CONTINUOUS;
            case 1:
                return STEP;
            default:
                throw new UndefinedExecutionMode("There is no execution mode with number " + number);
        }
    }
}


