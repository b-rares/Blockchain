package blockchain;

import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int i = 1;
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(new Block());
        while(i<5) {
            blocks.add(new Block(blocks.get(blocks.size() - 1)));
            i += 1;
        }
        for(Block block : blocks) {
            System.out.println("Block:");
            System.out.println("Id: " + block.getId());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Magic number: " + block.getTimeGenerating());
            System.out.println("Hash of the previous block: ");
            System.out.println(block.getPreviousHash());
            System.out.println("Hash of the block: ");
            System.out.println(block.getBlockHash());
            System.out.println("Block was generating for " + block.getTimeGenerating() + " seconds");
            System.out.println();
        }
    }
}

class Block {

    private static long totalCreated = 0;
    private long magicNumber = StringUtil.randomNumber();
    private static int numberOfZero;
    private long id;
    private long timestamp;
    private String previousHash;
    private String blockHash;
    private int timeGenerating = 0;

    public String generateHash(long id, long timestamp, String previousHash, long magicNumber) {
        return StringUtil.applySha256(id + previousHash + timestamp + magicNumber);
    }

    public Block(Block previous){
        this.id = totalCreated;
        Date date = new Date();
        this.timestamp = date.getTime();
        this.previousHash = previous.getBlockHash();
        this.blockHash = generateHash(this.id, this.timestamp, this.previousHash, this.magicNumber);
        if(numberOfZero > 0)
            while (!this.blockHash.substring(0, numberOfZero).equals("0".repeat( numberOfZero))) {
                this.blockHash = generateHash(this.id, this.timestamp, this.previousHash, this.magicNumber);
                this.magicNumber = StringUtil.randomNumber();
            }
        this.timeGenerating = (int) (date.getTime() - timestamp) / 10_000;
        totalCreated += 1;
    }

    public Block() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter how many zeros the hash must start with:");
        numberOfZero = scanner.nextInt();
        this.id = totalCreated;
        this.timestamp = new Date().getTime();
        this.previousHash = "0";
        this.blockHash = generateHash(this.id, this.timestamp, this.previousHash, this.magicNumber);
        if(numberOfZero > 0)
            while (!this.blockHash.substring(0,  numberOfZero).equals("0".repeat(numberOfZero))) {
                this.blockHash = generateHash(this.id, this.timestamp, this.previousHash, this.magicNumber);
                this.magicNumber = StringUtil.randomNumber();
            }
        totalCreated += 1;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public long getTotalCreated() {
        return totalCreated;
    }

    public Boolean checkBlockValidity(Block previous) {
        return this.previousHash == previous.getBlockHash();
    }

    public long getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getTimeGenerating() {
        return timeGenerating;
    }
}

class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static long randomNumber() {
        Random random = new Random();
        return random.nextLong();
    }
}