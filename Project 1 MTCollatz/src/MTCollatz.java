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
	private int stopTime;
	
	@Override
	public void run(){
		
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
		
		this.stopTime = getCollatzStoppingTime(this.n);
		
		MTCollatz.histData[this.n - 2] = this.stopTime;
		MTCollatz.kFreq[this.stopTime - 1]++;

	}
	
	public int getCollatzStoppingTime(int n) {
		
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
    static final int HIST_SIZE = 1000000;
	public static int COUNTER = 2;
	public static int NCollatz;
	public static int[] kFreq = new int[1000];
	public static long[] histData = new long[HIST_SIZE];
	public static ReentrantLock mutex = new ReentrantLock();
	
		
	public static void main(String[] args) {
		int numberThreads;
		Scanner scnr = new Scanner(System.in);
		CollatzCompute[] threads = new CollatzCompute[30];
		
		NCollatz = scnr.nextInt();
		numberThreads = scnr.nextInt();
		
		
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
					e.printStackTrace();
				}
			}	
		}

		
		Instant endTime = Instant.now();
		Duration processTime = Duration.between(startTime, endTime);
		
		for(int l = 0; l < kFreq.length; l++) {
			System.out.println(kFreq[l]);
		}
		
		
		System.out.println(processTime.toSeconds());
		

	}
}
