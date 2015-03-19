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