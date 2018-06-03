package mas.behaviours.explorer;

import mas.agents.AK_Agent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ECheckInBoxBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8844060548399027721L;
	private int onEndValue=0;
	
	
	public ECheckInBoxBehaviour(final Agent myagent) {
		super(myagent);
	}
	

	public void action() {
		ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		if(msg!=null)
			this.onEndValue = 1;
		
		else 
			msg = this.myAgent.receive();
		
		if(msg != null){
			((AK_Agent)myAgent).setToread(msg);
			System.out.println(myAgent.getLocalName()+" : Receive MSG ");
		}else {
			System.out.println(myAgent.getLocalName()+" : No MSG. ");
		}

//		while(myAgent.receive()!=null);
		
		
	}
	


	
	
	
	public int onEnd() {
		return onEndValue;
	}

}