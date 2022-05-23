package br.com.gic.math;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 
 * @author felipesilvaaraujo
 * https://en.wikipedia.org/wiki/Shunting_yard_algorithm
 */
public class MathExpressionResolver {
	
	private Map<Character, Integer> operators = Stream.of(new Object [][] {
		{'^', 4},
		{'*', 3},
		{'/', 3},
		{'+', 2},
		{'-', 2}
	}).collect(Collectors.toMap(data -> (Character)data[0], data -> (Integer) data[1]));
	
	private static final char LEFT_PARENTESIS = '(';
	private static final char RIGHT_PARENTESIS = ')';

	
	
	public MathExpressionResolver() {
	}

	public boolean isLetterOrDigit(char ch) {
		return Character.isLetterOrDigit(ch);
	}
	
	public int getPrecedence(char operator) {
		 Integer precedence = this.operators.get(operator);
		 if(precedence == null) {
			 return -1;
		 }
		 return precedence;
	}

	public String toPostfix(String expression) {
		Deque<Character> deque = new ArrayDeque<>();
		StringBuilder output = new StringBuilder();
		
		for(int i=0; i < expression.length(); i++) {
			char actualChar = expression.charAt(i);
			
			if(isLetterOrDigit(actualChar)) {
				output.append(actualChar);
			}
			else if(actualChar == LEFT_PARENTESIS) {
				deque.push(actualChar);
			}
			else if(actualChar == RIGHT_PARENTESIS) {
				while (!deque.isEmpty() && deque.peek() != LEFT_PARENTESIS) {
					output.append(deque.pop());
				}
				deque.pop();
			}
			else {
				while(!deque.isEmpty() && getPrecedence(actualChar) <= getPrecedence(deque.peek())) {
					output.append(deque.pop());
				}
				deque.push(actualChar);
			}
			
		}

		while(!deque.isEmpty()) {
			if(deque.peek() == LEFT_PARENTESIS) {
				return "Invalid";
			}
			output.append(deque.pop());
		}
		
		return output.toString();
		
	}
	
	public int parsePostfix(String postfix) {
		Deque<Double> deque = new ArrayDeque<>();
		for(int i = 0; i < postfix.length(); i++) {
			char actualChar = postfix.charAt(i);
			if(operators.containsKey(actualChar)) {
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
				double operand = (actualChar - '0');
				deque.push(operand);
			}
		}
		return Double.valueOf(deque.pop()).intValue();
	}
	
	
	public static void main(String[] args) {
		String expression = "(2+3)*5";
		MathExpressionResolver resolver = new MathExpressionResolver();
		System.out.println(resolver.parsePostfix(resolver.toPostfix(expression)));
	}
}
