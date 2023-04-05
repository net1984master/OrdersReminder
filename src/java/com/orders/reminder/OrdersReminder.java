package com.orders.reminder;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class OrdersReminder {
    final static private Integer port=getPort();
    final static private Integer maxRequest=3;
    final static private String mappngView="/order";
    private static TranslucentCircularDialog translucentCircularDialog=null;
    private static TranslucentCircularDialog translucentCircularDialogMarket=null;

    public static void main(String[] args) throws Exception{
        HttpServer server=HttpServer.create();
        server.bind(new InetSocketAddress(port),maxRequest);

        HttpContext context=server.createContext(mappngView,new EchoHandler());

        server.setExecutor(null);
        server.start();
    }


     static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Integer countOrders=getCountNumber(httpExchange.getRequestURI().getQuery(), "count");
            Integer countMarket=getCountNumber(httpExchange.getRequestURI().getQuery(), "market");
            String msg=getMsg(httpExchange.getRequestURI().getQuery());
            Boolean alarm= areAlarm(httpExchange.getRequestURI().getQuery());
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
            if(msg != null && !msg.trim().isEmpty()){
                Message messages=new Message(new JFrame(), "Превышение лимита", "<html> "+msg+" </html>");
                return;
            }


            if (countMarket != -1) {
                if (translucentCircularDialogMarket != null) {
                    translucentCircularDialogMarket.dispose();
                }
                if (countMarket != 0) {
                    translucentCircularDialogMarket = new TranslucentCircularDialog(countMarket, false, 70, 70, 0, -70, Color.yellow, Color.black);
                }
            }

            if (countOrders != -1) {
                if (translucentCircularDialog != null) {
                    translucentCircularDialog.dispose();
                }
                if (countOrders != 0) {
                    translucentCircularDialog = new TranslucentCircularDialog(countOrders, alarm, 70, 70, 0, 0, Color.blue, Color.white);
                }
            }
        }
    }

    public static Integer getCountNumber(String query, String desiredParam){
        Integer v_ret=0;
        for(String param:query.split("&")){
            String v_pair[]=param.split("=");
            if(v_pair[0].toLowerCase().equals(desiredParam)){
                v_ret=Integer.parseInt(v_pair[1].trim());
            }
        }
        return v_ret;
    }
    public static String getMsg(String query){
        String v_ret="";
        for(String param:query.split("&")){
            String v_pair[]=param.split("=",2);
            if(v_pair[0].toLowerCase().equals("msg")){
                v_ret=v_pair[1];
            }
        }
        return v_ret;
    }
    public static Boolean areAlarm(String query){
        for(String param:query.split("&")){
            String v_pair[]=param.split("=");
            if(v_pair[0].toLowerCase().equals("alarm") && !v_pair[1].trim().equals("0")){
                return true;
            }
        }
        return false;
    }

    private static Integer getPort(){
        Properties prop = new Properties();
        InputStream input = null;
        Integer v_ret=null;
        try {

            input = new FileInputStream("app.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            v_ret=Integer.parseInt(prop.getProperty("port"));
        } catch (IOException ex) {
            //ex.printStackTrace();
            v_ret=null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return v_ret;
    }
}
