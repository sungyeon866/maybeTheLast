import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class SignupAppAWT extends Frame {
    private LoginTextFieldPanel loginTextFieldPanel;
    private Label messageLabel;
    private Button signupButton;
    private Firestore firestore;

    public SignupAppAWT(Firestore firestore) {
        this.firestore = firestore;
        setTitle("회원가입");
        setSize(300, 200);
        setLayout(new BorderLayout());
        //버튼 간 간격 조절을 위해 BorderLayout 사용
        loginTextFieldPanel = new LoginTextFieldPanel();
        signupButton = new Button("Sign up");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.addActionListener(this::actionPerformed);

        add(loginTextFieldPanel.getIdField().getParent(), BorderLayout.NORTH);

        Panel panel = new Panel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(signupButton);
        messageLabel = new Label("");
        messageLabel.setPreferredSize(new Dimension(200, 30));
        panel.add(messageLabel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String id = loginTextFieldPanel.getIdFieldText();
        String password = loginTextFieldPanel.getPasswordFieldText();

        if (id.isEmpty() || password.isEmpty()) {
            messageLabel.setText("ID or Password cannot be empty");
        } else {
            try {
                DocumentReference docRef = firestore.collection("users").document(id);
                ApiFuture<DocumentSnapshot> future = docRef.get();
                DocumentSnapshot document = future.get();
                //파이어베이스 DB에서 해당 컬렉션의 DB를 가져옴
                //내부 원리는 정확히 안 찾아봄
                if (document.exists()) {
                    messageLabel.setText("ID already taken");
                } else {
                    byte[] salt = PasswordUtils.generateSalt();
                    //랜덤값 솔트 생성 ("SHA-256" 알고리즘 사용)
                    byte[] hashedPassword = PasswordUtils.hashPassword(password, salt);

                    User newUser = new User(id, Base64.getEncoder().encodeToString(hashedPassword), 0, 0, Base64.getEncoder().encodeToString(salt));
                    //솔트와 해시된 비밀번호를 String형으로 DB에 저장
                    ApiFuture<WriteResult> result = docRef.set(newUser);
                    result.get();
                    messageLabel.setText("Signup successful");
                    loginTextFieldPanel.clearFields();
                    //ID, 비밀번호 입력창 초기화
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dispose();
                            new StartingFrame(firestore);
                        }
                    }, 2000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setText("Error saving user data");
            }
        }
    }
}
