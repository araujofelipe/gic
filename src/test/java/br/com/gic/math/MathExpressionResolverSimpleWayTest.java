package br.com.gic.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MathExpressionResolverSimpleWayTest {
	
	MathExpressionResolverSimpleWay underTest;
	
	@BeforeEach
	void setup() {
		underTest = new MathExpressionResolverSimpleWay();
	}
	
	
	@Test
	void itShouldEvaluateASimpleExpression() {
		//Given
		String expression = "3+4";
		//When
		double result = underTest.evaluate(expression);
		//That
		assertThat(result).isEqualTo(7);
	}
}
