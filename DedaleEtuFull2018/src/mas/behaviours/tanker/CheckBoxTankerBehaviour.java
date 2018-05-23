package mas.behaviours.tanker;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import scala.Tuple3;
import Tools.DFDServices;
import Tools.GraphAK;
import mas.agents.AK_Agent;
import env.Attribute;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class CheckBoxTankerBehaviour extends TickerBehaviour{

	private static final long serialVersionUID = 2937310586048856461L;
//	private int onEndValue;
	
	public CheckBoxTankerBehaviour (final Agent myagent) {
		super(myagent,200);
	}
	
	@Override
	public void onTick() {
		
			GraphAK G = ((AK_Agent)myAgent).getGraph();
		
			G.addPossiblePositionSilo(((mas.abstractAgent)myAgent).getCurrentPosition());

			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			
			//Priorite 1 aux messages INFORM (MaJ de mes connaissances)
			if(msg!=null){
				final ACLMessage send_msg = new ACLMessage(ACLMessage.CONFIRM);
				send_msg.setSender(this.myAgent.getAID());
				
				//chercher les AID des agents que je j'envoie le message d'information
				AID[] sellerAgents = DFDServices.getAgentsByService("explorer",myAgent);
				//m'enlever de la liste des receivers
				if(sellerAgents != null){
					for(AID agt : sellerAgents)
							send_msg.addReceiver(agt);
				}
				
				sellerAgents = DFDServices.getAgentsByService("collector",myAgent);
				//m'enlever de la liste des receivers
				if(sellerAgents != null){
					for(AID agt : sellerAgents)
							send_msg.addReceiver(agt);
				}
				
				try {
					//Envoi un tuple contenant (informations sur les noeuds, dictionnaire d'adjacence, sommets ouverts, sommets fermes )
					send_msg.setContentObject(new Tuple3<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>>(G.getHashNode(),G.getDictAdjacences(),G.siloPosition()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				((mas.abstractAgent)this.myAgent).sendMessage(send_msg);
				System.out.println(myAgent.getLocalName()+" : CONFIRM TO AGENT THAT IM THE SILO ");
			}
			
		}

//		public boolean done() {
//			return false;
//		}
				
				

}		
