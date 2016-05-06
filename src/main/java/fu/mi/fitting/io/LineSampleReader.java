package fu.mi.fitting.io;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;

import java.io.File;
import java.util.List;

/**
 * get samples from txt file
 * every line is a sample
 */
public class LineSampleReader extends SampleReader {

    public LineSampleReader(File sampleFile) {
        super(sampleFile);
    }

    @Override
    public SampleCollection read() {
        int id = 1;
        List<SampleItem> samples = Lists.newArrayList();
        try {
            List<String> lines = Files.readLines(sampleFile, Charsets.US_ASCII);
            for(String line : lines){
                samples.add(new SampleItem(id, Double.valueOf(line)));
                id++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SampleCollection(samples);
    }
}
