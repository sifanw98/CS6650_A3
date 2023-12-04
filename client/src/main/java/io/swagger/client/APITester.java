package io.swagger.client;

import io.swagger.client.ApiClient;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.api.LikeApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;

import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class APITester {

    private static DefaultApi api;
    private static LikeApi likeApi;
    private static final String CSV_FILE = "results.csv";

    private static AtomicInteger successfulCount = new AtomicInteger(0);
    private static AtomicInteger failedCount = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: APITester <threadGroupSize> <numThreadGroups> <delay> <IPAddr>");
            return;
        }

        int threadGroupSize = Integer.parseInt(args[0]);
        int numThreadGroups = Integer.parseInt(args[1]);
        int delay = Integer.parseInt(args[2]) * 1000;
        String IPAddr = args[3];

        setupApiClient(IPAddr);
        initializeCsvFile();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreadGroups; i++) {
            runTest(threadGroupSize, 100); // Now only 100 requests per thread
            if (i < numThreadGroups - 1) {
                Thread.sleep(delay);
            }
        }

        long endTime = System.currentTimeMillis();
        long wallTime = (endTime - startTime) / 1000;
        long totalRequests = threadGroupSize * 100 * numThreadGroups; // 100 POST requests per thread
        double throughput = (double) totalRequests / wallTime;

        System.out.println("Wall Time: " + wallTime + " seconds");
        System.out.println("Throughput: " + throughput + " requests/second");

        calculateAndDisplayStatistics();
    }

    private static void setupApiClient(String basePath) {
        ApiClient client = new ApiClient();
        client.setBasePath(basePath);
        api = new DefaultApi();
        api.setApiClient(client);
        likeApi= new LikeApi(client);
    }

    private static void initializeCsvFile() throws Exception {
        Files.write(Paths.get(CSV_FILE), "StartTime,RequestType,Latency,ResponseCode\n".getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static void writeResultsToCSV(long startTime, String requestType, long latency, int responseCode) {
        String record = String.join(",",
                String.valueOf(startTime),
                requestType,
                String.valueOf(latency),
                String.valueOf(responseCode)
        );

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.APPEND)) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void postAlbumAndReviews() throws Exception {
        long startTime, endTime;
        int responseCode;

        // POST a new album
        startTime = System.currentTimeMillis();
        File image = new File("/Users/sifanwei/Desktop/6650test/nmtb.png"); // Change to actual image path
        AlbumsProfile profile = new AlbumsProfile("Title", "Artist", "Year");
        ImageMetaData metaData = api.newAlbum(image, profile);
        endTime = System.currentTimeMillis();
        responseCode = 200; // Assuming a successful POST, change based on actual response
        writeResultsToCSV(startTime, "POST", endTime - startTime, responseCode);

        String albumID = metaData.getAlbumID(); // Get album ID from POST response

        postReview(likeApi, albumID, "like");
        postReview(likeApi, albumID, "like");
        postReview(likeApi, albumID, "dislike");
    }

    private static void postReview(LikeApi likeApi, String albumID, String likeOrDislike) {
        try {
            long startTime = System.currentTimeMillis();
            likeApi.review(likeOrDislike, albumID); // Call the API to post a review
            long endTime = System.currentTimeMillis();
            int responseCode = 200; // Assuming response is successful
            writeResultsToCSV(startTime, "POST_REVIEW", endTime - startTime, responseCode);
        } catch (ApiException e) {
            System.out.println("API exception occurred: " + e.getMessage());
            e.printStackTrace();
            // Optionally print response body
            if (e.getResponseBody() != null) {
                System.out.println("Response body: " + e.getResponseBody());
            }
        }
    }


    private static void runTest(int threadCount, int requestsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        postAlbumAndReviews();
                        successfulCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
    }

    private static void calculateAndDisplayStatistics() throws IOException {
        List<Long> latencies = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(CSV_FILE))) {
            lines.skip(1).forEach(line -> {
                String[] parts = line.split(",");
                long latency = Long.parseLong(parts[2]);
                latencies.add(latency);
            });
        }

        if (latencies.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        latencies.sort(Long::compareTo);

        long sum = latencies.stream().mapToLong(Long::longValue).sum();
        double mean = (double) sum / latencies.size();

        double median;
        if (latencies.size() % 2 == 0) {
            median = ((double) latencies.get(latencies.size() / 2 - 1) + latencies.get(latencies.size() / 2)) / 2;
        } else {
            median = latencies.get(latencies.size() / 2);
        }

        double p99 = latencies.get((int) (latencies.size() * 0.99));

        long min = latencies.get(0);
        long max = latencies.get(latencies.size() - 1);

        System.out.println("Mean Latency (ms): " + mean);
        System.out.println("Median Latency (ms): " + median);
        System.out.println("99th Percentile Latency (ms): " + p99);
        System.out.println("Min Latency (ms): " + min);
        System.out.println("Max Latency (ms): " + max);
        System.out.println("Successful Requests: " + successfulCount.get());
        System.out.println("Failed Requests: " + failedCount.get());
    }
}


