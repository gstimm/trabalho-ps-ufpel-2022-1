package main;

public class VirtualMachine {
    private final int defultMemorySize = 2048;
    private final int defaultStackSize = 100;
    private final int defaultStackStartIndex = 2;
    private final int defaultStack = 1;
    private Memory memory;
    private CPU cpu;
    
    VirtualMachine(CPU cpu, Memory memory){
        memory = memory;
        cpu = cpu;
    }
    VirtualMachine(){
        cpu = new CPU();
        memory = new Memory(defultMemorySize);
    }
    
    public boolean isStackFull() {
        return cpu.getRegisters().getSP() == (defaultStackSize + defaultStackStartIndex);
    }
    public Memory getMemory(){
        return memory;
    }
    public CPU getCPU(){
        return cpu;
    }
    public void initMachine(){
        cpu.getRegisters();
    }
}    