package com.pixiu.fortune8keno.fortune8keno.service;


import com.pixiu.fortune8keno.fortune8keno.config.GameEngineProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Client for the platform's RNG endpoint:
 *
 *   POST {rngBaseUrl}/irgs/rng
 *   { "upperBounds": [10, 30] }  ->  { "randomNumbers": [3, 20] }
 *
 * Each returned value is uniform on [0, upperBound). The engine itself must
 * never generate its own randomness - all outcome-determining draws are
 * required to go through this platform-hosted RNG, per the spec's RNG API
 * section.
 */
@Service
public class RngClient {

    private final RestTemplate restTemplate;
    private final GameEngineProperties properties;

    public RngClient(RestTemplate restTemplate, GameEngineProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * Draws {@code count} unique numbers from the pool [1, poolSize] using a
     * single RNG call, via the classic "decreasing upper bound" technique for
     * sampling without replacement (equivalent to a partial Fisher-Yates
     * shuffle driven by an external RNG):
     *
     *   upperBounds = [poolSize, poolSize-1, ..., poolSize-count+1]
     *
     * For each returned index i (0-based, i < remaining pool size), remove
     * that element from the remaining pool and add it to the result.
     */
    public List<Integer> drawUnique(int poolSize, int count) {
        if (count > poolSize) {
            throw new IllegalArgumentException(
                    "Cannot draw " + count + " unique numbers from a pool of " + poolSize);
        }

        List<Long> upperBounds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            upperBounds.add((long) (poolSize - i));
        }

        RngModels.RngResponse response = restTemplate.postForObject(
                properties.getRngBaseUrl() ,
                new RngModels.RngRequest(upperBounds),
                RngModels.RngResponse.class);

        if (response == null || response.randomNumbers() == null
                || response.randomNumbers().size() != count) {
            throw new IllegalStateException("RNG service returned an invalid response");
        }

        List<Integer> pool = IntStream.rangeClosed(1, poolSize)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> drawn = new ArrayList<>(count);
        for (int index : response.randomNumbers()) {
            drawn.add(pool.remove(index));
        }
        if (new HashSet<>(drawn).size() != drawn.size()) {
            throw new IllegalStateException(
                    "RNG service returned duplicate numbers: " + drawn);
        }
        return drawn;
    }
}
