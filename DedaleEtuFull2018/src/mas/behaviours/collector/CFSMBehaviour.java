package mas.behaviours.collector;

import mas.abstractAgent;
import mas.agents.AK_Collector;
import mas.behaviours.GCheckInBoxBehaviour;
import mas.behaviours.GSendInformationAfterCollisionBehaviour;
import mas.behaviours.explorer.EMajKnowledgeBehaviour;
import jade.core.behaviours.FSMBehaviour;

public class CFSMBehaviour extends FSMBehaviour {

	
	private static final long serialVersionUID = 5095792340973440186L;


	public CFSMBehaviour(AK_Collector a){
	
		super(a);
		registerFirstState(new CWalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new GSendInformationAfterCollisionBehaviour(), "S");
		registerState(new GCheckInBoxBehaviour(a),"C");
		registerState(new EMajKnowledgeBehaviour(), "M"); // update of graph env
		
		
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
