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
	public static double[][] csv2matrix() {
		String fileName = "Speed Dating Data-before.csv";
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

	public static double[][] csv2matrix_caple() {
		String fileName = "Speed Dating Data-before caple.csv";
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
					&& j != Date.valueOf("match").ordinal() && j != Date.valueOf("caple").ordinal()) {
				sb.append(matrix[i][j]);
				sb.append(',');
			}

		}
		for (int j = 0; j < matrix[0].length; j++) {
			if (j != Date.valueOf("iid").ordinal() && j != Date.valueOf("id").ordinal()
					&& j != Date.valueOf("partner").ordinal() && j != Date.valueOf("pid").ordinal()
					&& j != Date.valueOf("caple").ordinal()) {
				sb.append(matrix[k][j]);
				sb.append(',');
			}

		}
		sb.append('\n');
		matrix[i][Date.valueOf("caple").ordinal()] = 1;
		matrix[k][Date.valueOf("caple").ordinal()] = 1;

	}

	public static void print2csv(double[][] matrix) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("Speed Dating Data-before caple.csv"));
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
					copy(i, k, matrix, sb);
					break;

				}
				
					
			}
//			if (matrix[i][Date.valueOf("caple").ordinal()] == 0) {
//				System.out.println("no caple"+matrix[i][Date.valueOf("id").ordinal()]+" "+matrix[i][Date.valueOf("iid").ordinal()]+" "+ matrix[i][Date.valueOf("pid").ordinal()]+" "+matrix[i][Date.valueOf("partner").ordinal()]+" "+matrix[i][Date.valueOf("match").ordinal()] );
//			}
		}

		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
	}

//	public static void main(String[] args) throws FileNotFoundException {
//
//		print2csv(csv2matrix());
//		double[][] mat = csv2matrix_caple();
//
//	}

	 public static double[][] getMatrix() throws FileNotFoundException {
	 print2csv(csv2matrix());
	 double[][] mat = csv2matrix_caple();
	
	 return mat;
	
	 }
}
