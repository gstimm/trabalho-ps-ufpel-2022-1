package assembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Scanner;

import assembler.errors.EndNotFound;
import assembler.errors.FailToReadTokens;
import assembler.errors.InvalidDigit;
import assembler.errors.LineTooLong;
import assembler.errors.MalformedToken;
import assembler.errors.WrongNumberOfOperands;
import assembler.errors.RedefinedSymbol;
import assembler.errors.UndefinedLabel;
import assembler.errors.UnidentifiedInstruction;
import assembler.errors.UnusedSymbols;
import assembler.pseudo_instructions.*;
import main.instructions.*;
import main.AddressingMode;
import main.Instruction;
import main.OneOperandInstruction;
import main.TwoOperandInstruction;
import main.errors.UndefinedAddressingMode;

public class Assembler {
    private HashMap<String, Instruction> instructionsTable;
    private HashMap<String, PseudoInstruction> pseudoInstructionsTable;
    private int lineCounter;
    private int addressCounter;
    public static final int MAX_LINE_SIZE = 80;

    // Símbolos que são definidos no módulo e utilizados apenas pelo módulo
    private HashMap<String, TableEntry> internalDefinitionTable;
    
    // Símbolos usados internamente e definidos em outro módulo
    private HashMap<String, InternalUseTableEntry> internalUseTable;

    // Símbolos definidos no módulo atual para uso externo
    private HashMap<String, TableEntry> globalDefinitionTable;

    public Assembler(){
        this.instructionsTable = new HashMap<>();
        this.pseudoInstructionsTable = new HashMap<>();
        this.lineCounter = 0;
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

        this.globalDefinitionTable = new HashMap<>();
        this.internalDefinitionTable = new HashMap<>();
        this.internalUseTable = new HashMap<>();
    }

    public void assemble(String fileName) throws FileNotFoundException {
        try {
            firstStep(fileName);
        }
        catch (EndNotFound endNotFound){
            System.out.println(endNotFound.getMessage());
            endNotFound.printStackTrace();
            System.exit(-1);
        }
        catch (InvalidDigit invalidDigit){
            System.out.println(invalidDigit.getMessage());
            invalidDigit.printStackTrace();
            System.exit(-1);
        }
        catch (RedefinedSymbol redefinedSymbol){
            System.out.println(redefinedSymbol.getMessage());
            redefinedSymbol.printStackTrace();
            System.exit(-1);
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
            catch (LineTooLong lineTooLong) {
                throw new LineTooLong("ERROR at line " + lineCounter + ": " + lineTooLong.getMessage());
            }
            catch (FailToReadTokens failToReadTokens){
                throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + failToReadTokens.getMessage());
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
                    if (checkLabel(label) == false){
                        throw new MalformedToken("ERROR at line " + lineCounter + ": The label <" + label + "> is malformed");
                    }
                    if (globalDefinitionTable.containsKey(label)){
                        TableEntry entry = globalDefinitionTable.get(label);
                        if (entry.getAddress() != null){
                            throw new RedefinedSymbol("ERROR at line " + lineCounter + ": redefinition of symbol <" + label + ">!!! Previous value = " + entry.getAddress() + ", new value = " + addressCounter);
                        }
                        else {
                            entry.setAddress(addressCounter);
                        }
                    }
                    else if (internalDefinitionTable.containsKey(label)){
                        TableEntry entry = internalDefinitionTable.get(label);
                        if (entry.getAddress() != null){
                            throw new RedefinedSymbol("ERROR at line " + lineCounter + ": redefinition of symbol <" + label + ">!!! Previous value = " + entry.getAddress() + ", new value = " + addressCounter);
                        }
                        else {
                            entry.setAddress(addressCounter);
                        }
                    }
                    else {
                        internalDefinitionTable.put(label, new TableEntry(label, addressCounter, 'r'));
                    }
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
                        if (verifyTableAllSymbolsAreDefined(globalDefinitionTable) == false || verifyTableAllSymbolsAreDefined(internalDefinitionTable) == false) {
                            throw new UndefinedLabel("ERROR at line " + lineCounter + ": Found a END pseudo instruction, but not all labels have been defined yet!!! Undefined labels are: "+ getUndefinedLabels(globalDefinitionTable) + getUndefinedLabels(internalDefinitionTable));
                        }
                        // Pode ser apenas um warning
                        if (verifyAllInternalUseSymbolsAreUsed() == false){
                            throw new UnusedSymbols("ERROR at line " + lineCounter + ": Found the END of file, but there are symbols marked as internal use from other modules not being used!!! Symbols are: "+ getUnusedInternalSymbolTable());
                        }
                        this.saveInternalUseTable(filename);
                        this.lineCounter = 0;
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
                            if (internalDefinitionTable.containsKey(operand1)){
                                internalDefinitionTable.remove(operand1);
                            }
                            if (globalDefinitionTable.containsKey(operand1) == false){
                                globalDefinitionTable.put(operand1, new TableEntry(operand1, null, 'r'));
                            }
                            else {
                                throw new RedefinedSymbol("ERROR at line " + lineCounter + ": The symbol <" + operand1 + "> was already in the Global Definition Table!!!");
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
                            internalUseTable.put(label, new InternalUseTableEntry(label));
                        }
                        break;

                    case "SPACE":
                        // A princípio não tem nada a fazer no primeiro passo para esta pseudo-instrução
                        break;

                    case "STACK":
                        // Não sei o que fazer para esta pseudo-instrução
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

                    if (intruction instanceof OneOperandInstruction){
                        if (operand1.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                        }
                        operand1 = stripAddressingMode(operand1);
                        if (isStringInteger(operand1) == false){
                            if (internalUseTable.containsKey(operand1)){
                                internalUseTable.get(operand1).addOccurence(addressCounter + 1);
                            }
                            else if (globalDefinitionTable.containsKey(operand1) == false &&
                                     internalDefinitionTable.containsKey(operand1) == false
                            ){
                                internalDefinitionTable.put(operand1, new TableEntry(operand1, null, 'r'));
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
                            else if (globalDefinitionTable.containsKey(operand2) == false &&
                                     internalDefinitionTable.containsKey(operand2) == false
                            ){
                                internalDefinitionTable.put(operand2, new TableEntry(operand1, null, 'r'));
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
        
        File output = new File(System.getProperty("java.class.path").split(";")[0] + "/assembler/" + filename.substring(filename.lastIndexOf("/"), filename.lastIndexOf(".")) + ".OBJ");
        output.createNewFile();
        FileWriter assembled_file = new FileWriter(output);

        File list_output = new File(System.getProperty("java.class.path").split(";")[0] + "/assembler/" + filename.substring(filename.lastIndexOf("/"), filename.lastIndexOf(".")) + ".LST");
        list_output.createNewFile();
        FileWriter list_file = new FileWriter(list_output);
        
        LineHandler lineHandler = new LineHandler();
        while (scanner.hasNextLine()) {
            try {
                lineHandler.readLine(scanner);
            }
            catch (LineTooLong lineTooLong) {
                throw new LineTooLong("ERROR at line " + lineCounter + ": " + lineTooLong.getMessage());
            }
            catch (FailToReadTokens failToReadTokens){
                throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + failToReadTokens.getMessage());
            }

            if (lineHandler.isComentary() == false) {
                String label = lineHandler.getLabel();
                String mnemonic = lineHandler.getMnemonic();
                String operand1 = lineHandler.getOperand1();
                String operand2 = lineHandler.getOperand2();

                String line = "";

                if (mnemonic.isBlank()){
                    throw new FailToReadTokens("ERROR at line " + lineCounter + ": " + "expected a Instruction or Pseudo-Instruction at the second column!!!");
                }

                // Todo rótulo já deve ter sido definido neste ponto
                if (label.isBlank() == false){
                    if (checkLabel(label) == false){
                        throw new MalformedToken("ERROR at line " + lineCounter + ": The label <" + label + "> is malformed");
                    }
                    if (globalDefinitionTable.containsKey(label)){
                        if (globalDefinitionTable.get(label).getAddress() == null){
                            throw new UndefinedLabel("ERROR at line " + lineCounter + ", in second step: The label <"+ label + "> should aready be defined at this point!!!");
                        }
                    }
                    if (internalDefinitionTable.containsKey(label)){
                        if (internalDefinitionTable.get(label).getAddress() == null){
                            throw new UndefinedLabel("ERROR at line " + lineCounter + ", in second step: The label <"+ label + "> should aready be defined at this point!!!");
                        }
                    }
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
                        // Aqui deve ser salvo em arquivos (binário ou texto) que serão utilizados pelo ligador 
                        // Salvar as tabelas de definições globais e a de usos internos
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
                            if (globalDefinitionTable.containsKey(operand1)){
                                value = globalDefinitionTable.get(operand1).getAddress();
                            }
                            else if (internalDefinitionTable.containsKey(operand1)){
                                value = internalDefinitionTable.get(operand1).getAddress();
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

                    if (intruction instanceof TwoOperandInstruction){
                        Integer operand1_value = null;
                        Integer operand2_value = null;
                        
                        AddressingMode operand1_AddressingMode;
                        AddressingMode operand2_AddressingMode;
                        
                        try {
                            operand1_AddressingMode = getOperandAddressingMode(operand1);
                            operand2_AddressingMode = getOperandAddressingMode(operand2);
                        }
                        catch (MalformedToken malformedToken){
                            throw new MalformedToken("ERROR at line " + lineCounter + ", second step: "+ malformedToken.getMessage());
                        }
                        
                        if (operand1.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                        }
                        if (isStringInteger(stripAddressingMode(operand1)) == false){
                            if (internalUseTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = null;
                            }
                            else if (globalDefinitionTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = globalDefinitionTable.get(stripAddressingMode(operand1)).getAddress();
                            }
                            else if (internalDefinitionTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = internalDefinitionTable.get(stripAddressingMode(operand1)).getAddress();
                            } 
                            else {
                                throw new UndefinedLabel("ERROR at line " + lineCounter + ": The label " + operand1 + " is not defined!!!");
                            }
                        }
                        else {
                            operand1_value = parseNumber(stripAddressingMode(operand1));
                        }

                        if (operand2.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 2 not to be empty!!!");
                        }
                        if (isStringInteger(stripAddressingMode(operand2)) == false){
                            if (internalUseTable.containsKey(stripAddressingMode(operand2))){
                                operand2_value = null;
                            }
                            else if (globalDefinitionTable.containsKey(stripAddressingMode(operand2))){
                                operand2_value = globalDefinitionTable.get(stripAddressingMode(operand2)).getAddress();
                            }
                            else if (internalDefinitionTable.containsKey(stripAddressingMode(operand2))){
                                operand2_value = internalDefinitionTable.get(stripAddressingMode(operand2)).getAddress();
                            }
                            else {
                                throw new UndefinedLabel("ERROR at line " + lineCounter + ": The label " + operand2 + " is not defined!!!");
                            }
                        }
                        else {
                            operand2_value = parseNumber(stripAddressingMode(operand2));
                        }

                        // Line creation and write to file
                        TwoOperandInstruction two_opd_instruction = ((TwoOperandInstruction) intruction);
                        try {
                            two_opd_instruction.setCurrentOperand1AddressingMode((short) AddressingMode.opcodeByAddressingMode(intruction.getOpcode() , operand1_AddressingMode, null));
                        }
                        catch (UndefinedAddressingMode undefinedAddressingMode) {
                            throw new UndefinedAddressingMode("ERROR at line " + lineCounter + ", second step: Wrong operand 1 addressing mode for instruction " + intruction.getMnemonic() + "!!!");
                        }
                        try {
                            two_opd_instruction.setCurrentOperand2AddressingMode((short) AddressingMode.opcodeByAddressingMode(intruction.getOpcode() , null, operand2_AddressingMode));
                        }
                        catch (UndefinedAddressingMode undefinedAddressingMode) {
                            throw new UndefinedAddressingMode("ERROR at line " + lineCounter + ", second step: Wrong operand 2 addressing mode for instruction " + intruction.getMnemonic() + "!!!");
                        }

                        // Creation of the realocation part
                        line = "0\t";
                        line += operand1_AddressingMode == AddressingMode.IMMEDIATE ? "0" : "1";
                        line += "\t";
                        line += operand2_AddressingMode == AddressingMode.IMMEDIATE ? "0" : "1";
                        line += "\t|\t";

                        // Creation of the mnemonic and operators part
                        line += (short) AddressingMode.opcodeByAddressingMode(intruction.getOpcode() , operand1_AddressingMode, operand2_AddressingMode);
                        line += "\t";
                        line += operand1_value == null ? stripAddressingMode(operand1) : operand1_value;
                        line += "\t";
                        line += operand2_value == null ? stripAddressingMode(operand2) : operand2_value;
                        line += "\n";
                    }
                    else if (intruction instanceof OneOperandInstruction){
                        Integer operand1_value = null;
                        
                        AddressingMode operand1_AddressingMode;
                        
                        try {
                            operand1_AddressingMode = getOperandAddressingMode(operand1);
                        }
                        catch (MalformedToken malformedToken){
                            throw new MalformedToken("ERROR at line " + lineCounter + ", second step: "+ malformedToken.getMessage());
                        }
                        
                        if (operand1.isBlank()){
                            throw new WrongNumberOfOperands("ERROR at line " + lineCounter + ": Instruction <"+ intruction.getMnemonic() +"> expects operand 1 not to be empty!!!");
                        }
                        if (isStringInteger(stripAddressingMode(operand1)) == false){
                            if (internalUseTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = null;
                            }
                            else if (globalDefinitionTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = globalDefinitionTable.get(stripAddressingMode(operand1)).getAddress();
                            }
                            else if (internalDefinitionTable.containsKey(stripAddressingMode(operand1))){
                                operand1_value = internalDefinitionTable.get(stripAddressingMode(operand1)).getAddress();
                            }
                            else {
                                throw new UndefinedLabel("ERROR at line " + lineCounter + ": The label " + operand1 + " is not defined!!!");
                            }
                        }
                        else {
                            operand1_value = parseNumber(stripAddressingMode(operand1));
                        }

                        // Line creation and write to file
                        OneOperandInstruction one_opd_instruction = ((OneOperandInstruction) intruction);
                        try {
                            one_opd_instruction.setCurrentOperand1AddressingMode((short) AddressingMode.opcodeByAddressingMode(intruction.getOpcode() , operand1_AddressingMode, null));
                        }
                        catch (UndefinedAddressingMode undefinedAddressingMode) {
                            throw new UndefinedAddressingMode("ERROR at line " + lineCounter + ", second step: Wrong operand 1 addressing mode for instruction " + intruction.getMnemonic() + "!!!");
                        }

                        // Creation of the realocation part
                        line = "\t";
                        line += "0\t";
                        line += operand1_AddressingMode == AddressingMode.IMMEDIATE ? "0" : "1";
                        line += "\t|\t";

                        // Creation of the mnemonic and operators part
                        line += (short) AddressingMode.opcodeByAddressingMode(intruction.getOpcode() , operand1_AddressingMode, null);
                        line += "\t";
                        line += operand1_value == null ? stripAddressingMode(operand1) : operand1_value;
                        line += "\n";
                    }
                    else {
                        line = "\t\t0\t|\t" + intruction.toDecimalString() + "\n";
                    }

                    assembled_file.write(line);
                    list_file.write("line: " + lineCounter + "\t addr: " + addressCounter + "\t#\t" +lineHandler.toString() + "\t-->>" + line.substring(line.indexOf("|", 0) +1));

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
        return label.matches("\\b[A-Za-z_]\\w*") && label.length() < 8;
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
    private boolean verifyTableAllSymbolsAreDefined(HashMap<String, TableEntry> table){
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
}
