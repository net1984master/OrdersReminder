package com.orders.reminder;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
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
            Integer countOrders=getCountNumber(httpExchange.getRequestURI().getQuery());
            String msg=getMsg(httpExchange.getRequestURI().getQuery());
            Boolean alarm= areAlarm(httpExchange.getRequestURI().getQuery());
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
            if(msg != null && !msg.trim().isEmpty()){
                Message messages=new Message(new JFrame(), "Превышение лимита", "<html> "+msg+" </html>");
                return;
            }
            if(translucentCircularDialog!=null) {
                translucentCircularDialog.dispose();
            }
            if(countOrders!=0){
                    translucentCircularDialog=new TranslucentCircularDialog(countOrders,alarm);
            }
        }
    }

    public static Integer getCountNumber(String query){
        Integer v_ret=0;
        for(String param:query.split("&")){
            String v_pair[]=param.split("=");
            if(v_pair[0].toLowerCase().equals("count")){
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
