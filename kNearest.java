import java.util.*;
import java.util.Scanner.*;

import jdk.jfr.Percentage;

import java.io.*;
import java.math.*;

public class kNearest {

    private static ArrayList<String> attributes = new ArrayList<String>();
    private static ArrayList<Double> featureRange = new ArrayList<Double>(); // This value stores the range of each                                                                      
    private static List<Instance> trainInstances, testInstances;

    public static void main(String[] args) {
        // Load in the data
        Scanner training = new Scanner(System.in);
        System.out.println("Enter training set: ");
        String trainingLine = training.next();
        Scanner test = new Scanner(System.in);
        System.out.println("Enter testing set: ");
        String testingLine = test.next();
        readDataFile(trainingLine, testingLine);
        Scanner kScan = new Scanner(System.in);
        System.out.println("Enter k-neighbours: ");
        int n = kScan.nextInt();

        kNearestNeighbour(n, trainInstances, testInstances);
   
        training.close();
        test.close();
        kScan.close();
    }

    /**
     * Compares every value of the test dataset to the training dataset in order to predict its class.
     * Also takes in a parameter which identifies how many neighbours the prediction should be based on.
     * 
     * Prints out the accuracy of the prediction by checking with the actual class value.
     * 
     * @param kNeighbours 
     * @param trainingList
     * @param testList
     */
    private static void kNearestNeighbour(int kNeighbours, List<Instance> trainingList, List<Instance> testList){
        for(int test = 0; test < testList.size(); test++){ 
            for(int training = 0; training < trainingList.size(); training++){ 
                double distance = eucDist(trainingList.get(training), testList.get(test));
                testList.get(test).addDistanceClass(distance, trainingList.get(training).getTrueClass());
                testList.get(test).setNumberOfNeighbours(kNeighbours);
            }   
          }

          // Determines the accuracy of the algorithm and prints it
          double counter = 0;
          for(Instance t : testInstances){
              if(t.getPredictedClass() == t.getTrueClass()){
                  System.out.println("Predicted Class: " + t.getPredictedClass() + " Actual Class: " + t.getTrueClass());
                  counter++;
              }
          }
          System.out.print((counter/testList.size())*100 + " " + (int)counter + "/" + testList.size());
    }

    /**
     * Calculates and returns the euclidean distance between two instances 
     * 
     * 
     * 
     * @param point1
     * @param point2
     */
    private static double eucDist(Instance point1, Instance point2){
        double distance = 0;
        double sum = 0;
        rangeCalculator(trainInstances); // Calculates the range of every attribute 

        for(int i = 0; i < point1.getAttributes().size() - 1; i++){
            double p1 = point1.getAttributes().get(i);
            double p2 = point2.getAttributes().get(i);
            sum+=((Math.pow((p1-p2), 2))/featureRange.get(i));
        }
        distance = Math.sqrt(sum);
        return distance;
    }

    /**
     * Accepts a list of instances and calculates the range of each attribute
     * 
     * @param instances 
     */
    private static void rangeCalculator(List<Instance> instances) {
        for (int i = 0; i < attributes.size() - 1; i++) { // -1 because the last column is the class
            double maxRange = 0;
            double minRange = 1000;
            for (int j = 0; j < instances.size(); j++) { // Range based on the training instance
                if (instances.get(j).getAttributes().get(i) > maxRange) {
                    maxRange = instances.get(j).getAttributes().get(i);
                }
                if (minRange > instances.get(j).getAttributes().get(i)) {
                    minRange = instances.get(j).getAttributes().get(i);
                }
            }
            featureRange.add(Math.pow((maxRange - minRange), 2));
        }
    }

    // METHODS FOR READING THE FILE
    
    private static void readDataFile(String training, String test) {
        try {
            // Scan both the training and the test files
            Scanner trainingFile = new Scanner(new File(training));
            Scanner testFile = new Scanner(new File(test));

            // puts the attributes in an array
            Scanner attributeScan = new Scanner(trainingFile.nextLine());
            while (attributeScan.hasNext()) {
                attributes.add(attributeScan.next());
            }

            testFile.nextLine(); // Skips the first line which are the attribute names

            // Read the instances
            trainInstances = readInstances(trainingFile);
            testInstances = readInstances(testFile);


            trainingFile.close();
            testFile.close();
            attributeScan.close();

        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO Exception");
        }
    }

    private static List<Instance> readInstances(Scanner fileScan) {
        List<Instance> instances = new ArrayList<Instance>();
        while (fileScan.hasNext()) {
            Instance ins = new Instance();
            Scanner instanceLine = new Scanner(fileScan.nextLine());
            while (instanceLine.hasNext()) {
                Double currentVal = Double.valueOf(instanceLine.next());
                ins.addAttribute(currentVal);
            }
            instances.add(ins);
            instanceLine.close();
        }
        return instances;
    }
}