package main.instructions;

import main.Instruction;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet;
import main.Memory;
import main.OneOperandInstruction;


public class Store extends Instruction implements OneOperandInstruction {
    private char operand1;

    public Store() {
        super("STORE", 7, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);

        this.setAddressingModesSuported(modes);
    }

    public void doOperation(Registers registers, Memory memory) {
        memory.setMemoryPosition(operand1, registers.getACC());
    }
     public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value){
        this.operand1 = value;
    }  
}
