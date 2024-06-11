import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class StartingFrame extends Frame {
    public StartingFrame(Firestore firestore) {
        Frame frame = new Frame();
        frame.setSize(300, 200);
        frame.setTitle("Login");
        setLayout(new BorderLayout());
        //버튼 간 간격 조절을 위해 BorderLayout 사용
        LoginTextFieldPanel loginTextFieldPanel = new LoginTextFieldPanel();
        frame.add(loginTextFieldPanel, BorderLayout.NORTH);

        Login login = new Login(loginTextFieldPanel, firestore);
        //로그인 텍스트 필드 생성
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        Button loginButton = new LoginButton(login, frame);
        //로그인 버튼 생성
        buttonPanel.add(loginButton);

        Button signUpButton = new SignUpButton(frame, firestore);
        buttonPanel.add(signUpButton);
        //회원가입 버튼 생성
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        //창 닫기 버튼을 누르면 프로그램 종료
        frame.setVisible(true);
    }
}

class SignUpButton extends Button {
    public SignUpButton(Frame frame, Firestore firestore) {
        super("Sign up");
        setPreferredSize(new Dimension(100, 30));
        //버튼 크기 설정
        //setPreferredSize 메소드만 사용하면 크기가 조절이 가능했었음
        addActionListener(e -> {
            frame.dispose();
            new SignupAppAWT(firestore);
        });
    }
}
class LoginButton extends Button {
    public LoginButton(Login login, Frame frame) {
        super("Login");
        setPreferredSize(new Dimension(100, 30));
        addActionListener(e -> {
            boolean check = login.login();
            if(check){
                frame.dispose();
            }
            else{
                JOptionPane.showMessageDialog(null, "Login failed");
            }
        });
    }
}