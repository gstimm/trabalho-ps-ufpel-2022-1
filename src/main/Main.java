package main;

import main.instructions.Stop;

public class Main {
    public static void main(String[] args){
        VirtualMachine maquina_virtual = new VirtualMachine();
        try {
            maquina_virtual.initMachine();
            Stop stop_instruction = new Stop();

            maquina_virtual.readFile(System.getProperty("user.dir") + "/src/main/file.txt");
            
            maquina_virtual.getMemory().printMemoryInRange(101, 120);
            System.out.println(maquina_virtual.getCPU().getRegisters().toString());
            

            while (maquina_virtual.getCPU().getRegisters().getRI() != stop_instruction.getOpcode()) {
                maquina_virtual.cycle();
                maquina_virtual.getMemory().printMemoryInRange(101, 120);
                System.out.println(maquina_virtual.getCPU().getRegisters().toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
