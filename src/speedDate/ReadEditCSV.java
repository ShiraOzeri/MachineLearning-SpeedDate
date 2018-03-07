package speedDate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import speedDate.parameters.Date;

public class ReadEditCSV {

	//read csv to matrix with attribute for caple
	public static double[][] csv2matrix(String s) {
		String fileName = s;
		File file = new File(fileName);
		List<List<String>> lines = new ArrayList<List<String>>();
		Scanner inputStream;

		try {
			inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String line = inputStream.next();
				String[] values = line.split(",");
				lines.add(Arrays.asList(values));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		double[][] matrix = new double[lines.size() - 1][(lines.remove(0).size()) + 1];
		int j = 0;
		for (List<String> line : lines) {
			for (int i = 0; i < line.size(); i++) { // iterate over the elements

				matrix[j][i] = Double.parseDouble(line.get(i));

			}
			j++;
		}

		return matrix;

	}

	//read csv to matrix basic
	public static double[][] csv2matrix_nocaple(String s) {
		String fileName = s;
		File file = new File(fileName);
		List<List<String>> lines = new ArrayList<List<String>>();
		Scanner inputStream;

		try {
			inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String line = inputStream.next();
				String[] values = line.split(",");
				lines.add(Arrays.asList(values));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		double[][] matrix = new double[lines.size() - 1][(lines.remove(0).size()) ];
		int j = 0;
		for (List<String> line : lines) {
			for (int i = 0; i < line.size(); i++) { // iterate over the elements

				matrix[j][i] = Double.parseDouble(line.get(i));

			}
			j++;
		}

		return matrix;

	}

	public static double[][] csv2matrix_caple(String s) {
		String fileName = s;
		File file = new File(fileName);
		List<List<String>> lines = new ArrayList<List<String>>();
		Scanner inputStream;

		try {
			inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String line = inputStream.next();
				String[] values = line.split(",");
				lines.add(Arrays.asList(values));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		double[][] matrix = new double[lines.size()][(lines.get(0).size())];
		int j = 0;
		for (List<String> line : lines) {
			for (int i = 0; i < line.size(); i++) { // iterate over the elements

				matrix[j][i] = Double.parseDouble(line.get(i));

			}
			j++;
		}

		return matrix;

	}

	private static void copy(int i, int k, double[][] matrix, StringBuilder sb) {
		for (int j = 0; j < matrix[0].length; j++) {
			if (j != Date.valueOf("iid").ordinal() && j != Date.valueOf("id").ordinal()
					&& j != Date.valueOf("partner").ordinal() && j != Date.valueOf("pid").ordinal()
					&& j != Date.valueOf("match").ordinal() && j != Date.valueOf("caple").ordinal()
					&& j != Date.valueOf("wave").ordinal() ) {
				sb.append(matrix[i][j]);
				sb.append(',');
			}

		}
		for (int j = 0; j < matrix[0].length; j++) {
			if (j != Date.valueOf("iid").ordinal() && j != Date.valueOf("id").ordinal()
					&& j != Date.valueOf("partner").ordinal() && j != Date.valueOf("pid").ordinal()
					&& j != Date.valueOf("caple").ordinal() && j != Date.valueOf("wave").ordinal()) {
				sb.append(matrix[k][j]);
				sb.append(',');
			}

		}
		sb.append('\n');
		matrix[i][Date.valueOf("caple").ordinal()] = 1;
		matrix[k][Date.valueOf("caple").ordinal()] = 1;

	}

	//copy to csv with ID
	private static void copyID(int i, int k, double[][] matrix, StringBuilder sb) {
		for (int j = 0; j < matrix[0].length; j++) {
			if ( j != Date.valueOf("match").ordinal() && j != Date.valueOf("caple").ordinal()
					&& j != Date.valueOf("wave").ordinal() ) {
				sb.append(matrix[i][j]);
				sb.append(',');
			}

		}
		for (int j = 0; j < matrix[0].length; j++) {
			if (j != Date.valueOf("caple").ordinal() && j != Date.valueOf("wave").ordinal()) {
				sb.append(matrix[k][j]);
				sb.append(',');
			}

		}
		sb.append('\n');
		matrix[i][Date.valueOf("caple").ordinal()] = 1;
		matrix[k][Date.valueOf("caple").ordinal()] = 1;

	}


	public static void print2csv_justcaple(double[][] matrix, String s) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(s));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {

			for (int k = 0; k < matrix.length; k++) {

				if (matrix[i][Date.valueOf("wave").ordinal()] == matrix[k][Date.valueOf("wave").ordinal()]
						&& matrix[i][Date.valueOf("partner").ordinal()] == matrix[k][Date.valueOf("id").ordinal()]
								&& matrix[i][Date.valueOf("pid").ordinal()] == matrix[k][Date.valueOf("iid").ordinal()]
										&& matrix[k][Date.valueOf("partner").ordinal()] == matrix[i][Date.valueOf("id").ordinal()]
												&& matrix[k][Date.valueOf("pid").ordinal()] == matrix[i][Date.valueOf("iid").ordinal()]) {
					for (int j = 0; j < matrix[0].length; j++) {
						if (j != Date.valueOf("caple").ordinal()) {
							sb.append(matrix[i][j]);
							sb.append(',');
						}

					}

					// copy(i, k, matrix, sb);
					sb.append('\n');
					matrix[i][Date.valueOf("caple").ordinal()] = 1;
					break;

				}

			}
			if (matrix[i][Date.valueOf("caple").ordinal()] == 0) {
				System.out.println("no caple" + matrix[i][Date.valueOf("id").ordinal()] + " "
						+ matrix[i][Date.valueOf("iid").ordinal()] + " " + matrix[i][Date.valueOf("pid").ordinal()] + ""
						+ matrix[i][Date.valueOf("partner").ordinal()] + ""
						+ matrix[i][Date.valueOf("match").ordinal()]);
			}
		}
		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
	}



	public static void print2csv(double[][] matrix, String s) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(s));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i][Date.valueOf("caple").ordinal()] == 1) {
				continue;
			}
			for (int k = 0; k < matrix.length; k++) {
				if (matrix[k][Date.valueOf("caple").ordinal()] == 1) {
					continue;
				}
				if (matrix[i][Date.valueOf("wave").ordinal()] == matrix[k][Date.valueOf("wave").ordinal()]
						&& matrix[i][Date.valueOf("partner").ordinal()] == matrix[k][Date.valueOf("id").ordinal()]
								&& matrix[i][Date.valueOf("pid").ordinal()] == matrix[k][Date.valueOf("iid").ordinal()]
										&& matrix[k][Date.valueOf("partner").ordinal()] == matrix[i][Date.valueOf("id").ordinal()]
												&& matrix[k][Date.valueOf("pid").ordinal()] == matrix[i][Date.valueOf("iid").ordinal()]) {
					//copy(i, k, matrix, sb);
					copyID(i, k, matrix, sb);
					break;

				}

			}
			if (matrix[i][Date.valueOf("caple").ordinal()] == 0) {
				System.out.println("no caple" + matrix[i][Date.valueOf("id").ordinal()] + " "
						+ matrix[i][Date.valueOf("iid").ordinal()] + " " + matrix[i][Date.valueOf("pid").ordinal()] + ""
						+ matrix[i][Date.valueOf("partner").ordinal()] + ""
						+ matrix[i][Date.valueOf("match").ordinal()]);
			}
		}

		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
	}

	// public static void main(String[] args) throws FileNotFoundException {
	//
	// print2csv(csv2matrix());
	// double[][] mat = csv2matrix_caple();
	//
	// }

	//Change the numbering of the goal to the evening from a csv file to a new csv file

	public static void change_goal(String s) throws FileNotFoundException {
		String fileName = s;
		File file = new File(fileName);
		List<List<String>> lines = new ArrayList<List<String>>();
		Scanner inputStream;

		try {
			inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String line = inputStream.next();
				String[] values = line.split(",");
				lines.add(Arrays.asList(values));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	

		String new_s=s.substring(0, s.length()-4);
		new_s=new_s+" goal.csv";

		PrintWriter pw = new PrintWriter(new File(new_s));
		StringBuilder sb = new StringBuilder();
		List<String> attr=lines.get(0);
		lines.remove(0);
		sb.append(attr);
		sb.append('\n');

		int j = 0;
		for (List<String> line : lines) {
			for (int i = 0; i < line.size(); i++) { // iterate over the elements
				if(attr.get(i).contains("goal")){
					int g=(int)Double.parseDouble(line.get(i));
					switch (g) {
					case 1:
						sb.append("5 ,");						
						break;
					case 2:
						sb.append("3 ,");						
						break;
					case 3:
						sb.append("2 ,");						
						break;
					case 4:
						sb.append("1 ,");						
						break;
					case 5:
						sb.append("4 ,");						
						break;					
					default:
						sb.append("6 ,");						
						break;	
					}
				}
				else{
					sb.append(Double.parseDouble(line.get(i)));
					sb.append(',');
					
				}

			}
			j++;
			sb.append('\n');

		}
		
		
		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");

	}
	

	public static double[][] getMatrix() throws FileNotFoundException {
		print2csv(csv2matrix("Speed Dating Data-before full1.csv"), "Speed Dating Data-before full1 caple.csv");
		double[][] mat = csv2matrix_caple("Speed Dating Data-before full1 caple.csv");

		return mat;

	}

	public static void getcsvfull() throws FileNotFoundException {
		//print2csv(csv2matrix("Speed Dating Data-before full.csv"), "Speed Dating Data-before caple id name.csv");

		print2csv_justcaple(csv2matrix("Speed Dating Data-before.csv"), "Speed Dating Data-before caple id name.csv");

		//print2csv_justcaple(csv2matrix("Speed Dating Data-before.csv"), "Speed Dating Data-before full1.csv");
		// double[][] mat = csv2matrix_caple("Speed Dating Data-before
		// caple.csv");

		// return mat;

	}

	public static double[][] getMatrixnormal() throws FileNotFoundException {
		print2csv(csv2matrix("Speed Dating Data-before full.csv"), "Speed Dating Data-before caple id name.csv");
		//double[][] mat = csv2matrix_caple("Speed Dating Data-before caple normal.csv");

		return null;

	}

	public static void main(String[] args) throws FileNotFoundException {
		// getMatrix();
		//getcsvfull();
		change_goal("speed-less fit half.csv");
	}
}
