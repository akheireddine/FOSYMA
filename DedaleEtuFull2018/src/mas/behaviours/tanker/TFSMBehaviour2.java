package mas.behaviours.tanker;

import jade.core.behaviours.FSMBehaviour;
import mas.abstractAgent;
import mas.agents.AK_Agent;

public class TFSMBehaviour2 extends FSMBehaviour {
	private static final long serialVersionUID = -6883275941056007511L;

	public TFSMBehaviour2() {
		super();
	}


	public TFSMBehaviour2(AK_Agent a){
		super(a);
		registerFirstState(new TWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new ClosenessVertices((abstractAgent) a,a.getGraph()),"V");
		registerLastState(new MoveToNode((abstractAgent) a,a.getGraph()),"G");
//		a.addBehaviour(new TFSMBehaviour1(a));
		
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
