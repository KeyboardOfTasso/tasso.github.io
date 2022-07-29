package com.thx.service;

import com.thx.qqcommon.Message;
import com.thx.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MessageClientService {
    public void sendMessageToAll(String content, String userId) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(userId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(userId + " 对大家说：" + content);
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToOne(String content, String senderId, String getterId) {
        //构建message
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setReceiver(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message对象
        System.out.println(senderId + " 对 " + getterId + " 说 " + content);
        //发送给服务端

        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
