package bestfood.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${spring.database.url}")
    private String supabaseUrl;

    @Value("${spring.database.bucket}")
    private String bucket;

    @Value("${spring.database.key}")
    private String supabaseKey;

    private final HttpClient client = HttpClient.newHttpClient();

    public String upload(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();

        String fileName = UUID.randomUUID() + "_" + originalName;

        String uploadUrl = supabaseUrl
                + "/storage/v1/object/"
                + bucket
                + "/"
                + fileName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Authorization",
                        "Bearer " + supabaseKey)
                .header("apikey",
                        supabaseKey)
                .header("Content-Type",
                        file.getContentType())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(
                        file.getBytes()))
                .build();

        try {

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200
                    && response.statusCode() < 300) {

                return supabaseUrl
                        + "/storage/v1/object/public/"
                        + bucket
                        + "/"
                        + fileName;

            } else {

                throw new RuntimeException(
                        "Supabase upload failed: "
                                + response.body());
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Upload interrupted",
                    e);
        }
    }
}