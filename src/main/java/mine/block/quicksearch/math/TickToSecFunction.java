package mine.block.quicksearch.math;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.math.RoundingMode;

@FunctionParameter(name = "ticks", nonNegative = true, nonZero = true)
public class TickToSecFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        return new EvaluationValue(parameterValues[0].getNumberValue().divide(BigDecimal.valueOf(20)));
    }
}
