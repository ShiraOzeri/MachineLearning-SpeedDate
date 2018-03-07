package speedDate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import speedDate.corr.Result;
import speedDate.parameters.Date;

public class matchpartner {
	static int t_p = 0;
	static int r_p = 0;
	static int c_p = 0;


	public static double[][] find_partners(double[][] sample, int x) {
		int count = 0;
		int low = x;
		int high = x;
		while (high < sample.length - 1
				&& sample[high + 1][Date.valueOf("iid").ordinal()] == sample[high][Date.valueOf("iid").ordinal()]
				&& sample[high + 1][Date.valueOf("id").ordinal()] == sample[high][Date.valueOf("id").ordinal()]) {
			high++;
		}
		double[][] partners = new double[high - low + 1][sample[0].length]; // [(int)
																			// sample[x][Date.valueOf("round").ordinal()]][sample.length];
		for (int i = 0; i < partners.length; i++) {
			int p = indexOf(sample, (int) sample[x + i][(int) Date.valueOf("pid").ordinal()]);
			while (sample[p][Date.valueOf("pid").ordinal()] != sample[i + x][Date.valueOf("iid").ordinal()]) {
				p++;
			}
			partners[i] = sample[p];
		}
		return partners;

	}

	public static int indexOf(double[][] a, int i) {
		int lo = 0;
		int hi = a.length - 1;
		while (lo <= hi) {
			// Key is in a[lo..hi] or not present.
			int mid = lo + (hi - lo) / 2;
			if (i < a[mid][Date.valueOf("iid").ordinal()])
				hi = mid - 1;
			else if (i > a[mid][Date.valueOf("iid").ordinal()])
				lo = mid + 1;
			else {
				if (mid == 0) {
					return mid;
				}
				while (mid != 0 && a[mid - 1][Date.valueOf("iid").ordinal()] == a[mid][Date.valueOf("iid").ordinal()]) {
					mid--;
				}
				return mid;
			}
		}
		return -1;
	}

	public static void knn_p(double[][] sample) {
		int right = 0;
		int all = 0;
		for (int i = 0; i < sample.length; i++) {
			double[][] partner = find_partners(sample, i);
			all += partner.length;
			right += dist2partner(partner, sample, i,partner.length/5);
			i = i + partner.length - 1;

		}
		System.out.println("all: "+all+" right: "+right );
		System.out.println("recall:"+t_p+" "+r_p+" "+((double) t_p / r_p) * 100 + "%");
		System.out.println("precision:"+t_p+" "+c_p+" "+((double) t_p / c_p) * 100 + "%");

		System.out.println("The accuracy is " + ((double) right / all) * 100 + "%");

	}

	public static int dist2partner(double[][] partner, double[][] sample, int x, int k) {
		double dist = 0.0;
		List<Result> resultList = new ArrayList<Result>();

		for (int i = 0; i < partner.length; i++) {
			double[] query = sample[x + i];
			// find disnaces
			dist = 0;
			for (int j = 0; j < partner[0].length - 1; j++) {
				if (j != Date.valueOf("iid").ordinal() && j != Date.valueOf("id").ordinal()
						&& j != Date.valueOf("idg").ordinal() && j != Date.valueOf("condtn").ordinal()
						&& j != Date.valueOf("round").ordinal() && j != Date.valueOf("position").ordinal()
						&& j != Date.valueOf("order").ordinal() && j != Date.valueOf("partner").ordinal()
						&& j != Date.valueOf("pid").ordinal() && j != Date.valueOf("match").ordinal()
						&& j != Date.valueOf("wave").ordinal()) {
					dist += Math.abs(partner[i][j] - query[j]);
					//dist += Math.pow(partner[i][j] - query[j], 2);
				}

			}

			double distance = Math.sqrt(dist);
			resultList.add(new Result(partner[i][Date.valueOf("iid").ordinal()],
					partner[i][Date.valueOf("id").ordinal()], distance, partner[i][Date.valueOf("match").ordinal()]));
		}
		Collections.sort(resultList, new DistanceComparator());

		int right = 0;
		int i = 0;
		int yes=0;
		int no=0;
		for (Result result : resultList) {
			if ((i < k && result.match == 1) || (i >= k && result.match == 0)) {
				right++;
			}
			if (i < k && result.match == 1) {
				t_p++;
			}
			if (result.match == 1){
				yes++;
				r_p++;
			}
			if(i<k){
				c_p++;
			}
			if (result.match == 0)
				no++;
			i++;

		}
		System.out.println(x + " "+right+" "+yes+" "+no+" " + resultList.toString());

		return right;

	}

	public static void main(String[] args) {
		double[][] sample = ReadEditCSV.csv2matrix_nocaple("Speed Dating Data-before normal full1.csv");
		knn_p(sample);

	}

	static class Result {
		double pid;
		double partner;
		double distance;
		double match;

		public Result(double pid, double partner, double distance, double match) {
			this.match = match;
			this.distance = distance;
			this.pid = pid;
			this.partner = partner;
		}

		public String toString() {
			// TODO Auto-generated method stub
			return ("[" + pid + ", " + partner + "," + distance + "," + match + "]");
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
