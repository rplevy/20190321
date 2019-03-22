# assignment

## Usage

```
(make-change 6 [1 5 10 25]) => ({1 6} {1 1, 5 1})

(make-change 6 [3 4])       => ({3 2})

(make-change 6 [1 3 4])     => ({1 6} {3 2} {1 2, 4 1})

(make-change 6 [5 7])       => ()
```

## How it works

1. First, rule out coinsets that are too big in the one-each case
   ```
   (possible-coinsets 6 [1 5 10 25]) => ((1) (5) (1 5))
   ```
   ruling out 10 and 25 or any combination thereof

2. Next generate coincount masks for each possible coinset.
   Coincount masks are generated recursively increasing the maximum count
   to explore, each time checking that the largest mask when applied does
   not exceed the amount
```
   (possible-coincount-masks 6 [1])   => ((1) (2) (3) (4) (5) (6))
   (possible-coincount-masks 6 [5])   => ((1) (2))
   (possible-coincount-masks 6 [1 5]) => ((1 1))
```

3. Apply each of the coincoint masts to the coinset, and for those that match
   the amount, produce a map with coins as keys and coin-counts as values
