package apps;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

    /**
     * Expression to be evaluated
     */
    String expr;                
    
    /**
     * Scalar symbols in the expression 
     */
    ArrayList<ScalarSymbol> scalars;   
    
    /**
     * Array symbols in the expression
     */
    ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     */
    public void buildSymbols(){
            /** COMPLETE THIS METHOD **/
        //declare the variables in the expression (Scalars and arrays)
        expr = expr.replaceAll("\\s+","");
        String strV = "";
        //create ArrayList for both arrays and scalars
        arrays = new ArrayList<ArraySymbol>();
        scalars = new ArrayList<ScalarSymbol>();
        //case for when its a variable (we don't know if its an array yet)
        //should index through, if letter, then letter is stores into string
        //keeps doing this until the next one is not a letter
        //if a bracket then array
        //not a bracket then scalar
        System.out.println(expr.length());
        for(int i = 0; i < expr.length(); i++){
            //Variable case check
            if(Character.isLetter(expr.charAt(i)) == true ){
                //System.out.println("Variable string is: " + strV + "index is at: " + i);
                while(i < expr.length() && (Character.isLetter(expr.charAt(i)) == true )) {
                    strV = strV + expr.charAt(i);
                    if(i == expr.length()-1){
                        break;
                    }else{
                    i++;
                    }
                }
                //System.out.println("index is at: " + i);
            }
            //variable array case
            
            if(strV != ""){
                if(expr.charAt(i) == '['){
                    ArraySymbol currentArray = new ArraySymbol(strV);
                    //System.out.println(currentArray);
                    strV = "";
                    arrays.add(currentArray);
                    //System.out.println(arrays);
                }else{
                    //scalar variable case
                    ScalarSymbol currentScalar = new ScalarSymbol(strV);
                    //System.out.println(currentScalar);
                        strV = "";
                        scalars.add(currentScalar);
                        //System.out.println(scalars);
                    }
            }
            printScalars();
            printArrays();
        }
            
    }

    
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
                continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
                asymbol = arrays.get(asi);
                asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }
    //gets Scalar Value
    private int getScalarValue(String name)
    {
        for (int i = 0; i < scalars.size(); i++)
        {
            if (scalars.get(i).name.equals(name))
            {
                return scalars.get(i).value;
            }
        }
        
        return 0;
    }
   
    //true or false if its is multiplication character
    private boolean isMultiplicationChar(char c) {
        return c == '*';
    }

    //true or false if its is division character
    private boolean isDivisionCharacter(char c) {
        return c == '/';
    }
    
     //true or false if its is Addition character
    private boolean isAdditionCharacter(char c) {
        return c == '+';
    }
    
     //true or false if its is Subtraction character
    private boolean isSubtractionCharacter(char c) {
        return c == '-';
    }
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
        /** COMPLETE THIS METHOD **/
        // following line just a placeholder for compilation
        System.out.println(scalars + "    " + arrays);
        float ansF = 0;
        String expression = expr;

        if(expr.length() == 1){
            //check if its a number, if so return number WORKS
            if(Character.isDigit(expr.charAt(0)) == true){
                ansF = Float.parseFloat("" + expr.charAt(0));;
                return ansF;
            }
        //check if its a single variable, if so return the variables value
            if(Character.isLetter(expr.charAt(0)) == true){
                ansF = getScalarValue(expr);
                System.out.println(ansF);
                return ansF;
                
            }
        }
        //case with multiple variables
        
        //replaces all scalar variables with their respective scalar value
        for (int i = 0; i < this.scalars.size(); i++) {
            expression = expression.replace(this.scalars.get(i).name, "" + this.scalars.get(i).value);
        }
        System.out.println(expression);
        //recursive method call
        expression = this.evaluate(expression);

        System.out.println(expression);

        //try catch
        try {
            return Float.parseFloat(expression);
        } catch (Exception e) {
            return 0;
        }
    }
    //recursive method
    private String evaluate (String expression){
        
        //Number holders
        float firstN;
        float secondN;

        //Number holders for indexes
        int firstNumdex;
        int secondNumdex;
        
        //t/f for if there is a negative value
        boolean isNegative;
        
        
        if (expression == null || expression.length() == 0) {
            System.out.println("I returned zero. I'm sorry. I know you hate that.");
            return "0";
        }
        //No arrays
        if (expression.indexOf('[') == -1) {
            //No sub expression
            if (expression.indexOf('(') == -1) {
                expression = expression.replace(" ", "");
                expression = " " + expression;

            
                //false for default
                isNegative = false;

                int numberOfOperators = 0;

                //Loop to find #number of operators in a given expression
                //Counts number of operators
                for (int i = 2; i < expression.length(); i++) {
                    if (this.isAdditionCharacter(expression.charAt(i)))
                        numberOfOperators++;
                    if (this.isSubtractionCharacter(expression.charAt(i)))
                        numberOfOperators++;
                    if (this.isMultiplicationChar(expression.charAt(i)))
                        numberOfOperators++;
                    if (this.isDivisionCharacter(expression.charAt(i))) {
                        numberOfOperators++;
                    }
                }
                System.out.println("Number of Operators is: " + numberOfOperators);

                //Case where no operators
                if (numberOfOperators == 0)
                    return expression;

                System.out.println(expression);
                for (int i = 0; i < expression.length(); i++) {
                    //Case where first number is  a negative number
                    if (i == 1 && this.isSubtractionCharacter(expression.charAt(i))) {
                        isNegative = true;
                    } else if (this.isMultiplicationChar(expression.charAt(i)) || this.isDivisionCharacter(expression.charAt(i))) {
                        firstNumdex = secondNumdex = i;

                        //Number before operator becomes first number 
                        //starts at operator, and checks at previous indexes if its still a character
                        //if so keeps going until it isn't so which would be the first number is a sequence of numbers for example 3 in 345
                        while (firstNumdex > 0 && (Character.isDigit(expression.charAt(firstNumdex - 1)) || expression.charAt(firstNumdex - 1) == '.')) {
                            firstNumdex--;
                        }

                        //Number after operator becomes second number
                        //Similar to above except it goes on until the end of a sequence of numbers
                        while (secondNumdex < expression.length() - 1 && (Character.isDigit(expression.charAt(secondNumdex + 1)) || expression.charAt(secondNumdex + 1) == '.')) {
                            secondNumdex++;
                        }

                        //Try to convert firstNumber into a float substring 
                        //if not possible just set to 0
                        try {
                            firstN = Float.parseFloat(expression.substring(firstNumdex, i));
                        } catch (Exception e){
                            firstN = 0;
                        }

                        
                        //cast the float type onto the numbers
                        try {
                            secondN = Float.parseFloat(expression.substring(i + 1, secondNumdex + 1));
                        } catch (Exception e) {
                            secondN = 0;
                        }

                        String result = "";

                        if (isNegative)
                            firstN = -1 * firstN;

                        //fi multiplication character then multiply
                        if (this.isMultiplicationChar(expression.charAt(i))) {
                            result = "" + firstN * secondN;

                        }else{
                            //not multiplication character so must be diviison, divide
                            result = "" + firstN / secondN;
                        }

                        int stadex = 0;
                        if (isNegative)
                            stadex = 2;
                        

                        //Relabeling expression after simplifying
                        expression = expression.substring(stadex, firstNumdex) + result + expression.substring(secondNumdex+ 1);
                        //return index to zero
                        i = 0;
                        //return local variable to default
                        isNegative = false;
                    }
                }
                isNegative = false;

                for (int i = 0; i < expression.length(); i++) {
                    if (i == 1 && this.isSubtractionCharacter(expression.charAt(i))) {
                        isNegative = true;
                    } else if (this.isAdditionCharacter(expression.charAt(i)) || this.isSubtractionCharacter(expression.charAt(i))) {
                        firstNumdex = secondNumdex = i;

                        while (firstNumdex > 0 && (Character.isDigit(expression.charAt(firstNumdex - 1)) || expression.charAt(firstNumdex - 1) == '.')) {
                            firstNumdex--;
                        }

                        while (secondNumdex < expression.length() - 1 && (Character.isDigit(expression.charAt(secondNumdex + 1)) || expression.charAt(secondNumdex + 1) == '.')) {
                            secondNumdex++;
                        }

                        try {
                            firstN = Float.parseFloat(expression.substring(firstNumdex, i));
                        } catch (Exception e) {
                            firstN = 0;
                        }
                        
                        try {
                            secondN = Float.parseFloat(expression.substring(i + 1, secondNumdex + 1));
                        } catch (Exception e) {
                            secondN = 0;
                        }

                        String result = "";

                        if (isNegative)
                            firstN = -1 * firstN;

                        if (this.isAdditionCharacter(expression.charAt(i))) {
                            result = "" + (firstN + secondN);

                        } else {
                            result = "" + (firstN - secondN);
                        }

                        int stadex = 0;
                        if (isNegative)
                            stadex = 2;

                        expression = expression.substring(stadex, firstNumdex) + result + expression.substring(secondNumdex+1);

                        i = 0;
                        isNegative = false;
                    }
                }

                expression = expression.replace(" ", "");

                return expression;
            } else {
                for (int index = 0; index < expression.length(); index++){
                    if (this.isOpeningBracket(expression.charAt(index))) {
                        int begex = index;
                        int skips = 1;
                        index++;
                        int endex = index;
                        for (; endex < expression.length(); endex++) {
                            //every time you reach a opening parenthesis you count up
                            if (expression.charAt(endex) == '(')
                                skips++;
                            //every time you find a matching closing parenthesis you count down
                            if (expression.charAt(endex) == ')')
                                skips--;
                            //essentially you should of reacher back to zero signaling you reached the end
                            if (skips == 0 && expression.charAt(endex) == ')')
                                break;
                        }

                        //first part from the first parenthesis to the next one
                        String beginning = expression.substring(0, begex);
                        //from the middle to right before parenthesis
                        String middle = this.evaluate(expression.substring(index, endex));
                        //includes parenthesis
                        String end = expression.substring(endex + 1);

                        return this.evaluate(beginning + middle + end);
                    }
                }
            }
        } else {
            for (int index = 0; index < expression.length(); index++){
                if (Character.isLetter(expression.charAt(index))) {
                    String currentSymbol = "";
                    boolean isArray = false;

                    int begex = index;

                    while (index < expression.length() && Character.isLetter(expression.charAt(index))){
                        currentSymbol += expression.charAt(index);
                        index++;
                    }
                    if (index < expression.length() && expression.charAt(index) == '[')
                        isArray = true;

                    if (isArray) {
                        int skips = 1;
                        index++;
                        int endex = index;

                        for (; endex < expression.length(); endex++) {
                            if (expression.charAt(endex) == '[')
                                skips++;
                            if (expression.charAt(endex) == ']')
                                skips--;
                            if (skips == 0 && expression.charAt(endex) == ']')
                                break;
                        }

                        String beforeArray = expression.substring(0, begex);
                        String afterArray = expression.substring(endex+1);

                        String evaluatedAddress = this.evaluate(expression.substring(index, endex));

                        String evaluatedArray = "" + this.arrayWithName(currentSymbol).values[(int)Float.parseFloat(evaluatedAddress)];

                        return this.evaluate(beforeArray + evaluatedArray + afterArray); //Recursion
                    }
                }
            }
        }
        return "";
    }
    private boolean isOpeningBracket(char potentialOpeningBracket) {
        if (potentialOpeningBracket == '(') {
            return true;
        } else if (potentialOpeningBracket == '[') {
            return true;
        } else return false;
    }

    @SuppressWarnings("unused")
    private boolean isClosingBracket(char potentialOpeningBracket) {
        if (potentialOpeningBracket == ']') {
            return true;
        } else if (potentialOpeningBracket == ')') {
            return true;
        } else return false;
    }
    private ArraySymbol arrayWithName(String name) {
        for (int i = 0; i < arrays.size(); i++) {
            if (arrays.get(i).name.equals(name)){
                return arrays.get(i);
            }
        }

        return null;
    }
    
    
    
    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
            for (ArraySymbol as: arrays) {
                System.out.println(as);
            }
    }

}
