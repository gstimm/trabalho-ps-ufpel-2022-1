package main.instructions;

import main.AddressingMode;
import main.ExecuteOperation;
import main.Instruction;
import main.Memory;
import main.Registers;

public class Ret extends Instruction implements ExecuteOperation {
    public Ret() {
        super("RET", 9, 1, 0);
    }

    public void doOperation(Registers registers, Memory memory) {
        registers.setPC(memory.getMemoryPosition(registers.getSP()));
        registers.incrementSP((short) -1);
    }

    @Override
    public String toDecimalString(){
        int result_opcode = AddressingMode.opcodeByAddressingMode(this.getOpcode(), null, null);
        String result = result_opcode + "";
        return result;
    }
}