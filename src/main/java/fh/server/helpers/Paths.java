package fh.server.helpers;

import fh.server.entity.Entity;
import fh.server.entity.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Paths {

    public static String resolve(String path, String name) {
        return (path != null? path + "." : "")+ name;
    }

    public static String resolve(Scope parent, String name) {
        return (parent != null? parent.getId() + "." : "")+ name;
    }

    public static void requireValidName(Scope parent, String name) {
        if (name == null || name.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name is mandatory");
        if (name.contains("."))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name contains separator");
        for (char c : CharPool.unsafe()) if (name.contains(""+c))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name contains unsafe characters");
        String prefix = parent == null? "" : parent.getId() + ".";
        if (prefix.length() + name.length() > Entity.ID_LENGTH)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name too long");
    }

}
