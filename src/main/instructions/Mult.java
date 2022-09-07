package main.instructions;

import java.util.HashSet;
import main.Memory;
import main.AddressingMode;
import main.Instruction;
import main.Registers;

public class Mult extends Instruction {
    public Mult(){
        super("MULT", 14, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        registers.setACC((char)(registers.getACC() * memory.getMemoryPosition(addressOperand)));
    }
    
}