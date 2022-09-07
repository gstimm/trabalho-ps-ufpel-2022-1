package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Memory;
import main.Registers;
import main.AddressingMode;

public class Call extends Instruction {
    public Call(){
        super("CALL", 15, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }
    
   
    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        registers.incrementSP((char) 1);
        memory.setMemoryPosition(registers.getSP(), registers.getPC());
        registers.setPC(memory.getMemoryPosition(addressOperand));
    }
}