package linker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import assembler.InternalUseTableEntry;
import assembler.TableEntry;
import assembler.errors.FailToReadTokens;
import assembler.errors.InvalidFileFormat;
import assembler.errors.RedefinedSymbol;

public class Linker {
    private ArrayList<FileObject> fileObject;
    private int address;
    private HashMap<String, TableEntry> globalSymbolTable;

    public Linker(ArrayList<String> filenames) throws FileNotFoundException, InvalidFileFormat, FailToReadTokens {
        this.fileObject = new ArrayList<>();
        for (String file : filenames) {
            fileObject.add(new FileObject(file));
        }        
        this.address = 0;
        this.globalSymbolTable = new HashMap<>();
    }

    public void link() throws RedefinedSymbol, IOException {
        firstStep();
        secondStep();
    }

    public void firstStep() throws RedefinedSymbol {
        for (FileObject file : fileObject) {
          // 1. Add all the symbols from the definition table of the first module to the global symbol table
            for (String symbol : file.getSymbolTable().getDefitionsTable().keySet()) {
                TableEntry entry = file.getSymbolTable().getDefitionsTable().get(symbol);
                if (globalSymbolTable.containsKey(symbol)) {
                    throw new RedefinedSymbol("The symbol " + symbol + " was already defined in another module!!!");
                }
                globalSymbolTable.put(symbol, new TableEntry(symbol, entry.getAddress() - file.getSymbolTable().getModuleStart() + address, true));
            }
          //2. change the address of the use table from the other modules
            for (String symbol : file.getSymbolTable().getInternalUseTable().keySet()) {
                InternalUseTableEntry entry = file.getSymbolTable().getInternalUseTable().get(symbol);
                ArrayList<Integer> occurences = entry.getOccurences();
                for (int c = 0; c < occurences.size(); c++) {
                    occurences.set(c, occurences.get(c) + address- file.getSymbolTable().getModuleStart());
                }
            }
            this.address += file.getSymbolTable().getModuleSize();
        }
          
  
    }

    public void secondStep() throws IOException {
        File output = new File(fileObject.get(0).getFileName().substring(0, fileObject.get(0).getFileName().lastIndexOf(".")) + ".HPX");
        output.createNewFile();
        FileWriter output_file = new FileWriter(output);

        // reset the address
        this.address = 0;
        
        int index = 0;

        //change adressess for the output file
        for (FileObject file_obj : fileObject) {
            // open the object file assembled
            File file = new File(file_obj.getFileName().substring(0, file_obj.getFileName().lastIndexOf(".")) + ".OBJ");
            Scanner scanner = new Scanner(file);
            HashMap<String, InternalUseTableEntry> uses = file_obj.getSymbolTable().getInternalUseTable();
            while (scanner.hasNextLine()){
                String output_line = "";
                String line = scanner.nextLine();
                String splitted_line[] = line.split("\\|");
                if (splitted_line.length != 2) break; // Arquivo com erro
                output_line += splitted_line[0] + "|";
                String object_code[] = splitted_line[1].strip().split("\\s+");
                String realocattion_mode[] = splitted_line[0].strip().split("\\s+");
                if (realocattion_mode.length != object_code.length) break; // Arquivo com erro
                for (int c = 0; c < object_code.length; c++) {
                    // Verificar se o endereço é a utilização interna de símbolo global
                    boolean internal_use = false;
                    for (String entry : uses.keySet()) {
                        InternalUseTableEntry use_Entry = uses.get(entry);
                        if (use_Entry.getOccurences().contains(index)) {
                            output_line += "\t" + globalSymbolTable.get(entry).getAddress().toString();
                            internal_use = true;
                        }
                    }
                    if (internal_use == false){
                        if (Integer.parseInt(realocattion_mode[c]) == 1){
                            output_line += "\t" + (Integer.parseInt(object_code[c]) + address - file_obj.getSymbolTable().getModuleStart());
                        }
                        else {
                            output_line += "\t" + object_code[c];
                        }
                    }
                    index++;
                }

                // write back to the output_file
                output_file.write(output_line + "\n");
            }
            this.address += file_obj.getSymbolTable().getModuleSize();
            scanner.close();
        }
        output_file.close();
    }

}
