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
public class MTCollatz {
	
	private int COUNTER = 2;
	
	public static int collatzStoppingTime(int k) {
		
		int stoppingTime = 0;
		
		while(k != 1) {
			
			if(k % 2 == 0) {
				k = k / 2; 
			}
			else {
				k = 3 * k + 1;
			}
			
			stoppingTime++;
		}
		
		return stoppingTime;
	}

	public static void main(String[] args) {
		
		Scanner scnr = new Scanner(System.in);
		
		int k = scnr.nextInt();
		
		System.out.println(collatzStoppingTime(k));

	}

}
