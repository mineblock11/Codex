package mine.block.codex.math;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

@FunctionParameter(name = "stacks", nonNegative = true, nonZero = true)
public class FromStackFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        EvaluationValue value = parameterValues[0];
        return new EvaluationValue(value.getNumberValue().multiply(BigDecimal.valueOf(64)));
    }
}
