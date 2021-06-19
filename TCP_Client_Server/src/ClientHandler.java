import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

class ClientHandler extends Thread {
    final Socket csocket;
    final DataInputStream is;
    final DataOutputStream os;


    public ClientHandler(Socket s, DataInputStream is, DataOutputStream os) {
        this.is = is;
        this.os = os;
        this.csocket = s;
    }

    public void Write(String rep, String req) throws IOException {

        File cf = new File(csocket.getInetAddress().toString() + ".txt");
        FileWriter fw;
        req = is.readUTF();
        if (cf.exists()) {
            fw = new FileWriter(cf, true);
        } else {
            try {
                cf.createNewFile();
            } catch (IOException io) {
                io.printStackTrace();
            }
            fw = new FileWriter(cf);
        }
        fw.write(req);
        fw.close();
        rep = "Information saved for client " + csocket.getInetAddress().toString();
        //os.writeUTF(rep);
        System.out.println(rep);
    }

    public void Read(String rep, String req) throws IOException {
        File cf = new File(csocket.getInetAddress().toString() + ".txt");
        if (cf.exists()) {
            rep = "Information for client " + csocket.getInetAddress().toString();
            System.out.println(rep);
            os.writeUTF(rep);
            Path fileName = Path.of(csocket.getInetAddress().toString() + ".txt");
            rep = Files.readString(fileName);
            os.writeUTF(rep);
        } else {
            rep = "No information found for client " + csocket.getInetAddress().toString();
            System.out.println(rep);
            os.writeUTF(rep);
        }

    }

    @Override
    public void run() {
        String req;
        String rep;
        while (true) {
            rep = "1.  Press 1 If you want to Write\n2. Press 2 If you want to Read\n3. Press 3 If you want to Exit\nChoice (#): ";
            try {
                os.writeUTF(rep);
                req = is.readUTF();
                if (req.equals("1")) {
                    Write(rep, req);
                } else if (req.equals("2")) {
                    Read(rep, req);
                } else if (req.equals("3")) {
                    TCPServer.Activec--;
                    System.out.println("Closing connection of client " + csocket.getInetAddress());
                    rep = "Connection closed";
                    os.writeUTF(rep);

                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}