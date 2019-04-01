public class IdCard {

    static String myIdPre = "123456789101112234";

    public static void main(String[] args) {
        check(myIdPre);
    }

    /**
     * 将身份证号码的第1位数字与7相乘；
     * 将身份证号码的第2位数字与9相乘；
     * 将身份证号码的第3位数字与10相乘；
     * 将身份证号码的第4位数字与5相乘；
     * 将身份证号码的第5位数字与8相乘；
     * 将身份证号码的第6位数字与4相乘；
     * 将身份证号码的第7位数字与2相乘；
     * 将身份证号码的第8位数字与1相乘；
     * 将身份证号码的第9位数字与6相乘；
     * 将身份证号码的第10位数字与3相乘；
     * 将身份证号码的第11位数字与7相乘；
     * 将身份证号码的第12位数字与9相乘；
     * 将身份证号码的第13位数字与10相乘；
     * 将身份证号码的第14位数字与5相乘；
     * 将身份证号码的第15位数字与8相乘；
     * 将身份证号码的第16位数字与4相乘；
     * 将身份证号码的第17位数字与2相乘。
     */
    static char[] r = {'1','0','X','9','8','7','6','5','4','3','2'};
    public static boolean check(String id) {
        if(id==null||id.trim().length()<18){
            return false;
        }

        int numCount = 18;
        if(id.endsWith("X")||id.endsWith("x")){
            numCount = 17;
        }

        for (int i = 0; i < numCount; i++) {
            if (Character.isDigit(id.charAt(i)) == false) {
                return false;
            }
        }

        int sum = Character.digit(id.charAt(0),10)*7
                +Character.digit(id.charAt(1),10)*9
                +Character.digit(id.charAt(2),10)*10
                +Character.digit(id.charAt(3),10)*5
                +Character.digit(id.charAt(4),10)*8
                +Character.digit(id.charAt(5),10)*4
                +Character.digit(id.charAt(6),10)*2
                +Character.digit(id.charAt(7),10)*1
                +Character.digit(id.charAt(8),10)*6
                +Character.digit(id.charAt(9),10)*3
                +Character.digit(id.charAt(10),10)*7
                +Character.digit(id.charAt(11),10)*9
                +Character.digit(id.charAt(12),10)*10
                +Character.digit(id.charAt(13),10)*5
                +Character.digit(id.charAt(14),10)*8
                +Character.digit(id.charAt(15),10)*4
                +Character.digit(id.charAt(16),10)*2;

        int  mod = sum%11;
        char crc = r[mod];
        return crc==id.charAt(17);
    }
}
