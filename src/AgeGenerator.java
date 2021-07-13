import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class AgeGenerator {

    private final String listType;
    private final String directory;
    private List<Age> myList;
    private static final String postcode = "AK AL AR AZ CA CO CT DC DE FL " +
            "GA HI IA ID IL IN KS KY LA MA " +
            "MD ME MI MN MO MS MT NC ND NE " +
            "NH NJ NM NV NY OH OK OR PA RI " +
            "SC SD TN TX UT VA VT WA WI WV WY";


    /**
     * Constructor to determine which ListType and Directory file to use.
     * @throws IOException
     */
    public AgeGenerator() throws IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        this.listType = properties.getProperty("ListType");
        this.directory = properties.getProperty("Directory");
        this.myList = getList(this.listType);
    }


    /**
     * Determines what type of list is defined in our Properties file.
     * @param listType
     * @return ArrayList or LinkedList
     */
    private List<Age> getList(String listType) {

        if (listType.equals("ArrayList"))
            return new ArrayList<>();
        else return new LinkedList<>();
    }


    /**
     * This functions main purpose is to read, parse, and store a CSV file
     * @param stateCode
     */
    private void readFiles(String stateCode) {

        this.myList = getList(this.listType);
        final Scanner sc;

        //Validates the user's stateCode with the directory defined in our Properties file.
        try {
            sc = new Scanner(new FileInputStream( this.directory + stateCode + ".TXT"));
        } catch (FileNotFoundException e) {
            System.out.printf("File  %s.TXT not found\n", stateCode);
            return;
        }
        System.out.printf("Reading %s.TXT...", stateCode);

        //Reads and parses a valid CSV file
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            String state = line[0];
            String gender = line[1];
            int year = Integer.parseInt(line[2]);
            String name = line[3];
            int frequency = Integer.parseInt(line[4]);

            Age age = new Age(state, gender, name, year, frequency);
            myList.add(age);
        }
        System.out.println("Done");
    }


    /**
     * Iterates through our collection inorder to find Max and Min values for the specific name
     * with the greatest amount of occurrences.
     * @param state
     * @param name
     * @param gender
     * @return int[]
     */
    private int[] findAge(String state, String name, String gender) {

        Age temp = new Age(state, gender, name, 0, 0);
        Iterator<Age> iterator = myList.iterator();
        int minYear = 1910;
        int maxYear = -1;
        int freq = -1;


        //Iterating through our collection of State specific values
        while (iterator.hasNext()) {
            Age age = iterator.next();
            if (age.name.equals(temp.name) && age.gender.equals(temp.gender) && age.state.equals(temp.state)) {

                //Monitors the number of occurrences of specific name, while also accounting for multiple valid age hypotheses
                if (age.freq == freq) {
                    minYear = Math.min(age.year, minYear);
                    maxYear = Math.max(age.year, maxYear);
                }else if (age.freq > freq) {
                    minYear = age.year;
                    maxYear = age.year;
                    freq = age.freq;
                }
            }
        }

        return new int[] {minYear, maxYear};
    }


    /**
     * A print function that uses the age range determined in our findAge function to output one of three scenarios.
     * The first being a the maxYear variable was not updated, likely meaning bad user input. The second scenario is
     * one were a single valid age hypotheses is made. The third meaning that multiple valid age hypotheses are calculated.
     * @param ageGenerator
     * @param name
     * @param gender
     * @param state
     */
    private static void printAge(AgeGenerator ageGenerator, String name, String gender, String state) {

        ageGenerator.readFiles(state);
        int[] ageRange = ageGenerator.findAge(state, name, gender);

        if (ageRange[1] == -1) {
            System.out.printf("No Such Person (Name: %s, Gender: %s, State: %s)\n", name, gender, state);

        } else if (ageRange[0] == ageRange[1]){
            System.out.printf("%s, born in %s is most likely around %s years old\n",
                    name, state, ((2021 - ageRange[1])));
        } else {
            System.out.printf("%s, born in %s is most likely around %s years old\n",
                    name, state, String.format("%s - %s", 2021 - ageRange[1], 2021 - ageRange[0]));
        }
    }


    public static void main(String[] args) throws IOException {

        final Scanner sc = new Scanner(System.in);
        AgeGenerator ageGenerator = new AgeGenerator();
        boolean valid = false;

        while (!valid) {
            String name, gender, state;

            System.out.print("Name of the person (or EXIT to quit) : ");
            name = sc.next();
            if (name.equals("EXIT") || name.equals("e")) {
                valid = true;
            } else {

                System.out.print("GENDER (M/F): ");
                gender = sc.next().toUpperCase();
                System.out.print("State of Birth (two-letter state code): ");
                state = sc.next().toUpperCase();
                while(!valid) {
                    if (postcode.contains(state)) {
                        valid = true;
                    } else {
                        System.out.print("Please enter a valid Postal Code: ");
                        state = sc.next().toUpperCase();
                        valid = false;
                    }
                }
                valid = false;

                printAge(ageGenerator, name, gender, state);
            }
        }
    }


    public static class Age {
        String state;
        String gender;
        String name;
        int year;
        int freq;

        /**
         *
         * @param state
         * @param gender
         * @param name
         * @param year
         * @param freq
         */
        public Age(String state, String gender, String name, int year, int freq) {

            this.state = state;
            this.gender = gender;
            this.name = name;
            this.year = year;
            this.freq = freq;
        }
    }
}
