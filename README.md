# Beautiful binary tree drawing
__This piece of code helps you to draw the binary tree beautifully.__

[DrawFactory](https://github.com/tungndtt/beautiful_binary_tree_drawing/tree/main/src/processing/DrawFactory.java) provides the drawer, which displays your tree in following formats :

## Tree Format
>By set Mode to DrawMode.TREE

- Displaying the binary tree in tree-format. The logic would take into account the length of your value in the tree node.

  Besides, you can also modify **the distance between 2 stages** as well as **the minimal indent-distance between 2 tree nodes**.

- Here is [the demo](https://github.com/tungndtt/beautiful_binary_tree_drawing/tree_demo.PNG) 

## Directory Format
>By set Mode to DrawMode.DIRECTORY

- Displaying the binary tree in directory-format.

  You can modyfy **the distance between 2 stages** as well as **the horizontal indent-distance**

- Here is [the demo](https://github.com/tungndtt/beautiful_binary_tree_drawing/directory_demo.PNG) 

## Hierarchy Format
>By set Mode to DrawMode.HIERARCHY

- Displaying the binary tree in hierarchy-format
  
  You can modify **minimal indent-distance between tree nodes.**

- Here is [the demo](https://github.com/tungndtt/beautiful_binary_tree_drawing/hierarchy_demo.PNG) 

___

## Parsing tree from file
In addition: If you are tired of constructing the tree node-instances by hand. You can provide your tree as following rules to get the tree

- Line format:  &nbsp;  __{position} , {node's value}__
  with position := 
  - t (root)
  - r (right)
  - l (left) 
  
  You can look into this [Example](https://github.com/tungndtt/beautiful_binary_tree_drawing/my_tree.txt)
- Just read text file (.txt-format)
- One node information per line