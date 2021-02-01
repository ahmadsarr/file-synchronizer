package filesync.filesystem;

import filesync.Utils;
import filesync.action.BaseAction;
import filesync.action.RenameAction;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Node {
    protected Path path;
    protected java.lang.String name;
    protected Node parent;
    protected boolean isDirty = false;
    protected java.lang.String base;

    public BasicFileAttributes getAtrributes() {
        return atrributes;
    }

    protected BasicFileAttributes atrributes;
    protected LinkedList<BaseAction> actions;

    public Node(Node parent, java.lang.String name, java.lang.String base) {
        this.base = base;
        this.name = name;
        this.parent = parent;
        this.actions = new LinkedList<>();
        this.path = Paths.get(getPathStr());
        if (!Files.exists(this.path)) {
            java.io.File file = this.path.toFile();
            file.mkdirs();
        }
        try {
            this.atrributes = Files.readAttributes(this.path, BasicFileAttributes.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Node child(java.lang.String path) {
        List<Node> chdren = getChildren();
        for (Node n : chdren)
            if (n.getPathStr().equals(path))
                return n;
        return null;
    }

    public abstract void removeChild(Node node);

    public Node find(String p) {
        p = p.startsWith("/") ? p.substring(1) : p;
        //LinkedList<Node> stacks = new LinkedList<>();
        //stacks.add(this)
        boolean b=true;
        Node node=this;
        while (b) {

            String file = node.getPath().getFileName().toString();
            if (file.equals(p))
                return node;
            p = p.startsWith("/") ? p.substring(1) : p;
            b=false;
            if (node.getChildren().size() > 0 ) {
                p = p.substring(file.length());
                p = p.startsWith("/") ? p.substring(1) : p;
                for (Node c : node.getChildren())
                    if(p.startsWith(c.getPath().getFileName().toString()))
                    {
                        node=c;
                        b=true;
                    }
            }
        }
        return null;
    }

    public Node child(int inode) {
        List<Node> chdren = getChildren();
        for (Node n : chdren) {
            java.lang.String k = n.getAtrributes().fileKey().toString();
            int key = Integer.parseInt(k.substring(k.indexOf("ino=") + 4, k.lastIndexOf(")")));
            if (key == inode)
                return n;
        }
        return null;
    }

    public void notif(List<BaseAction> actions) {
        if (parent != null) {
            actions.add(new BaseAction(getPathStr().substring(getBase().length()), "DIR_UPDATE"));
            isDirty = true;
            parent.notif(actions);
        } else {
            this.actions.addAll(actions);
            this.actions.add(new BaseAction(getPathStr().substring(getBase().length()), "DIR_UPDATE"));
            this.actions.stream().forEach(baseAction -> System.out.println(baseAction));
        }

    }

    public void notif(BaseAction a) {

        if (parent != null) {
            ArrayList<BaseAction> list = new ArrayList();
            list.add(a);
            parent.notif(list);
            isDirty = true;
        } else {
            isDirty = true;
            actions.add(a);
            this.actions.add(new BaseAction(getPathStr().substring(getBase().length()), "DIR_UPDATE"));
        }

    }

    public abstract boolean isFile();

    public abstract boolean isDir();

    public void rename(java.lang.String name) throws IOException {

        java.io.File file = Paths.get(parent.getPathStr() + "/" + name).toFile();
        if(isDir())
            file.mkdirs();
        else
           file.createNewFile();
        this.path.toFile().renameTo(file);
        this.name = parent.getPathStr() + "/" + name;
        this.path = Paths.get(getPathStr());
        this.atrributes = Files.readAttributes(this.path, BasicFileAttributes.class);

    }

    public void remove() {

        parent.removeChild(this);
        try {
            if(isDir())
            {
                List<Path> files= Utils.walk(getPath());
                files=files==null?null:files.stream().sorted(Comparator.comparing(Path::toString).reversed()).collect(Collectors.toList());
                for(Path f:files) {

                    Files.delete(f);
                }
            }else
               Files.delete(this.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createFile(String name) throws IOException {
            java.io.File file = Paths.get(getPathStr()+ "/" + name).toFile();
            Node node=null;
            if(name.indexOf(".")<0) {
                file.mkdirs();
                node = new Dir(this, file.getPath(), this.base);
            }else {
                file.createNewFile();
                node = new File(this, file.getPath(), this.base);
            }
            addchild(node);

    }

    public void replace(java.io.File source) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader((source)));
        PrintWriter writer = new PrintWriter(path.toFile());
        String line;
        while ((line = reader.readLine()) != null)
            writer.println(line);
        reader.close();
        writer.close();
        this.path = Paths.get(getPathStr());
        this.atrributes = Files.readAttributes(this.path, BasicFileAttributes.class);

    }

    public abstract void update(java.lang.String txt);

    public List<Node> getChildren() {
        return new ArrayList<Node>();
    }

    public void computeDirty() throws Exception {

        Path p = getPath();

        if (!Files.exists(p)) {

            parent.removeChild(this);
            if (isDir())

            notif(new BaseAction(getPath().toString().substring(getBase().length()), "DELETED"));
            return;
        }
        BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
        if (!attr.lastModifiedTime().equals(this.atrributes.lastModifiedTime())) {

            if (isFile()) {
                notif(new BaseAction(getPathStr().substring(getBase().length()), "UPDATE"));
            } else {
                List<Path> l = Files.walk(path, 1).collect(Collectors.toList());
                if (l.size() > getChildren().size()) //child add to that dir
                {
                    for (int i = 1; i < l.size(); i++) {
                        //BasicFileAttributes att = Files.readAttributes(l.get(i), BasicFileAttributes.class);
                        //java.lang.String k = att.fileKey().toString();
                        //int key = Integer.parseInt(k.substring(k.indexOf("ino=") + 4, k.lastIndexOf(")")));
                       // Node n = child(key);
                        Node n=child(l.get(i).toString());
                        if (n == null) {//le fichier ajout
                            n = (Files.isDirectory(l.get(i)) ? new Dir(this, l.get(i).toString(), base ,true) :
                                    new File(this, l.get(i).toString(), getBase()));
                            addchild(n);
                            n.computeDirty();
                            n.notif(new BaseAction(n.getPathStr().substring(getBase().length()), "ADD"));
                        }
                    }
                }

            }
            this.atrributes = attr;
        }

    }

    public java.lang.String getPathStr() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    public void addchild(Node e) {
    }

    public void removesAll() {
    }


    public java.lang.String getBase() {
        return base;
    }

    public List<BaseAction> getActions() {
        ArrayList<BaseAction> actions = new ArrayList<>();
        while (!this.actions.isEmpty())
            actions.add(this.actions.removeFirst());
        return actions;
    }


    public void setPath(Path path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setAtrributes(BasicFileAttributes atrributes) {
        this.atrributes = atrributes;
    }

    public void setActions(LinkedList<BaseAction> actions) {
        this.actions = actions;
    }

    public int getInod() {
        BasicFileAttributes att = this.atrributes;
        java.lang.String k = att.fileKey().toString();
        return Integer.parseInt(k.substring(k.indexOf("ino=") + 4, k.lastIndexOf(")")));
    }
}
