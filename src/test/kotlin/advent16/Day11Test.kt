package advent16

import advent16.Day11.Element.*
import org.junit.Test

class Day11Test {

    //The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
    //The second floor contains a hydrogen generator.
    //The third floor contains a lithium generator.
    //The fourth floor contains nothing relevant.
    private fun exampleFacility() = Day11.Facility().apply {
        floor(1).also {
            it.addChip(HYDROGEN)
            it.addChip(LITHIUM)
        }
        floor(2).also {
            it.addGenerator(HYDROGEN)
        }
        floor(3).also {
            it.addGenerator(LITHIUM)
        }
    }

    /*
    F4 .  .  .  .  .
    F3 .  .  .  LG .
    F2 .  HG .  .  .
    F1 E  .  HM .  LM
    */

    @Test(expected = IllegalArgumentException::class)
    fun `Elevator won't go down on floor 1`() {
        val facility = exampleFacility()
        facility.elevatorDown()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Empty elevator won't move`() {
        val facility = exampleFacility()
        facility.elevatorUp()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Powered chip + unpowered chip blows up`() {
        with(exampleFacility()) {
            chipToElevator(HYDROGEN)
            chipToElevator(LITHIUM)
            elevatorUp()
        }
    }

    @Test
    fun example() {
        /*
        Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it can get power from the Hydrogen Generator:

        F4 .  .  .  .  .
        F3 .  .  .  LG .
        F2 E  HG HM .  .
        F1 .  .  .  .  LM
        */

        val facility = exampleFacility()

        // step 1
        facility.run {
            chipToElevator(HYDROGEN)
            elevatorUp()
            printState()
        }

        /*
        Bring both Hydrogen-related items to the third floor, which is safe because the Hydrogen-compatible microchip is getting power from its generator:

        F4 .  .  .  .  .
        F3 E  HG HM LG .
        F2 .  .  .  .  .
        F1 .  .  .  .  LM
        */

        with(facility) {
            chipToElevator(HYDROGEN)
            generatorToElevator(HYDROGEN)
            elevatorUp()
            printState()
        }

        /*
        Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible Microchip back down with you so you can still use the elevator:

        F4 .  .  .  .  .
        F3 .  HG .  LG .
        F2 E  .  HM .  .
        F1 .  .  .  .  LM

        At the first floor, grab the Lithium-compatible Microchip, which is safe because Microchips don't affect each other:

        F4 .  .  .  .  .
        F3 .  HG .  LG .
        F2 .  .  .  .  .
        F1 E  .  HM .  LM
         */

        with(facility) {
            chipToElevator(HYDROGEN)
            elevatorDown()

            chipToElevator(HYDROGEN)
            elevatorDown()
            printState()
        }

        /*
        Bring both Microchips up one floor, where there is nothing to fry them:

        F4 .  .  .  .  .
        F3 .  HG .  LG .
        F2 E  .  HM .  LM
        F1 .  .  .  .  .

        Bring both Microchips up again to floor three, where they can be temporarily connected to their corresponding generators while the elevator recharges, preventing either of them from being fried:

        F4 .  .  .  .  .
        F3 E  HG HM LG LM
        F2 .  .  .  .  .
        F1 .  .  .  .  .


        Bring both Microchips to the fourth floor:

        F4 E  .  HM .  LM
        F3 .  HG .  LG .
        F2 .  .  .  .  .
        F1 .  .  .  .  .
         */
        with(facility) {
            chipToElevator(LITHIUM)
            chipToElevator(HYDROGEN)
            elevatorUp()

            chipToElevator(LITHIUM)
            chipToElevator(HYDROGEN)
            elevatorUp()

            chipToElevator(LITHIUM)
            chipToElevator(HYDROGEN)
            elevatorUp()

            printState()
        }

        with(facility) {
            // Leave the Lithium-compatible microchip on the fourth floor, but bring the Hydrogen-compatible one so you can still use the elevator; this is safe because although the Lithium Generator is on the destination floor, you can connect Hydrogen-compatible microchip to the Hydrogen Generator there:
            chipToElevator(HYDROGEN)
            elevatorDown()

            // Bring both Generators up to the fourth floor, which is safe because you can connect the Lithium-compatible Microchip to the Lithium Generator upon arrival
            generatorToElevator(HYDROGEN)
            generatorToElevator(LITHIUM)
            elevatorUp()

            // Bring the Lithium Microchip with you to the third floor so you can use the elevator:
            chipToElevator(LITHIUM)
            elevatorDown()

            // Bring both Microchips to the fourth floor:
            chipToElevator(LITHIUM)
            chipToElevator(HYDROGEN)
            elevatorUp()

            printState()
        }
    }


}