package environment.component.cache;

import java.io.*;
import java.nio.file.Files;


public abstract class Cache implements Serializable {

    private File resource;

    private File directory;

    private Object current;


    public Cache(String res, String path) throws Exception {
        if (path == null || res == null) {
            throw new Exception("Invalid parameters provided to cache");
        }

        if (path.indexOf('/') != 0) {
            path = '/' + path;
        }

        if (res.indexOf('/') != 0) {
            res = '/' + res;
        }

        this.directory = new File(System.getProperty("user.dir") + path);
        this.resource = new File(System.getProperty("user.dir") + res);

        if (!this.directory.exists()) {
            Files.createDirectories(this.directory.toPath());
        }

        if (!this.resource.exists()) {
            Files.createFile(this.resource.toPath());
        }
    }

    final public Cache write(Object object) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(resource)
            );

            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            return this;
        }

        return this;
    }

    final public Object fetch() {
        if (this.current != null) {
            return this.current;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(resource)
            );

            current = in.readObject();
            in.close();

            return this.current;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
