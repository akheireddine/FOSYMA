package mas.behaviours.collector;

import java.util.List;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.AK_Agent;

public class PickTreasur extends OneShotBehaviour{

	private static final long serialVersionUID = 1674247763532723070L;
	private int onEndValue = 0;


	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if(myPosition!="") {
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			List<Attribute> lattribute= lobs.get(0).getRight();
			Boolean pickedTreasur=false;
			final String myType = ((mas.abstractAgent)this.myAgent).getMyTreasureType();
			final int capacityBag = ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace();
			for(Attribute a:lattribute){
				switch(a) {
				case TREASURE: case DIAMONDS :
					if (a.getName().equals(myType)) {
						if(capacityBag > (int)a.getValue()) {
							((mas.abstractAgent)this.myAgent).pick();
							pickedTreasur=true;
						}
					}
//					System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
//					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
//					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
					break;
	
				default:
					break;
				}
			}
			
			if(pickedTreasur) {//chercher le silo
				if (((AK_Agent)myAgent).getGraph().isSiloPositionKnown()) {
				
				}
				
			}
				
		}
	}



    public int onEnd() {
	      return this.onEndValue ;
	    } 
	
	

}
