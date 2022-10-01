package assembler;

public abstract class PseudoInstruction {
    private final String mnemonic;
    private final int instructionSize;
    private final int numberOfOperands;

    public PseudoInstruction(String mnemonic, int instructionSize, int numberOfOperands){
        this.mnemonic = mnemonic;
        this.instructionSize = instructionSize;
        this.numberOfOperands = numberOfOperands;
    }

    @Override
    public String toString() {
        return "MNEMONIC:\t" + mnemonic + 
               "\nSIZE:\t\t" + instructionSize + 
               "\nOPERANDS:\t" + numberOfOperands + "\n";
    }

    public int getInstructionSize() {
        return instructionSize;
    }

    public int getNumberOfOperands() {
        return numberOfOperands;
    }

    public String getMnemonic() {
        return mnemonic;
    }
}
