
import java.util.*; 

public class Instance{
    private ArrayList<Double> attributes;
    private TreeMap<Double, Integer> distanceClass = new TreeMap<Double, Integer>(); // Maps the distances to the classes
    private int predictedClass;
    private int numberOfNeighbours;

    // Constructor that constructs an instance with an attributes list
    public Instance(){
        attributes = new ArrayList<Double>();
    }

    // Accessors and mutators for the attributes
    public void addAttribute(Double d){
        attributes.add(d);
    }

    public ArrayList<Double> getAttributes(){
        return attributes;
    }

    // Adding distances and class. Note: This is mainly present on the test instances
    public void addDistanceClass(Double d, int c){
        distanceClass.put(d, c);
    }

    public TreeMap<Double, Integer> getDistanceClass(){
        return distanceClass;
    }

    // Setting the predicted class, Note: This is mainly present on the test instances
    public void setNumberOfNeighbours(int k){
        numberOfNeighbours = k;
    }

    /**
     * Calculates the predicted class according to the mode of the values returned
     * by the k nearest neighbours
     * 
     */
    public int getPredictedClass(){
        ArrayList<Integer> prediction = new ArrayList<Integer>();
        Set<Double> keySet = distanceClass.keySet();
        ArrayList<Double> listOfKeys = new ArrayList<Double>(keySet);

        for(int i = 0; i < numberOfNeighbours; i++){
            prediction.add(distanceClass.get(listOfKeys.get(i)));
        }
        return mode(prediction);
    }

    public int getTrueClass(){
        double doubleClass = (attributes.get(attributes.size()-1)); // The class is the last column in the dataset 
        return (int)doubleClass;
    }

    /**
     * Calculates the mode of a list
     * 
     * @param a
     */
    private int mode(ArrayList<Integer> a){
        int maxValue = 0;
        int maxCount = 0;
        for(int i = 0; i < a.size(); i++){
            int count = 0;
            for(int j = 0; j < a.size(); j++){
                if(a.get(i) == a.get(j)){
                    count++;
                }
                if(count > maxCount){
                    maxCount = count;
                    maxValue = a.get(i);
                }
            }
        }
        return maxValue;
    }
}
