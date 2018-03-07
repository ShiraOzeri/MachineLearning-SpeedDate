package speedDate;

import java.util.Arrays;

import speedDate.parameters.Character;
import speedDate.parameters.Order;

public class character {

	public static double character(double[][] sample,String attr){
		System.out.println(sample.length);
		double [] me=new double[sample.length];
		double [] partner=new double[sample.length];

		for (int i = 0; i < sample.length; i++) {
				me[i]=sample[i][Character.valueOf(attr+"3_1").ordinal()];
				partner[i]=sample[i][Character.valueOf(attr+"_o").ordinal()];			
		}


		double avr=0;
		double []p=new double[11];
		double []sum=new double[11];
		
		for (int i = 1; i < me.length; i++) {
			p[(int)me[i]]++;
			sum[(int)me[i]]+=partner[i];
			avr+=Math.abs(me[i]-partner[i]);
		}
		double []dis=new double[11];
		for (int i = 0; i < sum.length; i++) {
			dis[i]=sum[i]/p[i];
		}
		System.out.println(Arrays.toString(sum));
		System.out.println(Arrays.toString(p));
		System.out.println(Arrays.toString(dis));

		return avr/sample.length;
	}
	
	public static void main(String[] args) {
		double[][] sample = ReadEditCSV.csv2matrix_nocaple("character.csv");
		double p=character(sample,"amb");
		System.out.println(p);

	}
}
