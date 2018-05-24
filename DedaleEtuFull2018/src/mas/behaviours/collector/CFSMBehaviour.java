package mas.behaviours.collector;

import mas.abstractAgent;
import mas.agents.AK_Collector;
import mas.behaviours.GNewMajKnowledgeBehaviour;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import mas.behaviours.explorer.ECheckInBoxBehaviour;
import mas.behaviours.explorer.EMajKnowledgeBehaviour;
import mas.behaviours.explorer.ESendInformationAfterCollisionBehaviour;
import mas.behaviours.explorer.EWalkBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class CFSMBehaviour extends FSMBehaviour {

	
	private static final long serialVersionUID = 5095792340973440186L;


	public CFSMBehaviour(AK_Collector a){
//		super(a);
//		
//		registerFirstState(new WalkBehaviourCollector((abstractAgent) a,a.getGraph()),"D");
//		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
//		registerState(new CheckInBoxCollectorBehaviour(a),"C");
//		registerState(new GNewMajKnowledgeBehaviour(), "M"); // update of graph env
////		registerState(new PickTreasur(), "P");
////		registerState(new SearchSilo((abstractAgent)a, a.getGraph()), "SS");
////		registerState(new ThrowBackPack(), "T");
//		
//
////		registerTransition("D", "SS", 1);
//		registerDefaultTransition("D", "S");
//		registerTransition("D", "C", 1);
//		
//		
////		registerDefaultTransition("P", "D");
////		registerTransition("P", "SS", 1);
//		
////		registerTransition("SS", "D", 1);
////		registerTransition("SS", "P", 2);
////		registerTransition("SS", "S", 3);
////		registerTransition("SS", "C", 4);
//		
//		
//		registerDefaultTransition("S", "D");
////		registerTransition("S", "SS", 2);
//		
//		registerDefaultTransition("C", "D");
//		registerTransition("C", "M", 1);
//		
//		registerDefaultTransition("M", "D");
		
		
		super(a);
		registerFirstState(new CWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
		registerState(new CCheckInBoxBehaviour(a),"C");
		registerState(new GNewMajKnowledgeBehaviour(), "M"); // update of graph env
		
		
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
//		myAgent.doDelete();
		return super.onEnd();
	}

}
