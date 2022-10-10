package phase;

public class App {

    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");

        master mos = new master(file);

        try{
            mos.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
