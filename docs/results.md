# Results Summary

## Method

We evaluated strategies using a verifier that:

* Simulates movement
* Tracks visited cells
* Stops at 35 × S steps

---

## Prime Strategy

### Strengths

* High coverage across most board shapes
* Strong performance on rectangular grids
* Avoids short cycles effectively

### Weaknesses

* Some large square boards approach step limit
* Performance depends on prime sequence progression 
* Horizontal bias helps to avoid simple cycles but hurts on very narrow grids.

Note: An experimental braid-style variant was explored to reduce the directional bias of the main prime strategy by interleaving horizontal and vertical prime-length runs more evenly. This idea is documented separately as future work rather than included in the final submission.

---

## Baseline Strategy

### Strengths

* Guaranteed coverage

### Weaknesses

* Extremely inefficient
* Often far exceeds practical limits

---

## Key Insight

The main challenge is avoiding cyclic paths on unknown toroidal grids.

Prime-based stepping:

* Reduces alignment with grid dimensions
* Produces better global coverage

---

## Important Note on "Failures"

A failure doesn't always mean:

> "The strategy cannot reach the apple"

Instead:

* It means it did not do so within 35S steps

---

## Conclusion

The Prime Strategy is a strong practical solution that:

* Works across diverse board sizes
* Maintains efficiency within constraints
* Significantly outperforms simple baselines
