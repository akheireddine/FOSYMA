package mas.behaviours.collector;

import mas.abstractAgent;
import mas.agents.AK_Collector;
import mas.behaviours.CheckInBoxBehaviour;
import mas.behaviours.NewMajKnowledgeBehaviour;
import mas.behaviours.SendInformationAfterCollisionBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class FSMBehaviourDSCMP extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	

	public FSMBehaviourDSCMP(AK_Collector a){
		super(a);
		registerFirstState(new WalkBehaviourCollector((abstractAgent) a,a.getGraph()),"D");
		registerState(new SendInformationAfterCollisionBehaviour(), "S");
		registerState(new CheckInBoxBehaviour(a),"C");
		registerState(new NewMajKnowledgeBehaviour(), "M"); // update of graph env
		registerState(new PickTreasur(), "P");
		
		//definition des transaction
//		registerDefaultTransition("S","C");
//		registerDefaultTransition("C","D");
//		registerDefaultTransition("M", "D");
//		registerDefaultTransition("P", "D");
		
		registerDefaultTransition("D", "P");
		registerDefaultTransition("P", "D");

//		registerTransition("D","S",1);
//		registerTransition("D", "P",2);
//		registerTransition("C","M",1);              // Quand l'agent recoit un message apres une collision, MaJ de ses connaissances
//		registerTransition("D","C",1);


	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
//		myAgent.doDelete();
		return super.onEnd();
	}

}
