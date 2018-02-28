package mas.agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.WalkBehaviour;

public class AK_Agent extends abstractAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475387134737498221L;
	
	
	
	
	protected void setup(){

		super.setup();

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorer");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try{
			DFService.register(this,dfd);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null){

			deployAgent((Environment) args[0]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		
		//Add the behaviours
		addBehaviour(new WalkBehaviour(this));
//		addBehaviour(new SayHello(this));                                                        

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}

}
