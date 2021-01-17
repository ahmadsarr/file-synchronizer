package filesync;

import filesync.filesystem.Dir;
import filesync.filesystem.File;
import filesync.filesystem.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public final class Utils {
    public final static void walk(Node root){
        Stack<Node> dirs=new Stack<>();
        dirs.push(root);
        Node current=null;
        while (!dirs.isEmpty())
        {
            current=dirs.pop();
            try {
                List<Path> paths= Files.walk(current.getPath(),1).collect(Collectors.toList());
                Path  p=null;
                for(int i=1;i<paths.toArray().length;i++){
                    p=paths.get(i);
                    Node node=null;
                    if(Files.isDirectory(p)){
                        node=new Dir(current,p.toString());
                        dirs.push(node);
                    }else{
                        node=new File(current,p.toString());
                    }
                    current.addchild(node);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(root.getChildren().size());
    }
}
