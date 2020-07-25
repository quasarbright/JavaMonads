import java.util.ArrayList;
import java.util.List;

public class UseList {

    public static ListMonad<Integer> plusMinus(int x) {
        return ListMonad.of(x, -x);
    }

    public static ListMonad<Integer> factors(int x) {
        if(x < 0) {
            return ListMonad.of();
        } else {
            ListMonad<Integer> ans = ListMonad.of();
            for(int i = 1; i <= (int) Math.sqrt(x); i++) {
                if(x % i == 0) {
                    ans.add(i);
                    ans.add(x / i);
                }
            }
            return ans;
        }
    }

    /**
     * returns +- the square root if x is a perfect square, empty otherwise
     */
    public static ListMonad<Integer> inverseSquare(int x) {
        Maybe<Integer> mSqrt = UseMaybe.safeSqrt(x);
        return mSqrt
                .fmap(sqrt -> ListMonad.of(sqrt, -sqrt))
                .orElse(ListMonad.of());
    }

    public static int add(int x, int y) {
        return x + y;
    }

    public static void main(String[] args) {
        ListMonad<Integer> nums = ListMonad.of(12, 13);
        System.out.println(nums.bind(UseList::go));
        // list comprehension. multiplies all numbers by all factors
        ListMonad<Integer> ns =
                nums.bind(a ->
                nums.bind(UseList::factors).bind(b ->
                nums.ret(a * b))); // just use nums statically
        System.out.println(ns);
    }

    public static ListMonad<Integer> go(int x) {
        return ListMonad.of(x)
            .bind(UseList::factors)
            .bind(UseList::plusMinus)
            .bind(UseList::inverseSquare)
//            .fmap(a -> add(a, 10))
                ;
    }
}
