package course.springdata.advanced.util;

import course.springdata.advanced.entity.Shampoo;

public class PrintUtil {

    public static void printShampoo(Shampoo shampoo) {
        System.out.printf("%s %s %slv.%n", shampoo.getBrand(), shampoo.getSize(), shampoo.getPrice());
    }

}
