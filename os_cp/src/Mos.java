package phase;
import kava.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class Mos {
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

  public Mos(File file) {
    this.file = file; // For getting file location from main()
  }


  public void print_memory() throws IOException {
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
            fr.write("\n");
        }
        fr.close();
    }

    public void executeUserProgram(){
        //loading IR
        while (IC<10 && M[IC][0] != '@') {
            for (int i = 0; i < 4; i++) {
                IR[i] = M[IC][i];
            }
            IC++;
            switch (IR[0]) {
                case 'L':
                    if(IR[1] == 'R'){
                        for(int i = 0;i<4;i++){
                            R[i] = M[(IR[2]-'0')*10 + (IR[3] -'0')][i];
                        }
                    }
                    break;
                case 'S':
                    if(IR[1] == 'R'){
                        for(int i = 0;i<4;i++){
                            M[(IR[2]-'0')*10 + (IR[3] -'0')][i] = R[i];
                        }
                    }
                    break;
                case 'C':
                    if (IR[1] == 'R') {
                        char a =IR[2];
                        char b =IR[3];
                        comparing(a,b);
                    }
                    break;
    
                case 'B':
                    if(IR[1] == 'T'){
                        if(C == true){
                            IC = (IR[2] - '0') *10 + (IR[3] - '0');
                        }
                    }
                    break;
    
                case 'G':
                    if (IR[1] == 'D') {
                        SI = 1;
                        start_execution();
                    }
                    break;
                case 'P':
                    if (IR[1] == 'D') {
                        SI = 2;
                        start_execution();
                    }
                    break;
                case 'H':
                    SI = 3;
                    start_execution();
                    break;
            }
        }
    }

  public void read(int address) {
        int i = 0, j = 0;
        String lineNum = inputBuffer[data_index];
        System.out.println(lineNum);
        address = (address/10)*10;
        while(lineNum.charAt(i) != '\n') {
            M[address][j] = lineNum.charAt(i);
            i++;
            j++;
            if (j==4) {
                j = 0;
                address+=1;
            }
        }
        data_index+=1; // to understand data_index and address
        executeUserProgram();
    }

  public void write(int address) throws Exception{
		FileWriter fr = new FileWriter("output.txt");
		address = (address/10)*10;
		for(int i=address;i<=address+10;i++){
			for(int j=0;j<4;j++){
				if(memory[i][j]=='\0'){
					break;
				}
				fr.write(memory[i][j]);
			}
		}
		fr.write('\n');
		fr.close();
    executeUserProgram();
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
      if (counter==0) {
        i = 0;
        for (int n = 0; n<Integer.parseInt(time); n++){
          if (line.charAt(i) == 'H')
        }
      }
    }
  }
}

  public void start_execution() {
    ic = 00;
    slave.execute_user_program();
  }

  public void master_mode() throws Exception {
    int operand = (IR[2]*10)+IR[3];
    if (SI==1) {
      read(operand);
    }
    else if (SI==2) {
      write(operand);
    }
    else if (SI==3) {
      terminate();
    }
  }
}

