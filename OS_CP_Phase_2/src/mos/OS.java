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
import java.util.*;

public class OS {

  // Essential variables
  private char[][] M = new char[300][4];
  private char[][] PT = new char[300][4];
  private char[] IR = new char[4];
  private char[] R = new char[4];
  private int SI = 3;
  private int TI = 0;
  private int PI;
  private int PTR;
  private String VA;
  private int RA;
  private int TTC;
  private int LLC;
  private int TTL;
  private int TLL;
  private int JID;
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
  Map<Integer, String> map = new HashMap<>();
  private int BA = 0;

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

    map.put(++EM, "OUT OF DATA");
    map.put(++EM, "LINE LIMIT EXCEEDED");
    map.put(++EM, "TIME LIMIT EXCEEDED");
    map.put(++EM, "OPERATION CODE");
    map.put(++EM, "OPERAND");
    map.put(++EM, "INVALID PAGE FAULT");
    map.put(++EM, "TIME LIMIT EXCEEDED AND OPERATION CODE ERROR");
    map.put(++EM, "TIME LIMIT EXCEEDED AND OPERAND ERROR");
    EM = 0;
  }

  public void print_memory() {
    for (int i = 0; i < 300; i++) {
      System.out.println("memory[" + i + "] " + new String(M[i]));
    }
  }

  public void OUTPUT() throws IOException {
    FileWriter fr = new FileWriter(outputFile);
    for (int i = 0; i < 300; i++) {
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
      if (IC == 300) {
        break;
      }

      VA = String.valueOf(IC);
      RA = ADDRESSMAP(VA);
      IR[0] = M[RA][0];
      IR[1] = M[RA][1];
      IR[2] = M[RA][2];
      IR[3] = M[RA][3];
      IC++;
      StringBuilder v = new StringBuilder();
      v.append(IR[2]).append(IR[3]);
      String vb = v.toString();
      VA = String.valueOf(vb);
      RA = ADDRESSMAP(VA);

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
      } else {
        PI = 1;
        MOS(inputBuffer);
      }
      SIMULATION();
    }
  }

  public static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public int ADDRESSMAP(String va) throws Exception {
    int ra = 0;
    if (isNumeric(va)) {
      char[] frameno = new char[2];
      frameno[0] = (char) (Character.getNumericValue(M[PTR][2]));
      frameno[1] = (char) (Character.getNumericValue(M[PTR][3]));
      String r = new String(frameno);

      if (frameno[0] == '*' && frameno[1] == '*') {
        PI = 3;
        MOS(inputBuffer);
      } else {
        ra = Integer.parseInt(r) * 10 + (Integer.parseInt(va) / 10);
      }
    } else {
      PI = 2;
      MOS(inputBuffer);
    }

    return ra;
  }

  public void SIMULATION() throws Exception {
    TTC++;
    if (TTC > TTL) {
      TI = 2;
      MOS(inputBuffer);
    }
  }

  public void READ(char[] inputBuffer) throws Exception {

    if (inputBuffer[0] == '$' && inputBuffer[1] == 'E' && inputBuffer[2] == 'N' && inputBuffer[3] == 'D') {
      TERMINATE(1);
    }

    IR[3] = '0';
    String line = new String(IR);

    int num = Integer.parseInt(line.substring(2)); // operand

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
    String t, total = "";
    String line = new String(IR);
    int num = Integer.parseInt(line.substring(2));
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
      System.out.println(map.get(error));
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
        inputBuffer = strLine.toCharArray(); // Started filling inputBuffer

        if (inputBuffer[0] == '$' && inputBuffer[1] == 'A' && inputBuffer[2] == 'M' && inputBuffer[3] == 'J') {
          init();
          StringBuilder sb = new StringBuilder();
          sb.append(inputBuffer[4]).append(inputBuffer[5]).append(inputBuffer[6]).append(inputBuffer[7]);
          int jid = Integer.parseInt(sb.toString());
          sb.setLength(0);
          sb.append(inputBuffer[8]).append(inputBuffer[9]).append(inputBuffer[10]).append(inputBuffer[11]);
          int ttl = Integer.parseInt(sb.toString());
          sb.setLength(0);
          sb.append(inputBuffer[12]).append(inputBuffer[13]).append(inputBuffer[14]).append(inputBuffer[15]);
          int tll = Integer.parseInt(sb.toString());
          sb.setLength(0);
          PCB pcb = new PCB(jid, ttl, tll);
          JID = pcb.JID;
          TTL = pcb.TLL;
          TLL = pcb.TLL;
          PTR = ALLOCATE();
          int row = PTR;
          int col = 0;
          while (row < PTR + 10) {
            PT[row][col] = '0';
            row++;
          }
          while (row < PTR + 10) {
            col = 1;
            PT[row][col] = '*';
            row++;
          }
          while (row < PTR + 10) {
            col = 2;
            PT[row][col] = '*';
            row++;
          }
          continue;
        }

        else if (inputBuffer[0] == 'G' && inputBuffer[1] == 'D' || inputBuffer[0] == 'P' && inputBuffer[1] == 'D'
            || inputBuffer[0] == 'L' && inputBuffer[1] == 'R' || inputBuffer[0] == 'S' && inputBuffer[1] == 'R'
            || inputBuffer[0] == 'B' && inputBuffer[1] == 'T' || inputBuffer[0] == 'C' && inputBuffer[1] == 'R'
            || inputBuffer[0] == 'H') {
          BA = ALLOCATE();
          int digit = BA / 10;
          int row = PTR;
          String cc = Integer.toString(digit);
          if (cc.length() == 1) {
            PT[row][2] = cc.charAt(0);
            PT[row][1] = '0';
          } else {
            PT[row][1] = cc.charAt(0);
            PT[row][2] = cc.charAt(1);
          }

          String tem = new String(inputBuffer);
          System.out.println(tem);

          for (int k = BA; k < BA + 10;) {
            for (int j = 0; j < strLine.length();) {
              M[k][j % 4] = tem.charAt(j);
              j++;
              if (j % 4 == 0)
                k++;
            }
          }
        }

        else if (inputBuffer[0] == '$' && inputBuffer[1] == 'D' && inputBuffer[2] == 'T' && inputBuffer[3] == 'A') {
          PTR++;
          EXECUTE_USER_PROGRAM();
          continue;
        } else if (inputBuffer[0] == '$' && inputBuffer[1] == 'E' && inputBuffer[2] == 'N' && inputBuffer[3] == 'D') {
          System.out.println("\n\n\n");
          SI = 3;
          MOS(inputBuffer);
          continue;
        }
        if (IC == 300) { // abort;
          System.out.println("Abort due to exceed M usage");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int ALLOCATE() {
    int size = 30;

    ArrayList<Integer> list = new ArrayList<Integer>(size);
    for (int i = 0; i < size; i++) {
      list.add(i);
    }

    int temp = 0;

    Random rand = new Random();
    while (list.size() > 0) {
      int index = rand.nextInt(list.size());
      temp = list.remove(index);
    }

    return temp * 10;
  }

  public void START_EXECUTION() throws Exception {
    IC = 00;
    EXECUTE_USER_PROGRAM();
  }

  public void init() {
    BA = 0;
    M = new char[300][4];
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
