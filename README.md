# Prometheus Metric Parser and Aggregator

## Metric Parser

This project has a Java implementation of Prometheus text exposition parser.
The parser is able to progressively handle metrics exported in the format specified at [Prometheus official site](https://prometheus.io/docs/instrumenting/exposition_formats/)

## Metric Aggregator and Gateway

The motivation of having an aggregator is to solve the problem where the entities that subject to monitoring are not reachable, or are intentially hidden from the Prometheus server. Monitoring of web application, IoT devices, and desktop applications are good example. In this case a gateway that is reachable from both the subject entities and the Prometheus server can capture and aggregate the metrics and make them available to Prometheus.

The MetricAggregator class is to group the metrics by the signature, which is composed of the name and labels. It handles adding, updating, and removing metrics.
The MetricAggregatorServlet provides the following API endpoints:

* GET: List current metrics.
* POST: Upload metrics.
* DELETE: Remove all metrics or metrics older than specified age.
