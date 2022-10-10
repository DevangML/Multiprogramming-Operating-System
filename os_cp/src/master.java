import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class master {

  byte[][] M = new byte[100][4];
  byte[] IR = new byte[4];
  byte[] R = new byte[4];
  int SI = 0;
  int ic = 0;
  byte c;
  File file; // For storing file location

  public master(byte[][] M, byte[] IR, byte[] R, File file) {
    this.M = M;
    this.IR = IR;
    this.R = R;
    this.file = file; // For getting file location from main()
  }

  public void terminate() {
    Writer output;
    output = new BufferedWriter(new FileWriter(file, true));
    output.append('\n');
    output.append('\n');
    output.close();
    load();
  }

  public void load(row,col) {
    M = 0;
    BufferedReader br = new BufferedReader(new FileReader(file));

    while(br.readLine() != null) {
      if (br.readLine() == "PD" || "GD") {
        if (M == 100) {
          // Alert that memory full
          break;
        }
        for (int i = 0; i<=9; i++) {
          for (int j = 0; j<4; j++){
            M[row][col++] = br.readLine();
          }
          row++;
        }
      }

      else {
        if (br.readLine() == "$AMJ")
          break;
      }
      else if (br.readLine() == "$DTA") {
        start_execution();
      }
      if (br.readLine() == "$END")
          break;

      M = M + 10;
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
}

