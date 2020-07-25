import java.util.function.Function;

public interface Monad<A> extends Applicative<A> {
    // really should be static, but that's not possible
    Monad<A> ret(A a);
    <B> Monad<B> bind(Function<A, Monad<B>> f);
    <B> Monad<B> sequence(Monad<B> mb);
}
