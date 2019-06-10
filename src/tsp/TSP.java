package tsp;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

//import com.sun.xml.internal.ws.message.MimeAttachmentSet;

public class TSP {

	public static final int A = 1;
	public static final int B = 2;
	public static final int C = 3;
	public static final int D = 4;


	private double[][] graphCSV;
	private ArrayList<Integer> simple;
	private ArrayList<Integer> cities;
	private ArrayList<String[]> data_col;
	private ArrayList<int[]> routesCSV;
	
	
	private static ArrayList<Integer> optimalRoute;
	private static double minCost;
	
	private final double[][] graph = {

			{0.0,20.0,42.0,35.0},
			{20.0,0.0,30.0,34.0},
			{42.0,30.0,0.0,12.0},
			{35.0,34.0,12.0,0.0}            
	}; 
	
	
	public TSP() {
		
		
		ArrayList<Integer> simple = new ArrayList<Integer>();
		simple.add(1);
		simple.add(2);
		simple.add(3);
		simple.add(4);

		randomSearch(simple,10);

//		randomSearch(this.getCities(),10);
//		reset();
//		twoOptSwapRnd(this.getCities(),10);
		


	}
	
	
	public void reset() {
		optimalRoute = null;
		minCost = 0.0;
		
	}

	
	public double[][] getCSVGraph(){
		return this.graphCSV;
	}
	
	public double[][] getGraph(){
		return this.graph;
	}
	
	public ArrayList<Integer> getCities(){
		return this.cities;
	}
	
	public ArrayList<Integer> getSimple(){
		return this.simple;
	}
	
	public ArrayList<Integer> getOptimalRoute(){
		return optimalRoute;
	}
	
	public double getMinCost(){
		return minCost;
	}
	
	public void setMinCost(double minCost) {
		this.minCost = minCost;
	}
	
	public void setOptimalRoute(ArrayList<Integer> optimalRoute) {
		this.optimalRoute = optimalRoute;
	}
	
	
	
	

	public double calcDistance(double xa, double xb, double ya, double yb) {

		double distance = Math.sqrt(Math.pow(xb-xa,2)+Math.pow(yb-ya,2));
		return distance;
	}
	
	
	public void checkDistance(ArrayList<Integer> route) {
		
			int cost = 0;

			int start = route.get(0);
			int end = route.get(route.size()-1);

			
			for(int i=0;i<route.size()-1 ;i++) {
				
				int current = route.get(i);
//				System.out.println(current);
				int next = route.get(i+1);
//				System.out.println(next);


				cost += graph[current-1][next-1];
//				System.out.println(cost);
			}
//			System.out.println(end);
//
//			System.out.println(start);
			cost += graph[end-1][start-1];
//			System.out.println(cost);


		
	}
	
	
	
//	//Generating permutation using Heap Algorithm 
//    static void heapAlgorithm(int[] a, int n) 
//    { 
//    	
//    		// if size becomes 1 then prints the obtained 
//            // permutation 
//            if (n == 1) {
//                	checkDistance(a);
//            }
//      
//            for (int i=0; i<n; i++) 
//            { 
//            	
//            		heapAlgorithm(a, n-1); 
//            	      
//                    // if n is odd, swap first and last 
//                    // element 
//                    if (n % 2 == 1) 
//                    { 
//                        int temp = a[0]; 
//                        a[0] = a[n-1]; 
//                        a[n-1] = temp; 
//                    } 
//          
//                    // If n is even, swap ith and last 
//                    // element 
//                    else
//                    { 
//                        int temp = a[i]; 
//                        a[i] = a[n-1]; 
//                        a[n-1] = temp; 
//                    } 
//            	
//            } 
//    
//    } 
    
    public void randomSearch(ArrayList<Integer> cities, int duration) {
    	
    	long start= System.currentTimeMillis();
    	long end = start+(duration*1000);
    	
    	while(System.currentTimeMillis() < end) {
    		   		
    		Collections.shuffle(cities);
    		checkDistance(cities);
    		
    	}
    	

    	
    }
    
    public void twoOptSwapRnd(ArrayList<Integer> route, int duration) {
    	
    	ArrayList<Integer> newRoute = new ArrayList<Integer>();

		for ( int i = 0; i < route.size()-1; i++ ){
            for ( int j = i+1; j < route.size(); j++){
            	
            	newRoute = twoOptSwap( i, j, route);

        		checkDistance(newRoute);
 
            }
        }
    		
		
    	
	    System.out.println("Shortest route for 2-OPT is: " + getOptimalRoute().toString() + " at cost: " + getMinCost());


    	
    	
    }
    
    public static ArrayList<Integer> twoOptSwap(int a, int b, ArrayList<Integer> route) {
    	    	
    	ArrayList<Integer> newRoute = new ArrayList<Integer>();
    	
    	
    	int temp = route.get(a);

    	route.set(a, route.get(b));
    	route.set(b, temp);
    	
    	
    	
       	return route;
    }
    
    
		

	public static void main(String[] args) throws FileNotFoundException {
		

		TSP tsp = new TSP();
		
		tsp.populateGraph("ulysses16.csv");
		
		tsp.randomSearch(tsp.getSimple(), 10);
		
		
	    System.out.println("Shortest route for RANDOM is: " + tsp.getOptimalRoute().toString() + " at cost: " + tsp.getMinCost());

		
		
		
//		for (int i = 0; i < tsp.getCSVGraph().length; i++) {
//		    for (int j = 0; j < tsp.getCSVGraph()[i].length; j++)
//		        System.out.print(tsp.getCSVGraph()[i][j] + " ");
//	    System.out.println();
//		}
		
		int stops[] = {A,B,C,D}; 
//	    heapAlgorithm(stops, stops.length);		
//	    
//	    System.out.println("Shortest route is: " + Arrays.toString(optimalRoute) + " at cost: " + minCost);
		

		
		
		
		

		
//		ArrayList<Integer> route = new ArrayList<Integer>();
//		route.add(1); 
//		route.add(2); 
//		route.add(3); 
//		route.add(4); 

//		twoOptSwapRnd(cities, 10);

		
    	
//	    heapAlgorithm(cities, cities.length);		
//
//	    System.out.println("Shortest route is: " + Arrays.toString(optimalRoute) + " at cost: " + minCost);
//	    

	}



	public void populateGraph(String filename) throws FileNotFoundException {
		
		Scanner scanner = new Scanner(new File(filename));
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();

		this.data_col = new ArrayList<String[]>();

	    while (scanner.hasNext())
		{
			String[] data = scanner.nextLine().split(",");
			data_col.add(data);
		}

		this.graphCSV = new double[data_col.size()][data_col.size()];
		this.cities = new ArrayList<Integer>();

		for(int i = 0; i < data_col.size(); i++) {

			String[] city = data_col.get(i);
			cities.add(Integer.valueOf(city[0]));
			for(int j = 0; j < data_col.size(); j++) {
				
				
				String[] city2 = data_col.get(j);
				if(i != j) {
					graphCSV[i][j] = calcDistance(Double.valueOf(city[1]), Double.valueOf(city2[1]), Double.valueOf(city[2]), Double.valueOf(city2[2]));
				}else {
					graphCSV[i][j] = 0;
				}
			}
		}		
	}
}
