package main.instructions;

import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.errors.UndefinedAddressingMode;
import main.AddressingMode;
import main.ExecuteOperation;

import java.util.HashSet;
import java.util.Set;

public class Store extends Instruction implements OneOperandInstruction, ExecuteOperation {
    private final Set<AddressingMode> operand1AddressingModes;
    private short operand1;
    private AddressingMode currentOperand1AddressingMode;

    public Store() {
        super("STORE", 7, 2, 1);
        this.operand1AddressingModes = new HashSet<AddressingMode>();
        this.operand1AddressingModes.add(AddressingMode.DIRECT);
        this.operand1AddressingModes.add(AddressingMode.INDIRECT);
        this.operand1 = 0;
        this.currentOperand1AddressingMode = null;
    }

    public void doOperation(Registers registers, Memory memory) {
        memory.setMemoryPosition(operand1, registers.getACC());
    }

    public short getOperand1() {
        return this.operand1;
    }

    public void setOperand1(short value) {
        this.operand1 = value;
    }

    public Set<AddressingMode> getOperand1AddressingModes() {
        return this.operand1AddressingModes;
    }

    public AddressingMode getCurrentOperand1AddressingMode() {
        return this.currentOperand1AddressingMode;
    }

    public void setCurrentOperand1AddressingMode(short opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode(opcode);
        if (this.operand1AddressingModes.contains(mode) == false) {
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString()
                    + " in not valid for the instruction " + this.getMnemonic());
        }
        this.currentOperand1AddressingMode = mode;
    }

    @Override
    public String toString() {
        return super.toString() +
                "OPERAND1: \t" + this.operand1 +
                "\nADDR MODE: \t" + this.currentOperand1AddressingMode.toString() +
                "\n";
    }

    @Override
    public String toBinary(){
        int result_opcode = AddressingMode.opcodeByAddressingMode(this.getOpcode(), this.currentOperand1AddressingMode, null);
        String result = result_opcode + "\t" + operand1;
        return result;
    }
}
