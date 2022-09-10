package main.instructions;

import main.Instruction;
import main.Memory;
import main.Registers;
import main.errors.StackOverflow;

import java.util.HashSet;
import main.AddressingMode;

public class Ret extends Instruction {
    public Ret(){
        super("RET", 9, 1, 0);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        this.setAddressingModesSuported(modes);
    }
    
    public void doOperation(Registers registers, Memory memory) throws StackOverflow {
        registers.setACC(memory.getMemoryPosition(registers.getSP()));
        registers.setSP((char) (registers.getSP() - 1));
    }
}