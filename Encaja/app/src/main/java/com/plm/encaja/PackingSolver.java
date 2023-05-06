package com.plm.encaja;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;

public class PackingSolver {
    private static class Node {
        double x, y, width, height;
        Node left, right;

        Node(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<ItemRectangle> packRectangles(ArrayList<ItemRectangle> rectanglesToPack, double widthTotal, double heightTotal) throws NoSpaceException{
        // Ordenar los rectángulos por altura decreciente
        rectanglesToPack.sort(Comparator.comparingDouble((ItemRectangle r) -> r.getDimension().getHeight()).reversed());

        Node rootNode = new Node(0, 0, widthTotal, heightTotal);

        for (ItemRectangle rectangle : rectanglesToPack) {
            double rectWidth = rectangle.getDimension().getWidth();
            double rectHeight = rectangle.getDimension().getHeight();
            Node node = findNode(rootNode, rectWidth, rectHeight);

            if (node != null) {
                Node newNode = splitNode(node, rectWidth, rectHeight);
                rectangle.setLocation(new Location(newNode.x, newNode.y));
            } else {
                throw new NoSpaceException(rectangle);
            }
        }

        return rectanglesToPack;
    }

    private static Node findNode(Node node, double width, double height) {
        if (node.left != null || node.right != null) {
            Node leftNode = findNode(node.left, width, height);
            if (leftNode != null) {
                return leftNode;
            } else {
                return findNode(node.right, width, height);
            }
        } else {
            if (node.width >= width && node.height >= height) {
                return node;
            } else {
                return null;
            }
        }
    }

    private static Node splitNode(Node node, double width, double height) {
        node.left = new Node(node.x, node.y + height, node.width, node.height - height);
        node.right = new Node(node.x + width, node.y, node.width - width, height);
        node.width = width;
        node.height = height;
        return node;
    }
}
