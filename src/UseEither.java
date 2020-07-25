public class UseEither {
    public static void main(String[] args) {
        System.out.println(go(32, 2));
        System.out.println(go(10, 0));
        System.out.println(go(10, 3));
        System.out.println(go(10, 2));
        System.out.println(go(100, -4));
    }

    public static Either<String, Integer> go(int x, int y) {
        return
                safeDiv(x, y).bind(divided ->
                safeSqrt(divided).bind(sqrted ->
                Either.asRight(triple(sqrted) + divided)));
    }

    public static Either<String, Integer> safeDiv(int a, int b) {
        if (b == 0) {
            return Either.asLeft("divide by zero: "+a+" / "+b);
        } else if(a % b != 0) {
            return Either.asLeft(""+a+" is not divisible by "+b);
        } else {
            return Either.asRight(a / b);
        }
    }

    public static Either<String, Integer> safeSqrt(int a) {
        if(a < 0) {
            return Either.asLeft("cannot take the square root of a negative: "+a);
        } else {
            double ans = Math.sqrt(a);
            if(ans - Math.floor(ans) > 0.00001) {
                return Either.asLeft(""+a+" is not a perfect square");
            } else {
                return Either.asRight((int) ans);
            }
        }
    }

    public static int triple(int a) {
        return a * 3;
    }
}
