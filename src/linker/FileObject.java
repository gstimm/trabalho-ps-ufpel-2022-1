package linker;

import java.io.FileNotFoundException;

import assembler.Tables;
import assembler.errors.FailToReadTokens;
import assembler.errors.InvalidFileFormat;

public class FileObject {
    private String fileName;
    private Tables symbolTable;

    public FileObject(String fileName) throws FileNotFoundException, InvalidFileFormat, FailToReadTokens {
        this.fileName = fileName;
        this.symbolTable = new Tables();
        symbolTable.readFromFile(fileName.substring(0, fileName.lastIndexOf(".")) + ".TABLE");
    }

    public String getFileName() {
        return fileName;
    }

    public Tables getSymbolTable() {
        return symbolTable;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSymbolTable(Tables tables) {
        this.symbolTable = tables;
    }

}
