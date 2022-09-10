package main.instructions;

import java.util.HashSet;
import main.AddressingMode;
import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;

public class Brpos extends Instruction implements OneOperandInstruction{
    char operand1;

    public Brpos(){
        super("BRPOS", 1, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    public void doOperation(Registers registers, Memory memory) {
        if(registers.getACC() > 0){
            registers.setPC(memory.getMemoryPosition(operand1));
        }
    }

    public char getOperand1() {
        return operand1;
    }

    public void setOperand1(char operand1) {
        this.operand1 = operand1;
    }
}