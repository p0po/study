#随机数字

```java

    private static int genRandomNums(){
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for(int i = 0; i < 6; i++)
            result = result * 10 + array[i];
        return result<100000?result*10:result;
    }
```

#随机字符

```java
    private static String getRandomString(int length){
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            switch (ThreadLocalRandom.current().nextInt(1,4)){
                case 1:
                    builder.append((char) (ThreadLocalRandom.current().nextInt(48, 58)));
                    break;
                case 2:
                    builder.append((char) (ThreadLocalRandom.current().nextInt(65, 91)));
                    break;
                case 3:
                    builder.append((char) (ThreadLocalRandom.current().nextInt(97, 123)));
                    break;
            }

        }
        return builder.toString();
    }

```

#工具类

```
RandomStringUtils

public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    public RandomStringUtils() {
    }

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, (char[])null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if(count == 0) {
            return "";
        } else if(count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else {
            if(start == 0 && end == 0) {
                end = 123;
                start = 32;
                if(!letters && !numbers) {
                    start = 0;
                    end = 2147483647;
                }
            }

            char[] buffer = new char[count];
            int gap = end - start;

            while(count-- != 0) {
                char ch;
                if(chars == null) {
                    ch = (char)(random.nextInt(gap) + start);
                } else {
                    ch = chars[random.nextInt(gap) + start];
                }

                if((!letters || !Character.isLetter(ch)) && (!numbers || !Character.isDigit(ch)) && (letters || numbers)) {
                    ++count;
                } else if(ch >= '\udc00' && ch <= '\udfff') {
                    if(count == 0) {
                        ++count;
                    } else {
                        buffer[count] = ch;
                        --count;
                        buffer[count] = (char)('\ud800' + random.nextInt(128));
                    }
                } else if(ch >= '\ud800' && ch <= '\udb7f') {
                    if(count == 0) {
                        ++count;
                    } else {
                        buffer[count] = (char)('\udc00' + random.nextInt(128));
                        --count;
                        buffer[count] = ch;
                    }
                } else if(ch >= '\udb80' && ch <= '\udbff') {
                    ++count;
                } else {
                    buffer[count] = ch;
                }
            }

            return new String(buffer);
        }
    }

    public static String random(int count, String chars) {
        return chars == null?random(count, 0, 0, false, false, (char[])null, RANDOM):random(count, (char[])chars.toCharArray());
    }

    public static String random(int count, char[] chars) {
        return chars == null?random(count, 0, 0, false, false, (char[])null, RANDOM):random(count, 0, chars.length, false, false, chars, RANDOM);
    }
}

```