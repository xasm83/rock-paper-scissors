Simple implementation of rock paper scissors game based on conditional probability and pattern detection.

It calculates  probability of the move after the current one and chooses the top one.
It also detects patterns and switches to prediction using top pattern if any.

Things to improve:

1) Calculate frequencies based on several moves in the past(only one previous move isused atm)

    e.g probability of ROCK after PAPER SCISSORS is other than ROCK after SCISSORS SCISSORS

2) Add corellation(recurrency) with previous frequencies.

3) Adjust ngram calculation to calculate top ngrams for multiple lengths

Current win ratios against semi random user generated sequences:

Computer win rate 0.6000018461595267

Computer win rate 0.5789416433616736

Computer win rate 0.5892535714285714

Computer win rate 0.6249937499553568

Computer win rate 0.6110837447546102
