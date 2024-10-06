import kotlin.random.Random

data class Individual(
    val params:MutableList<Float> = MutableList(12){0f}
)

data class geneticAlgo(
    var individuals:MutableList<Individual>
){
    private val population = 100
    private val percentageBest = 0.5f
    private val mutationRate = 0.01f
    private val generations = 1000

    private fun populate(){
        individuals.clear()
        for (i in 0 until population){
            val individual = Individual()
            for (j in individual.params.indices){
                individual.params[j] = (Random.nextFloat()-0.5f)*200
            }
            individuals.add(individual)
        }
    }

    private fun evaluateFitness(player: Individual): Int{
        var myQueue = Queue()
        var myBoard = Board()
        myQueue.gen()
        var tetrises = 0
        var linesCleared = 0
        var pieces = 0
        while (!myBoard.gameOver()){
            pieces++
            myQueue.gen()
            var move = tetrisAI.findMove(myBoard,myQueue,2,player)
            if (move.hold) tetrisAI.hold(myQueue)
            val mino = Tetromino(myQueue.current,move.offset,move.rotations,20,myBoard)
            mino.boardPlace()
            when (myBoard.fullLines()){
                4 -> tetrises++
                in (1..3) -> linesCleared++
                else -> {}
            }
            if (pieces > 1000){
                break
            }

        }
        return tetrises*tetrises*5 + linesCleared
    }

    //crossover and mutation for bottom percentage? printing the best params per population, and saving for every 100 gens?

    private fun getFitnessList(): MutableList<Int>{
        val fitnessList = mutableListOf<Int>()
        for (i in 1..population){
            fitnessList.add(evaluateFitness(individuals[i-1]))
        }
        return fitnessList
    }

    private fun getBestPlayers(fitnessList: MutableList<Int>): List<Individual> {
        val pairs = individuals.mapIndexed{i,individual ->
            Pair(individual,fitnessList[i])
        }

        return pairs.sortedByDescending{it.second}.take((population*percentageBest).toInt()).map{it.first}
    }


    private fun crossover(parent1: Individual, parent2: Individual): Individual{
        var newIndividual = Individual()
        for (i in 0 until 12){
            newIndividual.params[i] = if (Random.nextBoolean()) parent1.params[i] else parent2.params[i]
        }
        return newIndividual
    }

    private fun mutate(individual: Individual){
        for (idx in individual.params.indices){
            if (Random.nextFloat() < mutationRate){
                individual.params[idx] = (Random.nextFloat()-0.5f)*200
            }
        }
    }

    fun evolve(){
        populate()
        for (generation in 1..generations){
            val fitnesses = getFitnessList()
            val bestIndividuals = getBestPlayers(fitnesses)
            val newPopulation:MutableList<Individual> = mutableListOf()
            newPopulation.addAll(bestIndividuals)
            while (newPopulation.size < population){
                val parent1 = bestIndividuals.random()
                val parent2 = bestIndividuals.random()
                val child = crossover(parent1, parent2)
                mutate(child)
                newPopulation.add(child)
            }
            individuals.clear()
            individuals.addAll(newPopulation)

            println("GENERATION #$generation.")
            println("BEST PARAMETERS: ${bestIndividuals[0].params}")
        }
    }
}