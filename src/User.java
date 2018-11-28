import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.kmeans.Centroid;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class User {
    public double[] askQuestions(String filepath, String[] list, double[][] code){
        String[] input = new String[Numbers.QUESTIONNO];
        Scanner scanner = new Scanner(System.in);
        Model nnmodel = new Model();
        Destinations des = new Destinations();
        for (int i = Numbers.ZERO; i< Numbers.QUESTIONNO; i++) {
            System.out.println("Question " + (i + Numbers.ONE) + ": Do you want " + Questions.questions[i] + " ? [yes/no]");
            input[i] = scanner.next();
            for (;;){
                if ((!input[i].equalsIgnoreCase(Questions.NO))&&(!input[i].equalsIgnoreCase(Questions.YES))){
                    System.out.println("Sorry, invalid inputs. Please try again.");
                    input[i] = scanner.next();
                }else {
                    break;
                }
            }
            if (i == Numbers.RANDOMGUESS1 || i == Numbers.RANDOMGUESS2){
                BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(filepath));
                double[] parameters = convert2Double(input);
                double[][] predOne = nnmodel.predictOne(parameters, network, code[Numbers.ZERO].length);
                String desOne = des.convert22DStr(predOne, list)[Numbers.ZERO][Numbers.ZERO];
                answer(desOne);
                System.out.println("Not satisfied, go on? (yes/no)");
                String stop = scanner.next();
                if (stop.equalsIgnoreCase("No")){
                    System.exit(Numbers.ONE);
                }
            }
        }
        double[] answer = convert2Double(input);
        System.out.println("Are you satisfied with our recommendation?");
        String finalfeedback = scanner.next();
        return answer;
    }
    public double[] convert2Double(String[] str){
        double[] para = new double[Numbers.QUESTIONNO];
        for (int i =Numbers.ZERO; i<Numbers.QUESTIONNO; i++){
            String currstr = str[i];
            if (currstr == null){
                Random rand = new Random();
                if (rand.nextInt(Numbers.UNITES)%Numbers.TWO==Numbers.ZERO){
                    para[i] = Numbers.ZERO2;
                } else{
                    para[i] = Numbers.ONE2;
                }
            }else {
                if (currstr.equalsIgnoreCase(Questions.NO)) {
                    para[i] = 0.0;
                } else if (currstr.equalsIgnoreCase(Questions.YES)) {
                    para[i] = 1.0;
                }
            }
        }
        return para;
    }
    public void answer(String str){
        System.out.println("According to our system, we recommend you: " + str);
    }
}
