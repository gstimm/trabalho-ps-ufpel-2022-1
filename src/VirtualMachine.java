public class VirtualMachine {
    public static void main(String[] args) {
        //Tamanho total da memória = 2048 palavras * 16 bits(2 bytes) de cada palavra, ou seja o tamanho total da memória é 4096 bytes.
        Memory memory = new Memory(2048);
        memory.printMemory();
    }

}    