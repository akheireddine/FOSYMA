package mas.behaviours.collector;

import java.util.List;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import Tools.DFDServices;
import Tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.AK_Agent;

public class SearchSilo extends SimpleBehaviour {


	private static final long serialVersionUID = -7810370726603702064L;
	private Set<String> fermes,ouverts;
	private GraphAK G;
	private int onEndValue=0;
	private boolean finished = false;
	
	public SearchSilo(final mas.abstractAgent myagent, GraphAK g) {
		super(myagent);
		G = g;
		this.fermes = G.getFermes();
		this.ouverts = G.getOuverts();
	}	
	
	
	public String getNextPositionNearestOpenVertex(String src,String dest){
		String next_node = ((mas.abstractAgent)myAgent).getCurrentPosition();
		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
		List<String> shortestPath = dijkstraShortestPath.getPath(src,dest).getVertexList();
		next_node = shortestPath.get(1);
		
//		for(String dst: ouverts){
//			List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
//			if(shortestPath.size() < dist_min  && !coll_node.equals(shortestPath.get(1))){
//				dist_min = shortestPath.size();
//				next_node = shortestPath.get(1);
//			}
//		}
		return next_node;
	}
	
	
	public boolean throwMyTreasur() {
		AID[] sellerAgents = DFDServices.getAgentsByService("silo",myAgent);
		for(AID agt : sellerAgents) {
			return ((mas.abstractAgent)myAgent).emptyMyBackPack(agt.getLocalName());
		}
		return false;
	}
	
	
	@Override
	public void action() {
		
		String myPosition = ((mas.abstractAgent)myAgent).getCurrentPosition();
		List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
		Couple<String, List<Attribute>> curr_observation = lobs.remove(0);
		if(G.containsTreasur(myPosition,((mas.abstractAgent)myAgent).getMyTreasureType())) {
			this.finished = true;
			this.onEndValue = 2; //voir si on peut picker le tresor sur notre chemin
			return;
		}
		
		
		if (!((AK_Agent)myAgent).getGraph().isSiloPositionKnown()) { // Si je connais pas sa position je me remet en mode explorer

		}else {
			String next_pos = getNextPositionNearestOpenVertex(myPosition,G.getSiloPosition());
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
			
//			if(next_pos.equals(G.getSiloPosition()) && has_moved) {
//				this.finished = true;
//				this.onEndValue = 1; // On a trouve le silo on peut quitter le comportement de recherche
//				return;
//			}
			if(throwMyTreasur()) {
				this.onEndValue = 1;
				this.finished = true;
			}
			
			if (has_moved){//continue de bouger jusqu'a arriver vers le silo
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).CptPlus();
			}
			else{
				
				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision();
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision+1);
				
				
				boolean golem = false;
				for(Attribute a : curr_observation.getRight()) {
					switch(a) {
					case STENCH:
						this.fermes.add(next_pos);
						this.ouverts.remove(next_pos);
						golem = true;
						System.out.println(myAgent.getLocalName()+ " : Collision avec le golem !");
						break;
					case AGENT_TANKER:
						System.out.println(myAgent.getLocalName()+" : JE VOIS LE TANKER ! ");
					default:
						break;
					}
				}
			
				//Dans le cas ou on echange les Ouverts, fermes
//				ouverts.remove(next_pos);
//				fermes.add(next_pos);
			
				//Si premiere collision, envoie un message d'information
				if(nb_collision==1 && !golem) {
					this.finished=true;
					this.onEndValue = 3; //Envoyer info puis checker sa boite
				}
//				else if (nb_collision ==2 && !golem)
//					((AK_Agent)myAgent).setCollisionNode(next_pos);

				else if (nb_collision == 2 && !golem){//check s'il a bien lu le msg recu par l'agent collision
					this.finished=true;
					this.onEndValue = 4; //rechecker sa box    
				}
				else if (nb_collision>2 && golem) {
					System.out.println(myAgent.getLocalName()+" : GOLEM IS HERE ! ");
				}
				

			}
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.finished;
	}
	
	
    public int onEnd() {
      return this.onEndValue ;
    } 

}
