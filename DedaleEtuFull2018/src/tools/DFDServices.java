package tools;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFDServices {

	
	public static AID[] getAgentsByService( String service,Agent agent ){
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        dfd.addServices(sd);
        try{
        	DFAgentDescription[] result = DFService.search(agent, dfd);
            if (result.length>0){
            	AID[] sellerAgents = new AID[result.length];
            	for(int i = 0; i < result.length; i++){
					if (!agent.getAID().equals(sellerAgents[i]))
						sellerAgents[i] = result[i].getName();
    			}
            	return sellerAgents;
            }
        }catch (FIPAException fe){
        	fe.printStackTrace(); 
        }
        return null;
    }
	
	public static boolean typeOfserviceAgent(AID sender,Agent a,String service){
		try{
			AID[] r = getAgentsByService(service, a);
			if( r == null)
				return false;
			for(int i = 0;i< r.length;i++){
				if(r[i].equals(sender))
					return true;
			}
		}catch(Exception e){
			System.out.println("ERROR");
		}
		return false;

	}	
	
	public static void registerToService(String service, Agent agent) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(agent.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		sd.setName(agent.getLocalName());
		dfd.addServices(sd);
		try{
			DFService.register(agent,dfd);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
	}
}


