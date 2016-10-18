
This project was done online in the Codility website.

I could not save the question (and I was timed too) so this was the gist of the question:

Given:
* A board size called N
* A String S - that represents the top left and bottom right coordinates of ships
* A String T - that represents the map coordinate of targets

Input example could be: 
* N = 4
* S = "1A 1C, 2B 2B, 4C 4D"
* T = "1B, 2B, 4A 4C"

The output to this example input will be a String "1,2", where;
* 1 = The number of sunked ships (all of the ship for 2B-2B has being hit)
* 2 = The number of ships that have being hit, but not sunk yet - 1A-1C and 4C-4D are hit (4A hit nothing btw)

Basically, given N, S and T; return a String that answers the above.

Note; there were lots of assumptions:
* 0 > N <= 26
* Input is always valid (ships don't fit, ships coordinates are bad, board size is negative, upper case characters)
* Correctness is vital
* Performance is not vital
