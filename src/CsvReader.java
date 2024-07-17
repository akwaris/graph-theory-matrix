import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CsvReader {

    public int[][] reader(String csvFilePath) {

        int [][]matrix = new int[0][0];
        String line = "";
        String seperator = ";";
        int rowAmount = 0;
        int columnAmount = 0;

        System.out.println("Adjacency matrix:");
        try(BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(seperator);
                rowAmount++;
                columnAmount = numbers.length;
            }
            matrix = new int[rowAmount][columnAmount];
            br.close();
            BufferedReader br2 = new BufferedReader(new FileReader(csvFilePath));
            int row = 0;
            int column;
            while((line = br2.readLine()) != null) {
                String[] numbers = line.split(seperator);
                for (column = 0; column < numbers.length; column++) {
                    matrix[row][column] = Integer.parseInt(numbers[column]);
                }
                row++;
                System.out.println(Arrays.toString(numbers));
            }
            br2.close();
            System.out.println();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
}
