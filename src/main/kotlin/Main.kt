import com.google.api.gax.httpjson.HttpJsonClientCall.Listener
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.ListenerRegistration
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.logging.Log
import java.awt.Robot
import java.io.FileInputStream
import java.lang.Exception
import java.lang.Math.round
import java.net.http.WebSocket
import java.awt.event.*;
import kotlin.concurrent.thread


fun main(args: Array<String>) {

    val login = "sagay53@mail+ru"
    val password = "Sagay228"

    val screenResolutionX = 1920
    val screenResolutionY = 1080
    val displayAttitude: Float = (screenResolutionY.toFloat() / screenResolutionX.toFloat())
    var bot: Robot = Robot();


    GlobalScope.launch { // launch a new coroutine in background and continue

        val serviceAccount = FileInputStream("openmouse-a85e5-firebase-adminsdk-wray8-7e781b6c25.json")
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://openmouse-a85e5-default-rtdb.firebaseio.com/")
            .build()

        FirebaseApp.initializeApp(options)

        val myRef = FirebaseDatabase
            .getInstance()
            .getReference("OpenMouse/usersData/${login}")




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

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!isLoggedIn) {
                    var dbPassword = dataSnapshot.child("password").value.toString()
                    if (password == dbPassword) {
                        isLoggedIn = true
                    } else {
                        throw Exception("Invalid password")
                    }
                }


                maxDeflectAngleX = dataSnapshot.child("maxDeflectAngle").value.toString().toInt()
                maxDeflectAngleY = (round((maxDeflectAngleX * displayAttitude).toFloat()).toInt())

                maxFullDeflectionAngleX = maxDeflectAngleX * 20
                maxFullDeflectionAngleY = maxDeflectAngleY * 20

                fullCoordinateAngleX = dataSnapshot.child("coordinateX").value.toString()
                fullCoordinateAngleY = dataSnapshot.child("coordinateY").value.toString()
                coordinateAngleX = fullCoordinateAngleX.toInt() + maxDeflectAngleX * 10
                coordinateAngleY = fullCoordinateAngleY.roundLastCharToOdd().toInt() + maxDeflectAngleY * 10


                previousX = thisX
                previousY = thisY

                thisX = nextX
                thisY = nextY

                nextX = round(screenResolutionX.toFloat() / maxFullDeflectionAngleX * coordinateAngleX).toInt()
                nextY = screenResolutionY - round(screenResolutionY.toFloat() / maxFullDeflectionAngleY * coordinateAngleY).toInt()



//                println("$coordinateAngleX, $coordinateAngleY")
//                Thread.sleep(20)
//                println("$nextX, $nextY")

                moveMouseTo(bot, previousX, previousY, thisX, thisY, nextX, nextY)
                if (dataSnapshot.child("lastMouseCommand").toString() == "leftMouseButton") {
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    myRef.child("lastMouseCommand").setValue(" ") { a, b ->
                        println()
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
fun String.roundLastCharToOdd(): String {
    var lastChar = this[this.length-1]

    return when(lastChar) {
        '0' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
        '1' -> this.getStringFromIndexToIndex(0, this.length-2) + '1'
        '2' -> this.getStringFromIndexToIndex(0, this.length-2) + '2'
        '3' -> this.getStringFromIndexToIndex(0, this.length-2) + '3'
        '4' -> this.getStringFromIndexToIndex(0, this.length-2) + '4'
        '5' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
        '6' -> this.getStringFromIndexToIndex(0, this.length-2) + '6'
        '7' -> this.getStringFromIndexToIndex(0, this.length-2) + '7'
        '8' -> this.getStringFromIndexToIndex(0, this.length-2) + '8'
//        '9' -> this.getStringFromIndexToIndex(0, this.length-2) + '8'
        else -> {this.getStringFromIndexToIndex(0, this.length-2) + '9'}

//        '0' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '1' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '2' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '3' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '4' -> this.getStringFromIndexToIndex(0, this.length-2) + '4'
//        '5' -> this.getStringFromIndexToIndex(0, this.length-2) + '4'
//        '6' -> this.getStringFromIndexToIndex(0, this.length-2) + '4'
//        '7' -> this.getStringFromIndexToIndex(0, this.length-2) + '8'
//        '8' -> this.getStringFromIndexToIndex(0, this.length-2) + '8'
//        else -> {this.getStringFromIndexToIndex(0, this.length-2) + '8'}

//        '0' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '1' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '2' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        '3' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
//        '4' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
//        '5' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
//        '6' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
//        '7' -> this.getStringFromIndexToIndex(0, this.length-2) + '5'
//        '8' -> this.getStringFromIndexToIndex(0, this.length-2) + '0'
//        else -> {this.getStringFromIndexToIndex(0, this.length-2) + '0'}

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
//            var bot: Robot = Robot();
        var intermediateX = round((thisX + nextX).toFloat()/2)
        var intermediateY = round((thisY + nextY).toFloat()/2)
        var intermediateX1 = round((thisX + intermediateX).toFloat()/2)
        var intermediateY1 = round((thisY + intermediateY).toFloat()/2)
        var intermediateX2 = round((nextX + intermediateX).toFloat()/2)
        var intermediateY2 = round((nextY + intermediateY).toFloat()/2)

        println("$intermediateX $intermediateY")

        bot.mouseMove(thisX, thisY);
        bot.mouseMove(round((thisX + intermediateX1).toFloat()/2), round((thisY + intermediateY1).toFloat()/2));
        bot.mouseMove(intermediateX1, intermediateY1);
        bot.mouseMove(round((intermediateX1 + intermediateX).toFloat()/2), round((intermediateY1 + intermediateY).toFloat()/2));
        bot.mouseMove(intermediateX, intermediateY);
        bot.mouseMove(round((intermediateX + intermediateX2).toFloat()/2), round((intermediateY + intermediateY2).toFloat()/2));
        bot.mouseMove(intermediateX2, intermediateY2);
        bot.mouseMove(round((intermediateX2 + nextX).toFloat()/2), round((intermediateY2 + nextY).toFloat()/2));
        bot.mouseMove(nextX, nextY);

        var a = (nextX - thisX)

        Thread.sleep(2)

//        for (i in 0..a+1 step 2) {
//            Thread.sleep(1)
//            bot.mouseMove(thisX + i, thisY + i);
//
//
//        }


    }






//        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

}