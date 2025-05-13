package agentesgeneticos;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class AgenteEvolutivo extends Agent {

    private AgenteEvaluador evaluador;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " iniciado.");

        evaluador = new AgenteEvaluador();

        // Espera de mensaje con parámetros de ejecución
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = blockingReceive(mt);
                if (msg != null) {
                    System.out.println(getLocalName() + " recibió parámetros para ejecutar algoritmo genético.");

                    // Por simplicidad, asumimos que el contenido del mensaje son los parámetros separados por comas:
                    // genes,poblacionInicial,maxGeneraciones,evolucion
                    String contenido = msg.getContent();
                    String[] partes = contenido.split(",");
                    int genes = Integer.parseInt(partes[0]);
                    int poblacion = Integer.parseInt(partes[1]);
                    int generaciones = Integer.parseInt(partes[2]);
                    int evolucion = Integer.parseInt(partes[3]);

                    ejecutarAlgoritmo(genes, poblacion, generaciones, evolucion);
                }
            }
        });
    }

    public void ejecutarAlgoritmo(int genes, int poblacionInicial, int maxGeneraciones, int evolucion) {
        try {
            Configuration config = new DefaultConfiguration();
            config.setPreservFittestIndividual(true);
            config.setFitnessFunction(new FuncionAptitudAgente(evaluador));

            Gene[] geneModel = new Gene[genes];
            for (int i = 0; i < genes; i++) {
                geneModel[i] = new IntegerGene(config, 0, 1);
            }

            IChromosome sampleChromosome = new Chromosome(config, geneModel);
            config.setSampleChromosome(sampleChromosome);
            config.setPopulationSize(poblacionInicial);

            Genotype population = Genotype.randomInitialGenotype(config);

            double mejorAnterior = Double.NEGATIVE_INFINITY;
            int generacionesSinMejora = 0;
            int limiteEstancamiento = 10;

            for (int i = 0; i < maxGeneraciones; i++) {
                System.out.println("Generación " + (i + 1));
                population.evolve(evolucion);

                IChromosome mejor = population.getFittestChromosome();
                double valorActual = mejor.getFitnessValue();
                mostrarIndividuo(mejor);

                if (Math.abs(valorActual - mejorAnterior) < 1e-6) {
                    generacionesSinMejora++;
                } else {
                    generacionesSinMejora = 0;
                    mejorAnterior = valorActual;
                }

                if (generacionesSinMejora >= limiteEstancamiento) {
                    System.out.println("Estancamiento detectado. Terminando evolución.");
                    break;
                }
            }

            System.out.println("Mejor individuo encontrado:");
            mostrarIndividuo(population.getFittestChromosome());

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void mostrarIndividuo(IChromosome ic) {
        IndividuoUtils.mostrarIndividuo(ic);
    }
}
