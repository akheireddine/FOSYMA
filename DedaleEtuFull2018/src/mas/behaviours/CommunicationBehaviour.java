package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

public class CommunicationBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8701757571366016884L;

	
	public CommunicationBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
	}
	
	
	
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			
			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();
			
			for(Attribute a:lattribute){
				switch (a) {
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				default:
					break;
				}
			}
			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
		
		}
	}

	public boolean done() {
		return false;
	}


}
