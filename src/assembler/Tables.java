package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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
        
        table_file.write("[SIZE]\n"+this.moduleSize + "\n");
        table_file.write("\n[START]\n"+this.moduleStart + "\n");
        
        table_file.write("\n[GLOBAL]\n");
        for (TableEntry entry : definitionTable.values()) {
            if (entry.getIsGlobal()){
                table_file.write(entry.getLabel() + "\t" + entry.getAddress() + "\n");
            }
        }
        
        table_file.write("\n[USES]\n");
        for (InternalUseTableEntry entry : internalUseTable.values()) {
            table_file.write(entry.getLabel() + "\t" + entry.getOccurences().toString() + "\n");
        }
        table_file.close();
    }
}
