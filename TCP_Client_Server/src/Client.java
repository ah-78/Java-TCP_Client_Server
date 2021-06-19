import java.io.*;
import java.net.*;
import java.util.*;

class TCPClient{
    public static void main(String argv[]) throws Exception
    {
        try {
            Scanner input = new Scanner(System.in);

            Socket clientsocket = new Socket("127.0.0.1", 6787);

            System.out.println("Client " + clientsocket.getInetAddress() + " is active");

            DataInputStream is = new DataInputStream(clientsocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientsocket.getOutputStream());
            System.out.println(is.readUTF());
            while (true) {
                System.out.println(is.readUTF());
                String in = input.nextLine();
                os.writeUTF(in);

                // If client sends exit,close this connection
                // and then break from the while loop
                if (in.equals("3")) {
                    System.out.println("Closing this connection... ");
                    clientsocket.close();
                    System.out.println("Connection closed");
                    break;
                }
                else if(in.equals("1")){
                    System.out.println("Enter Data: ");
                    in = input.nextLine();
                    os.writeUTF(in);
                }
                else if(in.equals("2")){
                    String received = is.readUTF();


                    if(!received.equals("No information found for client " + clientsocket.getInetAddress().toString())){

                        File cf = new File("client.txt");
                        FileWriter fw;
                        if (cf.exists()){
                            fw = new FileWriter(cf, true);
                        }
                        else{
                            try{
                                cf.createNewFile();
                            }
                            catch (IOException io){
                                io.printStackTrace();
                            }
                            received = is.readUTF();

                            fw = new FileWriter(cf);
                        }
                        fw.write(received);
                        fw.close();
                        System.out.println(received);
                        System.out.println("Data from server saved successfully");
                    }else{
                        System.out.println(received);
                    }




                }




            }

            // closing resources
            input.close();
            is.close();
            os.close();

        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
