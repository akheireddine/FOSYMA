package mas.behaviours.tanker;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import jade.core.behaviours.FSMBehaviour;

public class TFSMBehaviour1 extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public TFSMBehaviour1() {
		super();
	}

	public TFSMBehaviour1(AK_Agent a){
		super(a);
		registerFirstState(new TWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new ClosenessVertices((abstractAgent) a,a.getGraph()),"V");
		registerLastState(new MoveToNode((abstractAgent) a,a.getGraph()),"G");
		a.addBehaviour(new TFSMBehaviour2(a));
		
//		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
//		registerState(new TCheckSendMessageBehaviour(a),"C");
//			
//		registerState(new EMajKnowledgeBehaviour(), "M"); // update of graph env
//		
//		
//		//definition des transaction
		registerDefaultTransition("D","V");
		registerDefaultTransition("V","G");
//		registerDefaultTransition("C","D");
//		registerDefaultTransition("M", "D");
//		
//		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
//		registerTransition("D","C",1);

	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		return super.onEnd();
	}

}
