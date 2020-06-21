package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileCacheManager<P, S> implements CacheManager<P, S> {
	LinkedHashMap<P, S> data;
	private int limit = 10;

	public FileCacheManager() {
		this.data = new LinkedHashMap<P, S>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4495459385327939802L;

			protected boolean removeEldestEntry(Map.Entry<P, S> eldest) { 	// Set the limit of Objects in LinkedHashMap
				return size() > limit; 										// if the size > limit, will remove the eldest object
			}
		};
	}

	@Override
	public boolean existSolution(P problem) {
		return this.data.containsKey(problem);
	}

	@Override
	public S loadSolution(P problem) {
		ObjectInputStream in;
		S solution = null;
		if (this.data.containsKey(problem)) {
			solution = this.data.get(problem);
		} else {
			File dir = new File("./" + String.valueOf(problem.hashCode()) + ".txt");
			if (dir.exists()) {
				try {
					in = new ObjectInputStream(new FileInputStream(String.valueOf(problem.hashCode()) + ".txt"));
					solution = (S) in.readObject();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
		return solution;
	}

	@Override
	public void store(P problem, S solution) {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(String.valueOf(problem.hashCode()) + ".txt")); // Create new file
			out.writeObject(solution);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};

		this.data.put(problem, solution);
	}
}
