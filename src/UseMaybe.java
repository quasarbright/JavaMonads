public class UseMaybe {
    public static void main(String[] args) {
        System.out.println(go(32, 2));
        System.out.println(go(10, 0));
        System.out.println(go(10, 3));
        System.out.println(go(10, 2));
    }

    public static Maybe<Integer> go(int x, int y) {
        Maybe<Integer> mx = Maybe.just(x);
        return (safeDiv(x, y)
                .bind(divided ->
                safeSqrt(divided).bind(sqrted ->
                mx.ret(triple(sqrted) + divided)))); // using mx statically
    }

    public static Maybe<Integer> safeDiv(int a, int b) {
        if (b == 0 || a % b != 0) {
            return Maybe.nothing();
        } else {
            return Maybe.just(a / b);
        }
    }

    public static Maybe<Integer> safeSqrt(int a) {
        if(a < 0) {
            return Maybe.nothing();
        } else {
            double ans = Math.sqrt(a);
            if(ans - Math.floor(ans) > 0.00001) {
                return Maybe.nothing();
            } else {
                return Maybe.just((int) ans);
            }
        }
    }

    public static int triple(int a) {
        return a * 3;
    }
}
