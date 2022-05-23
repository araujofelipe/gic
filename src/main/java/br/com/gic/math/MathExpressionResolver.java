package br.com.gic.math;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 
 * @author felipesilvaaraujo
 * Reference:
 * https://en.wikipedia.org/wiki/Shunting_yard_algorithm
 * https://artesoftware.com.br/2019/02/10/complexidade-cognitiva/
 * https://stackoverflow.com/questions/4589951/parsing-an-arithmetic-expression-and-building-a-tree-from-it-in-java/4590047#4590047
 */
public class MathExpressionResolver {
	
	private Map<Character, Integer> operators = Stream.of(new Object [][] {
		{'^', 4},
		{'*', 3},
		{'/', 3},
		{'+', 2},
		{'-', 2}
	}).collect(Collectors.toMap(data -> (Character)data[0], data -> (Integer) data[1]));
	
	private Map<Character, Character> operatorsMaps = Stream.of(new Object [][] {
		{'x',  '*'},
		{'÷', '/'},
	}).collect(Collectors.toMap(data -> (Character)data[0], data -> (Character) data[1]));
	
	private static final char LEFT_PARENTESIS = '(';
	private static final char RIGHT_PARENTESIS = ')';

	//Verify if the char is a letter or digit
	public boolean isLetterOrDigit(char ch) {
		return Character.isLetterOrDigit(ch);
	}
	
	//Returns precedence of operator
	public int getPrecedence(char operator) {
		 Integer precedence = this.operators.get(operator);
		 if(precedence == null) {
			 return -1;
		 }
		 return precedence;
	}
	
	
	//Execute shunting yard algorithm
	public String toPostfix(String expression) {
		Deque<Character> deque = new ArrayDeque<>();
		StringBuilder output = new StringBuilder();
		
		for(int i=0; i < expression.length(); i++) {
			char actualChar = replaceOperators(expression.charAt(i));
			
			if(isLetterOrDigit(actualChar)) {
				output.append(actualChar);
				continue;
			}
			
			//basically implements FIFO concepts to operators based on their precedence
			switch (actualChar) {
			case LEFT_PARENTESIS:
				deque.push(actualChar);
				break;
			case RIGHT_PARENTESIS:
				while (!deque.isEmpty() && deque.peek() != LEFT_PARENTESIS) {
					output.append(deque.pop());
				}
				deque.pop();
				break;
			default:
				while(!deque.isEmpty() && getPrecedence(actualChar) <= getPrecedence(deque.peek())) {
					output.append(deque.pop());
				}
				deque.push(actualChar);
				break;
			}
		}
		
		//append remaining operators 
		while(!deque.isEmpty()) {
			if(deque.peek() == LEFT_PARENTESIS) {
				return "Invalid";
			}
			output.append(deque.pop());
		}
		
		return output.toString();
		
	}
	
	//resolve the postfix expression.
	public int parsePostfix(String postfix) {
		Deque<Double> deque = new ArrayDeque<>();
		for(int i = 0; i < postfix.length(); i++) {
			char actualChar = postfix.charAt(i);
			if(operators.containsKey(actualChar)) { //verify that the char is a know operator
				double operandA = deque.pop();
				double operandB = deque.pop();
				
				switch (actualChar) {
				case '+' : deque.push(operandB + operandA);
					break;
				case '-' :  deque.push(operandB - operandA);
					break;
				case '*' :  deque.push(operandB * operandA);
				break;
				case '/' :  deque.push(operandB / operandA);
				break;
				case '^' :  deque.push(Math.pow(operandB, operandA));
				break;
				default:
					break;
				}
			}
			else {
				double operand = (actualChar - '0'); //converts digit to a double value.
				deque.push(operand);
			}
		}
		return deque.pop().intValue();
	}
	
	
	public char replaceOperators(char input) {
		 Character character = operatorsMaps.get(input);
		 if(character != null) {
			 return character;
		 }
		 return input;
	}
	
	public static void main(String[] args) {
		String expression = "10/2";
		MathExpressionResolver resolver = new MathExpressionResolver();
		System.out.println(resolver.parsePostfix(resolver.toPostfix(expression)));
	}

}
