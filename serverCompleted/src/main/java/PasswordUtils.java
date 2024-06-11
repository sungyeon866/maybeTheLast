import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {

    // 솔트 생성
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // 비밀번호 + 솔트를 해시
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //해싱 - 44자 길이의 특정 해시값을 생성
        //8개의 소수[2,3,5,7,11,13,17,19]의 제곱 근의 소수점 이하 32bit를 사용하여 해시값을 생성
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return hashedPassword;
    }

    // 비밀번호 비교
    public static boolean comparePasswords(String inputPassword, byte[] salt, byte[] storedHashedPassword) throws NoSuchAlgorithmException {
        byte[] inputHashedPassword = hashPassword(inputPassword, salt);
        return Arrays.equals(inputHashedPassword, storedHashedPassword);
    }
    //알고리즘이 사용 가능하지 않은 경우 NoSuchAlgorithmException 예외 발생
    //null값이거나 존재하지 않을때 발생
    public static void main(String[] args) {
        try {
            // 사용자 비밀번호와 솔트 생성 (예제용)
            String password = "userPassword123";
            byte[] salt = generateSalt();

            // 비밀번호 해싱
            byte[] hashedPassword = hashPassword(password, salt);
            System.out.println("storedHashedPassword: " + Arrays.toString(hashedPassword));
            // 예제: 저장된 해시와 솔트는 DB에 저장되어 있다고 가정
            // 사용자가 입력한 비밀번호를 가져와 비교
            String inputPassword = "userPassword123"; // 사용자가 입력한 비밀번호
            // 비교
            boolean isMatch = comparePasswords(inputPassword, salt, hashedPassword);

            System.out.println("passwordCorrespond: " + isMatch);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}