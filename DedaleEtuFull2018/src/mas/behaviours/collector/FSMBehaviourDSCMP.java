package mas.behaviours.collector;

import mas.abstractAgent;
import mas.agents.AK_Collector;
import mas.behaviours.NewMajKnowledgeBehaviour;
import mas.behaviours.SendInformationAfterCollisionBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class FSMBehaviourDSCMP extends FSMBehaviour {

	
	private static final long serialVersionUID = 5095792340973440186L;


	public FSMBehaviourDSCMP(AK_Collector a){
		super(a);
		
		registerFirstState(new WalkBehaviourCollector((abstractAgent) a,a.getGraph()),"D");
		registerState(new SendInformationAfterCollisionBehaviour(), "S");
		registerState(new CheckInBoxCollectorBehaviour(a),"C");
		registerState(new NewMajKnowledgeBehaviour(), "M"); // update of graph env
		registerState(new PickTreasur(), "P");
		registerState(new SearchSilo((abstractAgent)a, a.getGraph()), "SS");
		registerState(new ThrowBackPack(), "T");
		

		registerTransition("D", "P", 1);
		registerTransition("D", "S", 2);
		registerTransition("D", "C", 3);
		
		
		registerDefaultTransition("P", "D");
		registerTransition("P", "SS", 1);
		
		registerTransition("SS", "D", 1);
		registerTransition("SS", "P", 2);
		registerTransition("SS", "S", 3);
		registerTransition("SS", "C", 4);
		
		
		registerDefaultTransition("S", "D");
		registerTransition("S", "SS", 2);
		
		registerDefaultTransition("C", "D");
		registerTransition("C", "SS", 1);
		

	}
	
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
//		myAgent.doDelete();
		return super.onEnd();
	}

}
