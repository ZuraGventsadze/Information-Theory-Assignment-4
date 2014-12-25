#! /usr/bin/python

from sys import *

ansFile = open(argv[1])
outFile = open(argv[2])

k = int(ansFile.readline())
k_ = int(outFile.readline())

if k != k_:
	print 'Wrong number of elements'
	exit()

ansSeq = [int(inp) for inp in ansFile.readline().split()]
outSeq = [int(inp) for inp in outFile.readline().split()]

if ansSeq != outSeq:
	print 'Wrong sequence'
	exit()

print 'Correct'