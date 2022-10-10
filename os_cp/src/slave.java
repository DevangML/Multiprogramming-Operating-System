public class slave {
    char[][] M = new char[100][4];
    char[] IR = new char[4];
    char[] R = new char[4];
    int SI = 0;
    int ic = 0;
    char c;
    File file; // For storing file location

    public master(char[][] M, char[] IR, char[] R, File file) {
        this.M = M;
        this.IR = IR;
        this.R = R;
        this.file = file; // For getting file location from main()
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
}

