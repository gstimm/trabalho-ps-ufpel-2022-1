package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.AddressingMode;

public class Call extends Instruction implements OneOperandInstruction{
    private char operand1;

    public Call(){
        super("CALL", 15, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }
    
    public void doOperation(Registers registers, Memory memory){
        memory.setMemoryPosition(registers.getSP(), registers.getPC());
        registers.incrementSP((char) 1);
        registers.setPC(memory.getMemoryPosition(operand1));
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