package server;

import java.sql.*;

public class SimpleAuthService implements AuthService {
    Connection connection;
    PreparedStatement preparedStatement;


    public SimpleAuthService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String nickname = null;
        try {
            preparedStatement = connection.prepareStatement("Select nickname from users where login = ? AND password = ?");
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nickname = rs.getString("nickname");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println(nickname);
        return nickname;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            preparedStatement = connection.prepareStatement("Select nickname from users where login = ? or nickname = ?");
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,nickname);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO users(login, password, nickname) VALUES (?,?,?)");
                preparedStatement.setString(1,login);
                preparedStatement.setString(2,nickname);
                preparedStatement.setString(3,password);
                preparedStatement.executeUpdate();
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean changeNickname(String nickname, String newNickname) {
        try {
            preparedStatement = connection.prepareStatement("Select nickname From users Where  nickname = ?");
            preparedStatement.setString(1,newNickname);
            ResultSet st = preparedStatement.executeQuery();
            if (!st.next()) {
              preparedStatement = connection.prepareStatement("Update users Set nickname = ? Where nickname = ?");
              preparedStatement.setString(1,newNickname);
              preparedStatement.setString(2,nickname);
              preparedStatement.executeUpdate();
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
