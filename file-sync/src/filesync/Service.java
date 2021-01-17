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
        Utils.walk(this.root);

    }

    @Override
    public void run() {
        while (true){
            try {
            Stack<Node>piles=new Stack<>();
            piles.push(this.root);
            while (!piles.isEmpty()){
                Node cur=piles.pop();
                if(cur.isdity())
                     this.synchronizer.notifies(cur.getActions());
                for(Node n:cur.getChildren())
                    piles.push(n);
               // System.out.println(cur.getPathStr());
            }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
