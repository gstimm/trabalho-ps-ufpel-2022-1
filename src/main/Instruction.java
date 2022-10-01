package main;
import assembler.PseudoInstruction;

public abstract class Instruction extends PseudoInstruction {
    private final int opcode;

    public Instruction(String mnemonic, int opcode, int instructionSize, int numberOfOperands){
        super(mnemonic, instructionSize, numberOfOperands);
        this.opcode = opcode;
    }
    
    public String toString(){
        return "\nOPCODE:\t\t" + opcode + 
                "\n" + super.toString();
    }

    public int getOpcode() {
        return opcode;
    }

    public abstract String toBinary();
}