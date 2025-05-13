package agentesgeneticos;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class FuncionAptitudAgente extends FitnessFunction {
    private final AgenteEvaluador evaluador;

    public FuncionAptitudAgente(AgenteEvaluador evaluador) {
        this.evaluador = evaluador;
    }

    @Override
    protected double evaluate(IChromosome chromosome) {
        return evaluador.evaluar(chromosome);
    }
}
