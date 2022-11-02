package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import assembler.errors.FailToReadTokens;
import assembler.errors.InvalidFileFormat;

public class Tables {
    private int moduleStart;
    
    private int moduleSize;

    // Tabela que contém os símbolos definidos internamente para uso interno e 
    // os símbolos que são definidos internamente para uso interno e externo
    private HashMap<String, TableEntry> definitionTable;
    
    // Símbolos usados internamente e definidos em outro módulo
    private HashMap<String, InternalUseTableEntry> internalUseTable;

    public Tables() {
        this.moduleSize = 0;
        this.moduleStart = 0;
        this.definitionTable = new HashMap<>();
        this.internalUseTable = new HashMap<>();
    }

    public void setModuleStart(int number) {
        this.moduleStart = number;
    }

    public int getModuleStart() {
        return this.moduleStart;
    }

    public int getModuleSize() {
        return this.moduleSize;
    }

    public void setModuleSize(int number) {
        this.moduleSize = number;
    }

    public HashMap<String, TableEntry> getDefitionsTable() {
        return this.definitionTable;
    }

    public HashMap<String, InternalUseTableEntry> getInternalUseTable() {
        return this.internalUseTable;
    }

    public void saveToFile(String filename) throws IOException {
        File output = new File(filename.substring(0, filename.lastIndexOf(".")) + ".TABLE");
        output.createNewFile();
        FileWriter table_file = new FileWriter(output);
        
        table_file.write("[SIZE]\n");
        table_file.write("SIZE = " + this.moduleSize + "\n");
        table_file.write("[START]\n");
        table_file.write("START = "+this.moduleStart + "\n");
        
        table_file.write("[GLOBAL]\n");
        for (TableEntry entry : definitionTable.values()) {
            if (entry.getIsGlobal()){
                table_file.write(entry.getLabel() + " = " + entry.getAddress() + "\n");
            }
        }
        
        table_file.write("[USES]\n");
        for (InternalUseTableEntry entry : internalUseTable.values()) {
            table_file.write(entry.getLabel() + " = " + entry.getOccurences().toString() + "\n");
        }
        table_file.close();
    }

    public void readFromFile(String filename) throws FileNotFoundException, InvalidFileFormat, FailToReadTokens {
        File input = new File(filename);
        Scanner scanner = new Scanner(input);

        String line = "";
        String tokens[];

        line = scanner.nextLine();
        if (line.equals("[SIZE]") == false){
            scanner.close();
            throw new InvalidFileFormat("The file is not in the right format, must start with [SIZE]");
        }
        line = scanner.nextLine();
        tokens = tokensFromLine(line);
        this.moduleSize = Integer.parseInt(tokens[1].strip());
        

        line = scanner.nextLine();
        if (line.equals("[START]") == false){
            scanner.close();
            throw new InvalidFileFormat("The file is not in the right format, the second information must be [START]");
        }
        line = scanner.nextLine();
        tokens = tokensFromLine(line);
        this.moduleStart = Integer.parseInt(tokens[1].strip());


        line = scanner.nextLine();
        if (line.equals("[GLOBAL]") == false){
            scanner.close();
            throw new InvalidFileFormat("The file is not in the right format, the third information must be [GLOBAL]");
        }
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            if (line.contains("=") == false) {
                break;
            }

            tokens = tokensFromLine(line);
            this.definitionTable.put(tokens[0], new TableEntry(tokens[0], Integer.parseInt(tokens[1].strip()), true));
        }

        if (line.equals("[USES]") == false){
            scanner.close();
            throw new InvalidFileFormat("The file is not in the right format, the fouth information must be [USES]");
        }

        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            if (line.contains("=") == false) {
                break;
            }

            tokens = tokensFromLine(line);
            tokens[1] = tokens[1].replace("[", "");
            tokens[1] = tokens[1].replace("]", "");
            ArrayList<Integer> occurences = new ArrayList<>();
            String[] values = tokens[1].split(", ");
            for (String value : values) {
                occurences.add(Integer.parseInt(value.strip()));
            }
            
            this.internalUseTable.put(tokens[0], new InternalUseTableEntry(tokens[0], occurences));
        }

        scanner.close();
    }

    private String[] tokensFromLine(String line) throws FailToReadTokens {
        String[] tokens = line.split("=");
        if (tokens.length != 2) {
            throw new FailToReadTokens("Erro while reading the tokens from line, needed 2, found " + tokens.length + "!!!");
        }
        return tokens;
    }
}
