#! /usr/bin/python

from sys import *

ansFile = open(argv[1])
datFile = open(argv[2])
outFile = open(argv[3])

isParity = ansFile.readline().strip()
isParity_ = outFile.readline().strip()

if isParity != isParity_:
	print 'Wrong answer'
	exit()

ansSeq = [int(inp) for inp in ansFile.readline().split()]
outSeq = [int(inp) for inp in outFile.readline().split()]

p = int(datFile.readline())

for ansLeading in reversed(ansSeq):
	if ansLeading != 0:
		break

for outLeading in reversed(outSeq):
	if outLeading != 0:
		break

for i in range(len(ansSeq)):
	if (ansSeq[i] * outLeading) % p != (outSeq[i] * ansLeading) % p:
		print 'Wrong polynomial coefficient at degree', i
		exit()

print 'Correct'