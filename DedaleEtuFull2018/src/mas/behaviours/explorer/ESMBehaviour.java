package mas.behaviours.explorer;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.explorer.ECheckInBoxBehaviour;
import mas.behaviours.GNewMajKnowledgeBehaviour;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class ESMBehaviour extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public ESMBehaviour() {
		super();
	}

	public ESMBehaviour(AK_Agent a){
		super(a);
		registerFirstState(new EWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new ESendInformationAfterCollisionBehaviour(), "S");
		registerState(new ECheckInBoxBehaviour(a),"C");
			
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
