package mas.behaviours.explorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import mas.agents.AK_Agent;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import Tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

public class WalkDontKnowBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = -5308185370371990470L;
		private Set<String> fermes,ouverts;
		private GraphAK G;
		private int onEndValue=0;
		private boolean finished = false;
		
		public WalkDontKnowBehaviour(final mas.abstractAgent myagent, GraphAK g) {
			super(myagent);
			G = g;
			this.fermes = G.getFermes();
			this.ouverts = G.getOuverts();
		}


		/***
		 * @param src position courante 
		 * @param adjacents : liste de couple contenant le nom du noeud et les informations sur ce noeud
		 * @return List<String> des noeuds adjacents
		 */
		public List<String> m_a_j_graphe(String src, List<Couple<String, List<Attribute>>> adjacents){
			List<String> ladj_node = new ArrayList<String>();

			if(!((AK_Agent)myAgent).isExplorationDone()) {
				G.addVertex(src);
				for(Couple<String, List<Attribute>> adjacent: adjacents){
					String adj_name = adjacent.getLeft();
					ladj_node.add(adjacent.getLeft());
					G.addVertex(adj_name,adjacent.getRight());
					G.addEdge(src,adj_name);
				}
			}else {
				for(Couple<String, List<Attribute>> adjacent: adjacents){
					G.updateNode(adjacent.getLeft(), adjacent.getRight());
					ladj_node.add(adjacent.getLeft());
				}
			}
			return ladj_node;
		}
		
		
		/***
		 * @param src position courante 
		 * @return le prochain deplacement vers le noeud ouvert non exploré le plus proche(DIJKSTRA)
		 */
//		public String getNextPositionNearestOpenVertex(String src){
//			DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
//			int dist_min = G.vertexSet().size();
//			String next_node = src;
//			
//			for(String dst: ouverts){
//				List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
//				if(shortestPath.size() < dist_min){
//					dist_min = shortestPath.size();
//					next_node = shortestPath.get(1);
//				}
//			}
//			return next_node;
//		}
		
		
		public String getNextPositionNearestOpenVertex(String src){
			DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
			int dist_min = G.vertexSet().size();
			String next_node = "";
			String coll_node = ((AK_Agent)myAgent).getRecentCollisionNode();
			for(String dst: ouverts){
				List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
				if(shortestPath.size() < dist_min  && !coll_node.equals(shortestPath.get(1))){
					dist_min = shortestPath.size();
					next_node = shortestPath.get(1);
				}
			}
			return next_node;
		}
		
		
		
		public String choisirLeProchainNodeOuvert(List<String> successors){
			String next_node;
			Float r = new Random().nextFloat();
			
			if(true){//r <= 2./3. || !((AK_Agent)myAgent).getDoneExploration()){
				next_node = successors.get(0);
				int max = G.getNbOpenNeighborVertex(next_node);
				for(String succ : successors) {
					int value_tmp_node = G.getNbOpenNeighborVertex(succ);
					if(value_tmp_node > max) {
						max = value_tmp_node;
						next_node = succ;
					}
				}
//			}else if(1./3. < r && r <= 2./3. ){
//				next_node = successors.get(0);
//				int max = G.getDegreeOfNode(next_node);
//				for(String succ : successors) {
//					int	value_tmp_node = G.getDegreeOfNode(succ);
//					if(value_tmp_node > max) {
//						max = value_tmp_node;
//						next_node = succ;
//					}
				}
//			}else{
//				next_node = successors.get(new Random().nextInt(successors.size()));
//			}
			//		System.out.println(" PROCHAIN NOEUD "+next_node+"("+max+")");
			return next_node;
		}
		
		
		/***
		 * DEUX POSSIBILITES :
		 *  	Priorite au deplacement vers les adjacents ouverts
		 *  	Sinon vers la recherche d'un deplacement vers le plus proche noeud ouvert non voisin
		 * @param successeurs_non_visites Liste des adjacents non visité
		 * @return prochain deplacement
		 */
		public String getNextPosition(List<String> successeurs_non_visites ){
			String next_pos;
			if(!successeurs_non_visites.isEmpty()){
				next_pos =  choisirLeProchainNodeOuvert(successeurs_non_visites);
			}else{
				String myPosition = ((mas.abstractAgent)this.myAgent).getCurrentPosition();
				next_pos = getNextPositionNearestOpenVertex(myPosition);
			}
			return next_pos;
		}
		
		
		
		public List<String> get_open_neighbors(List<String> adj_names){
			//Completer les noeuds ouverts et determiner l'ensemble des successeurs potentiels
			List<String> successeurs_non_visites = new ArrayList<String>();
			for(String adj: adj_names){
				if(!this.fermes.contains(adj)){
					this.ouverts.add(adj);
					successeurs_non_visites.add(adj);
				}
			}
			return successeurs_non_visites;
		}
		
		
		
		
		public void action() {
			//Example to retrieve the current position
			String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

			if (myPosition!=""){
				//List of observable from the agent's current position
				List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition

				try {
//					System.in.read();
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
				
				if(!G.containsVertex(myPosition))
					G.addVertex(myPosition, lobs.get(0).getRight());
				
				ouverts.remove(myPosition);
				fermes.add(myPosition);
				
				List<Couple<String, List<Attribute>>> adjacents = lobs;
				Couple<String, List<Attribute>> curr_observation = adjacents.remove(0);
				G.updateNode(myPosition, curr_observation.getRight());                      //MaJ des informations sur les noeuds
				List<String> adj_names = m_a_j_graphe(myPosition, adjacents);
				List<String> voisins_ouverts = get_open_neighbors(adj_names);
				
				if(this.ouverts.isEmpty()){
					((AK_Agent)myAgent).exploration_is_done(); //Avertir quil a fini l'exploration, elle sera connu des autres agents
					G.clearFermes();       //Repeter l'operation d'exploration
					G.addAllOuverts(myPosition);
//					this.finished = true;
					System.out.println(myAgent.getLocalName()+" : Exploration DONE ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
					((AK_Agent)myAgent).RAZCpt();
					voisins_ouverts = get_open_neighbors(adj_names);
				}
				
				String next_pos = getNextPosition(voisins_ouverts);
				if(next_pos.equals("")) {
					G.clearFermes();
					G.addAllOuverts(myPosition);
					System.out.println(myAgent.getLocalName()+" : I passed in the wosrt case cuz "+next_pos);
					next_pos = getNextPosition(voisins_ouverts);
				}
				
				boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
				
				if (has_moved){
					this.finished=false;
					((AK_Agent)myAgent).setCollisionNode("toto");
					((AK_Agent)myAgent).setNombreDeCollision(0);
					((AK_Agent)myAgent).CptPlus();
//					System.out.println(myAgent.getLocalName()+" : Deplace vers "+next_pos);
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
						default:
							break;
						}
					}
				
					//Dans le cas ou on echange les Ouverts, fermes
					ouverts.remove(next_pos);
					fermes.add(next_pos);
				
					//Si premiere collision, envoie un message d'information
					if(nb_collision==1 && !golem) {
						this.finished=true;
						((AK_Agent)myAgent).setCollisionNode(next_pos);
					}
//					else if (nb_collision ==2 && !golem)
//						((AK_Agent)myAgent).setCollisionNode(next_pos);

					else if (nb_collision == 2 && !golem){//check s'il a bien lu le msg recu par l'agent collision
						this.finished=true;
						this.onEndValue = 1;    
					}

					

//					Random r = new Random();
//					if (r.nextFloat() < nb_collision/10.){
//						G.clearFermes();
//						System.out.println(myAgent.getLocalName()+" Je vide tout apres "+nb_collision+" collisions");
//						((AK_Agent)myAgent).setNombreDeCollision(0);
	//
//					}
//					if(r.nextFloat() < nb_collision/3.){
//						this.fermes.add(next_pos);
//						this.ouverts.remove(next_pos);
//						System.out.println(myAgent.getLocalName()+" Je retire un noeud  apres "+nb_collision+" collisions");
//						((AK_Agent)myAgent).setNombreDeCollision(0);
//					}
				}
				

				

//				if (((AK_Agent)myAgent).getNombreDeCollision() == 5 && (!this.ouverts.isEmpty())){ //N'a qu'un seul noeud ouvert a explorer
//						G.clearFermes();
//						this.fermes.add(next_pos);
//						this.fermes.add(myPosition);
//		//				this.onEndValue=1;
//						((AK_Agent)myAgent).setNombreDeCollision(0);
//						System.out.println("COLLISIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooN");
//				}
			}
		}

		
		public boolean done() {
			return this.finished;
		}
		
		 // le Behavior s'arrête
	    public int onEnd() {
	      return this.onEndValue;
	    } 
		
	}