import java.util.Scanner;

/**
 * Class: COP5518 Computing Essentials
 * @author Spencer Nguyen
 *
 * Project: Multi-Threaded Collatz Stopping Time Generator
 * Description: This program makes use of multiple threads to determine the stopping time of a Collatz
 * 				sequences ranging from 1 to N. The program accepts the following arguments:
 *              N: The out of bound of the range 1 to N of positive integers
 *              T: The number of threads to be used to compute the stopping times of each n
 *              
 *              
 */

class collatzCompute extends Thread{
	
	collatzCompute(int n){
		
		
		
		
		
		
		
		
	}
	
	public int getCollatzStoppingTime(int n) {
		
		int stoppingTime = 0; //counter for stopping time
		
		/* Keep looping through formulas until k = 1. 
		 * Count each iteration towards the stopping time. 
		 */
		while(n != 1) {
			
			if(n % 2 == 0) {
				n = n / 2; 
			}
			else {
				n = 3 * n + 1;
			}
			
			stoppingTime++;
		}
		
		return stoppingTime;
	}
}


public class MTCollatz {	
	
	public static int COUNTER = 2;
	public static int[] histData = new int [30];
		
	public static void main(String[] args) {
		
		

	}

}
