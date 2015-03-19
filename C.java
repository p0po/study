import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class C {
    double[] nums = new double[6];
    //double[] nums = {9.0, 76.0, 72.0, 42.0, 2.0, 49.0,55.0,1.0 };
    int go = 24;
    Random r = new Random();
    List<OneNum> onList = new ArrayList<OneNum>();
    List<OneNum> sourceList = new ArrayList<OneNum>();

    public void init() {
        System.out.println("=============================");
        for (int i = 0; i < nums.length; i++) {
            nums[i] = r.nextInt(1000);
            System.out.print(nums[i] + " ");
            OneNum on = new OneNum();
            on.setMe(nums[i]);
            int[] meId = {i};
            on.setpId(meId);
            on.setConnection(nums[i] + "");
            onList.add(on);
            sourceList.addAll(onList);
        }
        System.out.print("【" + go + "】 ");
        System.out.println("");
        System.out.println("=============================");
        kuochong();
    }

    long startTime = Calendar.getInstance().getTimeInMillis();
    int length = 0;

    public void kuochong() {

        int last = 0;

        do {
            int nowlength = onList.size();
            for (int i = 0; i < (nowlength - last); i++) {
                for (int j = last; j < nowlength; j++) {
                    che(onList.get(i), onList.get(j));
                }
            }
            last = nowlength;
        } while (length < nums.length);

        boolean haveOne = false;
        for (OneNum on : onList) {
            if (on.getMe() == go) {
                //System.out.println(on.getConnection()+"="+go);
                haveOne = true;
            }
        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        if (!haveOne) {
            System.out.println("没有 耗时【" + (endTime - startTime) + " ms】");
        }

        System.out.println("=========所需总时间===========" + (endTime - startTime) / 1000.0 + " s");
    }

    public void che(OneNum on1, OneNum on2) {
        if (on1.noRealition(on2) && on1.getMe() >= on2.getMe()) {
            int[] pid1 = on1.getpId();
            int[] pid2 = on2.getpId();
            int[] pid = new int[pid1.length + pid2.length];
            for (int i = 0; i < pid.length; i++) {
                if (i < pid1.length) {
                    pid[i] = pid1[i];
                } else {
                    pid[i] = pid2[i - pid1.length];
                }
            }
            length = pid.length > length ? pid.length : length;
            OneNum onJia = new OneNum();
            onJia.setMe(on1.getMe() + on2.getMe());
            onJia.setpId(pid);
            onJia.setConnection(on1.getConnection() + " + " + on2.getConnection());
            onList.add(onJia);

            if (on1.getMe() + on2.getMe() == go) {
                long thisTime = Calendar.getInstance().getTimeInMillis();
                System.out.println(on1.getConnection() + " + " + on2.getConnection() + " = " + go + "【耗时:" + (thisTime - startTime) + " ms】");
            }

            OneNum onCheng = new OneNum();
            onCheng.setMe(on1.getMe() * on2.getMe());
            onCheng.setpId(pid);
            onCheng.setConnection("(" + on1.getConnection() + ") * (" + on2.getConnection() + ")");
            onList.add(onCheng);

            if (on1.getMe() * on2.getMe() == go) {
                long thisTime = Calendar.getInstance().getTimeInMillis();
                System.out.println("(" + on1.getConnection() + ") * (" + on2.getConnection() + ") = " + go + "【耗时:" + (thisTime - startTime) + " ms】");
            }


            OneNum onJian = new OneNum();
            onJian.setMe(on1.getMe() - on2.getMe());
            onJian.setpId(pid);
            onJian.setConnection(on1.getConnection() + " - (" + on2.getConnection() + ")");
            onList.add(onJian);

            if (on1.getMe() - on2.getMe() == go) {
                long thisTime = Calendar.getInstance().getTimeInMillis();
                System.out.println(on1.getConnection() + " - (" + on2.getConnection() + ") = " + go + "【耗时:" + (thisTime - startTime) + " ms】");

            }
            if (on2.getMe() != 0) {
                OneNum onChu = new OneNum();
                onChu.setMe(on1.getMe() / on2.getMe());
                onChu.setpId(pid);
                onChu.setConnection("(" + on1.getConnection() + ") / (" + on2.getConnection() + ")");
                onList.add(onChu);

                if (on1.getMe() / on2.getMe() == go) {
                    long thisTime = Calendar.getInstance().getTimeInMillis();
                    System.out.println("(" + on1.getConnection() + ") / (" + on2.getConnection() + ") = " + go + "【耗时:" + (thisTime - startTime) + " ms】");
                }
            }

        }
    }

    private class OneNum {
        int[] pId;
        double me;
        String connection;

        public double getMe() {
            return me;
        }

        public String getConnection() {
            return connection;
        }

        public void setConnection(String connection) {
            this.connection = connection;
        }

        public void setMe(double me) {
            this.me = me;
        }


        public boolean noRealition(OneNum on) {
            int[] pid1 = this.getpId();
            int[] pid2 = on.getpId();


            if (pid1.length == 0 && pid2.length == 0) {
                if (on.getMe() == this.getMe()) {
                    return false;
                }

            }


            if (pid2.length == 0 && pid1.length != 0) {
                for (int i = 0; i < pid1.length; i++) {
                    if (on.getMe() == pid1[i]) {
                        return false;
                    }
                }
            }


            if (pid1.length == 0 && pid2.length != 0) {
                for (int i = 0; i < pid2.length; i++) {
                    if (this.getMe() == pid2[i]) {
                        return false;
                    }
                }
            }

            for (int i = 0; i < pid1.length; i++) {
                for (int j = 0; j < pid2.length; j++) {
                    if (pid1[i] == pid2[j]) {
                        return false;
                    }
                }
            }


            return true;
        }

        public int[] getpId() {
            return pId;
        }

        public void setpId(int[] pId) {
            this.pId = pId;
        }
    }


    public static void main(String[] args) {
        C c = new C();
        c.init();
    }
}