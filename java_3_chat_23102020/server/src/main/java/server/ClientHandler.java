package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientHandler {
    DataInputStream in;
    DataOutputStream out;
    Server server;
    Socket socket;
    Connection serverConnection;
    private String nickname;
    private String login;
//    private static final Logger logger = Logger.getLogger(Logger.class.getName());
//    private FileHandler fileHandler;

    public ClientHandler(Server server, Socket socket, Connection connection) {
        try {
//            fileHandler = new FileHandler("server/src/main/java/server/log/log.txt",true);
//            logger.addHandler(fileHandler);
//            fileHandler.setFormatter(new SimpleFormatter());

            this.server = server;
            this.socket = socket;
            serverConnection = connection;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
//            System.out.println("Client connected " + socket.getRemoteSocketAddress());
            Server.logger.info("Client connected " + socket.getRemoteSocketAddress());
            new Thread(() -> {
                try {
                    socket.setSoTimeout(5000);


                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/reg ")) {
                            String[] token = str.split("\\s");
                            if (token.length < 4) {
                                continue;
                            }
                            boolean b = server.getAuthService()
                                    .registration(token[1], token[2], token[3]);
                            if (b) {
                                sendMsg("/regok");
                            } else {
                                sendMsg("/regno");
                            }
                        }

                        if (str.startsWith("/auth ")) {
                            String[] token = str.split("\\s");
                            if (token.length < 3) {
                                continue;
                            }
                            String newNick = server.getAuthService()
                                    .getNicknameByLoginAndPassword(token[1], token[2]);
                            if (newNick != null) {
                                login = token[1];
                                if (!server.isLoginAuthenticated(login)) {
                                    nickname = newNick;
                                    sendMsg("/authok " + newNick);
                                    server.subscribe(this);
                                    socket.setSoTimeout(0);
                                    break;
                                } else {
                                    sendMsg("С этим логином уже вошли в чат");
                                }
                            } else {
                                sendMsg("Неверный логин / пароль");
                            }
                        }
                    }

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.startsWith("/chg ")) {
                                String[] token = str.split("\\s");
                                if (token.length < 3) {
                                    continue;
                                }

                                boolean isChangeNick = server.getAuthService().changeNickname(token[1],token[2]);
                                if (isChangeNick) {
                                    nickname = token[2];
                                    sendMsg("/chgOk " + token[2]);
                                    server.broadcastClientList();
                                } else {
                                    System.out.println("Введите другой ник");
                                }
                            }
                            if (str.equals("/end")) {
                                sendMsg("/end");
                                break;
                            }
                            if (str.startsWith("/w ")) {
                                String[] token = str.split("\\s", 3);
                                if (token.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, token[1], token[2]);
                            }
                        } else {
                            server.broadcastMsg(this, str);
                        }
                    }

                } catch (SocketTimeoutException e) {
                    sendMsg("/end");
//                    System.out.println("Client disconnected by timeout");
                    Server.logger.info("Client disconnected by timeout");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
//                    System.out.println("Client disconnected " + socket.getRemoteSocketAddress());
                    Server.logger.info("Client disconnected " + socket.getRemoteSocketAddress());
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

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
