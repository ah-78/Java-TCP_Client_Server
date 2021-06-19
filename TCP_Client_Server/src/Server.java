 import java.io.*;
import java.net.*;
 import java.nio.file.Files;
 import java.nio.file.Path;

 class TCPServer{
    public static int Activec=0;

    public static void main(String argv[]) throws Exception
    {
        //Server Socket Creation
        int s= 6787;
        ServerSocket ssocket = null;
        try{
            ssocket = new ServerSocket(s);
            //Wait for Connections
            while(true){
                if(Activec == 0){
                    System.out.println("Waiting For Client on Port: " +   s);
                }
                Socket csocket = null;
                try{
                    csocket = ssocket.accept();
                    Activec = Activec + 1;


                }
                catch(Exception e){
                    csocket.close();
                    e.printStackTrace();
                }

                System.out.println("Got connection from " + csocket.getInetAddress() + ":" + csocket.getPort());
                System.out.println("Active Connections: " + Activec);

                DataInputStream is = new DataInputStream(csocket.getInputStream());
                DataOutputStream os = new DataOutputStream(csocket.getOutputStream());

                String m = "Welcome  to the server\n" + ssocket.getInetAddress().toString() + ":" + Integer.toString(s);
                os.writeUTF(m);

                Thread t = new ClientHandler(csocket, is, os);
                t.start();

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
}
