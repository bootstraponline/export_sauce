package bootstraponline.export_sauce;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Export {

    public static final String jobsURL = new SauceKey().jobsURL();

    public static void writeJobs(final String path) {
        HttpClient client = new DefaultHttpClient();
        // Fix WARNING: Invalid cookie header
        // We're not using cookies so ignore them.
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
                CookiePolicy.IGNORE_COOKIES);

        try {
            String response = client.execute(new HttpGet(jobsURL),
                    new BasicResponseHandler());

            Files.write(Paths.get(path), response.getBytes("UTF-8"));
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    public static String generateIndexFromJSON()
            throws JsonProcessingException, IOException {
        final String jobsFile = "jobs.json";
        // writeJobs(jobsFile);
        String output = "";

        JsonNode json = new ObjectMapper().readTree(new File(jobsFile));

        for (JsonNode job : json) {
            output += "{ 'job': '" + "<a href=\""
                    + job.get("video_url").asText().substring(0, 59) + "\">"
                    + job.get("name").asText() + "</a>" + "',\n";

            output += "'duration': '"
                    + Time.longToTime(job.get("end_time").asLong()
                            - job.get("start_time").asLong()) + "'},\n";
        }

        // Remove last ",\n"
        return output.substring(0, output.length() - 2);
    }

    public static void writeIndex(final Path index) throws IOException {
        final List<String> lines = Files.readAllLines(
                Paths.get("index/template.html"), Charset.forName("UTF-8"));
        String newIndex = "";

        final String json = generateIndexFromJSON();

        for (String line : lines) {
            if (line.contains("/**{DATA}**/")) {
                newIndex += json + "\n";
                continue;
            }
            newIndex += line + "\n";
        }

        Files.write(index,
                newIndex.getBytes(Charset.forName("UTF-8")));
    }

    public static void main(String[] args) throws Exception {
        writeIndex(Paths.get("index/index.html"));
    }
}