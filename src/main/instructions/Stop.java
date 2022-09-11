package main.instructions;
import java.util.HashSet;

import main.AddressingMode;
import main.Instruction;
import main.Memory;
import main.Registers;

public class Stop extends Instruction {
    public Stop(){
        super("STOP", 11, 1, 0, new HashSet<AddressingMode>());
        this.getAddressingModesSuported().add(AddressingMode.IMMEDIATE);
        this.getAddressingModesSuported().add(AddressingMode.DIRECT);
        this.getAddressingModesSuported().add(AddressingMode.INDIRECT);
	}

    @Override
    public void doOperation(Registers registers, Memory memory) {}
}