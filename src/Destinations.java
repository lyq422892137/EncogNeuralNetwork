import java.util.ArrayList;

public class Destinations {
    public String[] countDes(String[][] results){
        int row = results.length;
        ArrayList<String> currlist = new ArrayList<>();
        currlist.add(results[Numbers.ZERO][Numbers.ZERO]);
        for (int i = Numbers.ONE; i<row; i++){
            String curr = results[i][Numbers.ZERO];
            if (!currlist.contains(curr)){
                currlist.add(curr);
            }
        }
        String[] str = currlist.toString().split(",");
        return str;
    }
    public double[][] desCoding(String[] des){
        int col = des.length;
        double[][] descode = new double[col][col];
        for (int i = Numbers.ZERO; i<col;i++){
            descode[i][i] = Numbers.ONE;
        }
        return descode;
    }
    public double[][] conver2doubleArray(String[][] results, double[][] codes, String[] des){
        double[][] y = new double[results.length][des.length];
        for (int i = Numbers.ZERO; i< results.length; i++){
            for (int j = Numbers.ZERO; j< des.length; j++){
                String str1 = des[j];
                String str2 = results[i][Numbers.ZERO];
                if (str1.contains(str2)){
                    y[i][j] = Numbers.ONE;
                }
            }
        }
        return y;
    }
    public String[][] convert22DStr(double[][] num, String[] codestr){
        int row = num.length, col = Numbers.ONE;
        String[][] predes = new String[row][col];
        for (int i = Numbers.ZERO; i < row; i++){
            for (int j = Numbers.ZERO; j < num[Numbers.ZERO].length; j++){
                if (num[i][j] == Numbers.ONE2){
                    predes[i][Numbers.ZERO] = codestr[j];
                }
            }
        }
        return predes;
    }
}
