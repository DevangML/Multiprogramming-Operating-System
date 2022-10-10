package phase;

public class App {

    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");

        Mos mos = new Mos(file);

        try{
            mos.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
