import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Maybe<A> implements Monad<A> {
    private final Optional<A> data;

    private Maybe(A a) {
        this.data = Optional.of(a);
    }

    private Maybe() {
        this.data = Optional.empty();
    }

    private Maybe(Optional<A> data) {
        this.data = data;
    }

    public static <A> Maybe<A> just(A a) {
        return new Maybe<>(a);
    }

    public static <A> Maybe<A> nothing() {
        return new Maybe<>();
    }

    @Override
    public Monad<A> ret(A a) {
        return this.pure(a);
    }

    @Override
    public <B> Maybe<B> bind(Function<A, Monad<B>> f) {
        if(this.data.isPresent()) {
            Monad<B> mb = f.apply(this.data.get());
            if(mb instanceof Maybe<?>) {
                return (Maybe<B>) mb;
            } else {
                throw new IllegalArgumentException("tried to bind monadic function to Maybe, but it didn't return a maybe");
            }
        } else {
            return nothing();
        }
    }

    @Override
    public <B> Maybe<B> sequence(Monad<B> mb) {
        return this.bind(ignored -> mb);
    }

    @Override
    public Maybe<A> pure(A a) {
        return just(a);
    }

    @Override
    public <B> Maybe<B> getAppliedBy(Applicative<Function<A, B>> af) {
        if(af instanceof Maybe<?>) {
            Maybe<Function<A, B>> mf = (Maybe<Function<A, B>>) af;
            if(this.isPresent() && mf.isPresent()) {
                Function<A, B> f = mf.get();
                A a = this.get();
                return just(f.apply(a));
            } else {
                return nothing();
            }
        } else {
            throw new IllegalArgumentException("Maybe can only be applied by Maybes");
        }
    }

    @Override
    public <B> Maybe<B> fmap(Function<A, B> f) {
        return new Maybe<>(this.map(f));
    }

    @Override
    public String toString() {
        return this.map(a -> "Just " + a).orElse("Nothing");
    }

    ////// optional delegations


    public static <T> Optional<T> empty() {
        return Optional.empty();
    }

    public static <T> Optional<T> of(T value) {
        return Optional.of(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return Optional.ofNullable(value);
    }

    public A get() {
        return data.get();
    }

    public boolean isPresent() {
        return data.isPresent();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void ifPresent(Consumer<? super A> action) {
        data.ifPresent(action);
    }

    public void ifPresentOrElse(Consumer<? super A> action, Runnable emptyAction) {
        data.ifPresentOrElse(action, emptyAction);
    }

    public Optional<A> filter(Predicate<? super A> predicate) {
        return data.filter(predicate);
    }

    public <U> Optional<U> map(Function<? super A, ? extends U> mapper) {
        return data.map(mapper);
    }

    public <U> Optional<U> flatMap(Function<? super A, ? extends Optional<? extends U>> mapper) {
        return data.flatMap(mapper);
    }

    public Optional<A> or(Supplier<? extends Optional<? extends A>> supplier) {
        return data.or(supplier);
    }

    public Stream<A> stream() {
        return data.stream();
    }

    public A orElse(A other) {
        return data.orElse(other);
    }

    public A orElseGet(Supplier<? extends A> supplier) {
        return data.orElseGet(supplier);
    }

    public A orElseThrow() {
        return data.orElseThrow();
    }

    public <X extends Throwable> A orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return data.orElseThrow(exceptionSupplier);
    }
}
