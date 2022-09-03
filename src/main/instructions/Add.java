package main.instructions;

import main.Instruction;
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
    
    @Override
    public void doOperation(Registers registers) {
        return;
    }
    
    
}
