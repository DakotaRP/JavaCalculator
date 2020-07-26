package com.dakota.calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.text.DecimalFormat;

public class CalculatorController {

    // Set up FX GUI elements
    @FXML
    private Label outputLabel;

    // Initialize global variables
    private final String DEFAULT_OUTPUT = "0";
    // false if no entry is detected, true if so
    private static boolean hasEntry = false;
    // false if operator has not been entered, true if so
    private static boolean hasOperator = false;
    // false if enter has not been pressed, true if so
    private static boolean priorCalc = false;
    // current string of text being formed prior to operator selection or calculation (=)
    private static String inputString = "";
    // saved value in memory
    private static double memory = 0.0;

    // formatting for proper comma output
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.0######");

    private static double num1 = 0.0;
    private static double num2 = 0.0;
    private static double result = 0.0;
    private static String operator = "";

    // uncomment all of this below after figuring out how to write and read from a file
    // (right now I don't know how to check if one exists before reading)
//    // write variables to file that will be read when program starts up next time
//    public static void writeToFile() throws IOException {
//        FileOutputStream outStream = new FileOutputStream("calculator.dat");
//        DataOutputStream outputFile = new DataOutputStream(outStream);
//
//        System.out.println("Starting to write");
//        outputFile.writeBoolean(hasEntry);
//        outputFile.writeBoolean(hasOperator);
//        outputFile.writeBoolean(priorCalc);
//        outputFile.writeUTF(inputString);
//        outputFile.writeDouble(memory);
//        outputFile.writeDouble(num1);
//        outputFile.writeDouble(num2);
//        outputFile.writeDouble(result);
//        outputFile.writeUTF(operator);
//        System.out.println("Finished writing to file");
//        outputFile.close();
//    } // end writeToFile()
//
//    // read variables from file on startup
//    public static void readFromFile() throws IOException{
//        FileInputStream inStream = new FileInputStream("calculator.dat");
//        DataInputStream inputFile = new DataInputStream(inStream);
//
//        // authenticate that the file exists somehow???
//        // then read from it

//        System.out.println("Starting to read from file");
//        hasEntry = inputFile.readBoolean();
//        hasOperator = inputFile.readBoolean();
//        priorCalc = inputFile.readBoolean();
//        inputString = inputFile.readUTF();
//        memory = inputFile.readDouble();
//        num1 = inputFile.readDouble();
//        num2 = inputFile.readDouble();
//        result = inputFile.readDouble();
//        operator = inputFile.readUTF();
//        System.out.println("Finished reading from file");
//        inputFile.close();
//    }

//    // setup outputs on calculator (only does something if the file exists)
//    public void initializeProgram(){
//        // if input ever existed
//        // else do nothing
//        if(hasEntry){
//            // remove place holding text from outputLabel
//            outputLabel.setText("");
//
//            // if number1 was still being worked on
//            if(!hasOperator){
//                outputLabel.setText(decimalFormat.format(num1));
//            } else if(num2 == 0){   // operator has been entered and num2 has not been edited, output operator
//                if(operator.equals("²")){
//                    outputLabel.setText(decimalFormat.format(num1) + operator);
//                } else if(operator.equals("√")) {
//                    outputLabel.setText(operator + decimalFormat.format(num1));
//                } else outputLabel.setText(decimalFormat.format(num1) + " " + operator);    // output normal operator
//            } else if(num2 != 0){   // operator has been entered and num2 has been edited
//                outputLabel.setText(decimalFormat.format(num2));
//            }
//        };
//    } // end initializeProgram()

    // Determine what happens when buttons are clicked
    public void onMouseClick(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource(); // cast object returned from getSource() to a Button
        String input = button.getText();       // get/store text from button pressed into String for processing

        switch(input){
            case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "0": case ".":
                insert(input);
                break;
            case "+": case "-": case "*": case "/":
                enterBasicOperator(input);
                break;
            case "X²":
                squareNumber();
                break;
            case "√":
                squareRootOfNumber();
                break;
            case "=":
                calculate();
                break;
            case "C":
                clearAll();
                break;
            case "CE":
                clearLastEntry();
                break;
            case "+/-":
                // convert from negative to positive and vice versa
                changeSign();
                break;
            case "MS":
                memoryStore();
                break;
            case "MC":
                memoryClear();
                break;
            case "MR":
                memoryRecall();
                break;
            case "M+":
                memoryAdd();
                break;
            case "M-":
                memorySubtract();
                break;

        }
    }

    // Insert the text of any button pressed into the outputLabel
    private void insert(String input){
        // display will be "0" when program is first opened or all output is cleared
        // clear default display prior to outputting new input
        if(!hasEntry) {
            System.out.println("removing text");
            outputLabel.setText("");
            hasEntry = true;    // entry is detected
        }

        // if output already has ".", do not append another to the inputString, leave method without further processing
        if(input.equals(".") && inputString.contains(".")){
            return;
        } else
            inputString += input; // append new input to end of inputString
        System.out.println("inputString: " + inputString);

        // no operator entry, format first number
        if(!hasOperator){
            // parse all input to double and store
            num1 = Double.parseDouble(inputString);
            System.out.println("Num1 = " + num1);

            // format number1 and store in outputLabel
            outputLabel.setText(decimalFormat.format(num1));

        } else {       // operator entry, format second number
            // when num2 is first being entered, clear the output label, clear the inputString except for most recent number entered
            if(num2 == 0){
                outputLabel.setText("");
                inputString = inputString.substring(inputString.length()-1);
            }
            System.out.println("InputString after operator input and number input: " + inputString);
            // parse all input to double and store
            num2 = Double.parseDouble(inputString);
            System.out.println("Num2 = " + num2);

            // format number2 and store in outputLabel
            outputLabel.setText(decimalFormat.format(num2));
        }

    } // end insert()

    // handle operator button pressed
    private void enterBasicOperator(String operator){
        // if a calculation has already been done and current operator has not been removed through CE,
        // take new operator, reset num1 to current result value, and output num1 with new operator
        if(priorCalc && hasOperator){
            primeForNextCalc();
            CalculatorController.operator = operator;
            outputLabel.setText(decimalFormat.format(num1) + " " + operator);
            System.out.println("Num1 = " + num1);
            System.out.println("inputString = " + inputString);
        }

        // if no prior operator has been pressed, output the current operator
        if(!hasOperator){
            hasOperator = true;
            CalculatorController.operator = operator;
            outputLabel.setText(outputLabel.getText() + " " + operator);
            System.out.println("Operator selected: " + operator);
        }


    }

    // square num1
    private void squareNumber(){
        // if prior calculation has been performed
        if(priorCalc){
            primeForNextCalc();
            hasOperator = false;
        }
        // if no prior operator has been pressed, output square symbol to screen
        if(!hasOperator){
            hasOperator = true;
            operator = "²";
            outputLabel.setText(outputLabel.getText() + operator);
            System.out.println("Squaring number");
        }
    }

    // take the square root of num1
    private void squareRootOfNumber(){
        // if prior calculation has been performed
        if(priorCalc && hasOperator){
            primeForNextCalc();
            hasOperator = false;
        }

        // if no prior operator has been pressed, output square root symbol to screen
        if(!hasOperator){
            hasOperator = true;
            operator = "√";
            outputLabel.setText(operator + outputLabel.getText());
            System.out.println("Taking square root of number");
        }
    }

    // remove operator
    private void removeOperator(){
        // if default output is not present, remove operator from outputLabel's text
        if(!outputLabel.getText().equals(DEFAULT_OUTPUT)) {
            if(operator.equals("²")){
                outputLabel.setText(outputLabel.getText().substring(0, outputLabel.getText().length()-1));
            } else if(operator.equals("√")){
                // if user tried to take the square root of a negative number and got "Invalid Input" as a result
                // change output back to previous and then remove the √
                if (outputLabel.getText().equals("Invalid Input")){
                    outputLabel.setText(operator + decimalFormat.format(num1));
                }
                outputLabel.setText(outputLabel.getText().replace("√", ""));
            } else {
                outputLabel.setText(outputLabel.getText().substring(0, outputLabel.getText().length()-2));
            }
        }

        hasOperator = false;
        operator = "";  // reset operator
        System.out.println("Removing operator");
    }

    // clear outputLabel text
    private void clearAll(){
        outputLabel.setText(DEFAULT_OUTPUT);    // output "0"
        inputString = ""; // reset all inputs
        num1 = 0.0;
        num2 = 0.0;
        result = 0.0;
        hasEntry = false; // prime for entry
        priorCalc = false;
        removeOperator();
        System.out.printf("Cleared%n%n%n");
    }

    // clear last entry
    private void clearLastEntry(){
        // if an operator has been entered and no secondary number input is detected,
        // remove operator and revert output and inputString to num1's value
        if(hasOperator && num2 == 0){
            removeOperator();
            // operator has been cleared, output num1 and revert currentString to num1 input
            if(num1 != 0 && inputString.equals("")) {
                inputString = Double.toString(num1);
                // if reverted inputString has a zero after the decimal, remove it
                // furthermore, if the input now has a decimal at the end, remove that as well
                if(inputString.endsWith("0")){
                    inputString = inputString.substring(0, inputString.length()-1);
                    if(inputString.endsWith(".")) {
                        inputString = inputString.substring(0, inputString.length()-1);
                    }
                }
                outputLabel.setText(decimalFormat.format(num1));
                System.out.println("InputString = " + inputString);
            }
            return;
        }

        // if inputString is not empty, remove last entry of inputString
        if(!inputString.equals("")) {
            System.out.println("Removing end of inputString");
            inputString = inputString.substring(0, inputString.length()-1);
        }

        System.out.println("inputString: " + inputString);

        // if no more input is found and no operator exists after clearing last entry, clear all
        // else if no input is found and an operator does exist, clear num2 and output
        if(inputString.equals("") && !hasOperator){
            clearAll();
        } else if(inputString.equals("")){  // display num1 and operator
            num2 = 0.0;
            outputLabel.setText(decimalFormat.format(num1) + " " + operator);
        }

        // if input still remains, update corresponding number
        if(!inputString.equals("")){
            System.out.println("Input remains");
            // if num2 is currently being edited
            if(hasOperator){
                // if inputString has only a negative sign left after removal
                // set num2 to zero and output num1 and operator entered
                if(inputString.equals("-")){
                    inputString = "";
                    num2 = 0;
                    outputLabel.setText(decimalFormat.format(num1) + " " + operator);
                } else {
                    num2 = Double.parseDouble(inputString);
                    outputLabel.setText(decimalFormat.format(num2));
                }
            }
            // if num1 is currently being edited
            if(num1 != 0 && !hasOperator){
                // remove negative sign and clear all, no input remains
                if(inputString.equals("-") || inputString.equals("√-")){
                    clearAll();
                } else {
                    num1 = Double.parseDouble(inputString);
                    outputLabel.setText(decimalFormat.format(num1));
                }
            }
        }

        System.out.println("Updated numbers:");
        System.out.println("Num1 = " + num1);
        System.out.println("Num2 = " + num2);
    } // end clearLastEntry()

    // change the current number to positive or negative depending on its current state
    private void changeSign(){
        System.out.println("Changing sign");
        // can't change the sign of 0
        if(num2 == 0 && num1 == 0){
            return;
        }

        // if prior calculation has been done,
        // update inputString to num1 value and format inputString
        if(priorCalc){
            primeForNextCalc();
            inputString = Double.toString(num1);
            // remove trailing 0 and decimal for proper formatting of inputString
            if(inputString.endsWith("0")){
                inputString = inputString.substring(0, inputString.length()-2);
                System.out.println("inputString = " + inputString);
            }
            // set to false so more calculations can be done after sign change
            removeOperator();
        }

        // if num1 is currently being modified
        // else if num2 is currently being modified
        if(!hasOperator){
            System.out.println("Changing num1 sign");
            num1 *= -1;
            // if num1 is negative, put "-" at beginning of inputString
            // else remove "-" from beginning of inputString
            if(num1 < 0){
                // if square root button has been pressed,
                // add a "-" after the √ then output and return
                if(operator.equals("√")){
                    System.out.println("Adding - to √");
                    inputString = inputString.replace("√", "");
                    inputString = operator + "-" + inputString;
                    outputLabel.setText(operator + decimalFormat.format(num1));
                    System.out.println("inputString = " + inputString);
                    return;
                } else {
                    inputString = "-" + inputString;
                }
            } else {
                inputString = inputString.replace("-", "");
                if(operator.equals("√")){
                    outputLabel.setText(operator + decimalFormat.format(num1));
                }
            }
            System.out.println("num1 = " + num1);
            System.out.println("inputString = " + inputString);
            outputLabel.setText(decimalFormat.format(num1)); // set outputLabel

        } else { //number2 is being modified currently
            num2 *= -1;

            // if num2 is negative, put "-" at beginning of inputString
            // else remove "-" from beginning of inputString
            if(num2 < 0) {
                inputString = "-" + inputString;
            } else inputString = inputString.replace("-", "");

            System.out.println("inputString = " + inputString);
            outputLabel.setText(decimalFormat.format(num2)); // set outputLabel
        }
    } // end changeSign()

    // perform calculation based on the operator specified
    private void calculate(){
        // validate operator exists
        if(operator.equals("")){
            return;
        }
        // calculate
        switch(operator){
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                // can't divide by zero
                if(num2 == 0){
                    result = 0;
                } else result = num1 / num2;
                break;
            case "²":
                if(num1 == 0){
                    result = 0;
                } else result = num1 * num1;
                break;
            case "√":
                System.out.println("num1 = " + num1);
                if(num1 < 0){
                    outputLabel.setText("Invalid Input");
                    return;
                } else result = Math.sqrt(num1);
                break;
        }

        System.out.println();
        System.out.println("Calculating");
        System.out.println("Num1 = " + num1);
        System.out.println("Operator = " + operator);
        System.out.println("Num2 = " + num2);
        System.out.println("Result = " + result);
        System.out.println("inputString = " + inputString);
        System.out.println("End calculation");

        // output result and note that a calculation has been done
        outputLabel.setText(decimalFormat.format(result));
        priorCalc = true;
    } // end calculate()

    // keep result and remove num2 from memory to ready program for next calculation
    private void primeForNextCalc(){
        num1 = result;
        num2 = 0;
    }

    // store the current value being worked on in memory
    private void memoryStore(){
        // if done a second time, it will overwrite the last number in memory
        if(priorCalc){
            System.out.println("Stored result = " + result);
            memory = result;
            return;
        }
        // if no operator (number1 is being edited), store num1 in memory
        // else (number2 is being edited), store num2 in memory
        if(!hasOperator){
            System.out.println("Stored number1 = " + num1);
            memory = num1;
        } else {
            System.out.println("Stored number2 = " + num2);
            memory = num2;
        }
    }

    // clear the current value stored in memory
    private void memoryClear(){
        System.out.println("Cleared memory");
        memory = 0;
    }

    // retrieve value stored in memory
    private void memoryRecall(){
        // if no operator (number1 is being edited), replace num1 with memory
        // else (number2 is being edited), replace num2 with memory
        if(!hasOperator){
            System.out.println("For num1, Recalled memory = " + memory);
            num1 = memory;
        } else {
            System.out.println("For num2, Recalled memory = " + memory);
            num2 = memory;
        }
        // change inputString to recalled memory, format, and output to screen
        inputString = Double.toString(memory);
        if(inputString.endsWith("0")){
            inputString = inputString.substring(0, inputString.length()-1);
            if(inputString.endsWith(".")){
                inputString = inputString.substring(0, inputString.length()-1);
            }
        }
        outputLabel.setText(decimalFormat.format(memory));
    }

    // Add current number to the number in memory and store it
    private void memoryAdd(){
        if(priorCalc){
            memory += result;
            return;
        }
        // if no operator (number1 is being edited), add num1 to memory variable
        // else (number2 is being edited), add num2 to memory variable
        if(!hasOperator){
            System.out.println("Adding num1 to memory = " + num1);
            memory += num1;
        } else {
            System.out.println("Adding num2 to memory = " + num2);
            memory += num2;
        }
    }

    // Subtract current number to the number in memory and store it
    private void memorySubtract(){
        if(priorCalc){
            memory -= result;
            return;
        }
        // if no operator (number1 is being edited), subtract num1 from memory
        // else (number2 is being edited), subtract num2 from memory
        if(!hasOperator) {
            System.out.println("Subtracting num1 from memory = " + num1);
            memory -= num1;
        } else {
            System.out.println("Subtracting num2 from memory = " + num2);
            memory -= num2;
        }
    }
} // end CalculatorController class
