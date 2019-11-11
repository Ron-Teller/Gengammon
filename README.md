# Gengammon
Artificial intelligence trained by genetic algorithm to play backgammon

In this Hackathon project we present a supervised training method using genetic algorithm to train a neural network to play Backgammon. The neural network is trained to mimic moves played by professional backgammon players in real games (taken from an online database). The neural network simply rates how good a certain board is for the current player, a higher number being better. Thus, if the current player has rolled his die and has 18 possible ways to move his checkers into a new board state, the board state with the highest ranking will be the best move calculated by the neural network.

<h1>Neural network</h2>

The neural network is comprised of 3 layers.</br></br>
The <b>first</b> layer is the input, consisting of 52 nodes, 26 nodes for each of the two players on the board, the current player and the opponent. Out of the 26 nodes allocated for each player, 24 of the 26 nodes represent the points on the board (1-24), and another 2 points represent the bar (checkers that have been “eaten” or “imprisoned” by the other side), one bar for each side, so for each side you can represent how checkers they have imprisoned, or have been eaten by the opponent. A number in each node represents the amount of checkers occupying the point, so 0 represents no checkers in this point, and 5 means there are five checkers of the corresponding side on that point.</br></br>
The <b>second</b> layer is the hidden layer, it consists of 26 nodes that are connected to all input nodes. The third layer is simply 1 node that is the sum of all the hidden layer nodes.</b></br>
A picture will illustrate this more clearly
