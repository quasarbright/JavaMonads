import java.util.function.Function;

public interface Functor<A> {
    <B> Functor<B> fmap(Function<A,B> f);
}
