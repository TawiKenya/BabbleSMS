package ke.co.tawi.babblesms.server.servlet.account.settings;

import java.util.Random;

public class PassGen{
    
    private static final String dCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String sChar = "!@#$%^&*";
    private static final String intChar = "0123456789";
    private static Random r = new Random();
    private static String pass = "";

    public String RadPass(String password) {
 
        while (pass.length () != 16){
            int rPick = r.nextInt(4);
            if (rPick == 0){
                int spot = r.nextInt(25);
                pass += dCase.charAt(spot);
            } else if (rPick == 1) {
                int spot = r.nextInt (25);
                pass += uCase.charAt(spot);
            } else if (rPick == 2) {
                int spot = r.nextInt (7);
                pass += sChar.charAt(spot);
            } else if (rPick == 3){
                int spot = r.nextInt (9);
                pass += intChar.charAt (spot);
            }
        }
        return pass;
        //System.out.println ("Generated Password: " + pass);
    }
}