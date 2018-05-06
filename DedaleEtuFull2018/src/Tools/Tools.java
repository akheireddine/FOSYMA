package Tools;

import java.util.List;

import env.Attribute;
import env.Couple;

public class Tools {
	
	
	
	
	
	public static Couple<Float, Integer> getCoupleTreasurIndex(List<Attribute> lattribute){
		int i = 0;
		for(Attribute a:lattribute){
			switch (a) {
			case TREASURE : case DIAMONDS :
				i++;
				return new Couple<Float, Integer>(Float.parseFloat(a.getValue().toString()),i);

			default:
				return new Couple<Float, Integer>((float)(-1),-1);
			}
		}
		return new Couple<Float, Integer>((float)(-1),-1);
	}
	
	
	public static Float getValueTreasurDiamond(List<Attribute> lattribute){
		if (lattribute != null){
			for(Attribute a:lattribute){
			switch (a) {
			case TREASURE : case DIAMONDS :
				return Float.parseFloat(a.getValue().toString());

			default:
				return (float)(-1);
			}
			}
		}
		return (float)(100000);
	}
	

}
