package com.tw.service;

import com.tw.util.InputType;
import com.tw.model.GalacticNumerals;
import com.tw.model.Metals;
import java.util.regex.Matcher;

/**
 *
 * @author Toni
 */
public class QuestionHandler {
    
    private ConversorService conversorService = ConversorService.getInstance();
    private GalacticNumerals galacticNumeral;
    private Metals metals;
    private String SPACE = "\\s";
    private String IS = " is ";

    public QuestionHandler(GalacticNumerals galacticNumeral, Metals metals) {
        this.galacticNumeral = galacticNumeral;
        this.metals = metals;
    }

    public String getAnswer(String userInput, InputType inputType) {
        
        String answer = "";
        
        if (inputType == InputType.QUESTION_HOW_MANY) {
            answer = howManyQuestionHandle(userInput);
        } else if (inputType == InputType.QUESTION_HOW_MUCH) {
            answer = howMuchQuestionHandle(userInput);
        }
        return answer;
    }

    private String howMuchQuestionHandle(String userInput) {
       
        String answer = null;
        Matcher matcher = conversorService.getHowMuchMatcher(userInput);

        if (matcher.matches()) {
            String inputNumerals = matcher.group(1);
            String[] galacticNumerals = matcher.group(1).split(" ");
            if (galacticNumeral.areGalacticalNumeralsPresent(galacticNumerals)) {
                int number = galacticNumeral.getArabicNumeral(galacticNumeral.createRomanNumeral(galacticNumerals));
                if (number != -1) {
                    answer = inputNumerals +IS+ number + ".";
                }
            }
        }
        return answer;
    }

    private String howManyQuestionHandle(String userInput) {
        
        String answer = null;
        Matcher matcher = conversorService.getHowManyMatcher(userInput);

        if (matcher.matches()) {
            String creditName = matcher.group(1);
            String[] alienNumerals = matcher.group(2).split(SPACE);
            String metalName = matcher.group(3);

            if (galacticNumeral.areGalacticalNumeralsPresent(alienNumerals) && metals.isPresent(metalName)) {
                int metalQuantity = galacticNumeral.getArabicNumeral(galacticNumeral.createRomanNumeral(alienNumerals));

                if (metalQuantity != -1) {
                    double totalCredits = metalQuantity * metals.getMetalsUnitValue(metalName);
                    StringBuilder result = new StringBuilder();
                    result.append(matcher.group(2)).append(metalName).append(IS).append(totalCredits).append(" ").append(creditName);
                    answer = result.toString();
                }
            }
        }

        return answer;
    }
}
