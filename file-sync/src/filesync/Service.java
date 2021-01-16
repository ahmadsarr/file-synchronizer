package filesync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Service  implements Runnable{
    protected Synchronizer synchronizer;
    protected  Node root;
    public Service(Node root,Synchronizer sync){
        this.root=root;
        this.synchronizer=sync;
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
    }
    @Override
    public void run() {
        while (true){
            try {
            Stack<Node>piles=new Stack<>();
            piles.push(this.root);
            while (!piles.isEmpty()){
                Node cur=piles.pop();
                if(cur.isDir() &&cur.isdity())
                     this.synchronizer.notifies(cur.getActions());
                for(Node n:cur.getChildren())
                    piles.push(n);
            }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
