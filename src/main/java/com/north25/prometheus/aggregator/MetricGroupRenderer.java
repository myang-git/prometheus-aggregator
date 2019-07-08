package com.north25.prometheus.aggregator;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import com.north25.prometheus.CommentElement;
import com.north25.prometheus.MutableMetricGroup;
import com.north25.prometheus.MutableSampleElement;
import com.north25.prometheus.SampleElement;
import com.north25.util.TimeDuration;

class MetricGroupRenderer {
	
	private boolean withAge;
	private long referenceTime;

	public MetricGroupRenderer(boolean withAge, long referenceTime) {
		this.withAge = withAge;
		this.referenceTime = referenceTime;
	}
	
	
	private void writeLine(Writer writer, String line) throws IOException {
		writer.write(line);
		writer.write("\n");
	}
	
	public void writeMetricGroup(MutableMetricGroup metricGroup, Writer writer) throws IOException {		
		CommentElement help = metricGroup.getHelpComment();
		if (help != null) {
			this.writeLine(writer, help.toString());
		}
		CommentElement type = metricGroup.getTypeComment();
		if (type != null) {
			this.writeLine(writer, type.toString());
		}
		Iterator<SampleElement> iter = metricGroup.getSampleIterator();
		while (iter.hasNext()) {
			MutableSampleElement sample = (MutableSampleElement)iter.next();
			writer.write(sample.toString());
			if (this.withAge) {
				writer.write(" ");
				TimeDuration age = new TimeDuration(this.referenceTime - sample.getReportTimestamp());
				writer.write("age = ");
				writer.write(age.toString());
			}
			writer.write('\n');
		}
	}

}
