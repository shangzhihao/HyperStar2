package fu.mi.fitting.io;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * get samples from txt file
 * every line is a sample
 */
public class LineSampleReader extends SampleReader {

    Logger logger = LoggerFactory.getLogger(LineSampleReader.class);

    public LineSampleReader(File sampleFile) {
        super(sampleFile);
    }

    @Override
    public SampleCollection read() {
        int id = 0;
        List<SampleItem> samples = Lists.newArrayList();
        try {
            List<String> lines = Files.readLines(sampleFile, Charsets.US_ASCII);
            for (String line : lines) {
                try {
                    samples.add(new SampleItem(id, Double.valueOf(line)));
                    id++;
                } catch (NumberFormatException e) {
                    logger.warn("{} is not a number", line);
                }
            }
        } catch (IOException e) {
            logger.warn("I/O error: {}", e.getMessage());
        }
        return new SampleCollection(samples);
    }
}
