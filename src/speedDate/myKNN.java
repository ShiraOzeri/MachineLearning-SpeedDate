package speedDate;

import java.io.IOException;

/**
 * To find k nearest neighbors of a new instance
 * Please watch my explanation of how KNN works: xxx
 *   - For classification it uses majority vote
 *   - For regression it finds the mean (average)  
 *  
 * Copyright (C) 2014 
 * @author Dr Noureddin Sadawi 
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ONLY for legal and ethical purposes
 * 
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and my YouTube Channel!
 *  
 */

import java.util.*;

class myKNN {

	
	/**
	 * option 2 to this class: findMajorityClass
	 * @param ss
	 * @return
	 */
	private static String findMajorityClass1(String[] ss) {
		int countY = 0;
		int countN = 0;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].equals("YES")) {
				countY++;
			} else
				countN++;

		}
		return countN > countY ? "NO" : countN < countY ? "YES" : ss[0];
	}

	/*
	 * distance L2
	 */
	public static void disL2() throws IOException {
		int q1 = 0;
		int anstruemaj1 = 0;
		int anstruemaj = 0;
		double[] query = null;
		String majClass = null;
		String majClass1 = null;
		int maj = 0;
		int maj1 = 0;
		int colum = 55;
		int ans = 54;
		int row = 2795; // 7372;
		int learn = 2000;// 6372;
		int k = 10;// # of neighbours
		// list to save city data
		List<Person> PesonList = new ArrayList<Person>();
		// list to save distance result
		// List<Result> resultList = new ArrayList<Result>();
		// add city data to cityList

		// the data
//		double[][] instances = ReadCSV.readFile(
//				"C:/Users/Roey/Documents/לימודים/מדעי המחשב/שנה ג סמסטר ב/למידת מכונה/Speed Dating Data half.csv", row,
//				colum);
		double[][] instances=ReadEditCSV.getMatrix();

		for (int i = 0; i < learn; i++) {
			PesonList.add(new Person(instances[i], instances[i][ans] == 0.0 ? "NO" : "YES"));
		}

		for (int i = learn; i < instances.length; i++) {

			// data about unknown city
			query = instances[i];
			System.out.println("i: " + i + " " + Arrays.toString(query));
			// find disnaces
			List<Result> resultList = new ArrayList<Result>();

			for (Person p : PesonList) {
				double dist = 0.0;
				for (int j = 0; j < p.PersonAttributes.length; j++) {
					if (j != 0 && j != 10)
						dist += Math.pow(p.PersonAttributes[j] - query[j], 2);
					// System.out.print(city.cityAttributes[j]+" ");
				}
				double distance = Math.sqrt(dist);
				resultList.add(new Result(distance, p.match));
				// System.out.println(distance);
			}

			// System.out.println(resultList);
			Collections.sort(resultList, new DistanceComparator());
			String[] ss = new String[k];
			for (int x = 0; x < k; x++) {
				System.out.println(resultList.get(x).match + " .... " + resultList.get(x).distance);
				// get classes of k nearest instances (city names) from the list
				// into an array
				ss[x] = resultList.get(x).match;

			}
			majClass1 = findMajorityClass1(ss);

			if ((query[ans] == 0 && majClass.equals("NO")) || (query[ans] == 1 && majClass.equals("YES"))) {
				maj++;
			}
			if ((query[ans] == 0 && majClass1.equals("NO")) || (query[ans] == 1 && majClass1.equals("YES"))) {
				maj1++;
			}
			if (query[ans] == 1) {
				q1++;
				if (majClass.equals("YES")) {
					anstruemaj++;
				}
				if (majClass1.equals("YES")) {
					anstruemaj1++;
				}
			}
			System.out.println("maj " + majClass + " maj1 " + majClass1 + " ans: " + query[ans]);

			// System.out.println("Class of new instance is: " + majClass);
			// System.out.println("try " + majClass1);
			// System.out.println("and the real ans is " + query[ans]);

		}
		System.out.println("maj " + maj + " maj1 " + maj1 + " q1: " + q1 + " anstruemaj " + anstruemaj + " anstruemaj1 "
				+ anstruemaj1);

		// System.out.println("maj " +maj+" maj1 "+maj1);
		// end disL2

	}

	/**
	 * distance L1
	 * @throws IOException
	 */
	public static void disL1(double[][]learn, double[][]test,int k ) throws IOException {
		int qyes=0;
		int qno=0;
		double[] query = null;
		String majClass1 = null;
		int maj1 = 0;
		int ans = learn[0].length-1;
		List<Person> PesonList = new ArrayList<Person>();

		for (int i = 0; i < learn.length; i++) {
			PesonList.add(new Person(learn[i], learn[i][ans] == 0.0 ? "NO" : "YES"));
		}

		for (int i = 0; i < test.length; i++) {
			query = test[i];
			// find disnaces
			List<Result> resultList = new ArrayList<Result>();
			for (Person p : PesonList) {
				double dist = 0.0;
				for (int j = 0; j < p.PersonAttributes.length-1; j++) {
						dist += Math.abs(p.PersonAttributes[j] - query[j]);
				}
				double distance = Math.sqrt(dist);
				resultList.add(new Result(distance, p.match));
			}

			Collections.sort(resultList, new DistanceComparator());
			String[] ss = new String[k];
			for (int x = 0; x < k; x++) {
				//System.out.println("match:"+query[ans]+"k="+x+": "+ resultList.get(x).match + " .... " + resultList.get(x).distance);

				ss[x] = resultList.get(x).match;
				

			}
			majClass1 = findMajorityClass1(ss);

			if ((query[ans] == 0 && majClass1.equals("NO")) || (query[ans] == 1 && majClass1.equals("YES"))) {
				maj1++;
			}
			
			if(query[ans]==0){
				qno++;
			}
			else if (query[ans]==1)
				qyes++;


		}
		System.out.println("Knn result with K = "+k);
		System.out.println("The accuracy is "+((double)maj1 / test.length)*100+"%");
		System.out.println("qyes:"+qyes+" qno"+qno);


	}
	
	


	public static void main(String args[]) throws IOException {

		
		// disL2();
		//disL1();
	}

	// simple class to model instances (features + class)
	static class Person {
		double[] PersonAttributes;
		String match;

		public Person(double[] cityAttributes, String instances) {
			this.match = instances;
			this.PersonAttributes = cityAttributes;
		}
	}

	// simple class to model results (distance + class)
	static class Result {
		double distance;
		String match;

		public Result(double distance, String match) {
			this.match = match;
			this.distance = distance;
		}
	}

	// simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}

}