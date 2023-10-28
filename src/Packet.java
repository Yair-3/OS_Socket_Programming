public class Packet implements Comparable<Packet> {
    private String message;
    private int sequenceNum;
    private boolean sent;


    public Packet(String m, int s, boolean ws) {
        this.message = m;
        this.sequenceNum = s;
        this.sent = ws;
    }

    public String getMessage() {
        return message;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String toFormattedString() {
        return message + "," + sequenceNum + "," + sent;
    }

    // Convert a string representation back to a Packet object
    public static Packet fromFormattedString(String str) {
        String[] parts = str.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Packet representation: " + str);
        }
        return new Packet(parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2]));
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public int compareTo(Packet o) {
        return Integer.compare(this.getSequenceNum(), o.sequenceNum);

}
}
