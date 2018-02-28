package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.HashSet;
import jade.util.leap.Iterator;

public class BFSWalkBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7019781867825759239L;
	private ArrayList<String> Pile;
	private HashSet fermes,ouverts;
	private SimpleGraph<String, DefaultEdge> G;
	
	public BFSWalkBehaviour (final mas.abstractAgent myagent) {
		super(myagent, 1000);
		Pile = new ArrayList<String>();
		fermes = new HashSet();
		ouverts = new HashSet();
		//super(myagent);
	}


	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);

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
			
//			for(Attribute a:lattribute){
//				switch (a) {
//				case TREASURE:
//					System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
//					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//					System.out.println("Value of the treasure on the current position: "+a.getValue());
//					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
//					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
//					b=true;
//					//Little pause to allow you to follow what is going on
//					try {
//						System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
//						System.in.read();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					break;
//			case DIAMONDS:
//				System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
//				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//				System.out.println("Value of the diamonds on the current position: "+a.getValue());
//				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
//				System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//				System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
//				b=true;
//				//Little pause to allow you to follow what is going on
//				try {
//					System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
//					System.in.read();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				default:
//					break;
//				}
//			}

			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
			String nextPos= "";
			Boolean repeat = false;
			
			do{
				repeat = false;
				String current_node = lobs.get(0).getLeft();
				Pile.add(0,current_node);
				fermes.add(current_node);
				if(myPosition!=""){
					ouverts.remove(current_node);
				}
				
				
				
				Iterator i = fermes.iterator();
				while(i.hasNext()){
					System.out.println(" stocked in fermees "+i.next().toString());
				}
				i = ouverts.iterator();
				while(i.hasNext()){
					System.out.println(" stocked in ouverts "+i.next().toString());
				}

				
				Boolean d = false, c= false;
				List<String> candidats = new ArrayList<String>();
				List<String> cand_adj = new ArrayList<String>();
				
				for(Couple<String,List<Attribute>> adj: lobs){
					String name_node = adj.getLeft();

					if( !fermes.contains(name_node) ){
						if( !ouverts.contains(name_node) ){
//							System.out.println(" noeud non ouvert non ferme "+name_node);
							cand_adj.add(name_node);
							ouverts.add(name_node);
							d = true;
						}
						else{
//							System.out.println(" noeud ouvert non ferme "+name_node);
							candidats.add(name_node);
							c = true;
						}
					}
				}
				
				if(d){           // nouveau noeud adj non dans la bordure
					Random r = new Random();
					nextPos = cand_adj.remove(r.nextInt(cand_adj.size()));
				}else if(c){   // noeuds adj deja dans la bordure
					Random r = new Random();
					nextPos = candidats.remove(r.nextInt(candidats.size()));
					Pile.remove(nextPos);
				}
				else if (!d && !c){  //depiler car ne trouve pas de noeuds adjacents a visite
					if(Pile.size()>2){
						Pile.remove(0);
						nextPos = Pile.remove(0);
					}
					else{
						System.out.println("REPEAAAT______________");
						fermes.clear();
						ouverts.clear();
						ouverts.add(current_node);
						repeat = true;
					}
				}

//				2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
				((mas.abstractAgent)this.myAgent).moveTo(nextPos);
			}while(repeat);
			

		}

	}

}