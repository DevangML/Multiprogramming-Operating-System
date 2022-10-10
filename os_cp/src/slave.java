public class slave {
  

    public void executeUserProgram(){
        //loading IR
        while (IC<10 && memory[IC][0] != '@') {
            for (int i = 0; i < 4; i++) {
                IR[i] = memory[IC][i];
            }
            IC++;
            switch (IR[0]) {
                case 'L':
                    if(IR[1] == 'R'){
                        for(int i = 0;i<4;i++){
                            R[i] = memory[(IR[2]-'0')*10 + (IR[3] -'0')][i];
                        }
                    }
                    break;
                case 'S':
                    if(IR[1] == 'R'){
                        for(int i = 0;i<4;i++){
                            memory[(IR[2]-'0')*10 + (IR[3] -'0')][i] = R[i];
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
                        if(toggle == true){
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

