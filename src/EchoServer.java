import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class EchoServer {
    public static void main(String[] args) throws IOException {

//        if (args.length != 1) {
//            System.err.println("Usage: java EchoServer <port number>");
//            System.exit(1);
//        }
//
//        int portNumber = Integer.parseInt(args[0]);

        int portNumber = 30121;

        try (
//                ServerSocket serverSocket =
//                        new ServerSocket(Integer.parseInt(args[0]));
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {

            ArrayList<Packet> packetList = new ArrayList<>();
            Random random = new Random();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                if (inputLine.equals("starting")) {

                    char lowerCaseLetterAscii = 97;
                    for (int i = 0; i < 20; i++) {
                        String lowerCaseLetter = Character.toString(lowerCaseLetterAscii);
                        lowerCaseLetterAscii++;
                        Packet packet = new Packet(lowerCaseLetter, i, false);
                        packetList.add(packet);
                    }

                    Collections.shuffle(packetList);

                    for (Packet packet : packetList) {
                        int odds = random.nextInt(100);
                        if (odds > 20) {
                            packet.setSent(true);
                            out.println(packet.toFormattedString());
                        }

                    }
                    out.println("END_OF_TRANSMISSION");
                    out.println(packetList.size());
                }
                else if (inputLine.startsWith("MISSING:")){
                    String[] missingPacketsNumbers = inputLine.substring(8).split(",");
                    for (String packetNumber : missingPacketsNumbers) {
                        int pNum = Integer.parseInt(packetNumber.trim());
                        for (Packet packet : packetList) {
                            if (packet.getSequenceNum() == pNum) {
                                if (random.nextInt(100) > 20 ) {
                                    packet.setSent(true);
                                    out.println(packet.toFormattedString());
                                }
                            }
                        }
                    }
                    out.println("END_OF_TRANSMISSION");
                    out.println(packetList.size());
                }

            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
