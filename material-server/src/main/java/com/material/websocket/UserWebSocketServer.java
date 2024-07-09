package com.material.websocket;


import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/user/{sid}")
public class UserWebSocketServer {

    //存放user端会话
    private static Map<String, Session> userSessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("用户：" + sid + "建立连接");
        userSessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        // TODO 将需要发送的信息存储在数据库中，以便上线后可以看到，并且有记录
        System.out.println("收到来自用户：" + sid + "的信息:" + message);
        // 解析消息，找到目标管理员
        String[] parts = message.split(":", 2);
        if (parts.length == 2) {
            String targetUserId = parts[0];
            String userMessage = parts[1];
            AdminWebSocketServer.sendMessageToAdmin(targetUserId, userMessage);
        }
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("用户连接断开:" + sid);
        userSessionMap.remove(sid);
    }

    /**
     * 群发给用户
     *
     * @param message
     */
    public static void sendToAllUser(String message) {
        Collection<Session> sessions = userSessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息(getAsyncRemote是非阻塞式的，getBasicRemote是阻塞式的)
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给特定User发消息
     * @param sid
     * @param message
     */
    public static void sendMessageToUser(String sid, String message) {
        Session session = userSessionMap.get(sid);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
                System.out.println("向用户 " + sid + " 发送消息: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("用户 " + sid + " 未连接");
        }
    }

}
