# HyperStar2

HyperStar2 fits a time series into a phase-type distribution or a Markov arrival process.
The original paper is [HyperStar2](https://doi.org/10.1145/3030207.3030243).

## Build Requirements

- JDK 25 (LTS)
- Gradle wrapper 9.3.1 (included)

## Build and Test

```bash
./gradlew clean test
```

## Run

```bash
./gradlew run
```

## Notes

- Vendored `org/jfree/chart/fx` sources were removed. The project now depends on `org.jfree:org.jfree.chart.fx:2.0.2`.
- Matrix exponential logic now uses a pure Java implementation in `MathUtils` (no `jblas` native dependency).
- A Python reimplementation is available at [HyperStarC](https://github.com/shangzhihao/HyperStarC).
