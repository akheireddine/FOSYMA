package mas.behaviours.explorer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import mas.agents.AK_Agent;
import tools.DFDServices;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

public class ESendInformationAfterCollisionBehaviour extends OneShotBehaviour {

		
	private static final long serialVersionUID = -5537592747419206404L;

	public void action() {
		
			final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(this.myAgent.getAID());
			
			//chercher les AID des agents que je j'envoie le message d'information
			AID[] sellerAgents = DFDServices.getAgentsByService("explorer",myAgent);
			//m'enlever de la liste des receivers
			if(sellerAgents != null){
				for(AID agt : sellerAgents)
					if (!agt.getLocalName().equals(myAgent.getLocalName()))
						msg.addReceiver(agt);
			}
			
			sellerAgents = DFDServices.getAgentsByService("collector",myAgent);
			//m'enlever de la liste des receivers
			if(sellerAgents != null){
				for(AID agt : sellerAgents)
					if (!agt.getLocalName().equals(myAgent.getLocalName()))
						msg.addReceiver(agt);
			}
			
			sellerAgents = DFDServices.getAgentsByService("silo",myAgent);
			if(sellerAgents != null){
				for(AID agt : sellerAgents)
					if (!agt.getLocalName().equals(myAgent.getLocalName()))
						msg.addReceiver(agt);
			}
			
			try {
				//Envoi un tuple contenant (informations sur les noeuds, dictionnaire d'adjacence, sommets ouverts, sommets fermes )
				msg.setContentObject((Serializable) ((AK_Agent)myAgent).getObjectToSend());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			((mas.abstractAgent)this.myAgent).sendMessage(msg);
			Set<String> receivers = new HashSet<String>();
			Iterator i = msg.getAllIntendedReceiver();
			while(i.hasNext())
				receivers.add((String)i.next());
			System.out.println(myAgent.getLocalName()+" : INFORM AGENT "+receivers+"\n\tFermes : "+((AK_Agent)myAgent).getGraph().getFermes()+"\n"
					+ "\tOuverts : "+((AK_Agent)myAgent).getGraph().getOuverts());
	}

}
