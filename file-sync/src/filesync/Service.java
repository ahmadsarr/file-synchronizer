package filesync;

import filesync.filesystem.Node;

import java.util.Stack;

public class Service  implements Runnable{

    protected Node root;
 //   protected LinkedList<BaseAction> dirties;
    public Service(Node root){
        this.root=root;
    //    this.dirties = dirties;
        Utils.walk(this.root);

    }

    @Override
    public void run() {
        while (true){

            try {
                Thread.sleep(6000);
            Stack<Node>piles=new Stack<>();
            piles.push(this.root);
            while (!piles.isEmpty()){
                Node cur=piles.pop();
                 cur.computeDirty();
                for(Node n:cur.getChildren())
                    piles.push(n);
            }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
