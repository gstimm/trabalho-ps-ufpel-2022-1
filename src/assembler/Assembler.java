package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import assembler.errors.EndNotFound;
import assembler.errors.FailToReadTokens;
import assembler.errors.InvalidDigit;
import assembler.errors.LineTooLong;
import assembler.errors.MalformedToken;
import assembler.errors.WrongNumberOfOperands;
import assembler.errors.RedefinedSymbol;
import assembler.errors.UnidentifiedInstruction;
import assembler.pseudo_instructions.*;
import main.instructions.*;
import main.Instruction;
import main.OneOperandInstruction;
import main.TwoOperandInstruction;
import main.errors.UnknownInstrucion;

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
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        try {
            firstStep(scanner);
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

    private void firstStep(Scanner scanner) 
    throws EndNotFound, LineTooLong, MalformedToken, InvalidDigit, FailToReadTokens, RedefinedSymbol, WrongNumberOfOperands, UnidentifiedInstruction 
    {
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
                        // Aqui ainda tem que verificar se todos os valores da tabela de definição interna estão definidos a.k.a. são diferentes de null
                        this.lineCounter = 0;
                        this.addressCounter = 0;
                        secondStep(scanner.reset());
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
    
    private void secondStep(Scanner scanner){
        // As pseudo instruções CONST e SPACE provavelmente vão ter que implementar o método
        // toBinaryString, porque elas vão estar no cógigo montado (CONST terá o valor e SPACE terá 0) ?!
    }

    private int parseNumber(String number) throws NumberFormatException {
        int result = 0;
        
        if (isStringInteger(number)){
            result = Integer.parseInt(number);
        }
        else {
            if (number.startsWith("H'") && number.endsWith("'")){
                result = Integer.parseInt(number, 2, number.length() - 1, 16);
            }
            else if (number.startsWith("@")){
                // Literal em decimal what the fuck??
            }
            else {
                throw new NumberFormatException("Unknown number format " + number);
            }
        }

        return result;
    }

    private boolean isStringInteger(String string){
        return string.matches("\\d+");
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
}
