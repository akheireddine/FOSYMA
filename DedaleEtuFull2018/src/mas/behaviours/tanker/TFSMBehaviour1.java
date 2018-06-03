package mas.behaviours.tanker;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import mas.behaviours.explorer.ECheckInBoxBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class TFSMBehaviour1 extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public TFSMBehaviour1() {
		super();
	}

	public TFSMBehaviour1(AK_Agent a){
		super(a);
		registerFirstState(new TWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
		registerState(new ECheckInBoxBehaviour(a),"C");
		registerState(new TMajKnowledgeBehaviour(), "M"); 
		
		registerLastState(new ClosenessVertices((abstractAgent) a,a.getGraph(),this),"V");
//		registerLastState(new MoveToNode((abstractAgent) a,a.getGraph()),"G");
		
		

		
		
		registerDefaultTransition("D","V");
		registerDefaultTransition("S","C");
//		registerDefaultTransition("S","D");

		registerDefaultTransition("C","D");
		registerDefaultTransition("M", "D");
		
		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
		registerTransition("D","C",1);
		registerTransition("D","S",0);
		
//		registerDefaultTransition("V","G");

	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
