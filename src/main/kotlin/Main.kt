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
import BaseFunctions


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
    val myRef1 = FirebaseDatabase
        .getInstance()
        .getReference("OpenMouse/usersData/${login}")
    var realPassword = RealtimeDatabase().getValue(myRef.child("password"))

    var monitorInfo: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
//    val screenResolutionX = monitorInfo.height
//    val screenResolutionY = monitorInfo.width
    val screenResolutionX = 1920
    val screenResolutionY = 1080
    val displayAttitude: Float = (screenResolutionY.toFloat() / screenResolutionX.toFloat())



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

    if (password == realPassword) {
        GlobalScope.launch { // launch a new coroutine in background and continue
            maxDeflectAngleX = RealtimeDatabase().getValue(myRef.child("maxDeflectAngle")).toString().toInt()
            maxDeflectAngleY = (round((maxDeflectAngleX * displayAttitude).toFloat()).toInt())
            maxFullDeflectionAngleX = maxDeflectAngleX * 20
            maxFullDeflectionAngleY = maxDeflectAngleY * 20

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


                    VirtualMouseFunctions().moveMouseTo(previousX, previousY, thisX, thisY, nextX, nextY)


                    if (dataSnapshot.child("didServerHandleCommand").value.toString() == "false") {
                        when (dataSnapshot.child("lastMouseCommand").value.toString()) {
                            "leftMouseButton" -> {
                                VirtualMouseFunctions().doClickMouseCommand(
                                    myRef,
                                    InputEvent.BUTTON1_DOWN_MASK
                                )
                            }

                            "rightMouseButton" -> {
                                VirtualMouseFunctions().doClickMouseCommand(
                                    myRef,
                                    InputEvent.BUTTON3_DOWN_MASK
                                )
                            }

                            "holdLeftMouseButton" -> {
                                VirtualMouseFunctions().doClickMouseCommand(
                                    myRef,
                                    InputEvent.BUTTON2_DOWN_MASK
                                )
//                                TODO ("тут я использовал ПКМ, нужно сделать так, чтобы было bot.mousePress(mouseId)," +
//                                        " но без bot.mouseRealize(mouseId) ")
                            }
                            "holdRightMouseButton" -> {
                                VirtualMouseFunctions().doClickMouseCommand(
                                    myRef,
                                    InputEvent.BUTTON3_DOWN_MASK
                                )
//                                TODO ("тут я использовал ПКМ, нужно сделать так, чтобы было bot.mousePress(mouseId)," +
//                                        " но без bot.mouseRealize(mouseId) ")
                            }
                            "scrollUp" -> {
                                VirtualMouseFunctions().scrollUp(myRef)
                            }
                            "scrollDown" -> {
                                VirtualMouseFunctions().scrollDown(myRef)
                            }


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







fun String.replaceLastCharToRoundedInt(): String {

    return when (this.get(this.length - 1)) {
//        '0' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
//        '1' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
//        '2' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
//        '3' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
//        '4' -> this.getStringFromIndexToIndex(0, this.length - 2) + "0"
//        else -> this.getStringFromIndexToIndex(0, this.length - 2) + "5"

        '0' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
        '1' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
        '2' -> this.getStringFromIndexToIndex(0, this.length-2) + "0"
        '3' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
        '4' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
        '5' -> this.getStringFromIndexToIndex(0, this.length-2) + "4"
        else -> this.getStringFromIndexToIndex(0, this.length-2) + "7"
    }
}
