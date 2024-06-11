import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.awt.*;

public class MoneyCharger extends JFrame {
    private Firestore firestore;
    private String userId;

    public MoneyCharger(Firestore firestore, String userId) {
        this.firestore = firestore;
        this.userId = userId;

        setTitle("Money Charger");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));
        //라벨은 특정 공간을 임의로 차지하지 않음
        Label inputLabel = new Label("Enter amount to charge:");
        add(inputLabel);

        TextField inputField = new TextField();
        add(inputField);

        Button chargeButton = new Button("Charge");
        chargeButton.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(inputField.getText());
                if (amount < 0) {
                    JOptionPane.showMessageDialog(null, "Amount must be positive");
                } else {
                    chargeMoney(amount);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount");
            }
        });
        add(chargeButton);

        setVisible(true);
    }

    private void chargeMoney(int amount) {
        try {
            DocumentReference docRef = firestore.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            //파이어베이스 DB에서 해당 컬렉션의 문서를 가져옴
            if (document.exists()) {
                User user = document.toObject(User.class);
                if (user != null) {
                    int currentMoney = user.getMoney();
                    int newMoney = currentMoney + amount;
                    docRef.update("money", newMoney).get();
                    //DB에 잔고를 업데이트함
                    JOptionPane.showMessageDialog(null, "Charged " + amount + " dollars. New balance: " + newMoney + " dollars");
                } else {
                    JOptionPane.showMessageDialog(null, "User data not found");
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user balance");
        }
    }
}
