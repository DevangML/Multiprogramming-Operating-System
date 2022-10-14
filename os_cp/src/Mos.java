package phase;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class Mos {
  public char[][] M = new char[100][4];
  public char[] IR = new char[4];
  public char[] R = new char[4];
  public int SI = 0;
  public int IC;
  public boolean C = false;
  public String[] inputBuffer = new String[40];
  public int data_index = 0;
  public String time, id, lines_printed;
  public File file; // For storing file location

  public Mos(File file) {
    this.file = file; // For getting file location from main
  }

  public void OUTPUT() throws IOException {
        FileWriter fr new FileWriter("output.txt");
        for (int i = 0; i<100; i++) {
            for (int j = 0; j<4; j++) {
                try {
                    fr.write(M[i][j]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        fr.close();
    }

  public void EXECUTE_USER_PROGRAM() {

    for (int i = 0; i < Integer.parseInt(time); i++) {
      IR = M[IC]; // loading current instruction into IR
      IC += 1;

      if (IC == 10) {
        IC += 10; // doubt
      }

      String instruction = "" + IR[0] + IR[1];
      int operand = (IR[2] * 10) + IR[3];

      if (instruction == "LR")
        R = M[operand];

      else if (instruction == "SR")
        M[operand] = R;

      else if (instruction == "CR") {
        if (M[operand] == R)
          C = true;
        else
          C = false;
      }

      else if (instruction == "BT") {
        if (C) {
          IC = operand;
        }
      }

      else if (instruction == "GD") {
        SI = 1;
        try {
          MASTER();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      else if (instruction == "PD") {
        SI = 2;
        try {
          MASTER();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      else if (instruction == "H\0") {
        SI = 3;
        try {
          MASTER();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      }
    }
  }

  public void READ(int address) {
    int i = 0, j = 0;
    String lineNum = inputBuffer[data_index];
    System.out.println(lineNum);
    address = (address / 10) * 10;
    // address base address && j is col number
    while (lineNum.charAt(i) != '\n') {
      M[address][j] = lineNum.charAt(i);
      i++; // Character counter for buffer
      j++;
      if (j == 4) {
        j = 0;
        address += 1; // M row number
      }
    }
    data_index += 1; // to understand data_index and address
    EXECUTE_USER_PROGRAM();

    // confusion between address and row number
  }

  public void WRITE(int address) throws Exception {
    FileWriter fr = new FileWriter("output.txt");
    address = (address / 10) * 10; // base address
    for (int i = address; i <= address + 10; i++) {
      for (int j = 0; j < 4; j++) {
        if (M[i][j] == '\0') {
          break;
        }
        fr.write(M[i][j]);
      }
    }
    fr.write('\n');
    fr.close();
    EXECUTE_USER_PROGRAM();
  }

  public void TERMINATE() throws Exception {
    Writer output;
    output = new BufferedWriter(new FileWriter("output.txt", true));
    output.append('\n');
    output.append('\n');
    System.out.println("Program Executed");
    output.close();
    LOAD();
  }

  public void LOAD() throws Exception {
    FileInputStream IN = new FileInputStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(IN));
    FileWriter fr = new FileWriter("output.txt");

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
        START_EXECUTION(); // It works according to counter value
        index = data_index - 1;
      }

      else if (line.substring(1,4) == "END") {
        counter = -1;
        C = false;
        for(int i1 = 0; i1<100; i1++) {
          for (int j = 0; j<4; j++) {
            M[i1][j] = '\0';
          }
        }
      }
      
      else {
        System.out.println("Error in file");
      }
    }
    else {
				if(counter==0) {
					i=0;
					for(int m=0;m<Integer.parseInt(time);m++) {
						if(line.charAt(i)=='H') {
							memory[m][0]=line.charAt(i);
							i+=1;
						}
						else {
							for(int kl=i;kl<i+4;kl++) {
								memory[m][kl-i] = line.charAt(kl);
							}
							i+=4;
						}
					}
				}
			}
      index+=1;
    }
    fr.close();
}

  public void START_EXECUTION() {
    IC = 00;
    EXECUTE_USER_PROGRAM();
  }

  public void MASTER() throws Exception {
    int operand = (IR[2] * 10) + IR[3];
    if (SI == 1) {
      READ(operand);
    } else if (SI == 2) {
      WRITE(operand);
    } else if (SI == 3) {
      TERMINATE();
    }
  }
}
