package agentesgeneticos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.jgap.IChromosome;


public class AgenteEvaluador extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " iniciado.");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mensaje = receive();
                if (mensaje != null) {
                    if (mensaje.getPerformative() == ACLMessage.REQUEST) {
                        try {
                            // Aquí recibimos el cromosoma como objeto serializado
                            IChromosome cromosoma = (IChromosome) mensaje.getContentObject();

                            // Evaluamos
                            double resultado = evaluar(cromosoma);

                            // Enviamos la respuesta
                            ACLMessage respuesta = mensaje.createReply();
                            respuesta.setPerformative(ACLMessage.INFORM);
                            respuesta.setContent(String.valueOf(resultado));
                            send(respuesta);

                            System.out.println(getLocalName() + " evaluó un cromosoma con resultado: " + resultado);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }

    public double evaluar(IChromosome cromosoma) {
        int[] valores = IndividuoUtils.obtenerXY(cromosoma);
        int x = valores[0];

        try {
            if ((7 * x + 1) <= 0 || (x + 3) <= 0) {
                return 1e-6;
            }

            double resultado = Math.log(7 * x + 1) - 2 * Math.log(x + 3) + Math.log(2);
            return Math.max(1e-6, resultado + 10);
        } catch (Exception e) {
            return 1e-6;
        }
    }
}
