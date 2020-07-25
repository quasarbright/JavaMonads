import java.util.function.Function;

public interface Applicative<A> extends Functor<A> {
    // really should be static, but that's not possible
    Applicative<A> pure(A a);
    <B> Applicative<B> getAppliedBy(Applicative<Function<A,B>> af);
}
