package com.thx.service;

import com.thx.qqcommon.Message;
import com.thx.qqcommon.MessageType;
import com.thx.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {
    private User user = new User();
    private Socket socket;
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        user.setId(userId);
        user.setPassword(pwd);
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message)ois.readObject();
            if (msg.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                b = true;
            } else {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    public void onlineFriendList() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getId()).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getId());//一定要指定我是哪个客户端id

        //发送message
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getId() + " 退出系统 ");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
