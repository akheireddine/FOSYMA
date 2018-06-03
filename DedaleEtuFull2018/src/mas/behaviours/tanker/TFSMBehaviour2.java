package mas.behaviours.tanker;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import mas.behaviours.explorer.ECheckInBoxBehaviour;

public class TFSMBehaviour2 extends FSMBehaviour {
	private static final long serialVersionUID = -6883275941056007511L;

	public TFSMBehaviour2() {
		super();
	}


	public TFSMBehaviour2(AK_Agent a){
		super(a);
		registerFirstState(new MoveToNode((abstractAgent) a,a.getGraph()),"G");
		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
		registerState(new ECheckInBoxBehaviour(a),"C");
		registerState(new TMajKnowledgeBehaviour(), "M"); 
		
		
//		
		registerDefaultTransition("S","C");

		registerDefaultTransition("C","G");
		registerDefaultTransition("M", "G");
		
		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
		registerTransition("G","C",1);
		registerTransition("G","S",0);
	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
