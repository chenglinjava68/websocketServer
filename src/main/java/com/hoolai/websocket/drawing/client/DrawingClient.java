package com.hoolai.websocket.drawing.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.hoolai.websocket.drawing.data.DrawingDecoder;
import com.hoolai.websocket.drawing.data.DrawingEncoder;
import com.hoolai.websocket.drawing.data.DrawingObject;

@ClientEndpoint(
    decoders = { DrawingDecoder.class },
    encoders = { DrawingEncoder.class }
)
public class DrawingClient {
    private Session session;
    private DrawingWindow window;
     
    public DrawingClient(DrawingWindow window) {
        this.window = window;   
    }
    
    @OnOpen
    public void init(Session session) {
        this.session = session;
    }
    
    @OnMessage
    public void drawingChanged(DrawingObject drawingObject) {
        window.addDrawingObject(drawingObject);
    }
    
    public void notifyServerDrawingChanged(DrawingObject drawingObject) {
        try {
            this.session.getBasicRemote().sendObject(drawingObject);
        } catch (IOException ioe) {
            System.out.println("Error: IO " + ioe.getMessage());
        } catch (EncodeException ee) {
            System.out.println("Error encoding object: " + ee.getObject());
        }
    }
    
    @OnError
    public void handleError(Throwable thw) {
         if (thw instanceof DecodeException) {
            System.out.println("Error decoding incoming message: " + ((DecodeException)thw).getText());
        } else {
            System.out.println("Client WebSocket error: " + thw.getMessage());
        }
    }
    
    public static DrawingClient connect(DrawingWindow window, String path) {
        WebSocketContainer wsc = ContainerProvider.getWebSocketContainer();
        try {
            DrawingClient client = new DrawingClient(window);
            wsc.connectToServer(client, new URI(path));
            return client;
        } catch (IOException ioe) {
            System.out.println("Error Connecting: " + ioe.getMessage());
        } catch (DeploymentException de) {
            System.out.println("Error deploying: " + de.getMessage());   
        } catch (URISyntaxException ue) {
            System.out.println("Bad path: " + path);
        }
        return null;
    }
    
    public void disconnect() {
        if (this.session != null) {
            try  {
                this.session.close();
            } catch (IOException ioe) {
                System.out.println("Error closing the session: " + ioe);
            }
        }
    }
}
