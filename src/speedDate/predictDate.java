package speedDate;

import Jama.Matrix;
import smile.math.kernel.GaussianKernel;
import smile.classification.SVM;
import smile.classification.GradientTreeBoost;
import smile.classification.AdaBoost;
import smile.classification.DecisionTree;
import smile.classification.FLD;
import smile.classification.Maxent;
import smile.data.NumericAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class predictDate {

	public static double[][] getSamples(double[][] pertner, int size) {
		int match = 0, no_match = 0;
		double[][] ans = new double[size * 2][pertner[0].length];
		int j = 0;
		for (int i = 0; i < pertner.length; i++) {
			if (j == size * 2) {
				break;
			}
			if ((pertner[i][pertner[0].length - 1] == 1 && no_match < size)
					|| (pertner[i][pertner[0].length - 1] == 0 && match < size)) {
				if (pertner[i][pertner[0].length - 1] == 1)
					no_match++;
				if (pertner[i][pertner[0].length - 1] == 0)
					match++;
				ans[j] = pertner[i];
				j++;
			}
		}
		return ans;
	}

	private static void learn_test(double[][] pertner, double[][] learn, double[][] test, int size_learn,
			int size_test,int add_no) {
		int match_l = 0, no_match_l = 0;
		int match_t = 0, no_match_t = 0;
		//
		// boolean first=true;
		// double[][] ans = new double[size_learn * 2][pertner[0].length];
		int j_l = 0, j_t = 0;
		
		for (int i = 0; i < pertner.length; i++) {
			if (j_l-add_no == size_learn * 2 && j_t-add_no == size_test * 2) {
				break;

			} else if ((j_t-add_no < size_test * 2) && ((pertner[i][pertner[0].length - 1] == 0 && no_match_t-add_no < size_test)
					|| (pertner[i][pertner[0].length - 1] == 1 && match_t < size_test))) {
				if (pertner[i][pertner[0].length - 1] == 0)
					no_match_t++;
				if (pertner[i][pertner[0].length - 1] == 1)
					match_t++;
				test[j_t] = pertner[i];
				j_t++;
			} else if ((j_l-add_no < size_learn * 2) && ((pertner[i][pertner[0].length - 1] == 0 && no_match_l-add_no < size_learn)
					|| (pertner[i][pertner[0].length - 1] == 1 && match_l < size_learn))) {
				if (pertner[i][pertner[0].length - 1] == 0)
					no_match_l++;
				if (pertner[i][pertner[0].length - 1] == 1)
					match_t++;
				learn[j_l] = pertner[i];
				j_l++;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// the matrix contains the employees data, the last column is the label
		// - 0/1 == stayed/left
		double[][] mat = ReadEditCSV.getMatrix();
		Matrix allData = new Matrix(new double[mat.length][mat[0].length]);
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				allData.set(i, j, mat[i][j]);
			}
		}
		printToFile(mat, "date_all.txt");

		int size_learn = 500;
		int size_test = 165;
		double[][] sample = getSamples(mat, size_learn);
		int add_no=0;

		double[][] test = new double[size_test * 2+add_no][mat[0].length];
		double[][] learn = new double[size_learn * 2+add_no][mat[0].length];
		learn_test(mat, learn, test, size_learn, size_test,add_no);
		printToFile(test, "date_test.txt");
		printToFile(learn, "date_learn.txt");

		Matrix trainingData = new Matrix(new double[sample.length][sample[0].length]);
		for (int i = 0; i < sample.length; i++) {
			for (int j = 0; j < sample[0].length; j++) {
				trainingData.set(i, j, sample[i][j]);
			}
		}

		printToFile(sample, "date_train.txt");

		myKNN.disL1(learn, test, 2);
		myKNN.disL1(learn, test, 5);
		myKNN.disL1(learn, test, 10);
		myKNN.disL1(learn, test, 20);
		myKNN.disL1(learn, test, 50);
		myKNN.disL1(learn, test, 100);
		myKNN.disL2(learn, test, 2);
		myKNN.disL2(learn, test, 5);
		myKNN.disL2(learn, test, 10);
		myKNN.disL2(learn, test, 20);
		myKNN.disL2(learn, test, 50);
		myKNN.disL2(learn, test, 100);

		double[][] x = new double[learn.length][learn[0].length - 1];
		for (int i = 0; i < learn.length; i++) {
			for (int j = 0; j < learn[0].length - 1; j++) {
				x[i][j] = learn[i][j];
			}
		}
		int[] y = new int[learn.length];
		for (int j = 0; j < learn.length; j++) {
			if ((int) learn[j][learn[0].length - 1] == 0)
				y[j] = 1;
			else
				y[j] = 2;
		}
		double[][] testx = new double[test.length][test[0].length - 1];
		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < test[0].length - 1; j++) {
				testx[i][j] = test[i][j];
			}
		}
		int[] testy = new int[test.length];
		for (int j = 0; j < test.length; j++) {
			if ((int) test[j][test[0].length - 1] == 0)
				testy[j] = 1;
			else
				testy[j] = 2;
		}

		SVM<double[]> svm = new SVM<double[]>(new GaussianKernel(8.0), 5.0, 3, SVM.Multiclass.ONE_VS_ONE);
		svm.learn(x, y);
		svm.finish();

		int error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (svm.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("SVM Algorithm");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
		//svm2
	    for (int i = 0; i < x.length; i++) {
	        //int j = Math.randomInt(x.length);
	        int j=(int)((Math.random()*(x.length)));
	        svm.learn(x[j], y[j]);
	    }
	            
	    svm.finish();

	    error = 0;
	    for (int i = 0; i < testx.length; i++) {
	        if (svm.predict(testx[i]) != testy[i]) {
	            error++;
	        }
	    }
		System.out.println("SVM2 Algorithm");

		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
	    //end svm2
		
		
		
		for (int j = 0; j < learn.length; j++) {
			if (y[j] == 1)
				y[j] = 0;
			else
				y[j] = 1;
		}
		AdaBoost forest = new AdaBoost(null, x, y, 100);
		for (int j = 0; j < testx.length; j++) {
			if (testy[j] == 1)
				testy[j] = 0;
			else
				testy[j] = 1;
		}
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (forest.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("ADA-BOOST Algorithm 100");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();

		forest = new AdaBoost(null, x, y, 500);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (forest.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("ADA-BOOST Algorithm 500");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
		
		forest = new AdaBoost(x, y, 200);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (forest.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("ADA-BOOST Algorithm 200");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
		
		
		forest = new AdaBoost(null, x, y, 200, 4);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (forest.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("ADA-BOOST Algorithm 200,4");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
		
		DecisionTree dtree=new DecisionTree(x, y, 100);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (dtree.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("DecisionTree Algorithm 100");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
		
		/*
		FLD fld=new FLD(x, y, -1);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (fld.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("FLD Algorithm 100");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();


		Maxent maxent=new Maxent(10, x, y);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (maxent.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("FLD Algorithm 100");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();
	*/	
		int []double_y=new int[learn.length];
		for (int j = 0; j < learn.length; j++) {
			if (double_y[j] == 1)
				y[j] = 0;
			else
				y[j] = 1;
		}
		
		GradientTreeBoost gtb=new GradientTreeBoost(x,y,10);
		error = 0;
		for (int i = 0; i < testx.length; i++) {
			if (gtb.predict(testx[i]) != testy[i]) {
				error++;
			}
		}
		System.out.println("gtb Algorithm 100");
		System.out.format("The accuracy is %.2f%%\n", 100.0 * (testx.length - error) / testx.length);
		System.out.println();

	}

	private static void printToFile(double[][] mat, String s) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			int numOfCols = mat[0].length - 1;
			writer.println(mat.length + " " + numOfCols + " 1");
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[0].length; j++) {
					writer.print(mat[i][j] + " ");
				}
				writer.print("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
