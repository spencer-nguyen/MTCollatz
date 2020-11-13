import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Thread;

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


class CollatzCompute extends Thread{
	
	private int n;
	private long stopTime;
	
	@Override
	public void run(){
		
		MTCollatz.mutex.lock();
		try {
			if(MTCollatz.COUNTER <= MTCollatz.NCollatz) {
				this.n = MTCollatz.COUNTER;
				MTCollatz.COUNTER++;
			}
			else {
				this.n = 1;
			}
		}
		finally {
			MTCollatz.mutex.unlock();
		}
		
		
		this.stopTime = getCollatzStoppingTime(this.n);
		
		
	//	MTCollatz.mutex.lock();
	//	try {
	//	MTCollatz.histData[n - 2] = this.stopTime;
	//	}
	//	finally {
	//		MTCollatz.mutex.unlock();
	//	}
	}
	
	public long getCollatzStoppingTime(int n) {
		
		int stoppingTime = 0; //counter for stopping time
		long temp = n;
		
		/* Keep looping through formulas until k = 1. 
		 * Count each iteration towards the stopping time. 
		 */
		while(temp != 1) {
			
			if(temp % 2 == 0) {
				temp = temp / 2; 
			}
			else {
				temp = 3 * temp + 1;
			}
			
			stoppingTime++;
		}
		
		return stoppingTime;
	}
}


public class MTCollatz {	
    static final int HIST_SIZE = 50000000;
	public static int COUNTER = 2;
	public static int NCollatz;
	public static long[] histData = new long[HIST_SIZE];
	public static ReentrantLock mutex = new ReentrantLock();
	
		
	public static void main(String[] args) {
		int numberThreads;
		Scanner scnr = new Scanner(System.in);
		CollatzCompute[] threads = new CollatzCompute[30];
		
		
		numberThreads = scnr.nextInt();
		NCollatz = scnr.nextInt();
		

		
		Instant startTime = Instant.now();
		
		while(COUNTER <= NCollatz) {
			
			for(int i = 0; i < numberThreads; i++) {
				threads[i] = new CollatzCompute();
			}	
			
			for(int j = 0; j < numberThreads; j++) {
				threads[j].start();
			}
			
			for(int k = 0; k < numberThreads; k++) {
				try {
					threads[k].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		

			
		
		
		Instant endTime = Instant.now();
		Duration processTime = Duration.between(startTime, endTime);
		
		System.out.println(processTime.toSeconds());
		
		/*for(int k = 0; k < COUNTER - 2; k++) {
			
			System.out.println(histData[k]);
		}*/
		


	}
}
