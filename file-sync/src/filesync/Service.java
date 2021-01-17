package filesync;

import filesync.action.Action;
import filesync.filesystem.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Service  implements Runnable{
    protected Synchronizer synchronizer;
    protected Node root;
    public Service(Node root,Synchronizer sync){
        this.root=root;
        this.synchronizer=sync;
        Utils.walk(this.root);

    }

    @Override
    public void run() {
        while (true){
            List<Action> actions=new ArrayList<>();
            try {
            Stack<Node>piles=new Stack<>();
            piles.push(this.root);
            while (!piles.isEmpty()){
                Node cur=piles.pop();
                if(cur.isdity())
                  //   this.synchronizer.notifies(cur.getActions());
                    actions.addAll(cur.getActions());
                for(Node n:cur.getChildren())
                    piles.push(n);
               // System.out.println(cur.getPathStr());
            }
            if(!actions.isEmpty()) {
                this.synchronizer.notifies();
                this.root.getActions().addAll(actions);
            }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
