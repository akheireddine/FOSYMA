package mas.behaviours.collector;


import mas.agents.AK_Agent;
import tools.GraphAK;
import jade.core.behaviours.OneShotBehaviour;

public class PickTreasur extends OneShotBehaviour{

	private static final long serialVersionUID = 1674247763532723070L;
	private int onEndValue = -1;


	public void action() {
		
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if(myPosition!="") {
			Boolean pickedTreasur=false;
			final int capacityBag = ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace();
			GraphAK G = ((AK_Agent)myAgent).getGraph();
			
			 String goal = G.chooseTreasureToPick(myPosition,((mas.abstractAgent)myAgent).getMyTreasureType(),capacityBag);
			if(goal !=null){
				
			}
			
//			for(Attribute a:lattribute){
//				switch(a) {
//				case TREASURE: case DIAMONDS :
//					if(capacityBag > (int)a.getValue()) {
//						((mas.abstractAgent)this.myAgent).pick();
//						pickedTreasur=true;
//					}
////					System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
////					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
////					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
////					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
//					break;
//	
//				default:
//					break;
//				}
//			}
			
			if(pickedTreasur) {//chercher le silo
				this.onEndValue = 1;
				System.out.println(myAgent.getLocalName()+" : I pick treasure");
			}
				
		}
	}


    public int onEnd() {
	      return this.onEndValue ;
	    } 
	

}
