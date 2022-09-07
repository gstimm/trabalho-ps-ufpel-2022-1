package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Memory;
import main.Registers;
import main.AddressingMode;

public class Copy extends Instruction {
    public Copy(){
        super("COPY", 13, 3, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    
    
    public static void doOperation(Registers registers, Memory memory, int addressOperand1, int addressOperand2) {
        memory.setMemoryPosition(addressOperand1, memory.getMemoryPosition(addressOperand2));
    }

}