import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Robot
import java.lang.Math.round
import java.awt.event.*;
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import java.awt.Dimension
import java.awt.Toolkit


fun main(args: Array<String>) {

    mainFunc()

}



fun mainFunc() {
    val login = "sagay53@mail.ru".replace(".", "+")
    val password = "Sagay228"

    Firebase().InitializeRealtimeFirebase()
    val myRef = FirebaseDatabase
        .getInstance()
        .getReference("OpenMouse/usersData/${login}")
    var realPassword = RealtimeDatabase().getValue(myRef.child("password"))

    var monitorInfo: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
//    val screenResolutionX = monitorInfo.height
//    val screenResolutionY = monitorInfo.width
    val screenResolutionX = 1920
    val screenResolutionY = 1080
    val displayAttitude: Float = (screenResolutionY.toFloat() / screenResolutionX.toFloat())
    var bot: Robot = Robot();


    var maxFullDeflectionAngleY: Int
    var maxFullDeflectionAngleX: Int
    var maxDeflectAngleY: Int
    var maxDeflectAngleX: Int
    var coordinateAngleX: Int
    var coordinateAngleY: Int
    var nextX: Int = 0
    var nextY: Int = 0
    var thisX: Int = 0
    var thisY: Int = 0
    var previousX: Int = 0
    var previousY: Int = 0
    lateinit var fullCoordinateAngleX: String
    lateinit var fullCoordinateAngleY: String
    var isLoggedIn = false
    var isGotExtraInfo = false

    if (password == realPassword) {
        GlobalScope.launch { // launch a new coroutine in background and continue
            maxDeflectAngleX = RealtimeDatabase().getValue(myRef.child("maxDeflectAngle")).toString().toInt()
            maxDeflectAngleY = (round((maxDeflectAngleX * displayAttitude).toFloat()).toInt())
            maxFullDeflectionAngleX = maxDeflectAngleX * 20
            maxFullDeflectionAngleY = maxDeflectAngleY * 20

            println(RealtimeDatabase().getValue(myRef))

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fullCoordinateAngleX = dataSnapshot.child("coordinateX").value.toString()
                    fullCoordinateAngleY = dataSnapshot.child("coordinateY").value.toString()
                    coordinateAngleX = fullCoordinateAngleX.toInt() + maxDeflectAngleX * 10
                    coordinateAngleY = fullCoordinateAngleY.replaceLastCharToRoundedInt().toInt() + maxDeflectAngleY * 10

                    previousX = thisX
                    previousY = thisY
                    thisX = nextX
                    thisY = nextY
                    nextX = round(screenResolutionX.toFloat() / maxFullDeflectionAngleX * coordinateAngleX)
                    nextY = screenResolutionY - round(screenResolutionY.toFloat() / maxFullDeflectionAngleY * coordinateAngleY)


                    moveMouseTo(bot, previousX, previousY, thisX, thisY, nextX, nextY)

                    println(dataSnapshot.child("lastMouseCommand").value)

//                    if (dataSnapshot.child("lastMouseCommand").value.toString() == "leftMouseButton") {
//                        doClickMouseCommand(
//                            "leftMouseButton",
//                            myRef.child("lastMouseCommand"),
//                            InputEvent.BUTTON1_DOWN_MASK
//                        )
//                    }

                    when(dataSnapshot.child("lastMouseCommand").value.toString()) {
                        "leftMouseButton" -> {
                            doClickMouseCommand(
                                "leftMouseButton",
                                myRef.child("lastMouseCommand"),
                                InputEvent.BUTTON1_DOWN_MASK
                            )
                        }
                        "rightMouseButton" -> {
                            doClickMouseCommand(
                                "rightMouseButton",
                                myRef.child("lastMouseCommand"),
                                InputEvent.BUTTON2_DOWN_MASK
                            )
                        }
                        "holdLeftMouseButton" -> {
                            doClickMouseCommand(
                                "holdLeftMouseButton",
                                myRef.child("lastMouseCommand"),
                                InputEvent.BUTTON3_DOWN_MASK
                            )
                        }





                    }




                }



                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        }
        while(true) {
            Thread.sleep(2000L)
        }
    }


}



fun String.getStringFromIndexToIndex(firstIndex: Int = 0, secondIndex: Int): String {
    var resultString = ""
    for (i in firstIndex..secondIndex) {
        resultString += this.get(i)
    }
    return resultString
}

fun moveMouseTo(bot: Robot, previousX: Int, previousY: Int, thisX: Int, thisY: Int, nextX: Int, nextY: Int) {

    if (previousY != nextY && previousY > nextY + 1 || previousY < nextY - 1) {
        var intermediateX = round((thisX + nextX).toFloat() / 2)
        var intermediateY = round((thisY + nextY).toFloat() / 2)
//        println("$intermediateX $intermediateY")

        bot.mouseMove(round((intermediateX + thisX).toFloat() / 2), round((intermediateY + thisY).toFloat() / 2));
        bot.mouseMove(intermediateX, intermediateY);
        bot.mouseMove(round((intermediateX + nextX).toFloat() / 2), round((intermediateY + nextY).toFloat() / 2));
        bot.mouseMove(nextX, nextY);
        Thread.sleep(1)


//        var a = (nextX - thisX)
//        var step = round(a.toFloat() / 6)
//        var count = 0
//        var x = thisX
//        var y = thisY
//        while (x <= nextX && y <= nextY) {
////            Thread.sleep(1)
//            x += step
//            y += step
//            bot.mouseMove(x, y)
//            count++
//            if (count == 6) {
//                break
//            }
//        }
    }
}

fun doClickMouseCommand(path: String, myRef: DatabaseReference, mouseId: Int) {
    var bot: Robot = Robot()
    // TODO короче, в самом приложении постоянно возвращается значение rightMouseButton , так как происходит не апдейт, а полная замена старой информации
    RealtimeDatabase().setValue("", myRef.child(path))
    bot.mousePress(mouseId)
    bot.mouseRelease(mouseId)
}






fun String.replaceLastCharToRoundedInt(): String {

    return when (this.get(this.length - 1)) {
        '0' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
        '1' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
        '2' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
        '3' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
        '4' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
        else -> this.getStringFromIndexToIndex(0, this.length - 2) + "5"

//        '0' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
//        '1' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
//        '2' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
//        '3' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
//        '4' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
//        '5' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
//        else -> this.getStringFromIndexToIndex(0, this.length-2) + "7"


    }
}




//        var a = (nextX - thisX)
//        var step = round(a.toFloat() / 4)
//        var count = 0
//        var x = thisX
//        var y = thisY
//        while(x <= nextX && y <= nextY) {
////            Thread.sleep(1)
//            x += step
//            y += step
//            bot.mouseMove(x,y)
//            count++
//            if (count == 4) {
//                break
//            }



