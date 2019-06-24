package utils;

public class Utils {

	public Utils(){}
	
	public static int getNumEdges(int v){
		int ne = 0;
		for(int i = v-1; i > 0; i--)
			ne += i;
		
		return ne;
	}
	
}
