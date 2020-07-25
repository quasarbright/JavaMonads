import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * List decorator that adds monadic functionality
 * @param <A> element type
 */
public class ListMonad<A> implements Monad<A>, List<A> {
    private final List<A> data;

    public ListMonad(List<A> data) {
        this.data = data;
    }

    public static <A> ListMonad<A> of(A... as) {
        ListMonad<A> ans = new ListMonad<>(new ArrayList<>(Arrays.asList(as)));
        return ans;
    }

    @Override
    public ListMonad<A> ret(A a) {
        return new ListMonad<>(List.of(a));
    }

    @Override
    public <B> ListMonad<B> bind(Function<A, Monad<B>> f) {
        List<B> ans = new ArrayList<>();
        // maybe could have used flatmap, but this is fine
        for(A a: this.data) {
            Monad<B> mb = f.apply(a);
            if(mb instanceof ListMonad<?>) {
                ListMonad<B> bs = (ListMonad<B>) mb;
                ans.addAll(bs.data);
            }
        }
        return new ListMonad<>(ans);
    }

    @Override
    public <B> ListMonad<B> sequence(Monad<B> mb) {
        return this.bind(ignored -> mb);
    }

    @Override
    public ListMonad<A> pure(A a) {
        return new ListMonad<>(List.of(a));
    }

    @Override
    public <B> ListMonad<B> getAppliedBy(Applicative<Function<A, B>> af) {
        if(af instanceof ListMonad<?>) {
            ListMonad<Function<A, B>> fs = (ListMonad<Function<A, B>>) af;
            return
                    fs.bind(f ->
                    this.bind(a ->
                    ListMonad.of(f.apply(a))));
        } else {
            throw new IllegalArgumentException("af must be a ListMonad");
        }
    }

    @Override
    public <B> ListMonad<B> fmap(Function<A, B> f) {
        return new ListMonad<>(
                this.stream()
                    .map(f)
                    .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        return new ArrayList<>(this.data).toString();
    }

    ///////// list delegators

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean contains(Object o) {
        return data.contains(o);
    }

    public Iterator<A> iterator() {
        return data.iterator();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return data.toArray(ts);
    }

    public boolean add(A a) {
        return data.add(a);
    }

    public boolean remove(Object o) {
        return data.remove(o);
    }

    public boolean containsAll(Collection<?> collection) {
        return data.containsAll(collection);
    }

    public boolean addAll(Collection<? extends A> collection) {
        return data.addAll(collection);
    }

    public boolean addAll(int i, Collection<? extends A> collection) {
        return data.addAll(i, collection);
    }

    public boolean removeAll(Collection<?> collection) {
        return data.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return data.retainAll(collection);
    }

    public void replaceAll(UnaryOperator<A> operator) {
        data.replaceAll(operator);
    }

    public void sort(Comparator<? super A> c) {
        data.sort(c);
    }

    public void clear() {
        data.clear();
    }

    public A get(int i) {
        return data.get(i);
    }

    public A set(int i, A a) {
        return data.set(i, a);
    }

    public void add(int i, A a) {
        data.add(i, a);
    }

    public A remove(int i) {
        return data.remove(i);
    }

    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    public ListIterator<A> listIterator() {
        return data.listIterator();
    }

    public ListIterator<A> listIterator(int i) {
        return data.listIterator(i);
    }

    public List<A> subList(int i, int i1) {
        return data.subList(i, i1);
    }

    public Spliterator<A> spliterator() {
        return data.spliterator();
    }

    public static <E> List<E> copyOf(Collection<? extends E> coll) {
        return List.copyOf(coll);
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return data.toArray(generator);
    }

    public boolean removeIf(Predicate<? super A> filter) {
        return data.removeIf(filter);
    }

    public Stream<A> stream() {
        return data.stream();
    }

    public Stream<A> parallelStream() {
        return data.parallelStream();
    }

    public void forEach(Consumer<? super A> action) {
        data.forEach(action);
    }
}
