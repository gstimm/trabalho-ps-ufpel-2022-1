package assembler;

public class Main {
    public static void main(String[] args){
        String caminho_arquivo = "";
        if (args.length < 1){
            System.out.println("PASSE O NOME DO ARQUIVO COMO ARGUMENTO!!!");
            System.exit(-1);
        }
        caminho_arquivo = args[0];
        System.out.println("Arquivo " + caminho_arquivo);
        Assembler assembler = new Assembler();
        try {
            assembler.assemble(caminho_arquivo);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
