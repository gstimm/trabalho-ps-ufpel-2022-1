package main.instructions;

import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet;

public class Add extends Instruction implements OneOperandInstruction {
    private char operand1;

    public Add(){
        super("ADD", 2, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    public void doOperation(Registers registers, Memory memory) {
        registers.incrementACC(memory.getMemoryPosition(operand1));
    }
    
    public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value){
        this.operand1 = value;
    }

    public AddressingMode getOperand1AddressingMode(char opcode) {
        return AddressingMode.addressingModeByOpcode(opcode);
    }
}
