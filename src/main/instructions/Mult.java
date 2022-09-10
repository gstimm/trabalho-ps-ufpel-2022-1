package main.instructions;

import java.util.HashSet;
import main.Memory;
import main.OneOperandInstruction;
import main.AddressingMode;
import main.Instruction;
import main.Registers;

public class Mult extends Instruction implements OneOperandInstruction{
    private char operand1; 

    public Mult(){
        super("MULT", 14, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    public void doOperation(Registers registers, Memory memory) {
        registers.setACC((char)(registers.getACC() * memory.getMemoryPosition(operand1)));
    }

     public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value){
        this.operand1 = value;
    }  
}