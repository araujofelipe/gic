package br.com.gic.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MathExpressionResolverTest {

	private MathExpressionResolver underTest;
	
	
	@BeforeEach
	void setup() {
		underTest = new MathExpressionResolver();
	}
	
	@ParameterizedTest
	@CsvSource({"1,true", "2, true", "+, false"} )
	void itShouldCheckIfIsALetterOrDigit(char ch, boolean expected) {
		//When
		boolean isLetterOrDigit = underTest.isLetterOrDigit(ch);
		//Then
		assertThat(isLetterOrDigit).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({"^,4", "*,3", "/,3", "+,2", "-,2", "%, -1"})
	void itShouldCheckPrecedenceOfOperator(char operator, int expected) {
		//When
		int precedence = underTest.getPrecedence(operator);
		//Then
		assertThat(precedence).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({"3+4, 34+", "9+2/(3-4)^5^2, 9234-5^2^/+"})
	void itShouldCheckInfixToPostfixExpression(String expression, String expected) {
		//When
		String postfix = underTest.toPostfix(expression);
		//Then
		assertThat(postfix).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({"3+4, 7", "((2+3)*5)^2, 625", "(2+3)*5, 25",  "2+3*5, 17", "(2+3)*5^2, 125", "(2+3)x5^2, 125"})
	void itShouldCheckPostfixParser(String infix, int expected) {
		//When
		String postfix = underTest.toPostfix(infix);
		int result = underTest.parsePostfix(postfix);
		//Then
		assertThat(result).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({"÷, /", "x, *", "+,+","-,-" })
	void itShouldVerifyIfOperatorsHasBeReplaced(char operator, char expected) {
		//Given
		//When
		char result = underTest.replaceOperators(operator);
		//Then
		assertThat(result).isEqualTo(expected);
	}
	
	
	@CsvSource({"10, 10 ", "150, 150", "2550, 2500"})
	void itShouldVerifyThatPassedNumberIsCorrectlyReturned(String number, String expected) {
		//TODO: refactory to accept two or more digits operands.
		//Without this feature, tests cases like "10/2" will broke. :(
	}
}
