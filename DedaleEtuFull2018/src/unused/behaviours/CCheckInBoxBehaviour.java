package unused.behaviours;

import mas.agents.AK_Agent;
import mas.agents.AK_Collector;
import scala.Tuple3;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import env.Attribute;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CCheckInBoxBehaviour extends OneShotBehaviour {


	private static final long serialVersionUID = 6848553541314740744L;
	private int onEndValue=-1;
	
	
	public CCheckInBoxBehaviour(final Agent myagent) {
		super(myagent);
	}
	

	public void action() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ACLMessage msg;
		if(((AK_Collector)myAgent).iPicked()){
			msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
			if(msg!=null) {
				try {
					@SuppressWarnings("unchecked")
					Tuple3<HashMap<String, List<Attribute>>, HashMap<String, Set<String>>, List<String>> info = (Tuple3<HashMap<String, List<Attribute>>, 
							HashMap<String,Set<String>>,List<String>>) msg.getContentObject();
					
					((AK_Agent)myAgent).getGraph().addAllPositionSilo(info._3());
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		 
		msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		//Priorite 1 aux messages INFORM (MaJ de mes connaissances)
		if(msg!=null)
			this.onEndValue = 1;
		else{
			//Priorite 2 aux messages REQUEST (un agent demande des informations)
			msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));            //Recu une demande d'information 
			if(msg!=null)
				this.onEndValue = 2;
			//Priorite 3 le reste
			else
				msg = this.myAgent.receive();
		}
		
		if(msg != null){
			((AK_Agent)myAgent).setToread(msg);
//			System.out.println(myAgent.getLocalName()+" : Receive MSG "+this.onEndValue);
		}else {
//			System.out.println(myAgent.getLocalName()+" : No MSG. ");
			this.onEndValue = -1;
		}
	}

	
	
	
	public int onEnd() {
		return onEndValue;
	}

}