package com.dakota.calculator;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CalculatorController {

    // Set up FX GUI elements
    @FXML
    private Label outputLabel;

    // Initialize global variables
    private final String DEFAULT_OUTPUT = "0";
    // false if no entry is detected, true if so
    private boolean hasEntry = false;
    // false if operator has not been entered, true if so
    private boolean hasOperator = false;
    // false if current number is positive, true if current number is negative
    private boolean isNegative = false;
    // current string of text being formed prior to operator selection or calculation (=)
    private String currentString = "";

    // formatting for proper comma output
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.0######");

    private double number1 = 0.0;
    private double number2 = 0.0;
    private String operator = "";

    // Determine what happens when any button is clicked
    public void onMouseClick(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource(); // cast object returned from getSource() to a Button
        String input = button.getText();       // get/store text from button pressed into String for processing

        switch(input){
            case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "0": case ".":
                insertCharacter(input);
                break;
            case "+": case "-": case "*": case "/":
                handleOperator(input);
                break;
            case "=":
                calculate(number1, number2, operator);
                break;
            case "C":
                clearOutput();
                break;
            case "CE":
                clearLastEntry();
                break;
            case "+-":
                // convert from negative to positive and vice versa
                changeSign();
                break;
            case "MC":
                break;
            case "MR":
                break;
            case "M+":
                break;
        }
    }

    // Insert the text of any button pressed into the outputLabel
    private void insertCharacter(String input){

        // display will be "0" when program is first opened or all output is cleared
        if(!hasEntry) {
            outputLabel.setText("");   // removes the "0" on first entry prior to number output
            hasEntry = true;    // entry is detected
        }

        // check if input is a decimal point (and if the string being output doesn't have one already)
        // if the output already has one, do not append another to the current string, leave method without further processing
        if(input.contains(".") && currentString.contains(".")){
            return;
        } else if(input.contains(".")) {
            currentString += input;
        } else
            currentString += input; // append new input to end of current string
        System.out.println("CurrentString: " + currentString);

        // no operator entry, format first number
        if(!hasOperator){
            // parse to double and store
            number1 = Double.parseDouble(currentString);
            System.out.println("Number1 = " + number1);

            // format number1 and store in outputLabel
            outputLabel.setText(decimalFormat.format(number1));

        } else {       // operator entry, format second number
            // clear screen when new number is pressed after operator is entered
            outputLabel.setText("");

            // if number2 has not been created yet, remove all but the last digit of current string, which is the most recent input
            if(number2 == 0){
                currentString = currentString.substring(currentString.length()-1);
            }
            System.out.println("Input log after operator input and number input: " + currentString);

            // parse to double and store
            number2 = Double.parseDouble(currentString);
            System.out.println("Number2 = " + number2);

            // format number2 and store in outputLabel
            outputLabel.setText(decimalFormat.format(number2));
        }

    }

    // handle operator button pressed
    private void handleOperator(String operator){
        hasOperator = true;
        this.operator = operator;
        System.out.println("Operator selected: " + operator);
    }


    // clear outputLabel text
    private void clearOutput(){
        outputLabel.setText(DEFAULT_OUTPUT);    // output "0"
        currentString = ""; // reset all inputs
        number1 = 0.0;
        number2 = 0.0;
        isNegative = false; // current number is no longer negative
        hasEntry = false; // prime for entry
        hasOperator = false; // indicate no operator has been entered
        operator = "";  // reset operator
        System.out.printf("Cleared%n%n%n");
    }

    // clear last entry
    private void clearLastEntry(){

        // if operator has been pressed and the second entry has not started, only remove current operator
        if(hasOperator && number2 == 0){
            hasOperator = false;
            operator = "";
        } else {
            // remove the end of the current string
            System.out.println("Before removal CurrentString: " + currentString);
            if(!currentString.equals("")){
                if(currentString.endsWith(".")){
                    System.out.println("has . at end");
                    currentString = currentString.substring(0, currentString.length()-2);   // strip last two characters of string
                } else currentString = currentString.substring(0, currentString.length()-1);   // strip last character of string
            }
            System.out.println("CurrentString: " + currentString);

            // update current number
            if(currentString.equals("")){
                number1 = 0.0;
                number2 = 0.0;
                outputLabel.setText(DEFAULT_OUTPUT);
                hasEntry = false;   // prime for entry
            } else {
                if(number2 == 0){
                    number1 = Double.parseDouble(currentString);
                    outputLabel.setText(decimalFormat.format(number1));
                } else if (number2 != 0){
                    number2 = Double.parseDouble(currentString);
                    outputLabel.setText(decimalFormat.format(number2));
                }
            }
        }
    } // end clearLastEntry()

    // change the current number to positive or negative depending on its current state
    private void changeSign(){
        if(number2 == 0 && number1 == 0){
            return;
        }
        if(number2 == 0){   // number1 being modified currently
            number1 *= -1;

            currentString = Double.toString(number1); // set currentString being worked on
            outputLabel.setText(decimalFormat.format(number1)); // set outputLabel

        } else if(number2 != 0){ //number2 is being modified currently
            number2 *= -1;

            currentString = Double.toString(number2); // set currentString being worked on
            outputLabel.setText(decimalFormat.format(number2)); // set outputLabel
        }

//        // display negative sign on screen if current number is a negative value
//        if(isNegative){
//            outputLabel.setText("-" + outputLabel.getText());
//        } else if (!isNegative){
//            System.out.println("Removing negative sign");
//            outputLabel.getText().replaceFirst("-", "");    // remove "-"
//        }
    }

    // perform calculation based on the operator specified
    private void calculate(double number1, double number2, String operator){
        double result = 0.0;
        // calculate
        switch(operator){
            case "+":
                result = number1 + number2;
                break;
            case "-":
                result = number1 - number2;
                break;
            case "*":
                result = number1 * number2;
                break;
            case "/":
                // can't divide by zero
                if(number2 == 0){
                    result = 0;
                } else result = number1 / number2;
                break;
        }

        outputLabel.setText(decimalFormat.format(result));
    } // end calculate()
} // end CalculatorController class
