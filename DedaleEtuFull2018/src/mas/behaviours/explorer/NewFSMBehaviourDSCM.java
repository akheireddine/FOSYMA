package mas.behaviours.explorer;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.behaviours.CheckInBoxBehaviour;
import mas.behaviours.NewMajKnowledgeBehaviour;
import mas.behaviours.SendInformationAfterCollisionBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class NewFSMBehaviourDSCM extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public NewFSMBehaviourDSCM() {
		super();
	}

	public NewFSMBehaviourDSCM(AK_Agent a,boolean i){
		super(a);
//		if (i)
			registerFirstState(new CopyOfWalkBehaviourHeuristic((abstractAgent) a,a.getGraph()),"D");
//		else
//			registerFirstState(new CopyOfWalkBehaviourRandomize((abstractAgent) a,a.getGraph()),"D");
		registerState(new SendInformationAfterCollisionBehaviour(), "S");
		registerState(new CheckInBoxBehaviour(a),"C");
			
		registerState(new NewMajKnowledgeBehaviour(), "M"); // update of graph env
		
		
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
