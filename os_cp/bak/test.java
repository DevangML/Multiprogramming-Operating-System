package phase;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter; 
import java.util.*; 
import java.io.BufferedReader;
import java.io.FileInputStream;

class Phase1{
  	// variables
	public char[] IR = new char[4]; // 
	public int IC; // 
	public char[] R = new char[4]; //
	public boolean C = false; //Program Counter
	public int SI=0; //
	public char[][] memory = new char[100][4];
	public String[] inputBuffer = new String[40];
	public int data_index=0;
	public String time, id, lines_printed;
	//file operations
	File outputFile = new File("output.txt");
	
	//MOS functions
	
	public void print_memory() throws IOException{
		FileWriter fr = new FileWriter("output.txt");
		for(int i=0;i<100;i++){
			for(int j=0;j<4;j++)
			{
				try {
					fr.write(memory[i][j]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fr.write("\n");
		}
		fr.close();
	}
	
	public void GD(int address){
		int i=0,j=0;
		String lineNum = inputBuffer[data_index];
		System.out.println(lineNum);
		address = (address/10)*10;
		while(lineNum.charAt(i)!='\n')
		{
			memory[address][j]=lineNum.charAt(i);
			i++;
			j++;
			if(j==4){
				j=0;
				address+=1;
			}
		}
		data_index+=1;
		
	}
	
	public void PD(int address) throws Exception{
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
	}
	
	public void terminate() throws Exception{
		FileWriter fr = new FileWriter("output.txt");
	fr.write("\n\n");
	System.out.println("Program Executed");
	fr.close();
	}
	
	public void load() throws Exception {
		FileInputStream IN = new FileInputStream("input.txt");
	    BufferedReader br = new BufferedReader(new InputStreamReader(IN));
		FileWriter fr = new FileWriter("output.txt");

		int i=0;
		try {
		    String strLine=br.readLine();
		    while(strLine !=null) {
		    	inputBuffer[i]=strLine;
		    	i++;
		    	strLine=br.readLine();
		    }
		    
		}
		catch (Exception e) {
			System.out.println(e);
		}
		br.close();
		
		int counter=-1, index=0;
		while(index<i) {
			String line = inputBuffer[index];
			if(line!=null && line.startsWith("$")) {
				if(line.substring(1, 4)=="AMJ") { //control card
					fr.write("Reading Program");
					id = line.substring(4,8);
					time = line.substring(8,12);
					lines_printed = line.substring(12, 16);
					counter=0;
				}
				else if(line.substring(1, 4)=="DTA") {
					fr.write("Reading Data\n");
					counter=1;
					data_index=index+1;
					mos_startexecution();
					index=data_index-1;
				}
				else if(line.substring(1, 4)=="END") {
					counter=-1;
					C =false;
					for(int i1=0;i1<100;i1++) {
						for(int j=0;j<4;j++)
							memory[i1][j]='\0';
					}
				}
				else {
					System.out.println("Error");
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

	public void mos_startexecution() {
		IC=00;
		execute_userprogram();
	}
	
	public void execute_userprogram() {
		for(int i=0;i<Integer.parseInt(time);i++) {
			IR = memory[IC];
			IC+=1;
			if(IC==10) {
				IC+=10;
			}
			String instruction = ""+IR[0]+IR[1];
			int operand= (IR[2]*10)+IR[3];
			if(instruction=="LR") R = memory[operand];
			else if(instruction=="SR") memory[operand] = R;
			else if(instruction=="CR") {
				if(memory[operand]==R) C=true;
				else C = false;
			}
			else if(instruction=="BT") {
				if(C) {
					IC=operand;
				}
			}
			else if(instruction=="GD") {
				SI = 1;
				try {
					master_mode();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(instruction=="PD") {
				SI = 2;
				try {
					master_mode();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(instruction=="H\0") {
				SI=3;
				try {
					master_mode();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	public void master_mode() throws Exception {
		int operand = (IR[2]*10)+IR[3];
		if(SI==1) {
			GD(operand);
		}
		else if(SI==2) {
			PD(operand);
		}
		else if(SI==3) {
			terminate();
		}
	}
	
	public static void main(String [] args){

	Phase1 phase1 = new Phase1();
	try {
		phase1.load();
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	}