import com.google.cloud.firestore.Firestore;
//파이어스토어 라이브러리
import java.io.IOException;
//입출력 예외 라이브러리
public class Main {
    public static void main(String[] args) {
        try {
            FirestoreInitializer.initializeFirestore();
            Firestore firestore = FirestoreInitializer.getFirestore();
            new StartingFrame(firestore);
            //초기화면 실행
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing Firestore: " + e.getMessage());
        }
    }
}