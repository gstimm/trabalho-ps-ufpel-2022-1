package main;

public class Main {
    public static void main(String[] args) {
        VirtualMachine maquina_virtual = new VirtualMachine();
        // maquina_virtual.getMemory().printMemory();
        maquina_virtual.getMemory().setMemoryPosition(2040, 'j');
        maquina_virtual.getMemory().printMemory();
        System.out.println(maquina_virtual.getCPU().getRegisters().toString());
        
    }
}
