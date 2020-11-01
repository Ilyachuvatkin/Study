package client;

import java.io.*;
import java.util.ArrayList;

public class History {
    private FileReader inFile;
    private FileWriter outFile;
    private String nickname;
    private File file;

    public History(String nickname) {
        this.nickname = nickname;
        String nameFileLog = "client\\src\\main\\java\\history_"+nickname+".txt";
        file = new File(nameFileLog);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        openFile(nameFileLog);
    }

    ArrayList<String> getOldHistory(int amountMessage) {
        BufferedReader inFileBuf = new BufferedReader(inFile);
        String str;
        ArrayList<String> oldMessages = new ArrayList<>();
        try {
            inFileBuf.mark(8191);
            long amountAllMessage = inFileBuf.lines().count();
            inFileBuf.reset();
            while ((str = inFileBuf.readLine()) != null) {
                if (amountAllMessage <= amountMessage) {
//                    textArea.appendText(str + "\n");
                    oldMessages.add(str);
                }
                --amountAllMessage;
                if (amountAllMessage == 0) {
                    break;
                }
            }
            inFileBuf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oldMessages;
    }

    void changeNameFileLog (String newName) {
        String newNameFileLog = "client\\src\\main\\java\\history_"+newName+".txt";
        closeFile();
        file.renameTo(new File(newNameFileLog));
        openFile(newNameFileLog);
    }

    void writeToLog(String str) {
        try {
            outFile.write(str+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeFile() {
        try {
            inFile.close();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void openFile(String newNameFileLog) {
        try {
            inFile = new FileReader(newNameFileLog);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outFile = new FileWriter(newNameFileLog,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
