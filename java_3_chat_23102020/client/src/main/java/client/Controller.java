package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<String> clientList;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    @FXML
    private HBox authPanel;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox msgPanel;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private FileReader inFile;
    private FileWriter outFile;

    private Stage stage;
    private Stage regStage;
    private RegController regController;

    private boolean authenticated;
    private String nickname;

    private void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);

        if (!authenticated) {
            nickname = "";
            setTitle("Балабол");
        } else {
            setTitle(String.format("[ %s ] - Балабол", nickname));
        }
        textArea.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) textField.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                System.out.println("bye");
                if (socket != null && !socket.isClosed()) {
                    try {
                        out.writeUTF("/end");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        setAuthenticated(false);
        createRegWindow();
    }


    private void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/authok ")) {
                            nickname = str.split("\\s")[1];
                            setAuthenticated(true);
                            break;
                        }

                        if (str.startsWith("/regok")) {
                            regController.addMessageTextArea("Регистрация прошла успешно");
                        }
                        if (str.startsWith("/regno")) {
                            regController.addMessageTextArea("Зарегистрироватся не удалось\n" +
                                    " возможно такой логин или никнейм уже заняты");
                        }

                        if (str.startsWith("/end")) {
                            throw new RuntimeException("disconnected by timeout");
                        }
                        textArea.appendText(str + "\n");
                    }
                    String nameFileLog = "client\\src\\main\\java\\history_"+nickname+".txt";
                    File file = new File(nameFileLog);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    inFile = new FileReader(nameFileLog);
                    BufferedReader inFile2 = new BufferedReader(inFile);
                    outFile = new FileWriter(nameFileLog,true);

                    String str1;
                    int amountMessage = 500;
                    while ((str1 = inFile2.readLine()) != null) {
                        textArea.appendText(str1 + "\n");
                        --amountMessage;
                        if (amountMessage == 0) {
                            break;
                        }
                    }

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                break;
                            }
                            if (str.startsWith("/chgOk")) {
                                String[] token = str.split("\\s"); // nickname = str.split(" ")[1];
                                setTitle(String.format("[ %s ] - Балабол", token[1]));
                                String newNameFileLog = "client\\src\\main\\java\\history_"+token[1]+".txt";
                                inFile.close();
                                outFile.close();
                                file.renameTo(new File(newNameFileLog));
                                inFile = new FileReader(newNameFileLog);
                                outFile = new FileWriter(newNameFileLog,true);
                                System.out.println();
                            }
                            if (str.startsWith("/clientlist ")) {
                                String[] token = str.split("\\s");
                                Platform.runLater(() -> {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < token.length; i++) {
                                        clientList.getItems().add(token[i]);
                                    }
                                });
                            }
                        } else {
                            if (!str.equals("\n")) {
                                outFile.write(str+"\n");
                            }
                            textArea.appendText(str + "\n");
                        }
                    }
                    inFile.close();
                    outFile.close();
                    inFile2.close();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    setAuthenticated(false);
                    try {
                        socket.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(ActionEvent actionEvent) {
        if (textField.getText().trim().length() == 0) {
            return;
        }
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        String msg = String.format("/auth %s %s",
                loginField.getText().trim(), passwordField.getText().trim());
        try {
            out.writeUTF(msg);
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitle(String title) {
        Platform.runLater(() -> {
            stage.setTitle(title);
        });
    }

    public void clickClientList(MouseEvent mouseEvent) {
        textField.setText(String.format("/w %s ", clientList.getSelectionModel().getSelectedItem()));
    }

    public void releasedMouseClientList(MouseEvent mouseEvent) {
        System.out.println(clientList.getSelectionModel().getSelectedItem());
        System.out.println(mouseEvent.getButton());
        System.out.println(mouseEvent.getClickCount());
    }

    private void createRegWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reg.fxml"));
            Parent root = fxmlLoader.load();
            regStage = new Stage();
            regStage.setTitle("Регистрация в чате Балабол");
            regStage.setScene(new Scene(root, 400, 300));
            regStage.initModality(Modality.APPLICATION_MODAL);

            regController = fxmlLoader.getController();
            regController.setController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void regStageShow(ActionEvent actionEvent) {
        regStage.show();
    }

    public void tryRegistration(String login, String password, String nickname) {
        String msg = String.format("/reg %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeNick(ActionEvent actionEvent) {
        textField.setText(String.format("/chg %s ",nickname));

    }
}
