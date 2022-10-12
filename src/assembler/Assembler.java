package assembler;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import assembler.errors.*;
import assembler.pseudo_instructions.*;
import main.instructions.*;
import main.*;
import main.errors.*;

public class Assembler {
    private HashMap<String, Instruction> instructionsTable;
    private HashMap<String, PseudoInstruction> pseudoInstructionsTable;
    private int lineCounter;
    private int addressCounter;
    public static final int MAX_LINE_SIZE = 80;
    public static final int MAX_LABEL_SIZE = 8;

    // Tabela que contém os símbolos definidos internamente para uso interno e 
    // os símbolos que são definidos internamente para uso interno e externo
    private HashMap<String, TableEntry> definitionTable;
    
    // Símbolos usados internamente e definidos em outro módulo
    private HashMap<String, InternalUseTableEntry> internalUseTable;

    public Assembler(){
        this.instructionsTable = new HashMap<>();
        this.pseudoInstructionsTable = new HashMap<>();
        this.lineCounter = 1;
        this.addressCounter = 0;

        PseudoInstruction aux;
        aux = new Const();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new End();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new IntDef();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new IntUse();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new Space();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new Stack();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);
        aux = new Start();
        pseudoInstructionsTable.put(aux.getMnemonic(), aux);

        Instruction aux_instruction;
        aux_instruction = new Add();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Br();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Brneg();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Brpos();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Brzero();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Call();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Copy();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Divide();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Load();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Mult();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Read();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Ret();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Stop();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Store();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Sub();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);
        aux_instruction = new Write();
        instructionsTable.put(aux_instruction.getMnemonic(), aux_instruction);

        this.definitionTable = new HashMap<>();
        this.internalUseTable = new HashMap<>();
    }

    public void assemble(String fileName) {
        try {
            firstStep(fileName);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void firstStep(String filename) 
    throws EndNotFound, LineTooLong, MalformedToken, InvalidDigit, FailToReadTokens, RedefinedSymbol, WrongNumberOfOperands, UnidentifiedInstruction, IOException, UndefinedLabel, UndefinedAddressingMode, UnusedSymbols
    {
        Scanner scanner = new Scanner(new File(filename));
        LineHandler lineHandler = new LineHandler();
        while (scanner.hasNextLine()) {
            try {
                lineHandler.readLine(scanner);
            }
            catch (Exception e) {
                throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + e.getMessage());
            }

            if (lineHandler.isComentary() == false) {
                String label = lineHandler.getLabel();
                String mnemonic = lineHandler.getMnemonic();
                String operand1 = lineHandler.getOperand1();
                String operand2 = lineHandler.getOperand2();

                if (mnemonic.isBlank()){
                    throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + "expected a Instruction or Pseudo-Instruction at the second column!!!");
                }

                // Trata os rótulos
                if (label.isBlank() == false){
                    handleLabel(label);
                }

                // Trata as pseudo instruções e as intruções
                if (pseudoInstructionsTable.containsKey(mnemonic)){
                    PseudoInstruction pseudo_intruction = pseudoInstructionsTable.get(mnemonic);

                    switch (mnemonic) {
                        case "START":
                            if (addressCounter != 0){
                                throw new MalformedToken("ERROR at line " + lineCounter + ": The START pseudo instruction must be before all instructions and appear only once!!!");
                            }
                            if (operand1.isBlank() == false){
                                if (isStringInteger(operand1)){
                                    this.addressCounter = parseNumber(operand1);
                                }
                                else {
                                    throw new MalformedToken("ERROR at line " + lineCounter + ": Expected a number after START, got <" + operand1 + ">!!!");
                                }
                            }
                            if (label.isBlank() == false) {
                                if (definitionTable.containsKey(label)){
                                    TableEntry entry = definitionTable.get(label);
                                    entry.setAddress(entry.getAddress() + addressCounter);
                                }
                            }
                            // Aqui talvez tenha que ser passado para o ligador e posteriormente para o carregador
                            // a posição que começa a ser executado o programa
                            break;
            
                        case "END":
                            if (verifyAllSymbolsAreDefined(definitionTable) == false) {
                                throw new UndefinedLabel("ERROR at line " + lineCounter + ": Found a END pseudo instruction, but not all labels have been defined yet!!! Undefined labels are: "+ getUndefinedLabels(definitionTable));
                            }
                            // Pode ser apenas um warning
                            if (verifyAllInternalUseSymbolsAreUsed() == false){
                                throw new UnusedSymbols("ERROR at line " + lineCounter + ": Found the END of file, but there are symbols marked as internal use from other modules not being used!!! Symbols are: "+ getUnusedInternalSymbolTable());
                            }
                            this.saveGlobalSymbols(filename);
                            this.saveInternalUseTable(filename);
                            this.lineCounter = 1;
                            this.addressCounter = 0;
                            scanner.close();
                            secondStep(filename);
                            return;
            
                        case "INTDEF":
                            if (operand1.isBlank()){
                                throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Expected 1 operand after INTDEF, got 0!!!");
                            }
                            else if (operand2.isBlank() == false){
                                throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Expected 1 operand after INTDEF, got 2!!!");
                            }
                            
                            if (isStringInteger(operand1) == false && checkLabel(operand1) == true){
                                if (definitionTable.containsKey(operand1)){
                                    TableEntry entry = definitionTable.get(operand1);
                                    if (entry.getIsGlobal()){
                                        throw new RedefinedSymbol("ERROR at line " + lineCounter + ": The symbol <" + operand1 + "> was already in the defined as a Global Symbol!!!");
                                    }
                                    entry.setIsGlobal(true);
                                }
                                else {
                                    definitionTable.put(operand1, new TableEntry(operand1, null, true));
                                }
                            }
                            else {
                                throw new MalformedToken("ERROR at line " + lineCounter +": The symbol <" + operand1 + ">does not met the requirements for a label!!!");
                            }
                            break;
                            
                        case "INTUSE":
                            if (label.isBlank()){
                                throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Expected a label before INTUSE!!!");
                            }
                            if (operand1.isBlank() == false || operand2.isBlank() == false){
                                throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Expected no operands after INTUSE!!!");
                            }
                            if (internalUseTable.containsKey(label)){
                                throw new RedefinedSymbol("ERROR at line " + lineCounter + ": The label <" + label + "> was already in the Internal Use Table!!!");
                            }
                            else {
                                if (definitionTable.containsKey(label)){
                                    TableEntry entry = definitionTable.remove(label);
                                    if (entry.getIsGlobal()){
                                        throw new RedefinedSymbol("ERROR at line " + lineCounter + ": The symbol <" + label +"> is marked as Internal Use from other file, but Symbol was already marked as internal definition for use in other files!!!");
                                    }
                                    else {
                                        if (entry.getAddress() != null && entry.getAddress() != addressCounter){
                                            throw new RedefinedSymbol("ERROR at line " + lineCounter + ": The symbol <" + label +"> is marked as Internal Use from other file, but Symbol was already defined inside this file!!!");
                                        }
                                    }
                                }
                                internalUseTable.put(label, new InternalUseTableEntry(label));
                            }
                            break;
            
                        case "SPACE":
                            // A princípio não tem nada a fazer no primeiro passo para esta pseudo-instrução
                            break;
            
                        case "STACK":
                            // A princípio não tem nada a fazer no primeiro passo para esta pseudo-instrução
                            break;
            
                        case "CONST":
                            // A princípio não tem nada a fazer no primeiro passo para esta pseudo-instrução
                            break;
            
                        default:
                            throw new UnidentifiedInstruction("ERROR at line " + lineCounter + ": " + mnemonic + " is not a valid Pseudo-Instruction!!!");
                        }

                    addressCounter += pseudo_intruction.getInstructionSize();
                }
                else if (instructionsTable.containsKey(mnemonic)){
                    Instruction intruction = instructionsTable.get(mnemonic);
                    int number_of_operands_read = lineHandler.getNumberOfOperandsRead();
                    if (number_of_operands_read != intruction.getNumberOfOperands()){
                        throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": The instruction <"+intruction.getMnemonic()+"> expected " +intruction.getNumberOfOperands()+" operands and got "+number_of_operands_read+"!!!");
                    }

                    if (intruction instanceof OneOperandInstruction){
                        if (operand1.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                        }
                        operand1 = stripAddressingMode(operand1);
                        if (isStringInteger(operand1) == false){
                            if (internalUseTable.containsKey(operand1)){
                                internalUseTable.get(operand1).addOccurence(addressCounter + 1);
                            }
                            else if (definitionTable.containsKey(operand1) == false){
                                definitionTable.put(operand1, new TableEntry(operand1, null, false));
                            }
                        }
                    }
                    if (intruction instanceof TwoOperandInstruction){
                        if (operand2.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 2 not to be empty!!!");
                        }
                        operand2 = stripAddressingMode(operand2);
                        if (isStringInteger(operand2) == false){
                            if (internalUseTable.containsKey(operand2)){
                                internalUseTable.get(operand2).addOccurence(addressCounter + 2);
                            }
                            else if (definitionTable.containsKey(operand2) == false){
                                definitionTable.put(operand2, new TableEntry(operand2, null, false));
                            }
                        }
                    }

                    addressCounter += intruction.getInstructionSize();
                }
                else {
                    throw new UnidentifiedInstruction("ERROR at line " + lineCounter + ": " + mnemonic + " is not either a valid Instructions or a Pseudo-Instruction!!!");
                }
            }

            lineCounter++;
        }
        throw new EndNotFound("The END Pseudo Instruction was not found in the file");
    }
    
    private void secondStep(String filename)
    throws EndNotFound, LineTooLong, MalformedToken, InvalidDigit, FailToReadTokens, RedefinedSymbol, WrongNumberOfOperands, UnidentifiedInstruction, IOException, UndefinedLabel, UndefinedAddressingMode
    {
        Scanner scanner = new Scanner(new File(filename));
        
        File output = new File(filename.substring(0, filename.length() - 4) + ".OBJ");
        output.createNewFile();
        FileWriter assembled_file = new FileWriter(output);

        File list_output = new File(filename.substring(0, filename.length() - 4) + ".LST");
        list_output.createNewFile();
        FileWriter list_file = new FileWriter(list_output);
        
        LineHandler lineHandler = new LineHandler();
        while (scanner.hasNextLine()) {
            try {
                lineHandler.readLine(scanner);
            }
            catch (Exception e) {
                assembled_file.close();
                list_file.close();
                throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + e.getMessage());
            }

            if (lineHandler.isComentary() == false) {
                
                String mnemonic = lineHandler.getMnemonic();
                String operand1 = lineHandler.getOperand1();
                String operand2 = lineHandler.getOperand2();

                String line = "";

                if (mnemonic.isBlank()){
                    assembled_file.close();
                    list_file.close();
                    throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + "expected a Instruction or Pseudo-Instruction at the second column!!!");
                }

                // Trata das pseudo instruções e das intruções
                if (pseudoInstructionsTable.containsKey(mnemonic)){
                    PseudoInstruction pseudo_intruction = pseudoInstructionsTable.get(mnemonic);

                    switch (mnemonic) {
                    case "START":
                        if (operand1.isBlank() == false){
                            if (isStringInteger(operand1)){
                                this.addressCounter = parseNumber(operand1);
                            }
                            else {
                                throw new MalformedToken("ERROR at line " + lineCounter + ": Expected a number after START, got <" + operand1 + ">!!!");
                            }
                        }
                        // Aqui talvez tenha que ser passado para o ligador e posteriormente para o carregador
                        // a posição que começa a ser executado o programa
                        break;

                    case "END":
                        scanner.close();
                        assembled_file.close();
                        list_file.close();
                        return;

                    case "INTDEF":
                        // Nada a fazer no segundo passo para esta pseudo-instrução
                        break; 

                    case "INTUSE":
                        // Nada a fazer no segundo passo para esta pseudo-instrução
                        break;

                    case "SPACE":
                        line = "\t\t0\t|\t0\n";
                        assembled_file.write(line);
                        list_file.write("line: " + lineCounter + "\t addr: " + addressCounter + "\t#\t" +lineHandler.toString() + "\t-->>" + line.substring(line.indexOf("|", 0) +1));
                        break;

                    case "STACK":
                        // Não sei o que fazer para esta pseudo-instrução
                        break;

                    case "CONST":
                        Integer const_value;
                        if (checkLabel(operand1)){
                            Integer value = null;
                            if (definitionTable.containsKey(operand1)){
                                value = definitionTable.get(operand1).getAddress();
                            }

                            if (value == null) {
                                throw new UndefinedLabel("ERROR at line " + lineCounter +", second step: The label <"+operand1+"> is not defined!!!");
                            }
                            const_value = value;
                        }
                        else if (isStringInteger(operand1)){
                            const_value = parseNumber(operand1);
                        }
                        else {
                            throw new MalformedToken("ERROR at line " + lineCounter + ", second step: Unknown format of operand <"+operand1+">!!!");
                        }
                        line = "\t\t0\t|\t"+const_value.toString()+"\n";
                        assembled_file.write(line);
                        list_file.write("line: " + lineCounter + "\t addr: " + addressCounter + "\t#\t" +lineHandler.toString() + "\t-->>" + line.substring(line.indexOf("|", 0) +1));
                        break;

                    default:
                        throw new UnidentifiedInstruction("ERROR at line " + lineCounter + ": " + mnemonic + " is not a valid Pseudo-Instruction!!!");
                    }
                    addressCounter += pseudo_intruction.getInstructionSize();
                }
                else if (instructionsTable.containsKey(mnemonic)){
                    Instruction intruction = instructionsTable.get(mnemonic);

                    line = "\t\t0\t";
                    
                    if (intruction instanceof OneOperandInstruction){
                        Integer operand1_value = 0;
                        
                        AddressingMode operand1_AddressingMode;
                        
                        try {
                            operand1_AddressingMode = getOperandAddressingMode(operand1);
                        }
                        catch (MalformedToken malformedToken){
                            assembled_file.close();
                            list_file.close();
                            throw new MalformedToken("ERROR at line " + lineCounter + ", second step: "+ malformedToken.getMessage());
                        }
                        
                        if (operand1.isBlank()){
                            assembled_file.close();
                            list_file.close();
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                        }
                        if (isStringInteger(stripAddressingMode(operand1)) == false){
                            if (internalUseTable.containsKey(operand1)){
                                operand1_value = 0;
                            }
                            else if (definitionTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = definitionTable.get(stripAddressingMode(operand1)).getAddress();
                            }
                            else {
                                assembled_file.close();
                                list_file.close();
                                throw new UndefinedLabel("ERROR at line " + lineCounter + ": The label " + operand1 + " is not defined!!!");
                            }
                        }
                        else {
                            operand1_value = parseNumber(stripAddressingMode(operand1));
                        }

                        OneOperandInstruction one_opd_instruction = ((OneOperandInstruction) intruction);
                        try {
                            one_opd_instruction.setCurrentOperand1AddressingMode(operand1_AddressingMode);
                        }
                        catch (UndefinedAddressingMode undefinedAddressingMode) {
                            assembled_file.close();
                            list_file.close();
                            throw new UndefinedAddressingMode("ERROR at line " + lineCounter + ", second step: Wrong operand 1 addressing mode for instruction " + intruction.getMnemonic() + "!!!");
                        }

                        one_opd_instruction.setOperand1(operand1_value.shortValue());
                                   
                        // Creation of the reallocation mode part of the line
                        line = "\t" + line.strip();
                        line += "\t";
                        line += operand1_AddressingMode == AddressingMode.IMMEDIATE ? "0" : "1";
                        line += "\t";

                        if (intruction instanceof TwoOperandInstruction){
                            Integer operand2_value = 0;
                            
                            AddressingMode operand2_AddressingMode;
                            
                            try {
                                operand2_AddressingMode = getOperandAddressingMode(operand2);
                            }
                            catch (MalformedToken malformedToken){
                                assembled_file.close();
                                list_file.close();
                                throw new MalformedToken("ERROR at line " + lineCounter + ", second step: "+ malformedToken.getMessage());
                            }
                            
                            if (operand2.isBlank()){
                                assembled_file.close();
                                list_file.close();
                                throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                            }
                            if (isStringInteger(stripAddressingMode(operand2)) == false){
                                if (internalUseTable.containsKey(operand2)){
                                    operand2_value = 0;
                                }
                                else if (definitionTable.containsKey(stripAddressingMode(operand2))){
                                    operand2_value = definitionTable.get(stripAddressingMode(operand2)).getAddress();
                                }
                                else {
                                    assembled_file.close();
                                    list_file.close();
                                    throw new UndefinedLabel("ERROR at line " + lineCounter + ": The label " + operand2 + " is not defined!!!");
                                }
                            }
                            else {
                                operand2_value = parseNumber(stripAddressingMode(operand2));
                            }
    
                            TwoOperandInstruction two_opd_instruction = ((TwoOperandInstruction) intruction);
                            try {
                                two_opd_instruction.setCurrentOperand2AddressingMode(operand2_AddressingMode);
                            }
                            catch (UndefinedAddressingMode undefinedAddressingMode) {
                                assembled_file.close();
                                list_file.close();
                                throw new UndefinedAddressingMode("ERROR at line " + lineCounter + ", second step: Wrong operand 1 addressing mode for instruction " + intruction.getMnemonic() + "!!!");
                            }
    
                            two_opd_instruction.setOperand2(operand2_value.shortValue());
    
                            // Creation of the reallocation mode part of the line
                            line = line.strip();
                            line += "\t";
                            line += operand2_AddressingMode == AddressingMode.IMMEDIATE ? "0" : "1";
                            line += "\t";
                        }
                    }
                    
                    line += "|\t";
                    line += intruction.toDecimalString() + "\n";

                    assembled_file.write(line);
                    list_file.write("line: " + lineCounter + "\t addr: " + addressCounter + "\t#\t" +lineHandler.toString() + "\t-->>" + line.substring(line.indexOf("|", 0) +1));

                    addressCounter += intruction.getInstructionSize();
                }
                else {
                    assembled_file.close();
                    list_file.close();
                    throw new UnidentifiedInstruction("ERROR at line " + lineCounter + ": " + mnemonic + " is not either a valid Instructions or a Pseudo-Instruction!!!");
                }
            }
            lineCounter++;
        }
        assembled_file.close();
        list_file.close();
        throw new EndNotFound("The END Pseudo Instruction was not found in the file");
    }

    private int parseNumber(String number) throws NumberFormatException {
        int result = 0;
        
        if (number.matches("\\d+")){
            result = Integer.parseInt(number);
        }
        else if (number.matches("H'[0-9A-F]+'")){
            result = Integer.parseInt(number, 2, number.length() - 1, 16);
        }
        else if (number.matches("@\\d+")){
            // Literal em decimal what the fuck??
        }
        else {
            throw new NumberFormatException("Unknown number format " + number);
        }

        return result;
    }

    private boolean isStringInteger(String string){
        return string.matches("\\d+") || string.matches("H'[0-9A-F]+'") || string.matches("@\\d+");
    }

    private boolean checkLabel(String label){
        return label.matches("\\b[A-Za-z_]\\w*") && label.length() < Assembler.MAX_LABEL_SIZE;
    }
    private String stripAddressingMode(String operand) {
        String result = operand;
        if (result.startsWith("#")){
            result = result.substring(1);
        }
        if (result.endsWith(",I")){
            result = result.substring(0, result.indexOf(",I", 0));
        }
        return result;
    }
    private AddressingMode getOperandAddressingMode(String operand) throws MalformedToken {
        AddressingMode result = AddressingMode.DIRECT;
        if (operand.startsWith("#")){
            result = AddressingMode.IMMEDIATE;
        }
        if (operand.endsWith(",I")){
            result = AddressingMode.INDIRECT;
        }

        if (operand.startsWith("#") &&
            operand.endsWith(",I")
        ){
            throw new MalformedToken("The operand <" +operand + "> cannot have two distinct addressing modes!!!");
        }
        return result;
    }
    private boolean verifyAllSymbolsAreDefined(HashMap<String, TableEntry> table){
        for (TableEntry entry : table.values()) {
            if (entry.getAddress() == null) return false;
        }
        return true;
    }
    private String getUndefinedLabels(HashMap<String, TableEntry> table){
        String result = "";
        for (TableEntry entry : table.values()) {
            if (entry.getAddress() == null) result += entry.getLabel() + " ";
        }
        return result;
    }
    private void saveInternalUseTable(String filename) throws IOException {
        File output = new File(filename.substring(0, filename.lastIndexOf(".")) + ".USE");
        output.createNewFile();
        FileWriter use_table_file = new FileWriter(output);
        
        // Pular os símbolos que não são utilizados?
        for (InternalUseTableEntry entry : internalUseTable.values()) {
            use_table_file.write(entry.getLabel() + "\t" + entry.getOccurences().toString() + "\n");
        }
        use_table_file.close();
    }
    private void saveGlobalSymbols(String filename) throws IOException {
        File output = new File(filename.substring(0, filename.lastIndexOf(".")) + ".GLO");
        output.createNewFile();
        FileWriter global_definition_table = new FileWriter(output);
        
        // Pular os símbolos que não são utilizados?
        for (TableEntry entry : definitionTable.values()) {
            if (entry.getIsGlobal()){
                global_definition_table.write(entry.getLabel() + "\t" + entry.getAddress() + "\n");
            }
        }
        global_definition_table.close();
    }
    private boolean verifyAllInternalUseSymbolsAreUsed(){
        for (InternalUseTableEntry entry : internalUseTable.values()) {
            if (entry.getOccurences().isEmpty()) return false;
        }
        return true;
    }
    private String getUnusedInternalSymbolTable(){
        String result = "";
        for (InternalUseTableEntry entry : internalUseTable.values()) {
            if (entry.getOccurences().isEmpty()) result += entry.getLabel() + " ";
        }
        return result;
    }
    private void handleLabel(String label) throws MalformedToken, RedefinedSymbol {
        if (checkLabel(label) == false){
            throw new MalformedToken("ERROR at line " + lineCounter + ": The label <" + label + "> is malformed");
        }
        
        if (definitionTable.containsKey(label)){
            TableEntry entry = definitionTable.get(label);
            if (entry.getAddress() != null){
                throw new RedefinedSymbol("ERROR at line " + lineCounter + ": redefinition of symbol <" + label + ">!!! Previous value = " + entry.getAddress() + ", new value = " + addressCounter);
            }
            else {
                entry.setAddress(addressCounter);
            }
        }
        else {
            definitionTable.put(label, new TableEntry(label, addressCounter, false));
        }
    }
}
