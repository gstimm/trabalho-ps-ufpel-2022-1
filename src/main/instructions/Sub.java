package main.instructions;

import main.Instruction;
import main.Memory;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet; 

public class Sub extends Instruction {
    public Sub() {
        super("SUB", 6, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.IMMEDIATE);
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        registers.setACC((char)(registers.getACC() - memory.getMemoryPosition(addressOperand)));
    }
}