package fu.mi.fitting.io;


import fu.mi.fitting.sample.SampleCollection;

import java.io.File;

/**
 * Created by shangzhihao on 3/11/16.
 * get samples from file
 */
public abstract class SampleReader {
    protected File sampleFile;
    public SampleReader(File sampleFile){
        this.sampleFile = sampleFile;
    }
    public abstract SampleCollection read();
}
