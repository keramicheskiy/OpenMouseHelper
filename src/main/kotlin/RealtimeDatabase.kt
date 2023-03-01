import com.google.firebase.database.*
import java.util.concurrent.CountDownLatch


//class RealtimeDatabaseReading {
//}
class RealtimeDatabase {

    lateinit var dataValue: DataSnapshot


//    fun getValue(myRef : DatabaseReference): DataSnapshot {
//
//        myRef.addListenerForSingleValueEvent( object: ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                saveValue(dataSnapshot)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//
//        })
//        return dataValue
//    }
//
//    fun saveValue(dataSnapshot: DataSnapshot) {
//        dataValue = dataSnapshot
//    }


    fun getValue(myRef: DatabaseReference): String? {
        val done = CountDownLatch(1)
        val b = StringBuilder()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // TODO Auto-generated method stub
                if (snapshot.exists()) {
                    b.append(snapshot.value)
//                    b.append(" ")
                    done.countDown()

                } else {
                    b.append("No existe ")
                    done.countDown()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Auto-generated method stub
                b.append("Error: " + error.details)
                done.countDown()
            }
        })
        try {
            done.await()
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return b.toString()
    }

    fun setValue(value: String, myRef: DatabaseReference) {
        myRef.setValue(value) { databaseError: DatabaseError, databaseReference: DatabaseReference ->
            databaseError
            databaseReference
        }
    }



}