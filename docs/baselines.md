# Baseline Strategies

## 1. Purpose

In addition to the main heuristic solution, I implemented a baseline strategy with a more structured and theoretically motivated approach to provide as comparison point and as a mathematically provable construction.

---

## 2. Provable Strategy

### 2.1 Core Idea

The provable strategy attempts to systematically cover the grid by iterating over possible structural assumptions about the board.

It works in phases, where each phase assumes a candidate dimension size and performs a traversal designed to cover the grid under that assumption.

Over time, these phases expand to cover all possible configurations.

---

### 2.2 Approach

- Iterate over increasing guesses of grid structure
- For each guess:
    - perform a structured traversal pattern
    - attempt to cover all cells under that assumption
- Continue until the apple is found

This guarantees eventual coverage regardless of the true dimensions.

---

## 3. Why It Works

Unlike the heuristic approach, this strategy does not rely on stride mixing or probabilistic coverage.

Instead, it systematically enumerates possible traversal patterns that are guaranteed to cover the grid for some configuration.

Because one of these configurations must match the true board, the apple is eventually found.

---

## 4. Limitations

- The total number of steps grows significantly as assumptions expand
- The implementation has complexity approximately:

\[
O(S \sqrt{S})
\]

---

## 5. Practical Role

This strategy is useful as:

- a correctness reference
- a comparison baseline
- a demonstration of a more structured (but slower) approach

It highlights the tradeoff:

| Strategy          | Guarantee | Efficiency |
|------------------|----------|-----------|
| Prime Heuristic  | No       | High      |
| Provable Baseline| Yes      | Low       |

---

## 6. Experimental Observations

- Always eventually finds the apple
- Significantly slower than the heuristic strategy
- Becomes impractical on larger boards due to step count growth

---

## 7. Conclusion

The provable strategy provides a useful theoretical baseline but does not meet the performance constraints required for submission.

It is included to demonstrate exploration of guaranteed approaches and to contextualize the effectiveness of the main heuristic strategy.