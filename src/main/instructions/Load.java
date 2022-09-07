package main.instructions;

import main.Instruction;
import main.Registers;
import main.Memory;
import java.util.HashSet;
import main.AddressingMode;

public class Load extends Instruction {
    public Load(){
        super("LOAD", 3, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.IMMEDIATE);
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    
    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        registers.setACC((memory.getMemoryPosition(addressOperand)));
    }
}