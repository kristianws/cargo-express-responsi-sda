import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryTree {

    private Node root;

    public BinaryTree() {
        this.root = null;
    }

    public void interfaceInsert(Scanner reader) {
        System.out.println("=== MASUKAN CARGO KE KAPAL ===");
        Cargo newCargo = new Cargo();

        System.out.print("Masukan Nama cargo : ");
        String name = reader.nextLine();

        System.out.print("Masukan Berat Cargo : ");
        int weight = reader.nextInt();
        reader.nextLine();

        if (weight > 50) {
            newCargo.setCargoName(name);
            newCargo.setWeightCargo(weight);

            insert(newCargo);
        } else {
            System.out.println("Error : Berat Cargo Kurang dari 50");
        }
    }

    public void insert(Cargo data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node current, Cargo data) {
        if (current == null) {
            return new Node(data);
        }

        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        }

        return current;
    }

    public void interfaceSearchCargo(Scanner reader) {

        System.out.println("=== MENCARI CARGO DI KAPAL ===");
        Cargo newCargo = new Cargo();

        System.out.print("Masukan Nama cargo : ");
        String name = reader.nextLine();
        newCargo.setCargoName(name);

        boolean searchResult = searchRecursive(root, newCargo);

        if (searchResult) {
            System.out.println("Cargo ada di Kapal");
        } else {
            System.out.println("Cargo tidak ada di Kapal");
        }
    }

    private boolean searchRecursive(Node current, Cargo data) {
        if (current == null) {
            return false;
        }

        int compareResult = data.compareTo(current.data);

        if (compareResult == 0) {
            return true;
        }

        if (compareResult < 0) {
            return searchRecursive(current.left, data);
        } else {
            return searchRecursive(current.right, data);
        }
    }

    public void inOrderTraversal() {
        System.out.println("=== CARGO YANG DIANGKUT ===");
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node current) {
        if (current != null) {
            inOrderRecursive(current.left);
            System.out.println(current.data);
            inOrderRecursive(current.right);
        }
    }

    public void interfaceDelete(Scanner reader) {
        System.out.println("=== KELUARKAN CARGO DARI KAPAL ===");
        System.out.print("Masukan nama Cargo : ");
        String name = reader.nextLine();

        Cargo newCargo = new Cargo();
        newCargo.setCargoName(name);

        delete(newCargo);
    }

    public void delete(Cargo data) {
        root = deleteRecursive(root, data);
    }

    private Node deleteRecursive(Node current, Cargo data) {
        if (current == null)
            return null;

        if (data.compareTo(current.data) < 0) {
            current.left = deleteRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = deleteRecursive(current.right, data);
        } else {
            // Node ditemukan
            if (current.left == null && current.right == null)
                return null;
            if (current.right == null)
                return current.left;
            if (current.left == null)
                return current.right;

            Cargo smallestValue = findSmallestValue(current.right);
            current.data = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
        }
        return current;
    }

    private Cargo findSmallestValue(Node root) {
        return root.left == null ? root.data : findSmallestValue(root.left);
    }

    public void saveToFile(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            // Cukup serialisasi node root. Semua anak akan ikut tersimpan secara rekursif.
            gson.toJson(this.root, writer);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan file: " + e.getMessage());
        }
    }

    public static BinaryTree loadFromFile(String filename) {
        Gson gson = new Gson();
        BinaryTree newTree = new BinaryTree();

        try (FileReader reader = new FileReader(filename)) {
            newTree.root = gson.fromJson(reader, Node.class);
        } catch (IOException e) {
            System.out.println("File save tidak ditemukan atau error. Memulai dengan tree baru.");
        }

        return newTree;
    }

}
