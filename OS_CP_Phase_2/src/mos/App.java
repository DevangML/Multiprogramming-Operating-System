package mos;

public class App {

  public static void main(String[] args) throws Exception {
    OS os = new OS("input.txt", "output.txt");
    os.LOAD();
  }
}