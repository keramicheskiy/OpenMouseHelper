import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

class Firebase {
    val config = config()
    val projectName = config.projectName

    fun InitializeRealtimeFirebase() {
        val serviceAccount = FileInputStream("src\\main\\resources\\openmouse-a85e5-firebase-adminsdk-wray8-7e781b6c25.json")
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://openmouse-a85e5-default-rtdb.firebaseio.com/")
            .build()
        FirebaseApp.initializeApp(options)
    }
}