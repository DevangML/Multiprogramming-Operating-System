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
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.midi.SysexMessage;

import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.*;

public class OS {

  // Essential variables
  private char[][] M = new char[300][4];
  private char[][] PT = new char[10][4];
  private char[] IR = new char[4];
  private char[] R = new char[4];
  private int SI = 3;
  private int TI = 0;
  private int PI;
  private int PTR;
  private int PCB; // To define class
  private int VA;
  private int RA;
  private int TTC;
  private int LLC;
  private int TTL;
  private int TLL;
  private int JID;
  private int EM = 0;
  private int IC;
  private int BA = 0;
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
  Set<Integer> set = new HashSet<>();

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
        MOS(inputBuffer);
      } else if (IR[0] == 'P' && IR[1] == 'D') {
        SI = 2;
        MOS(inputBuffer);
      } else if (IR[0] == 'H') {
        SI = 3;
        MOS(inputBuffer);
      }
    }
  }

  public void READ(char[] inputBuffer) {

    if (inputBuffer[0] == '$' && inputBuffer[1] == 'E' && inputBuffer[2] == 'N' && inputBuffer[3] == 'D') {
      TERMINATE(1);
    }

    String line = new String(RA);

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
    LLC++;
    if (LLC > TLL) {
      TERMINATE(2);
    }

    IR[3] = '0';
    String line = new String(RA);
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
    EXECUTE_USER_PROGRAM();
  }

  public void TERMINATE(int error) throws Exception {
    Writer output;
    output = new BufferedWriter(new FileWriter(outputFile, true));
    output.append('\n');
    output.append('\n');
    System.out.println("Program Executed");

    if (error > 0) {
      System.out.println("JOB ID : " + JID);
      if (error == 1) {
        System.out.println("OUT OF DATA");
      }

      if (error == 2) {
        System.out.println("LINE LIMIT EXCEEDED");
      }

      if (error == 3) {
        System.out.println("TIME LIMIT EXCEEDED");
      }

      if (error == 4) {
        System.out.println("OPCODE ERROR");
      }

      if (error == 5) {
        System.out.println("OPERAND ERROR");
      }

      if (error == 6) {
        System.out.println("PAGE FAULT");
      }

      if (error == 7) {
        System.out.println("TIME LIMIT EXCEEDED WITH OPCODE");
      }

      if (error == 8) {
        System.out.println("TIME LIMIT EXCEEDED WITH OPERAND");
      }
      System.out.println("IC : " + IC);
      String ir = new String(IR);
      System.out.println("IR : " + Integer.parseInt(ir));
      System.out.println("TTC : " + TTC);
      System.out.println("LLC : " + LLC);
    }
    output.close();
    LOAD();
  }

  public void LOAD() throws Exception {

    SI = 0;
    String strLine;

    try {
      // This loop fills the 40 byte inputBuffer for buffered execution
      while ((strLine = buff.readLine()) != null) {
        inputBuffer = strLine.replace(" ", "").toCharArray(); // Started filling inputBuffer

        if (inputBuffer[0] == '$' && inputBuffer[1] == 'A' && inputBuffer[2] == 'M' && inputBuffer[3] == 'J') {
          init();
          StringBuilder sb = new StringBuilder();
          sb.append(inputBuffer[5]).append(inputBuffer[6]).append(inputBuffer[7]).append(inputBuffer[8]);
          String jid = sb.toString();
          sb.setLength(0);
          JID = Integer.parseInt(jid);
          sb.append(inputBuffer[9]).append(inputBuffer[10]).append(inputBuffer[11]).append(inputBuffer[12]);
          String ttl = sb.toString();
          TTL = Integer.parseInt(ttl);
          sb.setLength(0);
          sb.append(inputBuffer[13]).append(inputBuffer[14]).append(inputBuffer[15]).append(inputBuffer[16]);
          String tll = sb.toString();
          TLL = Integer.parseInt(tll);
          sb.setLength(0);
          PCB PCB = new PCB(JID, TTL, TLL);
          PTR = ALLOCATE();
          int row = 0;
          int col = 0;
          while (row < PT.length) {
            PT[row][col] = '0';
            row++;
          }
          while (row < PT.length) {
            col = 1;
            PT[row][col] = '*';
            row++;
          }
          while (row < PT.length) {
            col = 2;
            PT[row][col] = '*';
            row++;
          }
          continue;
        }

        else if (inputBuffer[0] == 'G' && inputBuffer[0] == 'D' || inputBuffer[0] == 'P' && inputBuffer[0] == 'D'
            || inputBuffer[0] == 'L' && inputBuffer[0] == 'R' || inputBuffer[0] == 'S' && inputBuffer[0] == 'R'
            || inputBuffer[0] == 'B' && inputBuffer[0] == 'T' || inputBuffer[0] == 'C' && inputBuffer[0] == 'R'
            || inputBuffer[0] == 'H') {
          BA = ALLOCATE();
          int digit = BA / 10;
          char c = (char) digit;

          int row = 0;
          PT[row][1] = c;
          PT[row][2] = c;
          int temp_ttl = TTL - 1;
          while (temp_ttl > 0) {
            BA = ALLOCATE();
            digit = BA / 10;
            c = (char) digit;
            PT[row++][1] = c;
            PT[row++][2] = c;
            temp_ttl--;
          }
        }

        else if (inputBuffer[0] == '$' && inputBuffer[1] == 'D' && inputBuffer[2] == 'T' && inputBuffer[3] == 'A') {
          EXECUTE_USER_PROGRAM();
          continue;
        } else if (inputBuffer[0] == '$' && inputBuffer[1] == 'E' && inputBuffer[2] == 'N' && inputBuffer[3] == 'D') {
          System.out.println("\n\n\n");
          SI = 3;
          MOS(inputBuffer);
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

  public int ALLOCATE() {
    int temp = ThreadLocalRandom.current().nextInt(0, 30);
    if (set.contains(temp)) {
      temp = ThreadLocalRandom.current().nextInt(0, 30);
    }
    set.add(temp);
    return temp * 10;
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

  public void MOS(char[] inputBuffer) throws Exception {
    int si = this.SI;
    int ti = this.TI;
    int pi = this.PI;

    // Ti and Si section

    if (ti == 0 && si == 1) {
      READ(inputBuffer);
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
      TERMINATE(7);
    }

    if (ti == 2 && pi == 2) {
      TERMINATE(8);
    }

    if (ti == 2 && pi == 3) {
      TERMINATE(3);
    }

  }
}
