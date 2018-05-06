package Tools;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class FIPARespondeBlocking extends SimpleBehaviour {

	private static final long serialVersionUID = -8261155948519569418L;


		public void action() {
			MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
			this.myAgent.addBehaviour( new AchieveREResponder(myAgent, mt) { 
				private static final long serialVersionUID = 1L;
				protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) { 
				       System.out.println("Responder  has  received  the  following  message:  " + request); 
				       ACLMessage informDone = request.createReply(); 
				       informDone.setPerformative(ACLMessage.INFORM);       
				       informDone.setContent(((mas.abstractAgent)this.myAgent).getCurrentPosition()); 
				       return informDone;       
				}
				protected ACLMessage handleRequest(ACLMessage request) {
					return request;
					
				}
			
			});
		}
		
		
		public boolean done() {
			return true;
		}       
}
