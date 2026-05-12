package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ApiCacheService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void saveRates(CurrencyApiResponse data) {
        save(
                "/home/site/wwwroot/backend/data/latest-rates.json",
                data
        );
    }

    public void saveTimeframe(CurrencyTimeframeApiResponse data) {
        save(
                "/home/site/wwwroot/backend/data/latest-timeframe.json",
                data
        );
    }

    public CurrencyApiResponse loadRates() {
        return load(
                "/home/site/wwwroot/backend/data/latest-rates.json",
                CurrencyApiResponse.class
        );
    }

    public CurrencyTimeframeApiResponse loadTimeframe() {
        return load(
                "/home/site/wwwroot/backend/data/latest-timeframe.json",
                CurrencyTimeframeApiResponse.class
        );
    }

    private void save(String path, Object data) {
        try {
            File file = new File(path);

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, data);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save API cache",
                    e
            );
        }
    }

    private <T> T load(String path, Class<T> clazz) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                return null;
            }

            return mapper.readValue(file, clazz);

        } catch (IOException e) {
            return null;
        }
    }
}