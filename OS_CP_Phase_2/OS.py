import random
import os
os.chdir(r'D:\ISHAN\TY_AI-A_Sem 5\OS\CP\OS-CP\OS_CP_Phase_2')

m = 0
IR = [0 for i in range(4)]
IC = [0 for i in range(2)]
R = [0 for i in range(4)]
C = False
SI = 0
PI = 0
TI = 0
PTR = [0 for i in range(4)]

used_frames = set()

M = [['\0' for i in range(4)] for j in range(300)]
opfile = open('output.txt', 'w')
input_buffer = []  # size is 40 bytes
data_index = 0
pd_error = 0
gd_error = 0


class PCB:
    def __init__(self, job_id, ttl, tll, ttc, llc):
        self.job_id = job_id
        self.TTL = ttl   # total time limit
        self.TTC = ttc   # total time counter
        self.TLL = tll   # total line limit
        self.LLC = llc   # line limit counter

    def incrementLLC(self):
        self.LLC = self.LLC + 1

    def incrementTTC(self):
        self.TTC = self.TTC + 1


def print_memory():
    for i in range(300):
        for j in range(4):
            print(M[i][j], end=" ", file=opfile)
        print("\n", file=opfile)


def READ(address):
    global data_index, gd_error
    i, j = 0, 0
    line = input_buffer[data_index]
    if (line[0] == "$" and line[1] == "E"):  # checking for out of data error
        gd_error = -1
        return
    address = (address // 10) * 10
    while (line[i] != '\n'):          # adding data to the M at received address
        M[address][j] = line[i]
        i += 1
        j += 1
        if (j == 4):
            j = 0
            address += 1
    data_index += 1


def WRITE(address):
    global pd_error
    if (pcb.LLC >= pcb.TLL):  # checking for line limit exceeded
        pd_error = -1
        return
    address = (address // 10) * 10

    for i in range(address, address + 10):   # printing entire block to file
        for j in range(4):
            if (M[i][j] == '\0'):
                break
            print(M[i][j], end="", file=opfile)
    print('\n', file=opfile)
    pcb.incrementLLC()


def TERMINATE(EM):
    JID = pcb.job_id
    print("JOB ID : ", JID, file=opfile)
    if (EM == 0):
        print("\tProgram Executed Sucessfully", file=opfile)
    elif (EM == 1):
        print("\tOut of Data Error", file=opfile)
    elif (EM == 2):
        print("\tLine Limit Exceeded", file=opfile)
    elif (EM == 3):
        print("\tTime Limit Exceeded", file=opfile)
    elif (EM == 4):
        print("\tOperation Code Error", file=opfile)
    elif (EM == 5):
        print("\tOperand Error", file=opfile)
    elif (EM == 6):
        print("\tInvalid Page Fault", file=opfile)
    elif (EM == 7):
        print("\tTLE with opcode error", file=opfile)
    elif (EM == 8):
        print("\tTLE with operand error", file=opfile)

    print("IC     : ", IC, file=opfile)
    print("IR     : ", IR, file=opfile)
    print("TTC    : ", pcb.TTC, file=opfile)
    print("LLC    : ", pcb.LLC, file=opfile)
    print("TTL    : ", pcb.TLL, file=opfile)
    print("TTL    : ", pcb.TTL, file=opfile)
    print("\n", file=opfile)


def ALLOCATE():
    frame = (random.randint(0, 29))
    return frame


def LOAD():
    with open("input.txt", "r") as file:
        global input_buffer, M

        input_buffer = file.readlines()
        counter = -1
        index = 0
        while (index < len(input_buffer)):
            global data_index, pcb, id, time, lines_printed, PTR, used_frames, M, C, IR, IC, R, SI, PI, TI, pd_error, gd_error
            line = input_buffer[index]
            if (line[0].startswith('$')):
                if (line[1:4] == 'AMJ'):  # control card
                    id = line[4:8]
                    time = line[8:12]
                    lines_printed = line[12:16]
                    counter = 0
                    pcb = PCB(id, int(time), int(lines_printed), 0,
                              0)  # initialize PCB
                    # initialise frame for page table
                    frame_num = ALLOCATE()
                    used_frames.add(frame_num)   # add frame to used frames set
                    frame_num *= 10
                    # initialise page table register
                    PTR[1] = frame_num // 100
                    frame_num = frame_num % 100
                    PTR[2] = frame_num // 10
                    PTR[3] = frame_num % 10

                elif (line[1:4] == 'DTA'):
                    counter = 1
                    data_index = index + 1  # index of where data address begins
                    START_EXECUTION()
                    index = data_index - 1

                elif (line[1:4] == 'END'):
                    counter = -1
                    print("new prog")
                    # resetting all globals
                    C = False
                    M = [['\0' for i in range(4)] for j in range(300)]

                    IR = [0 for i in range(4)]
                    IC = [0 for i in range(2)]
                    R = [0 for i in range(4)]
                    C = False
                    SI = 0  # need to be looked
                    pd_error = 0
                    gd_error = 0
                    PI = 0
                    TI = 0
                    PTR = [0 for i in range(4)]
                    used_frames.clear()
                    counter = -1
                else:
                    print('error in input')
                    exit(-1)

            else:
                if (counter == 0):  # for reading instructions
                    # initialising frame for program
                    frame_num_prog = ALLOCATE()
                    while frame_num_prog in used_frames:  # finding unique frame
                        frame_num_prog = ALLOCATE()
                    used_frames.add(frame_num_prog)

                    pt_num = int(PTR[1]) * 100 + int(PTR[2]) * 10 + int(
                        PTR[3])  # updating page table entry

                    M[pt_num][2] = frame_num_prog // 10
                    M[pt_num][3] = frame_num_prog % 10
                    M[pt_num][0] = 0
                    M[pt_num][1] = 0

                    i = 0
                    # print(int(time))

                    frame_num_p = frame_num_prog * 10  # address where program is stored

                    while (i < len(line)):

                        if (line[i] == 'H'):

                            M[frame_num_p][0] = line[i]
                            i += 1  # since H has no operands associated with it
                            frame_num_p += 1
                        else:
                            M[frame_num_p][0:4] = line[i:i + 4]

                            i += 4
                            frame_num_p += 1
                        if (frame_num_p % 10 == 0):  # if frame size exceeded- assign new frame
                            frame_num_prog = ALLOCATE()
                            while frame_num_prog in used_frames:
                                frame_num_prog = ALLOCATE()
                            used_frames.add(frame_num_prog)
                            frame_num_p = frame_num_prog * 10
                            pt_num += 1  # increment page table index
                            # update page table entry
                            M[pt_num][2] = frame_num_prog // 10
                            M[pt_num][3] = frame_num_prog % 10
                            M[pt_num][0] = 0
                            M[pt_num][1] = 0
            index += 1


def START_EXECUTION():
    IC[0] = 0
    IC[1] = 0
    EXECUTE_USER_PROGRAM()


def MOS(valid=False):
    global SI, TI, PI, gd_error, pd_error

    if (gd_error == -1):
        TERMINATE(1)

    if (pd_error == -1):
        TERMINATE(2)

    if (TI == 0):
        if (PI == 1):
            TERMINATE(4)  # Operation Code Error
        elif (PI == 2):
            TERMINATE(5)  # Operand Error
        elif (PI == 3):  # page fault
            if (valid):  # valid argument passed to master mode function
                valid_page_fault()
            else:
                TERMINATE(6)  # invalid page fault
        else:
            if (SI == 1):  # read function GD
                READ(ADDRESSMAP(int(IR[2]) * 10 + int(IR[3])))
            elif (SI == 2):  # write function PD
                WRITE(ADDRESSMAP(int(IR[2]) * 10 + int(IR[3])))
            elif (SI == 3):  # TERMINATE successfully
                TERMINATE(0)

    elif (TI == 2):
        if (PI == 1):
            TERMINATE(7)  # TLE with opcode error
        elif (PI == 2):
            TERMINATE(8)  # TLE with operand error
        elif (PI == 3):
            TERMINATE(3)  # Time Limit Exceeded

    else:
        if (SI == 1):
            TERMINATE(3)
        elif (SI == 2):
            WRITE(ADDRESSMAP(int(IR[2]) * 10 + int(IR[3])))
            TERMINATE(3)
        elif (SI == 3):
            TERMINATE(0)


def ADDRESSMAP(VA):

    global PTR, M
    # first we get page table entry
    pte = (int(PTR[1]) * 100 + int(PTR[2]) * 10 + int(PTR[3])) + VA // 10

    if M[pte][0] == '\0':  # checking if anything is present in page table entry
        return -1  # if not invoke page fault

    # calculate M frame entry from pte
    addr = int(M[pte][2]) * 10 + int(M[pte][3])
    RA = addr * 10 + VA % 10  # calculate real address
    return RA


def valid_page_fault():
    global used_frames, M, PTR
    frame_num_data = ALLOCATE()  # initialise a new frame for data
    while frame_num_data in used_frames:
        frame_num_data = ALLOCATE()
    used_frames.add(frame_num_data)
    # find page table index which is empty
    c_ptr = (int(PTR[1]) * 100 + int(PTR[2]) * 10 + int(PTR[3]))
    while (M[c_ptr][0] != '\0'):
        c_ptr += 1
    # add page table entry
    M[c_ptr][2] = frame_num_data // 10
    M[c_ptr][3] = frame_num_data % 10
    M[c_ptr][0] = 0
    M[c_ptr][1] = 0

    print(M[c_ptr])


def SIMULATION():
    global SI, TI
    pcb.incrementTTC()
    if (pcb.TTC > pcb.TTL):
        SI = 1
        TI = 2
        MOS()


def EXECUTE_USER_PROGRAM():
    while (1):
        global IC, IR, R, C, T, SI, TI, PI
        SI = 0
        PI = 0
        TI = 0

        # converting virtual address to real addresss
        inst_count = ADDRESSMAP(10 * IC[0] + IC[1])
        if (inst_count == -1):  # master mode - operand error
            PI = 2
            MOS()
            break

        IC[1] += 1  # incrementing IC
        if IC[1] == 10:
            IC[0] += 1
            IC[1] = 0

        IR = M[inst_count]
        print("IR", IR)

        inst = "" + IR[0] + IR[1]

        if (inst[0] != "H"):

            if ((IR[2].isnumeric() and IR[3].isnumeric()) == False):  # checking for operand error
                PI = 2
                MOS()
                break
            real_address = ADDRESSMAP(int(IR[2]) * 10 + int(IR[3]))

        if inst == "LR":
            if (real_address == -1):  # invalid page fault
                PI = 3
                MOS()
                break
            R = M[real_address]

        elif inst == "SR":
            if (real_address == -1):  # valid page fault
                PI = 3
                # decrementing IC
                if IC[1] != 0:
                    IC[1] -= 1
                else:
                    IC[1] = 9
                    IC[0] -= 1
                MOS(valid=True)
                pcb.TTC -= 1
                # pcb.TTC -= 1
                continue
            else:
                M[real_address] = R

        elif inst == "CR":

            if (real_address == -1):  # invalid page fault
                PI = 3
                MOS()
                break
            if (M[real_address] == R):
                C = True
            else:
                C = False

        elif inst == "BT":
            if (C):
                IC[0] = int(IR[2])
                IC[1] = int(IR[3])

        elif inst == "GD":
            print(inst_count, real_address)
            if (real_address == -1):  # valid page fault
                PI = 3
                SI = 0
                print('valid page fault')
                MOS(valid=True)
                if IC[1] != 0:
                    IC[1] -= 1
                else:
                    IC[1] = 9
                    IC[0] -= 1
                PI = 0
                pcb.TTC -= 1
                continue
            SI = 1
            MOS()
            if (gd_error == -1):
                MOS()
                break
        elif inst == "PD":
            if (real_address == -1):  # invalid page fault
                PI = 3
                MOS()
                break
            else:
                SI = 2
                MOS()
                if (pd_error == -1):
                    MOS()
                    break

        elif inst == "H\0":
            SI = 3
            MOS()
            break

        else:
            PI = 1
            MOS()
            break
        SIMULATION()
    pcb.incrementTTC


if __name__ == "__main__":
    LOAD()
    opfile.close()
