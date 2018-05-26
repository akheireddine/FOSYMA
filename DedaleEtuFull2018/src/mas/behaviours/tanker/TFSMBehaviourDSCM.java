package mas.behaviours.tanker;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.explorer.ECheckInBoxBehaviour;
import mas.behaviours.explorer.EMajKnowledgeBehaviour;
import mas.behaviours.GNewMajKnowledgeBehaviour;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class TFSMBehaviourDSCM extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public TFSMBehaviourDSCM() {
		super();
	}

	public TFSMBehaviourDSCM(AK_Agent a){
		super(a);
		registerFirstState(new TWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
		registerState(new TCheckSendMessageBehaviour(a),"C");
			
		registerState(new EMajKnowledgeBehaviour(), "M"); // update of graph env
		
		
		//definition des transaction
		registerDefaultTransition("D","S");
		registerDefaultTransition("S","C");
		registerDefaultTransition("C","D");
		registerDefaultTransition("M", "D");
		
		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
		registerTransition("D","C",1);

	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
