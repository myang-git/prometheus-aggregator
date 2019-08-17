# Prometheus Metric Parser and Aggregator

## Metric Parser

This project has a Java implementation of Prometheus text exposition parser.
The parser is able to progressively handle metrics exported in the format specified at [Prometheus official site](https://prometheus.io/docs/instrumenting/exposition_formats/)

## Metric Aggregator and Gateway

The motivation of having an aggregator is to solve the problem where the source of metrics is not reachable, or is intentially hidden from the Prometheus server. Monitoring of web frontend, IoT device, and desktop application are a good example. In this case we need a gateway that is reachable from the Prometheus server to receive and aggregate the metrics from subject entities.

The `MetricAggregator` class is to group the metrics by the signature, which is composed of the name and labels. It handles adding, updating, and removing metrics.
The `MetricAggregatorServlet` handles the following HTTP methods:

* GET: List current metrics.
* POST: Upload metrics.
* DELETE: Remove all metrics or metrics older than specified age.
