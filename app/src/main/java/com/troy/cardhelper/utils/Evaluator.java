package com.troy.cardhelper.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenlongfei on 16/1/28.
 */
public class Evaluator {
    public static String eval(String infixExpression) {
        Stack<String> stack = new Stack<String>();
        String trimmedExp = infixExpression.replaceAll(" ", "");
        for (String c : split(trimmedExp)) {
            Log.i("Troy", "C:" + c);
            stack.push(c);
        }
        String result = performOperation(stack.pop(), stack.pop(),
                stack.pop()) + "";
        return result;
    }

    private static int performOperation(String operand2, String operator, String operand1) {
        int op1 = Integer.parseInt(operand1);
        int op2 = Integer.parseInt(operand2);
        switch (operator.toCharArray()[0]) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            default:
                return 0;
        }
    }

    static String[] split(String exp) {
        ArrayList<String> parts = new ArrayList<String>();
        Pattern pat = Pattern.compile("\\d++|\\+|\\-|\\*|/|\\(|\\)");
        Matcher matcher = pat.matcher(exp);
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        return parts.toArray(new String[0]);
    }
}
