package main.instructions;

import main.Instruction;
import main.AddressingMode;
import java.util.HashSet;
import main.Memory;


public class Write extends Instruction {
    public Write() {
        super("WRITE", 8, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.IMMEDIATE);
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);

        this.setAddressingModesSuported(modes);

    }

    public void doOperation(Memory memory, int addressOperand) {
         memory.getMemoryPosition(addressOperand);
    }    
}
