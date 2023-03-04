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
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import config
import java.io.FileInputStream


fun main(args: Array<String>) {
    GlobalScope.launch { // launch a new coroutine in background and continue
        mainFunc()
    }
    while(true) {
        Thread.sleep(2000L)
    }
}



fun mainFunc() {
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
    var isHoldLMBReleased: Boolean = true
    var isHoldRMBReleased: Boolean = true
    lateinit var fullCoordinateAngleX: String
    lateinit var fullCoordinateAngleY: String

    val user = config()
    val login = user.login.decapitalize().replace(".", "+")
    val password = user.password

    Firebase().InitializeRealtimeFirebase()
    val myRef = FirebaseDatabase.getInstance().getReference("OpenMouse/usersData/${login}")
    var realPassword = RealtimeDatabase().getValue(myRef.child("password"))
    var monitorInfo: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
    val screenResolutionX = monitorInfo.width
    val screenResolutionY = monitorInfo.height
    val displayAttitude: Float = (screenResolutionY.toFloat() / screenResolutionX.toFloat())


    var bot: Robot = Robot();


    if (password == realPassword) {
//            maxDeflectAngleX = RealtimeDatabase().getValue(myRef.child("maxDeflectAngle")).toString().toInt()
//            maxDeflectAngleY = (round((maxDeflectAngleX * displayAttitude).toFloat()).toInt())
//            maxFullDeflectionAngleX = maxDeflectAngleX * 20
//            maxFullDeflectionAngleY = maxDeflectAngleY * 20

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                maxDeflectAngleX = dataSnapshot.child("maxDeflectAngle").value.toString().toInt()
                maxDeflectAngleY = (round((maxDeflectAngleX * displayAttitude).toFloat()).toInt())
                maxFullDeflectionAngleX = maxDeflectAngleX * 20
                maxFullDeflectionAngleY = maxDeflectAngleY * 20

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
                            RealtimeDatabase().setValue("", myRef.child("lastMouseCommand"))
                            RealtimeDatabase().setValue("true", myRef.child("didServerHandleCommand"))
                            if (isHoldLMBReleased) {
                                bot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
                                isHoldLMBReleased = false

                            } else if (!isHoldLMBReleased) {
                                bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
                                isHoldLMBReleased = true
                            }
                        }
                        "holdRightMouseButton" -> {
                            RealtimeDatabase().setValue("", myRef.child("lastMouseCommand"))
                            RealtimeDatabase().setValue("true", myRef.child("didServerHandleCommand"))
                            if (isHoldRMBReleased) {
                                bot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
                                isHoldRMBReleased = false

                            } else if (!isHoldRMBReleased) {
                                bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
                                isHoldRMBReleased = true
                            }


                        }
                        "scrollUp" -> {
                            VirtualMouseFunctions().scrollUp(myRef)
                        }
                        "scrollDown" -> {
                            VirtualMouseFunctions().scrollDown(myRef)
                        }
                        "pressMouseWheel" -> {
                            VirtualMouseFunctions().doClickMouseCommand(
                                myRef,
                                InputEvent.BUTTON2_DOWN_MASK
                            )
//                                TODO ("тут я использовал ПКМ, нужно сделать так, чтобы было bot.mousePress(mouseId)," +
//                                        " но без bot.mouseRealize(mouseId) ")
                        }


                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


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
