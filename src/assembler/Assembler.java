package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import assembler.errors.EndNotFound;
import assembler.errors.InvalidDigit;
import assembler.errors.NotEnoughOperands;
import assembler.errors.RedefinedSymbol;
import assembler.errors.UnidentifiedInstruction;
import assembler.pseudo_instructions.*;
import main.instructions.*;
import main.Instruction;
import main.OneOperandInstruction;
import main.TwoOperandInstruction;

public class Assembler {
    private HashMap<String, Instruction> instructionsTable;
    private HashMap<String, PseudoInstruction> pseudoInstructionsTable;
    private int lineCounter;
    private int addressCounter;
    public static final int MAX_LINE_SIZE = 80;

    // Símbolos que são definidos no módulo e utilizados apenas pelo módulo
    private HashMap<String, Integer> internalDefinitionTable;
    
    // Símbolos usados internamente e definidos em outro módulo
    private HashMap<String, Integer> internalUseTable;

    // Símbolos definidos no módulo atual para uso externo
    private HashMap<String, Integer> globalDefinitionTable;

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
    }

    public void assemble(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        try {
            firstStep(scanner);
        }
        catch (EndNotFound endNotFound){
            endNotFound.printStackTrace();
            System.exit(-1);
        }
        catch (InvalidDigit invalidDigit){
            invalidDigit.printStackTrace();
            System.exit(-1);
        }
        catch (RedefinedSymbol redefinedSymbol){
            redefinedSymbol.printStackTrace();
            System.exit(-1);
        }
    }

    private void firstStep(Scanner scanner) throws EndNotFound, InvalidDigit, RedefinedSymbol {
        LineHandler lineHandler = new LineHandler();
        while (scanner.hasNextLine()) {
            try {
                lineHandler.readLine(scanner);
                if (lineHandler.isComentary() == false){
                    if (pseudoInstructionsTable.containsKey(lineHandler.getMnemonic())){
                        switch (lineHandler.getMnemonic()) {
                            case "CONST":
                            
                                break;
                            case "END":
                                secondStep(scanner.reset());
                                return;
                            case "INTDEF":
                                // Declara no arquivo atual uma variável que será utilizada externamente
                                if (globalDefinitionTable.containsKey(lineHandler.getOperand1())){
                                    throw new RedefinedSymbol("The symbol " + lineHandler.getOperand1() + " is being redefined!!!");
                                }
                                globalDefinitionTable.put(lineHandler.getOperand1(), null);
                                break;
                            case "INTUSE":
                                // Utiliza no arquivo atual uma variável definida em outro módulo
                                if (internalUseTable.containsKey(lineHandler.getMnemonic())){
                                    throw new RedefinedSymbol("The symbol " + lineHandler.getOperand1() + " is being redefined!!!");
                                }
                                internalUseTable.put(lineHandler.getMnemonic(), null);
                                break;
                            case "SPACE":
                            
                                break;
                            case "STACK":
                                //Define tamanho da pilha do módulo
                                
                                break;
                            case "START":
                                
                                break;
                            default:
                                break;
                        }
                        // Interpretar a pseudo instrução, switch case gigante
                    }
                    else if (instructionsTable.containsKey(lineHandler.getMnemonic())){
                        // Procurar os operadores e colocar nas tabelas respectivas
                        Instruction instruction = instructionsTable.get(lineHandler.getMnemonic());
                        if (instruction instanceof OneOperandInstruction) {
                            if (lineHandler.getOperand1().isBlank()){
                                throw new NotEnoughOperands("The line has not the number of operands expected!!! Expected 1 operand.");
                            }
                            // Verificar se o operando 1 é um número ou uma 'variável'
                            // Se for uma 'variável', procurar nas tabelas se ela está lá,
                            // Se não estiver adicionar
                        }
                        if (instruction instanceof TwoOperandInstruction){
                            if (lineHandler.getOperand2().isBlank()){
                                throw new NotEnoughOperands("The line has not the number of operands expected!!! Expected 2 operand.");
                            }
                            // Verificar se o operando 2 é um número ou uma 'variável'
                            // Se for uma 'variável', procurar nas tabelas se ela está lá,
                            // Se não estiver adicionar
                        }
                    }
                    else {
                        throw new UnidentifiedInstruction("The instruction with mnemonic " + lineHandler.getMnemonic() + " is not either a Pseudo Instruction or a Instruction");
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
        throw new EndNotFound("The END Pseudo Instruction was not found in the file");
    }
    
    private void secondStep(Scanner scanner){
        

    }

    private static int parseNumber(String number) throws InvalidDigit, NumberFormatException {
        int result = 0;
        try {
            result = Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            if (number.startsWith("H'") && number.endsWith("'")){
                try {
                    result = Integer.parseInt(number, 2, number.length() - 1, 16);
                }
                catch (Exception exception) {
                    throw new InvalidDigit("The number " + number + " contains a invalid digit!!!");
                }
            }
            else if (number.startsWith("@")){
                // Literal em decimal what the fuck??
            }
            else {
                throw new NumberFormatException();
            }
        }
        return result;
    }

}
