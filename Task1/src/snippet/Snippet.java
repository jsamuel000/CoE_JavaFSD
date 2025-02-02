package snippet;

import java.util.*;

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}

public class Snippet {

	// Serializes a binary tree to a string.
	public String serialize(TreeNode root) {
		StringBuilder sb = new StringBuilder();
		serializeHelper(root, sb);
		return sb.toString();
	}

	private void serializeHelper(TreeNode node, StringBuilder sb) {
		if (node == null) {
			sb.append("null,");
			return;
		}
		sb.append(node.val).append(",");
		serializeHelper(node.left, sb);
		serializeHelper(node.right, sb);
	}

	// Deserializes a string back to a binary tree.
	public TreeNode deserialize(String data) {
		Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
		return deserializeHelper(nodes);
	}

	private TreeNode deserializeHelper(Queue<String> nodes) {
		String val = nodes.poll();
		if (val.equals("null")) {
			return null;
		}

		TreeNode node = new TreeNode(Integer.parseInt(val));
		node.left = deserializeHelper(nodes);
		node.right = deserializeHelper(nodes);
		return node;
	}

	public static void main(String[] args) {
		Snippet codec = new Snippet();

		TreeNode root = new TreeNode(1);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		root.right.left = new TreeNode(4);
		root.right.right = new TreeNode(5);

		String serialized = codec.serialize(root);
		System.out.println("Serialized: " + serialized);

		TreeNode deserialized = codec.deserialize(serialized);
		System.out.println("Deserialized Root Value: " + deserialized.val);
	}
}
