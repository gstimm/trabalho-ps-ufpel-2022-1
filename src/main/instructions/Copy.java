package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Memory;
import main.Registers;
import main.TwoOperandInstruction;
import main.AddressingMode;

public class Copy extends Instruction implements TwoOperandInstruction{
    private char operand1;
    private char operand2;

    public Copy(){
        super("COPY", 13, 3, 2);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    
    public void doOperation(Registers registers, Memory memory) {
        memory.setMemoryPosition(operand1, memory.getMemoryPosition(operand2));
    }


    public char getOperand1() {
        return operand1;
    }


    public void setOperand1(char operand1) {
        this.operand1 = operand1;
    }


    public char getOperand2() {
        return operand2;
    }


    public void setOperand2(char operand2) {
        this.operand2 = operand2;
    }

    public AddressingMode getOperand1AddressingMode(char opcode) {
        return AddressingMode.addressingModeByOpcode((char) (opcode & 0b0101111));
    }

    public AddressingMode getOperand2AddressingMode(char opcode) {
        return AddressingMode.addressingModeByOpcode((char) (opcode & 0b1011111));
    }
}