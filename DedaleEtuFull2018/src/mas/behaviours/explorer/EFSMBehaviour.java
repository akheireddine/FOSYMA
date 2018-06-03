package mas.behaviours.explorer;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.explorer.ECheckInBoxBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class EFSMBehaviour extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public EFSMBehaviour() {
		super();
	}

	public EFSMBehaviour(AK_Agent a){
		super(a);
		registerFirstState(new EWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new ESendInformationAfterCollisionBehaviour(), "S");
		registerState(new ECheckInBoxBehaviour(a),"C");
			
		registerState(new EMajKnowledgeBehaviour(), "M"); // update of graph env
		
		
		//definition des transaction
		registerDefaultTransition("S","C");

		registerDefaultTransition("C","D");
		registerDefaultTransition("M", "D");
		
		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
		registerTransition("D","C",1);
		registerTransition("D","S",0);


	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
