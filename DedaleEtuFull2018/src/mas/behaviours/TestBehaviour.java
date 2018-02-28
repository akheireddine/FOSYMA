package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;



public class TestBehaviour extends TickerBehaviour {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2701771410667046484L;
	private Graph graph;
	private PriorityQueue<Couple<String, Integer>> ouverts;
	private Set<String> fermes;
	
//	private class OuvertDistanceComparator implements Comparator{

//		@Override
		
		
//	}

	public TestBehaviour(final mas.abstractAgent myagent) {
		
		super(myagent, 1000);
		this.graph = new SingleGraph(myagent.getName()+" 's graph");
		this.ouverts = new PriorityQueue<Couple<String, Integer>>(new OuvertDistanceComparator());
		this.fermes = new HashSet<String>();
		//super(myagent);
	}


	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);
			
			this.fermes.add(myPosition);
			
//			for 
			
			
//			//Little pause to allow you to follow what is going on
			try {
				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}


			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();

			
			//example related to the use of the backpack for the treasure hunt
			Boolean b=false;
			
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE:
					System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("Value of the treasure on the current position: "+a.getValue());
					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
					b=true;
					//Little pause to allow you to follow what is going on
					try {
						System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
						System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			case DIAMONDS:
				System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("Value of the diamonds on the current position: "+a.getValue());
				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
				System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				b=true;
				//Little pause to allow you to follow what is going on
				try {
					System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
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
			
			

			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
		
			//Random move from the current position
			Random r= new Random();
			//1) get a couple <Node ID,list of percepts> from the list of observables
			int moveId=r.nextInt(lobs.size());
		
			//2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
		}
	}	
}

