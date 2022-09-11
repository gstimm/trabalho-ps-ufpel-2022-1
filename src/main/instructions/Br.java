package main.instructions;

import java.util.HashSet;
import main.AddressingMode;
import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;

public class Br extends Instruction implements OneOperandInstruction{
    private char operand1;

    public Br(){
        super("BR", 0, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    public void doOperation(Registers registers, Memory memory) {
        registers.setPC(memory.getMemoryPosition(operand1));
    }

    public void setOperand1(char value) {
        this.operand1 = value;
    }

    public char getOperand1() {
        return this.operand1;
    }

    public AddressingMode getOperand1AddressingMode(char opcode) {
        return AddressingMode.addressingModeByOpcode(opcode);
    }
}
