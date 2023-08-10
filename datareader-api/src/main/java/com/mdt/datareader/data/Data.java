package com.mdt.datareader.data;

import java.util.List;

public class Data {

	private List<List<Double>> datasets;
	private List<Generator> generators;

	public List<List<Double>> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<List<Double>> datasets) {
		this.datasets = datasets;
	}

	public List<Generator> getGenerators() {
		return generators;
	}

	public void setGenerators(List<Generator> generators) {
		this.generators = generators;
	}

	@Override
	public String toString() {
		return "Data [datasets=" + datasets + ", generators=" + generators + "]";
	}

}
