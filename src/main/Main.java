package main;

import java.util.Set;
import java.util.Iterator;
import main.instructions.*;

public class Main {
    public static void main(String[] args) {
        VirtualMachine maquina_virtual = new VirtualMachine();

        /*
        maquina_virtual.getMemory().setMemoryPosition(2040, 'j');
        maquina_virtual.getMemory().printMemory();
        System.out.println(maquina_virtual.getCPU().getRegisters().toString());
        Set<Instruction> instrucoes = maquina_virtual.getCPU().getInstructions();
        Iterator<Instruction> iterator = instrucoes.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        */
        
        maquina_virtual.getMemory().setMemoryPosition(2040, (char) 150);
        maquina_virtual.getMemory().setMemoryPosition(2041, (char) 120);
        
        System.out.println("2040: " + (int) maquina_virtual.getMemory().getMemoryPosition(2040));
        System.out.println("2041: " + (int) maquina_virtual.getMemory().getMemoryPosition(2041));

        // Copy.doOperation(maquina_virtual.getCPU().getRegisters(), maquina_virtual.getMemory(), 2040, 2041);
        
        Load.doOperation(maquina_virtual.getCPU().getRegisters(), maquina_virtual.getMemory(), 2040);
        Store.doOperation(maquina_virtual.getCPU().getRegisters(), maquina_virtual.getMemory(), 2045);

        System.out.println(maquina_virtual.getCPU().getRegisters().toString());
    }
}
