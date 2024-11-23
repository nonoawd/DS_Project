class BSTNode<T> {
public String key;
public T data;
public BSTNode<T> left , right;

public BSTNode (String k , T d) {
key = k;
data = d;
left = right = null;
}

}