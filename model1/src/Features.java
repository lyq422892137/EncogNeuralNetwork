import java.util.Random;

public class Features {
    public String[] getFeatures(StringBuffer buffer){
        String[] str1 = buffer.toString().split("\n");
        StringBuffer curr = new StringBuffer();
        for (int i = Numbers.ZERO; i<str1.length; i++){
            curr.append(str1[i]);
        }
        String[] str2 = curr.toString().split(",");
        return str2;
    }
    public double[][] addFeature(double[][] inputs, String[] list, String feature){
        Destinations des = new Destinations();
        int flag = des.checkRepetition(feature, list);
        double[][] newInputs = new double[inputs.length][inputs[Numbers.ZERO].length + Numbers.ONE];
        if (flag==Numbers.ONE) {
            for (int i = Numbers.ZERO; i < inputs.length; i++) {
                for (int j = Numbers.ZERO; j <= inputs[Numbers.ZERO].length; j++) {
                    if (j != inputs[Numbers.ZERO].length) {
                        newInputs[i][j] = inputs[i][j];
                    } else {
                        Random rand = new Random();
                        if (rand.nextInt(Numbers.UNITES) % Numbers.TWO == Numbers.ZERO) {
                            newInputs[i][j] = Numbers.ZERO;
                        } else {
                            newInputs[i][j] = Numbers.ONE;
                        }
                    }
                }
            }
        }else{
            System.out.println("The feature has already exist.");
            System.exit(Numbers.ONE);
        }
        return newInputs;
    }
}
