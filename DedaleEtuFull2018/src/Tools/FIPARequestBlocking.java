package Tools;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class FIPARequestBlocking  extends SimpleBehaviour {

	private static final long serialVersionUID = 1777537060390682509L;

	
	
	public void action() {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST); 
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		AID[] sellerAgents = DFDServices.getAgentsByService("explorer",this.myAgent);
		for(AID agt : sellerAgents){
			if (!agt.getLocalName().equals(myAgent.getLocalName())){
				request.addReceiver(agt);
			}
		}
		myAgent.addBehaviour( new AchieveREInitiator(myAgent, request) { 
			private static final long serialVersionUID = 1L;
		protected void handleInform(ACLMessage inform) { 
		              System.out.println("Protocol  finished.  Rational  Effect  achieved. Received the following message: "+inform); 
		       }
		});
	}

	public boolean done() {
		return true;
	}

}
