package mas.behaviours;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import env.Attribute;
import env.Couple;
import Tools.GraphAK;
import mas.agents.AK_Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
/***
 * 
 * Renvoyer au destinataire ma connaissance de 
 * l'environement (graphe serialize)
 *
 */
public class OldInformAgentBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 2256094839475737699L;

	
	
	public void action() {
		
		final ACLMessage received_msg = ((AK_Agent)myAgent).getMessage();
		
		if(received_msg != null){
		
			final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(this.myAgent.getAID());
//			System.out.print(this.myAgent.getLocalName() + " J'informe ");
				try {
					GraphAK G = ((AK_Agent)myAgent).getGraph();
					if((boolean)received_msg.getContentObject())  //Si a deja tout explorer envoyer seulement la hashMap des noeuds
						msg.setContentObject(G.getHashNode());
					else
						msg.setContentObject(new Couple<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>>(G.getHashNode(),G.getDictAdjacences()));
					msg.addReceiver(received_msg.getSender());                           //n'informe que le demandeur
				} catch (IOException | UnreadableException e) {
					e.printStackTrace();
				}
			((mas.abstractAgent)this.myAgent).sendMessage(msg);
			System.out.println(myAgent.getLocalName()+" : INFORM AGENT ");

		}
	}


}
