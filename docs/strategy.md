# Blind Snake Strategy — Prime Sweep Heuristic

## 1. Problem Summary

We are given a snake game played on an unknown toroidal grid of size A × B, with total area S = A·B < 10^6.

Constraints:
- The snake starts at an unknown position
- The apple is at an unknown position
- The player receives no positional feedback
- Movement wraps around (torus topology)
- The only feedback is when the apple is found
- The solution must aim to finish within 35·S moves

---

## 2. Core Idea

The strategy performs repeated movement cycles of the form:

- Move RIGHT *k* times
- Move DOWN once

The value of *k* changes over time according to a predefined sequence of stride lengths (based on primes and small variations).

This produces a sequence of horizontal sweeps, where each row is traversed with a different horizontal offset.

---

## 3. Why This Works

### 3.1 The Problem with Fixed Strides

If we used a fixed stride *k*, the movement pattern would repeat with a period dependent on the unknown width A.

If `gcd(k, A) ≠ 1`, the snake would only visit a subset of columns, leading to incomplete coverage.

Even if `gcd(k, A) = 1`, the traversal may still align poorly with vertical movement, causing inefficient coverage.

---

### 3.2 Breaking Resonance with Variable Strides

To avoid this, we vary the stride length *k* over time.

By using a sequence of different values (including primes), we ensure:

- The horizontal step sizes interact differently with the unknown width A
- Alignment patterns are constantly disrupted
- The traversal avoids getting stuck in periodic cycles

Over time, this causes the horizontal offsets to “mix” across all columns.

---

### 3.3 Coverage Intuition

Each `(RIGHT k, DOWN 1)` block shifts the starting column of the next row.

Because *k* changes:

- The sequence of starting columns behaves like a mixing process modulo A
- Different rows are explored with different horizontal alignments
- Eventually, every cell is visited

This is not a formal proof, but provides strong intuition for coverage.

---

## 4. Algorithm Description

At a high level:
```
for each k in stride_sequence:
repeat k times:
move RIGHT
move DOWN
```

The stride sequence is designed to:
- include prime numbers
- include small values early
- gradually increase coverage diversity

---

## 5. Strengths

- Simple and deterministic
- Requires no knowledge of A, B, or S
- Works well on:
    - wide boards
    - medium aspect ratios
- Effectively breaks periodic alignment issues
- Empirically achieves near-linear coverage on many boards

---

## 6. Limitations

- No formal guarantee of finishing within 35·S for all boards
- Performs worse on:
    - very tall, narrow grids (see braid strategy below)
    - adversarial dimensions where stride interactions are unfavorable
- Performance depends on quality of stride sequence

---

## 7. Complexity Discussion

- Intended behavior: close to O(S)
- However, worst-case bound is not formally proven
- The strategy is best viewed as a heuristic, not a guaranteed optimal solution

---

## 8. Summary

This strategy transforms the blind search problem into a sequence of structured sweeps with varying horizontal strides.

By continuously changing the stride length, it avoids resonance with the unknown grid width and promotes full coverage of the toroidal space.

While not formally bounded by 35·S in all cases, it performs effectively across a wide range of board configurations and represents a strong practical solution.

# Braid Strategy

## Addressing Directional Bias (Experimental)

The prime strategy is horizontally biased:
- Long horizontal runs
- Minimal vertical movement (1 step)

To address this, an alternative “braid-style” traversal was explored.

### Idea

Instead of:
- right p steps → down 1

We interleave both dimensions:

- right p₁ steps → down 1
- right 1 steps → down p₁
- right p₂ steps → down 1
- right 1 steps → down p₂ steps

This creates a more symmetric exploration pattern.

This initially performed badly on exactly square patterns so an extra right step was added to try and create asymmetry however no significant improvement was found

### Motivation

- Reduce directional bias
- Improve performance on near vertical boards
- Increase mixing across both axes

### Status

This approach is still experimental and was not included in the final submission due to:
- incomplete validation
- lack of consistent improvement across all board shapes