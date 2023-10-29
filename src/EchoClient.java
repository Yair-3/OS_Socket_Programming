import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class EchoClient {
    public static void main(String[] args) throws IOException {

//        if (args.length != 2) {
//            System.err.println(
//                    "Usage: java EchoClient <host name> <port number>");
//            System.exit(1);
//        }
//
//        String hostName = args[0];
//        int portNumber = Integer.parseInt(args[1]);

        String hostName = "localhost";
        int portNumber = 30121;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
        ) {

            out.println("starting");
            out.flush();
            boolean shouldContinue = true;
            ArrayList<Integer> received = new ArrayList<>();
            ArrayList<Integer> sendBack = new ArrayList<>();
            ArrayList<Packet> recievedPackets = new ArrayList<>();

            while (shouldContinue) {
                String line = in.readLine();
                System.out.println("Received from server: " + line);
                if (line == null) {
                    // Handle unexpected end of the stream or disconnection
                    break;
                }
                if (line.equals("END_OF_TRANSMISSION")) {
                    int amountSent = Integer.parseInt(in.readLine());
                    for (int i = 0; i < amountSent; i++) {
                        if (!received.contains(i)) {
                            sendBack.add(i);
                        }
                    }

                    if (!sendBack.isEmpty()) {
                        StringBuilder missingNumbersRequest = new StringBuilder("MISSING:");
                        for (int i = 0; i < sendBack.size(); i++) {
                            missingNumbersRequest.append(sendBack.get(i));
                            if (i != sendBack.size() - 1) {
                                missingNumbersRequest.append(",");
                            }
                        }
                        System.out.println("Requesting retransmission for packets: " + missingNumbersRequest);
                        out.println(missingNumbersRequest);
                        sendBack.clear();
                    } else {
                        shouldContinue = false;
                        Collections.sort(recievedPackets);
                        System.out.println(recievedPackets);
                    }
                } else {
                    Packet packet = Packet.fromFormattedString(line);
                    received.add(packet.getSequenceNum());
                    recievedPackets.add(packet);
                }
            }

        } catch (
                UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (
                IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
