# Card Fraud Detector

Consider the following credit card fraud detection algorithm:
A credit card transaction is comprised of the following elements;
hashed credit card number
timestamp - of format 'year-month-dayThour:minute:second'
price - of format 'dollars.cents'

Transactions are to be received as a comma separated string of elements eg. '10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00'

A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit
card number over a 24-hour sliding window period exceeds the price threshold.

The file passed to your app will contain a sequence of transactions in chronological order.
