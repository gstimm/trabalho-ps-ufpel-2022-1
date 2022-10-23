package macro;

import java.util.ArrayList;
import java.util.Scanner;

import assembler.errors.FailToReadTokens;
import assembler.errors.LineTooLong;
import assembler.Assembler;

public class LineHandlerMacro {
  private String label;
  private String mnemonic;
  ArrayList<String> operands = new ArrayList<String>();
  private boolean isComentary;

  public void LineHandler(){
    resetVaules();
  }
  
  public void readLine(Scanner scanner) throws LineTooLong, FailToReadTokens {
    if (scanner.hasNext()) {
      this.isComentary = false;
      this.operands.clear();

      String line = scanner.nextLine();

      if(line.length() > Assembler.MAX_LINE_SIZE){
        throw new LineTooLong("Line is bigger than the defined maximum size. Line size: " + line.length() + ", maximum allowed line size: " + Assembler.MAX_LINE_SIZE + "!!!");
      }

      String tokens[] = line.split("\\s+");

      if(tokens.length == 0) {
        throw new FailToReadTokens("Not possible to read the line tokens");
      }

      if (tokens.length > 0) {
        if (tokens[0].equals("*")){
            this.isComentary = true;
            return;
        }
        this.label = tokens[0];
      }

      if (tokens.length > 1) {
        if(tokens[1].equals("*")){
          return;
        }
        this.mnemonic = tokens[1];
      }

      if (tokens.length > 2) {
        for (int i = 2; i < tokens.length; i++) {
          if(tokens[1].equals("*")){
            return;
          }
          this.operands.add(tokens[i]);
        }
      }
    }
  }

  public void readLine(String line) throws LineTooLong, FailToReadTokens {
    this.isComentary = false;
    this.operands.clear();

    if(line.length() > Assembler.MAX_LINE_SIZE){
      throw new LineTooLong("Line is bigger than the defined maximum size. Line size: " + line.length() + ", maximum allowed line size: " + Assembler.MAX_LINE_SIZE + "!!!");
    }

    String tokens[] = line.split("\\s+");

    if(tokens.length == 0) {
      throw new FailToReadTokens("Not possible to read the line tokens");
    }

    if (tokens.length > 0) {
      if (tokens[0].equals("*")){
          this.isComentary = true;
          return;
      }
      this.label = tokens[0];
    }

    if (tokens.length > 1) {
      if(tokens[1].equals("*")){
        return;
      }
      this.mnemonic = tokens[1];
    }

    if (tokens.length > 2) {
      for (int i = 2; i < tokens.length; i++) {
        if(tokens[1].equals("*")){
          return;
        }
        this.operands.add(tokens[i]);
      }
    }
  }

  public void resetVaules() {
    this.label = "";
    this.mnemonic = "";
    operands.clear();
    this.isComentary = false;
  }

  public int getNumberOfOperandsRead() {
    return operands.size();
  }
    
  public String getLabel() {
      return label;
  }

  public String getMnemonic() {
      return mnemonic;
  }

  public boolean isComentary() {
      return isComentary;
  }

  public ArrayList<String> getOperands() {
      return operands;
  }

  public String getOperand(int index) {
      return operands.get(index);
  }

  @Override
  public String toString() {
    String result = this.label + "\t" + 
                  this.mnemonic + "\t";
    for (String operand : operands) {
      result += operand + "\t";
    }
    return result;
  }
}