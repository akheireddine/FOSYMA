package mas.behaviours.tanker;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.GCheckInBoxBehaviour;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
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
		registerState(new GCheckInBoxBehaviour(a),"C");
		registerState(new TMajKnowledgeBehaviour(), "M"); 
		
		registerState(new ClosenessVertices((abstractAgent) a,a.getGraph(),this),"V");
		registerLastState(new MoveToNode((abstractAgent) a,a.getGraph()),"G");
		
//		a.addBehaviour(new TFSMBehaviour2(a));
		

		
		
		registerDefaultTransition("D","V");
		registerDefaultTransition("S","C");
//		registerDefaultTransition("S","D");

		registerDefaultTransition("C","D");
		registerDefaultTransition("M", "D");
		
		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
		registerTransition("D","C",1);
		registerTransition("D","S",0);
		
		
//		registerTransition("G", "S", 2);
//		registerTransition("G", "C", 3);
//		registerDefaultTransition("C", "G");
//		registerDefaultTransition("M", "G");
		
		
		registerDefaultTransition("V","G");

	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
