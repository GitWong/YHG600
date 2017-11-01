package com.galaxy.safe.Bean;

/**
 * Created by Dell on 2016/4/26.
 */
public class Record {
    public String sampling_no;
    public String sample_bh;
    public String report_conclusion;
    public String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSampling_no() {
        return sampling_no;
    }

    public void setSampling_no(String sampling_no) {
        this.sampling_no = sampling_no;
    }

    public String getSample_bh() {
        return sample_bh;
    }

    public void setSample_bh(String sample_bh) {
        this.sample_bh = sample_bh;
    }

    public String getReport_conclusion() {
        return report_conclusion;
    }

    public void setReport_conclusion(String report_conclusion) {
        this.report_conclusion = report_conclusion;
    }
}
