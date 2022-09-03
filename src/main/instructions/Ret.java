package main.instructions;

import main.Instruction;
import main.Registers;
import java.util.HashSet;
import main.AddressingMode;

public class Ret extends Instruction {
    public Ret(){
        super("RET", 9, 1, 0);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        this.setAddressingModesSuported(modes);
    }
    
    @Override
    public void doOperation(Registers registers) {
        return;
    }
}