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
  byte[][] buffer = new byte[10][4];
  String file; // For storing file location

  public master(byte[][] M, byte[] IR, byte[] R, String file) {
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

  public void load() {

  }

}
