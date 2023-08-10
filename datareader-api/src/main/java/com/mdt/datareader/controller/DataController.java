package com.mdt.datareader.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdt.datareader.data.Data;
import com.mdt.datareader.data.Generator;
import com.mdt.datareader.data.Operation;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DataController {

	@GetMapping("/")
	public String index() {
		return "Data Reader API Started!!";
	}
	
	@GetMapping("/read-data")
	public String readData() {
		try {
			JsonNode json = new ObjectMapper().readTree(new ClassPathResource("json/data.json").getFile());
			//return json.toPrettyString();
			return json.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "Error while reading MDT Data " + e.getMessage();
		}
	}

	@GetMapping("/start-generators")
	public List<String> startGenerators() {
		List<String> generatorResults = new ArrayList<>();
		try {
			JsonNode json = new ObjectMapper().readTree(new ClassPathResource("json/data.json").getFile());
			ObjectMapper mapper = new ObjectMapper();
			Data data = mapper.convertValue(json, Data.class);
			if (!ObjectUtils.isEmpty(data)) {
				generatorResults = runGenerators(data.getDatasets(), data.getGenerators());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generatorResults;
	}
	
	@PostMapping("/restart-generators")
	public List<String> restartGenerators(@RequestBody Data data) {
		List<String> generatorResults = new ArrayList<>();
		if (!ObjectUtils.isEmpty(data)) {
			generatorResults = runGenerators(data.getDatasets(), data.getGenerators());
		}
		return generatorResults;
	}

	private List<String> runGenerators(List<List<Double>> datasets, List<Generator> generators) {
		final int ZERO = 0;
		List<String> generatorResults = new ArrayList<>();
		if (!generators.isEmpty()) {
			AtomicInteger indexHolder = new AtomicInteger();
			LocalDateTime startTime = LocalDateTime.now();
			datasets.forEach(d -> {
				generators.forEach(g -> {
					if (null != g.getOperation() && g.getOperation().equalsIgnoreCase(Operation.SUM.toString())) {
						int interval = (indexHolder.get() == ZERO) ? ZERO : g.getInterval();
						generatorResults.add(formatResult(startTime, interval, g.getName(),
								d.stream().mapToDouble(Double::doubleValue).sum()));
					} else if (null != g.getOperation()
							&& g.getOperation().equalsIgnoreCase(Operation.AVERAGE.toString())) {
						int interval = (indexHolder.get() == ZERO) ? ZERO : g.getInterval();
						generatorResults.add((formatResult(startTime, interval, g.getName(),
								d.stream().mapToDouble(Double::doubleValue).average().getAsDouble())));
					} else if (null != g.getOperation()
							&& g.getOperation().equalsIgnoreCase(Operation.MIN.toString())) {
						int interval = (indexHolder.get() == ZERO) ? ZERO : g.getInterval();
						generatorResults.add((formatResult(startTime, interval, g.getName(),
								d.stream().mapToDouble(Double::doubleValue).min().getAsDouble())));
					} else if (null != g.getOperation()
							&& g.getOperation().equalsIgnoreCase(Operation.MAX.toString())) {
						int interval = (indexHolder.get() == ZERO) ? ZERO : g.getInterval();
						generatorResults.add((formatResult(startTime, interval, g.getName(),
								d.stream().mapToDouble(Double::doubleValue).max().getAsDouble())));
					}
				});
				indexHolder.getAndIncrement();
			});
		}
		return generatorResults;
	}

	private String formatResult(LocalDateTime startTime, int interval, String generator, Double result) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return timeFormatter.format(startTime.plusSeconds(interval)) + " " + generator + " " + formatResult(result);
	}

	public static String formatResult(double d) {
		if (d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%.2f", d);
	}

}
