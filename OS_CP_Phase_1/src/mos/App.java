package mos;

public class App {

  public static void main(String[] args) throws Exception {
    OS mos = new OS("input.txt", "output.txt");
    mos.LOAD();
  }
}