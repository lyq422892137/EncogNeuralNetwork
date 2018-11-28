import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
public class DataProcess {
    /**
     * read a file and store contents into a stringbuffer
     * **/
    public StringBuffer readFile(String filepath){
        StringBuffer buffer = new StringBuffer();
        File file = new File(filepath);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                while (st.hasMoreTokens()) {
                    buffer.append(st.nextToken());
                    buffer.append(",");
                }
                buffer.append("\n");
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return buffer;
    }
    /**
     * produce inputs used for training a NN
     * **/
    public double[][] inputProcessor(StringBuffer buffer){
        int col;
        int row;
        String str1[], str2[];
        // convert string buffer into a string[] by \n
        str1 = buffer.toString().split("\n");
        //clear the buffer
        buffer = new StringBuffer();
        // add one row into the cleared buffer
        buffer.append(str1[Numbers.ZERO]);
        /*
        * calculate numbers of rows and columns
        * */
        //the first row of column names will not be included
        row = str1.length - Numbers.ONE ;
        //the last col of results will not be included
        col = buffer.toString().split(",").length - Numbers.ONE;
        //declare a 2d int array
        double[][] inputs = new double[row][col];
//        System.out.println("row: "+ row +"\n column: "+ col);
        //assign values into the 2d double[][]
        for (int i=Numbers.ZERO; i<row; i++){
            //clear the buffer
            buffer = new StringBuffer();
            // add one row into the cleared buffer
            buffer.append(str1[i + Numbers.ONE]);
            // split values in the row
            str2 = buffer.toString().split(",");
            for (int j=Numbers.ZERO;j<col; j++){
                inputs[i][j]= Double.parseDouble(str2[j]);
            }
        }
        return inputs;
    }
    /**
     * produce outputs used for training a NN
     * **/
    public String[][] outputProcessor(StringBuffer buffer, int row){
        String[][] results = new String[row][Numbers.ONE];
        String str1[], str2[];
        str1 = buffer.toString().split("\n");
        for (int i=Numbers.ZERO; i<row; i++){
            //clear the buffer
            buffer = new StringBuffer();
            // add one row into the cleared buffer
            buffer.append(str1[i + Numbers.ONE]);
            // split values in the row
            str2 = buffer.toString().split(",");
            results[i][Numbers.ZERO] = str2[str2.length-Numbers.ONE].replaceAll(" ","");
        }
        results[Numbers.ZERO][Numbers.ZERO] = results[Numbers.ZERO][Numbers.ZERO].replaceAll("\\p{Punct}","");
        results[row - Numbers.ONE][Numbers.ZERO] = results[row - Numbers.ONE][Numbers.ZERO].replaceAll("\\p{Punct}","");
        return results;
    }
    public void printStrArray(String[] array) {
        for (int i=Numbers.ZERO; i< array.length; i++){
            System.out.print(array[i]+ " ");
        }
        System.out.println();
    }
    public void printDoubleArray(double[] array) {
        for (int i=Numbers.ZERO; i< array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
    public void printKIntArray(int[] array) {
        for (int i=Numbers.ZERO; i< array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
    public void print2DDoubleArray(double[][] array){
        for (int i=Numbers.ZERO; i<array.length;i++){
            for (int j=Numbers.ZERO; j<array[Numbers.ZERO].length;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public void print2DStrArray(String[][] array){
        for (int i=Numbers.ZERO; i<array.length;i++){
            for (int j=Numbers.ZERO; j<array[Numbers.ZERO].length;j++){
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

