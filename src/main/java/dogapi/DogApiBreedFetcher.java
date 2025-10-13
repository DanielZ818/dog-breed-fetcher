package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        String url = "https://dog.ceo/api/breed/" + breed + "/list";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            // assume: response.body() != null;
            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            String s = obj.getString("status");
            // assume: s.equals("success");
            JSONArray msg = obj.getJSONArray("message");
            List<String> arr = new ArrayList<>();
            for (int i = 0; i < msg.length(); i++) {
                arr.add(msg.getString(i));
            }
            return arr;
        } catch (Exception e) {
            throw new BreedNotFoundException("Error when calling API");
        }
    }

    // Simple test
    public static void main(String[] args) {
        DogApiBreedFetcher a = new DogApiBreedFetcher();
        String response = a.getSubBreeds("hound").toString();
        System.out.println(response.equals("[afghan, basset, blood, english, ibizan, plott, walker]"));
    }
}