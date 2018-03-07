package speedDate;

import java.util.Arrays;

import speedDate.parameters.Date;
import speedDate.parameters.Order;

public class order_by_dec {

	public static double[] order(double[][] sample){
		int n=(int)max(sample);
		System.out.println(n);
		int [] dec=new int[n+1];
		int [] all=new int[n+1];
		for (int i = 0; i < sample.length; i++) {
			for (int j = 0; j < sample[0].length; j++) {
				all[(int) sample[i][Order.valueOf("order").ordinal()]]++;
				if(sample[i][Order.valueOf("dec").ordinal()]==1){
					dec[(int) sample[i][Order.valueOf("order").ordinal()]]++;
				}
				
			}
			
		}
		System.out.println(Arrays.toString(dec));
		System.out.println(Arrays.toString(all));


		double []p=new double[n+1];
		for (int i = 1; i < p.length; i++) {
			p[i]=(double)dec[i]/(double)all[i];
		}
		return p;
	}
	
	public static double max(double[][] sample){
		double max=0;
		for (int i = 0; i < sample.length; i++) {
			if(max<sample[i][Order.valueOf("order").ordinal()]){
				max=sample[i][Order.valueOf("order").ordinal()];
			}
		}
		return max;
	}
	
	public static void main(String[] args) {
		double[][] sample = ReadEditCSV.csv2matrix_nocaple("order_dec.csv");
		double[] p=order(sample);
		System.out.println(Arrays.toString(p));

	}

	
}
