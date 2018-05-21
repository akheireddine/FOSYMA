package not.sure;

import java.io.IOException;

import mas.agents.AK_Agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class RequestKnowledge extends SimpleBehaviour {

	private static final long serialVersionUID = 1186641889943105924L;
	private boolean finished=false;
	
	
	/**
	 * 
	 * @param myagent the Agent this behaviour is linked to
	 */
	public RequestKnowledge(final Agent myagent) {
		super(myagent);
	}
	
	public void action() {
		
		final ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(this.myAgent.getAID());
		try {
			msg.setContentObject(((AK_Agent)myAgent).isExplorationDone());
		}catch (IOException e1) {
			e1.printStackTrace();
		}
//		msg.setContent(content);
//		System.out.println(this.myAgent.getLocalName() + " J'envoie une demande de complement de la carte\n");
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorer");
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		
		try {
			result = DFService.search(myAgent, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		AID[] sellerAgents = new AID[result.length];
		
		for(int i = 0; i < result.length; i++){
			sellerAgents[i] = result[i].getName();
		}
		
//		System.out.println("seller1 "+ result[1]);
		for(AID agt : sellerAgents){
			if (!agt.getLocalName().equals(myAgent.getLocalName())){
//				System.out.println("\n\n"+myAgent.getLocalName()+" : J'envoie une requête à "+agt.getLocalName());
				msg.addReceiver(agt);
			}
		}
		((mas.abstractAgent)this.myAgent).sendMessage(msg);
		System.out.println(myAgent.getLocalName()+" : REQUEST GRAPH ");
		this.finished = true;
	}

	public boolean done() {
		return finished;
	}
	
	public int onEnd() {
		return 2;
	}

}
