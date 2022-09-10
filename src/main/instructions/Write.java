package main.instructions;

import main.Instruction;
import main.AddressingMode;
import java.util.HashSet;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;


public class Write extends Instruction implements OneOperandInstruction {
    private char operand1;

    public Write() {
        super("WRITE", 8, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.IMMEDIATE);
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);

        this.setAddressingModesSuported(modes);

    }

    public void doOperation(Registers registers, Memory memory) {
        memory.getMemoryPosition(operand1);
    } 

    public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value){
        this.operand1 = value;
    }   
}
