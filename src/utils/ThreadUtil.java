package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池执行
 * @author xubing
 *
 */
public class ThreadUtil {
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public static void executeThread(Runnable runnable) {
		threadPool.execute(runnable);
	}
	
}
