import java.time.Duration;
import java.time.Instant;
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
	
	private int n;       // thread to compute Collatz sequence for n
	private int stopTime;// stopping time of Collatz sequence
	
	@Override
	public void run(){
		/*----------------------------------------------------------------------------*/
		/* This portion of the thread will access the counter that determines the next
		 * sequence to compute. Therefore it requires locking when pulling the current
		 * number and incrementing.*/
		
		MTCollatz.mutex.lock();
		try {
			if(MTCollatz.COUNTER <= MTCollatz.NCollatz) {
				this.n = MTCollatz.COUNTER;
				MTCollatz.COUNTER++;
			}
			else {
				return;
			}
		}
		finally {
			MTCollatz.mutex.unlock();
		}
		
		/*----------------------------------------------------------------------------*/
		/* Portion of thread to compute concurrently. */
		
		this.stopTime = getCollatzStoppingTime(this.n);
		
		/*----------------------------------------------------------------------------*/
		/* This portion increments the frequency of stopping times calculated for 1 -1000.*/
		
		MTCollatz.mutex.lock();
		try {
			MTCollatz.kFreq[this.stopTime - 1]++;
		}
		finally {
			MTCollatz.mutex.unlock();
		}

	}
	
	/**
	 * This method takes the current number and computes the Collatz sequence stopping time.
	 * @param int n
	 * @return int stopTime
	 */
	public int getCollatzStoppingTime(int n) {
		
		int stoppingTime = 0;// counter for stopping time
		long temp = n;       // temp variable for Collatz sequence
		
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
	
	public static int COUNTER = 2;            // left bound of Collatz sequences to calculate
	public static int NCollatz;               // right bound of Collatz sequences to calculate
	public static int[] kFreq = new int[1000];// array to store frequency of stopping times 
	public static ReentrantLock mutex = new ReentrantLock();
	
		
	public static void main(String[] args) {
		
		int numberThreads;// number of working threads
		long timeSec;     // compute time in seconds
		double timeNano;  // compute time in nanoseconds
		
		Scanner scnr = new Scanner(System.in);
		CollatzCompute[] threads = new CollatzCompute[30];
		
		/* Get arguments from user.*/
		NCollatz = scnr.nextInt();
		numberThreads = scnr.nextInt();
		
		Instant startTime = Instant.now();// begin time stamp
		
		while(COUNTER <= NCollatz) {
			
			/* Make new threads to user specified number of worker threads. */
			for(int i = 0; i < numberThreads; i++) {
				threads[i] = new CollatzCompute();
			}	
			
			/* Start threads to begin computation. */
			for(int j = 0; j < numberThreads; j++) {
				threads[j].start();
			}
			
			/* Kill worker threads by joining to main. */
			for(int k = 0; k < numberThreads; k++) {
				try {
					threads[k].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}

		
		Instant endTime = Instant.now();                            // end time stamp
		Duration processTime = Duration.between(startTime, endTime);// get program duration
		
		timeSec = processTime.getSeconds();
		timeNano = processTime.getNano(); 
		
		/* Print stopping time frequency to stderr. */
		for(int l = 0; l < kFreq.length; l++) {
			System.err.println((l + 1) + "," + kFreq[l]);
		}
		
		/* Print right bound collatz, number of worker threads, and time elapsed to output. */
		System.out.println(NCollatz + "," + numberThreads + "," + timeSec + "." + (int)timeNano);
	}
}
