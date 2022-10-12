package assembler.pseudo_instructions;

import java.util.Set;

import assembler.PseudoInstruction;
import main.AddressingMode;
import main.OneOperandInstruction;
import main.errors.UndefinedAddressingMode;

public class Start extends PseudoInstruction implements OneOperandInstruction {
    private short operand1;

    public Start() {
        super("START", 0, 1);
    }

    @Override
    public short getOperand1() {
        return operand1;
    }

    @Override
    public void setOperand1(short value) {
        operand1 = value;
    }

    @Override
    public Set<AddressingMode> getOperand1AddressingModes() {
        return null;
    }

    @Override
    public AddressingMode getCurrentOperand1AddressingMode() {
        return null;
    }

    @Override
    public void setCurrentOperand1AddressingMode(short opcode) throws UndefinedAddressingMode {}
    public void setCurrentOperand1AddressingMode(AddressingMode mode) throws UndefinedAddressingMode {}
}