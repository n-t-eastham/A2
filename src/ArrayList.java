public class ArrayList<T> implements List<T> {

    T[] arr;
    int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        size = 0;
        arr = (T[]) new Object[10];
    }

    @SuppressWarnings("unchecked")
    private void growArray() {
        T[] newArray = (T[]) new Object[arr.length * 2];

        for (int i = 0; i < arr.length; i++) {
            newArray[i] = arr[i];
        }
        arr = newArray;
    }

    @Override
    public void add(int pos, T value) throws Exception {
        if (pos < 0 || pos > size - 1) {
            throw new Exception("List Index Out of Bounds");
        }
        if (size == arr.length) {
            growArray();
        }

        for (int i = size; i > pos; i--) {
            arr[i] = arr[i-1];
        }
        arr[pos] = value;
        ++size;
    }

    @Override
    public void add(T value) {
        if (size == arr.length) {
            growArray();
        }
        arr[size++] = value;
    }

    @Override
    public T get(int pos) throws Exception {
        if (pos < 0 || pos >= size) {
            throw new Exception("List Index Out of Bounds");
        }
        return arr[pos];
    }

    @Override
    public T remove(int pos) throws Exception {
        if (pos < 0 || pos >= size) {
            throw new Exception("List Index Out of Bounds");
        }
        T obj = arr[pos];

        for (int i = pos; i < size; i++) {
            arr[i] = arr[i + 1];
        }
        --size;
        return obj;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator<>();
    }

    public class ArrayListIterator<T> implements Iterator<T> {

        private int index;

        public ArrayListIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            if (index < size)
                return true;
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            T value = (T) arr[index];
            ++index;
            return value;
        }
    }
}
