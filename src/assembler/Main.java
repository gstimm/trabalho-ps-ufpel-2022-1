package assembler;

public class Main {
    public static void main(String[] args){
        Assembler assembler = new Assembler();
        try {
            assembler.assemble(System.getProperty("java.class.path").split(";")[0] + "/assembler/test.asm");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
