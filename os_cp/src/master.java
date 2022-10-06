import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class master {

  String[][] M = new String[100][4];
  String[] IR = new String[4];
  String[] R = new String[4];
  int SI = 0;
  int ic = 0;
  String c;
  String[][] buffer = new String[10][4];
  String file; // For storing file location

  public master(String[][] M, String[] IR, String[] R, String file) {
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

}
