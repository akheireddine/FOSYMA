package mas.originalClasses;

import mas.abstractAgent;
import mas.behaviours.OldInformAgentBehaviour;
import not.sure.RequestKnowledge;
import jade.core.behaviours.FSMBehaviour;

public class FSMBehaviourDRC extends FSMBehaviour {

	
	private static final long serialVersionUID = -2099919019475959526L;
	
	public FSMBehaviourDRC() {
		super();
	}

	public FSMBehaviourDRC(AK_Agent a){
		super(a);
//		registerFirstState(new ReceiveMessage(a),"C");
		
		registerState(new WalkBehaviour((abstractAgent) a,a.getGraph()),"D");
		registerState(new RequestKnowledge(a),"R");// knowledge about the env
		registerState(new MajKnowledgeBehaviour(), "M"); // update of graph env
		registerState(new OldInformAgentBehaviour(), "I"); // send graph to neighbours agent
//		registerState(new FIPARequestBlocking(),"F");
//		registerState(new FIPARespondeBlocking(),"S");
		
		
		//definition des transaction
		registerDefaultTransition("M", "D");
		registerDefaultTransition("D","R"); //modifier le request quand on a tt explorer, on cherchera a savoir seulement les noeuds qui se sont modifies
		registerDefaultTransition("I","D");
		registerDefaultTransition("R","C");
//
//		
		registerDefaultTransition("C","D");
		registerTransition("C","I",1);
		registerTransition("C","M",2);
		
//		registerTransition("D","F", 1);
//		
//		registerDefaultTransition("F", "S");
//		registerDefaultTransition("S", "R");

	}
	
	
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
//		myAgent.doDelete();
		return super.onEnd();
	}

}
