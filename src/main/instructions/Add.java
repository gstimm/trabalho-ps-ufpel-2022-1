package main.instructions;

import main.Instruction;
import main.Memory;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet;

public class Add extends Instruction {
    public Add(){
        super("ADD", 2, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        registers.incrementACC(memory.getMemoryPosition(addressOperand));
    }
}
