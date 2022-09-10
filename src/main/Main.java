package main;

public class Main {
    public static void main(String[] args){
        VirtualMachine maquina_virtual = new VirtualMachine();
        try {
            maquina_virtual.initMachine();

            maquina_virtual.readFile("D:\\Scripts\\trabalho-ps-ufpel-2022-1\\src\\main\\file.txt");
            
            maquina_virtual.getMemory().printMemoryInRange(101, 120);
            System.out.println(maquina_virtual.getCPU().getRegisters().toString());
            
            while (maquina_virtual.getCPU().getHaltState() == false) {
                maquina_virtual.getCPU().cycle(maquina_virtual.getMemory());
                maquina_virtual.getMemory().printMemoryInRange(101, 120);
                System.out.println(maquina_virtual.getCPU().getRegisters().toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
