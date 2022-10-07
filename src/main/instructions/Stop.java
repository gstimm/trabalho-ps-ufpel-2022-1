package main.instructions;

import main.AddressingMode;
import main.Instruction;

public class Stop extends Instruction {
    public Stop() {
        super("STOP", 11, 1, 0);
    }

    @Override
    public String toDecimalString(){
        int result_opcode = AddressingMode.opcodeByAddressingMode(this.getOpcode(), null, null);
        String result = result_opcode + "";
        return result;
    }
}