import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

public class graph {

    Map<String, List<EdgeLocation>> adjList;
    Set<String> locations;
    int numLocation;

    public graph() {
        this.adjList = new HashMap<>();
        this.numLocation = 0;
        this.locations = new HashSet<>();
    }

    // Menambahkan vertex ke graph
    public void addVertex(String location) {
        if (!location.matches("^[A-Z0-9]+$")) {
            System.out.println("Error : Nama Harus terdiri dari huruf besar atau angka");
        } else if (!locations.contains(location)) {
            this.adjList.put(location, new ArrayList<>());
            this.locations = adjList.keySet();
            this.numLocation++;
        }
    }

    // Tampilan untuk menambahkan vertex
    public void interfaceAddVertex(Scanner reader) {
        // Scanner reader = new Scanner(System.in);
        System.out.print("Masukkan lokasi baru (Huruf Kapital): ");
        String newLocation = reader.nextLine().trim();
        addVertex(newLocation);
        // reader.close();
    }

    public void removeVertex(String location) {
        if (adjList.containsKey(location)) {
            
            for (String string : adjList.keySet()) {
                removeEdge(string, location);
            }
            adjList.remove(location);
            System.out.println(location + " Berhasil dihapus");
        } else {
            System.out.println("Error : Lokasi Tidak Ada");
        }
    }

    public void interfaceRemoveVertex(Scanner reader) {
        System.out.println("=== HAPUS LOKASI DARI DATA SISTEM ===");
        System.out.print("Masukan nama lokasi : ");
        String location = reader.nextLine();

        removeVertex(location);
    }

    // Tampilan untuk menambahkan edge
    public void interfaceAddEdge(Scanner reader) {
        System.out.print("Masukan Lokasi Asal : ");
        String sourceLocation = reader.nextLine().trim();
        System.out.print("Masukan Lokasi Tujuan : ");
        String destinationLocation = reader.nextLine().trim();
        System.out.print("Masukan Jarak Antar Lokasi : ");
        int distance = reader.nextInt();
        reader.nextLine();
        addEdge(sourceLocation, destinationLocation, distance);
    }

    // Menambahkan edge
    public void addEdge(String location1, String location2, int distance) {

        if (adjList.containsKey(location1) && adjList.containsKey(location2)) {

            EdgeLocation newWay1 = new EdgeLocation(location2, distance);
            EdgeLocation newWay2 = new EdgeLocation(location1, distance);
            this.adjList.get(location1).add(newWay1);
            this.adjList.get(location2).add(newWay2);
        } else {
            System.out.println("Salah satu City tidak ada di Data, Perhatikan huruf yang dimasukan");
        }
    }

    public void interfaceRemoveEdge(Scanner reader) {
        System.out.println("=== HAPUS JALAN ANTAR LOKASI ===");
        System.out.print("Masukan Lokasi 1 : ");
        String location1 = reader.nextLine();
        System.out.print("Masukan Lokasi 2 : ");
        String location2 = reader.nextLine();

        if (adjList.containsKey(location1) && adjList.containsKey(location2)) {
            removeEdge(location1, location2);
        } else {
            System.out.println("Tidak ada Jalan antara " + location1 + " dan " + location2);
        }

    }

    public void removeEdge(String location1, String location2) {
        List<EdgeLocation> wayToLocation1 = adjList.get(location1);
        List<EdgeLocation> wayToLocation2 = adjList.get(location2);

        for (int i = 0; i < wayToLocation1.size(); i++) {
            if (wayToLocation1.get(i).getDestination().equals(location2)) {
                wayToLocation1.remove(i);
            }
        }

        for (int i = 0; i < wayToLocation2.size(); i++) {
            if (wayToLocation2.get(i).getDestination().equals(location1)) {
                wayToLocation2.remove(i);
            }
        }
    }

    public List<EdgeLocation> findMST() {
        if (this.adjList.isEmpty()) {
            System.out.println("Data Lokasi dan Jalur Kosong");
            return null;
        }

        // List untuk menyimpan hasil akhir mst
        List<EdgeLocation> mstResult = new ArrayList<>();

        // Set menyimpan lokasi yang sudah dikunjungi
        Set<String> visited = new HashSet<>();

        // Priority queue untuk menyimpan edge, diurutkan berdasarkan jarak
        PriorityQueue<EdgeLocation> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.getDistance()));

        String startVertex = this.adjList.keySet().iterator().next();
        visited.add(startVertex);

        for (EdgeLocation edge : this.adjList.get(startVertex)) {
            EdgeLocation fullEdge = new EdgeLocation(edge.getDestination(), edge.getDistance());
            fullEdge.setSource(startVertex);
            pq.add(fullEdge);
        }

        while (!pq.isEmpty() && visited.size() < this.numLocation) {
            // ambil edge dengan jarak terkecil
            EdgeLocation minEdgeLocation = pq.poll();
            String destination = minEdgeLocation.getDestination();

            if (visited.contains(destination)) {
                continue;
            }

            mstResult.add(minEdgeLocation);
            visited.add(destination);

            for (EdgeLocation neighborEdgeLocation : this.adjList.get(destination)) {
                if (!visited.contains(neighborEdgeLocation.getDestination())) {
                    EdgeLocation newFullEdge = new EdgeLocation(neighborEdgeLocation.getDestination(),
                            neighborEdgeLocation.getDistance());
                    newFullEdge.setSource(destination);
                    pq.add(newFullEdge);
                }
            }
        }

        // show mst
        System.out.println("=== RUTE TERPENDEK KE SEMUA LOKASI ===");
        int totalDist = 0;
        for (EdgeLocation edge : mstResult) {
            System.out.println("hubungkan " + edge.getSource() + " ke " + edge.getDestination() + " (jarak : "
                    + edge.getDistance() + " km");
            totalDist += edge.getDistance();
        }
        System.out.println("Total Jarak Terpendek: " + totalDist + " km");
        System.out.println("==========================================");
        return mstResult;

    }

    public void findShortestPath(String startNode, String endNode) {

        if (!adjList.containsKey(startNode) || !adjList.containsKey(endNode)) {
            System.err.println("Error : Lokasi Awal atau Tujuan tidak ada di data Sistem");
            return;
        }

        Map<String, Integer> distanceFromStartNode = new HashMap<>();
        Map<String, String> previousNode = new HashMap<>();

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

        for (String vertex : adjList.keySet()) {
            if (vertex.equals(startNode)) {
                distanceFromStartNode.put(vertex, 0);
            } else {
                distanceFromStartNode.put(vertex, Integer.MAX_VALUE);
            }
            pq.add(new AbstractMap.SimpleEntry<>(vertex, distanceFromStartNode.get(vertex)));
        }

        while (!pq.isEmpty()) {
            String currentNode = pq.poll().getKey();

            if (currentNode.equals(endNode)) {
                break;
            }

            for (EdgeLocation edge : adjList.get(currentNode)) {
                String neighbor = edge.getDestination();
                int distanceToNeighbor = edge.getDistance();
                int newDist = distanceFromStartNode.get(currentNode) + distanceToNeighbor;

                if (newDist < distanceFromStartNode.get(neighbor)) {
                    distanceFromStartNode.put(neighbor, newDist);

                    previousNode.put(neighbor, currentNode);

                    pq.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }

            }

        }

        printShortestPath(startNode, endNode, distanceFromStartNode, previousNode);

    }

    private void printShortestPath(String start, String end, Map<String, Integer> distance,
            Map<String, String> previous) {
        System.out.println("\n=== RUTE TERPENDEK ===");
        Integer totalDistance = distance.get(end);

        if (totalDistance == Integer.MAX_VALUE) {
            System.out.println("Tidak ada rute yang tersedia dari " + start + " ke " + end);
            return;
        }

        List<String> path = new ArrayList<>();
        String currentNode = end;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = previous.get(currentNode);
        }

        Collections.reverse(path);

        System.out.println("Rute dari " + start + " ke " + end + ":");
        System.out.println(String.join(" -> ", path));
        System.out.println("Total Jarak: " + totalDistance + " km");
        System.out.println("====================================");
    }

    public void display() {
        System.out.println("=== PETA KE SEMUA LOKASI ===");
        if (locations.isEmpty()) {
            System.out.println("Tidak ada Data location di Sistem");
        } else {
            for (String location : adjList.keySet()) {
                System.out.println(location + " \t\t: " + this.adjList.get(location));
            }
        }
    }

    public static graph loadGraphFromFile(String filename) {
        Gson gson = new Gson();
        graph loadedGraph = new graph(); // Buat objek graph baru

        try (FileReader reader = new FileReader(filename)) {
            // Tentukan tipe data target yang kompleks menggunakan TypeToken
            Type type = new TypeToken<Map<String, List<EdgeLocation>>>() {
            }.getType();

            // Deserialisasi (ubah String JSON kembali ke objek Java)
            Map<String, List<EdgeLocation>> loadedAdjList = gson.fromJson(reader, type);

            // Isi data ke objek graph yang baru
            if (loadedAdjList != null) {
                loadedGraph.adjList = loadedAdjList;
                loadedGraph.locations = loadedAdjList.keySet();
                loadedGraph.numLocation = loadedAdjList.size();
                // System.out.println("Graph berhasil dimuat dari file: " + filename);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File save tidak ditemukan. Memulai dengan graph baru.");
        } catch (IOException e) {
            System.err.println("Gagal membaca file: " + e.getMessage());
        }

        return loadedGraph;
    }

    public void saveGraphToFile(String filename) {
        // Menggunakan GsonBuilder untuk membuat format JSON yang rapi (pretty printing)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this.adjList, writer);
            // System.out.println("Graph berhasil disimpan ke file: " + filename);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan file Graph: " + e.getMessage());
        }
    }

}
