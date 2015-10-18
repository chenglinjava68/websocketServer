package com.hoolai.websocket.drawing.client;


public class UIMain {
    
    public static void main(String args[]) {
        DrawingWindow wbw1 = new DrawingWindow("ws://localhost:8080/websocketServer/draw", 50, 10);
        DrawingWindow wbw2 = new DrawingWindow("ws://localhost:8080/websocketServer/draw", 500, 20);
    }  
}

