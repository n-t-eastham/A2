public interface List<T> {
    void add(int pos, T value) throws Exception;
    void add(T value);
    T get(int pos) throws Exception;
    T remove(int pos) throws Exception;
    int size();
    Iterator<T> iterator();
}
