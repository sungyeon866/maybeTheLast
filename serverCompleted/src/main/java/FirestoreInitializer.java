import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreInitializer {
    private static Firestore firestore;

    public static void initializeFirestore() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C:/javaprogramming-dcd0a-firebase-adminsdk-fj7gs-2f53fbaa2a.json");
        //파이어베이스 프로젝트의 Firebase Admin SDK에서 생성한 비공개 키 파일 경로
        //만드는법 - 파이어베이스 프로젝트 설정 -> 서비스계정 -> Firebase Admin SDK -> 새 비공개 키 생성
        //파일경로는 반드시 C:/로 시작해야 함
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("javaprogramming-dcd0a")
                .build();
        //파이어베이스 프로젝트 ID
        FirebaseApp.initializeApp(options);
        //내장함수
        firestore = FirestoreClient.getFirestore();
        //내장함수
        System.out.println("Firestore initialized successfully");
    }

    public static Firestore getFirestore() {
        return firestore;
    }
}
