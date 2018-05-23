package mas.behaviours.collector;

import Tools.DFDServices;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

public class ThrowBackPack extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3350035835226586761L;

	@Override
	public void action() {
		
		AID[] sellerAgents = DFDServices.getAgentsByService("silo",myAgent);
		for(AID agt : sellerAgents) {
			if(((mas.abstractAgent)myAgent).emptyMyBackPack(agt.getLocalName())) {
				
			}
		}
	}

}
