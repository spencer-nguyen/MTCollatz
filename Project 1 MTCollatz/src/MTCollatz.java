import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

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


class CollatzCompute implements Runnable{
	
	private int stopTime;
	
	@Override
	public void run(){
		MTCollatz.mutex.lock();
		try {
			stopTime = getCollatzStoppingTime(readAndIncrement());
		}
		finally {
			MTCollatz.mutex.unlock();
		}
		
		
	}
	
	private int readAndIncrement() {
			
		int temp = MTCollatz.COUNTER;
		MTCollatz.COUNTER++;
		
		return temp;
		
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
	public static int NCollatz;
	public static int[] histData;
	public static ReentrantLock mutex = new ReentrantLock();

		
	public static void main(String[] args) {
		
		int numberThreads;
		int NCollatz;
		Scanner scnr = new Scanner(System.in);
		ArrayList<CollatzCompute> threads = new ArrayList <CollatzCompute>();
		
		numberThreads = scnr.nextInt();
		NCollatz = scnr.nextInt();
		
		while(COUNTER < NCollatz) {
			
			for(int i = 0; i < numberThreads; i++) {
				CollatzCompute singleThread = new CollatzCompute();
				threads.add(i, singleThread);
			}	
			
			for(int j = 0; j < numberThreads; j++) {
				if(COUNTER < NCollatz) {
					threads.get(j).run();
				}
			}
		}
	}
}
