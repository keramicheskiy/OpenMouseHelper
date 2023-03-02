import com.google.firebase.database.DatabaseReference
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException
import java.lang.Math.round


class VirtualMouseFunctions {
    var bot: Robot = Robot();

    fun moveMouseTo(previousX: Int, previousY: Int, thisX: Int, thisY: Int, nextX: Int, nextY: Int) {
        if (previousY != nextY && previousY > nextY + 1 || previousY < nextY - 1) {
            var intermediateX = round((thisX + nextX).toFloat() / 2)
            var intermediateY = round((thisY + nextY).toFloat() / 2)
            bot.mouseMove(round((intermediateX + thisX).toFloat() / 2), round((intermediateY + thisY).toFloat() / 2));
            bot.mouseMove(intermediateX, intermediateY);
            bot.mouseMove(round((intermediateX + nextX).toFloat() / 2), round((intermediateY + nextY).toFloat() / 2));
            bot.mouseMove(nextX, nextY);
            Thread.sleep(1)
        }
    }

    fun doClickMouseCommand(myRef: DatabaseReference, mouseId: Int) {
        // TODO короче, в самом приложении постоянно возвращается значение rightMouseButton , так как происходит не апдейт, а полная замена старой информации
        RealtimeDatabase().setValue("", myRef.child("lastMouseCommand"))
        RealtimeDatabase().setValue("true", myRef.child("didServerHandleCommand"))
        bot.mousePress(mouseId)
        bot.mouseRelease(mouseId)
    }

    fun scrollUp(myRef: DatabaseReference) {
        RealtimeDatabase().setValue("", myRef.child("lastMouseCommand"))
        RealtimeDatabase().setValue("true", myRef.child("didServerHandleCommand"))
        bot.keyPress(KeyEvent.VK_PAGE_UP)
        bot.keyRelease(KeyEvent.VK_PAGE_UP)
    }

   fun scrollDown(myRef: DatabaseReference) {
       RealtimeDatabase().setValue("", myRef.child("lastMouseCommand"))
       RealtimeDatabase().setValue("true", myRef.child("didServerHandleCommand"))
       bot.keyPress(KeyEvent.VK_PAGE_DOWN)
       bot.keyRelease(KeyEvent.VK_PAGE_DOWN)
   }




}