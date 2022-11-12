package mos;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class OS {

  // Essential variables
  private char[][] M = new char[300][4];
  private char[] IR = new char[4];
  private char[] R = new char[4];
  private int SI;
  private int TI;
  private int PI;
  private char[] PTR = new char[4];
  private int PCB; // To define class
  private int VA;
  private int RA;
  private int TTC;
  private int LLC;
  private int TTL;
  private int TLL;
  private int EM = 0;
  private int IC;
  private boolean C = false;
  private char[] inputBuffer = new char[40];
  // private int data_index = 0;
  // private String time, id, lines_printed;
  private String inputFile;
  private String outputFile;

  // Semi-Essential variables
  private int memory_used;
  private FileReader input;
  private BufferedReader buff;
  private FileWriter output;
  private BufferedWriter fwrite;

  // Constructor
  public OS(String file, String output) {
    this.inputFile = file;
    this.outputFile = output;
    this.SI = 0;

    try {
      this.input = new FileReader(inputFile);
      this.buff = new BufferedReader(input);
      this.output = new FileWriter(output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void print_memory() {
    for (int i = 0; i < 100; i++) {
      System.out.println("memory[" + i + "] " + new String(M[i]));
    }
  }

  public void OUTPUT() throws IOException {
    FileWriter fr = new FileWriter(outputFile);
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 4; j++) {
        try {
          fr.write(M[i][j]);
        } catch (IOException e) {
          System.out.println("Error in output: " + e);
        }
      }
    }
    fr.close();
  }

  public void EXECUTE_USER_PROGRAM() throws Exception {
    while (2 < 3) {
      if (IC == 100) {
        break;
      }

      IR[0] = M[IC][0];
      IR[1] = M[IC][1];
      IR[2] = M[IC][2];
      IR[3] = M[IC][3];

      IC++;

      if (IR[0] == 'L' && IR[1] == 'R') {
        String line = new String(IR);
        // System.out.println(line);
        // System.out.println(line.substring(2));
        int num = Integer.parseInt(line.substring(2));
        R[0] = M[num][0];
        R[1] = M[num][1];
        R[2] = M[num][2];
        R[3] = M[num][3];
      } else if (IR[0] == 'S' && IR[1] == 'R') {
        String line = new String(IR);
        int num = Integer.parseInt(line.substring(2));
        M[num][0] = R[0];
        M[num][1] = R[1];
        M[num][2] = R[2];
        M[num][3] = R[3];
      } else if (IR[0] == 'C' && IR[1] == 'R') {
        String line = new String(IR);
        int num = Integer.parseInt(line.substring(2));
        if (M[num][0] == R[0] && M[num][1] == R[1] && M[num][2] == R[2] && M[num][3] == R[3]) {
          C = true;
        } else {
          C = false;

        }
      } else if (IR[0] == 'B' && IR[1] == 'T') {
        if (C == true) {
          String line = new String(IR);
          int num = Integer.parseInt(line.substring(2));
          IC = num;
          C = false;
        }
      } else if (IR[0] == 'G' && IR[1] == 'D') {
        SI = 1;
        MOS();
      } else if (IR[0] == 'P' && IR[1] == 'D') {
        SI = 2;
        MOS();
      } else if (IR[0] == 'H') {
        SI = 3;
        MOS();
      }
    }
  }

  public void READ() {
    IR[3] = '0';
    String line = new String(IR);

    int num = Integer.parseInt(line.substring(2));

    try {
      line = buff.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    inputBuffer = line.toCharArray();
    for (int i = 0; i < line.length();) {
      M[num][i % 4] = inputBuffer[i];
      i++;
      if (i % 4 == 0) {
        num++;
      }
    }
  }

  public void WRITE() throws Exception {
    IR[3] = '0';
    String line = new String(IR);
    int num = Integer.parseInt(line.substring(2));
    String t, total = "";
    for (int i = 0; i < 10; i++) {
      t = new String(M[num + i]);
      total = total.concat(t);
    }
    System.out.println(total + "In write");
    try {
      Writer output = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(outputFile, true), "UTF-8"));
      output.append(total);
      output.append("\r\n");
      output.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void TERMINATE() throws Exception {
    Writer output;
    output = new BufferedWriter(new FileWriter(outputFile, true));
    output.append('\n');
    output.append('\n');
    System.out.println("Program Executed");
    output.close();
    LOAD();
  }

  public void LOAD() throws Exception {
    SI = 0;
    String strLine;

    try {
      // This loop fills the 40 byte inputBuffer for buffered execution
      while ((strLine = buff.readLine()) != null) {
        inputBuffer = strLine.toCharArray(); // Started filling inputBuffer

        if (inputBuffer[0] == '$' && inputBuffer[1] == 'A' && inputBuffer[2] == 'M' && inputBuffer[3] == 'J') {
          init();
          continue;
        }

        else if (inputBuffer[0] == '$' && inputBuffer[1] == 'D' && inputBuffer[2] == 'T' && inputBuffer[3] == 'A') {
          EXECUTE_USER_PROGRAM();
          continue;
        } else if (inputBuffer[0] == '$' && inputBuffer[1] == 'E' && inputBuffer[2] == 'N' && inputBuffer[3] == 'D') {
          System.out.println("\n\n\n");
          SI = 3;
          MOS();
          continue;
        }
        if (memory_used == 100) { // abort;
          System.out.println("Abort due to exceed M usage");
        }

        for (int j = 0; j < strLine.length();) {
          M[memory_used][j % 4] = inputBuffer[j];
          j++;
          if (j % 4 == 0)
            memory_used++;
        }
      }
    } catch (Exception e) {
      System.out.println("Error in load: " + e);
    }
  }

  public void START_EXECUTION() throws Exception {
    IC = 00;
    EXECUTE_USER_PROGRAM();
  }

  public void init() {
    memory_used = 0;
    M = null;
    M = new char[100][4];
    C = false;
    IC = 0;
  }

  public void MOS() throws Exception {
    int si = this.SI;
    int ti = this.TI;
    int pi = this.PI;

    // Ti and Si section

    if (ti == 0 && si == 1) {
      READ();
    }
    if (ti == 0 && si == 2) {
      WRITE();
    }
    if (ti == 0 && si == 3) {
      TERMINATE(0);
    }
    if (ti == 2 && si == 1) {
      TERMINATE(1);
    }
    if (ti == 2 && si == 2) {
      TERMINATE(3);
    }
    if (ti == 2 && si == 3) {
      TERMINATE(0);
    }

    // Ti and Pi section

    if (ti == 0 && pi == 1) {
      TERMINATE(4);
    }

    if (ti == 0 && pi == 2) {
      TERMINATE(5);
    }

    if (ti == 0 && pi == 3) {
      // if valid handle using paging
      TERMINATE(6);
    }

    if (ti == 2 && pi == 1) {
      TERMINATE(3, 4);
    }

    if (ti == 2 && pi == 2) {
      TERMINATE(3, 5);
    }

    if (ti == 2 && pi == 3) {
      TERMINATE(3);
    }

  }
}
