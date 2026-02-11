import java.io.*;
import java.util.*;
import java.util.Scanner;

public class App {

    static void home() {
        String filename = "home.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error baca File : Home.txt");// TODO: handle exception
        }
    }

    static void interfaceShortestPath(Scanner reader, graph city) {
        System.out.println("=== MENCARI JALAN TERPENDEK ANTAR LOKASI ===");
        System.out.print("Masukan Lokasi Awal : ");
        String start = reader.nextLine().trim();

        System.out.print("Masukan Lokasi Tujuan : ");
        String end = reader.nextLine().trim();

        city.findShortestPath(start, end);

    }

    public static void main(String[] args) {

        String fileGraph = "graph.json";
        String fileTree = "cargo.json";

        int option;
        Scanner reader = new Scanner(System.in);
        boolean run = true;
        graph city = graph.loadGraphFromFile(fileGraph);
        BinaryTree cargoTree = BinaryTree.loadFromFile(fileTree);
        List<EdgeLocation> mstResult;

        while (run) {
            System.out.println();
            home();
            System.out.print(">> ");
            option = reader.nextInt();
            reader.nextLine();

            switch (option) {
                case 1:
                    // Menambahkan Lokasi ke Graph
                    city.interfaceAddVertex(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 2:
                    // Menambahkan jalan Antar lokasi
                    city.interfaceAddEdge(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 3:
                    // Menghapus Jalan antar Lokasi
                    city.interfaceRemoveEdge(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 4:
                    // Menampilkan Semua Lokasi dan Jalan nya
                    city.display();
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 5:
                    // Mendapatkan Peta menuju semua Lokasi dengan jalan terpendek
                    mstResult = city.findMST();
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 6:
                    // mendapatkan rute terbaik antar lokasi
                    interfaceShortestPath(reader, city);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 7:
                    // Menambahkan barang ke kargo yang akan dikirim
                    // System.out.println("Menambahkan Kargo");
                    cargoTree.interfaceInsert(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 8:
                    cargoTree.interfaceSearchCargo(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 9:
                    cargoTree.inOrderTraversal();
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 10:
                    city.interfaceRemoveVertex(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 11:
                    cargoTree.interfaceDelete(reader);
                    System.out.println("PRESS ENTER TO CONTINUE");
                    reader.nextLine();
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("INVALID INPUT OPTION");
                    reader.nextLine();
                    break;
            }
        }
        reader.close();
        city.saveGraphToFile(fileGraph);
        cargoTree.saveToFile(fileTree);
    }

}