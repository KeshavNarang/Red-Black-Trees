@SuppressWarnings("unchecked")
public class RedBlackTree <Key extends Comparable <Key>, Value>
{
	private Node root;
	
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private class Node
	{
		Key key;
		Value value;
		
		Node left;
		Node right;
		
		int numberChildren; 
		boolean color;
		
		Node(Key key, Value value, int numberChildren, boolean color)
		{
			this.key = key;
			this.value = value;
			this.numberChildren = numberChildren;
			this.color = color;
		}
	}
	
	private boolean isRed(Node find)
	{
		if (find == null)
		{
			return false;
		}
		return find.color == RED;
	}
	
	private Node rotateLeft (Node node)
	{
		 Node after = node.right;
		 
		 node.right = after.left;
		 after.left = node;
		 
		 after.color = node.color;
		 node.color = RED;
		 
		 after.numberChildren = node.numberChildren;
		 
		 node.numberChildren = 1 + size(node.left) + size(node.right);
		 
		 return after;
	}
	
	private Node rotateRight(Node node)
	{
		Node before = node.left;
		
		node.left = before.right;
		before.right = node;
		
		before.color = node.color;
		node.color = RED;
		
		before.numberChildren = node.numberChildren;
		node.numberChildren = 1 + size(node.left) + size(node.right);
		
		return before;
	}
	
	private Node moveRedLeft(Node node)
	{ 
		flipColors(node);
		if (isRed(node.right.left))
		{
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
		}
		return node;
	}
	
	private Node moveRedRight(Node node)
	{
		flipColors(node);
		if (!isRed(node.left.left))
		{
			node = rotateRight(node);
		}
		return node;
	}
	
	private void flipColors(Node node)
	{
		node.color = RED;
		node.left.color = BLACK;
		node.right.color = BLACK;
	}
	
	public boolean isEmpty ()
	{
		return size() == 0;
	}
	
	private int size()
	{
		return size(root);
	}
	
	private int size(Node find)
	{
		if (find == null)
		{
			return 0;
		}
		else
		{
			return find.numberChildren;
		}
	}
	
	public Value get(Key key)
	{ 
		return get(root, key); 
	}
	
	private Value get(Node node, Key key)
	{ 
		 if (node == null)
		 {
			 return null;
		 }
		 int cmp = key.compareTo(node.key);
		 if (cmp < 0)
		 {
			 return get(node.left, key);
		 }
		 else if (cmp > 0)
		 {
			 return get(node.right, key);
		 }
		 else 
		 {
			 return node.value;
		 }
	}
	
	public void put (Key key, Value value)
	{ 
		root = put(root, key, value);
		root.color = BLACK;
	}
	
	private Node put (Node node, Key key, Value value)
	{
		if (node == null)
		{
			return new Node(key, value, 1, RED);
		}
		
		int cmp = key.compareTo(node.key);
		
		if (cmp < 0)
		{
			node.left = put(node.left, key, value);
		}
		
		else if (cmp > 0)
		{
			node.right = put(node.right, key, value);
		}
		
		else
		{
			node.value = value;
			if (value == null)
			{
				return node;
			}
		}
		
		if (isRed(node.right) && !isRed(node.left))
		{
			node = rotateLeft(node);
		}
		
		if (isRed(node.left) && isRed(node.left.left))
		{
			node = rotateRight(node);
		}
		
		if (isRed(node.left) && isRed(node.right))
		{
			flipColors(node);
		}
		
		node.numberChildren = size(node.left) + size(node.right) + 1;
		
		return node;
	}
	
	public void delete(Key key)
	{
		if (!isRed(root.left) && !isRed(root.right))
		{
			root.color = RED;
		}
		root = delete(root, key);
		
		if (!isEmpty())
		{
			root.color = BLACK;
		}
	}
	
	private Node delete(Node node, Key key)
	{
		if (key.compareTo(node.key) < 0)
		{
			if (!isRed(node.left) && !isRed(node.left.left))
			{
				node = moveRedLeft(node);
			}
			node.left = delete(node.left, key);
		}
		
		else
		{
			if (isRed(node.left))
			{
				node = rotateRight(node);
			}
			if (key.compareTo(node.key) == 0 && (node.right == null))
			{
				return null;
			}
			if (!isRed(node.right) && !isRed(node.right.left))
			{
				node = moveRedRight(node);
			}
			if (key.compareTo(node.key) == 0)
			{
				node.value = get(node.right, min(node.right).key);
				node.key = min(node.right).key;
				node.right = deleteMin(node.right);
			}
			else
			{
				node.right = delete(node.right, key);
			}
		}
		if (isRed(node.right))
		{
			node = rotateLeft(node);
		}
		return node;
	}
	
	public Key min()
	{
		return min(root).key;
	}
	
	private Node min(Node node)
	{
		 if ((node.left == null) || (node.left.value == null))
		 {
			 if ((node.left != null) && (node.right.value != null))
			 {
				 return min(node.right);
			 }
			 if (node.value != null)
			 {
				return node;
			 }
		 }
		 return min(node.left);
	}
	
	public void deleteMin()
	{
		Node min = min(root);
		min = put(min, min.key, null);
		if (min.value != null)
		{
			if (!isRed(root.left) && !isRed(root.right))
			{
				root.color = RED;
			}
			root = deleteMin(root);		
			if (!isEmpty())
			{
				root.color = BLACK;
			}
		}
	}
	
	private Node deleteMin(Node node)
	{
		if (node.left == null)
		{
			return null;
		}
		if (!isRed(node.left) && !isRed(node.left.left))
		{
			node = moveRedLeft(node);
		}
		node.left = deleteMin(node.left);
		
		if (isRed(node.right))
		{
			node = rotateLeft(node);
		}
		return node;
	}	
	
	private void print ()
	{
		print(root);
	}
	
	private void print(Node node)
	{
		if ((node == null) || (node.value == null))
		{
		 return;
		}
		print(node.left);
		System.out.println(node.key);
		print(node.right);
	}
	
	public static void main (String [] args)
	{
		RedBlackTree tree = new RedBlackTree ();
		
		System.out.println("Adding keys 1 - 60: \n");
		
		for (int i = 1; i <= 60; i++)
		{
			tree.put(i, i);
		}
		
		tree.print();
		System.out.println("\nDeleting keys 1 - 15: \n");
		

		for (int i = 1; i <= 15; i++)
		{
			tree.deleteMin();
		}
		tree.print();
	}
}