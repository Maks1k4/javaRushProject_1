package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
    private static final String ALPHABET = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя.,:=!? АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";

    public static char shift_right (char symbol, int key) {

        if (ALPHABET.indexOf(symbol) != -1) {
            return ALPHABET.charAt((ALPHABET.indexOf(symbol) + key) % ALPHABET.length());
        }
        else {
            return symbol;
        }
    }

    public static char decoder (char symbol, int key) {
        if (ALPHABET.indexOf(symbol) != -1) {
            return ALPHABET.charAt((ALPHABET.indexOf(symbol) - key) % ALPHABET.length());
        }
        else {
            return symbol;
        }
    }

    public static String getNewFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(0,dotIndex) + "Skrypted" + fileName.substring(dotIndex);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Put key:");
        int key = Integer.parseInt(reader.readLine());
        System.out.println("Enter a name of file");
        String fileName = reader.readLine();
        Path path = Path.of(fileName);

        try(FileChannel inputChannel = FileChannel.open(path)) {

            ByteBuffer buff = ByteBuffer.allocate((int) inputChannel.size());
            StringBuilder result = new StringBuilder();
            inputChannel.read(buff);
            buff.flip();
            int forTest = buff.remaining();
            while (forTest < buff.limit()) {
                 result.append(shift_right(((char) buff.get()), key));
            }
            ByteBuffer buff2 = ByteBuffer.allocate((int) inputChannel.size());
            buff2.put(result.toString().getBytes());
            Path newFile = Path.of(getNewFileName(fileName));
            if (Files.notExists(newFile)) {
                Files.createFile(newFile);
            }
            try (FileChannel outputChannel = FileChannel.open(newFile, StandardOpenOption.WRITE)) {
                buff2.flip();
                outputChannel.write(buff2);
                buff2.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        reader.close();
    }
}
