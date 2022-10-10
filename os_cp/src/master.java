package phase;
import kava.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class master {
  public char[][] M = new char[100][4];
  public char[] IR = new char[4];
  public char[] R = new char[4];
  public int SI = 0;
  public int ic;
  public boolean C = false;
  public String[] inputBuffer = new String[40];
  public int data_index = 0;
  public String time, id, lines_printed;
  public File file; // For storing file location

  public master(File file) {
    this.file = file; // For getting file location from main()
  }

  public void terminate() throws Exception {
    Writer output;
    output = new BufferedWriter(new FileWriter(file, true));
    output.append('\n');
    output.append('\n');
    System.out.println("Program Executed");
    output.close();
    load();
  }

  public void load() throws Exception {
    FileInputStream IN = new FileInputStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(IN));
    FileWriter fr = new FileWriter("output.txt")

    int i = 0;

    try{
    String strLine = br.readLine();
    // This loop fills the 40 byte buffer for buffered execution
    while(strLine != null) {
      inputBuffer[i] = strLine; // Started filling buffer
      i++;
      strLine = br.readLine();
    }
      }
    catch (Exception e) {
      System.out.println(e);
    }
    br.close();

  int counter = -1, index = 0; // To read non code part i.e. metadata like TLE we keep counter at -1 initially

  while (index<i) {
    String line = inputBuffer[index];
    
    if (line != null && line.startsWith("$")) {
      if (line.substring(1,4) == "AMJ"){
        // This is control card code

        fr.write("Reading Program");
        id = line.substring(4,8);
        time = line.substring(8,12);
        lines_printed = line.substring(12,16);
        counter = 0;
      }

      else if (line.substring(1,4) == "DTA"){
        fr.write("Reading Data\n");
        counter = 1;
        data_index = index+1; // ye nahi samjha
        start_execution();
        index = data_index - 1;
      }

      else if (line.substring(1,4) == "END") {
        counter = -1;
        C = false;
        for(int i1 = 0; i1<100; i1++) {
          for (int j = 0; j<4; j++) {
            M[i1][j] = '\0'
          }
        }
      }
      else {
        System.out.println("Error in file");
      }
    }
    else {
      if (counter==0)
    }
  }




    }
  

  public void start_execution() {
    ic = 00;
    slave.execute_user_program();
  }

  public void read(row, col) {
    BufferedReader br = new BufferedReader(new FileReader(file));
    IR[3] = 0;

    // Declaring a string variable

    // Doubt : There are multiple $DTA

    int counter = 0;


    if (br.readLine() == "$DTA") {
      while(br.readLine() != "$END") {
        M[row++][col] = br.readLine();
      }
    }
        slave.execute_user_program();
  }

  public void master_mode() throws Exception {
    int operand = (IR[2]*10)+IR[3];
    if (SI==1) {
      GD(operand);
    }
    else if (SI==2) {
      PD(operand);
    }
    else if (SI==3) {
      terminate();
    }
  }
}

