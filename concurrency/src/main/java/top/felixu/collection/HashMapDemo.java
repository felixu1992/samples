package top.felixu.collection;

/**
 * @author felixu
 * @since 2020.06.05
 */
public class HashMapDemo {
    public static void main(String[] args) {
        // 15，23，31，39，47，55，63
        Integer i1 = 15;
        Integer i2 = 23;
        Integer i3 = 31;
        Integer i4 = 39;
        Integer i5 = 47;
        Integer i6 = 55;
        Integer i7 = 63;
        // hash
        int hash1 = hash(i1);
        int hash2 = hash(i2);
        int hash3 = hash(i3);
        int hash4 = hash(i4);
        int hash5 = hash(i5);
        int hash6 = hash(i6);
        int hash7 = hash(i7);

        System.out.println("---------------- before resize -------------------");
        printBeforeResize(i1, hash1);
        printBeforeResize(i2, hash2);
        printBeforeResize(i3, hash3);
        printBeforeResize(i4, hash4);
        printBeforeResize(i5, hash5);
        printBeforeResize(i6, hash6);
        printBeforeResize(i7, hash7);

        System.out.println("---------------- after  resize -------------------");
        printAfterResize(i1, hash1);
        printAfterResize(i2, hash2);
        printAfterResize(i3, hash3);
        printAfterResize(i4, hash4);
        printAfterResize(i5, hash5);
        printAfterResize(i6, hash6);
        printAfterResize(i7, hash7);
    }

    static void printBeforeResize(Integer value, int hash) {
        System.out.println("hash:  " + toBinary(hash, 8));
        System.out.println("index: " + toBinary(hash & (8 - 1), 8));
    }

    static void printAfterResize(Integer value, int hash) {
        System.out.println("hash:  " + toBinary(hash, 8));
        System.out.println("index: " + toBinary(hash & (16 - 1), 8));
        System.out.println("优化后: " + toBinary(hash & 16, 8));
    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static String toBinary(int num, int digits) {
        int value = 1 << digits | num;
        String bs = Integer.toBinaryString(value);
        return bs.substring(1);
    }
}
