package main.instructions;

import java.util.HashSet;
import main.AddressingMode;
import main.Instruction;
import main.Memory;
import main.Registers;

public class Brpos extends Instruction {
    public Brpos(){
        super("BRPOS", 1, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        if(registers.getACC() > 0){
            registers.setPC(memory.getMemoryPosition(addressOperand));
        }
    }
    
}
