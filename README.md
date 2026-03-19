# Blind Snake

Kotlin repository for the blind toroidal snake-search problem.

## Problem

A snake moves on an unknown `A x B` torus. The solver knows neither `A`, `B`, the starting cell, nor the apple position. The only feedback is whether a move has just landed on the apple. The target is to find the apple while staying within `35 * S` moves, where `S = A * B` and `S < 1_000_000`.

## Repo layout

- `src/main/kotlin/game/` — minimal engine-facing API
- `src/main/kotlin/strategy/` — main practical strategy
- `src/main/kotlin/baselines/` — slower comparison strategies
- `src/test/kotlin/verifier/` — offline simulators and verification utilities
- `docs/strategy.md` — write-up for the main heuristic
- `docs/baselines.md` — write-up for baseline approaches
- `results/` — generated experiment output, intentionally not required for understanding the code

## Current strategies

### Prime heuristic

The main practical strategy repeats blocks of:

1. move `RIGHT` several times,
2. move `DOWN` once,
3. vary the horizontal run length over time.

The purpose of the varying run lengths is to avoid bad resonance with the unknown width.

### Provable baseline

The baseline grows an area guess and tries structured sweep patterns under candidate width and height assumptions. It is useful as a comparison point, but it is not intended as the final strategy.

## Notes on verification

The verifier checks whether the deterministic walk covers every cell of a concrete board within a move budget. That is stronger than testing a single apple placement: if the walk covers the whole board, every apple position would be found.

## CLI
The current `main` entry point supports offline checks for a single board:

```bash
./gradlew run --args="check <strategy> <width> <height>"
./gradlew run --args="check <strategy> <width> <height> <appleX> <appleY>"
```

## Important caveat

The repository currently contains one practical heuristic and one structured baseline. The heuristic is the main candidate, but it is not presented as a complete proof of a universal `35 * S` bound.
