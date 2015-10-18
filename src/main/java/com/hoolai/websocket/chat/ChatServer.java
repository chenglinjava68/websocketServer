package com.hoolai.websocket.chat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.hoolai.websocket.chat.data.ChatDecoder;
import com.hoolai.websocket.chat.data.ChatEncoder;
import com.hoolai.websocket.chat.data.ChatMessage;
import com.hoolai.websocket.chat.data.basic.NewUserMessage;
import com.hoolai.websocket.chat.data.basic.UserSignoffMessage;
import com.hoolai.websocket.chat.data.structure.ChatUpdateMessage;
import com.hoolai.websocket.chat.data.structure.UserListUpdateMessage;

@ServerEndpoint(
	value = "/chat-server",
	subprotocols={"chat"},
	decoders = {ChatDecoder.class},
	encoders = {ChatEncoder.class},
	configurator = ChatServerConfigurator.class
)
public class ChatServer {
	
    private static final String USERNAME_KEY = "username";
    private static final String USERNAMES_KEY = "usernames";
    
    private Session session;
    private ServerEndpointConfig endpointConfig;
    private Transcript transcript;
    
    @OnOpen
    public void startChatChannel(EndpointConfig config, Session session) {
    	this.session = session;
    	
        //this.endpointConfig = (ServerEndpointConfig) config;
    	this.endpointConfig = getShareServerEndpointConfig(config);
    	
        ChatServerConfigurator csc = (ChatServerConfigurator) endpointConfig.getConfigurator();
        this.transcript = csc.getTranscript();
    }
    
    /**
     * we need this method because of the tomcat who supported WebSocket protocol 
     * Wraps the shared ServerEndpointConfig to be a WsPerSessionServerEndpointConfig 
     * which the map returned by {@link #getUserProperties()} is unique to this instance
     * 
     * @param config
     * @return
     */
    private ServerEndpointConfig getShareServerEndpointConfig(EndpointConfig config){
    	Class<?> secClazz = config.getClass();
    	Field perEndpointConfigField = null;
    	try {
			perEndpointConfigField = secClazz.getDeclaredField("perEndpointConfig");
			perEndpointConfigField.setAccessible(true);
			return (ServerEndpointConfig)perEndpointConfigField.get(config);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
    	throw new UnsupportedOperationException("Tomcat can not get Share ServerEndpointConfig");
    }

	@OnMessage
	public void handleChatMessage(ChatMessage message) {
		switch (message.getType()) {
		case NewUserMessage.USERNAME_MESSAGE:
			processNewUser((NewUserMessage) message);
			break;
		case ChatMessage.CHAT_DATA_MESSAGE:
			processChatUpdate((ChatUpdateMessage) message);
			break;
		case ChatMessage.SIGNOFF_REQUEST:
			processSignoffRequest((UserSignoffMessage) message);
			break;
		}
	}
    
    void processNewUser(NewUserMessage message) {
        String newUsername = validateUsername(message.getUsername());
        NewUserMessage uMessage = new NewUserMessage(newUsername);
        try {
            session.getBasicRemote().sendObject(uMessage);
        } catch (IOException | EncodeException ioe) {
            System.out.println("Error signing " + message.getUsername() + " into chat : " + ioe.getMessage());
        } 
        registerUser(newUsername);
        broadcastUserListUpdate();
        addMessage(" just joined.");
    }

    void processChatUpdate(ChatUpdateMessage message) {
        addMessage(message.getMessage());
    }

    void processSignoffRequest(UserSignoffMessage drm) {
        addMessage(" just left.");
        removeUser(false);
    }
    
    @OnError
    public void myError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }
    
    @OnClose
    public void endChatChannel() {
        if (getCurrentUsername() != null) {
            addMessage(" just left...without even signing out !");
            removeUser(true);
        }
    }

    private String getCurrentUsername() {
        return (String) session.getUserProperties().get(USERNAME_KEY);
    }
    
    private void registerUser(String username) {
        session.getUserProperties().put(USERNAME_KEY, username);
        updateUserList();
    }
    
    @SuppressWarnings("unchecked")
	private List<String> getUserList() {
        List<String> userList = (List<String>) endpointConfig.getUserProperties().get(USERNAMES_KEY);
        return (userList == null) ? new ArrayList<String>() : userList;
    }

    
    private String validateUsername(String newUsername) {
        if (getUserList().contains(newUsername)) {
            return validateUsername(newUsername + "1");
        }
        return newUsername;
    }

    private void broadcastUserListUpdate() {
        UserListUpdateMessage ulum = new UserListUpdateMessage(getUserList());
        for (Session nextSession : session.getOpenSessions()) {
            try {
                nextSession.getBasicRemote().sendObject(ulum);
            } catch (IOException | EncodeException ex) {
                System.out.println("Error updating a client : " + ex.getMessage());
            }
        }
    }

    private void removeUser(boolean isClosed) {
        try {
        	session.getUserProperties().remove(USERNAME_KEY);
            updateUserList();
            broadcastUserListUpdate();
            if(!isClosed){
            	session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "User logged off"));
            }
        } catch (IOException e) {
            System.out.println("Error removing user");
        }
    }
    
    private void updateUserList() {
        List<String> usernames = new ArrayList<>();
        for (Session s : session.getOpenSessions()) {
            String uname = (String) s.getUserProperties().get(USERNAME_KEY);
            if(uname == null){
            	continue;
            }
            usernames.add(uname);
        }
        endpointConfig.getUserProperties().put(USERNAMES_KEY, usernames);
    }
    
    private void addMessage(String message) {
        transcript.addEntry(getCurrentUsername(), message);
        broadcastTranscriptUpdate();
    }

    private void broadcastTranscriptUpdate() {
        for (Session nextSession : session.getOpenSessions()) {
            ChatUpdateMessage cdm = new ChatUpdateMessage(transcript.getLastUsername(), transcript.getLastMessage());
            try {
                nextSession.getBasicRemote().sendObject(cdm);
            } catch (IOException | EncodeException ex) {
                System.out.println("Error updating a client : " + ex.getMessage());
            }   
        }
    }

}
