public class LinkedList<T> implements List<T> {

    Node<T> head;
    int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    public Iterator<T> iterator() {
        return new LinkedIterator<>();
    }

    private static class Node<T> {
        T data;
        Node<T> next;
        public Node(T value) {
            data = value;
            next = null;
        }
    }

    @Override
    public void add(int pos, T value) throws Exception {
        if (pos < 0 || pos > size) {
            throw new Exception("List Index Out of Bounds");
        }
        if (pos == 0) {
            Node<T> node = new Node<>(value);
            node.next = head;
            head = node;
            ++size;
        }else {
            Node<T> prev = head;
            for (int i = 0; i < pos - 1; i++) {
                prev = prev.next;
            }
            Node<T> newNode = new Node<>(value);
            newNode.next = prev.next;
            prev.next = newNode;
            ++size;
        }
    }

    @Override
    public void add(T value) {
        if (head == null) {
            head = new Node<>(value);
        }else {
            Node<T> node = head;
            for (int i = 0; i < size - 1; i++) {
                node = node.next;
            }
            node.next = new Node<>(value);
        }
        ++size;
    }

    @Override
    public T get(int pos) throws Exception {
        if (pos < 0 || pos >= size) {
            throw new Exception("List Index Out of Bounds");
        }
        Node<T> curr = head;
        for (int i = 0; i < pos; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    @Override
    public T remove(int pos) throws Exception {
        if (pos < 0 || pos > size) {
            throw new Exception("List Index Out of Bounds");
        }
        if (pos == 0) {
            Node<T> node = head;
            head = head.next;
            --size;
            return node.data;
        }
        Node<T> prev = head;
        for (int i = 0; i < pos - 1; i++) {
            prev = prev.next;
        }
        Node<T> node = prev.next;
        prev.next = node.next;
        --size;
        return node.data;
    }

    @Override
    public int size() {
        return size;
    }

    private class LinkedIterator<E> implements Iterator<E> {

        private Node<E> node;

        @SuppressWarnings("unchecked")
        public LinkedIterator() {
            node = (Node<E>) head;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            Node<E> prev = node;
            node = node.next;
            return prev.data;
        }
    }
}
