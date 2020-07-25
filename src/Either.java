import java.util.Optional;
import java.util.function.Function;

public class Either<Left, Right> implements Monad<Right> {

    private final Optional<Left> left;
    private final Optional<Right> right;

    private Either(Left left, Right right) {
        if((left == null) == (right == null)) {
            throw new IllegalArgumentException();
        }
        this.left = Optional.ofNullable(left);
        this.right = Optional.ofNullable(right);
    }

    public Either(Optional<Left> left, Optional<Right> right) {
        this.left = left;
        this.right = right;
    }

    public static <Left, Right> Either<Left, Right> asLeft(Left left) {
        if(left == null) {
            throw new IllegalArgumentException("left cannot be null");
        }
        return new Either<>(left, null);
    }

    public static <Left, Right> Either<Left, Right> asRight(Right right) {
        if(right == null) {
            throw new IllegalArgumentException("right cannot be null");
        }
        return new Either<>(null, right);
    }

    public boolean isRight() {
        return this.right.isPresent();
    }

    public boolean isLeft() {
        return this.left.isPresent();
    }


    @Override
    public Either<Left, Right> ret(Right right) {
        return asRight(right);
    }

    @Override
    public <B> Either<Left, B> bind(Function<Right, Monad<B>> f) {
        if(this.left.isPresent()) {
            return asLeft(this.left.get());
        } else {
            if(this.right.isEmpty()) {
                throw new IllegalStateException();
            }
            Right r = this.right.get();
            Monad<B> mb = f.apply(r);
            try {
                return (Either<Left, B>) mb;
            } catch(ClassCastException e) {
                throw new IllegalArgumentException("function didn't return an Either<Left, B>");
            }
        }
    }

    @Override
    public <B> Either<Left, B> sequence(Monad<B> mb) {
        return this.bind(ignored -> mb);
    }

    @Override
    public Either<Left, Right> pure(Right right) {
        return asRight(right);
    }

    @Override
    public <B> Either<Left, B> getAppliedBy(Applicative<Function<Right, B>> af) {
        try{
            Either<Left, Function<Right, B>> ef = (Either<Left, Function<Right, B>>) af;
            return ef.bind(f ->
            this.bind(right ->
            asRight(f.apply(right))));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("af must be Either<Left, Right -> B>");
        }
    }

    @Override
    public <B> Either<Left, B> fmap(Function<Right, B> f) {
        return new Either<>(this.left, this.right.map(f));
    }

    @Override
    public String toString() {
        if(this.left.isPresent()) {
            return "Left "+this.left.get();
        } else if(this.right.isPresent()) {
            return "Right "+this.right.get();
        } else {
            throw new IllegalStateException();
        }
    }
}
