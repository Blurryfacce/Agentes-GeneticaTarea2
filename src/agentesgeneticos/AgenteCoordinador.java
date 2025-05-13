package agentesgeneticos;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteCoordinador extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " iniciado.");

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Parámetros: genes, poblaciónInicial, generaciones, evolución
                String parametros = "10,10,100,1";

                ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
                mensaje.addReceiver(new AID("AgenteEvolutivo", AID.ISLOCALNAME));
                mensaje.setLanguage("es");
                mensaje.setOntology("algoritmo-genetico");
                mensaje.setContent(parametros);

                send(mensaje);
                System.out.println(getLocalName() + " envió parámetros al AgenteEvolutivo.");
            }
        });
    }
}
