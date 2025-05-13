package agentesgeneticos;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contenedor {
    
    public void iniciarContenedor() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl(null, 1099, null);
        AgentContainer mainContainer = runtime.createMainContainer(profile);
        agregarAgentes(mainContainer);
    }

    private void agregarAgentes(AgentContainer container) {
        try {
            container.createNewAgent("AgenteEvaluador", AgenteEvaluador.class.getName(), null).start();
            container.createNewAgent("AgenteEvolutivo", AgenteEvolutivo.class.getName(), null).start();
            container.createNewAgent("AgenteCoordinador", AgenteCoordinador.class.getName(), null).start();
        } catch (StaleProxyException ex) {
            Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
